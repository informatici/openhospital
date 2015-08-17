package org.isf.patvac.manager;

/*------------------------------------------
* PatVacManager - patient-vaccine manager
* -----------------------------------------
* modification history
* 25/08/2011 - claudia - first beta version
* 14/11/2011 - claudia - inserted search condition on date
*------------------------------------------*/


import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import org.isf.patvac.service.IoOperations;
import org.isf.patvac.model.PatientVaccine;
import org.isf.utils.exception.OHException;



public class PatVacManager {

	private IoOperations ioOperations = new IoOperations();
	
	/**
	 * returns all {@link PatientVaccine}s of today or one week ago
	 * 
	 * @param minusOneWeek - if <code>true</code> return the last week
	 * @return the list of {@link PatientVaccine}s
	 */  
	public ArrayList<PatientVaccine> getPatientVaccine(boolean minusOneWeek) {
		try {
			return  ioOperations.getPatientVaccine(minusOneWeek);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * returns all {@link PatientVaccine}s within <code>dateFrom</code> and
	 * <code>dateTo</code>
	 * 
	 * @param vaccineTypeCode
	 * @param vaccineCode
	 * @param dateFrom
	 * @param dateTo
	 * @param sex
	 * @param ageFrom
	 * @param ageTo
	 * @return the list of {@link PatientVaccine}s
	 */
	public ArrayList<PatientVaccine> getPatientVaccine(String vaccineTypeCode,String vaccineCode, 
													   GregorianCalendar dateFrom, GregorianCalendar dateTo, 
													   char sex, int ageFrom, int ageTo) {
		try {
			return ioOperations.getPatientVaccine(vaccineTypeCode, vaccineCode, dateFrom, dateTo, sex, ageFrom, ageTo);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * inserts a {@link PatientVaccine} in the DB
	 * 
	 * @param patVac - the {@link PatientVaccine} to insert
	 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise 
	 */
	public boolean newPatientVaccine(PatientVaccine patVac) {
		try {
			return ioOperations.newPatientVaccine(patVac);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * updates a {@link PatientVaccine} 
	 * 
	 * @param patVac - the {@link PatientVaccine} to update
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise 
	 */
	public boolean updatePatientVaccine(PatientVaccine patVac) {
		try {
			return ioOperations.updatePatientVaccine(patVac);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * deletes a {@link PatientVaccine} 
	 * 
	 * @param patVac - the {@link PatientVaccine} to delete
	 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise 
	 */
	public boolean deletePatientVaccine(PatientVaccine patVac) {
		try {
			return ioOperations.deletePatientVaccine(patVac);
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
}

