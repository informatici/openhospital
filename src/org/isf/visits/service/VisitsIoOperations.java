package org.isf.visits.service;

import java.util.ArrayList;

import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHException;
import org.isf.visits.model.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class VisitsIoOperations {

	@Autowired
	private VisitsIoOperationRepository repository;
	
	/**
	 * returns the list of all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID. If <code>0</code> return the list of all {@link Visit}s
	 * @return the list of {@link Visit}s
	 * @throws OHException 
	 */
	public ArrayList<Visit> getVisits(
			Integer patID) throws OHException 
	{
		ArrayList<Visit> visits = null;

		
		if (patID != 0) {
			visits = new ArrayList<Visit>(repository.findAllWherePatientByOrderPatientAndDateAsc(patID));
		}
		else
		{
			visits = new ArrayList<Visit>(repository.findAllByOrderPatientAndDateAsc()); 
		}
		
		return visits;
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
		Visit savedVisit = repository.save(visit);
		    	
		return savedVisit.getVisitID();
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
		boolean result = true;

		
		repository.deleteWherePatient(patID);
		
        return result;
	}

	/**
	 * checks if the code is already in use
	 *
	 * @param code - the visit code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean isCodePresent(
			Integer code) throws OHException
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;	
	}
}
