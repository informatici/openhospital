package org.isf.operation.service;

/*----------------------------------------------------------
 * modification history
 * ====================
 * 13/02/09 - Alex - modified query for ordering resultset
 *                   by description only
 * 13/02/09 - Alex - added Major/Minor control
 -----------------------------------------------------------*/

import java.util.ArrayList;

import org.isf.operation.model.Operation;
import org.isf.opetype.model.OperationType;
import org.isf.utils.db.TranslateOHException;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class offers the io operations for recovering and managing
 * operations records from the database
 * 
 * @author Rick, Vero, pupo
 */
@Service
@Transactional(rollbackFor=OHException.class)
@TranslateOHException
public class OperationIoOperations {

	@Autowired
	private OperationIoOperationRepository repository;
	
	/**
	 * return the {@link Operation}s whose type matches specified string
	 * 
	 * @param typeDescription - a type description
	 * @return the list of {@link Operation}s. It could be <code>empty</code> or <code>null</code>.
	 * @throws OHException 
	 */
	public ArrayList<Operation> getOperation(
			String typeDescription) throws OHException 
	{
    	ArrayList<Operation> operations = null;

    	
		if (typeDescription == null) 
		{
			operations = repository.findAllWithoutDescription();
		}
		else
		{
			operations = repository.findAllByDescription(typeDescription);
		}	

		return operations;
	}
        
        public Operation findByCode(String code) throws OHException{
            return repository.findByCode(code);
        }
	
	/**
	 * insert an {@link Operation} in the DBs
	 * 
	 * @param operation - the {@link Operation} to insert
	 * @return <code>true</code> if the operation has been inserted, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean newOperation(
			Operation operation) throws OHException
	{
		boolean result = true;
	

		Operation savedOperation = repository.save(operation);
		result = (savedOperation != null);
    	
		return result;
	}
	
	/** 
	 * updates an {@link Operation} in the DB
	 * 
	 * @param operation - the {@link Operation} to update
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean updateOperation(
			Operation operation) throws OHException
	{
		boolean result = true;
	
		Operation savedOperation = repository.save(operation);
		result = (savedOperation != null);
    	
		return result;
	}
	
	/** 
	 * Delete a {@link Operation} in the DB
	 * @param operation - the {@link Operation} to delete
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean deleteOperation(
			Operation operation) throws OHException
	{
		boolean result = true;
	
		
		repository.delete(operation);
    	
		return result;
	}
	
	/**
	 * checks if an {@link Operation} code has already been used
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
	
	/**
	 * checks if an {@link Operation} description has already been used within the specified {@link OperationType} 
	 * 
	 * @param description - the {@link Operation} description
	 * @param typeCode - the {@link OperationType} code
	 * @return <code>true</code> if the description is already in use, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean isDescriptionPresent(
			String description, 
			String typeCode) throws OHException
	{
		Operation foundOperation = repository.findOneByDescriptionAndType(description, typeCode);
		boolean present = false;
				
			
		if (foundOperation != null && foundOperation.getDescription().compareTo(description) == 0)
		{
			present = true;
		}
		
		return present;
	}
}

