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
package org.eevolution.LMX.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for LMX_VendorService
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0
 */
public interface I_LMX_VendorService 
{

    /** TableName=LMX_VendorService */
    public static final String Table_Name = "LMX_VendorService";

    /** AD_Table_ID=53916 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name LMX_VendorService_ID */
    public static final String COLUMNNAME_LMX_VendorService_ID = "LMX_VendorService_ID";

	/** Set Vendor CFDI SOAP Service ID.
	  * Vendor CFDI SOAP Service ID
	  */
	public void setLMX_VendorService_ID (int LMX_VendorService_ID);

	/** Get Vendor CFDI SOAP Service ID.
	  * Vendor CFDI SOAP Service ID
	  */
	public int getLMX_VendorService_ID();

    /** Column name LMX_Vendor_ID */
    public static final String COLUMNNAME_LMX_Vendor_ID = "LMX_Vendor_ID";

	/** Set Vendor CFDI.
	  * Vendor CFDI
	  */
	public void setLMX_Vendor_ID (int LMX_Vendor_ID);

	/** Get Vendor CFDI.
	  * Vendor CFDI
	  */
	public int getLMX_Vendor_ID();

	public org.eevolution.LMX.model.I_LMX_Vendor getLMX_Vendor() throws RuntimeException;

    /** Column name SOAPAction */
    public static final String COLUMNNAME_SOAPAction = "SOAPAction";

	/** Set SOAP Action.
	  * SOAP Action
	  */
	public void setSOAPAction (String SOAPAction);

	/** Get SOAP Action.
	  * SOAP Action
	  */
	public String getSOAPAction();

    /** Column name SOAPBinding */
    public static final String COLUMNNAME_SOAPBinding = "SOAPBinding";

	/** Set SOAP Binding.
	  * SOAP Binding
	  */
	public void setSOAPBinding (String SOAPBinding);

	/** Get SOAP Binding.
	  * SOAP Binding
	  */
	public String getSOAPBinding();

    /** Column name SOAPEndpointAddress */
    public static final String COLUMNNAME_SOAPEndpointAddress = "SOAPEndpointAddress";

	/** Set SOAP Endpoint Address.
	  * SOAP Endpoint Address
	  */
	public void setSOAPEndpointAddress (String SOAPEndpointAddress);

	/** Get SOAP Endpoint Address.
	  * SOAP Endpoint Address
	  */
	public String getSOAPEndpointAddress();

    /** Column name SOAPPort */
    public static final String COLUMNNAME_SOAPPort = "SOAPPort";

	/** Set SOAP Port.
	  * SOAP Port
	  */
	public void setSOAPPort (String SOAPPort);

	/** Get SOAP Port.
	  * SOAP Port
	  */
	public String getSOAPPort();

    /** Column name SOAPRequest */
    public static final String COLUMNNAME_SOAPRequest = "SOAPRequest";

	/** Set SOAP Request.
	  * SOAP Request
	  */
	public void setSOAPRequest (String SOAPRequest);

	/** Get SOAP Request.
	  * SOAP Request
	  */
	public String getSOAPRequest();

    /** Column name SOAPServiceName */
    public static final String COLUMNNAME_SOAPServiceName = "SOAPServiceName";

	/** Set SOAP Service Name.
	  * SOAP Service Name
	  */
	public void setSOAPServiceName (String SOAPServiceName);

	/** Get SOAP Service Name.
	  * SOAP Service Name
	  */
	public String getSOAPServiceName();

    /** Column name SOAPServiceType */
    public static final String COLUMNNAME_SOAPServiceType = "SOAPServiceType";

	/** Set SOAP Service Type.
	  * SOAP Service Type
	  */
	public void setSOAPServiceType (String SOAPServiceType);

	/** Get SOAP Service Type.
	  * SOAP Service Type
	  */
	public String getSOAPServiceType();

    /** Column name SOAPTargetNS */
    public static final String COLUMNNAME_SOAPTargetNS = "SOAPTargetNS";

	/** Set SOAP TargetNS.
	  * SOAP TargetNS
	  */
	public void setSOAPTargetNS (String SOAPTargetNS);

	/** Get SOAP TargetNS.
	  * SOAP TargetNS
	  */
	public String getSOAPTargetNS();

    /** Column name SeqNo */
    public static final String COLUMNNAME_SeqNo = "SeqNo";

	/** Set Sequence.
	  * Method of ordering records;
 lowest number comes first
	  */
	public void setSeqNo (int SeqNo);

	/** Get Sequence.
	  * Method of ordering records;
 lowest number comes first
	  */
	public int getSeqNo();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
