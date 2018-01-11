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

package org.eevolution.LMX.engine.vendor;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.adempiere.exceptions.AdempiereException;
import org.apache.commons.lang.StringEscapeUtils;
import org.compiere.model.I_C_Invoice;
import org.compiere.model.MAttachment;
import org.compiere.model.MAttachmentNote;
import org.compiere.model.MImage;
import org.compiere.model.MInvoice;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.eevolution.LMX.engine.LMXVendorInterface;
import org.eevolution.LMX.engine.LMXVendorServiceInterface;
import org.eevolution.LMX.model.MLMXDocument;
import org.eevolution.LMX.model.MLMXVendor;
import org.eevolution.LMX.model.MLMXVendorService;
import org.eevolution.LMX.util.SHA1;
import org.eevolution.LMX.util.WebServiceConnector;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import sun.misc.BASE64Decoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by e-Evolution on 26/12/13.
 */
public class LMXEcodexService implements LMXVendorInterface {

	MLMXVendor vendor;
	MLMXDocument documentCFDI;
	PO document;

	public LMXEcodexService()
    {
        vendor = MLMXVendor.getByClassName(getClass().getCanonicalName());
        if (vendor == null)
            throw new AdempiereException("Implementación para este proveedor de servicios no existe: "+ getClass().getCanonicalName());
    }

    public Source execute(MLMXDocument documentCFDI  , String SOAPType)  throws Exception {
            LMXVendorServiceInterface service = vendor.getService(SOAPType);
            final WebServiceConnector wsc = new WebServiceConnector();
            String request = Env.parseVariable(service.getSOAPRequest(), documentCFDI, documentCFDI.get_TrxName(), false);
            System.out.println("Llamada del servicio :" + request);
            wsc.setRequest(request);
            wsc.setBinding(service.getSOAPBinding());
            wsc.setEndpointAddress(service.getSOAPEndpointAddress());
            wsc.setServiceName(service.getSOAPServiceName());
            wsc.setPortName(service.getSOAPPort());
            wsc.setTargetNS(service.getSOAPTargetNS());
            wsc.setSoapAction(service.getSOAPAction());
            wsc.executeConnector();
            final Source response = wsc.getResponse();
            return response;
    }

	private String getResponseXML(final Source response) throws TransformerFactoryConfigurationError, TransformerException, IOException, SOAPException
	{
		assert response != null;
		final Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF8");
		final StringWriter writer = new StringWriter();
		final StreamResult result = new StreamResult(writer);
		transformer.transform(response, result);
		return StringEscapeUtils.unescapeXml(writer.toString());
	}

	@Override
	public void setDocument(PO document) {
		this.document = document;
	}

