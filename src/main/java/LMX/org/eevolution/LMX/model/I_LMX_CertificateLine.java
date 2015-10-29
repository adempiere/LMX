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

/** Generated Interface for LMX_CertificateLine
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0
 */
public interface I_LMX_CertificateLine 
{

    /** TableName=LMX_CertificateLine */
    public static final String Table_Name = "LMX_CertificateLine";

    /** AD_Table_ID=53906 */
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

    /** Column name AD_Sequence_ID */
    public static final String COLUMNNAME_AD_Sequence_ID = "AD_Sequence_ID";

	/** Set Sequence.
	  * Document Sequence
	  */
	public void setAD_Sequence_ID (int AD_Sequence_ID);

	/** Get Sequence.
	  * Document Sequence
	  */
	public int getAD_Sequence_ID();

	public org.compiere.model.I_AD_Sequence getAD_Sequence() throws RuntimeException;

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

    /** Column name LMX_Certificate_ID */
    public static final String COLUMNNAME_LMX_Certificate_ID = "LMX_Certificate_ID";

	/** Set Localization México Certificate.
	  * Localization México Certificate
	  */
	public void setLMX_Certificate_ID (int LMX_Certificate_ID);

	/** Get Localization México Certificate.
	  * Localization México Certificate
	  */
	public int getLMX_Certificate_ID();

	public org.eevolution.LMX.model.I_LMX_Certificate getLMX_Certificate() throws RuntimeException;

    /** Column name LMX_CertificateLine_ID */
    public static final String COLUMNNAME_LMX_CertificateLine_ID = "LMX_CertificateLine_ID";

	/** Set MX Certificate Lines.
	  * MX Certificate Lines
	  */
	public void setLMX_CertificateLine_ID (int LMX_CertificateLine_ID);

	/** Get MX Certificate Lines.
	  * MX Certificate Lines
	  */
	public int getLMX_CertificateLine_ID();

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
