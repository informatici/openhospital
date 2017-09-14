package org.isf.visits.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.patient.model.Patient;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.visits.model.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VisitsIoOperations {
	@Autowired
	private DbJpaUtil jpa;

	/**
	 * returns the list of all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID. If <code>0</code> return the list of all {@link Visit}s
	 * @return the list of {@link Visit}s
	 * @throws OHException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Visit> getVisits(
			Integer patID) throws OHException 
	{
		
		ArrayList<Visit> visitList = null;
		ArrayList<Object> params = new ArrayList<Object>();
		String query = null;
				
		
		jpa.beginTransaction();
		
		query = "SELECT * FROM VISITS";
		if (patID != 0) {
			query += " WHERE VST_PAT_ID = ?";
			params.add(patID);
		}
		query += " ORDER BY VST_PAT_ID, VST_DATE";
		jpa.createQuery(query, Visit.class, false);
		jpa.setParameters(params, false);
		List<Visit> therapyLists = (List<Visit>)jpa.getList();
		visitList = new ArrayList<Visit>(therapyLists);			
		
		jpa.commitTransaction();
		
		return visitList;
	}

	/**
	 * Insert a new {@link Visit} for a patID
	 * 
	 * @param visit - the {@link Visit} related to patID. 
	 * @return the visitID
	 * @throws OHException 
	 */
	public int newVisit(
			Visit visit) throws OHException 
	{
		
		
		
		jpa.beginTransaction();	
		jpa.persist(visit);
    	jpa.commitTransaction();
    	
		return visit.getVisitID();
	}
	
	/**
	 * Deletes all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID
	 * @return <code>true</code> if the list has been deleted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean deleteAllVisits(
			int patID) throws OHException 
	{
		
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true;
        		
		
		jpa.beginTransaction();		

		try {
			jpa.createQuery("DELETE FROM VISITS WHERE VST_PAT_ID = ?", Visit.class, false);
			params.add(patID);
			jpa.setParameters(params, false);
			jpa.executeUpdate();
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 	

		jpa.commitTransaction();	
		
        return result;
	}
}
