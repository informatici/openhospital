package org.isf.distype.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.distype.model.DiseaseType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

/**
 * Persistence class for the DisType module.
 */
@Component
public class DiseaseTypeIoOperation {

	/**
	 * Returns all the stored {@link DiseaseType}s.
	 * @return a list of disease type.
	 * @throws OHException if an error occurs retrieving the diseases list.
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<DiseaseType> getDiseaseTypes() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<DiseaseType> diseaseTypes = null;
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM DISEASETYPE ORDER BY DCL_DESC";
		jpa.createQuery(query, DiseaseType.class, false);
		List<DiseaseType> diseaseTypeList = (List<DiseaseType>)jpa.getList();
		diseaseTypes = new ArrayList<DiseaseType>(diseaseTypeList);			
		
		jpa.commitTransaction();

		return diseaseTypes;
	}

	/**
	 * Updates the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to update.
	 * @return <code>true</code> if the disease type has been updated, false otherwise.
	 * @throws OHException if an error occurs during the update operation.
	 */
	public boolean updateDiseaseType(
			DiseaseType diseaseType) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.merge(diseaseType);
    	jpa.commitTransaction();
    	
		return result;	
	}

	/**
	 * Store the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to store.
	 * @return <code>true</code> if the {@link DiseaseType} has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the store operation.
	 */
	public boolean newDiseaseType(
			DiseaseType diseaseType) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.persist(diseaseType);
    	jpa.commitTransaction();
    	
		return result;
	}

	/**
	 * Deletes the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to remove.
	 * @return <code>true</code> if the disease has been removed, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the delete procedure.
	 */
	public boolean deleteDiseaseType(
			DiseaseType diseaseType) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.remove(diseaseType);
    	jpa.commitTransaction();
    	
		return result;	
	}

	/**
	 * Checks if the specified code is already used by any {@link DiseaseType}.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, false otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isCodePresent(
			String code) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		DiseaseType diseaseType;
		boolean result = true;
		
		
		jpa.beginTransaction();	
		diseaseType = (DiseaseType)jpa.find(DiseaseType.class, code);
		if (diseaseType != null)
		{
			result = true;
		}
    	jpa.commitTransaction();
    	
		return result;	
	}
}
