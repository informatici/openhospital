package org.isf.opetype.manager;

import java.util.ArrayList;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.opetype.model.OperationType;
import org.isf.opetype.service.OperationTypeIoOperation;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OperationTypeBrowserManager {

	private final Logger logger = LoggerFactory.getLogger(OperationTypeBrowserManager.class);
	
	private OperationTypeIoOperation ioOperations = Menu.getApplicationContext().getBean(OperationTypeIoOperation.class);
	
	/**
	 * return the list of {@link OperationType}s
	 * 
	 * @return the list of {@link OperationType}s. It could be <code>empty</code> or <code>null</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<OperationType> getOperationType() throws OHServiceException {
		try {
			return ioOperations.getOperationType();
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch (Exception e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null,
					MessageBundle.getMessage("angal.opetype.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * insert an {@link OperationType} in the DB
	 * 
	 * @param operationType - the {@link OperationType} to insert
	 * @return <code>true</code> if the {@link OperationType} has been inserted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newOperationType(OperationType operationType) throws OHServiceException {
		try {
			return ioOperations.newOperationType(operationType);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch (Exception e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null,
					MessageBundle.getMessage("angal.opetype.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * update an {@link OperationType}
	 * 
	 * @param operationType - the {@link OperationType} to update
	 * @return <code>true</code> if the {@link OperationType} has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateOperationType(OperationType operationType) throws OHServiceException {
		try {
			return ioOperations.updateOperationType(operationType);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch (Exception e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null,
					MessageBundle.getMessage("angal.opetype.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * delete an {@link OperationType}
	 * 
	 * @param operationType - the {@link OperationType} to delete
	 * @return <code>true</code> if the {@link OperationType} has been delete, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteOperationType(OperationType operationType) throws OHServiceException {
		try {
			return ioOperations.deleteOperationType(operationType);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch (Exception e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null,
					MessageBundle.getMessage("angal.opetype.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * checks if an {@link OperationType} code has already been used
	 * @param code - the code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise.
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
			throw new OHServiceException(e, new OHExceptionMessage(null,
					MessageBundle.getMessage("angal.opetype.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}
}
