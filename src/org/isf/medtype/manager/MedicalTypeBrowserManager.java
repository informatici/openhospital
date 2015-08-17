package org.isf.medtype.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;


import org.isf.medtype.model.MedicalType;
import org.isf.medtype.service.IoOperation;
import org.isf.utils.exception.OHException;

/**
 * Manager class for the medical type module.
 *
 */
public class MedicalTypeBrowserManager {

	private IoOperation ioOperations = new IoOperation();

	/**
	 * Retrieves all the medical types.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return all the medical types.
	 */
	public ArrayList<MedicalType> getMedicalType() {
		try {
			return ioOperations.getMedicalTypes();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Saves the specified medical type.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param medicalType the medical type to save.
	 * @return <code>true</code> if the medical type has been saved, <code>false</code> otherwise.
	 */
	public boolean newMedicalType(MedicalType medicalType) {
		try {
			return ioOperations.newMedicalType(medicalType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Updates the specified medical type.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param medicalType the medical type to update.
	 * @return <code>true</code> if the medical type has been updated, <code>false</code> otherwise.
	 */
	public boolean updateMedicalType(MedicalType medicalType) {
		try {
			return ioOperations.updateMedicalType(medicalType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Checks if the specified medical type code is already used.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, <code>false</code> owtherwise.
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
	 * Deletes the specified medical type.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param medicalType the medical type to delete.
	 * @return <code>true</code> if the medical type has been deleted, <code>false</code> otherwise.
	 */
	public boolean deleteMedicalType(MedicalType medicalType) {
		try {
			return ioOperations.deleteMedicalType(medicalType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
}
