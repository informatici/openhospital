package org.isf.admtype.service;

import java.util.ArrayList;

import org.isf.admtype.model.AdmissionType;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Persistence class for admtype module.
 */
@Service
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
public class AdmissionTypeIoOperation 
{

	@Autowired
	private AdmissionTypeIoOperationRepository repository;
	
	/**
	 * Returns all the available {@link AdmissionType}s.
	 * @return a list of admission types.
	 * @throws OHServiceException if an error occurs.
	 */
	public ArrayList<AdmissionType> getAdmissionType() throws OHServiceException 
	{
		return new ArrayList<AdmissionType>(repository.findAllByOrderByDescriptionAsc());
	}

	/**
	 * Updates the specified {@link AdmissionType}.
	 * @param admissionType the admission type to update.
	 * @return <code>true</code> if the admission type has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs during the update.
	 */
	public boolean updateAdmissionType(
			AdmissionType admissionType) throws OHServiceException 
	{
		boolean result = true;
	
		
		AdmissionType savedAdmissionType = repository.save(admissionType);
		result = (savedAdmissionType != null);
		
		return result;
	}

	/**
	 * Stores a new {@link AdmissionType}.
	 * @param admissionType the admission type to store.
	 * @return <code>true</code> if the admission type has been stored, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs during the storing operation.
	 */
	public boolean newAdmissionType(
			AdmissionType admissionType) throws OHServiceException 
	{
		boolean result = true;
	
		
		AdmissionType savedAdmissionType = repository.save(admissionType);
		result = (savedAdmissionType != null);
		
		return result;
	}

	/**
	 * Deletes the specified {@link AdmissionType}.
	 * @param admissionType the admission type to delete.
	 * @return <code>true</code> if the admission type has been deleted, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs during the delete operation.
	 */
	public boolean deleteAdmissionType(
			AdmissionType admissionType) throws OHServiceException 
	{
		boolean result = true;
	
		
		repository.delete(admissionType);
		
		return result;	
	}

	/**
	 * Checks if the specified Code is already used by others {@link AdmissionType}s.
	 * @param code the admission type code to check.
	 * @return <code>true</code> if the code is already used, <code>false</code> otherwise.
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
