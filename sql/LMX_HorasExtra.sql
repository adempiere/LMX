-- View: adempiere.lmx_horasextra

--DROP VIEW LMX_Horasextra;
CREATE OR REPLACE VIEW LMX_HorasExtra AS
 SELECT payselectioncheck.ad_client_id,
    payselectioncheck.ad_org_id,
    payselection.hr_process_id,
    payselectioncheck.c_bpartner_id,
    COALESCE((SELECT
            CASE
            WHEN _concept.value = 'DHE'  AND concept.value = 'HE' THEN _movement.qty
            WHEN _concept.value = 'DHED' AND concept.value = 'HED' THEN _movement.qty
            WHEN _concept.value = 'DHET' AND concept.value = 'HET' THEN _movement.qty
            END
           FROM HR_movement _movement
             JOIN hr_concept _concept ON (_movement.HR_Concept_ID = _concept.HR_Concept_ID)
          WHERE
          _movement.HR_Movement_ID = movement.HR_Movement_ID AND
          _concept.value IN ('DHE','DHED','DHET')) , 0.0) AS dias,
    concept.name AS tipohoras, --//HED hora extra dobles  HET horas extras triples
    movement.qty AS horasextra,
     COALESCE((SELECT
            CASE
            WHEN _concept.value = 'MHE'  AND concept.value = 'HE' THEN _movement.amount
            WHEN _concept.value = 'MHED' AND concept.value = 'HED' THEN _movement.amount
            WHEN _concept.value = 'MHET' AND concept.value = 'HET' THEN _movement.amount
            END
           FROM HR_movement _movement
             JOIN hr_concept _concept ON (_movement.HR_Concept_ID = _concept.HR_Concept_ID)
          WHERE
          _movement.HR_Movement_ID = movement.HR_Movement_ID AND
          _concept.value IN ('MHE','MHED','MHET')) , 0.0) AS importepagado, --//MHED hora extra dobles  MHET horas extras triples
   payselectioncheck.HR_PaySelectionCheck_ID
   FROM HR_PaySelectionCheck payselectioncheck
   INNER JOIN HR_PaySelection payselection ON (payselectioncheck.HR_PaySelection_ID=payselection.HR_PaySelection_ID)
   INNER JOIN HR_PaySelectionline payselectionline ON (payselectioncheck.HR_PaySelection_ID=payselectionline.HR_PaySelection_ID)
   INNER JOIN HR_Movement movement ON (payselectionline.HR_Movement_ID=movement.HR_Movement_ID)
   INNER JOIN HR_Concept concept ON (movement.hr_concept_id = concept.hr_concept_id)
   LEFT JOIN HR_Concept_Category category ON (concept.HR_Concept_Category_ID = category.HR_Concept_Category_ID)
   LEFT JOIN HR_Concept_Type type ON (concept.HR_Concept_Type_ID=type.HR_Concept_Type_ID)
   WHERE movement.C_BPartner_ID = payselectioncheck.C_BPartner_ID
   AND movement.HR_Process_ID=payselection.HR_Process_ID
   AND category.value = 'P' AND type.Description='019';

--ALTER TABLE adempiere.lmx_horasextra
--  OWNER TO adempiere;
