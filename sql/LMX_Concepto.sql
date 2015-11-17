CREATE OR REPLACE  VIEW LMX_CONCEPTOS AS
   SELECT 'es_MX' AS AD_Language, il.ad_client_id                                                   ,
    il.ad_org_id                                                            ,
    il.c_invoice_id                                                         ,
    il.c_invoiceline_id,
    il.linenetamt                                   AS conceptoimporte      ,
    il.qtyinvoiced                                  AS conceptocantidad     ,
    TO_CHAR(il.priceactual, '99999999990.99') AS conceptovalorunitario ,
    (SELECT c_uom_trl.uomsymbol
       FROM c_uom_trl
      WHERE c_uom_trl.ad_language = 'es_MX'
    AND c_uom_trl.c_uom_id = il.c_uom_id
    )  AS conceptounidad ,
    COALESCE(p.name, il.description , ch.name )  AS conceptodescription ,
    /*CASE
      WHEN bp.isdiscountprinted = 'Y'
      THEN COALESCE(bpp.vendorproductno, COALESCE(p.upc, p.sku, p.value, i.poreference))
      ELSE COALESCE(p.upc, p.sku, p.value, i.poreference)
    END AS conceptonoidentificacion*/
    CASE
    WHEN il.M_Product_ID IS NOT NULL THEN p.value
    ELSE ch.name END AS conceptonoidentificacion
FROM c_invoiceline il
  LEFT JOIN c_invoice i
       ON i.c_invoice_id = il.c_invoice_id
  LEFT JOIN c_bpartner bp
       ON bp.c_bpartner_id = i.c_bpartner_id
  LEFT JOIN c_bpartner_product bpp
       ON bpp.c_bpartner_id = bp.c_bpartner_id
  AND bpp.m_product_id      = il.m_product_id
  LEFT JOIN m_product p
       ON p.m_product_id = il.m_product_id
  LEFT JOIN c_charge ch ON (ch.C_Charge_ID = il.C_Charge_ID)
  LEFT JOIN c_uom uom
       ON uom.c_uom_id = p.c_uom_id
  LEFT JOIN m_attributesetinstance ai
       ON ai.m_attributesetinstance_id = p.m_attributesetinstance_id;
