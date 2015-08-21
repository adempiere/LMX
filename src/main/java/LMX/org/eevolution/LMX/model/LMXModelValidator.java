/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2003-2014 e-Evolution Consultants All Rights Reserved.       *
 * This program is free software; you can redistribute it and/or              *
 * modify it under the terms of the GNU General Public License                *
 * as published by the Free Software Foundation; either version 2             *
 * of the License, or (at your option) any later version.                     *
 * This program is distributed in the hope that it will be useful,            *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                       *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *                                                                            *
 * @author victor.perez@e-evolution.com, www.e-evolution.com                  *
 *****************************************************************************/
package org.eevolution.LMX.model;


import org.compiere.model.MAttachment;
import org.compiere.model.MAttachmentNote;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MSequence;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.eevolution.LMX.process.LMXCFDI;
import org.eevolution.model.MHRPaySelectionCheck;

import java.sql.Timestamp;
import java.util.List;


/**
 *	LiberoValidator 
 *	
 *	@author Victor Perez
 *  Model Validator for Mexico Location
 */
public class LMXModelValidator implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instanced when logging in and client is selected/known
	 */
	public LMXModelValidator()
	{
		super ();
	}	//	LiberoValidator
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(LMXModelValidator.class);
	/** Client			*/
	private int		m_AD_Client_ID = -1;
	
	
	/**
	 *	Initialize Validation
	 *	@param engine validation engine 
	 *	@param client client
	 */
	public void initialize (ModelValidationEngine engine, MClient client)
	{

		//client = null for global validator
		if (client != null) {	
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		}
		else  {
			log.info("Initializing global validator: "+this.toString());
		}
		System.err.println("======================================= monitor");
		//	Tables to be monitored
		engine.addModelChange(MInvoice.Table_Name, this);
		engine.addDocValidate(MInvoice.Table_Name, this);
		//engine.addDocValidate(MInOut.Table_Name, this);
		engine.addModelChange(MBPartner.Table_Name, this);
		engine.addModelChange(MHRPaySelectionCheck.Table_Name, this);
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	Called after PO.beforeSave/PO.beforeDelete
     *	when you called addModelChange for the table
     *	@param po persistent object
     *	@param type TYPE_
     * @param MHRPaySelectionCheck 
     *	@return error message or null
     *	@exception Exception if the recipient wishes the change to be not accept.
     */
	public String modelChange (PO po, int type) throws Exception
	{			
		
		if (po.get_TableName().equals("C_Invoice") && type == this.TYPE_BEFORE_NEW)
		{
			MInvoice invoice = (MInvoice)po;
			// Begin e-evolution ogi-cd Save to Date/hour/minute/second in to DateInvoiced
			if(invoice.getDocAction() == "CL" && invoice.getDocStatus() == "CO")
				invoice.setDateInvoiced(new Timestamp(System.currentTimeMillis()));
			if(invoice.getDocAction() == "" && invoice.getDocStatus() == "")
				invoice.setDateInvoiced(new Timestamp(System.currentTimeMillis()));
		}
		else if(po.get_TableName().equals("HR_PaySelectionCheck") && (type == this.TYPE_AFTER_CHANGE || type == this.TYPE_AFTER_NEW))
		{
			
			MHRPaySelectionCheck psck = (MHRPaySelectionCheck) po;
			String whereClause = MLMXDocument.COLUMNNAME_AD_Table_ID + "=? AND " +
								 MLMXDocument.COLUMNNAME_Record_ID + "=? AND " + 
								 MLMXDocument.COLUMNNAME_IsCancelled + "<>?";
			
			
			int stampCount = new Query(po.getCtx(), MLMXDocument.Table_Name, whereClause, po.get_TrxName())
			.setOnlyActiveRecords(true)
			.setParameters(MHRPaySelectionCheck.Table_ID, po.get_ID(), 'Y')
			.count();
								
			
			if(psck.getC_Payment_ID()>0 && stampCount==0)
			{
				LMXCFDI cfdi = LMXCFDI.get();
				cfdi.setPaySelection(psck);
				cfdi.generate();
			}
		}
		
		return null;
	}	//	modelChange
	
	/**
	 *	Validate Document.
	 *	Called as first step of DocAction.prepareIt 
     *	when you called addDocValidate for the table.
     *	Note that totals, etc. may not be correct.
	 *	@param po persistent object
	 *	@param timing see TIMING_ constants
     *	@return error message or null
	 */
	public String docValidate (PO po, int timing)
	{
		if (timing == this.TIMING_AFTER_COMPLETE) {
			if (MInvoice.Table_Name.equals(po.get_TableName()))
			{
				/**	Order Discount Example	**/
				MInvoice invoice = (MInvoice)po;
				if(invoice.isSOTrx())
				{	
					LMXCFDI cdfdi = LMXCFDI.get();
					cdfdi.setInvoice(invoice);
					cdfdi.generate();
				}
			}
		}
		
		
		if (timing == this.TIMING_AFTER_REVERSECORRECT) {
			if (po.get_TableName().equals(MInvoice.Table_Name))
			{
				/**	Order Discount Example	**/
				MInvoice invoice = (MInvoice)po;
				if(invoice.isSOTrx())
				{	
				MInvoice reversal = new MInvoice(invoice.getCtx(), invoice.getReversal_ID(), po.get_TrxName());
				reversal.setDocumentNo(invoice.getDocumentNo() + "-" + reversal.getC_Invoice_ID());
				invoice.setDocumentNo(invoice.getDocumentNo()+"+"+invoice.getC_Invoice_ID());				
				invoice.setDescription("(" + invoice.getDocumentNo() + ") <- (" + reversal.getDocumentNo() + ")");
				invoice.saveEx();
				reversal.setDescription("(" +reversal.getDocumentNo() + ") -> ("+invoice.getDocumentNo()+")");
				reversal.saveEx();

				MDocType  eDocType = MDocType.get(invoice.getCtx(),invoice.getC_DocType_ID());
				MSequence eSeq     = new MSequence(invoice.getCtx(),eDocType.getDocNoSequence_ID(),invoice.get_TrxName());

				eSeq.setCurrentNext(eSeq.getCurrentNext() -1);
				eSeq.save();
				/** Order Discount Example */
				log.info(po.toString());
				}
			}
		}
		return null;
	}	//	docValidate
	
	
	
	/**
	 *	User Login.
	 *	Called when preferences are set
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	public String login (int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		log.info("AD_User_ID=" + AD_User_ID);
		return null;
	}	//	login

	
	/**
	 *	Get Client to be monitored
	 *	@return AD_Client_ID client
	 */
	public int getAD_Client_ID()
	{
		return m_AD_Client_ID;
	}	//	getAD_Client_ID

	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("LiberoValidator");
		return sb.toString ();
	}	//	toString

}	//	LMXModelValidator.java
