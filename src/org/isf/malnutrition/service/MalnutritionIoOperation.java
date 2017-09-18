package org.isf.malnutrition.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.malnutrition.model.Malnutrition;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Persistence class for the malnutrition module.
 */
@Component
public class MalnutritionIoOperation {
	@Autowired
	private DbJpaUtil jpa;

	/**
	 * Returns all the available {@link Malnutrition} for the specified admission id.
	 * @param admissionId the admission id
	 * @return the retrieved malnutrition.
	 * @throws OHException if an error occurs retrieving the malnutrition list.
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<Malnutrition> getMalnutritions(
			String admissionId) throws OHException
	{
		
		ArrayList<Malnutrition> malnutritions = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM MALNUTRITIONCONTROL WHERE MLN_ADM_ID = ? ORDER BY MLN_DATE_SUPP";
		params.add(admissionId);
		jpa.createQuery(query, Malnutrition.class, false);
		jpa.setParameters(params, false);
		List<Malnutrition> malnutritionList = (List<Malnutrition>)jpa.getList();
		malnutritions = new ArrayList<Malnutrition>(malnutritionList);			
		
		jpa.commitTransaction();

		return malnutritions;
	}

	/**
	 * Stores a new {@link Malnutrition}. The malnutrition object is updated with the generated id.
	 * @param malnutrition the malnutrition to store.
	 * @return <code>true</code> if the malnutrition has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs storing the malnutrition.
	 */
	public boolean newMalnutrition(
			Malnutrition malnutrition) throws OHException
	{
		
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.persist(malnutrition);
    	jpa.commitTransaction();
    	
		return result;
	}

	/**
	 * Retrieves the lock value for the specified {@link Malnutrition} code.
	 * @param malnutritionCode the {@link Malnutrition} code.
	 * @return the retrieved code or -1 if no malnutrition has been found. 
	 * @throws OHException if an error occurs retrieving the code.
	 */
	public int getMalnutritionLock(
			int malnutritionCode) throws OHException
	{
		
		Malnutrition malnutrition = null;
		int lock = -1;
				
		
		jpa.beginTransaction();
		
		malnutrition = (Malnutrition)jpa.find(Malnutrition.class, malnutritionCode); 
		if (malnutrition != null)
		{
			lock = malnutrition.getLock();
		}
		
		jpa.commitTransaction();
		
		return lock;
	}

	/**
	 * Updates the specified {@link Malnutrition}.
	 * @param malnutrition the malnutrition to update.
	 * @return <code>true</code> if the malnutrition has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs updating the malnutrition.
	 */
	public boolean updateMalnutrition(
			Malnutrition malnutrition) throws OHException
	{
		
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.merge(malnutrition);
    	jpa.commitTransaction();
    	
		return result;
	}
	
	/**
	 * returns the last {@link Malnutrition} entry for specified patient ID
	 * @param patientID - the patient ID
	 * @return the last {@link Malnutrition} for specified patient ID. <code>null</code> if none.
	 * @throws OHException
	 */
    @SuppressWarnings("unchecked")
	public Malnutrition getLastMalnutrition(
			int patientID) throws OHException 
	{
		
		ArrayList<Malnutrition> malnutritions = null;
		ArrayList<Object> params = new ArrayList<Object>();
		Malnutrition theMalnutrition = null;
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM MALNUTRITIONCONTROL WHERE MLN_ADM_ID = ? ORDER BY MLN_DATE_SUPP DESC LIMIT 1";
		params.add(patientID);
		jpa.createQuery(query, Malnutrition.class, false);
		jpa.setParameters(params, false);
		List<Malnutrition> malnutritionList = (List<Malnutrition>)jpa.getList();
		malnutritions = new ArrayList<Malnutrition>(malnutritionList);			
		
		jpa.commitTransaction();
		
		try {
			theMalnutrition = malnutritions.get(0);
		} catch (Exception e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}

		return theMalnutrition;
	}

	/**
	 * Deletes the specified {@link Malnutrition}.
	 * @param malnutrition the malnutrition to delete.
	 * @return <code>true</code> if the malnutrition has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs deleting the specified malnutrition.
	 */
	public boolean deleteMalnutrition(
			Malnutrition malnutrition) throws OHException 
	{
		
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.remove(malnutrition);
    	jpa.commitTransaction();
    	
		return result;	
	}
}
