package org.isf.lab.manager;

/*------------------------------------------
 * LabManager - laboratory exam manager class
 * -----------------------------------------
 * modification history
 * 10/11/2006 - ross - added editing capability 
 *------------------------------------------*/

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JOptionPane;

import org.isf.generaldata.MessageBundle;
import org.isf.lab.model.Laboratory;
import org.isf.lab.model.LaboratoryForPrint;
import org.isf.lab.model.LaboratoryRow;
import org.isf.lab.service.LabIoOperations;
import org.isf.menu.gui.Menu;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LabManager {

	private final Logger logger = LoggerFactory.getLogger(LabManager.class);
	
	private LabIoOperations ioOperations = Menu.getApplicationContext().getBean(LabIoOperations.class);

	/**
	 * Return the whole list of exams ({@link Laboratory}s) within last year.
	 * @return the list of {@link Laboratory}s. It could be <code>empty</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<Laboratory> getLaboratory() throws OHServiceException {
		try {
			return ioOperations.getLaboratory();
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Return a list of exams ({@link Laboratory}s) related to a {@link Patient}.
	 * 
	 * @param aPatient - the {@link Patient}.
	 * @return the list of {@link Laboratory}s related to the {@link Patient}. It could be <code>empty</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<Laboratory> getLaboratory(Patient aPatient) throws OHServiceException {
		try {
			return ioOperations.getLaboratory(aPatient);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		}
	}

	/*
	 * NO LONGER USED
	 * 
	 * public ArrayList<Laboratory> getLaboratory(String aCode){ return
	 * ioOperations.getLaboratory(); }
	 */

	/**
	 * Return a list of exams ({@link Laboratory}s) between specified dates and matching passed exam name
	 * @param exam - the exam name as <code>String</code>
	 * @param dateFrom - the lower date for the range
	 * @param dateTo - the highest date for the range
	 * @return the list of {@link Laboratory}s. It could be <code>empty</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<Laboratory> getLaboratory(String exam, GregorianCalendar dateFrom, GregorianCalendar dateTo) throws OHServiceException {
		try {
			return ioOperations.getLaboratory(exam, dateFrom, dateTo);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Return a list of exams suitable for printing ({@link LaboratoryForPrint}s) 
	 * between specified dates and matching passed exam name. If a lab has multiple 
	 * results, these are concatenated and added to the result string
	 * @param exam - the exam name as <code>String</code>
	 * @param dateFrom - the lower date for the range
	 * @param dateTo - the highest date for the range
	 * @return the list of {@link LaboratoryForPrint}s . It could be <code>empty</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<LaboratoryForPrint> getLaboratoryForPrint(String exam, GregorianCalendar dateFrom, GregorianCalendar dateTo) throws OHServiceException {
		try {
			ArrayList<LaboratoryForPrint> labs = ioOperations.getLaboratoryForPrint(exam, dateFrom, dateTo);
			setLabMultipleResults(labs);
			
			return labs;
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		}
	}

	
	
	/*
	 * NO LONGER USED
	 * 
	 * public ArrayList<LaboratoryForPrint> getLaboratoryForPrint() {
		return ioOperations.getLaboratoryForPrint();
	}*/

	/*
	 * NO LONGER USED
	 * 
	 * public ArrayList<LaboratoryForPrint> getLaboratoryForPrint(String exam,
	 * String result) { return ioOperations.getLaboratoryForPrint(exam,result);
	 * }
	 */

	/**
	 * Inserts one Laboratory exam {@link Laboratory} (Procedure One)
	 * @param laboratory - the {@link Laboratory} to insert
	 * @param dbQuery - the connection manager
	 * @return <code>true</code> if the exam has been inserted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean newLabFirstProcedure(Laboratory laboratory) throws OHServiceException {
		try {
			return ioOperations.newLabFirstProcedure(laboratory);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Inserts one Laboratory exam {@link Laboratory} with multiple results (Procedure Two) 
	 * @param laboratory - the {@link Laboratory} to insert
	 * @param labRow - the list of results ({@link String}s)
	 * @return <code>true</code> if the exam has been inserted with all its results, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean newLabSecondProcedure(Laboratory laboratory, ArrayList<String> labRow) throws OHServiceException {
		try {
			return ioOperations.newLabSecondProcedure(laboratory, labRow);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Update an already existing Laboratory exam {@link Laboratory} (Procedure One).
	 * If old exam was Procedure Two all its releated result are deleted.
	 * @param laboratory - the {@link Laboratory} to update
	 * @return <code>true</code> if the exam has been updated, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean editLabFirstProcedure(Laboratory laboratory) throws OHServiceException {
		try {
			return ioOperations.updateLabFirstProcedure(laboratory);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Update an already existing Laboratory exam {@link Laboratory} (Procedure Two).
	 * Previous results are deleted and replaced with new ones.
	 * @param laboratory - the {@link Laboratory} to update
	 * @return <code>true</code> if the exam has been updated with all its results, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean editLabSecondProcedure(Laboratory laboratory, ArrayList<String> labRow) throws OHServiceException {
		try {
			return ioOperations.editLabSecondProcedure(laboratory, labRow);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Delete a Laboratory exam {@link Laboratory} (Procedure One or Two).
	 * Previous results, if any, are deleted as well.
	 * @param laboratory - the {@link Laboratory} to delete
	 * @return <code>true</code> if the exam has been deleted with all its results, if any. <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean deleteLaboratory(Laboratory laboratory) throws OHServiceException {
		try {
			return ioOperations.deleteLaboratory(laboratory);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		}
	}

	private void setLabMultipleResults(List<LaboratoryForPrint> labs) {
		LabRowManager rowManager = new LabRowManager();
		List<LaboratoryRow> rows = null;
		
		for (LaboratoryForPrint lab : labs) {
			String labResult = lab.getResult();
			if (labResult.equalsIgnoreCase(MessageBundle.getMessage("angal.lab.multipleresults"))) {
				try {
					rows = rowManager.getLabRow(lab.getCode());
				} catch (OHServiceException e) {
					rows = new ArrayList<LaboratoryRow>();
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
				
				if (rows == null || rows.size() == 0) {
					lab.setResult(MessageBundle.getMessage("angal.lab.allnegative"));
				} else {
					lab.setResult(MessageBundle.getMessage("angal.lab.positive")+" : "+rows.get(0).getDescription());
					for (LaboratoryRow row : rows) {
						labResult += ("," + row.getDescription());
					}
					lab.setResult(labResult);
				}
			}
		}
	}
}
