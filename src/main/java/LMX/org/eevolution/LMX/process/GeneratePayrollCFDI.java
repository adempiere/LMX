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

package org.eevolution.LMX.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MPayment;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.compiere.util.TrxRunnable;
import org.eevolution.LMX.model.MLMXDocument;
import org.eevolution.model.I_HR_PaySelection;
import org.eevolution.model.I_HR_PaySelectionCheck;
import org.eevolution.model.MHRPaySelectionCheck;

import java.util.List;

/**
 * Generate CFDI for Payroll Employee Payment
 */
public class GeneratePayrollCFDI extends SvrProcess {

	protected int payrollPaySelectionId;

	/**
	 * Prepare - e.g., get Parameters.
	 */
	protected void prepare() {
		for (ProcessInfoParameter para : getParameter()) {
			if (para.getParameter() == null)
				;
			else if (I_HR_PaySelection.COLUMNNAME_HR_PaySelection_ID.equals(para
					.getParameterName()))
				payrollPaySelectionId = para.getParameterAsInt();
		}
	}

	@Override
	protected String doIt() throws Exception {

		for (MHRPaySelectionCheck paySelectionCreateCheck : getPayments(payrollPaySelectionId)) {

			if (paySelectionCreateCheck.getC_Payment_ID() == 0)
			{
				String employeeName = DB.getSQLValueString(get_TrxName() , "SELECT Name FROM C_BPartner WHERE C_BPartner_ID=?", paySelectionCreateCheck.getC_BPartner_ID());
				String employeeValue = DB.getSQLValueString(get_TrxName() , "SELECT Value FROM C_BPartner WHERE C_BPartner_ID=?", paySelectionCreateCheck.getC_BPartner_ID());
				addLog("@C_PaySelection_ID@ : " + paySelectionCreateCheck.getHR_PaySelection().getName() + " @Value@ : " + employeeValue + " @HR_Employee_ID@ : " + employeeName + " @C_Payment_ID@  @NotFound@");
				continue;
			}


			Trx.run(new TrxRunnable()
			{
				private int paymentId;
				public TrxRunnable setParameters(int paymentId) {
					this.paymentId = paymentId;
					return this;
				}

				public void run(String trxName)
				{
					MPayment payment = new MPayment(Env.getCtx(), paymentId , trxName);
					if (payment == null)
						throw new AdempiereException("@C_Payment_ID@ @NotFound@");

					if (MLMXDocument.isProcessed(payment))
						return;
					String employeeName = DB.getSQLValueString(get_TrxName() , "SELECT Name FROM C_BPartner WHERE C_BPartner_ID=?", payment.getC_BPartner_ID());
					String employeeValue = DB.getSQLValueString(get_TrxName() , "SELECT Value FROM C_BPartner WHERE C_BPartner_ID=?", payment.getC_BPartner_ID());
					try {
						LMXCFDI cdfdi = LMXCFDI.get();
						cdfdi.setDocument(payment);
						MLMXDocument document = cdfdi.generate();
						String uuid = document.getCFDIUUID();
						addLog("@Value@ : " + employeeValue + " @HR_Employee_ID@ : " + employeeName + " @LMX_Document_ID@ : " + uuid + " @CFDISealingDate@ " + document.getCFDISealingDate());
					}
					catch (Exception e)
					{
						addLog("@Value@ : " + employeeValue + " @HR_Employee_ID@ : " + employeeName + " @Error@ " + e.getMessage());
					}
				}
			}.setParameters(paySelectionCreateCheck.getC_Payment_ID()));
		}
		return "@OK@";
	}

	private List<MHRPaySelectionCheck> getPayments(int payrollPaySelectionId) {
		StringBuilder whereClause = new StringBuilder();
		whereClause
				.append(I_HR_PaySelection.COLUMNNAME_HR_PaySelection_ID).append("=?");
		return new Query(getCtx(), I_HR_PaySelectionCheck.Table_Name, whereClause
				.toString(), get_TrxName())
				.setClient_ID()
				.setParameters(payrollPaySelectionId)
				.list();
	}

}
