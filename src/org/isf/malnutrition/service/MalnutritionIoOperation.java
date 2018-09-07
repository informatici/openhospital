package org.isf.malnutrition.service;

import java.util.ArrayList;

import org.isf.generaldata.MessageBundle;
import org.isf.malnutrition.model.Malnutrition;
import org.isf.utils.db.TranslateOHException;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Persistence class for the malnutrition module.
 */
@Component
@Transactional(rollbackFor=OHException.class)
@TranslateOHException
public class MalnutritionIoOperation {

	@Autowired
	private MalnutritionIoOperationRepository repository;
	
	/**
	 * Returns all the available {@link Malnutrition} for the specified admission id.
	 * @param admissionId the admission id
	 * @return the retrieved malnutrition.
	 * @throws OHException if an error occurs retrieving the malnutrition list.
	 */
    public ArrayList<Malnutrition> getMalnutritions(
			String admissionId) throws OHException
	{
		ArrayList<Malnutrition> malnutritions = (ArrayList<Malnutrition>) repository.findAllWhereAdmissionByOrderDate(admissionId);

		
		return malnutritions;
	}

	/**
	 * Stores a new {@link Malnutrition}. The malnutrition object is updated with the generated id.
	 * @param malnutrition the malnutrition to store.
	 * @return <code>true</code> if the malnutrition has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs storing the malnutrition.
	 */
	public boolean newMalnutrition(
			Malnutrition malnutrition) throws OHException
	{
		boolean result = true;
	

		Malnutrition savedMalnutrition = repository.save(malnutrition);
		result = (savedMalnutrition != null);
		
		return result;
	}

	/**
	 * Retrieves the lock value for the specified {@link Malnutrition} code.
	 * @param malnutritionCode the {@link Malnutrition} code.
	 * @return the retrieved code or -1 if no malnutrition has been found. 
	 * @throws OHException if an error occurs retrieving the code.
	 */
	public int getMalnutritionLock(
			int malnutritionCode) throws OHException
	{
		int lock = 0;		
		Malnutrition malnutrition = repository.findOne(malnutritionCode);
		
		
		if (malnutrition != null)
		{
			lock = malnutrition.getLock();
		}
				
		return lock;
	}

	/**
	 * Updates the specified {@link Malnutrition}.
	 * @param malnutrition the malnutrition to update.
	 * @return <code>true</code> if the malnutrition has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs updating the malnutrition.
	 */
	public boolean updateMalnutrition(
			Malnutrition malnutrition) throws OHException
	{
		boolean result = true;
	

		Malnutrition savedMalnutrition = repository.save(malnutrition);
		result = (savedMalnutrition != null);
		
		return result;
	}
	
	/**
	 * returns the last {@link Malnutrition} entry for specified patient ID
	 * @param patientID - the patient ID
	 * @return the last {@link Malnutrition} for specified patient ID. <code>null</code> if none.
	 * @throws OHException
	 */
	public Malnutrition getLastMalnutrition(
			int patientID) throws OHException 
	{
		ArrayList<Malnutrition> malnutritions = (ArrayList<Malnutrition>) repository.findAllWhereAdmissionByOrderDateLimit1(patientID);
		Malnutrition theMalnutrition = null;
				
		
		try {
			theMalnutrition = malnutritions.get(0);
		} catch (Exception e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}

		return theMalnutrition;
	}

	/**
	 * Deletes the specified {@link Malnutrition}.
	 * @param malnutrition the malnutrition to delete.
	 * @return <code>true</code> if the malnutrition has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs deleting the specified malnutrition.
	 */
	public boolean deleteMalnutrition(
			Malnutrition malnutrition) throws OHException 
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
	 * @throws OHException 
	 */
	public boolean isCodePresent(
			Integer code) throws OHException
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;	
	}
}
