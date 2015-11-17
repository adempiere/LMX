--DROP VIEW LMX_ConceptosNomina;
CREATE OR REPLACE VIEW LMX_ConceptosNomina AS
SELECT payselectioncheck.AD_Client_ID,
  payselectioncheck.AD_Org_ID,
  paymentselection.HR_Process_ID,
  payselectioncheck.C_BPartner_ID,
  1 AS cantidad,
  doctype.description AS Descripcion,
  movement.amount AS importe,
  movement.amount  AS valorunitario,
  'Servicio' AS unidad,
  paymentselectionline.HR_Movement_ID AS noidentificacion,
  payselectioncheck.HR_PaySelectionCheck_ID
FROM HR_PaySelectionCheck payselectioncheck
  INNER JOIN HR_PaySelection paymentselection ON (paymentselection.HR_PaySelection_ID = paymentselection.HR_PaySelection_ID)
  INNER JOIN HR_Process process ON (paymentselection.HR_Process_ID=process.HR_Process_ID)
  INNER JOIN HR_PaySelectionLine paymentselectionline ON (payselectioncheck.HR_PaySelection_ID = paymentselectionline.HR_PaySelection_ID)
  INNER JOIN HR_Movement movement ON (paymentselectionline.HR_Movement_ID = movement.HR_Movement_ID AND payselectioncheck.C_BPartner_ID = movement.C_BPartner_ID AND paymentselection.HR_Process_ID=movement.HR_Process_ID)
  INNER JOIN C_DocType doctype ON (process.C_DocType_ID = doctype.C_DocType_ID)
--WHERE payselectioncheck.HR_PaySelectionCheck_ID=1000345
;


ALTER TABLE LMX_ConceptosNomina
  OWNER TO adempiere;
