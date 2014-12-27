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

/** Generated Model for LMX_Invoice
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0RC - $Id$ */
public class X_LMX_Invoice extends PO implements I_LMX_Invoice, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20141224L;

    /** Standard Constructor */
    public X_LMX_Invoice (Properties ctx, int LMX_Invoice_ID, String trxName)
    {
      super (ctx, LMX_Invoice_ID, trxName);
      /** if (LMX_Invoice_ID == 0)
        {
			setLMX_Invoice_ID (0);
        } */
    }

    /** Load Constructor */
    public X_LMX_Invoice (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_LMX_Invoice[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set CFDI QR Id.
		@param CFDIQR_ID CFDI QR Id	  */
	public void setCFDIQR_ID (int CFDIQR_ID)
	{
		if (CFDIQR_ID < 1) 
			set_Value (COLUMNNAME_CFDIQR_ID, null);
		else 
			set_Value (COLUMNNAME_CFDIQR_ID, Integer.valueOf(CFDIQR_ID));
	}

	/** Get CFDI QR Id.
		@return CFDI QR Id	  */
	public int getCFDIQR_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CFDIQR_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Number of certificate of SAT.
		@param CFDISATCertificate Number of certificate of SAT	  */
	public void setCFDISATCertificate (String CFDISATCertificate)
	{
		set_Value (COLUMNNAME_CFDISATCertificate, CFDISATCertificate);
	}

	/** Get Number of certificate of SAT.
		@return Number of certificate of SAT	  */
	public String getCFDISATCertificate () 
	{
		return (String)get_Value(COLUMNNAME_CFDISATCertificate);
	}

	/** Set CFDI SAT Seal.
		@param CFDISATSeal CFDI SAT Seal	  */
	public void setCFDISATSeal (String CFDISATSeal)
	{
		set_Value (COLUMNNAME_CFDISATSeal, CFDISATSeal);
	}

	/** Get CFDI SAT Seal.
		@return CFDI SAT Seal	  */
	public String getCFDISATSeal () 
	{
		return (String)get_Value(COLUMNNAME_CFDISATSeal);
	}

	/** Set CFDI Seal.
		@param CFDISeal CFDI Seal	  */
	public void setCFDISeal (String CFDISeal)
	{
		set_Value (COLUMNNAME_CFDISeal, CFDISeal);
	}

	/** Get CFDI Seal.
		@return CFDI Seal	  */
	public String getCFDISeal () 
	{
		return (String)get_Value(COLUMNNAME_CFDISeal);
	}

	/** Set CFDI Sealing Date.
		@param CFDISealingDate CFDI Sealing Date	  */
	public void setCFDISealingDate (String CFDISealingDate)
	{
		set_Value (COLUMNNAME_CFDISealingDate, CFDISealingDate);
	}

	/** Get CFDI Sealing Date.
		@return CFDI Sealing Date	  */
	public String getCFDISealingDate () 
	{
		return (String)get_Value(COLUMNNAME_CFDISealingDate);
	}

	/** Set CFDI Token.
		@param CFDIToken CFDI Token	  */
	public void setCFDIToken (String CFDIToken)
	{
		set_Value (COLUMNNAME_CFDIToken, CFDIToken);
	}

	/** Get CFDI Token.
		@return CFDI Token	  */
	public String getCFDIToken () 
	{
		return (String)get_Value(COLUMNNAME_CFDIToken);
	}

	/** Set UUID of CFDI.
		@param CFDIUUID UUID of CFDI	  */
	public void setCFDIUUID (String CFDIUUID)
	{
		set_Value (COLUMNNAME_CFDIUUID, CFDIUUID);
	}

	/** Get UUID of CFDI.
		@return UUID of CFDI	  */
	public String getCFDIUUID () 
	{
		return (String)get_Value(COLUMNNAME_CFDIUUID);
	}

	/** Set XML of CFDI.
		@param CFDIXML XML of CFDI	  */
	public void setCFDIXML (String CFDIXML)
	{
		set_Value (COLUMNNAME_CFDIXML, CFDIXML);
	}

	/** Get XML of CFDI.
		@return XML of CFDI	  */
	public String getCFDIXML () 
	{
		return (String)get_Value(COLUMNNAME_CFDIXML);
	}

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (org.compiere.model.I_C_Invoice)MTable.get(getCtx(), org.compiere.model.I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_Value (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_Value (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set LMX Invoice Information ID.
		@param LMX_Invoice_ID LMX Invoice Information ID	  */
	public void setLMX_Invoice_ID (int LMX_Invoice_ID)
	{
		if (LMX_Invoice_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LMX_Invoice_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LMX_Invoice_ID, Integer.valueOf(LMX_Invoice_ID));
	}

	/** Get LMX Invoice Information ID.
		@return LMX Invoice Information ID	  */
	public int getLMX_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LMX_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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