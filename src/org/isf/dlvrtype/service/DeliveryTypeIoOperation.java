package org.isf.dlvrtype.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.dlvrtype.model.DeliveryType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

/**
 * The persistence class for the DeliveryType module.
 */
@Component
public class DeliveryTypeIoOperation {

	/**
	 * Returns all stored {@link DeliveryType}s.
	 * @return all stored delivery types.
	 * @throws OHException if an error occurs retrieving the delivery types. 
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<DeliveryType> getDeliveryType() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<DeliveryType> deliveryTypes = null;
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM DELIVERYTYPE ORDER BY DLT_DESC";
		jpa.createQuery(query, DeliveryType.class, false);
		List<DeliveryType> deliveryList = (List<DeliveryType>)jpa.getList();
		deliveryTypes = new ArrayList<DeliveryType>(deliveryList);			
		
		jpa.commitTransaction();

		return deliveryTypes;
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.merge(deliveryType);
    	jpa.commitTransaction();
    	
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.persist(deliveryType);
    	jpa.commitTransaction();
    	
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.remove(deliveryType);
    	jpa.commitTransaction();
    	
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		DeliveryType deliveryType;
		boolean result = true;
		
		
		jpa.beginTransaction();	
		deliveryType = (DeliveryType)jpa.find(DeliveryType.class, code);
		if (deliveryType != null)
		{
			result = true;
		}
    	jpa.commitTransaction();
    	
		return result;	
	}
}
