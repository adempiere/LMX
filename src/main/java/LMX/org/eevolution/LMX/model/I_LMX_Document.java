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
package org.eevolution.LMX.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for LMX_Document
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0
 */
public interface I_LMX_Document 
{

    /** TableName=LMX_Document */
    public static final String Table_Name = "LMX_Document";

    /** AD_Table_ID=53910 */
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

    /** Column name AD_Table_ID */
    public static final String COLUMNNAME_AD_Table_ID = "AD_Table_ID";

	/** Set Table.
	  * Database Table information
	  */
	public void setAD_Table_ID (int AD_Table_ID);

	/** Get Table.
	  * Database Table information
	  */
	public int getAD_Table_ID();

	public org.compiere.model.I_AD_Table getAD_Table() throws RuntimeException;

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

    /** Column name CFDIString */
    public static final String COLUMNNAME_CFDIString = "CFDIString";

	/** Set CFDI String.
	  * CFDI String
	  */
	public void setCFDIString (String CFDIString);

	/** Get CFDI String.
	  * CFDI String
	  */
	public String getCFDIString();

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

    /** Column name IsCancelled */
    public static final String COLUMNNAME_IsCancelled = "IsCancelled";

	/** Set Cancelled.
	  * The transaction was cancelled
	  */
	public void setIsCancelled (boolean IsCancelled);

	/** Get Cancelled.
	  * The transaction was cancelled
	  */
	public boolean isCancelled();

    /** Column name LMX_Document_ID */
    public static final String COLUMNNAME_LMX_Document_ID = "LMX_Document_ID";

	/** Set LMX Document Information ID	  */
	public void setLMX_Document_ID (int LMX_Document_ID);

	/** Get LMX Document Information ID	  */
	public int getLMX_Document_ID();

    /** Column name Record_ID */
    public static final String COLUMNNAME_Record_ID = "Record_ID";

	/** Set Record ID.
	  * Direct internal record ID
	  */
	public void setRecord_ID (int Record_ID);

	/** Get Record ID.
	  * Direct internal record ID
	  */
	public int getRecord_ID();

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

    /** Column name TipoDeComprobante */
    public static final String COLUMNNAME_TipoDeComprobante = "TipoDeComprobante";

	/** Set TipoDeComprobante	  */
	public void setTipoDeComprobante (String TipoDeComprobante);

	/** Get TipoDeComprobante	  */
	public String getTipoDeComprobante();

    /** Column name TipoRelacion */
    public static final String COLUMNNAME_TipoRelacion = "TipoRelacion";

	/** Set Tipo Relacion	  */
	public void setTipoRelacion (String TipoRelacion);

	/** Get Tipo Relacion	  */
	public String getTipoRelacion();

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

    /** Column name UsoCFDI */
    public static final String COLUMNNAME_UsoCFDI = "UsoCFDI";

	/** Set UsoCFDI.
	  * UsoCFDI
	  */
	public void setUsoCFDI (String UsoCFDI);

	/** Get UsoCFDI.
	  * UsoCFDI
	  */
	public String getUsoCFDI();
}
