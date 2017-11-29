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

/** Generated Model for LMX_DocType
 *  @author Adempiere (generated) 
 *  @version Release 3.9.0 - $Id$ */
public class X_LMX_DocType extends PO implements I_LMX_DocType, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171129L;

    /** Standard Constructor */
    public X_LMX_DocType (Properties ctx, int LMX_DocType_ID, String trxName)
    {
      super (ctx, LMX_DocType_ID, trxName);
      /** if (LMX_DocType_ID == 0)
        {
			setLMX_DocType_ID (0);
        } */
    }

    /** Load Constructor */
    public X_LMX_DocType (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_LMX_DocType[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Document Type for LMX Localization ID.
		@param LMX_DocType_ID Document Type for LMX Localization ID	  */
	public void setLMX_DocType_ID (int LMX_DocType_ID)
	{
		if (LMX_DocType_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LMX_DocType_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LMX_DocType_ID, Integer.valueOf(LMX_DocType_ID));
	}

	/** Get Document Type for LMX Localization ID.
		@return Document Type for LMX Localization ID	  */
	public int getLMX_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LMX_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Immutable Universally Unique Identifier.
		@param UUID 
		Immutable Universally Unique Identifier
	  */
	public void setUUID (String UUID)
	{
		set_Value (COLUMNNAME_UUID, UUID);
	}

	/** Get Immutable Universally Unique Identifier.
		@return Immutable Universally Unique Identifier
	  */
	public String getUUID () 
	{
		return (String)get_Value(COLUMNNAME_UUID);
	}
}