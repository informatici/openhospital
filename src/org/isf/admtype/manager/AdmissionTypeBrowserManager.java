package org.isf.admtype.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;


import org.isf.admtype.model.AdmissionType;
import org.isf.admtype.service.IoOperation;
import org.isf.utils.exception.OHException;

public class AdmissionTypeBrowserManager {

	private IoOperation ioOperations = new IoOperation();

	/**
	 * Returns all the available {@link AdmissionType}s.
	 * @return a list of admission types or <code>null</code> if the operation fails.
	 */
	public ArrayList<AdmissionType> getAdmissionType() {
		try {
			return ioOperations.getAdmissionType();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Stores a new {@link AdmissionType}.
	 * @param admissionType the admission type to store.
	 * @return <code>true</code> if the admission type has been stored, <code>false</code> otherwise.
	 */
	public boolean newAdmissionType(AdmissionType admissionType) {
		try {
			return ioOperations.newAdmissionType(admissionType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Updates the specified {@link AdmissionType}.
	 * @param admissionType the admission type to update.
	 * @return <code>true</code> if the admission type has been updated, <code>false</code> otherwise.
	 */
	public boolean updateAdmissionType(AdmissionType admissionType) {
		try {
			return ioOperations.updateAdmissionType(admissionType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Checks if the specified Code is already used by others {@link AdmissionType}s.
	 * @param code the admission type code to check.
	 * @return <code>true</code> if the code is already used, <code>false</code> otherwise.
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
	 * Deletes the specified {@link AdmissionType}.
	 * @param admissionType the admission type to delete.
	 * @return <code>true</code> if the admission type has been deleted, <code>false</code> otherwise.
	 */
	public boolean deleteAdmissionType(AdmissionType admissionType) {
		try {
			return ioOperations.deleteAdmissionType(admissionType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
}
