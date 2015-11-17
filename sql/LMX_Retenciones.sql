CREATE OR REPLACE VIEW LMX_RETENCIONES AS 
SELECT it.AD_Client_ID , it.AD_Org_ID, it.C_Invoice_ID , to_char(it.taxamt,'9999999990.99') as Retencionimporte,trim(t.taxindicator) as Retencionimpuesto
FROM C_InvoiceTax it
INNER JOIN C_Tax t on t.c_tax_id=it.c_tax_id and t.rate < 0;
