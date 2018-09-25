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

import org.isf.generaldata.MessageBundle;
import org.isf.lab.model.Laboratory;
import org.isf.lab.model.LaboratoryForPrint;
import org.isf.lab.model.LaboratoryRow;
import org.isf.lab.service.LabIoOperations;
import org.isf.menu.gui.Menu;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LabManager {

	@Autowired
	private LabIoOperations ioOperations;
	
	public LabManager() {
	}
	
	/**
	 * For JUnitTest org.isf.lab.test.Tests.testManagerNewLaboratoryTransaction()
	 * @param ioOperations
	 */
	public LabManager(LabIoOperations ioOperations) {
		if (ioOperations == null)
			this.ioOperations = Menu.getApplicationContext().getBean(LabIoOperations.class);
		else this.ioOperations = ioOperations;
	}
	
	/**
	 * Verify if the object is valid for CRUD and return a list of errors, if any
	 * @param deliveryResultType
	 * @return list of {@link OHExceptionMessage}
	 */
	protected List<OHExceptionMessage> validateLaboratory(Laboratory laboratory) {
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        if(laboratory.getResult().isEmpty()){
	        errors.add(new OHExceptionMessage("labRowNullOrEmptyError", 
	        		MessageBundle.getMessage("angal.labnew.someexamswithoutresultpleasecheck"), 
	        		OHSeverityLevel.ERROR));
        }
        if(laboratory.getMaterial().isEmpty() ){
	        errors.add(new OHExceptionMessage("materialEmptyError", 
	        		MessageBundle.getMessage("angal.lab.pleaseselectamaterial"), 
	        		OHSeverityLevel.ERROR));
        }
        if(laboratory.getExamDate() == null){
	        errors.add(new OHExceptionMessage("examDateNullError", 
	        		MessageBundle.getMessage("angal.lab.pleaseinsertexamdate"), 
	        		OHSeverityLevel.ERROR));
        }
        if(laboratory.getInOutPatient().isEmpty()){
	        errors.add(new OHExceptionMessage("ipdOPDEmptyError", 
	        		MessageBundle.getMessage("angal.lab.pleaseinsertiforipdoroforopd"), 
	        		OHSeverityLevel.ERROR));
        }
        if(laboratory.getSex().isEmpty()){
	        errors.add(new OHExceptionMessage("ipdOPDEmptyError", 
	        		MessageBundle.getMessage("angal.lab.pleaseinsertmformaleorfforfemale"), 
	        		OHSeverityLevel.ERROR));
        }
        return errors;
    }

	/**
	 * Return the whole list of exams ({@link Laboratory}s) within last year.
	 * @return the list of {@link Laboratory}s. It could be <code>empty</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<Laboratory> getLaboratory() throws OHServiceException {
		return ioOperations.getLaboratory();
	}

	/**
	 * Return a list of exams ({@link Laboratory}s) related to a {@link Patient}.
	 * 
	 * @param aPatient - the {@link Patient}.
	 * @return the list of {@link Laboratory}s related to the {@link Patient}. It could be <code>empty</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<Laboratory> getLaboratory(Patient aPatient) throws OHServiceException {
		return ioOperations.getLaboratory(aPatient);
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
		return ioOperations.getLaboratory(exam, dateFrom, dateTo);
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
		ArrayList<LaboratoryForPrint> labs = ioOperations.getLaboratoryForPrint(exam, dateFrom, dateTo);
		setLabMultipleResults(labs);
			
		return labs;
	}

	/**
	 * Inserts one Laboratory exam {@link Laboratory} (All Procedures)
	 * @param laboratory - the laboratory with its result (Procedure 1)
	 * @param labRow - the list of results (Procedure 2) - it can be <code>null</code>
	 * @return <code>true</code> if the exam has been inserted, <code>false</code> otherwise
	 * @throws OHServiceException
	 */
	public boolean newLaboratory(Laboratory laboratory, ArrayList<String> labRow) throws OHServiceException {
		List<OHExceptionMessage> errors = validateLaboratory(laboratory);
		if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
		if (laboratory.getExam().getProcedure() == 1) {
			return ioOperations.newLabFirstProcedure(laboratory);
		}
		else if (laboratory.getExam().getProcedure() == 2) {
			if (labRow == null || labRow.isEmpty())
				throw new OHServiceException(new OHExceptionMessage("labRowNullOrEmptyError", 
		        		MessageBundle.getMessage("angal.labnew.someexamswithoutresultpleasecheck"), 
		        		OHSeverityLevel.ERROR));
			return ioOperations.newLabSecondProcedure(laboratory, labRow);
		}
		else 
			throw new OHServiceException(new OHExceptionMessage("unknownProcedureError", 
	        		MessageBundle.getMessage("angal.lab.unknownprocedure"), 
	        		OHSeverityLevel.ERROR));
	}
	
	/**
	 * Inserts one Laboratory exam {@link Laboratory} (All Procedures)
	 * @param laboratory - the laboratory with its result (Procedure 1)
	 * @param labRow - the list of results (Procedure 2) - it can be <code>null</code>
	 * @return <code>true</code> if the exam has been inserted, <code>false</code> otherwise
	 * @throws OHServiceException
	 */
	public boolean updateLaboratory(Laboratory laboratory, ArrayList<String> labRow) throws OHServiceException {
		List<OHExceptionMessage> errors = validateLaboratory(laboratory);
		if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
		if (laboratory.getExam().getProcedure() == 1) {
			return ioOperations.updateLabFirstProcedure(laboratory);
		}
		else if (laboratory.getExam().getProcedure() == 2) {
			if (labRow == null || labRow.isEmpty())
				throw new OHServiceException(new OHExceptionMessage("labRowNullOrEmptyError", 
		        		MessageBundle.getMessage("angal.labnew.someexamswithoutresultpleasecheck"), 
		        		OHSeverityLevel.ERROR));
			return ioOperations.updateLabSecondProcedure(laboratory, labRow);
		}
		else 
			throw new OHServiceException(new OHExceptionMessage("unknownProcedureError", 
	        		MessageBundle.getMessage("angal.lab.unknownprocedure"), 
	        		OHSeverityLevel.ERROR));
	}
	
	/**
	 * Inserts list of Laboratory exams {@link Laboratory} (All Procedures)
	 * @param labList - the laboratory list with results
	 * @param labRowList - the list of results, it can be <code>null</code>
	 * @return <code>true</code> if the exam has been inserted, <code>false</code> otherwise
	 * @throws OHServiceException
	 */
	@Transactional(rollbackFor=OHServiceException.class)
	public boolean newLaboratory(List<Laboratory> labList, ArrayList<ArrayList<String>> labRowList) throws OHServiceException {
		if (labList.size() == 0)
			throw new OHServiceException(new OHExceptionMessage("emptyListError", 
		    		MessageBundle.getMessage("angal.labnew.noexamsinserted"), 
		    		OHSeverityLevel.ERROR));
		if (labList.size() != labRowList.size())
			throw new OHServiceException(new OHExceptionMessage("labRowNullOrEmptyError", 
		    		MessageBundle.getMessage("angal.labnew.someexamswithoutresultpleasecheck"), 
		    		OHSeverityLevel.ERROR));
		boolean result = true;
		for (int i = 0; i < labList.size(); i++) {
			result = result && newLaboratory(labList.get(i), labRowList.get(i));
		}
		return result;
	}
	
	/**
	 * Inserts one Laboratory exam {@link Laboratory} (Procedure One)
	 * @param laboratory - the {@link Laboratory} to insert
	 * @return <code>true</code> if the exam has been inserted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	protected boolean newLabFirstProcedure(Laboratory laboratory) throws OHServiceException {
		return ioOperations.newLabFirstProcedure(laboratory);
	}

	/**
	 * Inserts one Laboratory exam {@link Laboratory} with multiple results (Procedure Two) 
	 * @param laboratory - the {@link Laboratory} to insert
	 * @param labRow - the list of results ({@link String}s)
	 * @return <code>true</code> if the exam has been inserted with all its results, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	protected boolean newLabSecondProcedure(Laboratory laboratory, ArrayList<String> labRow) throws OHServiceException {
		return ioOperations.newLabSecondProcedure(laboratory, labRow);
	}

	/**
	 * Update an already existing Laboratory exam {@link Laboratory} (Procedure One).
	 * If old exam was Procedure Two all its releated result are deleted.
	 * @param laboratory - the {@link Laboratory} to update
	 * @return <code>true</code> if the exam has been updated, <code>false</code> otherwise
	 * @throws OHServiceException
	 * @deprecated use updateLaboratory() for all procedures
	 */
	protected boolean editLabFirstProcedure(Laboratory laboratory) throws OHServiceException {
		return ioOperations.updateLabFirstProcedure(laboratory);
	}

	/**
	 * Update an already existing Laboratory exam {@link Laboratory} (Procedure Two).
	 * Previous results are deleted and replaced with new ones.
	 * @param laboratory - the {@link Laboratory} to update
	 * @return <code>true</code> if the exam has been updated with all its results, <code>false</code> otherwise
	 * @throws OHServiceException 
	 * @deprecated use updateLaboratory() for all procedures
	 */
	protected boolean editLabSecondProcedure(Laboratory laboratory, ArrayList<String> labRow) throws OHServiceException {
		return ioOperations.updateLabSecondProcedure(laboratory, labRow);
	}

	/**
	 * Delete a Laboratory exam {@link Laboratory} (Procedure One or Two).
	 * Previous results, if any, are deleted as well.
	 * @param laboratory - the {@link Laboratory} to delete
	 * @return <code>true</code> if the exam has been deleted with all its results, if any. <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean deleteLaboratory(Laboratory laboratory) throws OHServiceException {
		return ioOperations.deleteLaboratory(laboratory);
	}

	private void setLabMultipleResults(List<LaboratoryForPrint> labs) throws OHServiceException {
		LabRowManager rowManager = new LabRowManager();
		List<LaboratoryRow> rows = null;
		
		for (LaboratoryForPrint lab : labs) {
			String labResult = lab.getResult();
			if (labResult.equalsIgnoreCase(MessageBundle.getMessage("angal.lab.multipleresults"))) {
				rows = rowManager.getLabRowByLabId(lab.getCode());
				
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
