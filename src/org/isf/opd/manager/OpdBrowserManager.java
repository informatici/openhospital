package org.isf.opd.manager;


import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import org.isf.generaldata.MessageBundle;
import org.isf.opd.model.Opd;
import org.isf.opd.service.IoOperations;
import org.isf.utils.exception.OHException;


/**
 * @author Vero
 * 
 */
public class OpdBrowserManager {
	
	private IoOperations ioOperations = new IoOperations();
	
	/**
	 * return all OPDs of today or one week ago
	 * 
	 * @param oneWeek - if <code>true</code> return the last week, only today otherwise.
	 * @return the list of OPDs. It could be <code>null</code>.
	 */
	public ArrayList<Opd> getOpd(boolean oneWeek){
		try {
			return ioOperations.getOpdList(oneWeek);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * 
	 * return all OPDs within specified dates
	 * 
	 * @param diseaseTypeCode
	 * @param diseaseCode
	 * @param dateFrom
	 * @param dateTo
	 * @param ageFrom
	 * @param ageTo
	 * @param sex
	 * @param newPatient
	 * @return the list of OPDs. It could be <code>null</code>.
	 */
	public ArrayList<Opd> getOpd(String diseaseTypeCode,String diseaseCode, GregorianCalendar dateFrom,GregorianCalendar dateTo,int ageFrom, int ageTo,char sex,String newPatient) {
		try {
			return ioOperations.getOpdList(diseaseTypeCode,diseaseCode,dateFrom,dateTo,ageFrom,ageTo,sex,newPatient);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * returns all {@link Opd}s associated to specified patient ID
	 * 
	 * @param patID - the patient ID
	 * @return the list of {@link Opd}s associated to specified patient ID.
	 * 		   the whole list of {@link Opd}s if <code>0</code> is passed.
	 */
	public ArrayList<Opd> getOpdList(int patientcode) {
		try {
			return ioOperations.getOpdList(patientcode);
		} catch (OHException e) {
			return null;
		}
	}

	/**
	 * insert a new item in the db
	 * 
	 * @param an {@link OPD}
	 * @return <code>true</code> if the item has been inserted
	 */
	public boolean newOpd(Opd opd) {
		try {
			return ioOperations.newOpd(opd);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Updates the specified {@link Opd} object.
	 * @param opd - the {@link Opd} object to update.
	 * @return <code>true</code> if has been updated, <code>false</code> otherwise.
	 */
	public boolean updateOpd(Opd opd){
		try {
			boolean recordUpdated = ioOperations.hasOpdModified(opd);

			if (!recordUpdated) { 
				// it was not updated
				return ioOperations.updateOpd(opd);
			} else { 
				// it was updated by someone else
				String message = MessageBundle.getMessage("angal.admission.thedatahasbeenupdatedbysomeoneelse")	+ MessageBundle.getMessage("angal.admission.doyouwanttooverwritethedata");
				int response = JOptionPane.showConfirmDialog(null, message, MessageBundle.getMessage("angal.admission.select"), JOptionPane.YES_NO_OPTION);
				boolean overWrite = response== JOptionPane.OK_OPTION;

				if (overWrite) {
					// the user has confirmed he wants to overwrite the record
					return ioOperations.updateOpd(opd);
				}
			}
			return false;
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * delete an {@link OPD} from the db
	 * 
	 * @param opd - the {@link OPD} to delete
	 * @return <code>true</code> if the item has been deleted. <code>false</code> otherwise.
	 */
	public boolean deleteOpd(Opd opd) {
		try {
			return ioOperations.deleteOpd(opd);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * Returns the max progressive number within specified year or within current year if <code>0</code>.
	 * 
	 * @param year
	 * @return <code>int</code> - the progressive number in the year
	 */
	public int getProgYear(int year) {
		try {
			return ioOperations.getProgYear(year);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return 0;
		}
	}
	
	/**
	 * return the last Opd in time associated with specified patient ID. 
	 * 
	 * @param patID - the patient ID
	 * @return last Opd associated with specified patient ID or <code>null</code>
	 */
	public Opd getLastOpd(int patientcode) {
		try {
			return ioOperations.getLastOpd(patientcode);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
}
