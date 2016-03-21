--View: adempiere.lmx_incapacidades
--DROP VIEW LMX_Incapacidades;
CREATE OR REPLACE VIEW LMX_Incapacidades AS
 SELECT payselectioncheck.ad_client_id,
    payselectioncheck.ad_org_id,
    payselection.hr_process_id,
    payselectioncheck.c_bpartner_id,
     COALESCE((SELECT sum(_movement.qty) AS sum
           FROM HR_movement _movement
          WHERE
          _movement.HR_Movement_ID = movement.HR_Movement_ID), 0.0) AS diasincapacidad,
        CASE
            WHEN concept.value = 'DI1' THEN 1 -- DI1 --'DIN_RT'
            WHEN concept.value = 'DI2' THEN 2 -- DI2 -- 'DIN'
            WHEN concept.value = 'DI3' THEN 3 -- DI3 -- 'DIN_M'
            ELSE NULL
        END AS tipoincapacidad,
    COALESCE((SELECT
            CASE
            WHEN _concept.value = 'DI1' THEN _movement.amount  -- DI1 --'DIN_RT'
            WHEN _concept.value = 'DI2' THEN _movement.amount  -- DI2 -- 'DIN'
            WHEN _concept.value = 'DI3' THEN _movement.amount  -- DI3 -- 'DIN_M'
            END
           FROM HR_movement _movement
             JOIN hr_concept _concept ON (_movement.HR_Concept_ID = concept.HR_Concept_ID)
          WHERE
          _movement.HR_Process_ID = movement.HR_Process_ID AND
          _movement.C_BPartner_ID = movement.C_BPartner_ID AND
          _concept.value IN ('DDI1','DDI2','DDI3')) , 0.0) AS descuento, -- D 006
    payselectioncheck.hr_payselectioncheck_id
   FROM HR_PaySelectionCheck payselectioncheck
   INNER JOIN HR_PaySelection payselection ON (payselectioncheck.HR_PaySelection_ID=payselection.HR_PaySelection_ID)
   INNER JOIN HR_PaySelectionline payselectionline ON (payselectioncheck.HR_PaySelection_ID=payselectionline.HR_PaySelection_ID)
   INNER JOIN HR_Movement movement ON (payselectionline.HR_Movement_ID=movement.HR_Movement_ID)
   INNER JOIN HR_Concept concept ON (movement.hr_concept_id = concept.hr_concept_id)
   LEFT JOIN HR_Concept_Category category ON (concept.HR_Concept_Category_ID = category.HR_Concept_Category_ID)
   LEFT JOIN HR_Concept_Type type ON (concept.HR_Concept_Type_ID=type.HR_Concept_Type_ID)
   WHERE movement.C_BPartner_ID = payselectioncheck.C_BPartner_ID
   AND movement.HR_Process_ID=payselection.HR_Process_ID
   AND category.value = 'D' AND type.Name='D006';

--ALTER TABLE adempiere.lmx_incapacidades
--  OWNER TO adempiere;

