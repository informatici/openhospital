/**
 * 19-dec-2005
 * 14-jan-2006
 */
package org.isf.exa.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.exa.model.Exam;
import org.isf.exa.service.IoOperations;
import org.isf.exatype.model.ExamType;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHException;

/**
 * Class that provides gui separation from database operations and gives some
 * useful logic manipulations of the dinamic data (memory)
 * 
 * @author bob
 * 
 */
public class ExamBrowsingManager {

	private IoOperations ioOperations = new IoOperations();

	/**
	 * Returns the list of {@link Exam}s
	 * @return the list of {@link Exam}s. It could be <code>null</code>
	 */
	public ArrayList<Exam> getExams() {
		try {
			return ioOperations.getExams();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * Returns the list of {@link Exam}s
	 * @return the list of {@link Exam}s. It could be <code>null</code>
	 * @deprecated use getExam() instead
	 */
	public ArrayList<Exam> getExamsbyDesc() {
		return this.getExams();
	}
	
	/**
	 * Returns the list of {@link Exam}s that matches passed description
	 * @param description - the exam description
	 * @return the list of {@link Exam}s. It could be <code>null</code>
	 */
	public ArrayList<Exam> getExams(String description) {
		try {
			return ioOperations.getExamsByDesc(description);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * Returns the list of {@link ExamType}s
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
	 * This function controls the presence of a record with the same key as in
	 * the parameter; Returns false if the query finds no record, else returns
	 * true
	 * 
	 * @param the {@link Exam}
	 * @return <code>true</code> if the Exam code has already been used, <code>false</code> otherwise
	 */
	public boolean isKeyPresent(Exam exam) {
		try {
			return ioOperations.isKeyPresent(exam);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * Insert a new {@link Exam} in the DB.
	 * 
	 * @param exam - the {@link Exam} to insert
	 * @return <code>true</code> if the {@link Exam} has been inserted, <code>false</code> otherwise
	 */
	public boolean newExam(Exam exam) {
		try {
			return ioOperations.newExam(exam);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * 
	 * Updates an existing {@link Exam} in the db
	 * 
	 * @param exam -  the {@link Exam} to update
	 * @return <code>true</code> if the existing {@link Exam} has been updated, <code>false</code> otherwise
	 */
	public boolean updateExam(Exam exam) {
		return this.updateExam(exam, true);
	}
	
	/**
	 * Updates an existing {@link Exam} in the db
	 * 
	 * @param exam -  the {@link Exam} to update
	 * @param check - if <code>true</code> check if the {@link Exam} has been modified since last read
	 * @return <code>true</code> if the existing {@link Exam} has been updated, <code>false</code> otherwise
	 */
	public boolean updateExam(Exam exam, boolean check) {
		try {
			if (!ioOperations.updateExam(exam, check)) {
				int ok = JOptionPane.showConfirmDialog(null,
						MessageBundle.getMessage("angal.exa.thedatahasbeenupdatedbysomeoneelse") + ".\n" + MessageBundle.getMessage("angal.exa.doyouwanttooverwritethedata") + "?",
						MessageBundle.getMessage("angal.exa.select"), JOptionPane.YES_NO_OPTION);
				if (ok != JOptionPane.OK_OPTION)
					return false;
				else 
					return this.updateExam(exam, false);
			} else 
				return true;
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Delete an {@link Exam}
	 * @param exam - the {@link Exam} to delete
	 * @return <code>true</code> if the {@link Exam} has been deleted, <code>false</code> otherwise
	 */
	public boolean deleteExam(Exam exam) {
		try {
			return ioOperations.deleteExam(exam);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
}
