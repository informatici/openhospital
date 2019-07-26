package org.isf.exa.manager;

import java.util.ArrayList;
import java.util.List;

import org.isf.exa.model.ExamRow;
import org.isf.exa.service.ExamRowIoOperations;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExamRowBrowsingManager {
	private ExamRowIoOperations ioOperations = Context.getApplicationContext().getBean(ExamRowIoOperations.class);
		
	private final Logger logger = LoggerFactory.getLogger(ExamRowBrowsingManager.class);
	
	/**
	 * Verify if the object is valid for CRUD and return a list of errors, if any
	 * @param examRow
	 * @return list of {@link OHExceptionMessage}
	 */
	protected List<OHExceptionMessage> validateExamRow(ExamRow examRow) {
		String description = examRow.getDescription();
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        if(description.isEmpty()){
	        errors.add(new OHExceptionMessage("descriptionEmptyError", 
	        		MessageBundle.getMessage("angal.exa.insertdescription"), 
	        		OHSeverityLevel.ERROR));
        }
        return errors;
    }
	
	/**
	 * Returns the list of {@link ExamRow}s
	 * @return the list of {@link ExamRow}s. It could be <code>null</code>
	 * @throws OHServiceException 
	 */
	public ArrayList<ExamRow> getExamRow() throws OHServiceException {
            
            return this.getExamRow(0, null);
	}
	/**
	 * Returns a list of {@link ExamRow}s that matches passed exam code
	 * @param aExamCode - the exam code
	 * @return the list of {@link ExamRow}s. It could be <code>null</code>
	 * @throws OHServiceException 
	 */
	public ArrayList<ExamRow> getExamRow(int aExamCode) throws OHServiceException {
		return this.getExamRow(aExamCode, null);
	}

	/**
	 * Returns a list of {@link ExamRow}s that matches passed exam code and description
	 * @param aExamRowCode - the exam code
	 * @param aDescription - the exam description
	 * @return the list of {@link ExamRow}s. It could be <code>null</code>
	 * @throws OHServiceException 
	 */
	public ArrayList<ExamRow> getExamRow(int aExamRowCode, String aDescription) throws OHServiceException {
		try {
			return ioOperations.getExamRow(aExamRowCode, aDescription);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
                            MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * Insert a new {@link ExamRow} in the DB.
	 * 
	 * @param examRow - the {@link ExamRow} to insert
	 * @return <code>true</code> if the {@link ExamRow} has been inserted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean newExamRow(ExamRow examRow) throws OHServiceException {
	    try {
			List<OHExceptionMessage> errors = validateExamRow(examRow);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
			return ioOperations.newExamRow(examRow);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Delete an {@link ExamRow}.
	 * @param examRow - the {@link ExamRow} to delete
	 * @return <code>true</code> if the {@link ExamRow} has been deleted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean deleteExamRow(ExamRow examRow) throws OHServiceException {
		try {
			return ioOperations.deleteExamRow(examRow);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

    /**
	 * Returns a list of {@link ExamRow}s that matches passed exam code
	 * @param aExamCode - the exam code
	 * @return the list of {@link ExamRow}s. It could be <code>null</code>
	 * @throws OHServiceException 
	 */
	public ArrayList<ExamRow> getExamRowByExamCode(String aExamCode) throws OHServiceException {
		try {
			return ioOperations.getExamRowByExamCode(aExamCode);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch(Exception e){
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
                            MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}
}
