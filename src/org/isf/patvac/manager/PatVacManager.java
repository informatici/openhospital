package org.isf.patvac.manager;

/*------------------------------------------
* PatVacManager - patient-vaccine manager
* -----------------------------------------
* modification history
* 25/08/2011 - claudia - first beta version
* 14/11/2011 - claudia - inserted search condition on date
*------------------------------------------*/


import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.patvac.model.PatientVaccine;
import org.isf.patvac.service.PatVacIoOperations;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.springframework.util.StringUtils;


public class PatVacManager {

	private PatVacIoOperations ioOperations = Context.getApplicationContext().getBean(PatVacIoOperations.class);
	
	/**
	 * returns all {@link PatientVaccine}s of today or one week ago
	 * 
	 * @param minusOneWeek - if <code>true</code> return the last week
	 * @return the list of {@link PatientVaccine}s
	 * @throws OHServiceException 
	 */  
	public ArrayList<PatientVaccine> getPatientVaccine(boolean minusOneWeek) throws OHServiceException {
        return  ioOperations.getPatientVaccine(minusOneWeek);
	}
	
	/**
	 * returns all {@link PatientVaccine}s within <code>dateFrom</code> and
	 * <code>dateTo</code>
	 * 
	 * @param vaccineTypeCode
	 * @param vaccineCode
	 * @param dateFrom
	 * @param dateTo
	 * @param sex
	 * @param ageFrom
	 * @param ageTo
	 * @return the list of {@link PatientVaccine}s
	 * @throws OHServiceException 
	 */
	public ArrayList<PatientVaccine> getPatientVaccine(String vaccineTypeCode,String vaccineCode, 
													   GregorianCalendar dateFrom, GregorianCalendar dateTo, 
													   char sex, int ageFrom, int ageTo) throws OHServiceException {
        return ioOperations.getPatientVaccine(vaccineTypeCode, vaccineCode, dateFrom, dateTo, sex, ageFrom, ageTo);
	}

	/**
	 * inserts a {@link PatientVaccine} in the DB
	 * 
	 * @param patVac - the {@link PatientVaccine} to insert
	 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise 
	 * @throws OHServiceException 
	 */
	public boolean newPatientVaccine(PatientVaccine patVac) throws OHServiceException {
        List<OHExceptionMessage> errors = validatePatientVaccine(patVac);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        return ioOperations.newPatientVaccine(patVac);
	}

	/**
	 * updates a {@link PatientVaccine} 
	 * 
	 * @param patVac - the {@link PatientVaccine} to update
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise 
	 * @throws OHServiceException 
	 */
	public boolean updatePatientVaccine(PatientVaccine patVac) throws OHServiceException {
        List<OHExceptionMessage> errors = validatePatientVaccine(patVac);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        return ioOperations.updatePatientVaccine(patVac);
	}

	/**
	 * deletes a {@link PatientVaccine} 
	 * 
	 * @param patVac - the {@link PatientVaccine} to delete
	 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise 
	 * @throws OHServiceException 
	 */
	public boolean deletePatientVaccine(PatientVaccine patVac) throws OHServiceException {
        return ioOperations.deletePatientVaccine(patVac);
	}
	
	/**
	 * Returns the max progressive number within specified year or within current year if <code>0</code>.
	 * 
	 * @param year
	 * @return <code>int</code> - the progressive number in the year
	 * @throws OHServiceException 
	 */
	public int getProgYear(int year) throws OHServiceException {
        return ioOperations.getProgYear(year);
	}

    protected List<OHExceptionMessage> validatePatientVaccine(PatientVaccine patientVaccine){
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();

        if(patientVaccine.getVaccineDate() == null){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.patvac.pleaseinsertvaccinedate"),
                    OHSeverityLevel.ERROR));
        }
        if(patientVaccine.getProgr() < 0){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.patvac.pleaseinsertavalidprogressive"),
                    OHSeverityLevel.ERROR));
        }
        if(patientVaccine.getVaccine() == null){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.patvac.pleaseselectavaccine"),
                    OHSeverityLevel.ERROR));
        }
        if(patientVaccine.getPatient() == null
                || StringUtils.isEmpty(patientVaccine.getPatName())
                || StringUtils.isEmpty(String.valueOf(patientVaccine.getPatSex()))){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.patvac.pleaseselectapatient"),
                    OHSeverityLevel.ERROR));
        }

        return errors;
    }
}

