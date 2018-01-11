﻿--DROP VIEW LMX_Donatarias;
CREATE OR REPLACE VIEW LMX_Donatarias AS
SELECT 'es_MX' AS AD_Language,
    ldt.AD_Client_ID,
    ldt.AD_Org_ID,
    ldt.CreatedBy,
    ldt.Created,
    ldt.UpdatedBy,
    ldt.Updated ,
    ldt.IsActive,
    ldt.C_DocType_ID,
    ldt.LMX_DocType_ID,
    ldt.ConComplementoParaDonatarias,
    TO_CHAR(ldt.fechaAutorizacion, 'yyyy-mm-dd"T"HH24:MI:SS ')  AS fechaAutorizacion,
    ldt.noAutorizacion,
    ldt.leyenda
    FROM LMX_DocType ldt;