package org.isf.vaccine.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHException;
import org.isf.vaccine.model.Vaccine;
import org.isf.vaccine.service.IoOperations;

/**
 * Class that provides gui separation from database operations and gives some
 * useful logic manipulations of the dinamic data (memory)
 *
 * @author Eva
 *
 *
 * modification history
 *  20/10/2011 - Cla - insert vaccinetype managment
 *
 */
public class VaccineBrowserManager {

	private IoOperations ioOperations = new IoOperations();

	/**
	 * returns the list of {@link Vaccine}s in the DB
	 *
	 * @return the list of {@link Vaccine}s
	 */
	public ArrayList<Vaccine> getVaccine() {
		return getVaccine(null);
	}

	/**
	 * returns the list of {@link Vaccine}s based on vaccine type code
	 *
	 * @param vaccineTypeCode - the type code.
	 * @return the list of {@link Vaccine}s
	 */
	public ArrayList<Vaccine> getVaccine(String vaccineTypeCode) {
		try {
			return ioOperations.getVaccine(vaccineTypeCode);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * inserts a new {@link Vaccine} in the DB
	 *
	 * @param vaccine - the item to insert
	 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise
	 */
	public boolean newVaccine(Vaccine vaccine) {
		try {
			return ioOperations.newVaccine(vaccine);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * updates the specified {@link Vaccine} object.
	 * @param vaccine - the {@link Vaccine} object to update.
	 * @return <code>true</code> if has been updated, <code>false</code> otherwise.
	 */
	public boolean updateVaccine(Vaccine vaccine){
		try {
			boolean recordUpdated = ioOperations.hasVaccineModified(vaccine);

			if (!recordUpdated) { 
				// it was not updated
				return ioOperations.updateVaccine(vaccine);
			} else { 
				// it was updated by someone else
				String message = MessageBundle.getMessage("angal.admission.thedatahasbeenupdatedbysomeoneelse")	+ MessageBundle.getMessage("angal.admission.doyouwanttooverwritethedata");
				int response = JOptionPane.showConfirmDialog(null, message, MessageBundle.getMessage("angal.admission.select"), JOptionPane.YES_NO_OPTION);
				boolean overWrite = response== JOptionPane.OK_OPTION;

				if (overWrite) {
					// the user has confirmed he wants to overwrite the record
					return ioOperations.updateVaccine(vaccine);
				}
			}
			return false;
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * deletes a {@link Vaccine} in the DB
	 *
	 * @param vaccine - the item to delete
	 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise
	 */
	public boolean deleteVaccine(Vaccine vaccine) {
		try {
			return ioOperations.deleteVaccine(vaccine);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * checks if the code is already in use
	 *
	 * @param code - the vaccine code
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
