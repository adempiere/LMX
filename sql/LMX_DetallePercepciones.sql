--DROP VIEW LMX_DetallePercepciones;
CREATE OR REPLACE VIEW LMX_DetallePercepciones AS
  SELECT payselectioncheck.AD_Client_ID,
    payselectioncheck.AD_Org_ID,
    payselection.HR_Process_ID,
    payselectioncheck.C_BPartner_ID,
    type.value AS TipoPercepcion,
    concept.hr_concept_id AS Clave,
    concept.name AS ConceptoDescription,
    CASE WHEN concept.IsTaxExempt = 'N' OR concept.IsTaxExempt IS NULL THEN movement.amount ELSE 0.0 END AS ImporteGravado,
    CASE WHEN concept.IsTaxExempt = 'Y' THEN movement.amount ELSE 0.0 END AS ImporteExento,
    payselectioncheck.HR_PaySelectionCheck_ID
   FROM HR_PaySelectionCheck payselectioncheck
   INNER JOIN HR_PaySelection payselection ON (payselectioncheck.HR_PaySelection_ID=payselection.HR_PaySelection_ID)
   INNER JOIN HR_Process process ON (payselection.HR_Process_ID=process.HR_Process_ID)
   INNER JOIN HR_Movement movement ON (payselectioncheck.C_BPartner_ID=movement.C_BPartner_ID AND
   payselection.HR_Process_ID = movement.HR_Process_ID)
   INNER JOIN HR_Concept concept ON (movement.hr_concept_id = concept.hr_concept_id)
   INNER JOIN HR_Concept_Category category ON (concept.HR_Concept_Category_ID = category.HR_Concept_Category_ID)
   INNER JOIN HR_Concept_Type type ON (concept.HR_Concept_Type_ID=type.HR_Concept_Type_ID)
  WHERE TRIM(category.Value) = 'P'  AND movement.amount >= 0
  --AND  payselectioncheck.HR_PaySelectionCheck_ID=1000345
   ;
--ALTER TABLE adempiere.lmx_detallepercepciones
--  OWNER TO adempiere;
