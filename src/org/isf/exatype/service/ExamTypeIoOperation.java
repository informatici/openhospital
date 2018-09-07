package org.isf.exatype.service;

import java.util.ArrayList;

import org.isf.exatype.model.ExamType;
import org.isf.utils.db.TranslateOHException;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor=OHException.class)
@TranslateOHException
public class ExamTypeIoOperation {

	@Autowired
	private ExamTypeIoOperationRepository repository;
	
	/**
	 * Return the list of {@link ExamType}s.
	 * @return the list of {@link ExamType}s.
	 * @throws OHException
	 */
	public ArrayList<ExamType> getExamType() throws OHException 
	{
		return new ArrayList<ExamType>(repository.findAllByOrderByDescriptionAsc()); 	
	}
	
	/**
	 * Update an already existing {@link ExamType}.
	 * @param examType - the {@link ExamType} to update
	 * @return <code>true</code> if the examType has been updated, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean updateExamType(
			ExamType examType) throws OHException 
	{
		boolean result = true;
	

		ExamType savedExamType = repository.save(examType);
		result = (savedExamType != null);
		
		return result;
	}
	
	/**
	 * Insert a new {@link ExamType} in the DB.
	 * @param examType - the {@link ExamType} to insert.
	 * @return <code>true</code> if the examType has been inserted, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean newExamType(
			ExamType examType) throws OHException 
	{
		boolean result = true;
	
		
		repository.save(examType);
		
		return result;
	}
	
	/**
	 * Delete the passed {@link ExamType}.
	 * @param examType - the {@link ExamType} to delete.
	 * @return <code>true</code> if the examType has been deleted, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean deleteExamType(
			ExamType examType) throws OHException 
	{
		boolean result = true;
	
		
		repository.delete(examType);
		
		return result;
	}
	
	/**
	 * This function controls the presence of a record with the same code as in
	 * the parameter.
	 * @param code - the code
	 * @return <code>true</code> if the code is present, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean isCodePresent(
			String code) throws OHException
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;
	}
}
