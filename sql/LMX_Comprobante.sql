
  CREATE OR REPLACE FORCE VIEW "ADEMPIERE"."LMX_COMPROBANTE" ("AD_LANGUAGE", "AD_CLIENT_ID", "AD_ORG_ID", "C_INVOICE_ID", "FOLIO", "TOTALIMPUESTOSRETENIDOS", "TOTAL", "SUBTOTAL", "TOTALIMPUESTOSTRASLADADOS", "MONEDA", "VERSION", "FECHA", "FORMADEPAGO", "METODODEPAGO", "RECEPTORNOMBRE", "RECEPTORRFC", "NUMCTAPAGO", "RECEPTORCALLE", "RECEPTORREFERENCIA", "RECEPTORESTADO", "RECEPTORCODIGOPOSTAL", "RECEPTORCOLONIA", "RECEPTORMUNICIPIO", "RECEPTORLOCALIDAD", "EMISORRFC", "REGIMEN", "EMISORNOMBRE", "RECEPTORPAIS", "EXPEDIDOENCODIGOPOSTAL", "EXPEDIDOENCOLONIA", "EXPEDIDOENLOCALIDAD", "EXPEDIDOENREFERENCIA", "EXPEDIDOENMUNICIPIO", "EXPEDIDOENESTADO", "EXPEDIDOENCALLE", "EXPEDIDOENPAIS", "EMISORCOLONIA", "EMISORCALLE", "EMISORLOCALIDAD", "EMISORRERERENCIA", "EMISORMUNICIPIO", "EMISORESTADO", "EMISORCODIGOPOSTAL", "EMISORPAIS", "CONDICIONESDEPAGO", "SERIE", "TIPODECOMPROBANTE", "NOAPROBACION", "ANOAPROBACION", "EXPEDIDOENNOEXTERIOR", "EXPEDIDOENNOINTERIOR", "EMISORNOEXTERIOR", "EMISORNOINTERIOR", "RECEPTORNOEXTERIOR", "RECEPTORNOINTERIOR") AS 
  SELECT 'es_MX' AS AD_Language, i.AD_Client_ID                                                         ,
    i.AD_Org_ID                                                                  ,
    i.C_Invoice_ID                                                               ,
    COALESCE(substr(i.documentno,LENGTH(seq.prefix)+1), i.documentno) AS Folio,
    (SELECT SUM(taxamt)
       FROM c_invoicetax it
    INNER JOIN c_tax t
         ON t.c_tax_id  =it.c_tax_id
      WHERE c_invoice_id=i.c_invoice_id
    AND t.rate          < 0
    )            AS TotalImpuestosRetenidos,
    i.GrandTotal AS Total                  ,
    i.TotalLines AS SubTotal               ,
    (SELECT SUM(taxamt)
       FROM c_invoicetax it
    INNER JOIN c_tax t
         ON t.c_tax_id  =it.c_tax_id
      WHERE c_invoice_id=i.c_invoice_id
    AND t.rate         >= 0
    )                                                AS TotalImpuestosTrasladados,
    'MXP'                                            AS Moneda                   ,
    '3.2'                                            AS Version                  ,
