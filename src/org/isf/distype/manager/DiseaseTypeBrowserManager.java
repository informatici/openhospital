package org.isf.distype.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;


import org.isf.distype.model.DiseaseType;
import org.isf.distype.service.IoOperation;
import org.isf.utils.exception.OHException;

/**
 * Manager class for DisType module.
 */
public class DiseaseTypeBrowserManager {

	private IoOperation ioOperations = new IoOperation();

	/**
	 * Returns all the stored {@link DiseaseType}s.
	 * @return a list of disease type, <code>null</code> if the operation is failed.
	 */
	public ArrayList<DiseaseType> getDiseaseType() {
		try {
			return ioOperations.getDiseaseTypes();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Store the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to store.
	 * @return <code>true</code> if the {@link DiseaseType} has been stored, <code>false</code> otherwise.
	 */
	public boolean newDiseaseType(DiseaseType diseaseType) {
		try {
			return ioOperations.newDiseaseType(diseaseType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Updates the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to update.
	 * @return <code>true</code> if the disease type has been updated, false otherwise.
	 */
	public boolean updateDiseaseType(DiseaseType diseaseType) {
		try {
			return ioOperations.updateDiseaseType(diseaseType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Checks if the specified code is already used by any {@link DiseaseType}.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, false otherwise.
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
	 * Deletes the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to remove.
	 * @return <code>true</code> if the disease has been removed, <code>false</code> otherwise.
	 */
	public boolean deleteDiseaseType(DiseaseType diseaseType) {
		try {
			return ioOperations.deleteDiseaseType(diseaseType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
}
