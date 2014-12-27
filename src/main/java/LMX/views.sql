DROP VIEW LMX_Comprobante;
CREATE OR REPLACE VIEW LMX_Comprobante AS
SELECT i.AD_Client_ID , i.AD_Org_ID, i.C_Invoice_ID,
COALESCE(substring(i.documentno,length(seq.prefix)+1), i.documentno) as Folio,
(select sum(taxamt) from c_invoicetax it inner join c_tax t on t.c_tax_id=it.c_tax_id where c_invoice_id=i.c_invoice_id and t.rate < 0) as TotalImpuestosRetenidos,
i.GrandTotal as Total,
i.TotalLines as SubTotal,
(select sum(taxamt) from c_invoicetax it inner join c_tax t on t.c_tax_id=it.c_tax_id where c_invoice_id=i.c_invoice_id and t.rate >= 0) as TotalImpuestosTrasladados,
'MXP' as Moneda,
'3.2' as Version,
to_char(current_timestamp,'YYYY-MM-DDThh:MM:SS') as Fecha,
'Pago en Una Sola Exhibicion' as FormaDePago,
(Select name from ad_ref_list_trl where ad_language='es_MX' and ad_ref_list_id in (Select ad_ref_list_id from ad_ref_list where AD_Reference_ID=195 and value=i.paymentrule)) as MetodoDePago,
bp.Name as ReceptorNombre,
bp.TaxID as ReceptorRfc,
bp.NAICS as NumCtaPago,
l.Address1 as ReceptorCalle,
l.Address4 as ReceptorReferencia,
l.RegionName as ReceptorEstado,
l.Postal as ReceptorCodigoPostal,
l.Address2 as ReceptorColonia,
l.Address3 as ReceptorMunicipio,
l.City as ReceptorLocalidad,
fi.TaxID as EmisorRfc,
'Regimen General de Ley Personas Morales' as Regimen,
fi.Name as EmisorNombre,
c.Name as ReceptorPais,
ol.Postal as ExpedidoEnCodigoPostal,
ol.Address2 as ExpedidoEnColonia,
ol.Address3 as ExpedidoEnLocalidad,
ol.Address4 as ExpedidoEnReferencia,
ol.City as ExpedidoEnMunicipio,
ol.RegionName as ExpedidoEnEstado,
ol.address1 as ExpedidoEnCalle,
oc.Name as ExpedidoEnPais,
el.address2 as EmisorColonia,
el.address1 as EmisorCalle,
el.address3 as EmisorLocalidad,
el.address4 as EmisorreRerencia,
el.City as EmisorMunicipio,
el.RegionName as EmisorEstado,
el.Postal as EmisorCodigoPostal,
ec.Name as EmisorPais,
pt.Name as CondicionesDePago,
seq.prefix as Serie,
'ingreso' as TipoDeComprobante,
'' as NoAprobacion,
'' as AnoAprobacion,
oln.ExternalNo as ExpedidoEnNoExterior,
oln.InternalNo as ExpedidoEnNoInterior,
en.ExternalNo as EmisorNoExterior,
en.InternalNo as EmisorNoInterior,
ln.ExternalNo as ReceptorNoExterior,
ln.InternalNo as ReceptorNoInterior
from C_Invoice i
inner join C_BPartner bp on bp.c_bpartner_id=i.c_bpartner_id
inner join C_BPartner_Location bpl on bpl.c_bpartner_location_id=i.c_bpartner_location_id
LEFT JOIN C_Location l on l.c_location_id=bpl.c_location_id
LEFT JOIN LMX_Tax fi on fi.lmx_Tax_id = (select lmx_tax_id from lmx_tax where ad_tree_org_id in(select ad_tree_id from ad_tree where ad_tree_id in (select ad_tree_id from ad_treenode where node_id = i.ad_org_id ) and treetype = 'OO'))
LEFT JOIN C_Country c on c.c_country_id=l.c_country_id
LEFT JOIN AD_OrgInfo o on o.ad_org_id=i.ad_org_id
LEFT JOIN C_Location ol on ol.c_location_id=o.c_location_id
LEFT JOIN C_Country oc on oc.c_country_id=ol.c_country_id
LEFT JOIN C_Location el on el.c_location_id=(select c_location_id from lmx_tax where ad_tree_org_id in(select ad_tree_id from ad_tree where ad_tree_id in (select ad_tree_id from ad_treenode where node_id = i.ad_org_id ) and treetype = 'OO'))
LEFT JOIN C_Country ec on ec.c_country_id=el.c_country_id
LEFT JOIN C_DocType dt on dt.c_doctype_id=i.c_doctypetarget_id
LEFT JOIN C_PaymentTerm pt on pt.c_paymentterm_id=i.c_paymentterm_id
LEFT JOIN AD_Sequence seq on seq.ad_sequence_id=dt.docnosequence_id
--inner join LMX_DocType mdt on mdt.c_doctype_id=i.c_doctypetarget_id
--inner join LMX_CtrlSequence mseq on mseq.c_doctype_id=i.c_doctypetarget_id and to_number(substring(i.documentno,length(seq.prefix)+1),'9999999') between mseq.sequencebegin and mseq.sequenceend
LEFT JOIN LMX_Location oln on oln.c_location_id = ol.c_location_id
LEFT JOIN LMX_Location en on en.c_location_id = el.c_location_id
LEFT JOIN LMX_Location ln on ln.c_location_id = l.c_location_id;





