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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.adempiere.exceptions.AdempiereException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.compiere.model.*;
import org.compiere.process.DocAction;
import org.compiere.util.*;
import org.eevolution.LMX.engine.LMXVendorEngine;
import org.eevolution.LMX.engine.LMXVendorInterface;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.eevolution.LMX.model.*;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import sun.misc.BASE64Encoder;


public final class LMXCFDI {

	private MLMXCertificate certificate = null;
	private LMXVendorInterface service = null;
	private MLMXTax taxInfo = null;
	private MLMXAddenda addendaInfo = null;
	private String CFDI = null;

	private static LMXCFDI instance = null;

	private static InputStream CFDI_XSLT = null;
	private static InputStream CFDI_STRING_XSLT = null;
	private static InputStream CFDI_ADDENDA_XSLT = null;
	private static InputStream CFDI_SCHEMA = null;
	private static InputStream PKCS12_CER = null;

	private static PO document  = null;
	private static MLMXDocument documentCFDI = null;
    private static KeyStore keyStore = null;
    private static String stringCFDI = "";


	/**
	 * get WMRule Engine like singleton instance
	 * 
	 * @return WM Rule Engine
	 */
	public static LMXCFDI get() {
		if (instance == null)
			instance = new LMXCFDI();
		return instance;
	}

	public LMXCFDI() {
		certificate = MLMXCertificate.get();
	}

	public void setDocument(PO po) {

		try {
			this.document = po;
			I_C_DocType docType = null;

			int docTypeId = document.get_ValueAsInt(I_C_DocType.COLUMNNAME_C_DocType_ID);
			docType = MDocType.get(document.getCtx() , docTypeId);
			if (docType == null)
				throw  new AdempiereException("El documento no valido");

			taxInfo = MLMXTax.getTax(document.getCtx(), document.getAD_Org_ID(), document.get_TrxName());
			if (taxInfo == null)
				throw new AdempiereException("No existe infomración de la compañia para el timbrado");

			if (addendaInfo == null)
				addendaInfo = MLMXAddenda.getByBPartnerId(document.getCtx(), taxInfo.getC_BPartner_ID(), document.get_TrxName());

			MLMXAddenda addInfoCustomer = MLMXAddenda.getByBPartnerId(
					document
					.getCtx(), document.get_ValueAsInt(I_C_BPartner.COLUMNNAME_C_BPartner_ID), document
					.get_TrxName());
			if (addInfoCustomer != null)
				addendaInfo = addInfoCustomer;

			if (addendaInfo == null)
				throw new AdempiereException("No hay esquema para comprobante Fiscal Digital");

			LMXVendorEngine engine = LMXVendorEngine.get();
			I_LMX_Vendor vendor = certificate.getVendorService(docType);
			if (vendor == null)
				throw new AdempiereException("No existe una implemetnación un PAC para el tipo de documento " + docType.getName());

			service = engine.getLMXVendorFactory(vendor.getClassname());
			if (service == null)
				throw new AdempiereException("No existe ningun integrador");

			service.setDocument(po);

			if (certificate.getPKCS12_ID() > 0) {
				PKCS12_CER = new ByteArrayInputStream(MImage.get(Env.getCtx(), certificate.getPKCS12_ID()).getData());
				setKeyStore(PKCS12_CER, certificate.getPassword());
				if (keyStore == null)
					throw new AdempiereException("La Llave PKCS12 no corresponde a la Clave del Certificado");
			}

			CFDI_SCHEMA = new ByteArrayInputStream(MImage.get(Env.getCtx(), addendaInfo.getCFDISchema_ID()).getData());
			//	ADempiere Transformera
			CFDI_XSLT = new ByteArrayInputStream(MImage.get(Env.getCtx(), addendaInfo.getCFDIADTransformer_ID()).getData());
			// CFDI String Transformer
			CFDI_STRING_XSLT = new ByteArrayInputStream(MImage.get(Env.getCtx(), addendaInfo.getCFDITransformerString_ID()).getData());
			//	CFDI Transformer
			CFDI_ADDENDA_XSLT = new ByteArrayInputStream(MImage.get(Env.getCtx(), addendaInfo.getCFDITransformer_ID()).getData());

			if (CFDI_ADDENDA_XSLT == null)
				throw new AdempiereException("No hay esquema para Comprobante Fiscal Digital");

		} catch (Exception e) {
			throw new AdempiereException(e.getMessage());
		}

	}

