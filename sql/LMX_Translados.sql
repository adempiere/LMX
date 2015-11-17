CREATE OR REPLACE VIEW LMX_TRASLADOS AS 
SELECT it.AD_Client_ID , it.AD_Org_ID, it.C_Invoice_ID,to_char(it.taxamt,'99999999990.99') as Trasladoimporte,it.C_Tax_ID as C_Tax_ID,trim(t.taxindicator) as Trasladoimpuesto,to_char(t.rate,'90.99') as Trasladotasa
FROM C_InvoiceTax it
INNER JOIN c_tax t on t.c_tax_id=it.c_tax_id and t.rate >= 0;
 
