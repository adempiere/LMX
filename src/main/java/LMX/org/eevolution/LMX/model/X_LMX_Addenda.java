/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.										*
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net or http://www.adempiere.net/license.html         *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.eevolution.LMX.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Model for LMX_Addenda
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_LMX_Addenda extends PO implements I_LMX_Addenda, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170613L;

    /** Standard Constructor */
    public X_LMX_Addenda (Properties ctx, int LMX_Addenda_ID, String trxName)
    {
      super (ctx, LMX_Addenda_ID, trxName);
      /** if (LMX_Addenda_ID == 0)
        {
			setLMX_Addenda_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_LMX_Addenda (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_LMX_Addenda[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_PrintFormat getAD_PrintFormat() throws RuntimeException
    {
		return (org.compiere.model.I_AD_PrintFormat)MTable.get(getCtx(), org.compiere.model.I_AD_PrintFormat.Table_Name)
			.getPO(getAD_PrintFormat_ID(), get_TrxName());	}

	/** Set Print Format.
		@param AD_PrintFormat_ID 
		Data Print Format
	  */
	public void setAD_PrintFormat_ID (int AD_PrintFormat_ID)
	{
		if (AD_PrintFormat_ID < 1) 
			set_Value (COLUMNNAME_AD_PrintFormat_ID, null);
		else 
			set_Value (COLUMNNAME_AD_PrintFormat_ID, Integer.valueOf(AD_PrintFormat_ID));
	}

	/** Get Print Format.
		@return Data Print Format
	  */
	public int getAD_PrintFormat_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PrintFormat_ID);
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

	/** Set CFDI ADempiere Transformer XSLT.
		@param CFDIADTransformer_ID 
		CFDI ADempiere Transformer XSLT
	  */
	public void setCFDIADTransformer_ID (int CFDIADTransformer_ID)
	{
		if (CFDIADTransformer_ID < 1) 
			set_Value (COLUMNNAME_CFDIADTransformer_ID, null);
		else 
			set_Value (COLUMNNAME_CFDIADTransformer_ID, Integer.valueOf(CFDIADTransformer_ID));
	}

	/** Get CFDI ADempiere Transformer XSLT.
		@return CFDI ADempiere Transformer XSLT
	  */
	public int getCFDIADTransformer_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CFDIADTransformer_ID);
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

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Información de la  addenda ID.
		@param LMX_Addenda_ID Información de la  addenda ID	  */
	public void setLMX_Addenda_ID (int LMX_Addenda_ID)
	{
		if (LMX_Addenda_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LMX_Addenda_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LMX_Addenda_ID, Integer.valueOf(LMX_Addenda_ID));
	}

	/** Get Información de la  addenda ID.
		@return Información de la  addenda ID	  */
	public int getLMX_Addenda_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LMX_Addenda_ID);
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

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getValue());
    }
}