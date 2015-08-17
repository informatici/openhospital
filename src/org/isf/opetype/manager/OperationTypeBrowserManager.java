package org.isf.opetype.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;


import org.isf.opetype.model.OperationType;
import org.isf.opetype.service.IoOperation;
import org.isf.utils.exception.OHException;

public class OperationTypeBrowserManager {

	private IoOperation ioOperations = new IoOperation();
	
	/**
	 * return the list of {@link OperationType}s
	 * 
	 * @return the list of {@link OperationType}s. It could be <code>empty</code> or <code>null</code>.
	 */
	public ArrayList<OperationType> getOperationType() {
		try {
			return ioOperations.getOperationType();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * insert an {@link OperationType} in the DB
	 * 
	 * @param operationType - the {@link OperationType} to insert
	 * @return <code>true</code> if the {@link OperationType} has been inserted, <code>false</code> otherwise.
	 */
	public boolean newOperationType(OperationType operationType) {
		try {
			return ioOperations.newOperationType(operationType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * update an {@link OperationType}
	 * 
	 * @param operationType - the {@link OperationType} to update
	 * @return <code>true</code> if the {@link OperationType} has been updated, <code>false</code> otherwise.
	 */
	public boolean updateOperationType(OperationType operationType) {
		try {
			return ioOperations.updateOperationType(operationType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * delete an {@link OperationType}
	 * 
	 * @param operationType - the {@link OperationType} to delete
	 * @return <code>true</code> if the {@link OperationType} has been delete, <code>false</code> otherwise.
	 */
	public boolean deleteOperationType(OperationType operationType) {
		try {
			return ioOperations.deleteOperationType(operationType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * checks if an {@link OperationType} code has already been used
	 * @param code - the code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise.
	 */
	public boolean codeControl(String code) {
		try {
			return ioOperations.isCodePresent(code);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
}
