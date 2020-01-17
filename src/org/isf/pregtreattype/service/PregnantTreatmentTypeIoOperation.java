package org.isf.pregtreattype.service;

import java.util.ArrayList;

import org.isf.pregtreattype.model.PregnantTreatmentType;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
public class PregnantTreatmentTypeIoOperation {

	@Autowired
	private PregnantTreatmentTypeIoOperationRepository repository;
	
	
	/**
	 * return the list of {@link PregnantTreatmentType}s
	 * 
	 * @return the list of {@link PregnantTreatmentType}s
	 * @throws OHServiceException 
	 */
	public ArrayList<PregnantTreatmentType> getPregnantTreatmentType() throws OHServiceException 
	{
		return new ArrayList<PregnantTreatmentType>(repository.findAllByOrderByDescriptionAsc()); 
	}
	
	/**
	 * insert a {@link PregnantTreatmentType} in the DB
	 * 
	 * @param pregnantTreatmentType - the {@link PregnantTreatmentType} to insert
	 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean newPregnantTreatmentType(
			PregnantTreatmentType pregnantTreatmentType) throws OHServiceException 
	{
		boolean result = true;
	

		PregnantTreatmentType savedPregnantTreatmentType = repository.save(pregnantTreatmentType);
		result = (savedPregnantTreatmentType != null);
		
		return result;
	}
	
	/**
	 * update a {@link PregnantTreatmentType} in the DB
	 * 
	 * @param pregnantTreatmentType - the {@link PregnantTreatmentType} to update
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean updatePregnantTreatmentType(
			PregnantTreatmentType pregnantTreatmentType) throws OHServiceException 
	{
		boolean result = true;
	

		PregnantTreatmentType savedPregnantTreatmentType = repository.save(pregnantTreatmentType);
		result = (savedPregnantTreatmentType != null);
		
		return result;
	}
	
	/**
	 * delete a {@link PregnantTreatmentType} in the DB
	 * 
	 * @param pregnantTreatmentType - the {@link PregnantTreatmentType} to delete
	 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean deletePregnantTreatmentType(
			PregnantTreatmentType pregnantTreatmentType) throws OHServiceException 
	{
		boolean result = true;
	
		
		repository.delete(pregnantTreatmentType);
		
		return result;
	}
	
	/**
	 * check if the code is already in use
	 * 
	 * @param code - the code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean isCodePresent(
			String code) throws OHServiceException
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;
	}
}
