--DROP VIEW lmx_comprobantenomina;
CREATE OR REPLACE VIEW lmx_comprobantenomina AS
SELECT 'es_MX' AS ad_language,
  process.ad_client_id,
  process.ad_org_id,
  process.hr_process_id,
  partner.c_bpartner_id,
  payselectioncheck.hr_payselectioncheck_id AS folio,
  COALESCE((SELECT SUM(importe) FROM LMX_DetalleRetenciones dr WHERE dr.hr_payselectioncheck_id = payselectioncheck.hr_payselectioncheck_id), 0.0)
  AS totalimpuestosretenidos,
  0 AS totalimpuestostrasladados,
  COALESCE ((SELECT TotalGravado + TotalExento FROM LMX_Percepciones p WHERE p.hr_payselectioncheck_id = payselectioncheck.hr_payselectioncheck_id),0)
  AS subtotal,
  COALESCE(COALESCE((SELECT TotalGravado + TotalExento FROM LMX_Deducciones dr WHERE dr.hr_payselectioncheck_id = payselectioncheck.hr_payselectioncheck_id),0) -
           COALESCE((SELECT SUM(importe) FROM LMX_DetalleRetenciones dr WHERE dr.hr_payselectioncheck_id = payselectioncheck.hr_payselectioncheck_id),0) ,0 )
  AS descuento,
       'Deducciones Nomina' AS motivoDescuento,
       movement.amount AS total,
       'PESOS' AS moneda,
       '1' AS tipoCambio,
       '3.2' AS version,
       to_char(getdate(), 'yyyy-mm-dd"T"HH24:MI:SS'::text) AS fecha,
       'Pago en una sola exhibicion' AS formadepago,
       CASE
       WHEN payselectioncheck.paymentrule = ANY (ARRAY['T'::bpchar, 'D'::bpchar]) THEN 'TRANSFERENCIA ELECTRONICA'::text
       WHEN payselectioncheck.paymentrule = 'B'::bpchar THEN 'EFECTIVO'::text
       WHEN payselectioncheck.paymentrule = 'S'::bpchar THEN 'CHEQUE'::text
       ELSE NULL::text
       END AS metododepago,
       COALESCE(partner.name,'') || ' ' || COALESCE(partner.name2,'') || ' '  || COALESCE(partner.description,'') AS receptornombre,
       replace(partner.taxid::text, '-'::text, ''::text) AS receptorrfc,
  NULL::unknown AS numctapago,