	public MLMXDocument generate() {

		try {
			
			if (document.get_ColumnIndex(MInvoice.COLUMNNAME_Reversal_ID) > 0  && document.get_ValueAsInt(MInvoice.COLUMNNAME_Reversal_ID) > 0)
				return cancelCFDI(getReversal(document));
			else
				return createCFDI();

		} catch (Exception e) {
			throw new AdempiereException(e.getMessage());
		}

	}


	private PO getReversal(PO po) {
		return new MInvoice(po.getCtx(), po.get_ValueAsInt(I_C_Invoice.COLUMNNAME_Reversal_ID), po
				.get_TrxName());
	}

	public MLMXDocument cancelCFDI(PO reverseInvoice) throws Exception {
		documentCFDI = MLMXDocument.get(reverseInvoice);
		service.getCancelCFDI(documentCFDI);
		MAttachment attachment = new MAttachment(documentCFDI.getCtx(), documentCFDI.get_Table_ID(), documentCFDI.getLMX_Document_ID(), documentCFDI.get_TrxName());
		attachment.setTitle("Acuse de Recibo CFDI");
		String fileName = "Acuse de Recibo CFDI" + reverseInvoice.get_ValueAsString(I_C_Invoice.COLUMNNAME_DocumentNo);
		String CFDName = "CFD" + fileName +  ".xml";
		attachment.addEntry(CFDName, documentCFDI.getCFDIXML().getBytes("UTF-8"));
		attachment.addTextMsg(documentCFDI.getCFDIXML());
		attachment.saveEx();

		MAttachmentNote attachmentNote = new MAttachmentNote(documentCFDI.getCtx(), 0, documentCFDI.get_TrxName());
		attachmentNote.setAD_Attachment_ID(attachment.get_ID());
		attachmentNote.setAD_User_ID(100);
		attachmentNote.setTextMsg(documentCFDI.getCFDIXML());
		attachmentNote.setTitle(document.get_ValueAsString(I_C_Invoice.COLUMNNAME_DocumentNo));
		attachmentNote.saveEx();

		documentCFDI.setIsCancelled(true);
		documentCFDI.saveEx();
		
		return documentCFDI;

	}

	public static String getXMLFromReportEngine(PO po , int printFormatId) {
		MClient client = MClient.get(po.getCtx());
		Language language = client.getLanguage();
		// Get Format & Data
		MPrintFormat format = MPrintFormat.get(po.getCtx(), printFormatId , false);
		format.setLanguage(language);
		format.setTranslationLanguage(language);
		// query
		MQuery query = new MQuery(format.getAD_Table().getTableName());
		
		query.addRestriction(po.get_TableName() + "_ID", MQuery.EQUAL,
				po.get_ID());
		
		PrintInfo printInfo = new PrintInfo(po.get_ValueAsString("DocumentNo"),
				format.getAD_Table().getAD_Table_ID() , po.get_ID());
		printInfo.setCopies(1);
		printInfo.setDocumentCopy(false); // true prints "Copy" on second
		printInfo.setPrinterName(format.getPrinterName());

		// Engine
		ReportEngine reportEngine = new ReportEngine(po.getCtx(), format,
				query, printInfo, po.get_TrxName());
		if (reportEngine == null)
			throw new AdempiereException("@NotFound@ @M_PrintFormat_ID@");

		reportEngine.getLayout();

		final StringWriter stringWriter = new StringWriter();
		reportEngine.createXML(stringWriter);
		return stringWriter.getBuffer().toString();
	}

