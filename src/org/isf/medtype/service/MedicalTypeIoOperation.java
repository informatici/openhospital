package org.isf.medtype.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.medtype.model.MedicalType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

/**
 * Persistence class for the medical type module.
 */
@Component
public class MedicalTypeIoOperation {


	/**
	 * Retrieves all the stored {@link MedicalType}s.
	 * @return the stored medical types.
	 * @throws OHException if an error occurs retrieving the medical types.
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<MedicalType> getMedicalTypes() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<MedicalType> medicaltypes = null;
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM MEDICALDSRTYPE ORDER BY MDSRT_DESC";
		jpa.createQuery(query, MedicalType.class, false);
		List<MedicalType> medicalTypeList = (List<MedicalType>)jpa.getList();
		medicaltypes = new ArrayList<MedicalType>(medicalTypeList);			
		
		jpa.commitTransaction();

		return medicaltypes;
	}

	/**
	 * Updates the specified {@link MedicalType}.
	 * @param medicalType the medical type to update.
	 * @return <code>true</code> if the medical type has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs updating the medical type.
	 */
	public boolean updateMedicalType(
			MedicalType medicalType) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.merge(medicalType);
    	jpa.commitTransaction();
    	
		return result;
	}

	/**
	 * Stores the specified {@link MedicalType}.
	 * @param medicalType the medical type to store.
	 * @return <code>true</code> if the medical type has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs storing the new medical type.
	 */
	public boolean newMedicalType(
			MedicalType medicalType) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.persist(medicalType);
    	jpa.commitTransaction();
    	
		return result;	
	}

	/**
	 * Deletes the specified {@link MedicalType}.
	 * @param medicalType the medical type to delete.
	 * @return <code>true</code> if the medical type has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs deleting the medical type.
	 */
	public boolean deleteMedicalType(
			MedicalType medicalType) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.remove(medicalType);
    	jpa.commitTransaction();
    	
		return result;
	}

	/**
	 * Checks if the specified {@link MedicalType} code is already stored.
	 * @param code the {@link MedicalType} code to check.
	 * @return <code>true</code> if the medical code is already stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isCodePresent(
			String code) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		MedicalType medicalType;
		boolean result = true;
		
		
		jpa.beginTransaction();	
		medicalType = (MedicalType)jpa.find(MedicalType.class, code);
		if (medicalType != null)
		{
			result = true;
		}
    	jpa.commitTransaction();
    	
		return result;	
	}
}
