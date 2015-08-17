package org.isf.lab.manager;

/*------------------------------------------
 * LabManager - laboratory exam manager class
 * -----------------------------------------
 * modification history
 * 10/11/2006 - ross - added editing capability 
 *------------------------------------------*/

import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import org.isf.lab.model.Laboratory;
import org.isf.lab.model.LaboratoryForPrint;
import org.isf.lab.service.IoOperations;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHException;

public class LabManager {

	private IoOperations ioOperations = new IoOperations();

	/**
	 * Return the whole list of exams ({@link Laboratory}s) within last year.
	 * @return the list of {@link Laboratory}s. It could be <code>empty</code>.
	 */
	public ArrayList<Laboratory> getLaboratory() {
		try {
			return ioOperations.getLaboratory();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return new ArrayList<Laboratory>();
		}
	}

	/**
	 * Return a list of exams ({@link Laboratory}s) related to a {@link Patient}.
	 * 
	 * @param aPatient - the {@link Patient}.
	 * @return the list of {@link Laboratory}s related to the {@link Patient}. It could be <code>empty</code>.
	 */
	public ArrayList<Laboratory> getLaboratory(Patient aPatient) {
		try {
			return ioOperations.getLaboratory(aPatient);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return new ArrayList<Laboratory>();
		}
	}

	/*
	 * NO LONGER USED
	 * 
	 * public ArrayList<Laboratory> getLaboratory(String aCode){ return
	 * ioOperations.getLaboratory(); }
	 */

	/**
	 * Return a list of exams ({@link Laboratory}s) between specified dates and matching passed exam name
	 * @param exam - the exam name as <code>String</code>
	 * @param dateFrom - the lower date for the range
	 * @param dateTo - the highest date for the range
	 * @return the list of {@link Laboratory}s. It could be <code>empty</code>.
	 */
	public ArrayList<Laboratory> getLaboratory(String exam, GregorianCalendar dateFrom, GregorianCalendar dateTo) {
		try {
			return ioOperations.getLaboratory(exam, dateFrom, dateTo);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return new ArrayList<Laboratory>();
		}
	}

	/**
	 * Return a list of exams suitable for printing ({@link LaboratoryForPrint}s) 
	 * between specified dates and matching passed exam name
	 * @param exam - the exam name as <code>String</code>
	 * @param dateFrom - the lower date for the range
	 * @param dateTo - the highest date for the range
	 * @return the list of {@link LaboratoryForPrint}s . It could be <code>empty</code>.
	 */
	public ArrayList<LaboratoryForPrint> getLaboratoryForPrint(String exam, GregorianCalendar dateFrom, GregorianCalendar dateTo) {
		try {
			return ioOperations.getLaboratoryForPrint(exam, dateFrom, dateTo);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return new ArrayList<LaboratoryForPrint>();
		}
	}

	/*
	 * NO LONGER USED
	 * 
	 * public ArrayList<LaboratoryForPrint> getLaboratoryForPrint() {
		return ioOperations.getLaboratoryForPrint();
	}*/

	/*
	 * NO LONGER USED
	 * 
	 * public ArrayList<LaboratoryForPrint> getLaboratoryForPrint(String exam,
	 * String result) { return ioOperations.getLaboratoryForPrint(exam,result);
	 * }
	 */

	/**
	 * Inserts one Laboratory exam {@link Laboratory} (Procedure One)
	 * @param laboratory - the {@link Laboratory} to insert
	 * @param dbQuery - the connection manager
	 * @return <code>true</code> if the exam has been inserted, <code>false</code> otherwise
	 */
	public boolean newLabFirstProcedure(Laboratory laboratory) {
		try {
			return ioOperations.newLabFirstProcedure(laboratory);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Inserts one Laboratory exam {@link Laboratory} with multiple results (Procedure Two) 
	 * @param laboratory - the {@link Laboratory} to insert
	 * @param labRow - the list of results ({@link String}s)
	 * @return <code>true</code> if the exam has been inserted with all its results, <code>false</code> otherwise
	 */
	public boolean newLabSecondProcedure(Laboratory laboratory, ArrayList<String> labRow) {
		try {
			return ioOperations.newLabSecondProcedure(laboratory, labRow);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Update an already existing Laboratory exam {@link Laboratory} (Procedure One).
	 * If old exam was Procedure Two all its releated result are deleted.
	 * @param laboratory - the {@link Laboratory} to update
	 * @return <code>true</code> if the exam has been updated, <code>false</code> otherwise
	 */
	public boolean editLabFirstProcedure(Laboratory laboratory) {
		try {
			return ioOperations.updateLabFirstProcedure(laboratory);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Update an already existing Laboratory exam {@link Laboratory} (Procedure Two).
	 * Previous results are deleted and replaced with new ones.
	 * @param laboratory - the {@link Laboratory} to update
	 * @return <code>true</code> if the exam has been updated with all its results, <code>false</code> otherwise
	 */
	public boolean editLabSecondProcedure(Laboratory laboratory, ArrayList<String> labRow) {
		try {
			return ioOperations.editLabSecondProcedure(laboratory, labRow);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Delete a Laboratory exam {@link Laboratory} (Procedure One or Two).
	 * Previous results, if any, are deleted as well.
	 * @param laboratory - the {@link Laboratory} to delete
	 * @return <code>true</code> if the exam has been deleted with all its results, if any. <code>false</code> otherwise
	 */
	public boolean deleteLaboratory(Laboratory laboratory) {
		try {
			return ioOperations.deleteLaboratory(laboratory);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
}
