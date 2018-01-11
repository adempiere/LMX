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

package org.eevolution.LMX.model;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

/**
 * Created by e-Evolution on 26/12/13.
 */
public class MLMXVendor extends X_LMX_Vendor {

    private List<MLMXVendorService> services;

    public static MLMXVendor getByClassName (String className)
    {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append(I_LMX_Vendor.COLUMNNAME_AD_Client_ID).append("=? AND ");
        whereClause.append(I_LMX_Vendor.COLUMNNAME_Classname).append("=?");

        return new Query(Env.getCtx(), I_LMX_Vendor.Table_Name, whereClause.toString(), null)
                .setParameters(Env.getAD_Client_ID(Env.getCtx()), className)
                .first();
    }


    public static List<MLMXVendor> getVendors ()
    {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append(I_LMX_Vendor.COLUMNNAME_AD_Client_ID).append("=?");
        return new Query(Env.getCtx(), I_LMX_Vendor.Table_Name, whereClause.toString(), null)
                .setParameters(Env.getAD_Client_ID(Env.getCtx()))
                .list();
    }


    public MLMXVendor(Properties ctx, int LMX_Vendor_ID, String trxName) {
        super(ctx, LMX_Vendor_ID, trxName);
    }

    public MLMXVendor(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public List<MLMXVendorService> getServices()
    {
        if (services != null )
                return services;

        services =  new Query(getCtx() , I_LMX_VendorService.Table_Name, I_LMX_VendorService.COLUMNNAME_LMX_Vendor_ID + "=?", get_TrxName())
                .setClient_ID()
                .setParameters(getLMX_Vendor_ID())
                .setOrderBy(I_LMX_VendorService.COLUMNNAME_SeqNo)
                .list();

        return services;
    }

    public MLMXVendorService getService(String serviceType)
    {
        for (MLMXVendorService service : getServices())
        {
            if( service.getSOAPServiceType().equals(serviceType))
                return service;
        }
        throw new AdempiereException("@SOAPServiceType@ @NotFound@");
    }

}