	public void getQR(MLMXDocument documentCFDI) {
		try {
			final Source response = execute(documentCFDI, MLMXVendorService.SOAPSERVICETYPE_QR);
			final String responseXML  = getResponseXML(response);
			String qrString = responseXML.substring(responseXML.indexOf("<Imagen>") + 8, responseXML.indexOf("</Imagen>"));
			if (qrString != null && qrString.length() > 0 ) {
				InputStream qrInput = new ByteArrayInputStream(qrString.getBytes());
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] qrImage = decoder.decodeBuffer(qrInput);
				MImage image = new MImage(documentCFDI.getCtx(), 0, documentCFDI.get_TrxName());
				image.setName(String.valueOf(documentCFDI.get_ID()));
				image.setDescription(String.valueOf(documentCFDI.getRecord_ID()));
				image.setBinaryData(qrImage);
				image.saveEx();
				documentCFDI.setCFDIQR_ID(image.get_ID());
				documentCFDI.saveEx();
			}
		}
		catch (NoSuchAlgorithmException e )
		{
			throw new AdempiereException(e.getMessage());
		}
	 	catch (Exception e) {
		throw new AdempiereException(e.getMessage());
		}
	}

	public void getToken(MLMXDocument documentCFDI) {
		try {
			Source response = execute(documentCFDI, MLMXVendorService.SOAPSERVICETYPE_Token);
			final String responseXML  = getResponseXML(response);
			Optional<String> optionalTokenTag = Optional.of(getElement(responseXML, "Token"));
			if (optionalTokenTag.isPresent()) {
				String toHash = String.format("%s|%s", vendor.getPassword(), optionalTokenTag.get());
				byte[] as = toHash.getBytes("UTF-8");
				String toHash2 = new String(as, "UTF-8");
				SHA1 sha1 = new SHA1();
				String token = sha1.getHash(toHash2);
				documentCFDI.setCFDIToken(token);
				documentCFDI.saveEx();
			}
		} catch (NoSuchAlgorithmException ex) {
			throw new AdempiereException(ex.getMessage());
		} catch (Exception e) {
			throw new AdempiereException(e.getMessage());
		}
	}

	@Override
	public void getCFDI(MLMXDocument documentCFDI) {
		getToken(documentCFDI);
		String CFDI = "";

		try {
			final Source response = execute(documentCFDI, MLMXVendorService.SOAPSERVICETYPE_GenerateStamp);
			final String responseXML  = getResponseXML(response);

			if (responseXML.contains("![CDATA["))
			{
				CFDI = responseXML.substring(responseXML.indexOf("![CDATA[") + 8, responseXML.indexOf("]]>"));
			}
			if (responseXML.contains("<DatosXML>"))
			{
				CFDI = responseXML.substring(responseXML.indexOf("<DatosXML>") + 10, responseXML.indexOf("</DatosXML>"));
			}
			else
			{
				CFDI = responseXML;
			}

			fillCFDIInfo(documentCFDI , CFDI);

			documentCFDI.saveEx();

			//CFDIWithAddenda = pasteAddenda(responseXML);
		} catch (Exception e) {
			throw new AdempiereException(e.toString());
		}

	}


	private void fillCFDIInfo(MLMXDocument documentCFDI, String CFDI) {
		try {
			String date = CFDI.substring(CFDI.indexOf("Fecha=") + 7, CFDI.indexOf("Fecha=") + 26);
			date = date.replace("T", " ");
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
			Date newDate = (Date) formatter.parse(date);
			java.sql.Timestamp dateDocument = new java.sql.Timestamp(newDate.getTime());
			if (document != null && document instanceof  MInvoice) {
				document.set_ValueOfColumn(I_C_Invoice.COLUMNNAME_DateInvoiced , dateDocument);
				document.set_ValueOfColumn(I_C_Invoice.COLUMNNAME_DateAcct, dateDocument);
				document.saveEx();
			}
			documentCFDI.setCFDISealingDate(date);
			documentCFDI.setCFDIXML(CFDI);
			documentCFDI.setCFDISeal(getSeal(CFDI));
			documentCFDI.setCFDISATSeal(getSealSAT(CFDI));
			documentCFDI.setCFDIUUID(getUUID(CFDI));
			documentCFDI.setCFDISATCertificate(getNoCertificateSAT(CFDI));
			documentCFDI.saveEx();
		} catch (Exception e) {
			throw new AdempiereException(e);
		}
	}

	private String getSeal(String CFDI) {
		String tmpString = CFDI.substring(CFDI.indexOf("Sello=") + 7);
		return tmpString.substring(0, tmpString.indexOf("\""));
	}

	private String getSealSAT(String CFDI) {

		String tmpSeal = CFDI.substring(CFDI.indexOf("SelloSAT=") + 10);
		return tmpSeal.substring(0, tmpSeal.indexOf("\""));

	}

	private String getNoCertificateSAT(String CFDI) {
		String tmpString = CFDI.substring(CFDI.indexOf("NoCertificadoSAT=") + 18);
		return tmpString.substring(0, tmpString.indexOf("\""));
	}

	private String getDate(String CFDI) {
		String fecha = CFDI.substring(CFDI.indexOf("Fecha=") + 7);
		fecha = fecha.substring(0, fecha.indexOf("\""));
		fecha = fecha.replace("T", " ");
		return fecha;
	}

	private static String pasteAddenda(String xml) {
		xml = xml.replace("<cfdi:Addenda/>", cfdiAddenda);
		return xml;
	}

	private String getUUID(String CFDI) {
		String tmpString = CFDI.substring(CFDI.indexOf("UUID=") + 6);
		return tmpString.substring(0, tmpString.indexOf("\""));
	}

	private static String cfdiAddenda = "<cfdi:Addenda/>";
	private static String cutAddenda(String xml) {
		if (xml.contains("<cfdi:Addenda>")) {
			cfdiAddenda = xml.substring(xml.indexOf("<cfdi:Addenda>"), xml
					.indexOf("</cfdi:Addenda>") + 15);
			xml = xml.replace(cfdiAddenda, "<cfdi:Addenda/>");
		}
		return xml;
	}

	@Override
	public void getCancelCFDI(MLMXDocument documentCFDI) {
		try {
			getToken(documentCFDI);
			final Source response = execute(documentCFDI, MLMXVendorService.SOAPSERVICETYPE_CancelStamp);
			final String responseXML = getResponseXML(response);
			MAttachment attachment = new MAttachment(documentCFDI.getCtx(), documentCFDI
					.get_Table_ID(), documentCFDI.getLMX_Document_ID(), documentCFDI
					.get_TrxName());
			attachment.setTitle("Acuse de Recibo CFDI");
			String fileName = "Acuse de Recibo CFDI" + documentCFDI.getCFDIUUID();
			String CFDName = "CFD" + fileName + ".xml";
			attachment.addEntry(CFDName, responseXML
					.getBytes("UTF-8"));
			attachment.addTextMsg(responseXML);
			attachment.saveEx();

			MAttachmentNote attachmentNote = new MAttachmentNote(documentCFDI.getCtx(), 0, documentCFDI.get_TrxName());
			attachmentNote.setAD_Attachment_ID(attachment.get_ID());
			attachmentNote.setAD_User_ID(100);
			attachmentNote.setTextMsg(responseXML);
			attachmentNote.setTitle(document.get_ValueAsString(I_C_Invoice.COLUMNNAME_DocumentNo));
			attachmentNote.saveEx();

			documentCFDI.setIsCancelled(true);
			documentCFDI.saveEx();
			return;
		} catch (NoSuchAlgorithmException ex) {
			throw new AdempiereException(ex.getMessage());
		} catch (Exception e) {
			throw new AdempiereException(e.getMessage());
		}
	}

	private String getElement(String xml, String key) {
		Document document = getDocument(xml);
		return document.getElementsByTagName(key).item(0).getTextContent();
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


	/*private String getCFDIXML() {
		try {
			service.getToken(documentCFDI, taxInfo.getPartnerID());
			final Source response = service.execute(documentCFDI,
					MLMXVendorService.SOAPSERVICETYPE_QueryDocument);
			String result = service.parse(response , "");
			String xml = result.substring(result.indexOf("<DatosXML>") + 10,
					result.indexOf("</DatosXML>"));
			CFDI = xml;
			return xml;
		} catch (Exception e) {
			throw new AdempiereException(e);
		}

	}*/


}
