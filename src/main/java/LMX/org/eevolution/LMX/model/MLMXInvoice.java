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

import org.compiere.model.MInvoice;
import org.compiere.model.Query;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Created by e-Evolution on 24/12/14.
 */
public class MLMXInvoice extends X_LMX_Invoice {

    public static MLMXInvoice getInvoice(MInvoice invoice)
    {
        return new Query(invoice.getCtx() , Table_Name , I_LMX_Invoice.COLUMNNAME_C_Invoice_ID + "=?" ,  invoice.get_TrxName())
                .setClient_ID()
                .setParameters(invoice.getC_Invoice_ID())
                .first();
    }
    public MLMXInvoice(Properties ctx, int LMX_Invoice_ID, String trxName) {
        super(ctx, LMX_Invoice_ID, trxName);
    }

    public MLMXInvoice(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
    public MLMXInvoice(MInvoice invoice)
    {
        super(invoice.getCtx() , 0 , invoice.get_TrxName());
        setC_Invoice_ID(getC_Invoice_ID());
    }
}
