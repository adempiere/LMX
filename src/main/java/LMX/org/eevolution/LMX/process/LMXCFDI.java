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
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.adempiere.exceptions.AdempiereException;
import org.apache.commons.lang.StringEscapeUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.compiere.model.MAttachment;
import org.compiere.model.MAttachmentNote;
import org.compiere.model.MClient;
import org.compiere.model.MImage;
import org.compiere.model.MInvoice;
import org.compiere.model.MPaySelectionCheck;
import org.compiere.model.MQuery;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Ini;
import org.eevolution.LMX.engine.LMXVendorEngine;
import org.eevolution.LMX.engine.LMXVendorInterface;
import org.compiere.model.PrintInfo;
import org.compiere.model.X_C_Invoice;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Login;
import org.eevolution.LMX.model.*;
import org.eevolution.LMX.util.SHA1;
import org.eevolution.model.MHRPaySelectionCheck;
import org.eevolution.model.MHRProcess;
import org.eevolution.model.X_HR_PaySelectionCheck;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public final class LMXCFDI {

	private MLMXCertificate certificate = null;
	private LMXVendorInterface service = null;
	private MLMXTax taxInfo = null;
	private MLMXAddenda addendaInfo = null;
	private String CFDI = null;

	private static LMXCFDI s_instance = null;

	private static InputStream CFDI_XSLT = null;
	private static InputStream CFDI_STRING_XSLT = null;
	private static InputStream CFDI_ADDENDA_XSLT = null;
	private static InputStream CFDI_SCHEMA = null;
	private static InputStream PKCS12_CER = null;

	private static MInvoice invoice = null;
	private static MHRPaySelectionCheck psck = null; 
	private static MHRProcess hrProcess = null;
	private static PO po = null;
	private static MLMXDocument documentCFDI = null;
    private static KeyStore m_Key_Store = null;
    private static String stringCFDI = "";


	/**
	 * get WMRule Engine like singleton instance
	 * 
	 * @return WM Rule Engine
	 */
	public static LMXCFDI get() {
		if (s_instance == null)
			s_instance = new LMXCFDI();
		return s_instance;
	}

	public LMXCFDI() {
		certificate = MLMXCertificate.get();
	}
	
	public void setPaySelection(MHRPaySelectionCheck po)
	{
		try
		{
			this.po = po;
			psck = po;
			
			taxInfo = MLMXTax.getTax(psck.getCtx(), psck.getAD_Org_ID(),
					psck.get_TrxName());
			if (taxInfo == null)
				throw new AdempiereException(
						"No existe infomración de la compañia");
			
			if (addendaInfo == null)
				addendaInfo = MLMXAddenda.getByBPartnerId(psck.getCtx(), taxInfo
						.getC_BPartner_ID(), psck.get_TrxName());
			
			if (addendaInfo == null)
				throw new AdempiereException(
						"No hay esquema para comprobante Fiscal Digital");
			
			LMXVendorEngine engine = LMXVendorEngine.get();

			hrProcess = (MHRProcess)psck.getHR_PaySelection().getHR_Process();
			
			I_LMX_Vendor vendor = certificate.getVendorService(hrProcess);
			if (vendor == null)
				return;
			
			service = engine.getLMXVendorFactory(vendor.getClassname());
			if (service == null)
				throw new AdempiereException("No existe ningun integrador");
			
			certificate = MLMXCertificate.get();
			
			PKCS12_CER = new ByteArrayInputStream(MImage.get(Env.getCtx(),
					certificate.getPKCS12_ID()).getData());
			
	        setKeyStore(PKCS12_CER, certificate.getPassword());

	        if (m_Key_Store == null) {
	            throw new AdempiereException("La Llave PKCS12 no corresponde a la Clave del Certificado");
	        }
			
			
			CFDI_SCHEMA = new ByteArrayInputStream(MImage.get(Env.getCtx(),
					addendaInfo.getCFDISchema_ID()).getData());

			CFDI_XSLT = new ByteArrayInputStream(MImage.get(Env.getCtx(),
					addendaInfo.getCFDITransformer_ID()).getData());
			
			CFDI_STRING_XSLT = new ByteArrayInputStream(MImage.get(
					Env.getCtx(), addendaInfo.getCFDITransformerString_ID())
					.getData());
			CFDI_ADDENDA_XSLT = new ByteArrayInputStream(MImage.get(
					Env.getCtx(), addendaInfo.getCFDISchema_ID()).getData());

			if (CFDI_ADDENDA_XSLT == null)
				throw new AdempiereException(
						"No hay esquema para Comprobante Fiscal Digital");
			
			
		}
		catch(Exception e)
		{
			throw new AdempiereException(e.getMessage());
		}
	}

	public void setInvoice(MInvoice po) {

		try {
			this.po = po;
			invoice = po;

			taxInfo = MLMXTax.getTax(invoice.getCtx(), invoice.getAD_Org_ID(),
					invoice.get_TrxName());
			if (taxInfo == null)
				throw new AdempiereException(
						"No existe infomración de la compañia");

			if (addendaInfo == null)
				addendaInfo = MLMXAddenda.getByBPartnerId(invoice.getCtx(), taxInfo
						.getC_BPartner_ID(), invoice.get_TrxName());

			MLMXAddenda addInfoCustomer = MLMXAddenda.getByBPartnerId(invoice
					.getCtx(), invoice.getC_BPartner_ID(), invoice
					.get_TrxName());
			if (addInfoCustomer != null)
				addendaInfo = addInfoCustomer;

			if (addendaInfo == null)
				throw new AdempiereException(
						"No hay esquema para comprobante Fiscal Digital");

			LMXVendorEngine engine = LMXVendorEngine.get();

			I_LMX_Vendor vendor = certificate.getVendorService(invoice);
			if (vendor == null)
				return;
			service = engine.getLMXVendorFactory(vendor.getClassname());
			if (service == null)
				throw new AdempiereException("No existe ningun integrador");

			CFDI_SCHEMA = new ByteArrayInputStream(MImage.get(Env.getCtx(),
					taxInfo.getCFDISchema_ID()).getData());

			CFDI_XSLT = new ByteArrayInputStream(MImage.get(Env.getCtx(),
					taxInfo.getCFDITransformer_ID()).getData());
			CFDI_STRING_XSLT = new ByteArrayInputStream(MImage.get(
					Env.getCtx(), taxInfo.getCFDITransformerString_ID())
					.getData());
			CFDI_ADDENDA_XSLT = new ByteArrayInputStream(MImage.get(
					Env.getCtx(), addendaInfo.getCFDITransformer_ID()).getData());

			if (CFDI_ADDENDA_XSLT == null)
				throw new AdempiereException(
						"No hay esquema para Comprobante Fiscal Digital");

		} catch (Exception e) {
			throw new AdempiereException(e.getMessage());
		}

	}

	public void generate() {

		try {
			
			if (invoice != null && invoice.isReversal()) {
				reverseInvoiceStamp((MInvoice) getReversal(invoice));

			} else
				createDocumentStamp();

		} catch (Exception e) {
			throw new AdempiereException(e.getMessage());
		}

	}


	private MInvoice getReversal(MInvoice invoice) {
		return new MInvoice(invoice.getCtx(), invoice.getReversal_ID(), invoice
				.get_TrxName());
	}

	public String reverseInvoiceStamp(MInvoice reverseInvoice)
			throws Exception {
		documentCFDI = MLMXDocument.get(reverseInvoice);
		service.getToken(documentCFDI, taxInfo.getPartnerID());
		final Source response = service.execute(documentCFDI,
				MLMXVendorService.SOAPSERVICETYPE_CancelStamp);
		String cancelXML = service.parse(response);
		MAttachment attachment = new MAttachment(documentCFDI.getCtx(), documentCFDI
				.get_Table_ID(), documentCFDI.getLMX_Document_ID(), documentCFDI
				.get_TrxName());
		attachment.setTitle("Acuse de Recibo CFDI");
		attachment.addEntry("Acuse de Recibo CFDI" + invoice.getDocumentNo() + ".xml", cancelXML
				.getBytes("UTF-8"));
		attachment.addTextMsg(cancelXML);
		attachment.saveEx();

		MAttachmentNote attachmentNote = new MAttachmentNote(documentCFDI.getCtx(), 0,
				documentCFDI.get_TrxName());
		attachmentNote.setAD_Attachment_ID(attachment.get_ID());
		attachmentNote.setAD_User_ID(100);
		attachmentNote.setTextMsg(cancelXML);
		attachmentNote.setTitle(invoice.getDocumentNo());
		attachmentNote.saveEx();

		documentCFDI.setIsCancelled(true);
		documentCFDI.saveEx();
		
		return cancelXML;

	}

	public static StringWriter getXMLFromReportEngine(MInvoice invoice , int printFormatId) {
		MClient client = MClient.get(invoice.getCtx());
		Language language = client.getLanguage();
		// Get Format & Data
		MPrintFormat format = MPrintFormat.get(invoice.getCtx(), printFormatId , false);
		format.setLanguage(language);
		format.setTranslationLanguage(language);
		// query
		MQuery query = new MQuery(X_C_Invoice.Table_Name);
		query.addRestriction(X_C_Invoice.COLUMNNAME_C_Invoice_ID, MQuery.EQUAL,
				invoice.getC_Invoice_ID());
		PrintInfo printInfo = new PrintInfo(invoice.getDocumentNo(),
				X_C_Invoice.Table_ID, invoice.getC_Invoice_ID(), invoice
						.getC_BPartner_ID());
		printInfo.setCopies(1);
		printInfo.setDocumentCopy(false); // true prints "Copy" on second
		printInfo.setPrinterName(format.getPrinterName());

		// Engine
		ReportEngine reportEngine = new ReportEngine(invoice.getCtx(), format,
				query, printInfo, invoice.get_TrxName());
		if (reportEngine == null)
			throw new AdempiereException("@NotFound@ @M_PrintFormat_ID@");

		reportEngine.getLayout();

		final StringWriter stringWriter = new StringWriter();
		reportEngine.createXML(stringWriter);
		return stringWriter;
	}
	
	public static StringWriter getXMLFromReportEngine(MHRPaySelectionCheck psck , int printFormatId) {
		MClient client = MClient.get(psck.getCtx());
		Language language = client.getLanguage();
		// Get Format & Data
		MPrintFormat format = MPrintFormat.get(psck.getCtx(), printFormatId , false);
		format.setLanguage(language);
		format.setTranslationLanguage(language);
		// query
		MQuery query = new MQuery(X_HR_PaySelectionCheck.Table_Name);
		
		query.addRestriction(X_HR_PaySelectionCheck.COLUMNNAME_HR_PaySelectionCheck_ID, MQuery.EQUAL,
				psck.getHR_PaySelectionCheck_ID());
		
		PrintInfo printInfo = new PrintInfo(hrProcess.getDocumentNo(),
				X_HR_PaySelectionCheck.Table_ID, psck.getHR_PaySelectionCheck_ID(), psck
						.getC_BPartner_ID());
		printInfo.setCopies(1);
		printInfo.setDocumentCopy(false); // true prints "Copy" on second
		printInfo.setPrinterName(format.getPrinterName());

		// Engine
		ReportEngine reportEngine = new ReportEngine(psck.getCtx(), format,
				query, printInfo, psck.get_TrxName());
		if (reportEngine == null)
			throw new AdempiereException("@NotFound@ @M_PrintFormat_ID@");

		reportEngine.getLayout();

		final StringWriter stringWriter = new StringWriter();
		reportEngine.createXML(stringWriter);
		return stringWriter;
	}

	public String getCFDI(MInvoice invoice) throws TransformerException {
		// get XML from ADempiere format
		StringWriter xmlReport = getXMLFromReportEngine(invoice, addendaInfo.getAD_PrintFormat_ID());
		StringWriter transformedCFDI = new StringWriter();
		// Generate CFDI using transformer
		transform(new StreamSource(CFDI_XSLT), new StreamSource(
				new StringReader(xmlReport.getBuffer().toString())),
				new StreamResult(transformedCFDI));
		return setAddenda(transformedCFDI.getBuffer().toString());
	}
	
	public String getCFDI(MHRPaySelectionCheck psck) throws TransformerException {
		// get XML from ADempiere format
		StringWriter xmlReport = getXMLFromReportEngine(psck, addendaInfo.getAD_PrintFormat_ID());
		StringWriter transformedCFDI = new StringWriter();
		// Generate CFDI using transformer

		transform(new StreamSource(CFDI_XSLT), new StreamSource(
				new StringReader(xmlReport.getBuffer().toString())),
				new StreamResult(transformedCFDI));
		return setAddenda(transformedCFDI.getBuffer().toString());
	}

	public String setAddenda(final String CFDI) throws TransformerException {
		StringWriter transformedCFDI = new StringWriter();
		transform(new StreamSource(CFDI_ADDENDA_XSLT), new StreamSource(
				new StringReader(CFDI)), new StreamResult(transformedCFDI));
		return transformedCFDI.getBuffer().toString();
	}

	private void createDocumentStamp() throws TransformerException, IOException,
			SAXException, URISyntaxException {
		

		documentCFDI = new MLMXDocument(po.getCtx(),0, po.get_TrxName());
		documentCFDI.setAD_Table_ID(po.get_Table_ID());
		documentCFDI.setRecord_ID(po.get_ID());
		documentCFDI.setTaxID(getRFC());
		documentCFDI.saveEx();

		// String tmpDir = System.getProperty("user.home");
		// if (!tmpDir.endsWith(File.separator))
		// tmpDir = tmpDir + File.separator;

		String CFDName = "CFD" +   
				(invoice!= null ? 
						(invoice.getDocumentNo() + ".xml") : (  psck.getC_BPartner().getTaxID() + hrProcess.getDocumentNo() +  ".xml"));
		
		String xmlCFDI = "";
		
		if(invoice!= null)
			xmlCFDI = generateCFDIWithStamp(getCFDI(invoice));
		else
			xmlCFDI = generateCFDIWithStamp(getCFDI(psck));
		
		
		if(invoice!=null)
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

		stampXML(xmlCFDI);

		if(invoice!=null) updateDateInvoice();

		
		documentCFDI.setCFDISeal(getSeal());
		documentCFDI.setCFDISATSeal(getSealSAT());
		documentCFDI.setCFDISATCertificate(getNoCertificateSAT());
		documentCFDI.setCFDIString(stringCFDI);
		documentCFDI.setCFDIXML(getCFDI());
		documentCFDI.setCFDISealingDate(getDate());
		
		documentCFDI.saveEx();

		service.generateQR(documentCFDI);

		MAttachment attachment = new MAttachment(documentCFDI.getCtx(), documentCFDI
				.get_Table_ID(), documentCFDI.getLMX_Document_ID(), documentCFDI
				.get_TrxName());
		attachment.setTitle("CFD");
		attachment.addEntry("CFD" + (invoice!= null ? 
				(invoice.getDocumentNo() + ".xml") : (  psck.getC_BPartner().getTaxID() + hrProcess.getDocumentNo() +  ".xml")), getCFDI()
				.getBytes("UTF-8"));
		attachment.addTextMsg(stringCFDI);
		attachment.saveEx();

		MAttachmentNote attachmentNote = new MAttachmentNote(documentCFDI.getCtx(), 0,
				documentCFDI.get_TrxName());
		attachmentNote.setAD_Attachment_ID(attachment.get_ID());
		attachmentNote.setAD_User_ID(100);
		attachmentNote.setTitle("CFD" +   
				(invoice!= null ? 
						(invoice.getDocumentNo() + ".xml") : (  psck.getC_BPartner().getTaxID() + hrProcess.getDocumentNo() +  ".xml")));
		attachmentNote.setTextMsg(getCFDI());
		attachmentNote.saveEx();
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
			cdfdi.setInvoice(invoice);
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

	public static void cancel(MInvoice invoice) {
		try {
			LMXCFDI cdfdi = LMXCFDI.get();
			cdfdi.setInvoice(invoice);
			cdfdi.reverseInvoiceStamp((MInvoice) cdfdi.getReversal(invoice));
		} catch (Exception e) {
			throw new AdempiereException(e.getMessage());
		}	
			
	}
	public static void regenerate(MInvoice invoice)
			throws TransformerException, IOException {
		LMXCFDI cdfdi = LMXCFDI.get();
		cdfdi.setInvoice(invoice);
		cdfdi.getCFDIXML();

		String CFDI = cdfdi.getCFDI();
		if (CFDI == null)
			return;

		documentCFDI = MLMXDocument.get(invoice);
		documentCFDI.setCFDIXML(CFDI);
		documentCFDI.saveEx();

		cdfdi.updateDateInvoice();
		documentCFDI.setCFDISeal(cdfdi.getSeal());
		documentCFDI.setCFDISATSeal(cdfdi.getSealSAT());
		documentCFDI.setCFDIString(cdfdi.getOriginalString(cdfdi.getCFDI()));
		documentCFDI.setCFDISATCertificate(cdfdi.getNoCertificateSAT());
		documentCFDI.saveEx();

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
		attachment.addEntry("CFD" + invoice.getDocumentNo() + ".xml", cdfdi
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
		attachmentNote.setTitle(invoice.getDocumentNo());
		attachmentNote.saveEx();
	}

	

	private String getCFDIXML() {
		try {
			service.getToken(documentCFDI, taxInfo.getPartnerID());
			final Source response = service.execute(documentCFDI,
					MLMXVendorService.SOAPSERVICETYPE_QueryDocument);
			String result = service.parse(response);
			String xml = result.substring(result.indexOf("<DatosXML>") + 10,
					result.indexOf("</DatosXML>"));
			CFDI = xml;
			return xml;
		} catch (Exception e) {
			throw new AdempiereException(e);
		}

	}

	private void updateDateInvoice() {
		try {
			String fecha = CFDI.substring(CFDI.indexOf("fecha=") + 7, CFDI
					.indexOf("\" xsi:schemaLocation="));
			fecha = fecha.replace("T", " ");
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
			Date newDate = (Date) formatter.parse(fecha);
			Timestamp invoiceDate = new Timestamp(newDate.getTime());
			invoice.setDateInvoiced(invoiceDate);
			invoice.setDateAcct(invoiceDate);
			invoice.saveEx();
			documentCFDI.setCFDISealingDate(fecha);
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
		return CFDI;
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

	private String stampXML(String xml) {

		service.getToken(documentCFDI, taxInfo.getPartnerID());

		String xmlF = "";
		xml = cutAddenda(xml);
		// xml = cutComplemento(xml);

		// xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");

		documentCFDI.setCFDIXML(xml);
		documentCFDI.saveEx();

		try {
			
	        generateStamp(documentCFDI);            


			final Source response = service.execute(documentCFDI,
					MLMXVendorService.SOAPSERVICETYPE_GenerateStamp);
			String stampXML = service.parse(response);
			
			if (stampXML.contains("![CDATA["))
			{	
				CFDI = stampXML.substring(stampXML.indexOf("![CDATA[") + 8,
					stampXML.indexOf("]]>"));
			}
			if (stampXML.contains("<DatosXML>"))
			{		
				CFDI = stampXML.substring(stampXML.indexOf("<DatosXML>") + 10,
					stampXML.indexOf("</DatosXML>"));
			}
			else
			{
				CFDI = stampXML;
			}

			documentCFDI.setCFDIXML(CFDI);
			documentCFDI.saveEx();

			xmlF = pasteAddenda(stampXML);

			documentCFDI.setCFDIUUID(getUUID());

		} catch (Exception e) {
			throw new AdempiereException(e.toString());
		}

		return xmlF;
	}



	private static String cleanResponse(String xml) {
		if (xml.contains("![CDATA["))
			xml = xml
					.substring(xml.indexOf("![CDATA[") + 8, xml.indexOf("]]>"));
		else
			xml = xml.substring(xml.indexOf("<DatosXML>") + 10, xml
					.indexOf("</DatosXML>"));

		xml = xml.replace("&lt;", "<");
		xml = xml.replace("&gt;", ">");
		xml = xml.replace("&amp;#209;", "Ñ");
		xml = xml.replace("&#209;", "Ñ");

		xml = xml.replace("&amp;#225;", "á");
		xml = xml.replace("&#225;", "á");

		xml = xml.replace("&amp;#193", "Á");
		xml = xml.replace("&#193;", "Á");

		xml = xml.replace("&amp;#233;", "é");
		xml = xml.replace("&#233;", "é");

		xml = xml.replace("&amp;#201;", "É");
		xml = xml.replace("&#201;", "É");

		xml = xml.replace("&amp;#237;", "í");
		xml = xml.replace("&#237;", "í");

		xml = xml.replace("&amp;#205;", "Í");
		xml = xml.replace("&#205;", "Í");

		xml = xml.replace("&amp;#243;", "ó");
		xml = xml.replace("&#243;", "ó");

		xml = xml.replace("&amp;#211;", "Ó");
		xml = xml.replace("&#211;", "Ó");

		xml = xml.replace("&amp;#250;", "ú");
		xml = xml.replace("&#250;", "ú");

		xml = xml.replace("&amp;#218;", "Ú");
		xml = xml.replace("&#218;", "Ú");

		xml = xml.replace("&amp;#241;", "ñ");
		xml = xml.replace("&#241;", "ñ");

		xml = xml.replace("&amp;#209;", "Ñ");
		xml = xml.replace("&#209;", "Ñ");

		return xml;
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

	

	private static void printResponse(final String response) {
		System.out.println("reponse=\n" + response);
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

            String sello = getSignedChain(m_Key_Store, stringCFDI, certificate.getPassword());
            xml = fp + sello.replaceAll("\n", "").replaceAll("\r", "") + sp;

            ind = xml.indexOf("certificado=\"") + 13;
            fp = xml.substring(0, ind);
            sp = xml.substring(ind, xml.length());
            xml = fp + getCertificateBASE64Encoder(m_Key_Store).replaceAll("\n", "").replaceAll("\r", "") + sp;

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
            m_Key_Store = ks;

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
            //System.out.print("Alias:"+ alias);
        }
        return alias;
    }

}