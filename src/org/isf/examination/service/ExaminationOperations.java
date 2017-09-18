/**
 * 
 */
package org.isf.examination.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.isf.examination.model.PatientExamination;
import org.isf.generaldata.ExaminationParameters;
import org.isf.patient.model.Patient;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Mwithi
 * 
 */
@Component
public class ExaminationOperations {
	@Autowired
	private DbJpaUtil jpa;

	public ExaminationOperations() {
	}

	/**
	 * Default PatientExamination
	 */
	public PatientExamination getDefaultPatientExamination(
			Patient patient) 
	{
		PatientExamination defaultPatient = new PatientExamination(new Timestamp(new Date().getTime()), patient, ExaminationParameters.HEIGHT_INIT, ExaminationParameters.WEIGHT_INIT,
				ExaminationParameters.AP_MIN, ExaminationParameters.AP_MAX, ExaminationParameters.HR_INIT, ExaminationParameters.TEMP_INIT, ExaminationParameters.SAT_INIT, "");
		return defaultPatient;
	}

	/**
	 * Get from last PatientExamination (only height, weight & note)
	 */
	public PatientExamination getFromLastPatientExamination(
			PatientExamination lastPatientExamination) 
	{
		PatientExamination newPatientExamination = new PatientExamination(new Timestamp(new Date().getTime()), lastPatientExamination.getPatient(), lastPatientExamination.getPex_height(),
				lastPatientExamination.getPex_weight(), lastPatientExamination.getPex_pa_min(), lastPatientExamination.getPex_pa_max(), lastPatientExamination.getPex_fc(), 
				lastPatientExamination.getPex_temp(), lastPatientExamination.getPex_sat(), lastPatientExamination.getPex_note());
		return newPatientExamination;
	}

	/**
	 * 
	 * @param path
	 *            - the PatientHistory to save
	 * @throws OHException 
	 */
	public void saveOrUpdate(
			PatientExamination patex) throws OHException 
	{
		
		
		
		jpa.beginTransaction();
		if (patex.getPex_ID() != 0)
		{
			jpa.merge(patex);			
		}
		else
		{			
			jpa.persist(patex);
		}
    	jpa.commitTransaction();
    	
		return;	
	}

	public PatientExamination getByID(
			int ID) throws OHException 
	{
		
		
		
		jpa.beginTransaction();	
		PatientExamination foundPatientExamination = (PatientExamination)jpa.find(PatientExamination.class, ID);
    	jpa.commitTransaction();
    	
		return foundPatientExamination;
	}

	public PatientExamination getLastByPatID(
			int patID) throws OHException 
	{
		ArrayList<PatientExamination> patExamination = getByPatID(patID);
		
		return !patExamination.isEmpty() ? patExamination.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<PatientExamination> getLastNByPatID(
			int patID, 
			int number) throws OHException 
	{
		
		ArrayList<PatientExamination> patExaminations = null;
		ArrayList<Object> params = new ArrayList<Object>();
			
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM PATIENTEXAMINATION WHERE PEX_PAT_ID = ? ORDER BY PEX_DATE DESC LIMIT ?";
		params.add(patID);
		params.add(number);
		jpa.createQuery(query, PatientExamination.class, false);
		jpa.setParameters(params, false);
		List<PatientExamination> patExaminationList = (List<PatientExamination>)jpa.getList();
		patExaminations = new ArrayList<PatientExamination>(patExaminationList);			
		
		jpa.commitTransaction();
		
		return patExaminations;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<PatientExamination> getByPatID(
			int patID) throws OHException 
	{
		
		ArrayList<PatientExamination> patExaminations = null;
		ArrayList<Object> params = new ArrayList<Object>();
			
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM PATIENTEXAMINATION WHERE PEX_PAT_ID = ? ORDER BY PEX_DATE DESC";
		params.add(patID);
		jpa.createQuery(query, PatientExamination.class, false);
		jpa.setParameters(params, false);
		List<PatientExamination> patExaminationList = (List<PatientExamination>)jpa.getList();
		patExaminations = new ArrayList<PatientExamination>(patExaminationList);			
		
		jpa.commitTransaction();
		
		return patExaminations;
	}
}
