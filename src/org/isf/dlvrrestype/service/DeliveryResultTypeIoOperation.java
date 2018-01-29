package org.isf.dlvrrestype.service;

import java.util.ArrayList;

import org.isf.dlvrrestype.model.DeliveryResultType;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Persistence class for DeliveryResultType module.
 */
@Component
@Transactional
public class DeliveryResultTypeIoOperation {
	
	@Autowired
	private DeliveryResultIoOperationRepository repository;

	/**
	 * Returns all stored {@link DeliveryResultType}s.
	 * @return the stored {@link DeliveryResultType}s.
	 * @throws OHException if an error occurs retrieving the stored delivery result types.
	 */
	public ArrayList<DeliveryResultType> getDeliveryResultType() throws OHException 
	{
		return new ArrayList<DeliveryResultType>(repository.findAllByOrderByDescriptionAsc()); 		
	}

	/**
	 * Updates the specified {@link DeliveryResultType}.
	 * @param deliveryresultType the delivery result type to update.
	 * @return <code>true</code> if the delivery result type has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateDeliveryResultType(
			DeliveryResultType deliveryResultType) throws OHException 
	{
		boolean result = true;
	

		DeliveryResultType savedDeliveryResultType = repository.save(deliveryResultType);
		result = (savedDeliveryResultType != null);
		
		return result;
	}

	/**
	 * Stores the specified {@link DeliveryResultType}.
	 * @param deliveryresultType the delivery result type to store.
	 * @return <code>true</code> if the delivery result type has been stored. 
	 * @throws OHException if an error occurs during the store operation.
	 */
	public boolean newDeliveryResultType(
			DeliveryResultType deliveryResultType) throws OHException 
	{
		boolean result = true;
	

		DeliveryResultType savedDeliveryResultType = repository.save(deliveryResultType);
		result = (savedDeliveryResultType != null);
		
		return result;
	}

	/**
	 * Deletes the specified {@link DeliveryResultType}.
	 * @param deliveryresultType the delivery result type to delete.
	 * @return <code>true</code> if the delivery result type has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the delete operation.
	 */
	public boolean deleteDeliveryResultType(
			DeliveryResultType deliveryResultType) throws OHException 
	{
		boolean result = true;
	
		
		repository.delete(deliveryResultType);
		
		return result;
	}

	/**
	 * Checks if the specified code is already used by others {@link DeliveryResultType}s.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isCodePresent(
			String code) throws OHException 
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;
	}
}
