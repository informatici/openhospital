package org.isf.exatype.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.exatype.model.ExamType;
import org.isf.exatype.service.IoOperation;
import org.isf.utils.exception.OHException;

public class ExamTypeBrowserManager {

	private IoOperation ioOperations = new IoOperation();

	/**
	 * Return the list of {@link ExamType}s.
	 * @return the list of {@link ExamType}s. It could be <code>null</code>
	 */
	public ArrayList<ExamType> getExamType() {
		try {
			return ioOperations.getExamType();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Insert a new {@link ExamType} in the DB.
	 * 
	 * @param examType - the {@link ExamType} to insert.
	 * @return <code>true</code> if the examType has been inserted, <code>false</code> otherwise.
	 */
	public boolean newExamType(ExamType examType) {
		try {
			return ioOperations.newExamType(examType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Update an already existing {@link ExamType}.
	 * @param examType - the {@link ExamType} to update
	 * @return <code>true</code> if the examType has been updated, <code>false</code> otherwise.
	 */
	public boolean updateExamType(ExamType examType) {
		try {
			return ioOperations.updateExamType(examType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * This function controls the presence of a record with the same code as in
	 * the parameter.
	 * @param code - the code
	 * @return <code>true</code> if the code is present, <code>false</code> otherwise.
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
	 * Delete the passed {@link ExamType}.
	 * @param examType - the {@link ExamType} to delete.
	 * @return <code>true</code> if the examType has been deleted, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean deleteExamType(ExamType examType) {
		try {
			return ioOperations.deleteExamType(examType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
}