location.address1 AS receptorcalle,
location.address4 AS receptorreferencia,
location.regionname AS receptorestado,
location.postal AS receptorcodigopostal,
location.address2 AS receptorcolonia,
location.address3 AS receptormunicipio,
location.city AS receptorlocalidad,
replace(tax_partner.taxid::text, '-'::text, ''::text) AS emisorrfc,
tax_group.description AS regimen,
tax_partner.name AS emisornombre,
COALESCE(country.name, tax_country.name) AS receptorpais,
tax_location.postal AS expedidoencodigopostal,
tax_location.address2 AS expedidoencolonia,
tax_location.address3 AS expedidoenlocalidad,
tax_location.address4 AS expedidoenreferencia,
tax_location.city AS expedidoenmunicipio,
COALESCE(tax_location.regionname, region.name) AS expedidoenestado,
org_location.address1 AS expedidoencalle,
org_country.name AS expedidoenpais,
org_location.address2 AS emisorcolonia,
org_location.address1 AS emisorcalle,
org_location.city AS emisorlocalidad,
org_location.address4 AS emisorrererencia,
org_location.city AS emisormunicipio,
COALESCE(org_location.regionname, org_region.name) AS emisorestado,
org_location.postal AS emisorcodigopostal,
org_country.name AS emisorpais,
'PAGONOMINA' AS condicionesdepago,
seq.prefix AS serie,
btrim(dt.description::text) AS tipodecomprobante,
'' AS noaprobacion,
'' AS anoaprobacion,
tax_location_ext.externalno AS expedidoennoexterior,
tax_location_ext.internalno AS expedidoennointerior,
org_location_ext.externalno AS emisornoexterior,
org_location_ext.internalno AS emisornointerior,
location_ext.externalno AS receptornoexterior,
location_ext.internalno AS receptornointerior,
'1.1' AS versionnomina,
partner.value AS numeroempleado,
replace(employee.nationalcode::text, '-'::text, ''::text) AS curp,
contract.value AS tiporegimen,
to_char(payselection.paydate, 'yyyy-mm-dd'::text) AS fechapago,
to_char(period.startdate, 'yyyy-mm-dd'::text) AS fechainicialpago,
to_char(period.enddate , 'yyyy-mm-dd'::text) AS fechafinalpago,
(SELECT qty FROM hr_movement m WHERE m.c_bpartner_id = partner.c_bpartner_id and m.hr_process_id=process.hr_process_id and exists (select 1 FROM hr_concept c WHERE c.hr_concept_id=m.hr_concept_id and c.value='DP')) :: Integer AS numdiaspagados,
(SELECT amount FROM hr_movement m WHERE m.c_bpartner_id = partner.c_bpartner_id and m.hr_process_id=process.hr_process_id and exists (select 1 FROM hr_concept c WHERE c.hr_concept_id=m.hr_concept_id and c.value='SDI')) AS salariodiariointegrado,
(SELECT max(amount) FROM hr_movement m WHERE m.c_bpartner_id = partner.c_bpartner_id and m.hr_process_id=process.hr_process_id and exists (select 1 FROM hr_concept c WHERE c.hr_concept_id=m.hr_concept_id and c.value='SDIBASE')) AS salariobasecotapor,
employee.sscode AS numseguridadsocial,
department.name AS departamento,
job.name AS puesto,
job.description AS riesgopuesto,
org.taxid as registropatronal,
payroll.name AS periodicidadpago,
certificate.documentno,
payselectioncheck.hr_payselectioncheck_id,
payselectioncheck.c_payment_id
FROM hr_payselectioncheck payselectioncheck
JOIN hr_payselection payselection ON payselectioncheck.hr_payselection_id = payselection.hr_payselection_id
JOIN hr_payselectionline payselectionline ON payselectioncheck.hr_payselection_id = payselectionline.hr_payselection_id
JOIN hr_movement movement ON payselectionline.hr_movement_id = movement.hr_movement_id AND movement.c_bpartner_id = payselectioncheck.c_bpartner_id
JOIN c_bpartner partner ON payselectioncheck.c_bpartner_id = partner.c_bpartner_id
JOIN hr_process process ON movement.hr_process_id = process.hr_process_id
JOIN hr_payroll payroll ON process.hr_payroll_id = payroll.hr_payroll_id
JOIN hr_contract contract ON payroll.hr_contract_id = contract.hr_contract_id
JOIN hr_concept concept ON concept.hr_concept_id = movement.hr_concept_id
JOIN hr_employee employee ON partner.c_bpartner_id = employee.c_bpartner_id AND employee.isactive = 'Y'
JOIN hr_department department ON employee.hr_department_id = department.hr_department_id
JOIN hr_job job ON employee.hr_job_id = job.hr_job_id
LEFT JOIN ad_orginfo org ON employee.ad_org_id = org.ad_org_id
LEFT JOIN hr_period period ON process.hr_period_id = period.hr_period_id
LEFT JOIN c_bpartner_location partner_location  ON partner_location.c_bpartner_location_id = (SELECT MAX(c_bpartner_location_id) FROM c_bpartner_location WHERE partner_location.c_bpartner_id=partner.c_bpartner_id)
LEFT JOIN c_location location ON partner_location.c_location_id = location.c_location_id
LEFT JOIN lmx_location location_ext ON location.c_location_id = location_ext.c_location_id
LEFT JOIN c_country country ON location.c_country_id = country.c_country_id
LEFT JOIN c_region region ON location.c_region_id = region.c_region_id
LEFT JOIN lmx_tax tax ON payselectioncheck.ad_client_id = tax.ad_client_id
LEFT JOIN c_bpartner tax_partner ON tax.c_bpartner_id = tax_partner.c_bpartner_id
LEFT JOIN c_location tax_location ON tax.c_location_id = tax_location.c_location_id
LEFT JOIN lmx_location tax_location_ext ON tax_location.c_location_id = tax_location_ext.c_location_id
LEFT JOIN c_country tax_country ON tax_location.c_country_id = tax_country.c_country_id
LEFT JOIN c_region tax_region ON tax_location.c_region_id = tax_region.c_region_id
LEFT JOIN c_taxgroup tax_group ON tax_partner.c_taxgroup_id = tax_group.c_taxgroup_id
LEFT JOIN c_country org_country ON tax_location.c_country_id = org_country.c_country_id
LEFT JOIN c_location org_location ON org.c_location_id = org_location.c_location_id
LEFT JOIN lmx_location org_location_ext ON org_location.c_location_id = org_location_ext.c_location_id
LEFT JOIN c_region org_region ON org_location.c_region_id = org_region.c_region_id
LEFT JOIN c_payment payment ON payselectioncheck.c_payment_id = payment.c_payment_id
LEFT JOIN c_doctype dt ON payment.c_doctype_id=dt.c_doctype_id
LEFT JOIN ad_sequence seq ON dt.docnosequence_id = seq.ad_sequence_id
LEFT JOIN lmx_certificate certificate ON payment.ad_org_id = certificate.ad_org_id
--WHERE payselectioncheck.HR_PaySelectionCheck_ID=1000921;
;

--ALTER TABLE lmx_comprobantenomina
--  OWNER TO adempiere;
