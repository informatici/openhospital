package org.isf.medstockmovtype.service;

import java.util.ArrayList;

import org.isf.medstockmovtype.model.MovementType;

import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Persistence class for the medstockmovtype module.
 *
 */
@Component
@Transactional
public class MedicalStockMovementTypeIoOperation {

	@Autowired
	private MedicalStockMovementTypeIoOperationRepository repository;
	
	/**
	 * Retrieves all the stored {@link MovementType}.
	 * @return all the stored {@link MovementType}s.
	 * @throws OHException if an error occurs retrieving the medical stock movement types.
	 */
	public ArrayList<MovementType> getMedicaldsrstockmovType() throws OHException 
	{
		return new ArrayList<MovementType>(repository.findAllByOrderByDescriptionAsc()); 	
	}

	/**
	 * Updates the specified {@link MovementType}.
	 * @param medicaldsrstockmovType the medical stock movement type to update.
	 * @return <code>true</code> if the specified stock movement type has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateMedicaldsrstockmovType(
			MovementType medicaldsrstockmovType) throws OHException 
	{
		boolean result = true;
	

		MovementType savedMedicaldsrstockmovType = repository.save(medicaldsrstockmovType);
		result = (savedMedicaldsrstockmovType != null);
		
		return result;
	}

	/**
	 * Stores the specified {@link MovementType}.
	 * @param medicaldsrstockmovType the medical stock movement type to store.
	 * @return <code>true</code> if the medical movement type has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the store operation.
	 */
	public boolean newMedicaldsrstockmovType(
			MovementType medicaldsrstockmovType) throws OHException 
	{
		boolean result = true;
	

		MovementType savedMedicaldsrstockmovType = repository.save(medicaldsrstockmovType);
		result = (savedMedicaldsrstockmovType != null);
		
		return result;
	}

	/**
	 * Deletes the specified {@link MovementType}.
	 * @param medicaldsrstockmovType the medical stock movement type to delete.
	 * @return <code>true</code> if the medical stock movement type has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the delete operation.
	 */
	public boolean deleteMedicaldsrstockmovType(
			MovementType medicaldsrstockmovType) throws OHException 
	{
		boolean result = true;
	
		
		repository.delete(medicaldsrstockmovType);
		
		return result;	
	}

	/**
	 * Checks if the specified medical stock movement type is already used.
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
