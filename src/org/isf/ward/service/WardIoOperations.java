package org.isf.ward.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;
import org.springframework.stereotype.Component;

/**
 * This class offers the io operations for recovering and managing
 * ward records from the database
 * 
 * @author Rick
 * 
 */
@Component
public class WardIoOperations {

	/**
	 * Retrieves the number of patients currently admitted in the {@link Ward}
	 * @param ward - the ward
	 * @return the number of patients currently admitted
	 * @throws OHException
	 */
	@SuppressWarnings("unchecked")
	public int getCurrentOccupation(
			Ward ward) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Ward> wards = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		try{
			jpa.beginTransaction();

			String query = "SELECT * FROM ADMISSION WHERE ADM_IN = 1 AND ADM_WRD_ID_A = ?";
			jpa.createQuery(query, Ward.class, false);
			params.add(ward.getCode());
			jpa.setParameters(params, false);
			List<Ward> wardList = (List<Ward>)jpa.getList();
			wards = new ArrayList<Ward>(wardList);			

			jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return wards.size();
	}
	
	/**
	 * Retrieves all stored {@link Ward}s with flag maternity equals <code>false</code>.
	 * @return the retrieved wards.
	 * @throws OHException if an error occurs retrieving the diseases.
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<Ward> getWardsNoMaternity() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Ward> wards = null;
				
		try{
			jpa.beginTransaction();

			String query = "SELECT * FROM WARD WHERE WRD_ID_A <> 'M'";
			jpa.createQuery(query, Ward.class, false);
			List<Ward> wardList = (List<Ward>)jpa.getList();
			wards = new ArrayList<Ward>(wardList);			

			jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return wards;
	}
	
	/**
	 * Retrieves all stored {@link Ward}s with the specified ward ID.
	 * @param wardID - the ward ID, can be <code>null</code>
	 * @return the retrieved wards.
	 * @throws OHException if an error occurs retrieving the wards.
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<Ward> getWards(
			String wardID) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Ward> wards = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		try{
			jpa.beginTransaction();

			String query = "SELECT * FROM WARD";

			if (wardID != null && wardID.trim().length() > 0) 
			{
				query = query + " WHERE WRD_ID_A LIKE ?";
			}	
			jpa.createQuery(query, Ward.class, false);
			if (wardID != null && wardID.trim().length() > 0) 
			{
				params.add(wardID);
				jpa.setParameters(params, false);
			}
			List<Ward> wardList = (List<Ward>)jpa.getList();
			wards = new ArrayList<Ward>(wardList);			

			jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return wards;
	}
	
	/**
	 * Stores the specified {@link Ward}. 
	 * @param ward the ward to store.
	 * @return <code>true</code> if the ward has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs storing the ward.
	 */
	public boolean newWard(
			Ward ward) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		try{
			jpa.beginTransaction();	
			jpa.persist(ward);
			jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return result;	
	}
	
	/**
	 * Updates the specified {@link Ward}.
	 * @param disease the {@link Ward} to update.
	 * @param isConfirmedOverwriteRecord if the user has confirmed he wants to overwrite the record
	 * @return <code>true</code> if the ward has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateWard(
			Ward ward) throws OHException
	{				
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		try{
			jpa.beginTransaction();	
			ward.setLock(ward.getLock()+1);
			jpa.merge(ward);
			jpa.commitTransaction();		
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return result;
	}
	
	/**
	 * Mark as deleted the specified {@link Ward}.
	 * @param ward the ward to make delete.
	 * @return <code>true</code> if the ward has been marked, <code>false</code> otherwise.
	 * @throws OHException if an error occurred during the delete operation.
	 */
	public boolean deleteWard(
			Ward ward) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		try{
			Ward wardToRemove = (Ward) jpa.find(Ward.class, ward.getCode());

			jpa.beginTransaction();	
			jpa.remove(wardToRemove);
			jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return result;	
	}
	
	/**
	 * Check if the specified code is used by other {@link Ward}s.
	 * @param code the code to check.
	 * @return <code>true</code> if it is already used, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isCodePresent(
			String code) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		Ward ward;
		boolean result = false;
		
		try{
			jpa.beginTransaction();
			ward = (Ward)jpa.find(Ward.class, code);
			jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		if (ward != null)
		{
			result = true;
		}
    	
    	return result;
	}
	
	
	/**
	 * Check if the maternity ward exist
	 * @return <code>true</code> if is exist, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isMaternityPresent() throws OHException
	{
		boolean result = false;
		
		
		result = isCodePresent("M");
    	
    	return result;
	}
	
	/**
	 * Check if the ward is locked
	 * @return <code>true</code> if the ward is locked <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isLockWard(
			Ward ward) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean isLockWard = false;
		Ward foundWard = null;
		try{
			jpa.beginTransaction();
			foundWard = (Ward)jpa.find(Ward.class, ward.getCode());
			jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		
		if (foundWard.getLock() == ward.getLock())
		{
			isLockWard = true;
		}

		return isLockWard;
	}
}
