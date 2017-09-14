package org.isf.admtype.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.admtype.model.AdmissionType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Persistence class for admtype module.
 */
@Component
public class AdmissionTypeIoOperation 
{
	
	@Autowired
	private DbJpaUtil jpa;
	/**
	 * Returns all the available {@link AdmissionType}s.
	 * @return a list of admission types.
	 * @throws OHException if an error occurs.
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<AdmissionType> getAdmissionType() throws OHException 
	{
		
		ArrayList<AdmissionType> padmissiontype = null;
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM ADMISSIONTYPE ORDER BY ADMT_DESC";
		jpa.createQuery(query, AdmissionType.class, false);
		List<AdmissionType> admissionTypeList = (List<AdmissionType>)jpa.getList();
		padmissiontype = new ArrayList<AdmissionType>(admissionTypeList);			
		
		jpa.commitTransaction();

		return padmissiontype;
	}

	/**
	 * Updates the specified {@link AdmissionType}.
	 * @param admissionType the admission type to update.
	 * @return <code>true</code> if the admission type has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateAdmissionType(
			AdmissionType admissionType) throws OHException 
	{
		
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.merge(admissionType);
    	jpa.commitTransaction();
    	
		return result;	
	}

	/**
	 * Stores a new {@link AdmissionType}.
	 * @param admissionType the admission type to store.
	 * @return <code>true</code> if the admission type has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the storing operation.
	 */
	public boolean newAdmissionType(
			AdmissionType admissionType) throws OHException 
	{
		
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.persist(admissionType);
    	jpa.commitTransaction();
    	
		return result;	
	}

	/**
	 * Deletes the specified {@link AdmissionType}.
	 * @param admissionType the admission type to delete.
	 * @return <code>true</code> if the admission type has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the delete operation.
	 */
	public boolean deleteAdmissionType(
			AdmissionType admissionType) throws OHException 
	{
		
		boolean result = true;
		
		jpa.beginTransaction();	
		AdmissionType objToRemove = (AdmissionType) jpa.find(AdmissionType.class, admissionType.getCode());
		jpa.remove(objToRemove);
    	jpa.commitTransaction();
    	
		return result;	
	}

	/**
	 * Checks if the specified Code is already used by others {@link AdmissionType}s.
	 * @param code the admission type code to check.
	 * @return <code>true</code> if the code is already used, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isCodePresent(
			String code) throws OHException
	{
		
		AdmissionType admissionType;
		boolean result = false;
		
		
		jpa.beginTransaction();	
		admissionType = (AdmissionType)jpa.find(AdmissionType.class, code);
		if (admissionType != null)
		{
			result = true;
		}
    	jpa.commitTransaction();
    	
		return result;	
	}
}
