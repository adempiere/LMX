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
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;
import java.util.List;

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
import org.compiere.model.I_C_BPartner;
import org.compiere.model.I_C_DocType;
import org.compiere.model.I_C_Invoice;
import org.compiere.model.I_C_PaySelectionCheck;
import org.compiere.model.I_C_Payment;
import org.compiere.model.MAttachment;
import org.compiere.model.MAttachmentNote;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MDocType;
import org.compiere.model.MImage;
import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.model.MQuery;
import org.compiere.model.PO;
import org.compiere.model.PrintInfo;
import org.compiere.model.Query;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Language;
import org.compiere.util.Login;
import org.eevolution.LMX.engine.LMXVendorEngine;
import org.eevolution.LMX.engine.LMXVendorInterface;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.eevolution.LMX.model.I_LMX_Vendor;
import org.eevolution.LMX.model.MLMXAddenda;
import org.eevolution.LMX.model.MLMXBPartner;
import org.eevolution.LMX.model.MLMXCertificate;
import org.eevolution.LMX.model.MLMXDocType;
import org.eevolution.LMX.model.MLMXDocument;
import org.eevolution.LMX.model.MLMXTax;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.apache.commons.ssl.PKCS8Key;
import sun.misc.BASE64Encoder;


public final class LMXCFDI {

	private MLMXCertificate certificate = null;
	private LMXVendorInterface service = null;
	private MLMXTax taxInfo = null;
	private MLMXAddenda addendaInfo = null;
	private String CFDI = null;

	private static LMXCFDI instance = null;
	private static PO document  = null;
	private static MLMXDocument documentCFDI = null;
	private static CCache<Integer , LMXCFDI> cfdiCache = new CCache<>("LMXCFDI" , 5 , 1440);
	private MDocType docType = null;


	/***
	* get WMRule Engine like singleton instance
	*
	* @return WM Rule Engine
	**/
	public static LMXCFDI get() {
		Integer clientId = Env.getAD_Client_ID(Env.getCtx());
		if (cfdiCache.containsKey(clientId))
			instance = cfdiCache.get(clientId);
		else {
			instance = new LMXCFDI();
			cfdiCache.put(clientId , instance);
		}
		return instance;
	}

	public LMXCFDI() {
		certificate = MLMXCertificate.get();
	}


	public void setDocument(PO po) {
		try {
			this.document = po;
			if (certificate.getCertificateFile_ID() > 0 && !getNoCertificado().equals(certificate.getDocumentNo()))
				throw new AdempiereException("Numero Certificado no coincide con el ");

			int docTypeId = po.get_ValueAsInt(I_C_DocType.COLUMNNAME_C_DocType_ID);
			docType = MDocType.get(po.getCtx() , docTypeId);
			if (docType == null)
				throw  new AdempiereException("El documento no valido");

			taxInfo = MLMXTax.getTax(po.getCtx(), po.getAD_Org_ID(), po.get_TrxName());
			if (taxInfo == null)
				throw new AdempiereException("No existe infomración de la compañia para el timbrado");

			if (addendaInfo == null)
				addendaInfo = MLMXAddenda.getByBPartnerId(po.getCtx(), taxInfo.getC_BPartner_ID(), po.get_TrxName());

			MLMXAddenda addInfoCustomer = MLMXAddenda.getByBPartnerId(po.getCtx(), po.get_ValueAsInt(I_C_BPartner.COLUMNNAME_C_BPartner_ID), po.get_TrxName());
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

		} catch (Exception e) {
			throw new AdempiereException(e.getMessage());
		}

	}

	private InputStream getCertificate()
	{
		InputStream inputStramCertificate = null;
		// Certificate File
		if (certificate.getCertificateFile_ID() > 0 )
			inputStramCertificate = new ByteArrayInputStream(MImage.get(Env.getCtx(), certificate.getCertificateFile_ID()).getData());
		else
			throw new AdempiereException("No existe archivo certificado .cer adjunto");
		return inputStramCertificate;
	}

	private InputStream getKey(){
		InputStream inputStramCertificateKey = null;
		// Key file
		if (certificate.getKeyFile_ID() > 0)
			inputStramCertificateKey = new ByteArrayInputStream(MImage.get(Env.getCtx(), certificate.getKeyFile_ID()).getData());
		else
			throw new AdempiereException("No existe archivo .key de clave adjunta ");

		return inputStramCertificateKey;

	}

	private InputStream getCFDI_SCHEMA()
	{
		InputStream inputStramSchema = null;
		// ADempiere Schema
		if (addendaInfo.getCFDISchema_ID() > 0)
			inputStramSchema = new ByteArrayInputStream(MImage.get(Env.getCtx(), addendaInfo.getCFDISchema_ID()).getData());
		else
			throw new AdempiereException("No existe esquema para CFDI");

		return inputStramSchema;

	}

