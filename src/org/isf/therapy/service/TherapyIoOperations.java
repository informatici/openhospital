package org.isf.therapy.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.therapy.model.TherapyRow;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

@Component
public class TherapyIoOperations {

	/**
	 * insert a new {@link TherapyRow} (therapy) in the DB
	 * 
	 * @param thRow - the {@link TherapyRow} (therapy)
	 * @param numTherapy - the therapy progressive number for the patient
	 * @return the therapyID
	 * @throws OHException 
	 */
	public TherapyRow newTherapy(
			TherapyRow thRow) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		
		try{
			jpa.beginTransaction();	
			jpa.persist(thRow);
			jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return thRow;
	}
	
	/**
	 * insert a new {@link TherapyRow} (therapy) in the DB
	 * 
	 * @param thRow - the {@link TherapyRow} (therapy)
	 * @param numTherapy - the therapy progressive number for the patient
	 * @return the therapyID
	 * @throws OHException 
	 */
	public int updateOrCreateTherapy(
			TherapyRow thRow) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		
		try{
			jpa.beginTransaction();	
			jpa.merge(thRow);
			jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return thRow.getTherapyID();
	}

	/**
	 * return the list of {@link TherapyRow}s (therapies) for specified Patient ID
	 * or
	 * return all {@link TherapyRow}s (therapies) if <code>0</code> is passed
	 * 
	 * @param patID - the Patient ID
	 * @return the list of {@link TherapyRow}s (therapies)
	 * @throws OHException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TherapyRow> getTherapyRows(
			int patID) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<TherapyRow> therapyList = null;
		ArrayList<Object> params = new ArrayList<Object>();
		String query = null;
				
		try{
			jpa.beginTransaction();

			query = "SELECT * FROM THERAPIES JOIN (MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A) ON THR_MDSR_ID = MDSR_ID";
			if (patID != 0) {
				query += " WHERE THR_PAT_ID = ?";
				params.add(patID);
			}
			query += " ORDER BY THR_PAT_ID, THR_ID";
			jpa.createQuery(query, TherapyRow.class, false);
			jpa.setParameters(params, false);
			List<TherapyRow> therapyLists = (List<TherapyRow>)jpa.getList();
			therapyList = new ArrayList<TherapyRow>(therapyLists);			

			jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return therapyList;
	}
	
	/**
	 * delete all {@link TherapyRow}s (therapies) for specified Patient ID
	 * 
	 * @param patID - the Patient ID
	 * @return <code>true</code> if the therapies have been deleted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean deleteAllTherapies(
			int patID) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true;
        		
		try {
			jpa.beginTransaction();		

			jpa.createQuery("DELETE FROM THERAPIES WHERE THR_PAT_ID = ?", TherapyRow.class, false);
			params.add(patID);
			jpa.setParameters(params, false);
			jpa.executeUpdate();

			jpa.commitTransaction();	
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}		
        return result;
	}
}
