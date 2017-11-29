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

/** Generated Model for LMX_BPartner
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_LMX_BPartner extends PO implements I_LMX_BPartner, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171129L;

    /** Standard Constructor */
    public X_LMX_BPartner (Properties ctx, int LMX_BPartner_ID, String trxName)
    {
      super (ctx, LMX_BPartner_ID, trxName);
      /** if (LMX_BPartner_ID == 0)
        {
			setC_BPartner_ID (0);
        } */
    }

    /** Load Constructor */
    public X_LMX_BPartner (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_LMX_BPartner[")
        .append(get_ID()).append("]");
      return sb.toString();
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
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
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

	/** Set Immutable Universally Unique Identifier.
		@param UUID 
		Immutable Universally Unique Identifier
	  */
	public void setUUID (String UUID)
	{
		set_Value (COLUMNNAME_UUID, UUID);
	}

	/** Get Immutable Universally Unique Identifier.
		@return Immutable Universally Unique Identifier
	  */
	public String getUUID () 
	{
		return (String)get_Value(COLUMNNAME_UUID);
	}

	/** UsoCFDI AD_Reference_ID=53948 */
	public static final int USOCFDI_AD_Reference_ID=53948;
	/** Adquisición de mercancías = G01 */
	public static final String USOCFDI_AdquisiciónDeMercancías = "G01";
	/** Devoluciones, descuentos o bonificaciones = G02 */
	public static final String USOCFDI_DevolucionesDescuentosOBonificaciones = "G02";
	/** Gastos en general = G03 */
	public static final String USOCFDI_GastosEnGeneral = "G03";
	/** Set UsoCFDI.
		@param UsoCFDI 
		UsoCFDI
	  */
	public void setUsoCFDI (String UsoCFDI)
	{

		set_Value (COLUMNNAME_UsoCFDI, UsoCFDI);
	}

	/** Get UsoCFDI.
		@return UsoCFDI
	  */
	public String getUsoCFDI () 
	{
		return (String)get_Value(COLUMNNAME_UsoCFDI);
	}
}