package org.isf.admission.service;

/*----------------------------------------------------------
 * modification history
 * ====================
 * 10/11/06 - ross - removed from the list the deleted patients
 *                   the list is now in alphabetical  order
 * 11/08/08 - alessandro - addedd getFather&Mother Names
 * 26/08/08 - claudio - changed getAge for managing varchar type
 * 					  - added getBirthDate
 * 01/01/09 - Fabrizio - changed the calls to PAT_AGE fields to
 *                       return again an integer type
 * 20/01/09 - Chiara -   restart of progressive number of maternity 
 * 						 ward on 1st July conditioned to parameter 
 * 						 MATERNITYRESTARTINJUNE in generalData.properties                   
 *-----------------------------------------------------------*/

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.isf.admission.model.Admission;
import org.isf.admission.model.AdmittedPatient;
import org.isf.admtype.model.AdmissionType;
import org.isf.admtype.service.AdmissionTypeIoOperationRepository;
import org.isf.disctype.model.DischargeType;
import org.isf.disctype.service.DischargeTypeIoOperationRepository;
import org.isf.generaldata.GeneralData;
import org.isf.patient.model.Patient;
import org.isf.patient.service.PatientIoOperationRepository;
import org.isf.utils.db.TranslateOHException;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional(rollbackFor=OHException.class)
@TranslateOHException
public class AdmissionIoOperations 
{
	@Autowired
	private AdmissionIoOperationRepository repository;
	@Autowired
	private AdmissionTypeIoOperationRepository typeRepository;
	@Autowired
	private DischargeTypeIoOperationRepository dischargeRepository;
	@Autowired
	private PatientIoOperationRepository patientRepository;
	
	/**
	 * Returns all patients with ward in which they are admitted.
	 * @return the patient list with associated ward.
	 * @throws OHException if an error occurs during database request.
	 */
	public ArrayList<AdmittedPatient> getAdmittedPatients() throws OHException {
		return getAdmittedPatients(null);
	}

	/**
	 * Returns all patients with ward in which they are admitted filtering the list using the passed search term.
	 * @param searchTerms the search terms to use for filter the patient list, <code>null</code> if no filter have to be applied.
	 * @return the filtered patient list.
	 * @throws OHException if an error occurs during database request.
	 */
	public ArrayList<AdmittedPatient> getAdmittedPatients(
			String searchTerms) throws OHException 
	{
		ArrayList<AdmittedPatient> admittedPatients = new ArrayList<AdmittedPatient>();
		List<Object[]> admittedPatientsList = (List<Object[]>)repository.findAllBySearch(searchTerms);
		Iterator<Object[]> it = admittedPatientsList.iterator();
		
		
		while (it.hasNext()) {
			Object[] object = it.next();
			Patient patient = patientRepository.findOne((Integer)object[0]);
			Admission admission = null;
			Integer admissionId = (Integer)object[26];
			if (admissionId != null) admission = repository.findOne((Integer)object[26]);
			
					
			AdmittedPatient admittedPatient = new AdmittedPatient(patient, admission);
			admittedPatients.add(admittedPatient);
		}

		return admittedPatients;
	}

	/**
	 * Returns the only one admission without dimission date (or null if none) for the specified patient.
	 * @param patient the patient target of the admission.
	 * @return the patient admission.
	 * @throws OHException if an error occurs during database request.
	 */
	public Admission getCurrentAdmission(
			Patient patient) throws OHException 
	{ 
		Admission admission = repository.findAllWherePatient(patient.getCode()).get(0);
		
		
		return admission;
	}

	/**
	 * Returns the admission with the selected id.
	 * @param id the admission id.
	 * @return the admission with the specified id, <code>null</code> otherwise.
	 * @throws OHException if an error occurs during database request.
	 */
	public Admission getAdmission(
			int id) throws OHException 
	{
		Admission admission = repository.findOne(id);
		
		
		return admission;
	}

	/**
	 * Returns all the admissions for the specified patient.
	 * @param patient the patient.
	 * @return the admission list.
	 * @throws OHException if an error occurs during database request.
	 */
	public ArrayList<Admission> getAdmissions(
			Patient patient) throws OHException 
	{
		ArrayList<Admission> padmission = (ArrayList<Admission>) repository.findAllWherePatientByOrderByDate(patient.getCode());
	
		
		return padmission;
	}
	
	/**
	 * Inserts a new admission.
	 * @param admission the admission to insert.
	 * @return <code>true</code> if the admission has been successfully inserted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the insertion.
	 */
	public boolean newAdmission(
			Admission admission) throws OHException 
	{
		boolean result = true;
	

		Admission savedAdmission = repository.save(admission);
		result = (savedAdmission != null);
		
		return result;
	}

	/**
	 * Inserts a new {@link Admission} and the returns the generated id.
	 * @param admission the admission to insert.
	 * @return the generated id.
	 * @throws OHException if an error occurs during the insertion.
	 */
	public int newAdmissionReturnKey(
			Admission admission) throws OHException 
	{
		newAdmission(admission);
		
		return admission.getId();
	}

