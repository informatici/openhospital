package org.isf.exa.manager;

import java.util.ArrayList;

import org.isf.exa.model.ExamRow;
import org.isf.exa.service.ExamIoOperations;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExamRowBrowsingManager {
	private ExamIoOperations ioOperations = Menu.getApplicationContext().getBean(ExamIoOperations.class);
		
	private final Logger logger = LoggerFactory.getLogger(ExamRowBrowsingManager.class);
	
	/**
	 * Returns the list of {@link ExamRow}s
	 * @return the list of {@link ExamRow}s. It could be <code>null</code>
	 * @throws OHServiceException 
	 */
	public ArrayList<ExamRow> getExamRow() throws OHServiceException {
		return this.getExamRow(null, null);
	}
	
	/**
	 * Returns a list of {@link ExamRow}s that matches passed exam code
	 * @param aExamCode - the exam code
	 * @return the list of {@link ExamRow}s. It could be <code>null</code>
	 * @throws OHServiceException 
	 */
	public ArrayList<ExamRow> getExamRow(String aExamCode) throws OHServiceException {
		return this.getExamRow(aExamCode, null);
	}

	/**
	 * Returns a list of {@link ExamRow}s that matches passed exam code and description
	 * @param aExamCode - the exam code
	 * @param aDescription - the exam description
	 * @return the list of {@link ExamRow}s. It could be <code>null</code>
	 * @throws OHServiceException 
	 */
	public ArrayList<ExamRow> getExamRow(String aExamCode, String aDescription) throws OHServiceException {
		try {
			return ioOperations.getExamRow(aExamCode,aDescription);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.exa.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
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
			return ioOperations.newExamRow(examRow);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.exa.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
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
					MessageBundle.getMessage("angal.exa.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}
}
