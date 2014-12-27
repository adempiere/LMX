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

/** Generated Model for LMX_VendorService
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0RC - $Id$ */
public class X_LMX_VendorService extends PO implements I_LMX_VendorService, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20141224L;

    /** Standard Constructor */
    public X_LMX_VendorService (Properties ctx, int LMX_VendorService_ID, String trxName)
    {
      super (ctx, LMX_VendorService_ID, trxName);
      /** if (LMX_VendorService_ID == 0)
        {
			setLMX_VendorService_ID (0);
        } */
    }

    /** Load Constructor */
    public X_LMX_VendorService (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_LMX_VendorService[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Vendor CFDI SOAP Service ID.
		@param LMX_VendorService_ID Vendor CFDI SOAP Service ID	  */
	public void setLMX_VendorService_ID (int LMX_VendorService_ID)
	{
		if (LMX_VendorService_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LMX_VendorService_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LMX_VendorService_ID, Integer.valueOf(LMX_VendorService_ID));
	}

	/** Get Vendor CFDI SOAP Service ID.
		@return Vendor CFDI SOAP Service ID	  */
	public int getLMX_VendorService_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LMX_VendorService_ID);
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
			set_ValueNoCheck (COLUMNNAME_LMX_Vendor_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LMX_Vendor_ID, Integer.valueOf(LMX_Vendor_ID));
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

	/** Set SOAP Action.
		@param SOAPAction 
		SOAP Action
	  */
	public void setSOAPAction (String SOAPAction)
	{
		set_Value (COLUMNNAME_SOAPAction, SOAPAction);
	}

	/** Get SOAP Action.
		@return SOAP Action
	  */
	public String getSOAPAction () 
	{
		return (String)get_Value(COLUMNNAME_SOAPAction);
	}

	/** Set SOAP Binding.
		@param SOAPBinding 
		SOAP Binding
	  */
	public void setSOAPBinding (String SOAPBinding)
	{
		set_Value (COLUMNNAME_SOAPBinding, SOAPBinding);
	}

	/** Get SOAP Binding.
		@return SOAP Binding
	  */
	public String getSOAPBinding () 
	{
		return (String)get_Value(COLUMNNAME_SOAPBinding);
	}

	/** Set SOAP Endpoint Address.
		@param SOAPEndpointAddress 
		SOAP Endpoint Address
	  */
	public void setSOAPEndpointAddress (String SOAPEndpointAddress)
	{
		set_Value (COLUMNNAME_SOAPEndpointAddress, SOAPEndpointAddress);
	}

	/** Get SOAP Endpoint Address.
		@return SOAP Endpoint Address
	  */
	public String getSOAPEndpointAddress () 
	{
		return (String)get_Value(COLUMNNAME_SOAPEndpointAddress);
	}

	/** Set SOAP Port.
		@param SOAPPort 
		SOAP Port
	  */
	public void setSOAPPort (String SOAPPort)
	{
		set_Value (COLUMNNAME_SOAPPort, SOAPPort);
	}

	/** Get SOAP Port.
		@return SOAP Port
	  */
	public String getSOAPPort () 
	{
		return (String)get_Value(COLUMNNAME_SOAPPort);
	}

	/** Set SOAP Request.
		@param SOAPRequest 
		SOAP Request
	  */
	public void setSOAPRequest (String SOAPRequest)
	{
		set_Value (COLUMNNAME_SOAPRequest, SOAPRequest);
	}

	/** Get SOAP Request.
		@return SOAP Request
	  */
	public String getSOAPRequest () 
	{
		return (String)get_Value(COLUMNNAME_SOAPRequest);
	}

	/** Set SOAP Service Name.
		@param SOAPServiceName 
		SOAP Service Name
	  */
	public void setSOAPServiceName (String SOAPServiceName)
	{
		set_Value (COLUMNNAME_SOAPServiceName, SOAPServiceName);
	}

	/** Get SOAP Service Name.
		@return SOAP Service Name
	  */
	public String getSOAPServiceName () 
	{
		return (String)get_Value(COLUMNNAME_SOAPServiceName);
	}

	/** SOAPServiceType AD_Reference_ID=1000000 */
	public static final int SOAPSERVICETYPE_AD_Reference_ID=1000000;
	/** Cancel Stamp = CD */
	public static final String SOAPSERVICETYPE_CancelStamp = "CD";
	/** Generate Stamp = GS */
	public static final String SOAPSERVICETYPE_GenerateStamp = "GS";
	/** Query Document = QD */
	public static final String SOAPSERVICETYPE_QueryDocument = "QD";
	/** QR = QR */
	public static final String SOAPSERVICETYPE_QR = "QR";
	/** Token = TK */
	public static final String SOAPSERVICETYPE_Token = "TK";
	/** Set SOAP Service Type.
		@param SOAPServiceType 
		SOAP Service Type
	  */
	public void setSOAPServiceType (String SOAPServiceType)
	{

		set_Value (COLUMNNAME_SOAPServiceType, SOAPServiceType);
	}

	/** Get SOAP Service Type.
		@return SOAP Service Type
	  */
	public String getSOAPServiceType () 
	{
		return (String)get_Value(COLUMNNAME_SOAPServiceType);
	}

	/** Set SOAP TargetNS.
		@param SOAPTargetNS 
		SOAP TargetNS
	  */
	public void setSOAPTargetNS (String SOAPTargetNS)
	{
		set_Value (COLUMNNAME_SOAPTargetNS, SOAPTargetNS);
	}

	/** Get SOAP TargetNS.
		@return SOAP TargetNS
	  */
	public String getSOAPTargetNS () 
	{
		return (String)get_Value(COLUMNNAME_SOAPTargetNS);
	}

	/** Set Sequence.
		@param SeqNo 
		Method of ordering records; lowest number comes first
	  */
	public void setSeqNo (int SeqNo)
	{
		set_Value (COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Method of ordering records; lowest number comes first
	  */
	public int getSeqNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}