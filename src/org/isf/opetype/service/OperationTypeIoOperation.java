package org.isf.opetype.service;

import java.util.ArrayList;

import org.isf.opetype.model.OperationType;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OperationTypeIoOperation {

	@Autowired
	private OperationTypeIoOperationRepository repository;
	
	/**
	 * return the list of {@link OperationType}s
	 * 
	 * @return the list of {@link OperationType}s. It could be <code>empty</code> or <code>null</code>.
	 * @throws OHException 
	 */
	public ArrayList<OperationType> getOperationType() throws OHException 
	{
		return new ArrayList<OperationType>(repository.findAllOrderByDescriptionAsc()); 
	}
	
	/**
	 * insert an {@link OperationType} in the DB
	 * 
	 * @param operationType - the {@link OperationType} to insert
	 * @return <code>true</code> if the {@link OperationType} has been inserted, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean newOperationType(
			OperationType operationType) throws OHException 
	{
		boolean result = true;
	

		OperationType savedOperationType = repository.save(operationType);
		result = (savedOperationType != null);
		
		return result;
	}
	
	/**
	 * update an {@link OperationType}
	 * 
	 * @param operationType - the {@link OperationType} to update
	 * @return <code>true</code> if the {@link OperationType} has been updated, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean updateOperationType(
			OperationType operationType) throws OHException 
	{
		boolean result = true;
	

		OperationType savedOperationType = repository.save(operationType);
		result = (savedOperationType != null);
		
		return result;
	}
	
	/**
	 * delete an {@link OperationType}
	 * 
	 * @param operationType - the {@link OperationType} to delete
	 * @return <code>true</code> if the {@link OperationType} has been delete, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean deleteOperationType(
			OperationType operationType) throws OHException 
	{
		boolean result = true;
	
		
		repository.delete(operationType);
		
		return result;
	}
	
	/**
	 * checks if an {@link OperationType} code has already been used
	 * @param code - the code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise.
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
