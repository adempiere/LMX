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

package org.eevolution.LMX.engine.vendor;


import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInvoice;
import org.compiere.util.Env;
import org.eevolution.LMX.engine.LMXVendorInterface;
import org.eevolution.LMX.engine.LMXVendorServiceInterface;
import org.eevolution.LMX.model.MLMXVendor;
import org.eevolution.LMX.util.WebServiceConnector;

import  javax.xml.transform.Source;

/**
 * Created by e-Evolution on 26/12/13.
 */
public class LMXEcodexService implements LMXVendorInterface {

    public MLMXVendor m_vendor;

    public LMXEcodexService()
    {
        m_vendor = MLMXVendor.getByClassName(getClass().getCanonicalName());
        if (m_vendor == null)
            throw new AdempiereException("Implementación para este proveedor de servicios no existe: "+ getClass().getCanonicalName());
    }

    public Source execute(MInvoice invoice  , String SOAPType)  throws Exception {
    {
            LMXVendorServiceInterface service = m_vendor.getService(SOAPType);
            final WebServiceConnector wsc = new WebServiceConnector();
            String request = Env.parseVariable(service.getSOAPRequest(), invoice, invoice.get_TrxName(), false);
            System.out.println("Llamada del servicio :" + request);
            wsc.setRequest(request);
            wsc.setBinding(service.getSOAPBinding());
            wsc.setEndpointAddress(service.getSOAPEndpointAddress());
            wsc.setServiceName(service.getSOAPServiceName());
            wsc.setPortName(service.getSOAPPort());
            wsc.setTargetNS(service.getSOAPTargetNS());
            wsc.setSoapAction(service.getSOAPAction());
            wsc.executeConnector();
            final Source response = wsc.getResponse();
            return response;
        }

    }
}
