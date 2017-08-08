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

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Created by e-Evolution on 22/12/13.
 */
public class MLMXAddenda extends X_LMX_Addenda {

    public static MLMXAddenda getByBPartnerId(Properties ctx , int C_BPartner_ID ,String  trxName)
    {
        return new Query(ctx, MLMXAddenda.Table_Name, "C_BPartner_ID=?", trxName)
                .setParameters(C_BPartner_ID)
                .setClient_ID()
                .setOnlyActiveRecords(true)
                .first();
    }

    public MLMXAddenda(Properties ctx, int LMX_Addenda_ID, String trxName) {
        super(ctx, LMX_Addenda_ID, trxName);
    }

    public MLMXAddenda(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
