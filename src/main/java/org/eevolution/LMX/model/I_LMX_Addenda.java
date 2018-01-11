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

/** Generated Interface for LMX_Addenda
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0
 */
public interface I_LMX_Addenda 
{

    /** TableName=LMX_Addenda */
    public static final String Table_Name = "LMX_Addenda";

    /** AD_Table_ID=53904 */
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

    /** Column name AD_Column_ID */
    public static final String COLUMNNAME_AD_Column_ID = "AD_Column_ID";

	/** Set Column.
	  * Column in the table
	  */
	public void setAD_Column_ID (int AD_Column_ID);

	/** Get Column.
	  * Column in the table
	  */
	public int getAD_Column_ID();

	public org.compiere.model.I_AD_Column getAD_Column() throws RuntimeException;

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

    /** Column name AD_PrintFormat_ID */
    public static final String COLUMNNAME_AD_PrintFormat_ID = "AD_PrintFormat_ID";

	/** Set Print Format.
	  * Data Print Format
	  */
	public void setAD_PrintFormat_ID (int AD_PrintFormat_ID);

	/** Get Print Format.
	  * Data Print Format
	  */
	public int getAD_PrintFormat_ID();

	public org.compiere.model.I_AD_PrintFormat getAD_PrintFormat() throws RuntimeException;

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Business Partner .
	  * Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Business Partner .
	  * Identifies a Business Partner
	  */
	public int getC_BPartner_ID();

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name CFDIADTransformer_ID */
    public static final String COLUMNNAME_CFDIADTransformer_ID = "CFDIADTransformer_ID";

	/** Set CFDI ADempiere Transformer XSLT.
	  * CFDI ADempiere Transformer XSLT
	  */
	public void setCFDIADTransformer_ID (int CFDIADTransformer_ID);

	/** Get CFDI ADempiere Transformer XSLT.
	  * CFDI ADempiere Transformer XSLT
	  */
	public int getCFDIADTransformer_ID();

    /** Column name CFDISchema_ID */
    public static final String COLUMNNAME_CFDISchema_ID = "CFDISchema_ID";

	/** Set CFDI Schema XML.
	  * CFDI Schema XML
	  */
	public void setCFDISchema_ID (int CFDISchema_ID);

	/** Get CFDI Schema XML.
	  * CFDI Schema XML
	  */
	public int getCFDISchema_ID();

    /** Column name CFDITransformer_ID */
    public static final String COLUMNNAME_CFDITransformer_ID = "CFDITransformer_ID";

	/** Set CFDI Transformer XSLT.
	  * CFDI Transformer XSLT
	  */
	public void setCFDITransformer_ID (int CFDITransformer_ID);

	/** Get CFDI Transformer XSLT.
	  * CFDI Transformer XSLT
	  */
	public int getCFDITransformer_ID();

    /** Column name CFDITransformerString_ID */
    public static final String COLUMNNAME_CFDITransformerString_ID = "CFDITransformerString_ID";

	/** Set CFDI Transformer String XSLT.
	  * CFDI Transformer String XSLT
	  */
	public void setCFDITransformerString_ID (int CFDITransformerString_ID);

	/** Get CFDI Transformer String XSLT.
	  * CFDI Transformer String XSLT
	  */
	public int getCFDITransformerString_ID();

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

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

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

    /** Column name LMX_Addenda_ID */
    public static final String COLUMNNAME_LMX_Addenda_ID = "LMX_Addenda_ID";

	/** Set Información de la  addenda ID	  */
	public void setLMX_Addenda_ID (int LMX_Addenda_ID);

	/** Get Información de la  addenda ID	  */
	public int getLMX_Addenda_ID();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name Parent_Column_ID */
    public static final String COLUMNNAME_Parent_Column_ID = "Parent_Column_ID";

	/** Set Parent Column.
	  * The link column on the parent tab.
	  */
	public void setParent_Column_ID (int Parent_Column_ID);

	/** Get Parent Column.
	  * The link column on the parent tab.
	  */
	public int getParent_Column_ID();

	public org.compiere.model.I_AD_Column getParent_Column() throws RuntimeException;

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

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();
}
