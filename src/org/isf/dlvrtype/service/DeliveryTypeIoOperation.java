package org.isf.dlvrtype.service;

import java.util.ArrayList;

import org.isf.dlvrtype.model.DeliveryType;
import org.isf.utils.db.TranslateOHException;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * The persistence class for the DeliveryType module.
 */
@Component
@Transactional(rollbackFor=OHException.class)
@TranslateOHException
public class DeliveryTypeIoOperation {
	
	@Autowired
	private DeliveryTypeIoOperationRepository repository;
	    
	/**
	 * Returns all stored {@link DeliveryType}s.
	 * @return all stored delivery types.
	 * @throws OHException if an error occurs retrieving the delivery types. 
	 */
	public ArrayList<DeliveryType> getDeliveryType() throws OHException 
	{
		return new ArrayList<DeliveryType>(repository.findAll());
	}

	/**
	 * Updates the specified {@link DeliveryType}.
	 * @param deliveryType the delivery type to update.
	 * @return <code>true</code> if the delivery type has been update.
	 * @throws OHException if an error occurs during the update operation.
	 */
	public boolean updateDeliveryType(
			DeliveryType deliveryType) throws OHException 
	{
		boolean result = true;
	

		DeliveryType savedDeliveryType = repository.save(deliveryType);
		result = (savedDeliveryType != null);
		
		return result;
	}

	/**
	 * Stores the specified {@link DeliveryType}.
	 * @param deliveryType the delivery type to store.
	 * @return <code>true</code> if the delivery type has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurred during the store operation.
	 */
	public boolean newDeliveryType(
			DeliveryType deliveryType) throws OHException 
	{
		boolean result = true;
	

		DeliveryType savedDeliveryType = repository.save(deliveryType);
		result = (savedDeliveryType != null);
		
		return result;
	}

	/**
	 * Delete the specified {@link DeliveryType}.
	 * @param deliveryType the delivery type to delete.
	 * @return <code>true</code> if the delivery type has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurred during the delete operation.
	 */
	public boolean deleteDeliveryType(
			DeliveryType deliveryType) throws OHException 
	{
		boolean result = true;
	
		
		repository.delete(deliveryType);
		
		return result;
	}

	/**
	 * Checks if the specified code is already used by others {@link DeliveryType}s.
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
