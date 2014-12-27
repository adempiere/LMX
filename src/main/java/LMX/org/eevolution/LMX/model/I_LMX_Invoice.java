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

/** Generated Interface for LMX_Invoice
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0RC
 */
public interface I_LMX_Invoice 
{

    /** TableName=LMX_Invoice */
    public static final String Table_Name = "LMX_Invoice";

    /** AD_Table_ID=1000034 */
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

    /** Column name CFDIQR_ID */
    public static final String COLUMNNAME_CFDIQR_ID = "CFDIQR_ID";

	/** Set CFDI QR Id	  */
	public void setCFDIQR_ID (int CFDIQR_ID);

	/** Get CFDI QR Id	  */
	public int getCFDIQR_ID();

    /** Column name CFDISATCertificate */
    public static final String COLUMNNAME_CFDISATCertificate = "CFDISATCertificate";

	/** Set Number of certificate of SAT	  */
	public void setCFDISATCertificate (String CFDISATCertificate);

	/** Get Number of certificate of SAT	  */
	public String getCFDISATCertificate();

    /** Column name CFDISATSeal */
    public static final String COLUMNNAME_CFDISATSeal = "CFDISATSeal";

	/** Set CFDI SAT Seal	  */
	public void setCFDISATSeal (String CFDISATSeal);

	/** Get CFDI SAT Seal	  */
	public String getCFDISATSeal();

    /** Column name CFDISeal */
    public static final String COLUMNNAME_CFDISeal = "CFDISeal";

	/** Set CFDI Seal	  */
	public void setCFDISeal (String CFDISeal);

	/** Get CFDI Seal	  */
	public String getCFDISeal();

    /** Column name CFDISealingDate */
    public static final String COLUMNNAME_CFDISealingDate = "CFDISealingDate";

	/** Set CFDI Sealing Date	  */
	public void setCFDISealingDate (String CFDISealingDate);

	/** Get CFDI Sealing Date	  */
	public String getCFDISealingDate();

    /** Column name CFDIToken */
    public static final String COLUMNNAME_CFDIToken = "CFDIToken";

	/** Set CFDI Token	  */
	public void setCFDIToken (String CFDIToken);

	/** Get CFDI Token	  */
	public String getCFDIToken();

    /** Column name CFDIUUID */
    public static final String COLUMNNAME_CFDIUUID = "CFDIUUID";

	/** Set UUID of CFDI	  */
	public void setCFDIUUID (String CFDIUUID);

	/** Get UUID of CFDI	  */
	public String getCFDIUUID();

    /** Column name CFDIXML */
    public static final String COLUMNNAME_CFDIXML = "CFDIXML";

	/** Set XML of CFDI	  */
	public void setCFDIXML (String CFDIXML);

	/** Get XML of CFDI	  */
	public String getCFDIXML();

    /** Column name C_Invoice_ID */
    public static final String COLUMNNAME_C_Invoice_ID = "C_Invoice_ID";

	/** Set Invoice.
	  * Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID);

	/** Get Invoice.
	  * Invoice Identifier
	  */
	public int getC_Invoice_ID();

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException;

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

    /** Column name LMX_Invoice_ID */
    public static final String COLUMNNAME_LMX_Invoice_ID = "LMX_Invoice_ID";

	/** Set LMX Invoice Information ID	  */
	public void setLMX_Invoice_ID (int LMX_Invoice_ID);

	/** Get LMX Invoice Information ID	  */
	public int getLMX_Invoice_ID();

    /** Column name TaxID */
    public static final String COLUMNNAME_TaxID = "TaxID";

	/** Set Tax ID.
	  * Tax Identification
	  */
	public void setTaxID (String TaxID);

	/** Get Tax ID.
	  * Tax Identification
	  */
	public String getTaxID();

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
