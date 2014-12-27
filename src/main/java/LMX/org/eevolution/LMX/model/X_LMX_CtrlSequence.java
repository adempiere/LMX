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
package org.eevolution.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for LMX_CtrlSequence
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_LMX_CtrlSequence extends PO implements I_LMX_CtrlSequence, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20131227L;

    /** Standard Constructor */
    public X_LMX_CtrlSequence (Properties ctx, int LMX_CtrlSequence_ID, String trxName)
    {
      super (ctx, LMX_CtrlSequence_ID, trxName);
      /** if (LMX_CtrlSequence_ID == 0)
        {
			setC_DocType_ID (0);
			setLMX_CtrlSequence_ID (0);
			setdaterequest (new Timestamp( System.currentTimeMillis() ));
			setsequencebegin (0);
			setsequencectrlno (null);
			setsequenceend (0);
			setsequencerange (0);
        } */
    }

    /** Load Constructor */
    public X_LMX_CtrlSequence (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_LMX_CtrlSequence[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set LMS_CtrlSequence.
		@param LMX_CtrlSequence_ID LMS_CtrlSequence	  */
	public void setLMX_CtrlSequence_ID (int LMX_CtrlSequence_ID)
	{
		if (LMX_CtrlSequence_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LMX_CtrlSequence_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LMX_CtrlSequence_ID, Integer.valueOf(LMX_CtrlSequence_ID));
	}

	/** Get LMS_CtrlSequence.
		@return LMS_CtrlSequence	  */
	public int getLMX_CtrlSequence_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LMX_CtrlSequence_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Autorization Number.
		@param autorizationnumber Autorization Number	  */
	public void setautorizationnumber (String autorizationnumber)
	{
		set_Value (COLUMNNAME_autorizationnumber, autorizationnumber);
	}

	/** Get Autorization Number.
		@return Autorization Number	  */
	public String getautorizationnumber () 
	{
		return (String)get_Value(COLUMNNAME_autorizationnumber);
	}

	/** Set Date Request.
		@param daterequest Date Request	  */
	public void setdaterequest (Timestamp daterequest)
	{
		set_Value (COLUMNNAME_daterequest, daterequest);
	}

	/** Get Date Request.
		@return Date Request	  */
	public Timestamp getdaterequest () 
	{
		return (Timestamp)get_Value(COLUMNNAME_daterequest);
	}

	/** Set Sequence Begin.
		@param sequencebegin Sequence Begin	  */
	public void setsequencebegin (int sequencebegin)
	{
		set_Value (COLUMNNAME_sequencebegin, Integer.valueOf(sequencebegin));
	}

	/** Get Sequence Begin.
		@return Sequence Begin	  */
	public int getsequencebegin () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_sequencebegin);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Sequence Ctrl No.
		@param sequencectrlno Sequence Ctrl No	  */
	public void setsequencectrlno (String sequencectrlno)
	{
		set_Value (COLUMNNAME_sequencectrlno, sequencectrlno);
	}

	/** Get Sequence Ctrl No.
		@return Sequence Ctrl No	  */
	public String getsequencectrlno () 
	{
		return (String)get_Value(COLUMNNAME_sequencectrlno);
	}

	/** Set Sequence End.
		@param sequenceend Sequence End	  */
	public void setsequenceend (int sequenceend)
	{
		set_Value (COLUMNNAME_sequenceend, Integer.valueOf(sequenceend));
	}

	/** Get Sequence End.
		@return Sequence End	  */
	public int getsequenceend () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_sequenceend);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Sequence Range.
		@param sequencerange Sequence Range	  */
	public void setsequencerange (int sequencerange)
	{
		set_Value (COLUMNNAME_sequencerange, Integer.valueOf(sequencerange));
	}

	/** Get Sequence Range.
		@return Sequence Range	  */
	public int getsequencerange () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_sequencerange);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}