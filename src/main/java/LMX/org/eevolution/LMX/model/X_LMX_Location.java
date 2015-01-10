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

/** Generated Model for LMX_Location
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0 - $Id$ */
public class X_LMX_Location extends PO implements I_LMX_Location, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20150109L;

    /** Standard Constructor */
    public X_LMX_Location (Properties ctx, int LMX_Location_ID, String trxName)
    {
      super (ctx, LMX_Location_ID, trxName);
      /** if (LMX_Location_ID == 0)
        {
			setC_Location_ID (0);
			setLMX_Location_ID (0);
        } */
    }

    /** Load Constructor */
    public X_LMX_Location (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_LMX_Location[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_Location getC_Location() throws RuntimeException
    {
		return (org.compiere.model.I_C_Location)MTable.get(getCtx(), org.compiere.model.I_C_Location.Table_Name)
			.getPO(getC_Location_ID(), get_TrxName());	}

	/** Set Address.
		@param C_Location_ID 
		Location or Address
	  */
	public void setC_Location_ID (int C_Location_ID)
	{
		if (C_Location_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Location_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Location_ID, Integer.valueOf(C_Location_ID));
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

	/** Set External No.
		@param ExternalNo External No	  */
	public void setExternalNo (String ExternalNo)
	{
		set_Value (COLUMNNAME_ExternalNo, ExternalNo);
	}

	/** Get External No.
		@return External No	  */
	public String getExternalNo () 
	{
		return (String)get_Value(COLUMNNAME_ExternalNo);
	}

	/** Set Internal No.
		@param InternalNo Internal No	  */
	public void setInternalNo (String InternalNo)
	{
		set_Value (COLUMNNAME_InternalNo, InternalNo);
	}

	/** Get Internal No.
		@return Internal No	  */
	public String getInternalNo () 
	{
		return (String)get_Value(COLUMNNAME_InternalNo);
	}

	/** Set LMX Location ID.
		@param LMX_Location_ID LMX Location ID	  */
	public void setLMX_Location_ID (int LMX_Location_ID)
	{
		if (LMX_Location_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LMX_Location_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LMX_Location_ID, Integer.valueOf(LMX_Location_ID));
	}

	/** Get LMX Location ID.
		@return LMX Location ID	  */
	public int getLMX_Location_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LMX_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}