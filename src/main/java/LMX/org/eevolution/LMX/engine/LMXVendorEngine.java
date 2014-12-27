/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2003-2014 e-Evolution Consultants All Rights Reserved.       *
 * This program is free software; you can redistribute it and/or              *
 * modify it under the terms of the GNU General Public License                *
 * as published by the Free Software Foundation; either version 2             *
 * of the License, or (at your option) any later version.                     *
 * This program is distributed in the hope that it will be useful,            *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                       *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *                                                                            *
 * @author victor.perez@e-evolution.com, www.e-evolution.com                  *
 *****************************************************************************/

package org.eevolution.LMX.engine;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.CLogger;
import org.eevolution.LMX.model.MLMXVendor;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created by e-Evolution on 26/12/13.
 */
public final class LMXVendorEngine {


    /** Logger */
    protected CLogger log = CLogger.getCLogger(getClass());

    private static LMXVendorEngine s_instance = null;

    /** Vendor Service Implementation (negative cache) */
    private static TreeSet<String> s_VendorImplement = new TreeSet<String>();
    /** Vendor Service */
    private HashMap<String, LMXVendorInterface> m_VendorService = new HashMap<String, LMXVendorInterface>();

    private LMXVendorEngine() {
        registerLMXVendor(true);
    }

    /**
     * WMRule factory instance
     *
     * @return document factory instance
     */
    public LMXVendorInterface getLMXVendorFactory(String className) {
        if (m_VendorService.containsKey(className)) {
            return (LMXVendorInterface) m_VendorService.get(className);
        }

        try {
            Class<? extends LMXVendorInterface> cl = getClass(className);
            //
            // Check <constructor>():
            Constructor<?> constructor = null;
            try {
                constructor = cl.getDeclaredConstructor();
            } catch (Exception e) {
                log.fine("Not found LMX Vendor");
            }
            if (constructor != null) {
                LMXVendorInterface vendorService = (LMXVendorInterface) constructor
                        .newInstance();
                m_VendorService.put(className, vendorService);
                return vendorService;
            }
            //
            // Check <constructor>():
            constructor = cl.getDeclaredConstructor();
            LMXVendorInterface service = (LMXVendorInterface) constructor.newInstance();
            m_VendorService.put(className, service);
            return service;
        } catch (ClassNotFoundException e) {
            s_VendorImplement.add(className); // Add to negative cache (won't
            // try to load again)
        } catch (Throwable e) {
            throw new AdempiereException(e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected <T extends LMXVendorInterface> Class<T> getClass(String className)
            throws ClassNotFoundException {
        return (Class<T>) Class.forName(className);
    }


    /*protected String getClassName(MLMXVendorService service) {

            return service.getClass().getSimpleName();
    }*/



    /**
     * register the WM Rule implementation
     *
     * @param reset
     */
    protected void registerLMXVendor(boolean reset) {
        if (reset) {
            m_VendorService.clear();
        }

        for (MLMXVendor vendor : MLMXVendor.getVendors())
        {
            getLMXVendorFactory(vendor.getClassname());
        }
    }

    /**
     * get WMRule Engine like singleton instance
     *
     * @return WM Rule Engine
     */
    public static LMXVendorEngine get() {
        if (s_instance == null) {
            s_instance = new LMXVendorEngine();
        }
        return s_instance;
    }
}
