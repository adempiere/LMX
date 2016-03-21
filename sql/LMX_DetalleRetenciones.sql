--DROP VIEW lmx_detalleretenciones;

CREATE OR REPLACE VIEW LMX_DetalleRetenciones AS
 SELECT payselectioncheck.ad_client_id,
    payselectioncheck.ad_org_id,
    payselection.hr_process_id,
    payselectioncheck.c_bpartner_id,
    COALESCE(type.description, 'ISR') AS impuesto,
    sum(
        CASE
            WHEN type.hr_concept_type_id = concept.hr_concept_type_id THEN movement.amount
            ELSE 0::numeric
        END) AS importe,
    payselectioncheck.hr_payselectioncheck_id
   FROM hr_payselectioncheck payselectioncheck
     JOIN hr_payselection payselection ON payselectioncheck.hr_payselection_id = payselection.hr_payselection_id
     LEFT JOIN hr_movement movement ON payselection.hr_process_id = movement.hr_process_id AND payselectioncheck.c_bpartner_id = movement.c_bpartner_id
     LEFT JOIN hr_concept concept ON movement.hr_concept_id = concept.hr_concept_id
     LEFT JOIN hr_concept_category category ON concept.hr_concept_category_id = category.hr_concept_category_id
     LEFT JOIN hr_concept_type type ON concept.hr_concept_type_id = type.hr_concept_type_id
     WHERE
     movement.C_BPartner_ID = payselectioncheck.C_BPartner_ID AND
     movement.HR_Process_ID=payselection.HR_Process_ID AND
     category.value = 'D' AND type.Name='D002'
  GROUP BY
  payselectioncheck.ad_client_id,
  payselectioncheck.ad_org_id,
  payselection.hr_process_id,
  payselectioncheck.c_bpartner_id,
  type.description,
  payselectioncheck.hr_payselectioncheck_id;
