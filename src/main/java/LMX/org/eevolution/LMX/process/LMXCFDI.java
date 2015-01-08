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
import java.util.List;

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
import org.compiere.model.MAttachment;
import org.compiere.model.MAttachmentNote;
import org.compiere.model.MClient;
import org.compiere.model.MImage;
import org.compiere.model.MInvoice;
import org.compiere.model.MQuery;
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
import org.eevolution.LMX.model.I_LMX_Vendor;
import org.eevolution.LMX.model.MLMXAddenda;
import org.eevolution.LMX.model.MLMXCertificate;
import org.eevolution.LMX.model.MLMXInvoice;
import org.eevolution.LMX.model.MLMXTax;
import org.eevolution.LMX.model.MLMXVendorService;
import org.eevolution.LMX.util.SHA1;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import sun.misc.BASE64Decoder;

public final class LMXCFDI {

	private MLMXCertificate certificate = null;
	private LMXVendorInterface service = null;
	private MLMXTax taxInfo = null;
	private MLMXAddenda addInfo = null;
	private String CFDI = null;

	private static LMXCFDI s_instance = null;

	private static InputStream CFDI_XSLT = null;
	private static InputStream CFDI_STRING_XSLT = null;
	private static InputStream CFDI_ADDENDA_XSLT = null;
	private static InputStream CFDI_SCHEMA = null;

	private static MInvoice invoice = null;
	private static MLMXInvoice invoiceCFDI = null;

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

	public void setInvoice(MInvoice po) {

		try {
			invoice = po;

			taxInfo = MLMXTax.getTax(invoice.getCtx(), invoice.getAD_Org_ID(),
					invoice.get_TrxName());
			if (taxInfo == null)
				throw new AdempiereException(
						"No existe infomración de la compañia");

			if (addInfo == null)
				addInfo = MLMXAddenda.getByBPartnerId(invoice.getCtx(), taxInfo
						.getC_BPartner_ID(), invoice.get_TrxName());

			MLMXAddenda addInfoCustomer = MLMXAddenda.getByBPartnerId(invoice
					.getCtx(), invoice.getC_BPartner_ID(), invoice
					.get_TrxName());
			if (addInfoCustomer != null)
				addInfo = addInfoCustomer;

			if (addInfo == null)
				throw new AdempiereException(
						"No hay esquema para comprobante Fiscal Digital");

			LMXVendorEngine engine = LMXVendorEngine.get();

			I_LMX_Vendor vendor = certificate.getVendorService(invoice);
			if (vendor == null)
				return;
			service = engine.getLMXVendorFactory(vendor.getClassname());
			if (service == null)
				throw new AdempiereException("No existe ningun integrador");

			invoiceCFDI = new MLMXInvoice(po);
			invoiceCFDI.setTaxID(taxInfo.getTaxID());
			invoiceCFDI.saveEx();

			CFDI_SCHEMA = new ByteArrayInputStream(MImage.get(Env.getCtx(),
					taxInfo.getCFDISchema_ID()).getData());

			CFDI_XSLT = new ByteArrayInputStream(MImage.get(Env.getCtx(),
					taxInfo.getCFDITransformer_ID()).getData());
			CFDI_STRING_XSLT = new ByteArrayInputStream(MImage.get(
					Env.getCtx(), taxInfo.getCFDITransformerString_ID())
					.getData());
			CFDI_ADDENDA_XSLT = new ByteArrayInputStream(MImage.get(
					Env.getCtx(), addInfo.getCFDITransformer_ID()).getData());

			if (CFDI_ADDENDA_XSLT == null)
				throw new AdempiereException(
						"No hay esquema para Comprobante Fiscal Digital");

		} catch (Exception e) {
			throw new AdempiereException(e.getMessage());
		}

	}

