package org.isf.exa.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.exa.model.ExamRow;
import org.isf.exa.service.IoOperations;
import org.isf.utils.exception.OHException;

public class ExamRowBrowsingManager {
	private IoOperations ioOperations = new IoOperations();
		
	/**
	 * Returns the list of {@link ExamRow}s
	 * @return the list of {@link ExamRow}s. It could be <code>null</code>
	 */
	public ArrayList<ExamRow> getExamRow() {
		return this.getExamRow(null, null);
	}
	
	/**
	 * Returns a list of {@link ExamRow}s that matches passed exam code
	 * @param aExamCode - the exam code
	 * @return the list of {@link ExamRow}s. It could be <code>null</code>
	 */
	public ArrayList<ExamRow> getExamRow(String aExamCode) {
		return this.getExamRow(aExamCode, null);
	}

	/**
	 * Returns a list of {@link ExamRow}s that matches passed exam code and description
	 * @param aExamCode - the exam code
	 * @param aDescription - the exam description
	 * @return the list of {@link ExamRow}s. It could be <code>null</code>
	 */
	public ArrayList<ExamRow> getExamRow(String aExamCode, String aDescription) {
		try {
			return ioOperations.getExamRow(aExamCode,aDescription);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * Insert a new {@link ExamRow} in the DB.
	 * 
	 * @param examRow - the {@link ExamRow} to insert
	 * @return <code>true</code> if the {@link ExamRow} has been inserted, <code>false</code> otherwise
	 */
	public boolean newExamRow(ExamRow examRow) {
		try {
			return ioOperations.newExamRow(examRow);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Delete an {@link ExamRow}.
	 * @param examRow - the {@link ExamRow} to delete
	 * @return <code>true</code> if the {@link ExamRow} has been deleted, <code>false</code> otherwise
	 */
	public boolean deleteExamRow(ExamRow examRow) {
		try {
			return ioOperations.deleteExamRow(examRow);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
}
