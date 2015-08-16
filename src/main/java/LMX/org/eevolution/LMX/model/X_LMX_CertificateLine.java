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

/** Generated Model for LMX_CertificateLine
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0 - $Id$ */
public class X_LMX_CertificateLine extends PO implements I_LMX_CertificateLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20150816L;

    /** Standard Constructor */
    public X_LMX_CertificateLine (Properties ctx, int LMX_CertificateLine_ID, String trxName)
    {
      super (ctx, LMX_CertificateLine_ID, trxName);
      /** if (LMX_CertificateLine_ID == 0)
        {
			setAD_Sequence_ID (0);
			setLMX_CertificateLine_ID (0);
			setLMX_Certificate_ID (0);
			setLMX_Vendor_ID (0);
        } */
    }

    /** Load Constructor */
    public X_LMX_CertificateLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_LMX_CertificateLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Sequence getAD_Sequence() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Sequence)MTable.get(getCtx(), org.compiere.model.I_AD_Sequence.Table_Name)
			.getPO(getAD_Sequence_ID(), get_TrxName());	}

	/** Set Sequence.
		@param AD_Sequence_ID 
		Document Sequence
	  */
	public void setAD_Sequence_ID (int AD_Sequence_ID)
	{
		if (AD_Sequence_ID < 1) 
			set_Value (COLUMNNAME_AD_Sequence_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Sequence_ID, Integer.valueOf(AD_Sequence_ID));
	}

	/** Get Sequence.
		@return Document Sequence
	  */
	public int getAD_Sequence_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Sequence_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set MX Certificate Lines.
		@param LMX_CertificateLine_ID 
		MX Certificate Lines
	  */
	public void setLMX_CertificateLine_ID (int LMX_CertificateLine_ID)
	{
		if (LMX_CertificateLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LMX_CertificateLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LMX_CertificateLine_ID, Integer.valueOf(LMX_CertificateLine_ID));
	}

	/** Get MX Certificate Lines.
		@return MX Certificate Lines
	  */
	public int getLMX_CertificateLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LMX_CertificateLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.eevolution.LMX.model.I_LMX_Certificate getLMX_Certificate() throws RuntimeException
    {
		return (org.eevolution.LMX.model.I_LMX_Certificate)MTable.get(getCtx(), org.eevolution.LMX.model.I_LMX_Certificate.Table_Name)
			.getPO(getLMX_Certificate_ID(), get_TrxName());	}

	/** Set Localization México Certificate.
		@param LMX_Certificate_ID 
		Localization México Certificate
	  */
	public void setLMX_Certificate_ID (int LMX_Certificate_ID)
	{
		if (LMX_Certificate_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LMX_Certificate_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LMX_Certificate_ID, Integer.valueOf(LMX_Certificate_ID));
	}

	/** Get Localization México Certificate.
		@return Localization México Certificate
	  */
	public int getLMX_Certificate_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LMX_Certificate_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.eevolution.LMX.model.I_LMX_Vendor getLMX_Vendor() throws RuntimeException
    {
		return (org.eevolution.LMX.model.I_LMX_Vendor)MTable.get(getCtx(), org.eevolution.LMX.model.I_LMX_Vendor.Table_Name)
			.getPO(getLMX_Vendor_ID(), get_TrxName());	}

	/** Set Vendor CFDI.
		@param LMX_Vendor_ID 
		Vendor CFDI
	  */
	public void setLMX_Vendor_ID (int LMX_Vendor_ID)
	{
		if (LMX_Vendor_ID < 1) 
			set_Value (COLUMNNAME_LMX_Vendor_ID, null);
		else 
			set_Value (COLUMNNAME_LMX_Vendor_ID, Integer.valueOf(LMX_Vendor_ID));
	}

	/** Get Vendor CFDI.
		@return Vendor CFDI
	  */
	public int getLMX_Vendor_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LMX_Vendor_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}