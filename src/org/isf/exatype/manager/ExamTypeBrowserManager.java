package org.isf.exatype.manager;

import java.util.ArrayList;

import org.isf.exatype.model.ExamType;
import org.isf.exatype.service.ExamTypeIoOperation;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExamTypeBrowserManager {

	private final Logger logger = LoggerFactory.getLogger(ExamTypeBrowserManager.class);
	
	private ExamTypeIoOperation ioOperations = Menu.getApplicationContext().getBean(ExamTypeIoOperation.class);

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
							MessageBundle.getMessage("angal.exatype.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
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
			return ioOperations.newExamType(examType);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(
					MessageBundle.getMessage("angal.exatype.newexamtype"), e.getMessage(), OHSeverityLevel.ERROR));
		} catch (Exception e) {
			logger.error("", e);
			throw new OHServiceException(e,
					new OHExceptionMessage(MessageBundle.getMessage("angal.exatype.newexamtype"),
							MessageBundle.getMessage("angal.exatype.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
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
			return ioOperations.updateExamType(examType);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch (Exception e) {
			logger.error("", e);
			throw new OHServiceException(e,
					new OHExceptionMessage(null,
							MessageBundle.getMessage("angal.exatype.thedatacouldnotbesaved"),
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
							MessageBundle.getMessage("angal.exatype.problemsoccurredwiththesqlistruction"),
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
							MessageBundle.getMessage("angal.exatype.problemsoccurredwiththesqlistruction"),
							OHSeverityLevel.ERROR));
		}
	}
}
