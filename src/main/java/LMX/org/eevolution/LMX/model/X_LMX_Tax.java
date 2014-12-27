/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.eevolution.LMX.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Model for LMX_Tax
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0RC - $Id$ */
public class X_LMX_Tax extends PO implements I_LMX_Tax, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20141224L;

    /** Standard Constructor */
    public X_LMX_Tax (Properties ctx, int LMX_Tax_ID, String trxName)
    {
      super (ctx, LMX_Tax_ID, trxName);
      /** if (LMX_Tax_ID == 0)
        {
			setAD_Tree_Org_ID (0);
			setC_BPartner_ID (0);
			setC_Location_ID (0);
			setLMX_Tax_ID (0);
			setName (null);
			setTaxID (null);
        } */
    }

    /** Load Constructor */
    public X_LMX_Tax (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_LMX_Tax[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Tree getAD_Tree_Org() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Tree)MTable.get(getCtx(), org.compiere.model.I_AD_Tree.Table_Name)
			.getPO(getAD_Tree_Org_ID(), get_TrxName());	}

	/** Set Organization Tree.
		@param AD_Tree_Org_ID 
		Trees are used for (financial) reporting and security access (via role)
	  */
	public void setAD_Tree_Org_ID (int AD_Tree_Org_ID)
	{
		if (AD_Tree_Org_ID < 1) 
			set_Value (COLUMNNAME_AD_Tree_Org_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Tree_Org_ID, Integer.valueOf(AD_Tree_Org_ID));
	}

	/** Get Organization Tree.
		@return Trees are used for (financial) reporting and security access (via role)
	  */
	public int getAD_Tree_Org_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Tree_Org_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set CFDI Schema XML.
		@param CFDISchema_ID 
		CFDI Schema XML
	  */
	public void setCFDISchema_ID (int CFDISchema_ID)
	{
		if (CFDISchema_ID < 1) 
			set_Value (COLUMNNAME_CFDISchema_ID, null);
		else 
			set_Value (COLUMNNAME_CFDISchema_ID, Integer.valueOf(CFDISchema_ID));
	}

	/** Get CFDI Schema XML.
		@return CFDI Schema XML
	  */
	public int getCFDISchema_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CFDISchema_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set CFDI Transformer String XSLT.
		@param CFDITransformerString_ID 
		CFDI Transformer String XSLT
	  */
	public void setCFDITransformerString_ID (int CFDITransformerString_ID)
	{
		if (CFDITransformerString_ID < 1) 
			set_Value (COLUMNNAME_CFDITransformerString_ID, null);
		else 
			set_Value (COLUMNNAME_CFDITransformerString_ID, Integer.valueOf(CFDITransformerString_ID));
	}

	/** Get CFDI Transformer String XSLT.
		@return CFDI Transformer String XSLT
	  */
	public int getCFDITransformerString_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CFDITransformerString_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set CFDI Transformer XSLT.
		@param CFDITransformer_ID 
		CFDI Transformer XSLT
	  */
	public void setCFDITransformer_ID (int CFDITransformer_ID)
	{
		if (CFDITransformer_ID < 1) 
			set_Value (COLUMNNAME_CFDITransformer_ID, null);
		else 
			set_Value (COLUMNNAME_CFDITransformer_ID, Integer.valueOf(CFDITransformer_ID));
	}

	/** Get CFDI Transformer XSLT.
		@return CFDI Transformer XSLT
	  */
	public int getCFDITransformer_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CFDITransformer_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Location getC_Location() throws RuntimeException
    {
		return (I_C_Location)MTable.get(getCtx(), I_C_Location.Table_Name)
			.getPO(getC_Location_ID(), get_TrxName());	}

	/** Set Address.
		@param C_Location_ID 
		Location or Address
	  */
	public void setC_Location_ID (int C_Location_ID)
	{
		if (C_Location_ID < 1) 
			set_Value (COLUMNNAME_C_Location_ID, null);
		else 
			set_Value (COLUMNNAME_C_Location_ID, Integer.valueOf(C_Location_ID));
	}

	/** Get Address.
		@return Location or Address
	  */
	public int getC_Location_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Mexico Tax Information.
		@param LMX_Tax_ID 
		Mexico Tax Information
	  */
	public void setLMX_Tax_ID (int LMX_Tax_ID)
	{
		if (LMX_Tax_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LMX_Tax_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LMX_Tax_ID, Integer.valueOf(LMX_Tax_ID));
	}

	/** Get Mexico Tax Information.
		@return Mexico Tax Information
	  */
	public int getLMX_Tax_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LMX_Tax_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set Partner ID.
		@param PartnerID 
		Partner ID or Account for the Payment Processor
	  */
	public void setPartnerID (String PartnerID)
	{
		set_Value (COLUMNNAME_PartnerID, PartnerID);
	}

	/** Get Partner ID.
		@return Partner ID or Account for the Payment Processor
	  */
	public String getPartnerID () 
	{
		return (String)get_Value(COLUMNNAME_PartnerID);
	}

	/** Set Tax ID.
		@param TaxID 
		Tax Identification
	  */
	public void setTaxID (String TaxID)
	{
		set_Value (COLUMNNAME_TaxID, TaxID);
	}

	/** Get Tax ID.
		@return Tax Identification
	  */
	public String getTaxID () 
	{
		return (String)get_Value(COLUMNNAME_TaxID);
	}
}