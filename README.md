Localización de ADempiere para México

Esta localización permite generar el XML para crear Comprobante Fiscal Digitales por Internet (CFDI)

Actualmente el timbrado esta implementado con el proveedor CODEX

== Antes de Instalar ==

Para poder configurar e instalar la localización se requiere:
* Razón social y RFC para tu compañía
* Certificado y contraseña proporcionado por SAT para timbrar tus documentos

== Instalar ultima versión de ADempiere  y la localización ==

* Descarga e Instala ADempiere desde http://bintray.com/adempiere/Official-Repository/
* Crea un clone de la localización git clone https://github.com/adempiere/LMX.git
* Establece la propiedad adempiere.home editando el archivo build.properties
* Ejecuta ant install
* Importar y aplicar el script LMX.xml con la definición del diccionario de aplicaciones
    ** Application Dictionary -> Import migration from XML [File Name: org.eevolution.LMX/xml/LMX.xml]
    ** Application Dictionary -> Migration , busca el script LMX y aplica en tu instalación
* Crear la configuración requerida para tu compañía

== Configuración de la localización ==

* Crear un registro de información de impuesto LMX Localization -> LMX Tax Information
    ** Agregar esquema del CFDI [CFDI Schema XML] (cfdv32.xsd.xml)
    ** Agregar transformador XSLT [CFDI Transformer XSLT] (ADempiereConvertMutator.xslt) de ADempiere
    ** Agregar transformador XSLT [CFDI Transformer String XSLT] (CadenaOriginal.xslt) para la cadena original
* Crear un registro para el certificado y agregar secuencias de documentos LMX Localization -> LMX Certificate CFDI
* Crear un registro para la Addenda de CFDI LMX Localization -> LMX Addenda CFDI
    ** Agregar transformador XSLT [CFDI Transformer XLTS] (EsquemaCFDICompany.xslt)
* Configurar Implementación de proveedor CODEX
