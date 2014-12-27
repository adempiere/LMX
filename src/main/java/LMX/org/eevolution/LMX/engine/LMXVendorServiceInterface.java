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

package org.eevolution.LMX.engine;

/**
 * Created by e-Evolution on 26/12/13.
 */
public interface LMXVendorServiceInterface {

 
    int getSeqNo();

   
    String getSOAPAction();

   
    String getSOAPBinding();

   
    String getSOAPEndpointAddress();

   
    String getSOAPPort();

   
    String getSOAPServiceName();

   
    String getSOAPTargetNS();

    String getSOAPRequest();

   
    void setSOAPAction(String SOAPAction);

   
    void setSeqNo(int SeqNo);

   
    void setSOAPBinding(String SOAPBinding);

   
    void setSOAPEndpointAddress(String SOAPEndpointAddress);

   
    void setSOAPPort(String SOAPPort);

   
    void setSOAPServiceName(String SOAPServiceName);

   
    void setSOAPTargetNS(String SOAPTargetNS);
}