	private InputStream getCFDI_XSLT() {
		InputStream inputStramCFDIXSLT = null;
		//	ADempiere Transformera
		if (addendaInfo.getCFDIADTransformer_ID() > 0)
			inputStramCFDIXSLT = new ByteArrayInputStream(MImage.get(Env.getCtx(), addendaInfo.getCFDIADTransformer_ID()).getData());
		else
			throw new AdempiereException("No existe plantilla para para CFDI");
		return inputStramCFDIXSLT;
	}

	private InputStream getCFDI_STRING_XSLT(){
		InputStream 	inputStreamCFDI_STRING_XSLT = null;
		// CFDI String Transformer
		if (addendaInfo.getCFDITransformerString_ID() > 0)
			inputStreamCFDI_STRING_XSLT = new ByteArrayInputStream(MImage.get(Env.getCtx(), addendaInfo.getCFDITransformerString_ID()).getData());
		else
			throw new AdempiereException("No existe plantilla para Cadena Orginal");
		return inputStreamCFDI_STRING_XSLT;
	}

	private InputStream getCFDI_ADDENDA_XSLT (){
		InputStream inputStreamCFDI_ADDENDA_XSLT = null;
		//	CFDI Transformer
		if (addendaInfo.getCFDITransformer_ID() > 0)
			inputStreamCFDI_ADDENDA_XSLT = new ByteArrayInputStream(MImage.get(Env.getCtx(), addendaInfo.getCFDITransformer_ID()).getData());
		else
			throw new AdempiereException("No hay esquema para Comprobante Fiscal Digital");
		return inputStreamCFDI_ADDENDA_XSLT;
	}

	public MLMXDocument generate() {

		try {
			if (document != null && document.get_Table_ID() == MInvoice.Table_ID) {
				MInvoice invoice = (MInvoice) document;
				if (invoice.isReversal())
					return cancelCFDI(getReversal(document));
				else return createCFDI();
			}
			else if (document != null && document.get_Table_ID() == MPayment.Table_ID)
			{
				MPayment payment = (MPayment) document;
				if (payment.isReversal())
					return cancelCFDI(getReversal(document));
				else
					return createCFDI();
			}
			return null;
		} catch (Exception e) {
			throw new AdempiereException(e.getMessage());
		}

	}

	private static final String RSA = "RSA";
	private static final String FIRMA = "SHA256withRSA";
	private static final String X_509 = "X.509";

	private  String getCertificateBASE64Encoder() {
		try {
			CertificateFactory cf = CertificateFactory.getInstance(X_509);
			X509Certificate certificate = (X509Certificate) cf.generateCertificate(getCertificate());
			byte[] binary_certificate = certificate.getEncoded();
			String signatureB64 = new BASE64Encoder().encode(binary_certificate);
			return signatureB64;
		}
		catch (CertificateException certificateBASE64Encoder)
		{
			throw new AdempiereException(certificateBASE64Encoder.getMessage());
		}
	}

