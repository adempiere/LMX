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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.compiere.model.I_C_Invoice;
import org.compiere.model.MInvoice;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

public class RegenerateCFDI extends SvrProcess {

	protected Timestamp pDateFrom;
	protected Timestamp pDateTo;
	protected int pDocTypeId;
	protected int pInvoiceId;

	/**
	 * Prepare - e.g., get Parameters.
	 */
	protected void prepare() {
		for (ProcessInfoParameter para : getParameter()) {

			String name = para.getParameterName();
			if (para.getParameter() == null)
				;
			else if (I_C_Invoice.COLUMNNAME_C_Invoice_ID.equals(para
					.getParameterName()))
				pInvoiceId = para.getParameterAsInt();
			else if (I_C_Invoice.COLUMNNAME_DateAcct.equals(para
					.getParameter_ToAsBoolean())) {
				pDateFrom = (Timestamp) para.getParameter();
				pDateTo = (Timestamp) para.getParameter_To();
			} else if (I_C_Invoice.COLUMNNAME_C_DocType_ID.equals(para
					.getParameterName()))
				pDocTypeId = para.getParameterAsInt();

		}
	}

	@Override
	protected String doIt() throws Exception {

		for (MInvoice invoice : getDocuments(pInvoiceId , pDateFrom, pDateTo, pDocTypeId)) {
			cancelCFDI(invoice);
			generateCFDI(invoice);
		}

		return "@OK@";
	}

	private void generateCFDI(PO po) {
		if (po.get_ValueAsBoolean("IsSOTrx")) {
			LMXCFDI cdfdi = LMXCFDI.get();
			cdfdi.setDocument(po);
			cdfdi.generate();
		}
	}

	private void cancelCFDI(PO po) {
		LMXCFDI cdfdi = LMXCFDI.get();
		cdfdi.setDocument(po);
		cdfdi.cancel(po);

	}

	private List<MInvoice> getDocuments(int invoiceId, Timestamp from,
			Timestamp to, int docTypeId) {
		StringBuilder whereClause = new StringBuilder();
		List<Object> parameters = new ArrayList<Object>();
		if (invoiceId > 0) {
			whereClause.append(I_C_Invoice.COLUMNNAME_C_Invoice_ID)
					.append("=?");
			parameters.add(invoiceId);
		} else {

			whereClause.append(I_C_Invoice.COLUMNNAME_DateAcct).append(
					">= ? AND");
			whereClause.append(I_C_Invoice.COLUMNNAME_DateAcct).append(
					"<=? AND ");
			whereClause.append(I_C_Invoice.COLUMNNAME_C_DocType_ID)
					.append("=?");
			parameters.add(from);
			parameters.add(to);
			parameters.add(docTypeId);
		}

		return new Query(getCtx(), I_C_Invoice.Table_Name, whereClause
				.toString(), get_TrxName()).setClient_ID().setParameters(
						parameters).list();
	}

}
