package org.isf.examination.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.isf.examination.model.PatientExamination;
import org.isf.examination.service.ExaminationOperations;
import org.isf.generaldata.ExaminationParameters;
import org.isf.menu.manager.Context;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;

public class ExaminationBrowserManager {

	private ExaminationOperations ioOperations = Context.getApplicationContext().getBean(ExaminationOperations.class);

	/**
	 * Default PatientExamination
	 */
	public PatientExamination getDefaultPatientExamination(	Patient patient){
		PatientExamination defaultPatient = new PatientExamination(new Timestamp(new Date().getTime()), patient, ExaminationParameters.HEIGHT_INIT, ExaminationParameters.WEIGHT_INIT,
				ExaminationParameters.AP_MIN, ExaminationParameters.AP_MAX, ExaminationParameters.HR_INIT, ExaminationParameters.TEMP_INIT, ExaminationParameters.SAT_INIT, "");
		return defaultPatient;
	}

	/**
	 * Get from last PatientExamination (only height, weight & note)
	 */
	public PatientExamination getFromLastPatientExamination(PatientExamination lastPatientExamination){
		PatientExamination newPatientExamination = new PatientExamination(new Timestamp(new Date().getTime()), lastPatientExamination.getPatient(), lastPatientExamination.getPex_height(),
				lastPatientExamination.getPex_weight(), lastPatientExamination.getPex_pa_min(), lastPatientExamination.getPex_pa_max(), lastPatientExamination.getPex_fc(), 
				lastPatientExamination.getPex_temp(), lastPatientExamination.getPex_sat(), lastPatientExamination.getPex_note());
		return newPatientExamination;
	}

	/**
	 * 
	 * @param path
	 *            - the PatientHistory to save
	 * @throws OHServiceException 
	 * @throws OHException 
	 */
	public void saveOrUpdate(PatientExamination patex) throws OHServiceException {
        ioOperations.saveOrUpdate(patex);
	}

	public PatientExamination getByID(int id) throws OHServiceException{
        return ioOperations.getByID(id);
	}

	public PatientExamination getLastByPatID(int patID) throws OHServiceException {
		ArrayList<PatientExamination> patExamination = getByPatID(patID);
		
		return !patExamination.isEmpty() ? patExamination.get(0) : null;
	}

	public ArrayList<PatientExamination> getLastNByPatID(int patID, int number) throws OHServiceException {
        return ioOperations.getLastNByPatID(patID, number);
	}

	public ArrayList<PatientExamination> getByPatID(int patID) throws OHServiceException {
        return ioOperations.getByPatID(patID);
	}

}
