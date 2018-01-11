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

package org.eevolution.LMX.util;


import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import java.io.StringReader;


public class WebServiceConnector {
 
  private String targetNS;
  private String serviceName;
  private String portName;
  private String request;
  private String endpointAddress;
  private String binding;
  private String soapAction;
 
  private Source response;
 
  
  public void executeConnector() throws Exception {
    final QName serviceQName = new QName(targetNS, serviceName);
    final QName portQName = new QName(targetNS, portName);
    final Service service = Service.create(serviceQName);
    service.addPort(portQName, binding, endpointAddress);
    final Dispatch<Source> dispatch = service.createDispatch(portQName, Source.class, Service.Mode.MESSAGE);
   
    if (soapAction != null) {
      dispatch.getRequestContext().put(BindingProvider.SOAPACTION_USE_PROPERTY, true);
      dispatch.getRequestContext().put(BindingProvider.SOAPACTION_URI_PROPERTY, soapAction);
      dispatch.getRequestContext().put("Accept","application/xml; charset=utf-8");
      dispatch.getRequestContext().put("Content-Type", "application/xml; charset=utf-8");
    }
   
    this.response = dispatch.invoke(new StreamSource(new StringReader(request)));
  }
 
  public Source getResponse() {
    return response;
  }
 
  public void setEndpointAddress(String endpointAddress) {
    this.endpointAddress = endpointAddress;
  }
 
  public void setBinding(String binding) {
    this.binding = binding;
  }
 
  public void setTargetNS(String targetNS) {
    this.targetNS = targetNS;
  }
 
  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }
 
  public void setPortName(String portName) {
    this.portName = portName;
  }
 
  public void setRequest(String request) {
    this.request = request;
  }
 
  public void setSoapAction(String soapAction) {
    this.soapAction = soapAction;
  }
}