--    TO_CHAR(CURRENT_TIMESTAMP,'YYYY-MM-DDThh:MM:SS') AS Fecha                    ,
    TO_CHAR(sysdate, 'yyyy-mm-dd"T"HH24:MI:SS') AS Fecha,
    'Pago en Una Sola Exhibicion'                    AS FormaDePago              ,
    (SELECT name
       FROM ad_ref_list_trl
      WHERE ad_language ='es_MX'
    AND ad_ref_list_id IN
      (SELECT ad_ref_list_id
         FROM ad_ref_list
        WHERE AD_Reference_ID=195
      AND value              =i.paymentrule
      )
    )                                         AS MetodoDePago          ,
    bp.Name                                   AS ReceptorNombre        ,
    bp.TaxID                                  AS ReceptorRfc           ,
    bp.NAICS                                  AS NumCtaPago            ,
    l.Address1                                AS ReceptorCalle         ,
    l.Address4                                AS ReceptorReferencia    ,
    l.RegionName                              AS ReceptorEstado        ,
    l.Postal                                  AS ReceptorCodigoPostal  ,
    l.Address2                                AS ReceptorColonia       ,
    l.Address3                                AS ReceptorMunicipio     ,
    l.City                                    AS ReceptorLocalidad     ,
    fi.TaxID                                  AS EmisorRfc             ,
    'Regimen General de Ley Personas Morales' AS Regimen               ,
    fi.Name                                   AS EmisorNombre          ,
    c.Name                                    AS ReceptorPais          ,
    ol.Postal                                 AS ExpedidoEnCodigoPostal,
    ol.Address2                               AS ExpedidoEnColonia     ,
    ol.Address3                               AS ExpedidoEnLocalidad   ,
    ol.Address4                               AS ExpedidoEnReferencia  ,
    ol.City                                   AS ExpedidoEnMunicipio   ,
    COALESCE(ol.RegionName,rol.name)          AS ExpedidoEnEstado      ,
    ol.address1                               AS ExpedidoEnCalle       ,
    oc.Name                                   AS ExpedidoEnPais        ,
    el.address2                               AS EmisorColonia         ,
    el.address1                               AS EmisorCalle           ,
    el.address3                               AS EmisorLocalidad       ,
    el.address4                               AS EmisorreRerencia      ,
    el.City                                   AS EmisorMunicipio       ,
    COALESCE(el.RegionName ,rel.name)        AS EmisorEstado          ,
    el.Postal                                 AS EmisorCodigoPostal    ,
    ec.Name                                   AS EmisorPais            ,
    pt.Name                                   AS CondicionesDePago     ,
    seq.prefix                                AS Serie                 ,
    TRIM(dt.description)                      AS TipoDeComprobante     ,
    ''                                        AS NoAprobacion          ,
    ''                                        AS AnoAprobacion         ,
    oln.ExternalNo                            AS ExpedidoEnNoExterior  ,
    oln.InternalNo                            AS ExpedidoEnNoInterior  ,
    en.ExternalNo                             AS EmisorNoExterior      ,
    en.InternalNo                             AS EmisorNoInterior      ,
    ln.ExternalNo                             AS ReceptorNoExterior    ,
    ln.InternalNo                             AS ReceptorNoInterior
    FROM C_Invoice i

  INNER JOIN C_BPartner bp
       ON bp.c_bpartner_id=i.c_bpartner_id
  INNER JOIN C_BPartner_Location bpl
       ON bpl.c_bpartner_location_id=i.c_bpartner_location_id
  LEFT JOIN C_Location l
       ON l.c_location_id=bpl.c_location_id
  LEFT JOIN LMX_Tax fi ON (fi.AD_Client_ID=i.AD_Client_ID)     
    /*LEFT JOIN LMX_Tax fi
    ON fi.lmx_Tax_id =
    (SELECT lmx_tax_id
    FROM lmx_tax
    WHERE ad_tree_org_id IN
    (SELECT ad_tree_id
    FROM ad_tree
    WHERE ad_tree_id IN
    (SELECT ad_tree_id FROM ad_treenode WHERE node_id = i.ad_org_id
    )
    AND treetype = 'OO'
    )
    )*/
  LEFT JOIN C_Country c
       ON c.c_country_id=l.c_country_id
  LEFT JOIN AD_OrgInfo o
       ON o.ad_org_id=i.ad_org_id
  LEFT JOIN C_Location ol
       ON ol.c_location_id=o.c_location_id
  LEFT JOIN C_Region rol ON (rol.C_Region_ID = ol.C_Region_ID)     
  LEFT JOIN C_Country oc
       ON oc.c_country_id=ol.c_country_id
    /*LEFT JOIN C_Location el
    ON el.c_location_id=
    (SELECT c_location_id
    FROM lmx_tax
    WHERE ad_tree_org_id IN
    (SELECT ad_tree_id
    FROM ad_tree
    WHERE ad_tree_id IN
    (SELECT ad_tree_id FROM ad_treenode WHERE node_id = i.ad_org_id
    )
    AND treetype = 'OO'
    )
    )*/
  LEFT JOIN C_Location el ON (el.C_Location_ID=fi.C_Location_ID) 
  LEFT JOIN C_Region rel ON(rel.C_Region_ID=el.C_Region_ID)
  LEFT JOIN C_Country ec
       ON ec.c_country_id=el.c_country_id
  LEFT JOIN C_DocType dt
       ON dt.c_doctype_id=i.c_doctypetarget_id
  LEFT JOIN C_PaymentTerm pt
       ON pt.c_paymentterm_id=i.c_paymentterm_id
  LEFT JOIN AD_Sequence seq
       ON seq.ad_sequence_id=dt.docnosequence_id
    --inner join LMX_DocType mdt on mdt.c_doctype_id=i.c_doctypetarget_id
    --inner join LMX_CtrlSequence mseq on mseq.c_doctype_id=i.c_doctypetarget_id and to_number(substring(i.documentno,length(seq.prefix)+1),'9999999') between mseq.sequencebegin and mseq.sequenceend
  LEFT JOIN LMX_Location oln
       ON oln.c_location_id = ol.c_location_id
  LEFT JOIN LMX_Location en
       ON en.c_location_id = el.c_location_id
  LEFT JOIN LMX_Location ln
       ON ln.c_location_id = l.c_location_id
    WHERE fi.LMX_Tax_ID    =1000000
  AND el.C_Location_ID     = 1013962;
 
