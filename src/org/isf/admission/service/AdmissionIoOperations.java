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

import javax.persistence.Query;

import org.isf.admission.model.Admission;
import org.isf.admission.model.AdmittedPatient;
import org.isf.admtype.model.AdmissionType;
import org.isf.disctype.model.DischargeType;
import org.isf.generaldata.GeneralData;
import org.isf.patient.model.Patient;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

@Component
public class AdmissionIoOperations 
{
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
    @SuppressWarnings({ "unchecked" })
	public ArrayList<AdmittedPatient> getAdmittedPatients(
			String searchTerms) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<AdmittedPatient> admittedPatients = new ArrayList<AdmittedPatient>();
		ArrayList<Object> params = new ArrayList<Object>();
		
		
		jpa.beginTransaction();
		
		String[] terms = _calculateAdmittedPatientsTerms(searchTerms);
		String query = _calculateAdmittedPatientsQuery(terms);
		params = _calculateAdmittedPatientParameters(terms);
		
		Query q = jpa.getEntityManager().createNativeQuery(query,"AdmittedPatient");
		jpa.createQuery(query, null, false);
		jpa.setParameters(params, false);
		List<Object[]> admittedPatientsList = (List<Object[]>)q.getResultList();
		Iterator<Object[]> it = admittedPatientsList.iterator();
		while (it.hasNext()) {
			Object[] object = it.next();
			Patient patient = (Patient) object[0];
			Admission admission = (Admission) object[1];
			AdmittedPatient admittedPatient = new AdmittedPatient(patient, admission);
			admittedPatients.add(admittedPatient);
		}
		
		jpa.commitTransaction();

