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

import org.compiere.model.I_C_Payment;
import org.compiere.model.MPayment;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.eevolution.LMX.model.MLMXDocument;
import org.eevolution.model.I_HR_PaySelection;

import java.sql.Timestamp;
import java.util.List;

public class GenerateInvoiceCFDI extends SvrProcess {

	protected Timestamp payDate;
	protected int payrollProcessId;

	/**
	 * Prepare - e.g., get Parameters.
	 */
	protected void prepare() {
		for (ProcessInfoParameter para : getParameter()) {

			String name = para.getParameterName();
			if (para.getParameter() == null)
				;
			else if (I_HR_PaySelection.COLUMNNAME_HR_Process_ID.equals(para
					.getParameterName()))
				payrollProcessId = para.getParameterAsInt();
			else if (I_HR_PaySelection.COLUMNNAME_PayDate.equals(para.getParameter_ToAsBoolean()))
				payDate = (Timestamp) para.getParameter();
		}
	}

	@Override
	protected String doIt() throws Exception {

		for (MPayment payment : getPayments(payrollProcessId)) {
			String employeeName =  payment.getC_BPartner().getName();
			try {
				LMXCFDI cdfdi = LMXCFDI.get();
				cdfdi.setDocument(payment);
				MLMXDocument document = cdfdi.generate();
				String uuid = document.getCFDIUUID();
				addLog("@HR_Employee_ID@ : " + employeeName + " @LMX_Document_ID@ : " + uuid +  " @CFDISealingDate@ " +document.getCFDISealingDate());
			}
			catch (Exception e)
			{
				addLog("@HR_Employee_ID@ : " + employeeName + " @Error@ :" + e.getMessage());
			}
		}
		return "@OK@";
	}

	private List<MPayment> getPayments(int payrollProcessId) {
		StringBuilder whereClause = new StringBuilder();
		whereClause
				.append("EXISTS (SELECT 1 FROM HR_PaySelection ps INNER JOIN HR_PaySelectionCheck psc ON (ps.HR_PaySelection_ID=psc.HR_PaySelection_ID))")
				.append("WHERE ps.HR_Process_ID = ? AND C_Payment.C_Paymnet_ID = psc.C_Paymnet_ID)");
		return new Query(getCtx(), I_C_Payment.Table_Name, whereClause
				.toString(), get_TrxName())
				.setClient_ID()
				.setParameters(payrollProcessId)
				.list();
	}

}
