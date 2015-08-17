package org.isf.pregtreattype.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;


import org.isf.pregtreattype.model.PregnantTreatmentType;
import org.isf.pregtreattype.service.IoOperation;
import org.isf.utils.exception.OHException;

public class PregnantTreatmentTypeBrowserManager {

	private IoOperation ioOperations = new IoOperation();
	
	/**
	 * return the list of {@link PregnantTreatmentType}s
	 * 
	 * @return the list of {@link PregnantTreatmentType}s
	 */
	public ArrayList<PregnantTreatmentType> getPregnantTreatmentType() {
		try {
			return ioOperations.getPregnantTreatmentType();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * insert a {@link PregnantTreatmentType} in the DB
	 * 
	 * @param pregnantTreatmentType - the {@link PregnantTreatmentType} to insert
	 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise
	 */
	public boolean newPregnantTreatmentType(PregnantTreatmentType pregnantTreatmentType) {
		try {
			return ioOperations.newPregnantTreatmentType(pregnantTreatmentType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * update a {@link PregnantTreatmentType} in the DB
	 * 
	 * @param pregnantTreatmentType - the {@link PregnantTreatmentType} to update
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise
	 */
	public boolean updatePregnantTreatmentType(PregnantTreatmentType pregnantTreatmentType) {
		try {
			return ioOperations.updatePregnantTreatmentType(pregnantTreatmentType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * delete a {@link PregnantTreatmentType} in the DB
	 * 
	 * @param pregnantTreatmentType - the {@link PregnantTreatmentType} to delete
	 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise
	 */
	public boolean deletePregnantTreatmentType(PregnantTreatmentType pregnantTreatmentType) {
		try {
			return ioOperations.deletePregnantTreatmentType(pregnantTreatmentType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * check if the code is already in use
	 * 
	 * @param code - the code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
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
