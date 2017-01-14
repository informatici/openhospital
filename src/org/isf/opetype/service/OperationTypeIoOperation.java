package org.isf.opetype.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.opetype.model.OperationType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

@Component
public class OperationTypeIoOperation {

	/**
	 * return the list of {@link OperationType}s
	 * 
	 * @return the list of {@link OperationType}s. It could be <code>empty</code> or <code>null</code>.
	 * @throws OHException 
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<OperationType> getOperationType() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<OperationType> operationTypes = null;
				
		
		jpa.beginTransaction();
		
		String query = "SELECT OCL_ID_A, OCL_DESC FROM OPERATIONTYPE ORDER BY OCL_DESC";
		jpa.createQuery(query, OperationType.class, false);
		List<OperationType> operationTypeList = (List<OperationType>)jpa.getList();
		operationTypes = new ArrayList<OperationType>(operationTypeList);	
		
		jpa.commitTransaction();

		return operationTypes;
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.persist(operationType);
    	jpa.commitTransaction();
    	
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.merge(operationType);
    	jpa.commitTransaction();
    	
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		OperationType objToRemove = (OperationType) jpa.find(OperationType.class, operationType.getCode());
		jpa.remove(objToRemove);
    	jpa.commitTransaction();
    	
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		OperationType operationType;
		boolean result = false;
		
		
		jpa.beginTransaction();	
		operationType = (OperationType)jpa.find(OperationType.class, code);
		if (operationType != null)
		{
			result = true;
		}
    	jpa.commitTransaction();
    	
		return result;
	}
}
