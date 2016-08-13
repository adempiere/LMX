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
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;

import org.adempiere.exceptions.AdempiereException;
import org.apache.commons.lang.StringEscapeUtils;
import org.compiere.util.Env;
import org.eevolution.LMX.engine.LMXVendorInterface;
import org.eevolution.LMX.engine.LMXVendorServiceInterface;
import org.eevolution.LMX.model.MLMXDocument;
import org.eevolution.LMX.model.MLMXVendor;
import org.eevolution.LMX.util.WebServiceConnector;
import org.w3c.dom.NodeList;

/**
 * Created by e-Evolution on 26/12/13.
 */
public class LMXFoliosDigitalesService implements LMXVendorInterface {

	public MLMXVendor m_vendor;

	public LMXFoliosDigitalesService()
	{
		m_vendor = MLMXVendor.getByClassName(getClass().getCanonicalName());
		if (m_vendor == null)
			throw new AdempiereException("Implementación para este proveedor de servicios no existe: "+ getClass().getCanonicalName());
	}

	public Source execute(MLMXDocument document  , String SOAPType)  throws Exception {
		LMXVendorServiceInterface service = m_vendor.getService(SOAPType);
		final WebServiceConnector wsc = new WebServiceConnector();
		document.setCFDIXML(document.getCFDIXML().replace("\n", "").replaceAll(" +", " "));
		document.saveEx();
		String request = Env.parseVariable(service.getSOAPRequest(), document, document.get_TrxName(), true);
		request = Env.parseVariable(request, m_vendor, m_vendor.get_TrxName(), false);
		//request = request.replaceAll("\n", "");
		//System.out.println("Llamada del servicio :" + request);
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

	public String parse(final Source response)
			throws TransformerFactoryConfigurationError, TransformerException, IOException, SOAPException {
		assert response != null;
		final Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();

		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF8");
		final StringWriter writer = new StringWriter();
		final StreamResult result = new StreamResult(writer);
		transformer.transform(response, result);

		//String respxml = StringEscapeUtils.unescapeXml(writer.toString());
        String respxml = "";
        
        respxml = getXMLSealed(writer.toString());


		return respxml;
	}

public String getXMLSealed(final String respxml) throws AdempiereException, IOException, SOAPException {
        
        String[] respuesta = null;

            MessageFactory factory = MessageFactory.newInstance();
            SOAPMessage message = factory.createMessage(
                    new MimeHeaders(),
                    new ByteArrayInputStream(respxml.getBytes(Charset.forName("UTF-8"))));
            
            SOAPBody body = message.getSOAPBody();
            NodeList returnList = body.getElementsByTagName("TimbrarPruebaCFDIResult");
            NodeList innerRes = returnList.item(0).getChildNodes();
            
            boolean failed = false;
            
            respuesta = new String[innerRes.getLength()];
			String messageError = null;
            
            for(int i = 0; i < innerRes.getLength(); i++) {
                respuesta[i] = innerRes.item(i).getTextContent();
                
                if(i < 3 && !respuesta[i].equals("")) {
                    failed = true;
					messageError = respuesta[i];
					//System.out.println(messageError);
                }
            }
            
            if(failed)
				throw new AdempiereException(messageError);
            
            respuesta[3] = StringEscapeUtils.unescapeXml(respuesta[3]);
        return respuesta[3];
    }


	@Override
	public void generateQR(MLMXDocument documentCFDI) {

	}

	@Override
	public String getToken(MLMXDocument documentCFDI, String partnerID) {
		return "No Supported";
	}
}