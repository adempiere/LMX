/**
  * Copyright (C) 2003-2018, e-Evolution Consultants S.A. , http://www.e-evolution.com
  * This program is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 2 of the License, or
  * (at your option) any later version.
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  * You should have received a copy of the GNU General Public License
  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
  * Email: victor.perez@e-evolution.com, http://www.e-evolution.com , http://github.com/e-Evolution
  * Created by victor.perez@e-evolution.com , www.e-evolution.com
  */

package org.eevolution.LMX.process

import java.sql.Timestamp

import org.adempiere.exceptions
import org.adempiere.exceptions.AdempiereException
import org.compiere.model._
import org.eevolution.LMX.model.{I_LMX_Document, MLMXDocument, X_LMX_Document}
import org.eevolution.model.{I_C_TaxGroup, X_C_TaxGroup}

import scala.xml.XML

class LoadInvoiceFromXMLCFDI extends LoadInvoiceFromXMLCFDIAbstract {

  override def prepare(): Unit = {
    super.prepare()
  }

  override def doIt(): String = {
    //Carga archivo con CFDI xml para ser procesado
    val cfdi = XML.loadFile(getFilePathOrName())
    val uuid = (cfdi \\ "TimbreFiscalDigital" \ "@UUID").text
    if (uuid.isEmpty)
      return "@File@ @NotValid@"

    // Obtiene información del Timbre Fiscal Digital
    val fechaTimbrado = (cfdi \\ "TimbreFiscalDigital" \ "@FechaTimbrado").text
    val rfcProvCertificado = (cfdi \\ "TimbreFiscalDigital" \ "@RfcProvCertif").text
    val noCertificadoSAT = Option((cfdi \\ "TimbreFiscalDigital" \ "@NoCertificadoSAT").text).getOrElse((cfdi \\ "TimbreFiscalDigital" \ "@noCertificadoSAT").text)
    val selloSAT = Option((cfdi \\ "TimbreFiscalDigital" \ "@SelloSAT").text).getOrElse((cfdi \\ "TimbreFiscalDigital" \ "@selloSAT").text)
    val cadenaCFDI = Option((cfdi \\ "TimbreFiscalDigital" \ "@CadenaOriginalTimbre").text).getOrElse((cfdi \\ "TimbreFiscalDigital" \ "@cadenaOriginalTimbre").text)
    // Agrega log para el proceso de los datos obtenidos del CFDI
    val logTimbrado = s" Factura UUID : ${uuid} Fecha Timbrado : ${fechaTimbrado}  Rfc Proveedor Certificado ${rfcProvCertificado} No Certificado SAT : ${noCertificadoSAT} Sello SAT ${selloSAT}"
    addLog(logTimbrado)
    // Obtine Datos del Comprobate
    val cfdiCertificado = Option((cfdi \\ "Comprobante" \ "@Certificado").text).getOrElse((cfdi \\ "Comprobante" \ "@certificado").text)
    val condicionesDePago = Option((cfdi \\ "Comprobante" \ "@CondicionesDePago").text).getOrElse((cfdi \\ "Comprobante" \ "@CondicionesDePago").text)
    val fecha = Option((cfdi \\ "Comprobante" \ "@Fecha").text).getOrElse((cfdi \\ "Comprobante" \ "@fecha").text)
    val folio = Option((cfdi \\ "Comprobante" \ "@Folio").text).getOrElse((cfdi \\ "Comprobante" \ "@folio").text)
    val formaPago = Option((cfdi \\ "Comprobante" \ "@FormaPago").text).getOrElse((cfdi \\ "Comprobante" \ "@formaPago").text)
    val lugarExpedicion = (cfdi \\ "Comprobante" \ "@LugarExpedicion").text
    val metodoPago = Option((cfdi \\ "Comprobante" \ "@MetodoPago").text).getOrElse((cfdi \\ "Comprobante" \ "@metodoPago").text)
    val moneda = (cfdi \\ "Comprobante" \ "@Moneda").text
    val noCertificado = Option((cfdi \\ "Comprobante" \ "@NoCertificado").text).getOrElse((cfdi \\ "Comprobante" \ "@noCertificado").text)
    val sello = Option((cfdi \\ "Comprobante" \ "@Sello").text).getOrElse((cfdi \\ "Comprobante" \ "@sello").text)
    val serie = Option((cfdi \\ "Comprobante" \ "@Serie").text).getOrElse((cfdi \\ "Comprobante" \ "@serie").text)
    val subTotal = Option((cfdi \\ "Comprobante" \ "@SubTotal").text).getOrElse((cfdi \\ "Comprobante" \ "@subTotal").text)
    val total = Option((cfdi \\ "Comprobante" \ "@Total").text).getOrElse((cfdi \\ "Comprobante" \ "@total").text)
    val tipoCambio = (cfdi \\ "Comprobante" \ "@TipoCambio").text
    val tipoDeComprobante = Option((cfdi \\ "Comprobante" \ "@TipoDeComprobante").text).getOrElse((cfdi \\ "Comprobante" \ "@tipoDeComprobante").text)
    val version = Option((cfdi \\ "Comprobante" \ "@Version").text).getOrElse((cfdi \\ "Comprobante" \ "@version").text)
    val usoCFDI = Option((cfdi \\ "Receptor" \ "@UsoCFDI").text).getOrElse((cfdi \\ "Comprobante" \ "@usoCFDI").text)
    // Obiene Datos del Emidor
    val rfcEmisor = Option((cfdi \\ "Emisor" \ "@Rfc").text).getOrElse((cfdi \\ "Emisor" \ "@rfc").text)
    val nombreEmisor = Option((cfdi \\ "Emisor" \ "@Nombre").text).getOrElse((cfdi \\ "Emisor" \ "@nombre").text)
    val regimenFiscalEmisor = Option((cfdi \\ "Emisor" \ "@RegimenFiscal").text).getOrElse((cfdi \\ "Emisor" \ "@regimenFiscal").text)
    val logEmisor = s"RFC ${rfcEmisor} Nombre : ${nombreEmisor} Regimen Fiscal : ${regimenFiscalEmisor}"
    )
    addLog(logEmisor)
    // Genera o Acualiza Factura basado en los datos del CFDI
    val invoice = createInvoice(uuid, rfcEmisor, nombreEmisor, regimenFiscalEmisor, folio, fechaTimbrado, moneda, null, selloSAT, sello, fechaTimbrado, usoCFDI, cfdi.toString())
    // Agrega Lineas de Factura basado en los datos del CFDI
    val conceptos = (cfdi \\ "Conceptos")


