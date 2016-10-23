package org.isf.vactype.service;

/*------------------------------------------
 * IoOperation - methods to interact with DB
 * -----------------------------------------
 * modification history
 * 19/10/2011 - Cla - version is now 1.0
 *------------------------------------------*/


import java.util.ArrayList;
import java.util.List;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.vactype.model.VaccineType;
import org.springframework.stereotype.Component;

@Component
public class VacTypeIoOperation {
	
	/**
	 * returns all {@link VaccineType}s from DB	
	 * 	
	 * @return the list of {@link VaccineType}s
	 * @throws OHException 
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<VaccineType> getVaccineType() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<VaccineType> pvaccineType = null;
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM VACCINETYPE ORDER BY VACT_ID_A";
		jpa.createQuery(query, VaccineType.class, false);
		List<VaccineType> vaccineTypeList = (List<VaccineType>)jpa.getList();
		pvaccineType = new ArrayList<VaccineType>(vaccineTypeList);			
		
		jpa.commitTransaction();

		return pvaccineType;
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.merge(vaccineType);
    	jpa.commitTransaction();
    	
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.persist(vaccineType);
    	jpa.commitTransaction();
    	
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.remove(vaccineType);
    	jpa.commitTransaction();
    	
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		VaccineType vaccineType;
		boolean result = true;
		
		
		jpa.beginTransaction();	
		vaccineType = (VaccineType)jpa.find(VaccineType.class, code);
		if (vaccineType != null)
		{
			result = true;
		}
    	jpa.commitTransaction();
    	
		return result;	
	}
}