DROP VIEW lmx_conceptos;

CREATE OR REPLACE VIEW lmx_conceptos AS
 SELECT il.ad_client_id,
    il.ad_org_id,
    il.c_invoice_id,
    il.linenetamt AS conceptoimporte,
    il.qtyinvoiced AS conceptocantidad,
    to_char(il.priceactual, '99999999990.99'::text) AS conceptovalorunitario,
    ( SELECT c_uom_trl.uomsymbol
           FROM c_uom_trl
          WHERE c_uom_trl.ad_language::text = 'es_MX'::text AND c_uom_trl.c_uom_id = il.c_uom_id) AS conceptounidad,
    COALESCE(p.name, il.description, 'NC'::character varying) AS conceptodescription,
        CASE
            WHEN bp.isdiscountprinted = 'Y'::bpchar THEN COALESCE(bpp.vendorproductno, COALESCE(p.upc, p.sku, p.value, i.poreference))
            ELSE COALESCE(p.upc, p.sku, p.value, i.poreference)
        END AS conceptonoidentificacion
   FROM c_invoiceline il
   LEFT JOIN c_invoice i ON i.c_invoice_id = il.c_invoice_id
   LEFT JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   LEFT JOIN c_bpartner_product bpp ON bpp.c_bpartner_id = bp.c_bpartner_id AND bpp.m_product_id = il.m_product_id
   LEFT JOIN m_product p ON p.m_product_id = il.m_product_id
   LEFT JOIN c_uom uom ON uom.c_uom_id = p.c_uom_id
   LEFT JOIN m_attributesetinstance ai ON ai.m_attributesetinstance_id = p.m_attributesetinstance_id
   ;




DROP VIEW LMX_Retenciones;
CREATE OR REPLACE VIEW LMX_Retenciones AS
SELECT it.AD_Client_ID , it.AD_Org_ID, it.C_Invoice_ID , to_char(it.taxamt,'9999999990.99') as Retencionimporte,trim(t.taxindicator) as Retencionimpuesto
FROM C_InvoiceTax it
INNER JOIN C_Tax t on t.c_tax_id=it.c_tax_id and t.rate < 0 ;

DROP VIEW LMX_Traslados;
CREATE OR REPLACE VIEW LMX_Traslados AS
SELECT it.AD_Client_ID , it.AD_Org_ID, it.C_Invoice_ID,to_char(it.taxamt,'99999999990.99') as Trasladoimporte,it.C_Tax_ID as C_Tax_ID,trim(t.taxindicator) as Trasladoimpuesto,to_char(t.rate,'90.99') as Trasladotasa
FROM C_InvoiceTax it
INNER JOIN c_tax t on t.c_tax_id=it.c_tax_id and t.rate >= 0;


DROP VIEW LMX_InformacionAduanera;
CREATE OR REPLACE VIEW LMX_InformacionAduanera AS
SELECT il.AD_Client_ID , il.AD_Org_ID, il.C_Invoice_ID,
(SELECT ai.Value FROM M_AttributeInstance ai
LEFT JOIN M_Attribute a ON (a.M_Attribute_ID=ai.M_Attribute_ID)
WHERE ai.M_AttributeSetInstance_ID=il.M_AttributeSetInstance_ID AND a.Name='numero') AS Numero,
(SELECT ai.Value FROM M_AttributeInstance ai
LEFT JOIN M_Attribute a ON (a.M_Attribute_ID=ai.M_Attribute_ID)
WHERE ai.M_AttributeSetInstance_ID=il.M_AttributeSetInstance_ID AND a.Name='fecha') AS Fecha,
(SELECT ai.Value FROM M_AttributeInstance ai
LEFT JOIN M_Attribute a ON (a.M_Attribute_ID=ai.M_Attribute_ID)
WHERE ai.M_AttributeSetInstance_ID=il.M_AttributeSetInstance_ID AND a.Name='aduana') AS Aduana
FROM C_InvoiceLine il;