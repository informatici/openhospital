package org.isf.pregtreattype.service;

import java.util.ArrayList;

import org.isf.pregtreattype.model.PregnantTreatmentType;
import org.isf.utils.db.TranslateOHException;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor=OHException.class)
@TranslateOHException
public class PregnantTreatmentTypeIoOperation {

	@Autowired
	private PregnantTreatmentTypeIoOperationRepository repository;
	
	
	/**
	 * return the list of {@link PregnantTreatmentType}s
	 * 
	 * @return the list of {@link PregnantTreatmentType}s
	 * @throws OHException 
	 */
	public ArrayList<PregnantTreatmentType> getPregnantTreatmentType() throws OHException 
	{
		return new ArrayList<PregnantTreatmentType>(repository.findAllByOrderByDescriptionAsc()); 
	}
	
	/**
	 * insert a {@link PregnantTreatmentType} in the DB
	 * 
	 * @param pregnantTreatmentType - the {@link PregnantTreatmentType} to insert
	 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean newPregnantTreatmentType(
			PregnantTreatmentType pregnantTreatmentType) throws OHException 
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
	 * @throws OHException 
	 */
	public boolean updatePregnantTreatmentType(
			PregnantTreatmentType pregnantTreatmentType) throws OHException 
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
	 * @throws OHException 
	 */
	public boolean deletePregnantTreatmentType(
			PregnantTreatmentType pregnantTreatmentType) throws OHException 
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
	 * @throws OHException 
	 */
	public boolean isCodePresent(
			String code) throws OHException
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;
	}
}
