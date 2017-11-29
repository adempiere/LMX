/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.										*
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net or http://www.adempiere.net/license.html         *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.eevolution.LMX.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for LMX_Document
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_LMX_Document extends PO implements I_LMX_Document, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171129L;

    /** Standard Constructor */
    public X_LMX_Document (Properties ctx, int LMX_Document_ID, String trxName)
    {
      super (ctx, LMX_Document_ID, trxName);
      /** if (LMX_Document_ID == 0)
        {
			setLMX_Document_ID (0);
        } */
    }

    /** Load Constructor */
    public X_LMX_Document (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_LMX_Document[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Table getAD_Table() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Table)MTable.get(getCtx(), org.compiere.model.I_AD_Table.Table_Name)
			.getPO(getAD_Table_ID(), get_TrxName());	}

	/** Set Table.
		@param AD_Table_ID 
		Database Table information
	  */
	public void setAD_Table_ID (int AD_Table_ID)
	{
		if (AD_Table_ID < 1) 
			set_Value (COLUMNNAME_AD_Table_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Table_ID, Integer.valueOf(AD_Table_ID));
	}

	/** Get Table.
		@return Database Table information
	  */
	public int getAD_Table_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Table_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set CFDI QR ID.
		@param CFDIQR_ID CFDI QR ID	  */
	public void setCFDIQR_ID (int CFDIQR_ID)
	{
		if (CFDIQR_ID < 1) 
			set_Value (COLUMNNAME_CFDIQR_ID, null);
		else 
			set_Value (COLUMNNAME_CFDIQR_ID, Integer.valueOf(CFDIQR_ID));
	}

	/** Get CFDI QR ID.
		@return CFDI QR ID	  */
	public int getCFDIQR_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CFDIQR_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Number of certificate of SAT.
		@param CFDISATCertificate Number of certificate of SAT	  */
	public void setCFDISATCertificate (String CFDISATCertificate)
	{
		set_Value (COLUMNNAME_CFDISATCertificate, CFDISATCertificate);
	}

	/** Get Number of certificate of SAT.
		@return Number of certificate of SAT	  */
	public String getCFDISATCertificate () 
	{
		return (String)get_Value(COLUMNNAME_CFDISATCertificate);
	}

	/** Set CFDI SAT Seal.
		@param CFDISATSeal CFDI SAT Seal	  */
	public void setCFDISATSeal (String CFDISATSeal)
	{
		set_Value (COLUMNNAME_CFDISATSeal, CFDISATSeal);
	}

	/** Get CFDI SAT Seal.
		@return CFDI SAT Seal	  */
	public String getCFDISATSeal () 
	{
		return (String)get_Value(COLUMNNAME_CFDISATSeal);
	}

	/** Set CFDI Seal.
		@param CFDISeal CFDI Seal	  */
	public void setCFDISeal (String CFDISeal)
	{
		set_Value (COLUMNNAME_CFDISeal, CFDISeal);
	}

	/** Get CFDI Seal.
		@return CFDI Seal	  */
	public String getCFDISeal () 
	{
		return (String)get_Value(COLUMNNAME_CFDISeal);
	}

	/** Set CFDI Sealing Date.
		@param CFDISealingDate CFDI Sealing Date	  */
	public void setCFDISealingDate (String CFDISealingDate)
	{
		set_Value (COLUMNNAME_CFDISealingDate, CFDISealingDate);
	}

	/** Get CFDI Sealing Date.
		@return CFDI Sealing Date	  */
	public String getCFDISealingDate () 
	{
		return (String)get_Value(COLUMNNAME_CFDISealingDate);
	}

	/** Set CFDI String.
		@param CFDIString 
		CFDI String
	  */
	public void setCFDIString (String CFDIString)
	{
		set_Value (COLUMNNAME_CFDIString, CFDIString);
	}

	/** Get CFDI String.
		@return CFDI String
	  */
	public String getCFDIString () 
	{
		return (String)get_Value(COLUMNNAME_CFDIString);
	}

	/** Set CFDI Token.
		@param CFDIToken CFDI Token	  */
	public void setCFDIToken (String CFDIToken)
	{
		set_Value (COLUMNNAME_CFDIToken, CFDIToken);
	}

	/** Get CFDI Token.
		@return CFDI Token	  */
	public String getCFDIToken () 
	{
		return (String)get_Value(COLUMNNAME_CFDIToken);
	}

	/** Set UUID of CFDI.
		@param CFDIUUID UUID of CFDI	  */
	public void setCFDIUUID (String CFDIUUID)
	{
		set_Value (COLUMNNAME_CFDIUUID, CFDIUUID);
	}

	/** Get UUID of CFDI.
		@return UUID of CFDI	  */
	public String getCFDIUUID () 
	{
		return (String)get_Value(COLUMNNAME_CFDIUUID);
	}

	/** Set XML of CFDI.
		@param CFDIXML XML of CFDI	  */
	public void setCFDIXML (String CFDIXML)
	{
		set_Value (COLUMNNAME_CFDIXML, CFDIXML);
	}

	/** Get XML of CFDI.
		@return XML of CFDI	  */
	public String getCFDIXML () 
	{
		return (String)get_Value(COLUMNNAME_CFDIXML);
	}

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (org.compiere.model.I_C_Invoice)MTable.get(getCtx(), org.compiere.model.I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_Value (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_Value (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Cancelled.
		@param IsCancelled 
		The transaction was cancelled
	  */
	public void setIsCancelled (boolean IsCancelled)
	{
		set_Value (COLUMNNAME_IsCancelled, Boolean.valueOf(IsCancelled));
	}

	/** Get Cancelled.
		@return The transaction was cancelled
	  */
	public boolean isCancelled () 
	{
		Object oo = get_Value(COLUMNNAME_IsCancelled);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set LMX Document Information ID.
		@param LMX_Document_ID LMX Document Information ID	  */
	public void setLMX_Document_ID (int LMX_Document_ID)
	{
		if (LMX_Document_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LMX_Document_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LMX_Document_ID, Integer.valueOf(LMX_Document_ID));
	}

	/** Get LMX Document Information ID.
		@return LMX Document Information ID	  */
	public int getLMX_Document_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LMX_Document_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Record ID.
		@param Record_ID 
		Direct internal record ID
	  */
	public void setRecord_ID (int Record_ID)
	{
		if (Record_ID < 0) 
			set_Value (COLUMNNAME_Record_ID, null);
		else 
			set_Value (COLUMNNAME_Record_ID, Integer.valueOf(Record_ID));
	}

	/** Get Record ID.
		@return Direct internal record ID
	  */
	public int getRecord_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Record_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Tax ID.
		@param TaxID 
		Tax Identification
	  */
	public void setTaxID (String TaxID)
	{
		set_Value (COLUMNNAME_TaxID, TaxID);
	}

	/** Get Tax ID.
		@return Tax Identification
	  */
	public String getTaxID () 
	{
		return (String)get_Value(COLUMNNAME_TaxID);
	}

	/** TipoDeComprobante AD_Reference_ID=53947 */
	public static final int TIPODECOMPROBANTE_AD_Reference_ID=53947;
	/** Ingreso = I */
	public static final String TIPODECOMPROBANTE_Ingreso = "I";
	/** Egreso = E */
	public static final String TIPODECOMPROBANTE_Egreso = "E";
	/** Traslado = T */
	public static final String TIPODECOMPROBANTE_Traslado = "T";
	/** Nómina = N */
	public static final String TIPODECOMPROBANTE_Nómina = "N";
	/** Pago = P */
	public static final String TIPODECOMPROBANTE_Pago = "P";
	/** Set TipoDeComprobante.
		@param TipoDeComprobante TipoDeComprobante	  */
	public void setTipoDeComprobante (String TipoDeComprobante)
	{

		set_Value (COLUMNNAME_TipoDeComprobante, TipoDeComprobante);
	}

	/** Get TipoDeComprobante.
		@return TipoDeComprobante	  */
	public String getTipoDeComprobante () 
	{
		return (String)get_Value(COLUMNNAME_TipoDeComprobante);
	}

	/** TipoRelacion AD_Reference_ID=53990 */
	public static final int TIPORELACION_AD_Reference_ID=53990;
	/** Nota de crédito de los documentos relacionados = 01 */
	public static final String TIPORELACION_NotaDeCréditoDeLosDocumentosRelacionados = "01";
	/** Nota de débito de los documentos relacionados = 02 */
	public static final String TIPORELACION_NotaDeDébitoDeLosDocumentosRelacionados = "02";
	/** Devolución de mercancía sobre facturas o traslados previos = 03 */
	public static final String TIPORELACION_DevoluciónDeMercancíaSobreFacturasOTrasladosPrevios = "03";
	/** Sustitución de los CFDI previos = 04 */
	public static final String TIPORELACION_SustituciónDeLosCFDIPrevios = "04";
	/** Traslados de mercancias facturados previamente. = 05 */
	public static final String TIPORELACION_TrasladosDeMercanciasFacturadosPreviamente = "05";
	/** Factura generada por los traslados previos = 06 */
	public static final String TIPORELACION_FacturaGeneradaPorLosTrasladosPrevios = "06";
	/** CFDI por aplicación de anticipo = 07 */
	public static final String TIPORELACION_CFDIPorAplicaciónDeAnticipo = "07";
	/** Set Tipo Relacion.
		@param TipoRelacion Tipo Relacion	  */
	public void setTipoRelacion (String TipoRelacion)
	{

		set_Value (COLUMNNAME_TipoRelacion, TipoRelacion);
	}

	/** Get Tipo Relacion.
		@return Tipo Relacion	  */
	public String getTipoRelacion () 
	{
		return (String)get_Value(COLUMNNAME_TipoRelacion);
	}

	/** UsoCFDI AD_Reference_ID=53948 */
	public static final int USOCFDI_AD_Reference_ID=53948;
	/** Adquisición de mercancías = G01 */
	public static final String USOCFDI_AdquisiciónDeMercancías = "G01";
	/** Devoluciones, descuentos o bonificaciones = G02 */
	public static final String USOCFDI_DevolucionesDescuentosOBonificaciones = "G02";
	/** Gastos en general = G03 */
	public static final String USOCFDI_GastosEnGeneral = "G03";
	/** Set UsoCFDI.
		@param UsoCFDI 
		UsoCFDI
	  */
	public void setUsoCFDI (String UsoCFDI)
	{

		set_Value (COLUMNNAME_UsoCFDI, UsoCFDI);
	}

	/** Get UsoCFDI.
		@return UsoCFDI
	  */
	public String getUsoCFDI () 
	{
		return (String)get_Value(COLUMNNAME_UsoCFDI);
	}
}