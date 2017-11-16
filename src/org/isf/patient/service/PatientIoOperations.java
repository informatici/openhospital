package org.isf.patient.service;

/*------------------------------------------
 * IoOperations - dB operations for the patient entity
 * -----------------------------------------
 * modification history
 * 05/05/2005 - giacomo  - first beta version 
 * 03/11/2006 - ross - added toString method. Gestione apici per
 *                     nome, cognome, citta', indirizzo e note
 * 11/08/2008 - alessandro - added father & mother's names
 * 26/08/2008 - claudio    - added birth date
 * 							 modififed age
 * 01/01/2009 - Fabrizio   - changed the calls to PAT_AGE fields to
 *                           return again an int type
 * 03/12/2009 - Alex       - added method for merge two patients history
 *------------------------------------------*/

import java.util.ArrayList;

import org.isf.generaldata.MessageBundle;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class PatientIoOperations 
{
	@Autowired
	private PatientIoOperationRepository repository;
	
	/**
	 * method that returns the full list of Patients not logically deleted
	 * 
	 * @return the list of patients
	 * @throws OHException
	 */
	public ArrayList<Patient> getPatients() throws OHException 
	{
		ArrayList<Patient> pPatient = null;
		
		
		pPatient = new ArrayList<Patient>(repository.findAllWhereDeleted());			
					
		return pPatient;
	}

	/**
	 * method that returns the full list of Patients not logically deleted with Height and Weight 
	 * 
	 * @param regex
	 * @return the full list of Patients with Height and Weight
	 * @throws OHException
	 */
	public ArrayList<Patient> getPatientsWithHeightAndWeight(
			String regex) throws OHException 
	{

		ArrayList<Patient> pPatient = null;
		
		
		pPatient = new ArrayList<Patient>(repository.findAllPatientsWithHeightAndWeight(regex));			
					
		return pPatient;
	}	

	/**
	 * method that get a Patient by his/her name
	 * 
	 * @param name
	 * @return the Patient that match specified name
	 * @throws OHException
	 */
	public Patient getPatient(
			String name) throws OHException 
	{
		ArrayList<Patient> pPatient = null;
		Patient patient = null;	
		
		
		pPatient = new ArrayList<Patient>(repository.findAllWhereNameAndDeletedOrderedByName(name));
		if (pPatient.size() > 0)
		{			
			patient = pPatient.get(pPatient.size()-1);			
		}
					
		return patient;
	}

	/**
	 * method that get a Patient by his/her ID
	 * 
	 * @param code
	 * @return the Patient
	 * @throws OHException
	 */
	public Patient getPatient(
			Integer code) throws OHException 
	{
		ArrayList<Patient> pPatient = null;
		Patient patient = null;	
		
		
		pPatient = new ArrayList<Patient>(repository.findAllWhereIdAndDeleted(code));
		if (pPatient.size() > 0)
		{			
			patient = pPatient.get(pPatient.size()-1);			
		}
					
		return patient;
	}

	/**
	 * get a Patient by his/her ID, even if he/her has been logically deleted
	 * 
	 * @param code
	 * @return the list of Patients
	 * @throws OHException
	 */
	public Patient getPatientAll(
			Integer code) throws OHException 
	{
		ArrayList<Patient> pPatient = null;
		Patient patient = null;	
		
		
		pPatient = new ArrayList<Patient>(repository.findAllWhereId(code));
		if (pPatient.size() > 0)
		{			
			patient = pPatient.get(pPatient.size()-1);			
		}
					
		return patient;
	}

	/**
	 * Method that insert a new Patient in the dB
	 * 
	 * @param patient
	 * @return true - if the new Patient has been inserted
	 * @throws OHException
	 */
	public boolean newPatient(
			Patient patient) throws OHException 
	{
		boolean result = true;
	

		Patient patientVaccine = repository.save(patient);
		result = (patientVaccine != null);
		
		return result;
	}
	
	/**
	 * 
	 * method that update an existing {@link Patient} in the db
	 * 
	 * @param patient - the {@link Patient} to update
	 * @param check - if <code>true</code> it will performs an integrity check
	 * @return true - if the existing {@link Patient} has been updated
	 * @throws OHException
	 */
	public boolean updatePatient(
			Patient patient, 
			boolean check) throws OHException 
	{
		int lock = 0;
		boolean result = true;
				

		lock = _getUpdatePatientLock(patient.getCode(), check);
	
		
		try {
			repository.updatePatientLock(patient, lock);

		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 				
	
		return result;
	}
	
	private int _getUpdatePatientLock(
			Integer code, 
			boolean check) throws OHException 
	{
		int lock = 0;

		
		if (check == true) 
		{
			Patient patient = repository.findOne(code);
			
			
			lock = patient.getLock();
		}
		
		return lock;
	}

	/**
	 * method that logically delete a Patient (not physically deleted)
	 * 
	 * @param aPatient
	 * @return true - if the Patient has been deleted (logically)
	 * @throws OHException
	 */
	public boolean deletePatient(
			Patient patient) throws OHException 
	{
		boolean result = false;
		int updates = 0;
		
		
		updates = repository.updateDeleted(patient.getCode());
		if (updates > 0)
		{
			result = true;
		}
		
		return result;
	}

	/**
	 * method that check if a Patient is already present in the DB by his/her name
	 * 
	 * @param name
	 * @return true - if the patient is already present
	 * @throws OHException
	 */
	public boolean isPatientPresent(
			String name) throws OHException 
	{
		boolean result = false;
		
		
		ArrayList<Patient> pPatient = null;
		
		
		pPatient = new ArrayList<Patient>(repository.findAllWhereName(name));
		if (pPatient.size() > 0)
		{			
			result = true;				
		}
					
		return result;
	}

	/**
	 * Method that get next PAT_ID is going to be used.
	 * 
	 * @return code
	 * @throws OHException
	 */
	public int getNextPatientCode() throws OHException 
	{
		Integer code = repository.findMaxCode();

		return (code + 1);
	}

	/**
	 * method that merge all clinic details under the same PAT_ID
	 * 
	 * @param mergedPatient
	 * @param patient2
	 * @return true - if no OHExceptions occurred
	 * @throws OHException 
	 */
	public boolean mergePatientHistory(
			Patient mergedPatient, 
			Patient patient2) throws OHException {
		int mergedID = mergedPatient.getCode();
		int obsoleteID = patient2.getCode();
		boolean result = false;
		int updates = 0;
		
		
		updates = repository.updateAdmission(mergedID, obsoleteID);
		updates += repository.updateExamination(mergedID, obsoleteID);	    
		updates += repository.updateLaboratory(mergedID, mergedPatient.getName(), mergedPatient.getAge(), String.valueOf(mergedPatient.getSex()), obsoleteID);
		updates += repository.updateOpd(mergedID, mergedPatient.getAge(), String.valueOf(mergedPatient.getSex()), obsoleteID);
		updates += repository.updateBill(mergedID, mergedPatient.getName(), obsoleteID);
		updates += repository.updateMedicalStock(mergedID, obsoleteID);
		updates += repository.updateTherapy(mergedID, obsoleteID);
		updates += repository.updateVisit(mergedID, obsoleteID);
		updates += repository.updatePatientVaccine(mergedID, obsoleteID);
		updates += repository.updateDelete(obsoleteID); 	
		if (updates > 0)
		{
			result = true;
		}
		
		return result;
	}
}
