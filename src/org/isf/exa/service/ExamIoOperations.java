package org.isf.exa.service;
/*------------------------------------------
 * IoOperations - provides the io operations for recovering and managing exam records from the database.
 * -----------------------------------------
 * modification history
 * ??/??/2005 - Davide/Theo - first beta version 
 * 07/11/2006 - ross - modified to accept, within the description, the character quote (')
 *                     (to do this, just double every quote. replaceall("'","''") 
 *                     when record locked all data is saved now, not only descritpion
 *------------------------------------------*/

import java.util.ArrayList;
import java.util.List;

import org.isf.exa.model.Exam;
import org.isf.exa.model.ExamRow;
import org.isf.exatype.model.ExamType;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

@Component
public class ExamIoOperations {

	/**
	 * Returns a list of {@link ExamRow}s that matches passed exam code and description
	 * @param aExamCode - the exam code
	 * @param aDescription - the exam description
	 * @return the list of {@link ExamRow}s
	 * @throws OHException
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<ExamRow> getExamRow(
			String aExamCode, 
			String aDescription) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<ExamRow> examrows = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		try {
			jpa.beginTransaction();
			
			String query = "SELECT * FROM EXAMROW";
			if (aExamCode != null) {
				query += " WHERE EXR_EXA_ID_A = ?";
				params.add(aExamCode);
			}
			if (aDescription != null) {
				query += " AND EXR_DESC = ?";
				params.add(aDescription);
			}
			query += " ORDER BY EXR_EXA_ID_A, EXR_DESC";
			jpa.createQuery(query, ExamRow.class, false);
			jpa.setParameters(params, false);
			List<ExamRow> examRowList = (List<ExamRow>)jpa.getList();
			examrows = new ArrayList<ExamRow>(examRowList);			
			
			jpa.commitTransaction();
		} catch (OHException e) {
			// DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return examrows;
	}

	/**
	 * Returns the list of {@link Exam}s
	 * @return the list of {@link Exam}s
	 * @throws OHException
	 */
	public ArrayList<Exam> getExams() throws OHException 
	{
		return getExamsByDesc(null);
	}
	
	/**
	 * Returns the list of {@link Exam}s that matches passed description
	 * @param description - the exam description
	 * @return the list of {@link Exam}s
	 * @throws OHException
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<Exam> getExamsByDesc(
			String description) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Exam> exams = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		try {
			jpa.beginTransaction();
			
			String query = "SELECT * FROM EXAM JOIN EXAMTYPE ON EXA_EXC_ID_A = EXC_ID_A";
			if (description != null) {
				query += " WHERE EXC_DESC LIKE ?";
				params.add('%'+description+'%');
			}
			query += " ORDER BY EXC_DESC, EXA_DESC";
			jpa.createQuery(query, Exam.class, false);
			jpa.setParameters(params, false);
			List<Exam> examList = (List<Exam>)jpa.getList();
			exams = new ArrayList<Exam>(examList);			
			
			jpa.commitTransaction();
		} catch (OHException e) {
			// DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return exams;
	}

	/**
	 * Returns the list of {@link ExamType}s
	 * @return the list of {@link ExamType}s
	 * @throws OHException
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<ExamType> getExamType() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<ExamType> examTypes = null;
				
		try {
			jpa.beginTransaction();
			
			String query = "SELECT EXC_ID_A, EXC_DESC FROM EXAMTYPE ORDER BY EXC_DESC";
			jpa.createQuery(query, ExamType.class, false);
			List<ExamType> examTypeList = (List<ExamType>)jpa.getList();
			examTypes = new ArrayList<ExamType>(examTypeList);			
			
			jpa.commitTransaction();
		} catch (OHException e) {
			// DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return examTypes;
	}

	/**
	 * Insert a new {@link Exam} in the DB.
	 * 
	 * @param exam - the {@link Exam} to insert
	 * @return <code>true</code> if the {@link Exam} has been inserted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean newExam(
			Exam exam) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		try {
			jpa.beginTransaction();	
			jpa.persist(exam);
	    	jpa.commitTransaction();
		} catch (OHException e) {
			// DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return result;
	}

	/**
	 * Insert a new {@link ExamRow} in the DB.
	 * 
	 * @param examRow - the {@link ExamRow} to insert
	 * @return <code>true</code> if the {@link ExamRow} has been inserted, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean newExamRow(
			ExamRow examRow) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		try {
			jpa.beginTransaction();	
			jpa.persist(examRow);
	    	jpa.commitTransaction();
		} catch (OHException e) {
			// DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return result;
	}

	/**
	 * Update an already existing {@link Exam}.
	 * @param exam - the {@link Exam} to update
	 * @param check - if <code>true</code> it will perform an integrity check
	 * @return <code>true</code> if the {@link Exam} has been updated, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean updateExam(
			Exam exam, 
			boolean check) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		try{
			jpa.beginTransaction();	
			if (check) 
			{ 
				Exam foundExam = (Exam)jpa.find(Exam.class, exam.getCode()); 
				if (foundExam == null)
				{
					throw new OHException(MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"));		
				}
				else if (foundExam.getLock() != exam.getLock())
				{
					result = false;
				}		
			}	
			jpa.merge(exam);
	    	jpa.commitTransaction();
	} catch (OHException e) {
		// DbJpaUtil managed exception
		jpa.rollbackTransaction();
		throw e;
	}
		return result;	
	}

	/**
	 * Delete an {@link Exam}
	 * @param exam - the {@link Exam} to delete
	 * @return <code>true</code> if the {@link Exam} has been deleted, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean deleteExam(
			Exam exam) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;		
		ArrayList<Object> params = new ArrayList<Object>();

		try {
			jpa.beginTransaction();
			
			jpa.createQuery("DELETE FROM EXAMROW WHERE EXR_EXA_ID_A = ?", ExamRow.class, false);
			params.add(exam.getCode());
			jpa.setParameters(params, false);
			jpa.executeUpdate();
			
			Exam examToRemove = (Exam) jpa.find(Exam.class, exam.getCode());
			jpa.remove(examToRemove);
			
			jpa.commitTransaction();	
		} catch (OHException e) {
			result = false;
			
			// DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return result;	
	}

	/**
	 * Delete an {@link ExamRow}.
	 * @param examRow - the {@link ExamRow} to delete
	 * @return <code>true</code> if the {@link ExamRow} has been deleted, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean deleteExamRow(
			ExamRow examRow) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		try {
			jpa.beginTransaction();	
			jpa.remove(examRow);
	    	jpa.commitTransaction();
		} catch (OHException e) {
			// DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}    	
		return result;	
	}

	/**
	 * This function controls the presence of a record with the same key as in
	 * the parameter; Returns false if the query finds no record, else returns
	 * true
	 * 
	 * @param the {@link Exam}
	 * @return <code>true</code> if the Exam code has already been used, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean isKeyPresent(
			Exam exam) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = false;
		
		try {
			jpa.beginTransaction();	
			Exam foundExam = (Exam)jpa.find(Exam.class, exam.getCode());
			if (foundExam != null)
			{
				result = true;
			}
	    	jpa.commitTransaction();
		} catch (OHException e) {
			// DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return result;	
	}
	
	/**
	 * Sanitize the given {@link String} value. 
	 * This method is maintained only for backward compatibility.
	 * @param value the value to sanitize.
	 * @return the sanitized value or <code>null</code> if the passed value is <code>null</code>.
	 */
	protected String sanitize(
			String value)
	{
		String result = null;
		
		
		if (value != null) 
		{
			result = value.trim().replaceAll("'", "''");
		}
		
		return result;
	}
}
