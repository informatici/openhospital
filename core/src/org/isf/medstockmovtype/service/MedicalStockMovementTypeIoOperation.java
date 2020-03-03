package org.isf.medstockmovtype.service;

import org.isf.medstockmovtype.model.MovementType;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Persistence class for the medstockmovtype module.
 *
 */
@Service
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
public class MedicalStockMovementTypeIoOperation {

	@Autowired
	private MedicalStockMovementTypeIoOperationRepository repository;
	
	/**
	 * Retrieves all the stored {@link MovementType}.
	 * @return all the stored {@link MovementType}s.
	 * @throws OHServiceException if an error occurs retrieving the medical stock movement types.
	 */
	public ArrayList<MovementType> getMedicaldsrstockmovType() throws OHServiceException 
	{
		return new ArrayList<MovementType>(repository.findAllByOrderByDescriptionAsc()); 	
	}

	/**
	 * Updates the specified {@link MovementType}.
	 * @param medicaldsrstockmovType the medical stock movement type to update.
	 * @return <code>true</code> if the specified stock movement type has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs during the update.
	 */
	public boolean updateMedicaldsrstockmovType(
			MovementType medicaldsrstockmovType) throws OHServiceException 
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
	 * @throws OHServiceException if an error occurs during the store operation.
	 */
	public boolean newMedicaldsrstockmovType(
			MovementType medicaldsrstockmovType) throws OHServiceException 
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
	 * @throws OHServiceException if an error occurs during the delete operation.
	 */
	public boolean deleteMedicaldsrstockmovType(
			MovementType medicaldsrstockmovType) throws OHServiceException 
	{
		boolean result = true;
	
		
		repository.delete(medicaldsrstockmovType);
		
		return result;	
	}

	/**
	 * Checks if the specified medical stock movement type is already used.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs during the check.
	 */
	public boolean isCodePresent(
			String code) throws OHServiceException 
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;
	}
        
        /**
	 * Get an existing movementType.
	 * @param code the code to check.
	 * @return MovementType object whose code is provided as parameter.
	 */
	public MovementType getMovementType(String code) {
            List<MovementType> results = repository.findAllByCode(code);
            if(!results.isEmpty()) {
                return results.get(0);
            }
            return null;
	}
}