	public String getCFDI(PO po) throws TransformerException {
		String xmlReport = getXMLFromReportEngine(po, addendaInfo.getAD_PrintFormat_ID());
		String transformedCFDI = getXMLTransform(xmlReport);
		String documentCFDI = getAddendaTransform(transformedCFDI);
		return documentCFDI;
	}

	public String getXMLTransform(final String xmlReport) throws TransformerException {

		StringWriter transformedCFDI = new StringWriter();
		// Generate CFDI using transformer
		transform(new StreamSource(CFDI_XSLT), new StreamSource(
						new StringReader(xmlReport)),
				new StreamResult(transformedCFDI));
		return transformedCFDI.getBuffer().toString();
	}

	public String getAddendaTransform(final String CFDI) throws TransformerException {
		StringWriter transformedCFDI = new StringWriter();
		transform(new StreamSource(CFDI_ADDENDA_XSLT), new StreamSource(
				new StringReader(CFDI)), new StreamResult(transformedCFDI));
		return transformedCFDI.getBuffer().toString();
	}

	private MLMXDocument createCFDI() throws TransformerException, IOException,
			SAXException, URISyntaxException {

		documentCFDI = MLMXDocument.get(document);
		if (documentCFDI != null && documentCFDI.getCFDIUUID() != null && !documentCFDI.getCFDIUUID().isEmpty())
			return documentCFDI;
		else if (documentCFDI == null) {
			documentCFDI = new MLMXDocument(document.getCtx(), 0, document.get_TrxName());
			documentCFDI.setAD_Table_ID(document.get_Table_ID());
			documentCFDI.setRecord_ID(document.get_ID());
			documentCFDI.setTaxID(getRFC());
			documentCFDI.saveEx();
		}

		/*String fileName = null;

		if (I_C_Invoice.Table_Name.equals(document.get_TableName()))
			fileName = document.get_ValueAsString("DocumentNo");
		if (I_C_Payment.Table_Name.equals(document.get_TableName())) {
			String taxId = DB.getSQLValueString(document.get_TrxName(),
					"SELECT TaxID  FROM C_BPartner bp WHERE C_BPartner_ID=?", document.get_ValueAsInt(I_C_BPartner.COLUMNNAME_C_BPartner_ID));
			String documentNo = DB.getSQLValueString(document.get_TrxName(),
					"SELECT DocumentNo FROM C_Payment p WHERE p.C_Payment_ID = ?" , document.get_ValueAsInt(I_C_Payment.COLUMNNAME_C_Payment_ID));
			fileName = taxId + documentNo;
		}*/

		String xmlCFDI = "";
		
		if(document != null)
			xmlCFDI = generateCFDIWithStamp(getCFDI(document));

		if(document!=null && I_C_Invoice.Table_Name.equals(document.get_TableName()))
		{
			// Schema Validation
			String schemaLang = "http://www.w3.org/2001/XMLSchema";
			// get validation driver:
			SchemaFactory schema_factory = SchemaFactory.newInstance(schemaLang);
			// create schema by reading it from an XSD file:
			Schema schema = schema_factory.newSchema(new StreamSource(CFDI_SCHEMA));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new StringReader(xmlCFDI)));
		}

		if ("".equals(xmlCFDI) || xmlCFDI == null)
			throw new AdempiereException("El CFDI no es Válido");

		stringCFDI = getOriginalString(xmlCFDI);
		documentCFDI.setCFDIString(stringCFDI);
		documentCFDI.setCFDIXML(xmlCFDI);
		service.getCFDI(documentCFDI);
		service.getQR(documentCFDI);
		MAttachment attachment = new MAttachment(documentCFDI.getCtx(), documentCFDI
				.get_Table_ID(), documentCFDI.getLMX_Document_ID(), documentCFDI
				.get_TrxName());
		attachment.setTitle("CFD");
		String CFDName = "CFD" + documentCFDI.getCFDIUUID() +  ".xml";
		attachment.addEntry(CFDName, getCFDI()
				.getBytes("UTF-8"));
		attachment.addTextMsg(stringCFDI);
		attachment.saveEx();

		MAttachmentNote attachmentNote = new MAttachmentNote(documentCFDI.getCtx(), 0,
				documentCFDI.get_TrxName());
		attachmentNote.setAD_Attachment_ID(attachment.get_ID());
		attachmentNote.setAD_User_ID(100);
		attachmentNote.setTitle(documentCFDI.getCFDIUUID());
		attachmentNote.setTextMsg(getCFDI());
		attachmentNote.saveEx();
		return documentCFDI;
	}

	public static void main(String[] args) throws Exception {

		Ini.setProperty(Ini.P_UID, "SuperUser");
		Ini.setProperty(Ini.P_PWD, "System");
		Ini.setProperty(Ini.P_ROLE, "AHVR admin");
		Ini.setProperty(Ini.P_CLIENT, "Aldo Hugo Vargas Rodríguez");
		Ini.setProperty(Ini.P_ORG, "Matriz");
		Ini.setProperty(Ini.P_WAREHOUSE, "Estándar");
		Ini.setProperty(Ini.P_LANGUAGE, "English");

		org.compiere.Adempiere.startup(true);
		Login login = new Login(Env.getCtx());
		login.batchLogin();

		//List<MInvoice> invoices = new Query(Env.getCtx(), MInvoice.Table_Name,
		//		"DocStatus = 'CO'  AND CFDIUUID IS NOT NULL AND SendEMail='N'",
		//		null).setClient_ID().setOrderBy("DateInvoiced").list();
		
		List<MInvoice> invoices = new Query(Env.getCtx(), MInvoice.Table_Name,
				"DocumentNo='B1195'",
				null).setClient_ID().setOrderBy("DateInvoiced").list();
		
		for (MInvoice invoice : invoices) {
			System.out.println("Factura ... "
					+ invoice.getDocumentNo());
			//regenerate(invoice);
			LMXCFDI cdfdi = LMXCFDI.get();
			cdfdi.setDocument(invoice);
			cdfdi.generate();

			System.out.println("Factura procesada ... "
					+ invoice.getDocumentNo());
		}
		
		
		/*List<MInvoice> invoices = new Query(Env.getCtx(), MInvoice.Table_Name,
		"DocStatus = 'RE'  AND CFDIUUID IS NOT NULL ",
		null).setClient_ID().setOrderBy("DateInvoiced").list();

		for (MInvoice invoice : invoices) {
			System.out.println("Factura Cancelación processado ... "
					+ invoice.getDocumentNo());
			cancel(invoice);
			System.out.println("Factura Cancelación processada ... "
					+ invoice.getDocumentNo());
		}*/
	}

	public static void cancel(PO invoice) {
		try {
			LMXCFDI cdfdi = LMXCFDI.get();
			cdfdi.setDocument(invoice);
			cdfdi.cancelCFDI((MInvoice) cdfdi.getReversal(invoice));
		} catch (Exception e) {
			throw new AdempiereException(e.getMessage());
		}	
			
	}
	public static void regenerate(PO po)
			throws TransformerException, IOException {
		String fileName = null;

		if (I_C_Invoice.Table_Name.equals(document.get_TableName()))
			fileName = document.get_ValueAsString("DocumentNo");
		if (I_C_PaySelectionCheck.Table_Name.equals(document.get_TableName())) {
			String taxId = DB.getSQLValueString(document.get_TrxName(),
					"SELECT TaxID  FROM C_BPartner bp WHERE C_BPartner_ID", document.get_ValueAsInt(I_C_BPartner.COLUMNNAME_C_BPartner_ID));
			String documentNo = DB.getSQLValueString(document.get_TrxName(),
					"SELECT DocumentNo FROM C_Payment p WHERE p.C_Payment_ID = ?" , document.get_ValueAsInt(I_C_Payment.COLUMNNAME_C_Payment_ID));
			fileName = taxId + documentNo;
		}

		LMXCFDI cdfdi = LMXCFDI.get();
		cdfdi.setDocument(po);
		cdfdi.getCFDIXML();

		String CFDI = cdfdi.getCFDI();
		if (CFDI == null)
			return;

		MImage image = new MImage(documentCFDI.getCtx(), documentCFDI.getCFDIQR_ID(), null);
		if (image != null)
			image.deleteEx(true);

		//service.generateQR(documentCFDI);

		MAttachment attachment = new Query(documentCFDI.getCtx(), MAttachment.Table_Name,
				"AD_Table_ID=? AND Record_ID=?", null).setClient_ID()
				.setParameters(documentCFDI.get_Table_ID(),
						documentCFDI.getLMX_Document_ID()).first();
		if (attachment != null) {
			for (int i = 0; i <= attachment.getEntries().length; i = i + 1) {
				attachment.deleteEntry(i);
			}
		} else
			attachment = new MAttachment(documentCFDI.getCtx(), documentCFDI.get_Table_ID(),
					documentCFDI.getLMX_Document_ID(), documentCFDI.get_TrxName());

		attachment.setTitle("CFD");
		String CFDName = "CFD" + fileName +  ".xml";
		attachment.addEntry(CFDName, cdfdi
				.getCFDI().getBytes("UTF-8"));
		attachment.addTextMsg(cdfdi.getOriginalString(cdfdi.getCFDI()));
		attachment.saveEx();

		MAttachmentNote attachmentNote = new Query(documentCFDI.getCtx(),
				MAttachmentNote.Table_Name, "AD_Attachment_ID=?", null)
				.setClient_ID().setParameters(attachment.getAD_Attachment_ID())
				.first();

		if (attachmentNote == null)
			attachmentNote = new MAttachmentNote(documentCFDI.getCtx(), 0, documentCFDI
					.get_TrxName());

		attachmentNote.setAD_Attachment_ID(attachment.get_ID());
		attachmentNote.setAD_User_ID(100);
		attachmentNote.setTitle(fileName);
		attachmentNote.saveEx();
	}



	private void getCFDIXML() {

		service.getCFDI(documentCFDI);
	}

	private void updateDate(String CFDI) {
		try {
			String date = CFDI.substring(CFDI.indexOf("fecha=") + 7, CFDI
					.indexOf("\" xsi:schemaLocation="));
			date = date.replace("T", " ");
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
			Date newDate = (Date) formatter.parse(date);
			Timestamp dateDocument = new Timestamp(newDate.getTime());
			if (document instanceof MInvoice) {
				document.set_ValueOfColumn(I_C_Invoice.COLUMNNAME_DateInvoiced , dateDocument);
				document.set_ValueOfColumn(I_C_Invoice.COLUMNNAME_DateAcct, dateDocument);
				document.saveEx();
			}
			documentCFDI.setCFDISealingDate(date);
			documentCFDI.saveEx();
		} catch (Exception e) {
			throw new AdempiereException(e);
		}
	}

	private Document getDocument(String xml) {
		DocumentBuilderFactory docfactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = docfactory.newDocumentBuilder();
			return builder.parse(new InputSource(new StringReader(xml)));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getElement(String xml, String key) {
		Document document = getDocument(xml);
		return document.getElementsByTagName(key).item(0).getTextContent();
	}

	private String getUUID() {
		
		/*return CFDI.substring(CFDI.indexOf("UUID=") + 6, CFDI
		.indexOf("\" FechaTimbrado="));*/
		
		String tmpString = CFDI.substring(CFDI.indexOf("UUID=") + 6);
		return tmpString.substring(0, tmpString.indexOf("\""));
	}

	private String getSeal() {
		
		/*return CFDI.substring(CFDI.indexOf("sello=") + 7, CFDI
				.indexOf("\" certificado="));*/
		
		String tmpString = CFDI.substring(CFDI.indexOf("sello=") + 7);
		return tmpString.substring(0, tmpString.indexOf("\""));
	}

	private String getCFDI() {
		return documentCFDI.getCFDIXML();
	}

	private String getSealSAT() {

		String tmpSeal = CFDI.substring(CFDI.indexOf("selloSAT=") + 10);
		return tmpSeal.substring(0, tmpSeal.indexOf("\""));

	}

	private String getNoCertificateSAT() {
		/*return CFDI.substring(CFDI.indexOf("noCertificadoSAT=") + 18, CFDI
				.indexOf("\" selloSAT="));*/
		
		String tmpString = CFDI.substring(CFDI.indexOf("noCertificadoSAT=") + 18);
		return tmpString.substring(0, tmpString.indexOf("\""));
	}

	private String getDate() {
		
		/*String fecha = CFDI.substring(CFDI.indexOf("fecha=") + 7, CFDI
				.indexOf("\" version="));*/
		
		String fecha = CFDI.substring(CFDI.indexOf("fecha=") + 7);
		fecha = fecha.substring(0, fecha.indexOf("\""));
		fecha = fecha.replace("T", " ");
		return fecha;
	}

	private static String tmpAddenda = "<cfdi:Addenda/>";

	private static String cutAddenda(String xml) {
		if (xml.contains("<cfdi:Addenda>")) {
			tmpAddenda = xml.substring(xml.indexOf("<cfdi:Addenda>"), xml
					.indexOf("</cfdi:Addenda>") + 15);
			xml = xml.replace(tmpAddenda, "<cfdi:Addenda/>");
		}
		return xml;
	}

	private static String pasteAddenda(String xml) {
		xml = xml.replace("<cfdi:Addenda/>", tmpAddenda);
		return xml;
	}

	private static String tmpComplemento = "<cfdi:Complemento/>";

	private static String cutComplemento(String xml) {
		if (xml.contains("<cfdi:Complemento>")) {
			tmpComplemento = xml.substring(
					xml.indexOf("<cfdi:Complemento>") + 18, xml
							.indexOf("</cfdi:Complemento>") + 19);
			xml = xml.replace("<cfdi:Complemento>" + tmpComplemento,
					"<cfdi:Complemento/>");
		}
		return xml;
	}

	private static String pasteComplemento(String xml) {
		if (!(tmpComplemento.equals("<cfdi:Complemento/>")))

			xml = xml.replace("</cfdi:Complemento>", tmpComplemento);
		return xml;
	}

	

	// Old, do not remove
	public String generateCFDIWithStamp(String xml) {
		try {
			int ind = xml.indexOf("sello=\"") + 7;
			String fp = xml.substring(0, ind);
			String sp = xml.substring(ind, xml.length());
			ind = xml.indexOf("noCertificado=\"");
			fp = xml.substring(0, ind);
			sp = xml.substring(ind, xml.length());
			xml = fp + " certificado=\"\" " + sp;

			ind = xml.indexOf("noCertificado=\"") + 15;
			fp = xml.substring(0, ind);
			sp = xml.substring(ind, xml.length());
			xml = fp + certificate.getDocumentNo() + sp;
			return xml;
		} catch (Exception e) {
			throw new AdempiereException(
					"No se pudo generar el sello para este documento");
		}
	}

	private String getOriginalString(String CFDI) throws TransformerException,
			IOException {
		StringWriter transformed = new StringWriter();
		transform(new StreamSource(CFDI_STRING_XSLT), new StreamSource(
				new StringReader(CFDI)), new StreamResult(transformed));
		return transformed.getBuffer().toString();
	}

	public void transform(StreamSource xslt, StreamSource xml,
			StreamResult result /* File result */) throws TransformerException {
		TransformerFactory transFact = TransformerFactory.newInstance();
		Transformer trans = transFact.newTransformer(xslt);
		trans.setOutputProperty(OutputKeys.ENCODING, "UTF8");
		StreamResult xmlResult = result;
		trans.transform(xml, xmlResult);
	}

	private MLMXTax getTax() {
		return taxInfo;
	}

	private String getRFC() {
		return getTax().getC_BPartner().getTaxID();
	}
	

	private void generateStamp(MLMXDocument documentCFD) {
		try {
			String xml = documentCFD.getCFDIXML();
            int ind = xml.indexOf("sello=\"") + 7;
            String fp = xml.substring(0, ind);
            String sp = xml.substring(ind, xml.length());
            String sello = getSignedChain(keyStore, stringCFDI, certificate.getPassword());
            xml = fp + sello.replaceAll("\n", "").replaceAll("\r", "") + sp;
            ind = xml.indexOf("certificado=\"") + 13;
            fp = xml.substring(0, ind);
            sp = xml.substring(ind, xml.length());
            xml = fp + getCertificateBASE64Encoder(keyStore).replaceAll("\n", "").replaceAll("\r", "") + sp;
	        documentCFD.setCFDIXML(xml);
	        documentCFD.saveEx();
	        
        } catch (Exception e) {
            throw new AdempiereException("No se pudo generar sellado en este momento");
        }
	}

    private static void setKeyStore(InputStream stream, String password) {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore ks;
        try {
            ks = KeyStore.getInstance("PKCS12");
            ks.load(stream, password.toCharArray()); 
            keyStore = ks;
        } catch (Exception e) {
        }
    }
    
    private static String getSignedChain(KeyStore keystore, String original, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchProviderException {
        String[] aliases = getAliasArray(keystore);
        String alias = null;
        for (int i = 0; i < aliases.length; i++) {
            alias = aliases[i];
        }

        try {
            PrivateKey priv = (PrivateKey) keystore.getKey(alias, password.toCharArray());
            byte[] binary_chain = original.getBytes("UTF8");
            Signature signature = Signature.getInstance("SHA1withRSA", "BC");
            signature.initSign(priv);
            signature.update(binary_chain);
            byte[] binary_signature = signature.sign();
            String signatureB64 = new BASE64Encoder().encode(binary_signature);
            return signatureB64;
        } catch (Exception e) {
        }
        return null;
    }
    
    public static String getCertificateBASE64Encoder(KeyStore ks) throws CertificateEncodingException {
        X509Certificate certificate = getCertificate(ks);
        byte[] binary_certificate = certificate.getEncoded();
        String signatureB64 = new BASE64Encoder().encode(binary_certificate);
        return signatureB64;
    }
    
    public static String[] getAliasArray(KeyStore keystore) {
        Enumeration e = null;
        try {
            // List the aliases 
            e = keystore.aliases();
        } catch (KeyStoreException err) {
        }
        Vector v = new Vector();
        while (e.hasMoreElements()) {
            String alias = (String) e.nextElement();
            v.addElement(alias);
        }
        String s[] = new String[v.size()];
        v.copyInto(s);
        return s;
    }
    
    public static X509Certificate getCertificate(KeyStore ks) {
        try {
            String alias = getKeyStoreAlias(ks);
            return (X509Certificate) ks.getCertificate(alias);
        } catch (KeyStoreException e) {
        }
        return null;
    }
    
    public static String getKeyStoreAlias(KeyStore s) {
        String[] aliases = getAliasArray(s);
        String alias = null;
        for (int i = 0; i < aliases.length; i++) {
            alias = aliases[i];
        }
        return alias;
    }

}