	private String singner(String originalString) {
		try {
			String keyFileString = getKeyString();
			String keyWord = certificate.getPassword();
			PKCS8Key pkcs8 = new PKCS8Key(Base64.getDecoder().decode(keyFileString), keyWord.toCharArray());
			KeyFactory privateKeyFactory = KeyFactory.getInstance(RSA);
			PKCS8EncodedKeySpec pkcs8Encoded = new PKCS8EncodedKeySpec(pkcs8.getDecryptedBytes());
			PrivateKey privateKey = privateKeyFactory.generatePrivate(pkcs8Encoded);
			Signature signature = Signature.getInstance(FIRMA);
			signature.initSign(privateKey);
			signature.update(originalString.getBytes());
			String singnatureString = new String(Base64.getEncoder().encode(signature.sign()));
			return singnatureString;
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getNoCertificado() {
		try {
			CertificateFactory cf = CertificateFactory.getInstance(X_509);
			X509Certificate certificado = (X509Certificate) cf.generateCertificate(getCertificate());
			byte[] byteArray = certificado.getSerialNumber().toByteArray();
			String noCertificado = new String(byteArray);
			return noCertificado;
		} catch (CertificateException certificateException)
		{
			throw new AdempiereException(certificateException.getMessage());
		}
	}

	private String getKeyString() {
		try {
			InputStream inputStreamKey = getKey();
			byte[] fileBytes = new byte[inputStreamKey.available()];
			inputStreamKey.read(fileBytes);
			inputStreamKey.close();
			String fileString = new String(Base64.getEncoder().encode(fileBytes));
			return fileString;
		} catch (IOException exeption) {
			throw new AdempiereException(exeption.getMessage());
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
		transform(new StreamSource(getCFDI_XSLT()), new StreamSource(
						new StringReader(xmlReport)),
				new StreamResult(transformedCFDI));
		return transformedCFDI.getBuffer().toString();
	}

	public String getAddendaTransform(final String CFDI) throws TransformerException {
		StringWriter transformedCFDI = new StringWriter();
		transform(new StreamSource(getCFDI_ADDENDA_XSLT()), new StreamSource(
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
			// Set Relation Tipe
			if (docType != null) {
				MLMXDocType.getByDocType(docType).ifPresent(
						documentType ->
						{
							documentCFDI.setTipoDeComprobante(documentType.getTipoDeComprobante());
							documentCFDI.setTipoRelacion(documentType.getTipoRelacion());
						}
				);
			}
			;
			// Set Uso CFDI
			if (document.get_ColumnIndex(MBPartner.COLUMNNAME_C_BPartner_ID) > 0) {
				MBPartner bPartner = MBPartner.get(document.getCtx(), document.get_ValueAsInt(MBPartner.COLUMNNAME_C_BPartner_ID));
				MLMXBPartner.getByBPartner(bPartner).ifPresent(
						partner -> documentCFDI.setUsoCFDI(partner.getUsoCFDI())
				);
			}
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

		if(document != null) {
			documentCFDI.setCFDIXML(generateStamp(getCFDI(document)));
			documentCFDI.saveEx();
		}

		if(document!=null && I_C_Invoice.Table_Name.equals(document.get_TableName()))
		{
			// Schema Validation
			String schemaLang = "http://www.w3.org/2001/XMLSchema";
			// get validation driver:
			SchemaFactory schemaFactory = SchemaFactory.newInstance(schemaLang);
			// create schema by reading it from an XSD file:
			Schema schema = schemaFactory.newSchema(new StreamSource(getCFDI_SCHEMA()));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new StringReader(documentCFDI.getCFDIXML())));
		}

		if ("".equals(documentCFDI.getCFDIXML()) || documentCFDI.getCFDIXML() == null)
			throw new AdempiereException("El CFDI no es Válido");

		service.getCFDI(documentCFDI);
		service.getQR(documentCFDI);
		MAttachment attachment = new MAttachment(documentCFDI.getCtx(), documentCFDI
				.get_Table_ID(), documentCFDI.getLMX_Document_ID(), documentCFDI
				.get_TrxName());
		attachment.setTitle("CFD");
		String CFDName = "CFD" + documentCFDI.getCFDIUUID() +  ".xml";
		attachment.addEntry(CFDName, getCFDI()
				.getBytes("UTF-8"));
		attachment.addTextMsg(documentCFDI.getCFDIString());
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
			String date = CFDI.substring(CFDI.indexOf("Fecha=") + 7, CFDI
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
		
		String tmpString = CFDI.substring(CFDI.indexOf("Sello=") + 7);
		return tmpString.substring(0, tmpString.indexOf("\""));
	}

	private String getCFDI() {
		return documentCFDI.getCFDIXML();
	}

	private String getSealSAT() {

		String tmpSeal = CFDI.substring(CFDI.indexOf("SelloSAT=") + 10);
		return tmpSeal.substring(0, tmpSeal.indexOf("\""));

	}

	private String getNoCertificateSAT() {
		/*return CFDI.substring(CFDI.indexOf("noCertificadoSAT=") + 18, CFDI
				.indexOf("\" selloSAT="));*/
		
		String tmpString = CFDI.substring(CFDI.indexOf("NoCertificadoSAT=") + 18);
		return tmpString.substring(0, tmpString.indexOf("\""));
	}

	private String getDate() {
		
		/*String fecha = CFDI.substring(CFDI.indexOf("fecha=") + 7, CFDI
				.indexOf("\" version="));*/
		
		String fecha = CFDI.substring(CFDI.indexOf("Fecha=") + 7);
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

	private String getOriginalString(String CFDI) throws TransformerException,
			IOException {
		StringWriter transformed = new StringWriter();
		transform(new StreamSource(getCFDI_STRING_XSLT()), new StreamSource(
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


    private String generateStamp(String CFDIXML) {
    try {
        int certificateNoIndex = CFDIXML.indexOf(" NoCertificado=\"") + 16;
        String firstCertificateXml = CFDIXML.substring(0, certificateNoIndex);
        String lastCertificateXml = CFDIXML.substring(certificateNoIndex, CFDIXML.length());
        String xmlWithCertificateNo = firstCertificateXml + certificate.getDocumentNo().replaceAll("\n", "").replaceAll("\r", "") + lastCertificateXml;

        documentCFDI.setCFDIString(getOriginalString(xmlWithCertificateNo));
        documentCFDI.saveEx();
        String stamp =  singner(documentCFDI.getCFDIString());
        int stampIndex = xmlWithCertificateNo.indexOf(" Sello=\"") + 8;
        String firstStampXml = xmlWithCertificateNo.substring(0, stampIndex);
        String lastStampXml = xmlWithCertificateNo.substring(stampIndex, xmlWithCertificateNo.length());
        String xmlWithStamp = firstStampXml + stamp + "\" Certificado=\"" + getCertificateBASE64Encoder().replaceAll("\n", "").replaceAll("\r", "") + lastStampXml;
        return xmlWithStamp;

    } catch (Exception e) {
        throw new AdempiereException("No se pudo generar sellado en este momento");
    }
}
}