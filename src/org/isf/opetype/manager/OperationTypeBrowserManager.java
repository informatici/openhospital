package org.isf.opetype.manager;

import java.util.ArrayList;

import org.isf.menu.gui.Menu;
import org.isf.opetype.model.OperationType;
import org.isf.opetype.service.OperationTypeIoOperation;
import org.isf.utils.exception.OHServiceException;

public class OperationTypeBrowserManager {

	private OperationTypeIoOperation ioOperations = Menu.getApplicationContext().getBean(OperationTypeIoOperation.class);
	
	/**
	 * return the list of {@link OperationType}s
	 * 
	 * @return the list of {@link OperationType}s. It could be <code>empty</code> or <code>null</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<OperationType> getOperationType() throws OHServiceException {
        return ioOperations.getOperationType();
	}
	
	/**
	 * insert an {@link OperationType} in the DB
	 * 
	 * @param operationType - the {@link OperationType} to insert
	 * @return <code>true</code> if the {@link OperationType} has been inserted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newOperationType(OperationType operationType) throws OHServiceException {
        return ioOperations.newOperationType(operationType);
	}

	/**
	 * update an {@link OperationType}
	 * 
	 * @param operationType - the {@link OperationType} to update
	 * @return <code>true</code> if the {@link OperationType} has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateOperationType(OperationType operationType) throws OHServiceException {
        return ioOperations.updateOperationType(operationType);
	}
	
	/**
	 * delete an {@link OperationType}
	 * 
	 * @param operationType - the {@link OperationType} to delete
	 * @return <code>true</code> if the {@link OperationType} has been delete, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteOperationType(OperationType operationType) throws OHServiceException {
        return ioOperations.deleteOperationType(operationType);
	}
	
	/**
	 * checks if an {@link OperationType} code has already been used
	 * @param code - the code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean codeControl(String code) throws OHServiceException {
        return ioOperations.isCodePresent(code);
	}
}
