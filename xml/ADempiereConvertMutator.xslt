<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
    xmlns:xs="http://www.w3.org/2001/XMLSchema" 
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>

    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
    <xsl:template match="adempiereData/row">
    <LMX_Comprobante>
    	<xsl:for-each select="element">
			<xsl:variable name="atrname" select="@name" />    			
			<xsl:attribute name="{$atrname}">
				<xsl:value-of select="."/>
			</xsl:attribute>
 		</xsl:for-each>		
 		<xsl:for-each select="adempiereData">
 			<xsl:variable name="elename" select="@name" />
 			<xsl:element name="{$elename}">
 				 <xsl:for-each select="row">
 					<xsl:variable name="subelename" select="prueba"/>
 					<xsl:element name="row">
 						<xsl:for-each select="element">
 							<xsl:variable name="subatrname" select="@name"/>
 							<xsl:attribute name="{$subatrname}">
								<xsl:value-of select="."/>
							</xsl:attribute>
                            </xsl:for-each>
                            <xsl:for-each select="adempiereData/row">
                                <LMX_InformacionAduanera>
                                    <xsl:for-each select="element">
			                             <xsl:variable name="atrname" select="@name" />    			
			                             <xsl:attribute name="{$atrname}">
				                              <xsl:value-of select="."/>
			                             </xsl:attribute>
 		                            </xsl:for-each>                              
                                </LMX_InformacionAduanera>   
                            </xsl:for-each>    
 					</xsl:element>
 				</xsl:for-each> 
 			</xsl:element>
 		</xsl:for-each>
    </LMX_Comprobante>
    </xsl:template>
 </xsl:stylesheet>