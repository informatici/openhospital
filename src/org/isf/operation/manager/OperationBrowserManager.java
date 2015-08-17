package org.isf.operation.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.generaldata.MessageBundle;
import org.isf.operation.model.Operation;
import org.isf.operation.service.IoOperations;
import org.isf.opetype.model.OperationType;
import org.isf.utils.exception.OHException;

/**
 * Class that provides gui separation from database operations and gives some
 * useful logic manipulations of the dinamic data (memory)
 * 
 * @author Rick, Vero, Pupo
 * 
 */
public class OperationBrowserManager {

	private IoOperations ioOperations = new IoOperations();

	/**
	 * return the list of {@link Operation}s
	 * 
	 * @return the list of {@link Operation}s. It could be <code>empty</code> or <code>null</code>.
	 */
	public ArrayList<Operation> getOperation() {
		try {
			return ioOperations.getOperation(null);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * return the {@link Operation}s whose type matches specified string
	 * 
	 * @param typeDescription - a type description
	 * @return the list of {@link Operation}s. It could be <code>empty</code> or <code>null</code>.
	 * @throws OHException 
	 */
	public ArrayList<Operation> getOperation(String typecode) {
		try {
			return ioOperations.getOperation(typecode);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * insert an {@link Operation} in the DB
	 * 
	 * @param operation - the {@link Operation} to insert
	 * @return <code>true</code> if the operation has been inserted, <code>false</code> otherwise.
	 */
	public boolean newOperation(Operation operation) {
		try {
			return ioOperations.newOperation(operation);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/** 
	 * updates an {@link Operation} in the DB
	 * 
	 * @param operation - the {@link Operation} to update
	 * @return <code>true</code> if the item has been updated. <code>false</code> other
	 * @throws OHException 
	 */
	public boolean updateOperation(Operation operation) {
		try {
			boolean recordUpdated = ioOperations.hasOperationModified(operation);

			if (!recordUpdated) { 
				// it was not updated
				return ioOperations.updateOperation(operation);
			} else { 
				// it was updated by someone else
				String message = MessageBundle.getMessage("angal.admission.thedatahasbeenupdatedbysomeoneelse")	+ MessageBundle.getMessage("angal.admission.doyouwanttooverwritethedata");
				int response = JOptionPane.showConfirmDialog(null, message, MessageBundle.getMessage("angal.admission.select"), JOptionPane.YES_NO_OPTION);
				boolean overWrite = response== JOptionPane.OK_OPTION;

				if (overWrite) {
					// the user has confirmed he wants to overwrite the record
					return ioOperations.updateOperation(operation);
				}
			}
			return false;
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/** 
	 * Delete a {@link Operation} in the DB
	 * @param operation - the {@link Operation} to delete
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise.
	 */
	public boolean deleteOperation(Operation operation) {
		try {
			return ioOperations.deleteOperation(operation);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * checks if an {@link Operation} code has already been used
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
	
	/**
	 * checks if an {@link Operation} description has already been used within the specified {@link OperationType} 
	 * 
	 * @param description - the {@link Operation} description
	 * @param typeCode - the {@link OperationType} code
	 * @return <code>true</code> if the description is already in use, <code>false</code> otherwise.
	 */
	public boolean descriptionControl(String description, String typeCode) {
		try {
			return ioOperations.isDescriptionPresent(description,typeCode);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
}
