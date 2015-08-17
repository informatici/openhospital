package org.isf.admission.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.admission.model.Admission;
import org.isf.admission.model.AdmittedPatient;
import org.isf.admission.service.IoOperations;
import org.isf.admtype.model.AdmissionType;
import org.isf.disctype.model.DischargeType;
import org.isf.generaldata.MessageBundle;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHException;

public class AdmissionBrowserManager {

	private IoOperations ioOperations = new IoOperations();

	/**
	 * Returns all patients with ward in which they are admitted.
	 * @return the patient list with associated ward or <code>null</code> if the operation fails.
	 */
	public ArrayList<AdmittedPatient> getAdmittedPatients(){
		try {
			return ioOperations.getAdmittedPatients();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Returns all patients with ward in which they are admitted filtering the list using the passed search term.
	 * @param searchTerms the search terms to use for filter the patient list, <code>null</code> if no filter have to be applied.
	 * @return the filtered patient list or <code>null</code> if the operation fails.
	 */
	public ArrayList<AdmittedPatient> getAdmittedPatients(String searchTerms){
		try {
			return ioOperations.getAdmittedPatients(searchTerms);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Returns the admission with the selected id.
	 * @param id the admission id.
	 * @return the admission with the specified id, <code>null</code> otherwise.
	 */
	public Admission getAdmission(int id){
		try {
			return ioOperations.getAdmission(id);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Returns the only one admission without dimission date (or null if none) for the specified patient.
	 * @param patient the patient target of the admission.
	 * @return the patient admission or <code>null</code> if the operation fails.
	 */
	public Admission getCurrentAdmission(Patient patient){
		try {
			return ioOperations.getCurrentAdmission(patient);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Returns all the admissions for the specified patient.
	 * @param patient the patient.
	 * @return the admission list or <code>null</code> if the operation fails.
	 */
	public ArrayList<Admission> getAdmissions(Patient patient){
		try {
			return ioOperations.getAdmissions(patient);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Returns the next prog in the year for a certain ward.
	 * @param wardId the ward id.
	 * @return the next prog or <code>null</code> if the operation fails.
	 */
	public int getNextYProg(String wardId){
		try {
			return ioOperations.getNextYProg(wardId);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return 1;
		}
	}

	/**
	 * Lists the {@link AdmissionType}s.
	 * @return the admission types  or <code>null</code> if the operation fails.
	 */
	public ArrayList<AdmissionType> getAdmissionType(){	
		try {
			return ioOperations.getAdmissionType();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Lists the {@link DischargeType}s.
	 * @return the discharge types  or <code>null</code> if the operation fails.
	 */
	public ArrayList<DischargeType> getDischargeType(){	
		try {
			return ioOperations.getDischargeType();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Inserts a new admission.
	 * @param admission the admission to insert.
	 * @return <code>true</code> if the admission has been successfully inserted, <code>false</code> otherwise.
	 */
	public boolean newAdmission(Admission admission){
		try {
			return ioOperations.newAdmission(admission);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Inserts a new {@link Admission} and the returns the generated id.
	 * @param admission the admission to insert.
	 * @return the generated id or <code>null</code> if the operation fails.
	 */
	public int newAdmissionReturnKey(Admission admission){
		try {
			return ioOperations.newAdmissionReturnKey(admission);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return -1;
		}
	}

	/**
	 * Updates the specified {@link Admission} object.
	 * @param admission the admission object to update.
	 * @return <code>true</code> if has been updated, <code>false</code> otherwise.
	 */
	public boolean updateAdmission(Admission admission){
		try {
			boolean recordUpdated = ioOperations.hasAdmissionModified(admission);

			if (!recordUpdated) { 
				// it was not updated
				return ioOperations.updateAdmission(admission);
			} else { 
				// it was updated by someone else
				String message = MessageBundle.getMessage("angal.admission.thedatahasbeenupdatedbysomeoneelse")	+ MessageBundle.getMessage("angal.admission.doyouwanttooverwritethedata");
				int response = JOptionPane.showConfirmDialog(null, message, MessageBundle.getMessage("angal.admission.select"), JOptionPane.YES_NO_OPTION);
				boolean overWrite = response== JOptionPane.OK_OPTION;

				if (overWrite) {
					// the user has confirmed he wants to overwrite the record
					return ioOperations.updateAdmission(admission);
				}
			}
			return false;
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Sets an admission record to deleted.
	 * @param admissionId the admission id.
	 * @return <code>true</code> if the record has been set to delete.
	 */
	public boolean setDeleted(int admissionId){
		try {
			return ioOperations.setDeleted(admissionId);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Counts the number of used bed for the specified ward.
	 * @param wardId the ward id.
	 * @return the number of used beds.
	 */
	public int getUsedWardBed(String wardId) {
		try {
			return ioOperations.getUsedWardBed(wardId);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return 0;
		}
	}

	/**
	 * Deletes the patient photo.
	 * @param patientId the patient id.
	 * @return <code>true</code> if the photo has been deleted, <code>false</code> otherwise.
	 */
	public boolean deletePatientPhoto(int id) {
		try {
			return ioOperations.deletePatientPhoto(id);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
}
