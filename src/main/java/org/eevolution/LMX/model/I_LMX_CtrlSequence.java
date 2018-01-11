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

/** Generated Interface for LMX_CtrlSequence
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_LMX_CtrlSequence 
{

    /** TableName=LMX_CtrlSequence */
    public static final String Table_Name = "LMX_CtrlSequence";

    /** AD_Table_ID=1000001 */
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

    /** Column name C_DocType_ID */
    public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

	/** Set Document Type.
	  * Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID);

	/** Get Document Type.
	  * Document type or rules
	  */
	public int getC_DocType_ID();

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException;

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

    /** Column name LMX_CtrlSequence_ID */
    public static final String COLUMNNAME_LMX_CtrlSequence_ID = "LMX_CtrlSequence_ID";

	/** Set LMS_CtrlSequence	  */
	public void setLMX_CtrlSequence_ID (int LMX_CtrlSequence_ID);

	/** Get LMS_CtrlSequence	  */
	public int getLMX_CtrlSequence_ID();

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

    /** Column name autorizationnumber */
    public static final String COLUMNNAME_autorizationnumber = "autorizationnumber";

	/** Set Autorization Number	  */
	public void setautorizationnumber (String autorizationnumber);

	/** Get Autorization Number	  */
	public String getautorizationnumber();

    /** Column name daterequest */
    public static final String COLUMNNAME_daterequest = "daterequest";

	/** Set Date Request	  */
	public void setdaterequest (Timestamp daterequest);

	/** Get Date Request	  */
	public Timestamp getdaterequest();

    /** Column name sequencebegin */
    public static final String COLUMNNAME_sequencebegin = "sequencebegin";

	/** Set Sequence Begin	  */
	public void setsequencebegin (int sequencebegin);

	/** Get Sequence Begin	  */
	public int getsequencebegin();

    /** Column name sequencectrlno */
    public static final String COLUMNNAME_sequencectrlno = "sequencectrlno";

	/** Set Sequence Ctrl No	  */
	public void setsequencectrlno (String sequencectrlno);

	/** Get Sequence Ctrl No	  */
	public String getsequencectrlno();

    /** Column name sequenceend */
    public static final String COLUMNNAME_sequenceend = "sequenceend";

	/** Set Sequence End	  */
	public void setsequenceend (int sequenceend);

	/** Get Sequence End	  */
	public int getsequenceend();

    /** Column name sequencerange */
    public static final String COLUMNNAME_sequencerange = "sequencerange";

	/** Set Sequence Range	  */
	public void setsequencerange (int sequencerange);

	/** Get Sequence Range	  */
	public int getsequencerange();
}
