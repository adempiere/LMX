CREATE OR REPLACE VIEW LMX_INFORMACIONADUANERA AS 
SELECT il.AD_Client_ID , il.AD_Org_ID, il.C_Invoice_ID, il.C_InvoiceLine_ID,
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
 
