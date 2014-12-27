<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.2" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:detallista="http://www.sat.gob.mx/detallista"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:cfdi="http://www.sat.gob.mx/cfd/3"
	
	
	xsi:schemaLocation="http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd http://www.sat.gob.mx/detallista
	http://www.sat.gob.mx/sitio_internet/cfd/detallista/detallista.xsd">
	
	
	<xsl:variable name="schemaLoc" select="'http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd http://www.sat.gob.mx/detallista
		http://www.sat.gob.mx/sitio_internet/cfd/detallista/detallista.xsd'"/>
	
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	
	<xsl:template match="LMX_Comprobante">
		<cfdi:Comprobante>
			
			<xsl:attribute name="xsi:schemaLocation" namespace="http://www.w3.org/2001/XMLSchema-instance">
				<xsl:value-of select="$schemaLoc"/>
			</xsl:attribute>
			
			
			<xsl:attribute name="version"><xsl:value-of select="@Version"/></xsl:attribute>
			<xsl:if test="@Serie"> 
				<xsl:attribute name="serie"><xsl:value-of select="@Serie"/></xsl:attribute>
			</xsl:if>
			<xsl:attribute name="folio"><xsl:value-of select="@Folio"/></xsl:attribute>
			<xsl:attribute name="fecha"><xsl:value-of select="@Fecha"/></xsl:attribute>
			<xsl:attribute name="sello"><xsl:value-of select="@Sello"/></xsl:attribute>
			<xsl:attribute name="formaDePago"><xsl:value-of select="@FormaDePago"/></xsl:attribute>
			
			<xsl:attribute name="noCertificado"><xsl:value-of select="@NoCertificado"/></xsl:attribute>
			<xsl:if test="@Certificado">
				<xsl:attribute name="certificado"><xsl:value-of select="@Certificado"/></xsl:attribute>
			</xsl:if>
			<xsl:if test="@CondicionesDePago">
				<xsl:attribute name="condicionesDePago"><xsl:value-of select="@CondicionesDePago"/></xsl:attribute>
			</xsl:if>
			<xsl:attribute name="subTotal"><xsl:value-of select="@SubTotal"/></xsl:attribute>
			<xsl:if test="@Descuento">
				<xsl:attribute name="descuento"><xsl:value-of select="@Descuento"/></xsl:attribute>
			</xsl:if>
			<xsl:if test="@MotivoDescuento">
				<xsl:attribute name="motivoDescuento"><xsl:value-of select="@MotivoDescuento"/></xsl:attribute>
			</xsl:if>
			<xsl:attribute name="total"><xsl:value-of select="@Total"/></xsl:attribute>
			<xsl:attribute name="Moneda"><xsl:value-of select="@Moneda"/></xsl:attribute>
			
			<xsl:if test="@MetodoDePago">
				<xsl:attribute name="metodoDePago"><xsl:value-of select="@MetodoDePago"/></xsl:attribute>
			</xsl:if>
			
			<xsl:attribute name="tipoDeComprobante"><xsl:value-of select="@TipoDeComprobante"/></xsl:attribute>
			
			<xsl:if test="@NumCtaPago">
				<xsl:attribute name="NumCtaPago"><xsl:value-of select="@NumCtaPago"/></xsl:attribute>
			</xsl:if>
			
			<xsl:attribute name="LugarExpedicion"><xsl:value-of select="@EmisorLocalidad"/></xsl:attribute>
			
			<cfdi:Emisor>
				<xsl:attribute name="rfc"><xsl:value-of select="@EmisorRfc"/></xsl:attribute>
				<xsl:attribute name="nombre"><xsl:value-of select="@EmisorNombre"/></xsl:attribute>
				<cfdi:DomicilioFiscal>
					<xsl:attribute name="calle"><xsl:value-of select="@EmisorCalle"/></xsl:attribute>
					<xsl:if test="@EmisorNoExterior">
						<xsl:attribute name="noExterior"><xsl:value-of select="@EmisorNoExterior"/></xsl:attribute>
					</xsl:if>
					<xsl:if test="@EmisorNoInterior">	
						<xsl:attribute name="noInterior"><xsl:value-of select="@EmisorNoInterior"/></xsl:attribute>
					</xsl:if>
					<xsl:if test="@EmisorColonia">	
						<xsl:attribute name="colonia"><xsl:value-of select="@EmisorColonia"/></xsl:attribute>
					</xsl:if>
					<xsl:if test="@EmisorLocalidad">	
						<xsl:attribute name="localidad"><xsl:value-of select="@EmisorLocalidad"/></xsl:attribute>
					</xsl:if>
					<xsl:if test="@EmisorReferencia">	
						<xsl:attribute name="referencia"><xsl:value-of select="@EmisorReferencia"/></xsl:attribute>
					</xsl:if>
					<xsl:attribute name="municipio"><xsl:value-of select="@EmisorMunicipio"/></xsl:attribute>
					<xsl:attribute name="estado"><xsl:value-of select="@EmisorEstado"/></xsl:attribute>
					<xsl:attribute name="pais"><xsl:value-of select="@EmisorPais"/></xsl:attribute>
					<xsl:attribute name="codigoPostal"><xsl:value-of select="@EmisorCodigoPostal"/></xsl:attribute>		        	
				</cfdi:DomicilioFiscal>
				<xsl:if test="@ExpedidoEnPais">        	
					<cfdi:ExpedidoEn>
						<xsl:if test="@ExpedidoEnCalle">
							<xsl:attribute name="calle"><xsl:value-of select="@ExpedidoEnCalle"/></xsl:attribute>
						</xsl:if>
						<xsl:if test="@ExpedidoEnNoExterior">
							<xsl:attribute name="noExterior"><xsl:value-of select="@ExpedidoEnNoExterior"/></xsl:attribute>
						</xsl:if>
						<xsl:if test="@ExpedidoEnNoInterior">
							<xsl:attribute name="noInterior"><xsl:value-of select="@ExpedidoEnNoInterior"/></xsl:attribute>
						</xsl:if>
						<xsl:if test="@ExpedidoEnColonia">
							<xsl:attribute name="colonia"><xsl:value-of select="@ExpedidoEnColonia"/></xsl:attribute>
						</xsl:if>
						<xsl:if test="@ExpedidoEnLocalidad">
							<xsl:attribute name="localidad"><xsl:value-of select="@ExpedidoEnLocalidad"/></xsl:attribute>
						</xsl:if>
						<xsl:if test="@ExpedidoEnReferencia">
							<xsl:attribute name="referencia"><xsl:value-of select="@ExpedidoEnReferencia"/></xsl:attribute>
						</xsl:if>
						<xsl:if test="@ExpedidoEnMunicipio">
							<xsl:attribute name="municipio"><xsl:value-of select="@ExpedidoEnMunicipio"/></xsl:attribute>
						</xsl:if>
						<xsl:if test="@ExpedidoEnEstado">
							<xsl:attribute name="estado"><xsl:value-of select="@ExpedidoEnEstado"/></xsl:attribute>
						</xsl:if>
						<xsl:attribute name="pais"><xsl:value-of select="@ExpedidoEnPais"/></xsl:attribute>
						<xsl:if test="@ExpedidoEnCodigoPostal">
							<xsl:attribute name="codigoPostal"><xsl:value-of select="@ExpedidoEnCodigoPostal"/></xsl:attribute>		        	
						</xsl:if>
					</cfdi:ExpedidoEn>
				</xsl:if>	
				
				<cfdi:RegimenFiscal>
					<xsl:attribute name="Regimen"><xsl:value-of select="@Regimen"/></xsl:attribute>
				</cfdi:RegimenFiscal> 
				
				
			</cfdi:Emisor>
			
			<cfdi:Receptor>
				<xsl:attribute name="rfc"><xsl:value-of select="@ReceptorRfc"/></xsl:attribute>
				<xsl:attribute name="nombre"><xsl:value-of select="@ReceptorNombre"/></xsl:attribute>
				<cfdi:Domicilio>
					<xsl:if test="@ReceptorCalle">
						<xsl:attribute name="calle"><xsl:value-of select="@ReceptorCalle"/></xsl:attribute>
					</xsl:if>
					<xsl:if test="@ReceptorNoExterior">
						<xsl:attribute name="noExterior"><xsl:value-of select="@ReceptorNoExterior"/></xsl:attribute>
					</xsl:if>
					<xsl:if test="@ReceptorNoInterior">
						<xsl:attribute name="noInterior"><xsl:value-of select="@ReceptorNoInterior"/></xsl:attribute>
					</xsl:if>
					<xsl:if test="@ReceptorColonia">
						<xsl:attribute name="colonia"><xsl:value-of select="@ReceptorColonia"/></xsl:attribute>
					</xsl:if>
					<xsl:if test="@ReceptorLocalidad">
						<xsl:attribute name="localidad"><xsl:value-of select="@ReceptorLocalidad"/></xsl:attribute>
					</xsl:if>
					<xsl:if test="@ReceptorReferencia">
						<xsl:attribute name="referencia"><xsl:value-of select="@ReceptorReferencia"/></xsl:attribute>
					</xsl:if>
					<xsl:if test="@ReceptorMunicipio">
						<xsl:attribute name="municipio"><xsl:value-of select="@ReceptorMunicipio"/></xsl:attribute>
					</xsl:if>
					<xsl:if test="@ReceptorEstado">
						<xsl:attribute name="estado"><xsl:value-of select="@ReceptorEstado"/></xsl:attribute>
					</xsl:if>
					<xsl:attribute name="pais"><xsl:value-of select="@ReceptorPais"/></xsl:attribute>
					<xsl:if test="@ReceptorCodigoPostal">
						<xsl:attribute name="codigoPostal"><xsl:value-of select="@ReceptorCodigoPostal"/></xsl:attribute>		        	
					</xsl:if>
				</cfdi:Domicilio>
			</cfdi:Receptor>
			
			<cfdi:Conceptos>
				<xsl:for-each select="LMX_Conceptos/row">
					<cfdi:Concepto>
						<xsl:attribute name="cantidad"><xsl:value-of select="@ConceptoCantidad"/></xsl:attribute>
						<xsl:if test="@ConceptoUnidad">
							<xsl:attribute name="unidad"><xsl:value-of select="@ConceptoUnidad"/></xsl:attribute>
						</xsl:if>
						<xsl:if test="@ConceptoNoIdentificacion">
							<xsl:attribute name="noIdentificacion"><xsl:value-of select="@ConceptoNoIdentificacion"/></xsl:attribute>
						</xsl:if>
						<xsl:attribute name="descripcion"><xsl:value-of select="@ConceptoDescription"/></xsl:attribute>
						<xsl:attribute name="valorUnitario"><xsl:value-of select="@ConceptoValorUnitario"/></xsl:attribute>
						<xsl:attribute name="importe"><xsl:value-of select="@ConceptoImporte"/></xsl:attribute>						
						<xsl:for-each select="LMX_InformacionAduanera">
							<cfdi:InformacionAduanera>
								<xsl:if test="@Numero">
									<xsl:attribute name="numero"><xsl:value-of select="@Numero"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="@Fecha">
									<xsl:attribute name="fecha"><xsl:value-of select="@Fecha"/></xsl:attribute>
	                            </xsl:if>                                
								<xsl:if test="@Aduana">
									<xsl:attribute name="aduana"><xsl:value-of select="@Aduana"/></xsl:attribute>
								</xsl:if>
							</cfdi:InformacionAduanera>							
						</xsl:for-each>
						<xsl:if test="@CuentaPredialNumero">
							<cfdi:CuentaPredial>
								<xsl:attribute name="numero"><xsl:value-of select="@CuentaPredialNumero"/></xsl:attribute>
							</cfdi:CuentaPredial>
						</xsl:if>
						<xsl:if test="@ComplementoConcepto">
							<cfdi:ComplementoConcepto>
								<xsl:attribute name="ComplementoConcepto"><xsl:value-of select="@ComplementoConcepto"/></xsl:attribute>
							</cfdi:ComplementoConcepto>
						</xsl:if>
						<xsl:if test="@ParteCantidad">
							<cfdi:Parte>
								<xsl:attribute name="cantidad"><xsl:value-of select="@ParteCantidad"/></xsl:attribute>
								<xsl:if test="@ParteUnidad">
									<xsl:attribute name="unidad"><xsl:value-of select="@ParteUnidad"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="@ParteNoIdentificacion">
									<xsl:attribute name="noIdentificacion"><xsl:value-of select="@ParteNoIdentificacion"/></xsl:attribute>
								</xsl:if>
								<xsl:attribute name="descripcion"><xsl:value-of select="@ParteDescription"/></xsl:attribute>
								<xsl:if test="@ParteValorUnitario">
									<xsl:attribute name="valorUnitario"><xsl:value-of select="@ParteValorUnitario"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="@ParteImporte">
									<xsl:attribute name="importe"><xsl:value-of select="@ParteImporte"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="@ParteInformacionAduaneraNumero">
									<InformacionAduanera>
										<xsl:attribute name="numero"><xsl:value-of select="@ParteInformacionAduaneranumero"/></xsl:attribute>
										<xsl:attribute name="fecha"><xsl:value-of select="@ParteInformacionAduanerafecha"/></xsl:attribute>
										<xsl:attribute name="aduana"><xsl:value-of select="@ParteInformacionAduaneraaduana"/></xsl:attribute>
									</InformacionAduanera>
								</xsl:if>
							</cfdi:Parte>
						</xsl:if>
					</cfdi:Concepto>
				</xsl:for-each>
			</cfdi:Conceptos>
			
			<cfdi:Impuestos>
				<xsl:if test="@TotalImpuestosRetenidos">
					<xsl:attribute name="totalImpuestosRetenidos"><xsl:value-of select="@TotalImpuestosRetenidos"/></xsl:attribute>
				</xsl:if>
				<xsl:if test="@TotalImpuestosTrasladados">
					<xsl:attribute name="totalImpuestosTrasladados"><xsl:value-of select="@TotalImpuestosTrasladados"/></xsl:attribute>
				</xsl:if>
				
				<xsl:if test="LMX_Retenciones/row/@RetencionImpuesto">
					<cfdi:Retenciones>
						<xsl:for-each select="LMX_Retenciones/row">
							<cfdi:Retencion>
								<xsl:attribute name="impuesto"><xsl:value-of select="@RetencionImpuesto"/></xsl:attribute>
								<xsl:attribute name="importe"><xsl:value-of select="@RetencionImporte"/></xsl:attribute>
							</cfdi:Retencion>
						</xsl:for-each>
					</cfdi:Retenciones>
				</xsl:if>
				
				<xsl:if test="LMX_Traslados/row/@TrasladoImpuesto">
					<cfdi:Traslados>
						<xsl:for-each select="LMX_Traslados/row">	        
							<cfdi:Traslado>
								<xsl:attribute name="impuesto"><xsl:value-of select="@TrasladoImpuesto"/></xsl:attribute>
								<xsl:attribute name="tasa"><xsl:value-of select="@TrasladoTasa"/></xsl:attribute>					    		
								<xsl:attribute name="importe"><xsl:value-of select="@TrasladoImporte"/></xsl:attribute>
							</cfdi:Traslado>
						</xsl:for-each>
					</cfdi:Traslados>
				</xsl:if>
			</cfdi:Impuestos>
			
			<cfdi:Complemento> </cfdi:Complemento>
		</cfdi:Comprobante>
	</xsl:template>
</xsl:stylesheet>
