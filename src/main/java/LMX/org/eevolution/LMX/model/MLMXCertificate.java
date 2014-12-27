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


import org.compiere.model.I_C_DocType;
import org.compiere.model.MInvoice;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

/**
 * Created by e-Evolution on 22/12/13.
 */
public class MLMXCertificate extends X_LMX_Certificate {

    private static MLMXCertificate certificate;
    private List<MLMXCertificateLine> certificateLines;

    public static MLMXCertificate get()
    {
        if (certificate != null)
            return certificate;

        certificate =  new Query(Env.getCtx(), I_LMX_Certificate.Table_Name, "" , null)
                .setClient_ID()
                .first();
        return certificate;
    }

    public MLMXCertificate(Properties ctx, int LMX_Certificate_ID, String trxName) {
        super(ctx, LMX_Certificate_ID, trxName);
    }

    public MLMXCertificate(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public List<MLMXCertificateLine> getLines() {

        if (certificateLines != null)
            return certificateLines;

        certificateLines = new Query(getCtx(), I_LMX_CertificateLine.Table_Name , I_LMX_CertificateLine.COLUMNNAME_LMX_Certificate_ID +  "=?", get_TrxName())
                .setClient_ID()
                .setParameters(getLMX_Certificate_ID())
                .list();

        return certificateLines;
    }

    public I_LMX_Vendor getVendorService(MInvoice invoice)
    {
        I_C_DocType docType = invoice.getC_DocType();

        for (MLMXCertificateLine line : getLines())
            if (line.getAD_Sequence_ID() == docType.getDocNoSequence_ID())
                return line.getLMX_Vendor();

        return null;
    }
}
