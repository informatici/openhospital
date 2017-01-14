package org.isf.medstockmovtype.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.medstockmovtype.model.MovementType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

/**
 * Persistence class for the medstockmovtype module.
 *
 */
@Component
public class MedicalStockMovementTypeIoOperation {

	/**
	 * Retrieves all the stored {@link MovementType}.
	 * @return all the stored {@link MovementType}s.
	 * @throws OHException if an error occurs retrieving the medical stock movement types.
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<MovementType> getMedicaldsrstockmovType() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<MovementType> medicaldsrstockmovtypes = null;
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM MEDICALDSRSTOCKMOVTYPE ORDER BY MMVT_DESC";
		jpa.createQuery(query, MovementType.class, false);
		List<MovementType> movementTypeList = (List<MovementType>)jpa.getList();
		medicaldsrstockmovtypes = new ArrayList<MovementType>(movementTypeList);			
		
		jpa.commitTransaction();

		return medicaldsrstockmovtypes;
	}

	/**
	 * Updates the specified {@link MovementType}.
	 * @param medicaldsrstockmovType the medical stock movement type to update.
	 * @return <code>true</code> if the specified stock movement type has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateMedicaldsrstockmovType(
			MovementType medicaldsrstockmovType) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.merge(medicaldsrstockmovType);
    	jpa.commitTransaction();
    	
		return result;	
	}

	/**
	 * Stores the specified {@link MovementType}.
	 * @param medicaldsrstockmovType the medical stock movement type to store.
	 * @return <code>true</code> if the medical movement type has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the store operation.
	 */
	public boolean newMedicaldsrstockmovType(
			MovementType medicaldsrstockmovType) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.persist(medicaldsrstockmovType);
    	jpa.commitTransaction();
    	
		return result;	
	}

	/**
	 * Deletes the specified {@link MovementType}.
	 * @param medicaldsrstockmovType the medical stock movement type to delete.
	 * @return <code>true</code> if the medical stock movement type has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the delete operation.
	 */
	public boolean deleteMedicaldsrstockmovType(
			MovementType medicaldsrstockmovType) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		MovementType objToRemove = (MovementType) jpa.find(MovementType.class, medicaldsrstockmovType.getCode());

		jpa.remove(objToRemove);
    	jpa.commitTransaction();
    	
		return result;	
	}

	/**
	 * Checks if the specified medical stock movement type is already used.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isCodePresent(
			String code) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		MovementType medicaldsrstockmovType;
		boolean result = false;
		
		
		jpa.beginTransaction();	
		medicaldsrstockmovType = (MovementType)jpa.find(MovementType.class, code);
		if (medicaldsrstockmovType != null)
		{
			result = true;
		}
    	jpa.commitTransaction();
    	
		return result;	
	}
}
