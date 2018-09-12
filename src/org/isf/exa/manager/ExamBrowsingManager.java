/**
 * 19-dec-2005
 * 14-jan-2006
 */
package org.isf.exa.manager;

import java.util.ArrayList;
import java.util.List;

import org.isf.exa.model.Exam;
import org.isf.exa.service.ExamIoOperations;
import org.isf.exatype.model.ExamType;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that provides gui separation from database operations and gives some
 * useful logic manipulations of the dinamic data (memory)
 * 
 * @author bob
 * 
 */
public class ExamBrowsingManager {

	private ExamIoOperations ioOperations = Menu.getApplicationContext().getBean(ExamIoOperations.class);

	private final Logger logger = LoggerFactory.getLogger(ExamBrowsingManager.class);
	
	/**
	 * Verify if the object is valid for CRUD and return a list of errors, if any
	 * @param exam
	 * @return list of {@link OHExceptionMessage}
	 */
	protected List<OHExceptionMessage> validateExam(Exam exam) {
		String key = exam.getCode();
		String description = exam.getDescription();
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        if(key.isEmpty() || description.isEmpty()){
	        errors.add(new OHExceptionMessage("codeAndOrDescriptionEmptyError", 
	        		MessageBundle.getMessage("angal.exa.pleaseinsertcodeoranddescription"), 
	        		OHSeverityLevel.ERROR));
        }
        return errors;
    }
	
	/**
	 * Returns the list of {@link Exam}s
	 * @return the list of {@link Exam}s. It could be <code>null</code>
	 * @throws OHServiceException 
	 */
	public ArrayList<Exam> getExams() throws OHServiceException {
		try {
			return ioOperations.getExams();
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.exa.problemsoccurredwiththesqlistruction"), 
					OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * Returns the list of {@link Exam}s
	 * @return the list of {@link Exam}s. It could be <code>null</code>
	 * @throws OHServiceException 
	 * @deprecated use getExam() instead
	 */
	public ArrayList<Exam> getExamsbyDesc() throws OHServiceException {
		return this.getExams();
	}
	
	/**
	 * Returns the list of {@link Exam}s that matches passed description
	 * @param description - the exam description
	 * @return the list of {@link Exam}s. It could be <code>null</code>
	 * @throws OHServiceException 
	 */
	public ArrayList<Exam> getExams(String description) throws OHServiceException {
		try {
			return ioOperations.getExamsByDesc(description);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.exa.problemsoccurredwiththesqlistruction"), 
					OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * Returns the list of {@link ExamType}s
	 * @return the list of {@link ExamType}s. It could be <code>null</code>
	 * @throws OHServiceException 
	 */
	public ArrayList<ExamType> getExamType() throws OHServiceException {
		try {
			return ioOperations.getExamType();
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.exa.problemsoccurredwiththesqlistruction"),
					OHSeverityLevel.ERROR));
		}
	}

	/**
	 * This function controls the presence of a record with the same key as in
	 * the parameter; Returns false if the query finds no record, else returns
	 * true
	 * 
	 * @param the {@link Exam}
	 * @return <code>true</code> if the Exam code has already been used, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean isKeyPresent(Exam exam) throws OHServiceException {
		try {
			return ioOperations.isKeyPresent(exam);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.exa.problemsoccurredwiththesqlistruction"),
					OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * Insert a new {@link Exam} in the DB.
	 * 
	 * @param exam - the {@link Exam} to insert
	 * @return <code>true</code> if the {@link Exam} has been inserted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean newExam(Exam exam) throws OHServiceException {
		try {
			if (true == isKeyPresent(exam)) {
				throw new OHServiceException(new OHExceptionMessage(null, 
						MessageBundle.getMessage("angal.exa.changethecodebecauseisalreadyinuse"),
						OHSeverityLevel.ERROR));
			}
			List<OHExceptionMessage> errors = validateExam(exam);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
			return ioOperations.newExam(exam);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		}  catch (OHServiceException e) {
			throw e;
		} catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.exa.problemsoccurredwiththesqlistruction"),
					OHSeverityLevel.ERROR));
		}
	}

	/**
	 * 
	 * Updates an existing {@link Exam} in the db
	 * 
	 * @param exam -  the {@link Exam} to update
	 * @return <code>true</code> if the existing {@link Exam} has been updated, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean updateExam(Exam exam) throws OHServiceException {
		return this.updateExam(exam, true);
	}
	
	/**
	 * Updates an existing {@link Exam} in the db
	 * 
	 * @param exam -  the {@link Exam} to update
	 * @param check - if <code>true</code> check if the {@link Exam} has been modified since last read
	 * @return <code>true</code> if the existing {@link Exam} has been updated, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean updateExam(Exam exam, boolean check) throws OHServiceException {
		try {
			List<OHExceptionMessage> errors = validateExam(exam);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
			return ioOperations.updateExam(exam, check);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		}  catch (OHServiceException e) {
			throw e;
		} catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.exa.problemsoccurredwiththesqlistruction"),
					OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Delete an {@link Exam}
	 * @param exam - the {@link Exam} to delete
	 * @return <code>true</code> if the {@link Exam} has been deleted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean deleteExam(Exam exam) throws OHServiceException {
		try {
			return ioOperations.deleteExam(exam);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.exa.problemsoccurredwiththesqlistruction"),
					OHSeverityLevel.ERROR));
		}
	}
}