		return admittedPatients;
	}
    
    private String[] _calculateAdmittedPatientsTerms(
			String searchTerms) throws OHException 
	{
    	String[] terms = null;
    	
    	
    	if (searchTerms != null && !searchTerms.isEmpty()) 
		{
			searchTerms = searchTerms.trim().toLowerCase();
			terms = searchTerms.split(" ");
		}
    	
    	return terms;
	}
    
    @SuppressWarnings("unused") 
    private String _calculateAdmittedPatientsQuery(
    		String[] terms) throws OHException 
	{
    	String query = null;	

    	
    	query = "SELECT PAT.*, ADM.* " +
    			"FROM PATIENT PAT LEFT JOIN " +
    			"(SELECT * FROM ADMISSION WHERE (ADM_DELETED='N' or ADM_DELETED is null) AND ADM_IN = 1) ADM " +
    			"ON ADM.ADM_PAT_ID = PAT.PAT_ID " +
    			"WHERE (PAT.PAT_DELETED='N' or PAT.PAT_DELETED is null) ";
    	if (terms != null) 
		{
			for (String term:terms) 
			{
				query += " AND CONCAT(PAT_ID, LOWER(PAT_SNAME), LOWER(PAT_FNAME), LOWER(PAT_NOTE), LOWER(PAT_TAXCODE)) LIKE ?";
			}
		}
		query += " ORDER BY PAT_ID DESC";
    	
    	return query;
	}
		
    private ArrayList<Object> _calculateAdmittedPatientParameters(
    		String[] terms) throws OHException 
	{
		ArrayList<Object> params = new ArrayList<Object>();
		
		
    	if (terms != null) 
		{
			for (String term:terms) 
			{
				String parameter = "%" + term + "%";
			
				
				params.add(parameter);
			}
		}
    	
    	return params;
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		Admission admission = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM ADMISSION WHERE ADM_PAT_ID=? AND ADM_DELETED='N' AND ADM_DATE_DIS IS NULL";
		jpa.createQuery(query, Admission.class, false);
		params.add(patient.getCode());
		jpa.setParameters(params, false);
		admission = (Admission)jpa.getResult();		
		
		jpa.commitTransaction();

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
		DbJpaUtil jpa = new DbJpaUtil(); 
		Admission admission = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM ADMISSION WHERE ADM_ID=?";
		jpa.createQuery(query, Admission.class, false);
		params.add(id);
		jpa.setParameters(params, false);
		admission = (Admission)jpa.getResult();		
		
		jpa.commitTransaction();

		return admission;
	}

	/**
	 * Returns all the admissions for the specified patient.
	 * @param patient the patient.
	 * @return the admission list.
	 * @throws OHException if an error occurs during database request.
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<Admission> getAdmissions(
			Patient patient) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Admission> padmission = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM ADMISSION WHERE ADM_PAT_ID=? and ADM_DELETED='N' ORDER BY ADM_DATE_ADM ASC";
		jpa.createQuery(query, Admission.class, false);
		params.add(patient.getCode());
		jpa.setParameters(params, false);
		List<Admission> admissionList = (List<Admission>)jpa.getList();
		padmission = new ArrayList<Admission>(admissionList);			
		
		jpa.commitTransaction();

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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.persist(admission);
    	jpa.commitTransaction();
    	
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = false;
				
		
		jpa.beginTransaction();	
		Admission foundAdmission = (Admission)jpa.find(Admission.class, admission.getId()); 
		if (foundAdmission.getLock() != admission.getLock())
		{
			result = true;
		}		
    	jpa.commitTransaction();

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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.merge(admission);
    	jpa.commitTransaction();
    	
		return result;
	}

	/**
	 * Lists the {@link AdmissionType}s.
	 * @return the admission types.
	 * @throws OHException 
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<AdmissionType> getAdmissionType() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<AdmissionType> padmissiontype = null;
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM ADMISSIONTYPE";
		jpa.createQuery(query, AdmissionType.class, false);
		List<AdmissionType> admissionTypeList = (List<AdmissionType>)jpa.getList();
		padmissiontype = new ArrayList<AdmissionType>(admissionTypeList);			
		
		jpa.commitTransaction();

		return padmissiontype;
	}

	/**
	 * Lists the {@link DischargeType}s.
	 * @return the discharge types.
	 * @throws OHException 
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<DischargeType> getDischargeType() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<DischargeType> dischargeTypes = null;
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM DISCHARGETYPE";
		jpa.createQuery(query, DischargeType.class, false);
		List<DischargeType> dischargeList = (List<DischargeType>)jpa.getList();
		dischargeTypes = new ArrayList<DischargeType>(dischargeList);			
		
		jpa.commitTransaction();

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
		DbJpaUtil jpa = new DbJpaUtil(); 
		Admission admission = null;
		ArrayList<Object> params = new ArrayList<Object>();
		
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM ADMISSION " +
				"WHERE ADM_WRD_ID_A=? AND ADM_DATE_ADM >= ? AND ADM_DATE_ADM <= ? AND ADM_DELETED='N' " +
				"ORDER BY ADM_YPROG DESC";
		jpa.createQuery(query, Admission.class, false);
		params = _calculateNextYProgParameters(wardId);
		jpa.setParameters(params, false);
		admission = (Admission)jpa.getResult();	
		next = admission.getYProg() + 1; 
			
		jpa.commitTransaction();

		return next;
	}
	
	private ArrayList<Object> _calculateNextYProgParameters(
			String wardId) throws OHException 
	{
		ArrayList<Object> params = new ArrayList<Object>();		
		GregorianCalendar now = new GregorianCalendar();
		GregorianCalendar first = null;
		GregorianCalendar last = null;
		
		
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

		params.add(wardId);
		params.add(first);
		params.add(last);
		
		return params;
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		Admission foundAdmission = (Admission)jpa.find(Admission.class, admissionId);  
		foundAdmission.setDeleted("Y");
		jpa.merge(foundAdmission);
    	jpa.commitTransaction();
    	
		return result;
	}

	/**
	 * Counts the number of used bed for the specified ward.
	 * @param wardId the ward id.
	 * @return the number of used beds.
	 * @throws OHException if an error occurs retrieving the bed count.
	 */
    @SuppressWarnings("unchecked")
	public int getUsedWardBed(
			String wardId) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Object> params = new ArrayList<Object>();	
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM ADMISSION WHERE ADM_IN = 1 AND ADM_WRD_ID_A = ? AND ADM_DELETED = 'N'";
		jpa.createQuery(query, Admission.class, false);
		params.add(wardId);
		jpa.setParameters(params, false);
		List<Admission> admissionList = (List<Admission>)jpa.getList();		
		
		jpa.commitTransaction();

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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		Patient foundPatient = (Patient)jpa.find(Patient.class, patientId);  
		foundPatient.setPhoto(null);;
		jpa.merge(foundPatient);
    	jpa.commitTransaction();
    	
		return result;
	}
}