	/**
	 * Checks if the specified {@link Admission} has been modified.
	 * @param admission the admission to check.
	 * @return <code>true</code> if has been modified, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean hasAdmissionModified(
			Admission admission) throws OHException 
	{
		boolean result = false;
				
		
		Admission foundAdmission = repository.findOne(admission.getId()); 
		if (foundAdmission.getLock() != admission.getLock())
		{
			result = true;
		}		

		return result;
	}

	/**
	 * Updates the specified {@link Admission} object.
	 * @param admission the admission object to update.
	 * @return <code>true</code> if has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs.
	 */
	public boolean updateAdmission(
			Admission admission) throws OHException 
	{
		boolean result = true;
	

		Admission savedAdmission = repository.save(admission);
		result = (savedAdmission != null);
		
		return result;
	}

	/**
	 * Lists the {@link AdmissionType}s.
	 * @return the admission types.
	 * @throws OHException 
	 */
	public ArrayList<AdmissionType> getAdmissionType() throws OHException 
	{
		ArrayList<AdmissionType> padmissiontype = (ArrayList<AdmissionType>) typeRepository.findAll();

		
		return padmissiontype;
	}

	/**
	 * Lists the {@link DischargeType}s.
	 * @return the discharge types.
	 * @throws OHException 
	 */
	public ArrayList<DischargeType> getDischargeType() throws OHException 
	{
		ArrayList<DischargeType> dischargeTypes = (ArrayList<DischargeType>) dischargeRepository.findAll();
				
		return dischargeTypes;
	}

    
	/**
	 * Returns the next prog in the year for a certain ward.
	 * @param wardId the ward id.
	 * @return the next prog.
	 * @throws OHException if an error occurs retrieving the value.
	 */
	public int getNextYProg(
			String wardId) throws OHException 
	{
		int next = 1;
		GregorianCalendar now = new GregorianCalendar();
		GregorianCalendar first = null;
		GregorianCalendar last = null;
		Admission admission = null;
		
		
		// de Felice - 20/01/2008 - richiesta di james che, per la sola
		// maternita', chiede di ripartire
		// da 1 il primo di luglio. questo e' implementato ora nel modo
		// seguente:
		// per il reparto maternity (M) il progressivo riparte il primo luglio
		// questo per ogni anno d'ora in poi se il parametro
		// MATERNITYRESTARTINJUNE in generalData.properties � uguale a yes!!
		if (wardId.equalsIgnoreCase("M") && GeneralData.MATERNITYRESTARTINJUNE) 
		{
			if (now.get(Calendar.MONTH) < 6) 
			{
				first = new GregorianCalendar(now.get(Calendar.YEAR) - 1, Calendar.JULY, 1);
				last = new GregorianCalendar(now.get(Calendar.YEAR), Calendar.JUNE, 30);
			} 
			else 
			{
				first = new GregorianCalendar(now.get(Calendar.YEAR), Calendar.JULY, 1);
				last = new GregorianCalendar(now.get(Calendar.YEAR) + 1, Calendar.JUNE, 30);
			}

		} 
		else 
		{
			first = new GregorianCalendar(now.get(Calendar.YEAR), 0, 1);
			last = new GregorianCalendar(now.get(Calendar.YEAR), 11, 31);
		}
		
		admission = repository.findAllWhereWardAndDates(wardId, first, last).get(0);
		if (admission != null) 
		{
			next = admission.getYProg() + 1; 		
		} 
		
		return next;
	}

	/**
	 * Sets an admission record to deleted.
	 * @param admissionId the admission id.
	 * @return <code>true</code> if the record has been set to delete.
	 * @throws OHException if an error occurs.
	 */
	public boolean setDeleted(
			int admissionId) throws OHException 
	{
		boolean result = true;
		
		
		Admission foundAdmission = repository.findOne(admissionId);  
		foundAdmission.setDeleted("Y");
		Admission savedAdmission = repository.save(foundAdmission);
		result = (savedAdmission != null);    	
    	
		return result;
	}

	/**
	 * Counts the number of used bed for the specified ward.
	 * @param wardId the ward id.
	 * @return the number of used beds.
	 * @throws OHException if an error occurs retrieving the bed count.
	 */
	public int getUsedWardBed(
			String wardId) throws OHException 
	{
    	List<Admission> admissionList = repository.findAllWhereIn1(wardId);
		

		return admissionList.size();
	}

	/**
	 * Deletes the patient photo.
	 * @param patientId the patient id.
	 * @return <code>true</code> if the photo has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs.
	 */
	public boolean deletePatientPhoto(
			int patientId) throws OHException
	{
		boolean result = true;
		
		
		Patient foundPatient = patientRepository.findOne(patientId);  
		foundPatient.setPhoto(null);;
		Patient savedPatient = patientRepository.save(foundPatient);
		result = (savedPatient != null);    
		
		return result;
	}
}
