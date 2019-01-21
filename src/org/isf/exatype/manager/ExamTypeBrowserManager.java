package org.isf.exatype.manager;

import java.util.ArrayList;
import java.util.List;

import org.isf.exatype.model.ExamType;
import org.isf.exatype.service.ExamTypeIoOperation;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExamTypeBrowserManager {

	private final Logger logger = LoggerFactory.getLogger(ExamTypeBrowserManager.class);
	
	private ExamTypeIoOperation ioOperations = Context.getApplicationContext().getBean(ExamTypeIoOperation.class);

	/**
	 * Verify if the object is valid for CRUD and return a list of errors, if any
	 * @param examType
	 * @return list of {@link OHExceptionMessage}
	 */
	protected List<OHExceptionMessage> validateExamType(ExamType examType) {
		String key = examType.getCode();
		String description = examType.getDescription();
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        if(key.isEmpty() ){
	        errors.add(new OHExceptionMessage("codeEmptyError", 
	        		MessageBundle.getMessage("angal.exatype.pleaseinsertacode"), 
	        		OHSeverityLevel.ERROR));
        }
        if(key.length()>2){
	        errors.add(new OHExceptionMessage("codeTooLongError", 
	        		MessageBundle.getMessage("angal.exatype.codetoolongmaxchar"), 
	        		OHSeverityLevel.ERROR));
        }
        if(description.isEmpty() ){
            errors.add(new OHExceptionMessage("descriptionEmptyError", 
            		MessageBundle.getMessage("angal.exatype.pleaseinsertavaliddescription"), 
            		OHSeverityLevel.ERROR));
        }
        return errors;
    }
	
	/**
	 * Return the list of {@link ExamType}s.
	 * @return the list of {@link ExamType}s. It could be <code>null</code>
	 * @throws OHServiceException 
	 */
	public ArrayList<ExamType> getExamType() throws OHServiceException {
		try {
			return ioOperations.getExamType();
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch (Exception e) {
			logger.error("", e);
			throw new OHServiceException(e,
					new OHExceptionMessage(null,
							MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Insert a new {@link ExamType} in the DB.
	 * 
	 * @param examType - the {@link ExamType} to insert.
	 * @return <code>true</code> if the examType has been inserted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newExamType(ExamType examType) throws OHServiceException {
		try {
			List<OHExceptionMessage> errors = validateExamType(examType);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
			if (codeControl(examType.getCode())){
				throw new OHServiceException(new OHExceptionMessage(null, 
						MessageBundle.getMessage("angal.common.codealreadyinuse"), 
						OHSeverityLevel.ERROR));
			}
			return ioOperations.newExamType(examType);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(
					MessageBundle.getMessage("angal.exatype.newexamtype"), e.getMessage(), OHSeverityLevel.ERROR));
		} catch(OHServiceException e){
			throw e;
		} catch (Exception e) {
			logger.error("", e);
			throw new OHServiceException(e,
					new OHExceptionMessage(MessageBundle.getMessage("angal.exatype.newexamtype"),
							MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Update an already existing {@link ExamType}.
	 * @param examType - the {@link ExamType} to update
	 * @return <code>true</code> if the examType has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateExamType(ExamType examType) throws OHServiceException {
		try {
			List<OHExceptionMessage> errors = validateExamType(examType);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
			return ioOperations.updateExamType(examType);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch(OHServiceException e){
			throw e;
		} catch (Exception e) {
			logger.error("", e);
			throw new OHServiceException(e,
					new OHExceptionMessage(null,
							MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"),
							OHSeverityLevel.ERROR));
		}
	}

	/**
	 * This function controls the presence of a record with the same code as in
	 * the parameter.
	 * @param code - the code
	 * @return <code>true</code> if the code is present, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean codeControl(String code) throws OHServiceException {
		try {
			return ioOperations.isCodePresent(code);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch (Exception e) {
			logger.error("", e);
			throw new OHServiceException(e,
					new OHExceptionMessage(null,
							MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"),
							OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Delete the passed {@link ExamType}.
	 * @param examType - the {@link ExamType} to delete.
	 * @return <code>true</code> if the examType has been deleted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 * @throws OHException
	 */
	public boolean deleteExamType(ExamType examType) throws OHServiceException {
		try {
			return ioOperations.deleteExamType(examType);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch (Exception e) {
			logger.error("", e);
			throw new OHServiceException(e,
					new OHExceptionMessage(MessageBundle.getMessage("angal.exatype.deleteexamtype"),
							MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"),
							OHSeverityLevel.ERROR));
		}
	}
}
