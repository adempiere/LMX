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
import org.compiere.model.MBPartner;
import org.compiere.model.Query;
import org.compiere.util.CCache;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

/**
 * Created by e-Evolution on 22/12/13.
 */
public class MLMXAddenda extends X_LMX_Addenda {

    private static CCache<String, MLMXAddenda> cacheAddenda = new CCache<>(Table_Name, 5);

    private static CCache<String, List<MLMXAddenda>> cacheAddendaList = new CCache<>(Table_Name, 5);

    /**
     *
     * @param ctx
     * @param partnerId
     * @param trxName
     * @return
     */
    @Deprecated
    public static MLMXAddenda getByBPartnerId(Properties ctx , int partnerId ,String  trxName)
    {
        String key = String.valueOf(Env.getAD_Client_ID(Env.getCtx())) + "-" + String.valueOf(partnerId);
        MLMXAddenda addenda = cacheAddenda.get(key);
        if (addenda != null)
            return addenda;

        addenda =  new Query(ctx, MLMXAddenda.Table_Name, "C_BPartner_ID=?", trxName)
                .setClient_ID()
                .setParameters(partnerId)
                .setOnlyActiveRecords(true)
                .first();
        if (addenda != null)
            cacheAddenda.put(key, addenda);
        return addenda;
    }


    /**
     * Get Addenda by Partner
     * @param partner
     * @return
     */
    public static List<MLMXAddenda> getByBPartnerId(MBPartner partner)
    {
        String key = String.valueOf(Env.getAD_Client_ID(Env.getCtx())) + "-" + String.valueOf(partner.getC_BPartner_ID());
        List<MLMXAddenda> addendaList = cacheAddendaList.get(key);
        if (addendaList != null)
            return addendaList;

        addendaList =  new Query(partner.getCtx(), MLMXAddenda.Table_Name, "C_BPartner_ID=?", partner.get_TrxName())
                .setClient_ID()
                .setParameters(partner.getC_BPartner_ID())
                .setOnlyActiveRecords(true)
                .setOrderBy(MLMXAddenda.COLUMNNAME_SeqNo)
                .list();
        if (cacheAddendaList != null)
            cacheAddendaList.put(key, addendaList);
        return addendaList;
    }

    public MLMXAddenda(Properties ctx, int addendaId, String trxName) {
        super(ctx, addendaId, trxName);
    }

    public MLMXAddenda(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    protected boolean beforeSave (boolean newRecord)
    {
        if (getValue() == null)
            throw  new AdempiereException("@Value@ @IsMandatory@");

        if (getName() == null)
            throw  new AdempiereException("@Value@ @IsMandatory@");

        if (getC_BPartner_ID() <= 0)
            throw  new AdempiereException("@C_BPartner_ID@ @IsMandatory@");

        if (getAD_PrintFormat_ID() <= 0)
            throw  new AdempiereException("@AD_PrintFormat_ID@ @IsMandatory@");

        if (getAD_Column_ID() <= 0)
            throw  new AdempiereException("@AD_Column_ID@ @IsMandatory@");

        return  true;
    }

    protected boolean afterSave (boolean newRecord, boolean success) {
        return true;
    }
}
