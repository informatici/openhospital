package org.isf.visits.service;

import java.util.ArrayList;

import org.isf.patient.model.Patient;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHServiceException;
import org.isf.visits.model.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
public class VisitsIoOperations {

	@Autowired
	private VisitsIoOperationRepository repository;
	
	/**
	 * returns the list of all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID. If <code>0</code> return the list of all {@link Visit}s
	 * @return the list of {@link Visit}s
	 * @throws OHServiceException 
	 */
	public ArrayList<Visit> getVisits(
			Integer patID) throws OHServiceException 
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
	 * @throws OHServiceException 
	 */
	public int newVisit(
			Visit visit) throws OHServiceException 
	{		
		Visit savedVisit = repository.save(visit);
		    	
		return savedVisit.getVisitID();
	}
	
	/**
	 * Deletes all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID
	 * @return <code>true</code> if the list has been deleted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean deleteAllVisits(
			int patID) throws OHServiceException 
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
	 * @throws OHServiceException 
	 */
	public boolean isCodePresent(
			Integer code) throws OHServiceException
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;	
	}
}
