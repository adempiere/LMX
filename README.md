# Localización de ADempiere para México

Esta localización permite generar el XML para crear Comprobante Fiscal Digitales por Internet (CFDI)

Actualmente el timbrado esta implementado con el proveedor CODEX

## Antes de Instalar

Para poder configurar e instalar la localización se requiere:
- Razón social y RFC para tu compañía
- Certificado y contraseña proporcionado por el SAT para el timbrado de los documentos
- Ant (http://ant.apache.org)

## Instalacón de la localización

- Descarga e instala la ultima versión de ADempiere (http://bintray.com/adempiere/Official-Repository/)
- Descargar la localización usando git clone https://github.com/adempiere/LMX.git
- Establece la propiedad adempiere.home editando el archivo build.properties
- Ejecuta el comando ant install
- Importar y aplicar el script LMX.xml con la definición del diccionario de aplicaciones
    - Application Dictionary -> Import migration from XML [File Name: org.eevolution.LMX/xml/LMX.xml]
    - Application Dictionary -> Migration , busca el script LMX y aplica en tu instalación
- Crear la configuración requerida para tu compañía en ADempiere

## Configuración de la localización

- Crear un registro de información de impuesto LMX Localization -> LMX Tax Information
    - Agregar esquema del CFDI Schema XML (cfdv32.xsd.xml)
    - Agregar transformador XSLT CFDI Transformer XSLT (ADempiereConvertMutator.xslt) de ADempiere
    - Agregar transformador XSLT CFDI Transformer String XSLT (CadenaOriginal.xslt) para la cadena original
- Crear un registro para el certificado y agregar secuencias de documentos LMX Localization -> LMX Certificate CFDI
- Crear un registro para la Addenda de CFDI LMX Localization -> LMX Addenda CFDI
    - Agregar transformador XSLT CFDI Transformer XLTS (EsquemaCFDICompany.xslt)
- Configurar Implementación de proveedor CODEX

## Licencia
Copyright (C) 2003-2014 e-Evolution Consultants All Rights Reserved.
Código disponible con licencia [GPL](http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt).

## Contacto
Victor Perez victor.perez@e-evoution.com , e-Evolution
