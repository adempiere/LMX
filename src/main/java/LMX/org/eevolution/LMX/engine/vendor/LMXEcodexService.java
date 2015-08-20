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
import java.security.NoSuchAlgorithmException;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MImage;
import org.compiere.model.MInvoice;
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
import  javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

/**
 * Created by e-Evolution on 26/12/13.
 */
public class LMXEcodexService implements LMXVendorInterface {

    public MLMXVendor m_vendor;

    public LMXEcodexService()
    {
        m_vendor = MLMXVendor.getByClassName(getClass().getCanonicalName());
        if (m_vendor == null)
            throw new AdempiereException("Implementación para este proveedor de servicios no existe: "+ getClass().getCanonicalName());
    }

    public Source execute(MLMXDocument document  , String SOAPType)  throws Exception {
    {
            LMXVendorServiceInterface service = m_vendor.getService(SOAPType);
            final WebServiceConnector wsc = new WebServiceConnector();
            String request = Env.parseVariable(service.getSOAPRequest(), document, document.get_TrxName(), false);
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

    }

	@Override
	public String parse(Source response)
			throws TransformerFactoryConfigurationError, TransformerException {
		return "";
	}
	
	
	public void generateQR(MLMXDocument documentCFDI) {
		try {
			final Source response = execute(documentCFDI,
					MLMXVendorService.SOAPSERVICETYPE_QR);
			String res = parse(response);
			String qrString = res.substring(res.indexOf("<Imagen>") + 8, res
					.indexOf("</Imagen>"));
			InputStream qrInput = new ByteArrayInputStream(qrString.getBytes());
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] qrImage = decoder.decodeBuffer(qrInput);

			MImage image = new MImage(documentCFDI.getCtx(), 0, documentCFDI
					.get_TrxName());
			image.setName(String.valueOf(documentCFDI.get_ID()));
			image.setDescription(String.valueOf(documentCFDI.getC_Invoice_ID()));
			image.setBinaryData(qrImage);
			image.saveEx();

			documentCFDI.setCFDIQR_ID(image.getAD_Image_ID());
			documentCFDI.saveEx();

		} catch (Exception e) {
			throw new AdempiereException(e);
		}

	}
	
	public String getToken(MLMXDocument documentCFDI, String partnetID) {

		try {
			Source response = execute(documentCFDI,
					MLMXVendorService.SOAPSERVICETYPE_Token);
			final String xml = parse(response);
			String token = getElement(xml, "Token");
			String toHash = String.format("%s|%s", partnetID,
					token);
			byte[] as = toHash.getBytes("UTF-8");
			String toHash2 = new String(as, "UTF-8");
			SHA1 sha1 = new SHA1();
			token = sha1.getHash(toHash2);
			documentCFDI.setCFDIToken(token);
			documentCFDI.saveEx();
			return token;
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
}
