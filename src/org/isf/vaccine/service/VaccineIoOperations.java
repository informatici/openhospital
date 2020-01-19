package org.isf.vaccine.service;

import java.util.ArrayList;

import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHServiceException;
import org.isf.vaccine.model.Vaccine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class offers the io operations for recovering and managing
 * vaccine records from the database
 *
 * @author Eva
 * 
 * modification history
 * 20/10/2011 - Cla - insert vaccinetype managment
 *
 */

@Service
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
public class VaccineIoOperations {

	@Autowired
	private VaccineIoOperationRepository repository;
	
	/**
	 * returns the list of {@link Vaccine}s based on vaccine type code
	 *
	 * @param vaccineTypeCode - the type code. If <code>null</code> returns all {@link Vaccine}s in the DB
	 * @return the list of {@link Vaccine}s
	 * @throws OHServiceException 
	 */
	public ArrayList<Vaccine> getVaccine(
			String vaccineTypeCode) throws OHServiceException 
	{
		ArrayList<Vaccine> pvaccine = null;

		
		if (vaccineTypeCode != null) {
			pvaccine = new ArrayList<Vaccine>(repository.findAllWhereIdByOrderDescriptionAsc(vaccineTypeCode));
		}
		else
		{
			pvaccine = new ArrayList<Vaccine>(repository.findAllByOrderByDescriptionAsc()); 
		}
		
		return pvaccine;
	}

	/**
	 * inserts a new {@link Vaccine} in the DB
	 *
	 * @param vaccine - the item to insert
	 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean newVaccine(
			Vaccine vaccine) throws OHServiceException
	{
		boolean result = true;
	

		Vaccine savedVaccine = repository.save(vaccine);
		result = (savedVaccine != null);
		
		return result;
	}
	
	/**
	 * updates a {@link Vaccine} in the DB
	 *
	 * @param vaccine - the item to update
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean updateVaccine(
			Vaccine vaccine) throws OHServiceException
	{
		boolean result = true;
	
		Vaccine savedVaccine = repository.save(vaccine);
		result = (savedVaccine != null);
		
		return result;
	}

	/**
	 * deletes a {@link Vaccine} in the DB
	 *
	 * @param vaccine - the item to delete
	 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean deleteVaccine(
			Vaccine vaccine) throws OHServiceException
	{
		boolean result = true;
	
		
		repository.delete(vaccine);
		
		return result;	
	}

	/**
	 * checks if the code is already in use
	 *
	 * @param code - the vaccine code
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


