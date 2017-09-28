package org.isf.vactype.service;

/*------------------------------------------
 * IoOperation - methods to interact with DB
 * -----------------------------------------
 * modification history
 * 19/10/2011 - Cla - version is now 1.0
 *------------------------------------------*/


import java.util.ArrayList;

import org.isf.utils.exception.OHException;
import org.isf.vactype.model.VaccineType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class VacTypeIoOperation {

	@Autowired
	private VaccineTypeIoOperationRepository repository;
	
	/**
	 * returns all {@link VaccineType}s from DB	
	 * 	
	 * @return the list of {@link VaccineType}s
	 * @throws OHException 
	 */
	public ArrayList<VaccineType> getVaccineType() throws OHException 
	{
		return new ArrayList<VaccineType>(repository.findAllByOrderByDescriptionAsc()); 
	}
	
	/**
	 * inserts a new {@link VaccineType} into DB
	 * 
	 * @param vaccineType - the {@link VaccineType} to insert 
	 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean newVaccineType(
			VaccineType vaccineType) throws OHException 
	{
		boolean result = true;
	

		VaccineType savedVaccineType = repository.save(vaccineType);
		result = (savedVaccineType != null);
		
		return result;
	}
	
	/**
	 * updates a {@link VaccineType} in the DB
	 *
	 * @param vaccineType - the item to update
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean updateVaccineType(
			VaccineType vaccineType) throws OHException 
	{
		boolean result = true;
	

		VaccineType savedVaccineType = repository.save(vaccineType);
		result = (savedVaccineType != null);
		
		return result;
	}
	
	/**
	 * deletes a {@link VaccineType} in the DB
	 *
	 * @param vaccineType - the item to delete
	 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean deleteVaccineType(
			VaccineType vaccineType) throws OHException 
	{
		boolean result = true;
	
		
		repository.delete(vaccineType);
		
		return result;
	}
	
	
	/**
	 * checks if the code is already in use
	 *
	 * @param code - the {@link VaccineType} code
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
