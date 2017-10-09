package org.isf.therapy.service;

import java.util.ArrayList;
import org.isf.therapy.model.TherapyRow;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TherapyIoOperations {

	@Autowired
	private TherapyIoOperationRepository repository;
	
	/**
	 * insert a new {@link TherapyRow} (therapy) in the DB
	 * 
	 * @param thRow - the {@link TherapyRow} (therapy)
	 * @param numTherapy - the therapy progressive number for the patient
	 * @return the therapyID
	 * @throws OHException 
	 */
	public int newTherapy(
			TherapyRow thRow) throws OHException 
	{
		TherapyRow savedVaccine = repository.save(thRow);
		
		return savedVaccine.getTherapyID();
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
	public ArrayList<TherapyRow> getTherapyRows(
			int patID) throws OHException 
	{
		ArrayList<TherapyRow> therapyList = null;

		
		if (patID != 0) {
			therapyList = new ArrayList<TherapyRow>(repository.findAllWherePatientByOrderPatientAndIdAsc(patID));
		}
		else
		{
			therapyList = new ArrayList<TherapyRow>(repository.findAllByOrderPatientAndIdAsc()); 
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
		boolean result = true;
	
		
		repository.deleteWherePatient(patID);
		
		return result;	
	}

	/**
	 * checks if the code is already in use
	 *
	 * @param code - the therapy code
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
