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

import org.compiere.model.Query;
import org.compiere.util.CCache;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Created by e-Evolution on 22/12/13.
 */
public class MLMXTax extends X_LMX_Tax {

    private static CCache<String, MLMXTax> cacheTaxInfo = new CCache<>(Table_Name, 5);

    public MLMXTax(Properties ctx, int LMX_Tax_ID, String trxName) {
        super(ctx, LMX_Tax_ID, trxName);
    }

    public MLMXTax(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public static MLMXTax getTax(Properties ctx, int orgId, String trxName) {
        String key = String.valueOf(Env.getAD_Client_ID(Env.getCtx())) + "-" + String.valueOf(orgId);
        MLMXTax taxInfo = cacheTaxInfo.get(key);
        if (taxInfo != null)
            return taxInfo;

        StringBuilder whereClause = new StringBuilder();
        whereClause.append("AD_Tree_Org_ID IN(SELECT AD_Tree_ID FROM AD_Tree WHERE AD_Tree_ID IN (SELECT AD_Tree_ID FROM AD_TreeNode WHERE  Node_ID = ?) AND TreeType = 'OO')");
        taxInfo = new Query(ctx, MLMXTax.Table_Name, whereClause.toString(), trxName)
                .setClient_ID()
                .setParameters(orgId)
                .first();
        if (taxInfo != null)
            cacheTaxInfo.put(key, taxInfo);

        return taxInfo;
    }
}
