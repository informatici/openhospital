package org.isf.ward.service;

import java.util.ArrayList;

import org.isf.admission.model.Admission;
import org.isf.admission.service.AdmissionIoOperationRepository;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHServiceException;
import org.isf.ward.model.Ward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class offers the io operations for recovering and managing
 * ward records from the database
 * 
 * @author Rick
 * 
 */

@Service
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
public class WardIoOperations {

	@Autowired
	private WardIoOperationRepository repository;
	@Autowired
	private AdmissionIoOperationRepository admissionRepository;
	
	/**
	 * Retrieves the number of patients currently admitted in the {@link Ward}
	 * @param ward - the ward
	 * @return the number of patients currently admitted
	 * @throws OHServiceException
	 */
	public int getCurrentOccupation(
			Ward ward) throws OHServiceException 
	{		
		ArrayList<Admission> admissions = new ArrayList<Admission>(admissionRepository.findAllWhereWard(ward.getCode()));

		return admissions.size();
	}
	
	/**
	 * Retrieves all stored {@link Ward}s with flag maternity equals <code>false</code>.
	 * @return the retrieved wards.
	 * @throws OHServiceException if an error occurs retrieving the diseases.
	 */
	public ArrayList<Ward> getWardsNoMaternity() throws OHServiceException 
	{		
		ArrayList<Ward> wards = new ArrayList<Ward>(repository.findAllWhereWardIsM());

		return wards;
	}
	
	/**
	 * Retrieves all stored {@link Ward}s with the specified ward ID.
	 * @param wardID - the ward ID, can be <code>null</code>
	 * @return the retrieved wards.
	 * @throws OHServiceException if an error occurs retrieving the wards.
	 */
	public ArrayList<Ward> getWards(
			String wardID) throws OHServiceException 
	{ 
		ArrayList<Ward> wards = null;
		
		
		if (wardID != null && wardID.trim().length() > 0) 
		{
			wards = new ArrayList<Ward>(repository.findAllWhereIdLike(wardID));
		}	
		else
		{
			wards = new ArrayList<Ward>(repository.findAll());
		}

		return wards;
	}
	
	/**
	 * Stores the specified {@link Ward}. 
	 * @param ward the ward to store.
	 * @return <code>true</code> if the ward has been stored, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs storing the ward.
	 */
	public boolean newWard(
			Ward ward) throws OHServiceException 
	{
		boolean result = true;
	

		Ward savedWard = repository.save(ward);
		result = (savedWard != null);
		
		return result;
	}
	
	/**
	 * Updates the specified {@link Ward}.
	 * @param disease the {@link Ward} to update.
	 * @param isConfirmedOverwriteRecord if the user has confirmed he wants to overwrite the record
	 * @return <code>true</code> if the ward has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs during the update.
	 */
	public boolean updateWard(
			Ward ward) throws OHServiceException
	{				
		boolean result = true;
	

		Ward savedWard = repository.save(ward);
		result = (savedWard != null);
		
		return result;
	}
	
	/**
	 * Mark as deleted the specified {@link Ward}.
	 * @param ward the ward to make delete.
	 * @return <code>true</code> if the ward has been marked, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurred during the delete operation.
	 */
	public boolean deleteWard(
			Ward ward) throws OHServiceException
	{
		boolean result = true;
	
		
		repository.delete(ward);
		
		return result;	
	}
	
	/**
	 * Check if the specified code is used by other {@link Ward}s.
	 * @param code the code to check.
	 * @return <code>true</code> if it is already used, <code>false</code> otherwise.
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
	 * Check if the maternity ward exist
	 * @return <code>true</code> if is exist, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs during the check.
	 */
	public boolean isMaternityPresent() throws OHServiceException
	{
		boolean result = false;
		
		
		result = isCodePresent("M");
    	
    	return result;
	}
	
}
