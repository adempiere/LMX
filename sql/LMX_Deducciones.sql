-- View: adempiere.lmx_deducciones

-- DROP VIEW adempiere.lmx_deducciones;
CREATE OR REPLACE VIEW LMX_Deducciones AS 
 SELECT payselectioncheck.AD_Client_ID,
    payselectioncheck.AD_Org_ID,
    payselection.HR_Process_ID,
    payselectioncheck.C_BPartner_ID,
    SUM(CASE WHEN concept.IsTaxExempt = 'N' THEN movement.amount ELSE 0.0 END) AS TotalGravado,
    SUM(CASE WHEN concept.IsTaxExempt = 'Y' THEN movement.amount ELSE 0.0 END) AS TotalExento,
     payselectioncheck.HR_PayselectionCheck_ID
   FROM HR_PaySelectionCheck payselectioncheck
   INNER JOIN HR_PaySelection payselection ON (payselectioncheck.HR_PaySelection_ID=payselection.HR_PaySelection_ID)
   INNER JOIN HR_Process process ON (payselection.HR_Process_ID=process.HR_Process_ID)
   INNER JOIN HR_Movement movement ON (payselectioncheck.C_BPartner_ID=movement.C_BPartner_ID AND payselection.HR_Process_ID = movement.HR_Process_ID)
   INNER JOIN HR_Concept concept ON (movement.hr_concept_id = concept.hr_concept_id)
   INNER JOIN HR_Concept_Category category ON (concept.HR_Concept_Category_ID = category.HR_Concept_Category_ID)
   INNER JOIN HR_Concept_Type type ON (concept.HR_Concept_Type_ID=type.HR_Concept_Type_ID)
   WHERE category.value = 'D'
   --AND payselectioncheck.HR_PaySelectionCheck_ID=1000345
   GROUP BY payselectioncheck.AD_Client_ID,
    payselectioncheck.AD_Org_ID,
    payselection.HR_Process_ID,
    payselectioncheck.HR_PayselectionCheck_ID
   ;

ALTER TABLE LMX_Deducciones
  OWNER TO adempiere;