    conceptos.foreach(linea => {
      val claveProdServ = Option((linea \\ "Concepto" \ "@ClaveProdServ").text).getOrElse((linea \\ "Concepto" \ "@claveProdServ").text)
      val cantidad = Option((linea \\ "Concepto" \ "@Cantidad").text).getOrElse((linea \\ "Concepto" \ "@cantidad").text)
      val claveUnidad = Option((linea \\ "Concepto" \ "@ClaveUnidad").text).getOrElse((linea \\ "Concepto" \ "@claveUnidad").text)
      val unidad = Option((linea \\ "Concepto" \ "@Unidad").text).getOrElse((linea \\ "Concepto" \ "@unidad").text)
      val descripcion = Option((linea \\ "Concepto" \ "@Descripcion").text).getOrElse((linea \\ "Concepto" \ "@descripcion").text)
      val valorUnitario = Option((linea \\ "Concepto" \ "@ValorUnitario").text).getOrElse((linea \\ "Concepto" \ "@valorUnitario").text)
      val importe = Option((linea \\ "Concepto" \ "@Importe").text).getOrElse((linea \\ "Concepto" \ "@importe").text)
      val invoiceLine = createInvoiceLine(invoice, claveProdServ, descripcion, claveUnidad, new java.math.BigDecimal(cantidad), new java.math.BigDecimal(valorUnitario))
      println(s"Valve de Producto / Serviceio : ${claveProdServ} Cantidad : ${cantidad} Clave Unidad : ${claveUnidad} Unidad : ${unidad} Descripción: ${descripcion} Valor Unitario : ${valorUnitario} Importe : ${importe}")
    })
    "@Ok@"
  }

  /**
    * Crea factura basado en los datos del CFDI
    * Create Invoice based on the CFDI data
    *
    * @param uuid
    * @param taxId
    * @param partnerName
    * @param taxGroupValue
    * @param documentNo
    * @param dateInvoicedString
    * @param currencyISO
    * @param satCertificate
    * @param satSeal
    * @param cfdiSeal
    * @param cfdiDate
    * @param cfdiUse
    * @param cfdiXML
    * @return
    */
  def createInvoice(uuid: String,
                    taxId: String,
                    partnerName: String,
                    taxGroupValue: String,
                    documentNo: String,
                    dateInvoicedString: String,
                    currencyISO: String,
                    satCertificate: String,
                    satSeal: String,
                    cfdiSeal: String,
                    cfdiDate: String,
                    cfdiUse: String,
                    cfdiXML: String
                   ): MInvoice = {
    if (existsCFDI(uuid)) {
      throw new AdempiereException(" @CFDIUUID@ @AlreadyExists@")
    }
    else {
      // Crear o Obtine el Socio de Negocio basado en los datos del CFDI
      val partner = getOrCreatePartner(taxId, partnerName, taxGroupValue)
      // Crear factura de Cuentas por Pagar basado en los datos del CFDI
      val invoice = new MInvoice(getCtx, getRecord_ID, get_TrxName())
      invoice.setDocumentNo(documentNo)
      val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
      val dateInvoiced = new Timestamp(format.parse(dateInvoicedString).getTime)
      invoice.setDateInvoiced(dateInvoiced)
      invoice.setBPartner(partner)
      val documentType = getDocumentType()
      if (documentType.isDefined) {
        invoice.setC_DocTypeTarget_ID(documentType.get.getC_DocType_ID)
        invoice.setC_DocType_ID(documentType.get.getC_DocType_ID)
      }
      val currentOption = getCurrencyOption(currencyISO)
      if (currentOption.isDefined) {
        val currency = currentOption.get
        invoice.setC_Currency_ID(currency.getC_Currency_ID)
        val priceList = getPriceList(currency.getC_Currency_ID)
        if (priceList.isDefined)
          invoice.setM_PriceList_ID(priceList.get.getM_PriceList_ID)
      }
      invoice.saveEx()

      //Crear Documento CFDI para adjuntar XML de la factura de Cuentas por Pagar
      val cfdi = new MLMXDocument(invoice)
      cfdi.setCFDIUUID(uuid)
      cfdi.setCFDISATCertificate(satCertificate)
      cfdi.setTaxID(taxId)
      cfdi.setCFDISATSeal(satSeal)
      cfdi.setCFDISeal(cfdiSeal)
      cfdi.setCFDISealingDate(cfdiDate)
      cfdi.setCFDIXML(cfdiXML)
      cfdi.setTipoDeComprobante(X_LMX_Document.TIPODECOMPROBANTE_Egreso)
      cfdi.setUsoCFDI(cfdiUse)
      cfdi.saveEx()
      // Incluye el Adjunto del archivo CFDI xml
      val attachment = new MAttachment(cfdi.getCtx, cfdi.get_Table_ID, cfdi.getLMX_Document_ID, cfdi.get_TrxName)
      attachment.setTitle("CFD")
      val CFDName = "CFD" + cfdi.getCFDIUUID + ".xml"
      attachment.addEntry(CFDName, cfdiXML.getBytes("UTF-8"))
      attachment.addTextMsg(cfdi.getCFDIString)
      attachment.saveEx()
      val attachmentNote = new MAttachmentNote(cfdi.getCtx, 0, cfdi.get_TrxName)
      attachmentNote.setAD_Attachment_ID(attachment.get_ID)
      attachmentNote.setAD_User_ID(100)
      attachmentNote.setTitle(cfdi.getCFDIUUID)
      attachmentNote.setTextMsg(cfdiXML)
      attachmentNote.saveEx()
      invoice
    }
  }

  /**
    * Obtiene o Crea un nuevo Socio de Negocio
    * Get or Create a new Business Partner
    *
    * @param taxId
    * @param partnerName
    * @param taxGroupValue
    * @return
    */
  def getOrCreatePartner(taxId: String, partnerName: String, taxGroupValue: String): MBPartner = {
    if (taxId == null || taxId.isEmpty || partnerName == null || partnerName.isEmpty || taxGroupValue.isEmpty || taxGroupValue == null)
      throw new exceptions.AdempiereException("@TaxID@ @PartnerName@ @C_TaxGroup_ID@ @NotValid@")

    val partnerOption = Option(MBPartner.get(getCtx, taxId))
    partnerOption.getOrElse({
      val partner = new MBPartner(getCtx, 0, get_TrxName())
      partner.setValue(taxId)
      partner.setTaxID(taxId)
      partner.setName(partnerName)
      val taxGroupOption = getTaxGroup(taxGroupValue)
      if (taxGroupOption.isDefined)
        partner.setC_BP_Group_ID(taxGroupOption.get.getC_TaxGroup_ID)

      val partnerGroup = getPartnerGroup()
      if (partnerGroup.isDefined)
        partner.setC_BP_Group_ID(partnerGroup.get.getC_BP_Group_ID)
      partner.saveEx()
      partner
    })
  }

  /**
    * Obtiene el grupo de impuesto basado en tipo de regimen del SAT
    * Get the tax group based on the SAT code
    *
    * @param taxGroupValue
    * @return
    */
  def getTaxGroup(taxGroupValue: String): Option[X_C_TaxGroup] = {
    return Option(
      new Query(getCtx, I_C_TaxGroup.Table_Name, I_C_TaxGroup.COLUMNNAME_Value + "=?", get_TrxName())
        .setClient_ID()
        .setParameters(taxGroupValue)
        .first()
    )
  }

  /**
    * Obtiene grupo de socio predeterminado
    * Get business partner group by default
    *
    * @return
    */
  def getPartnerGroup(): Option[X_C_BP_Group] = {
    return Option(
      new Query(getCtx, I_C_BP_Group.Table_Name, I_C_BP_Group.COLUMNNAME_IsDefault + "=?", get_TrxName())
        .setClient_ID()
        .setParameters("Y")
        .first()
    )
  }

  /**
    * Obtiene tipo de documento
    * Get document Type
    *
    * @return
    */
  def getDocumentType(): Option[MDocType] = {
    return Option(MDocType.getOfDocBaseType(getCtx, X_C_DocType.DOCBASETYPE_APInvoice).head)
  }

  /**
    * Obtiene moneda basado en codigo del SAT
    * Get currenty baded on SAT code
    *
    * @param currencyISO
    * @return
    */
  def getCurrencyOption(currencyISO: String): Option[MCurrency] = {
    Option(MCurrency.get(getCtx, currencyISO))
  }

  /**
    * Obtiene liata de precios de compra
    * Get price list for purchasing
    *
    * @param currencyId
    * @return
    */
  def getPriceList(currencyId: Integer): Option[MPriceList] = {
    val whereClause = I_M_PriceList.COLUMNNAME_C_Currency_ID + "=? AND " + I_M_PriceList.COLUMNNAME_IsSOPriceList + "=?"
    return Option(
      new Query(getCtx, I_M_PriceList.Table_Name, whereClause, get_TrxName())
        .setClient_ID()
        .setParameters(currencyId, "N")
        .first()
    )
  }

  /**
    * Valida si existe un CFDI badado en UUID
    * Validate if exist a CFDI based on UUID
    *
    * @param uuid
    * @return
    */
  def existsCFDI(uuid: String): Boolean = {
    return new Query(getCtx, I_LMX_Document.Table_Name, I_LMX_Document.COLUMNNAME_CFDIUUID + "=?", get_TrxName())
      .setClient_ID()
      .setParameters(uuid)
      .`match`()
  }

  /**
    * Agrega lineas a la factura basado en las lineas del complemento del CFDI
    * Add lines to invoice based on the complement lines from CFDI
    *
    * @param invoice
    * @param productValue
    * @param description
    * @param uomValue
    * @param quantity
    * @param price
    * @return
    */
  def createInvoiceLine(invoice: MInvoice,
                        productValue: String,
                        description: String,
                        uomValue: String,
                        quantity: java.math.BigDecimal,
                        price: java.math.BigDecimal): MInvoiceLine = {

    //Valida si la unidad de medida indicada en el CFDI existe en el sistema para el producto
    val uom = getUOM(uomValue)
    if (!uom.isDefined)
      throw new exceptions.AdempiereException(s"@C_UOM_ID@ @NotFound@ $uomValue @ProductValue@ $productValue")
    //Valida si el producto indicado en el CFDI existe en el sistema
    val product = getProduct(productValue, uom.get.getC_UOM_ID)
    if (!product.isDefined)
      throw new exceptions.AdempiereException(s"@M_Product_ID@ @NotFound@ $productValue @C_UOM_ID@ ${uom.get.getName}")
    //Crea una nueva linea a la factura de CxP
    val invoiceLine = new MInvoiceLine(invoice);
    invoiceLine.setM_Product_ID(product.get.getM_Product_ID, uom.get.getC_UOM_ID)
    invoiceLine.setDescription(description)
    invoiceLine.setQtyEntered(quantity)
    invoiceLine.setQtyInvoiced(quantity)
    invoiceLine.setPrice(price)
    invoiceLine.saveEx()
    invoiceLine
  }

  /**
    * Obtiene unidad de Medida basado en codigo del SAT
    * Get unit of measure based on Sat code
    *
    * @param uomValue
    * @return
    */
  def getUOM(uomValue: String): Option[MUOM] = {
    val whereClause = I_C_UOM.COLUMNNAME_Description + "=?"
    return Option(
      new Query(getCtx, I_C_UOM.Table_Name, whereClause, get_TrxName())
        .setParameters(uomValue)
        .first()
    )
  }

  /**
    * Obtiene Producto basado en el codigo del SAT
    * Get product based on SAT Code
    *
    * @param productValue
    * @param uomId
    * @return
    */
  def getProduct(productValue: String, uomId: Integer): Option[MProduct] = {

    val whereClause = "EXISTS ( SELECT 1 FROM C_TaxType tt WHERE tt.Value = ? AND tt.C_TaxType_ID = M_Product.C_TaxType_ID) AND M_Product.C_UOM_ID=?"
    return Option(
      new Query(getCtx, I_M_Product.Table_Name, whereClause, get_TrxName())
        .setClient_ID()
        .setParameters(productValue, uomId)
        .first()
    )
  }

}

/** *
  * Ejecutar Aplicación para prueba
  * Execute Aplication for test
  */
object LoadInvoiceFromXMLCFDI extends App {
  val loadCFDI = new LoadInvoiceFromXMLCFDI()
  loadCFDI.doIt()
}
