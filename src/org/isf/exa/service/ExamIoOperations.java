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

import org.isf.exa.model.Exam;
import org.isf.exa.model.ExamRow;
import org.isf.exatype.model.ExamType;
import org.isf.exatype.service.ExamTypeIoOperationRepository;
import org.isf.utils.db.TranslateOHException;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor=OHException.class)
@TranslateOHException
public class ExamIoOperations {

	@Autowired
	private ExamIoOperationRepository repository;
	
	@Autowired
	private ExamRowIoOperationRepository rowRepository;
	
	@Autowired
	private ExamTypeIoOperationRepository typeRepository;
	
	/**
	 * Returns a list of {@link ExamRow}s that matches passed exam code and description
	 * @param aExamCode - the exam code
	 * @param aDescription - the exam description
	 * @return the list of {@link ExamRow}s
	 * @throws OHException
	 */
	public ArrayList<ExamRow> getExamRow(
			String aExamCode, 
			String aDescription) throws OHException 
	{
    	ArrayList<ExamRow> examrows = null;
    	
    	
		if (aExamCode != null) 
		{
			if (aDescription != null) 
			{
				examrows = (ArrayList<ExamRow>) rowRepository.findAllWhereIdAndDescriptionByOrderIdAndDescriptionAsc(aExamCode, aDescription); 	
			}
			else
			{
				examrows = (ArrayList<ExamRow>) rowRepository.findAllWhereIdByOrderIdAndDescriptionAsc(aExamCode); 	
			}
		}
		else
		{
			examrows = (ArrayList<ExamRow>) rowRepository.findAllByOrderIdAndDescriptionAsc(); 	
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
	public ArrayList<Exam> getExamsByDesc(
			String description) throws OHException 
	{ 
		ArrayList<String> examIds = null;
		ArrayList<Exam> exams = new ArrayList<Exam>();
				
		
		if (description != null) 
		{
			examIds = (ArrayList<String>) repository.findAllWhereDescriptionByOrderDescriptionAsc(description);	
		}
		else
		{
			examIds = (ArrayList<String>) repository.findAllByOrderDescriptionAsc();			
		}		
		
		for (int i=0; i<examIds.size(); i++)
		{
			String code = examIds.get(i);
			Exam exam = repository.findOne(code);
			
			
			exams.add(i, exam);
		}
	
		return exams;
	}

	/**
	 * Returns the list of {@link ExamType}s
	 * @return the list of {@link ExamType}s
	 * @throws OHException
	 */
	public ArrayList<ExamType> getExamType() throws OHException 
	{
		ArrayList<ExamType> examTypes = (ArrayList<ExamType>) typeRepository.findAllByOrderByDescriptionAsc();
				
	
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
		boolean result = true;
	

		Exam savedExam = repository.save(exam);
		result = (savedExam != null);
		
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
		boolean result = true;
	

		ExamRow savedExamRow = rowRepository.save(examRow);
		result = (savedExamRow != null);
		
		return result;
	}

	/**
	 * Update an already existing {@link Exam}.
	 * @param exam - the {@link Exam} to update
	 * @return <code>true</code> if the {@link Exam} has been updated, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean updateExam(
			Exam exam) throws OHException 
	{
		boolean result = true;
		
		repository.save(exam);
    	
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
		boolean result = true;		
	
		
		rowRepository.deleteWhereCode(exam.getCode());
		repository.delete(exam);
		
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
		boolean result = true;
	
		
		rowRepository.delete(examRow);
		
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
		boolean result = false;
		Exam foundExam = repository.findOne(exam.getCode());
		
		
		if (foundExam != null)
		{
			result = true;
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

	/**
	 * checks if the code is already in use
	 *
	 * @param code - the exam code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean isCodePresent(
			String code) throws OHException
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;	
	}

	/**
	 * checks if the code is already in use
	 *
	 * @param code - the exam row code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean isRowPresent(
			Integer code) throws OHException
	{
		boolean result = true;
	
		
		result = rowRepository.exists(code);
		
		return result;	
	}
}
