package org.isf.medstockmovtype.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;


import org.isf.medstockmovtype.model.MovementType;
import org.isf.medstockmovtype.service.IoOperation;
import org.isf.utils.exception.OHException;

/**
 * Manager class for the medical stock movement type.
 *
 */
public class MedicaldsrstockmovTypeBrowserManager {

	private IoOperation ioOperations = new IoOperation();

	/**
	 * Returns all the medical stock movement types.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return all the medical stock movement types.
	 */
	public ArrayList<MovementType> getMedicaldsrstockmovType() {
		try {
			return ioOperations.getMedicaldsrstockmovType();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Save the specified {@link MovementType}.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param medicaldsrstockmovType the medical stock movement type to save.
	 * @return <code>true</code> if the medical stock movement type has been saved, <code>false</code> otherwise.
	 */
	public boolean newMedicaldsrstockmovType(MovementType medicaldsrstockmovType) {
		try {
			return ioOperations.newMedicaldsrstockmovType(medicaldsrstockmovType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Updates the specified {@link MovementType}.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param medicaldsrstockmovType the medical stock movement type to update.
	 * @return <code>true</code> if the medical stock movement type has been updated, <code>false</code> otherwise.
	 */
	public boolean updateMedicaldsrstockmovType(MovementType medicaldsrstockmovType) {
		try {	
			return ioOperations.updateMedicaldsrstockmovType(medicaldsrstockmovType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Checks if the specified {@link MovementType} code is already used.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, <code>false</code> otherwise.
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
	 * Deletes the specified {@link MovementType}.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param medicaldsrstockmovType the medical stock movement type to delete.
	 * @return <code>true</code> if the medical stock movement type has been deleted, <code>false</code> otherwise.
	 */
	public boolean deleteMedicaldsrstockmovType(MovementType medicaldsrstockmovType) {
		try {
			return ioOperations.deleteMedicaldsrstockmovType(medicaldsrstockmovType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

}
