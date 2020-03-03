package org.isf.malnutrition.service;

import org.isf.malnutrition.model.Malnutrition;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Persistence class for the malnutrition module.
 */
@Service
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
public class MalnutritionIoOperation {

	@Autowired
	private MalnutritionIoOperationRepository repository;
	
	/**
	 * Returns all the available {@link Malnutrition} for the specified admission id.
	 * @param admissionId the admission id
	 * @return the retrieved malnutrition.
	 * @throws OHServiceException if an error occurs retrieving the malnutrition list.
	 */
    public ArrayList<Malnutrition> getMalnutritions(
			String admissionId) throws OHServiceException
	{
		ArrayList<Malnutrition> malnutritions = (ArrayList<Malnutrition>) repository.findAllWhereAdmissionByOrderDate(admissionId);

		
		return malnutritions;
	}

	/**
	 * Stores a new {@link Malnutrition}. The malnutrition object is updated with the generated id.
	 * @param malnutrition the malnutrition to store.
	 * @return <code>true</code> if the malnutrition has been stored, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs storing the malnutrition.
	 */
	public boolean newMalnutrition(
			Malnutrition malnutrition) throws OHServiceException
	{
		boolean result = true;
	

		Malnutrition savedMalnutrition = repository.save(malnutrition);
		result = (savedMalnutrition != null);
		
		return result;
	}

	/**
	 * Updates the specified {@link Malnutrition}.
	 * @param malnutrition the malnutrition to update.
	 * @return the updated {@link Malnutrition}
	 * @throws OHServiceException if an error occurs updating the malnutrition.
	 */
	public Malnutrition updateMalnutrition(
			Malnutrition malnutrition) throws OHServiceException
	{

		Malnutrition savedMalnutrition = repository.save(malnutrition);
				
		return savedMalnutrition;
	}
	
	/**
	 * returns the last {@link Malnutrition} entry for specified patient ID
	 * @param patientID - the patient ID
	 * @return the last {@link Malnutrition} for specified patient ID. <code>null</code> if none.
	 * @throws OHServiceException
	 */
	public Malnutrition getLastMalnutrition(
			int patientID) throws OHServiceException 
	{
		ArrayList<Malnutrition> malnutritions = (ArrayList<Malnutrition>) repository.findAllWhereAdmissionByOrderDateDescLimit1(patientID);
		
		Malnutrition lastMalnutrition = malnutritions.get(0);

		return lastMalnutrition;
	}

	/**
	 * Deletes the specified {@link Malnutrition}.
	 * @param malnutrition the malnutrition to delete.
	 * @return <code>true</code> if the malnutrition has been deleted, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs deleting the specified malnutrition.
	 */
	public boolean deleteMalnutrition(
			Malnutrition malnutrition) throws OHServiceException 
	{
		boolean result = true;
	
		
		repository.delete(malnutrition);
		
		return result;
	}

	/**
	 * checks if the code is already in use
	 *
	 * @param code - the malnutrition code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean isCodePresent(
			Integer code) throws OHServiceException
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;	
	}
}