	public void generate() {

		try {
			if (invoice.isReversal()) {
				// reverseInvoiceStamp((MInvoice) invoice.getReversal(),
				// invoice.getC_Invoice_ID());
				// Codigo para versiones obsoletas de Adempiere
				reverseInvoiceStamp((MInvoice) getReversal(invoice));

			} else
				createInvoiceStamp();

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
		getToken();
		final Source response = service.execute(invoiceCFDI,
				MLMXVendorService.SOAPSERVICETYPE_CancelStamp);
		String cancelXML = parse(response);
		MAttachment eAttach = new MAttachment(invoice.getCtx(), invoice
				.get_Table_ID(), invoice.getC_Invoice_ID(), invoice
				.get_TrxName());
		eAttach.setTitle("Acuse de Recibo CFDI");
		eAttach.addEntry("Acuse de Recibo CFDI" + invoice.getDocumentNo() + ".xml", getCFDI()
				.getBytes("UTF-8"));
		eAttach.addTextMsg(cancelXML);
		eAttach.saveEx();

		MAttachmentNote attNote = new MAttachmentNote(invoice.getCtx(), 0,
				invoice.get_TrxName());
		attNote.setAD_Attachment_ID(eAttach.get_ID());
		attNote.setAD_User_ID(100);
		attNote.set_ValueOfColumn("TEXTMSG", cancelXML);
		attNote.setTitle(invoice.getDocumentNo());
		attNote.saveEx();
		
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
		PrintInfo info = new PrintInfo(invoice.getDocumentNo(),
				X_C_Invoice.Table_ID, invoice.getC_Invoice_ID(), invoice
						.getC_BPartner_ID());
		info.setCopies(1);
		info.setDocumentCopy(false); // true prints "Copy" on second
		info.setPrinterName(format.getPrinterName());

		// Engine
		ReportEngine reportEngine = new ReportEngine(invoice.getCtx(), format,
				query, info, invoice.get_TrxName());
		if (reportEngine == null)
			throw new AdempiereException("@NotFound@ @M_PrintFormat_ID@");

		reportEngine.getLayout();

		final StringWriter stringWriter = new StringWriter();
		reportEngine.createXML(stringWriter);
		return stringWriter;
	}

	public String getCFDI(MInvoice invoice) throws TransformerException {
		// get XML from ADempiere format
		StringWriter xmlReport = getXMLFromReportEngine(invoice , addInfo.getAD_PrintFormat_ID());
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

	private void createInvoiceStamp() throws TransformerException, IOException,
			SAXException, URISyntaxException {

		// String tmpDir = System.getProperty("user.home");
		// if (!tmpDir.endsWith(File.separator))
		// tmpDir = tmpDir + File.separator;

		String CFDName = "CFD" + invoice.getDocumentNo() + ".xml";
		String xmlCFDI = generateCFDIWithStamp(getCFDI(invoice));
		// Schema Validation
		String schemaLang = "http://www.w3.org/2001/XMLSchema";
		// get validation driver:
		SchemaFactory schema_factory = SchemaFactory.newInstance(schemaLang);
		// create schema by reading it from an XSD file:
		Schema schema = schema_factory.newSchema(new StreamSource(CFDI_SCHEMA));
		Validator validator = schema.newValidator();
		validator.validate(new StreamSource(new StringReader(xmlCFDI)));

		if ("".equals(xmlCFDI) || xmlCFDI == null)
			throw new AdempiereException("El CFDI no es Válido");

		String stringCFDI = getOriginalString(xmlCFDI);

		stampXML(xmlCFDI);

		updateDateInvoice();

		invoiceCFDI.setCFDISeal(getSeal());
		invoiceCFDI.setCFDISATSeal(getSealSAT());
		invoiceCFDI.setCFDISATCertificate(getNoCertificateSAT());
		invoiceCFDI.saveEx();

		generateQR();

		MAttachment eAttach = new MAttachment(invoice.getCtx(), invoice
				.get_Table_ID(), invoice.getC_Invoice_ID(), invoice
				.get_TrxName());
		eAttach.setTitle("CFD");
		eAttach.addEntry("CFD" + invoice.getDocumentNo() + ".xml", getCFDI()
				.getBytes("UTF-8"));
		eAttach.addTextMsg(stringCFDI);
		eAttach.saveEx();

		MAttachmentNote attNote = new MAttachmentNote(invoice.getCtx(), 0,
				invoice.get_TrxName());
		attNote.setAD_Attachment_ID(eAttach.get_ID());
		attNote.setAD_User_ID(100);
		attNote.set_ValueOfColumn("TEXTMSG", getCFDI());
		attNote.setTitle(invoice.getDocumentNo());
		attNote.saveEx();
	}

	public static void main(String[] args) throws Exception {

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
			System.out.println("Factura procesando ... "
					+ invoice.getDocumentNo());
			//regenerate(invoice);
			LMXCFDI cdfdi = LMXCFDI.get();
			cdfdi.setInvoice(invoice);
			cdfdi.generate();

			System.out.println("Factura procesado ... "
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

		invoiceCFDI = MLMXInvoice.getInvoice(invoice);
		invoiceCFDI.setCFDIXML(CFDI);
		invoiceCFDI.saveEx();

		cdfdi.updateDateInvoice();
		invoiceCFDI.setCFDISeal(cdfdi.getSeal());
		invoiceCFDI.setCFDISATSeal(cdfdi.getSealSAT());
		invoiceCFDI.setCFDISATCertificate(cdfdi.getNoCertificateSAT());
		invoiceCFDI.saveEx();

		MImage image = new MImage(Env.getCtx(), invoiceCFDI.getCFDIQR_ID(), null);
		if (image != null)
			image.deleteEx(true);

		cdfdi.generateQR();

		MAttachment eAttach = new Query(Env.getCtx(), MAttachment.Table_Name,
				"AD_Table_ID=? AND Record_ID=?", null).setClient_ID()
				.setParameters(invoice.get_Table_ID(),
						invoice.getC_Invoice_ID()).first();
		if (eAttach != null) {
			for (int i = 0; i <= eAttach.getEntries().length; i = i + 1) {
				eAttach.deleteEntry(i);
			}
		} else
			eAttach = new MAttachment(invoice.getCtx(), invoice.get_Table_ID(),
					invoice.getC_Invoice_ID(), invoice.get_TrxName());

		eAttach.setTitle("CFD");
		eAttach.addEntry("CFD" + invoice.getDocumentNo() + ".xml", cdfdi
				.getCFDI().getBytes("UTF-8"));
		eAttach.addTextMsg(cdfdi.getOriginalString(cdfdi.getCFDI()));
		eAttach.saveEx();

		MAttachmentNote attNote = new Query(Env.getCtx(),
				MAttachmentNote.Table_Name, "AD_Attachment_ID=?", null)
				.setClient_ID().setParameters(eAttach.getAD_Attachment_ID())
				.first();

		if (attNote == null)
			attNote = new MAttachmentNote(invoice.getCtx(), 0, invoice
					.get_TrxName());

		attNote.setAD_Attachment_ID(eAttach.get_ID());
		attNote.setAD_User_ID(100);
		attNote.set_ValueOfColumn("TEXTMSG", cdfdi.getCFDI());
		attNote.setTitle(invoice.getDocumentNo());
		attNote.saveEx();
	}

	private void generateQR() {
		try {
			final Source response = service.execute(invoiceCFDI,
					MLMXVendorService.SOAPSERVICETYPE_QR);
			String res = parse(response);
			String qrString = res.substring(res.indexOf("<Imagen>") + 8, res
					.indexOf("</Imagen>"));
			InputStream qrInput = new ByteArrayInputStream(qrString.getBytes());
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] qrImage = decoder.decodeBuffer(qrInput);

			MImage image = new MImage(invoice.getCtx(), 0, invoice
					.get_TrxName());
			image.setName(invoice.getDocumentNo());
			image.setDescription(String.valueOf(invoice.getC_Invoice_ID()));
			image.setBinaryData(qrImage);
			image.saveEx();

			invoiceCFDI.setCFDIQR_ID(image.getAD_Image_ID());
			invoice.saveEx();
		} catch (Exception e) {
			throw new AdempiereException(e);
		}

	}

	private String getCFDIXML() {
		try {
			getToken();
			final Source response = service.execute(invoiceCFDI,
					MLMXVendorService.SOAPSERVICETYPE_QueryDocument);
			String result = parse(response);
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
			invoice.set_ValueOfColumn("CFDIFechaTimbrado", fecha);
			fecha = fecha.replace("T", " ");
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
			Date newDate = (Date) formatter.parse(fecha);
			Timestamp invoiceDate = new Timestamp(newDate.getTime());
			invoice.setDateInvoiced(invoiceDate);
			invoice.setDateAcct(invoiceDate);
			invoice.saveEx();
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
		return CFDI.substring(CFDI.indexOf("UUID=") + 6, CFDI
				.indexOf("\" FechaTimbrado="));
	}

	private String getSeal() {
		return CFDI.substring(CFDI.indexOf("sello=") + 7, CFDI
				.indexOf("\" certificado="));
	}

	private String getCFDI() {
		return CFDI;
	}

	private String getSealSAT() {

		String tmpSeal = CFDI.substring(CFDI.indexOf("selloSAT=") + 10);
		tmpSeal.substring(0, tmpSeal.indexOf("\" xsi:schemaLocation="));
		return tmpSeal;

	}

	private String getNoCertificateSAT() {
		return CFDI.substring(CFDI.indexOf("noCertificadoSAT=") + 18, CFDI
				.indexOf("\" selloSAT="));
	}

	private String getDate() {
		String fecha = CFDI.substring(CFDI.indexOf("fecha=") + 7, CFDI
				.indexOf("\" version="));
		fecha = fecha.replace("T", " ");
		return fecha;
	}

	private String stampXML(String xml) {

		getToken();

		String xmlF = "";
		xml = cutAddenda(xml);
		// xml = cutComplemento(xml);

		// xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");

		invoiceCFDI.setCFDIXML(xml);
		invoiceCFDI.saveEx();

		try {

			final Source response = service.execute(invoiceCFDI,
					MLMXVendorService.SOAPSERVICETYPE_GenerateStamp);
			String stampXML = parse(response);
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

			invoiceCFDI.setCFDIXML(CFDI);
			invoiceCFDI.saveEx();

			xmlF = pasteAddenda(stampXML);

			invoiceCFDI.setCFDIUUID(getCFDI());

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

	public String getToken() {

		try {
			Source response = service.execute(invoiceCFDI,
					MLMXVendorService.SOAPSERVICETYPE_Token);
			final String xml = parse(response);
			String token = getElement(xml, "Token");
			String toHash = String.format("%s|%s", taxInfo.getPartnerID(),
					token);
			byte[] as = toHash.getBytes("UTF-8");
			String toHash2 = new String(as, "UTF-8");
			SHA1 sha1 = new SHA1();
			token = sha1.getHash(toHash2);
			invoiceCFDI.setCFDIToken(token);
			invoiceCFDI.saveEx();
			return token;
		} catch (NoSuchAlgorithmException ex) {
			throw new AdempiereException(ex.getMessage());
		} catch (Exception e) {
			throw new AdempiereException(e.getMessage());
		}
	}

	private static String parse(final Source response)
			throws TransformerFactoryConfigurationError, TransformerException {
		assert response != null;
		final Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();

		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF8");
		final StringWriter writer = new StringWriter();
		final StreamResult result = new StreamResult(writer);
		transformer.transform(response, result);

		String respxml = StringEscapeUtils.unescapeXml(writer.toString());
		return respxml;
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
			xml = fp + " certificado=\"\" " + sp; // +
													// getCertificateBASE64Encoder(m_Key_Store).replaceAll("\n",
													// "") + "\" " + sp;

			ind = xml.indexOf("noCertificado=\"") + 15;
			fp = xml.substring(0, ind);
			sp = xml.substring(ind, xml.length());
			xml = fp + certificate.getDocumentNo() + sp;
			// seal = ""; // sello.replaceAll("\n", "");

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
		return getTax().getTaxID();
	}

}