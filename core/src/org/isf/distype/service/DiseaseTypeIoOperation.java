package org.isf.distype.service;

import java.util.ArrayList;

import org.isf.distype.model.DiseaseType;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Persistence class for the DisType module.
 */
@Service
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
public class DiseaseTypeIoOperation {

	@Autowired
	private DiseaseTypeIoOperationRepository repository;
	
	/**
	 * Returns all the stored {@link DiseaseType}s.
	 * @return a list of disease type.
	 * @throws OHServiceException if an error occurs retrieving the diseases list.
	 */
	public ArrayList<DiseaseType> getDiseaseTypes() throws OHServiceException 
	{
		return new ArrayList<DiseaseType>(repository.findAllByOrderByDescriptionAsc());
	}

	/**
	 * Updates the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to update.
	 * @return <code>true</code> if the disease type has been updated, false otherwise.
	 * @throws OHServiceException if an error occurs during the update operation.
	 */
	public boolean updateDiseaseType(
			DiseaseType diseaseType) throws OHServiceException 
	{
		boolean result = true;
	

		DiseaseType savedDiseaseType = repository.save(diseaseType);
		result = (savedDiseaseType != null);
		
		return result;
	}

	/**
	 * Store the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to store.
	 * @return <code>true</code> if the {@link DiseaseType} has been stored, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs during the store operation.
	 */
	public boolean newDiseaseType(
			DiseaseType diseaseType) throws OHServiceException 
	{
		boolean result = true;
	
		
		DiseaseType savedDiseaseType = repository.save(diseaseType);
		result = (savedDiseaseType != null);
		
		return result;
	}

	/**
	 * Deletes the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to remove.
	 * @return <code>true</code> if the disease has been removed, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs during the delete procedure.
	 */
	public boolean deleteDiseaseType(
			DiseaseType diseaseType) throws OHServiceException 
	{
		boolean result = true;
	
		
		repository.delete(diseaseType);
		
		return result;
	}

	/**
	 * Checks if the specified code is already used by any {@link DiseaseType}.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, false otherwise.
	 * @throws OHServiceException if an error occurs during the check.
	 */
	public boolean isCodePresent(
			String code) throws OHServiceException
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;
	}
}
