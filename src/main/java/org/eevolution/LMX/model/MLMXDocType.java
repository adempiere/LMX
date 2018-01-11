/**
 * Copyright (C) 2003-2017, e-Evolution Consultants S.A. , http://www.e-evolution.com
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Email: victor.perez@e-evolution.com, http://www.e-evolution.com , http://github.com/e-Evolution
 * Created by victor.perez@e-evolution.com , www.e-evolution.com
 */

package org.eevolution.LMX.model;

import org.compiere.model.MDocType;
import org.compiere.model.Query;

import java.sql.ResultSet;
import java.util.Optional;
import java.util.Properties;

public class MLMXDocType  extends X_LMX_DocType{

    public static Optional<MLMXDocType> getByDocType(MDocType docType) {
        return Optional.ofNullable(new Query(docType.getCtx(), Table_Name, MDocType.COLUMNNAME_C_DocType_ID + "=?", docType.get_TrxName())
                .setClient_ID()
                .setParameters(docType.getC_DocType_ID())
                .first());
    }


    public MLMXDocType(Properties ctx, int LMX_DocType_ID, String trxName) {
        super(ctx, LMX_DocType_ID, trxName);
    }

    public MLMXDocType(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
