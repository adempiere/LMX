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

import org.eevolution.LMX.model.MLMXDocument;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

/**
 * Created by e-Evolution on 26/12/13.
 */
public interface LMXVendorInterface {
   public Source execute(MLMXDocument document , String typeService) throws Exception;
   public String parse(final Source response)
			throws TransformerFactoryConfigurationError, TransformerException;
   
   public void generateQR(MLMXDocument documentCFDI);
   
   public String getToken(MLMXDocument documentCFDI, String partnerID);
}