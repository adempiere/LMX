/**
 * Copyright (C) 2003-2018, e-Evolution Consultants S.A. , http://www.e-evolution.com
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
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

package org.eevolution.LMX.process;

import org.compiere.process.SvrProcess;

/** Generated Process for (Load Invoice from XML CFDI)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.0
 */
public abstract class LoadInvoiceFromXMLCFDIAbstract extends SvrProcess {
	/** Process Value 	*/
	private static final String VALUE_FOR_PROCESS = "LMX Load Invoice from XML CFDI";
	/** Process Name 	*/
	private static final String NAME_FOR_PROCESS = "Load Invoice from XML CFDI";
	/** Process Id 	*/
	private static final int ID_FOR_PROCESS = 54031;
	/**	Parameter Name for File Path or Name	*/
	public static final String FILEPATHORNAME = "FilePathOrName";
	/**	Parameter Value for File Path or Name	*/
	private String filePathOrName;

	@Override
	protected void prepare() {
		filePathOrName = getParameterAsString(FILEPATHORNAME);
	}

	/**	 Getter Parameter Value for File Path or Name	*/
	protected String getFilePathOrName() {
		return filePathOrName;
	}

	/**	 Setter Parameter Value for File Path or Name	*/
	protected void setFilePathOrName(String filePathOrName) {
		this.filePathOrName = filePathOrName;
	}

	/**	 Getter Parameter Value for Process ID	*/
	public static final int getProcessId() {
		return ID_FOR_PROCESS;
	}

	/**	 Getter Parameter Value for Process Value	*/
	public static final String getProcessValue() {
		return VALUE_FOR_PROCESS;
	}

	/**	 Getter Parameter Value for Process Name	*/
	public static final String getProcessName() {
		return NAME_FOR_PROCESS;
	}
}