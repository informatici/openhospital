package org.isf.patient.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.isf.accounting.manager.BillBrowserManager;
import org.isf.accounting.model.Bill;
import org.isf.accounting.service.AccountingIoOperations;
import org.isf.admission.manager.AdmissionBrowserManager;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.patient.model.Patient;
import org.isf.patient.service.PatientIoOperations;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Scope("singleton")
public class PatientBrowserManager {
	
	@Autowired
	private PatientIoOperations ioOperations;
	
	/**
	 * methot that insert a new Patient in the db
	 * 
	 * @param patient
	 * @return true - if the new Patient has been inserted
	 * @throws OHServiceException 
	 */
	public boolean newPatient(Patient patient) throws OHServiceException {
        List<OHExceptionMessage> errors = validatePatient(patient);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        return ioOperations.newPatient(patient);
	}
	
	/**
	 * 
	 * method that update an existing Patient in the db
	 * 
	 * @param patient
	 * @return true - if the existing Patient has been updated
	 * @throws OHServiceException 
	 */
	public boolean updatePatient(Patient patient) throws OHServiceException {
        List<OHExceptionMessage> errors = validatePatient(patient);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        return ioOperations.updatePatient(patient);
	}
	
	/**
	 * method that returns the full list of Patients not logically deleted
	 * 
	 * @return the list of patients (could be empty)
	 * @throws OHServiceException 
	 */
	public ArrayList<Patient> getPatient() throws OHServiceException {
        return ioOperations.getPatients();
	}

	/**
	 * method that returns the full list of Patients not logically deleted by pages
	 * 
	 * @return the list of patients (could be empty)
	 * @throws OHServiceException 
	 */
	public ArrayList<Patient> getPatient(int page, int size) throws OHServiceException {
        return ioOperations.getPatients(new PageRequest(page, size));
	}

	/**
	 * method that get a Patient by his/her name
	 * 
	 * @param name
	 * @return the Patient that match specified name (could be null)
	 * @throws OHServiceException 
	 */
	public Patient getPatient(String name) throws OHServiceException {
        return ioOperations.getPatient(name);
	}

	/**
	 * method that get a Patient by his/her ID
	 * 
	 * @param code
	 * @return the Patient (could be null)
	 * @throws OHServiceException 
	 */
	public Patient getPatient(Integer code) throws OHServiceException {
        return ioOperations.getPatient(code);
	}
	

	/**
	 * get a Patient by his/her ID, even if he/her has been logically deleted
	 * 
	 * @param code
	 * @return the list of Patients (could be null)
	 * @throws OHServiceException 
	 */
	public Patient getPatientAll(Integer code) throws OHServiceException {
        return ioOperations.getPatientAll(code);
	}
	
	/**
	 * methot that get next PAT_ID is going to be used.
	 * 
	 * @return code
	 * @throws OHServiceException 
	 */
	public int getNextPatientCode() throws OHServiceException {
        return ioOperations.getNextPatientCode();
	}

    protected List<OHExceptionMessage> validateMergePatients(Patient mergedPatient, Patient patient2) throws OHServiceException {
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        boolean admitted = false;
        AdmissionBrowserManager admMan = new AdmissionBrowserManager();
        if (admMan.getCurrentAdmission(mergedPatient) != null) admitted = true;
        else if (admMan.getCurrentAdmission(patient2) != null) admitted = true;
        if (admitted) {
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.admission.merge"),
                    MessageBundle.getMessage("angal.admission.cannotmergeadmittedpatients"), OHSeverityLevel.ERROR));
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.admission.merge"),
                    MessageBundle.getMessage("angal.admission.patientscannothavependingtask"), OHSeverityLevel.INFO));
        }

        boolean billPending = false;
        BillBrowserManager billMan = new BillBrowserManager(Context.getApplicationContext().getBean(AccountingIoOperations.class));
        ArrayList<Bill> bills = billMan.getPendingBills(mergedPatient.getCode());
        bills = billMan.getPendingBills(mergedPatient.getCode());
        if (bills != null && !bills.isEmpty()) billPending = true;
        else {
            bills = billMan.getPendingBills(patient2.getCode());
            if (bills != null && !bills.isEmpty()) billPending = true;
        }
        if (billPending) {
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.admission.merge"),
                    MessageBundle.getMessage("angal.admission.cannotmergewithpendingbills"), OHSeverityLevel.ERROR));
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.admission.merge"),
                    MessageBundle.getMessage("angal.admission.patientscannothavependingtask"), OHSeverityLevel.INFO));
        }
        if (mergedPatient.getSex() != patient2.getSex()) {
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.admission.merge"), MessageBundle.getMessage("angal.admission.selectedpatientshavedifferentsex"),
                    OHSeverityLevel.ERROR));
        }
        return errors;
    }

    /**
	 * method that logically delete a Patient (not phisically deleted)
	 * 
	 * @param aPatient
	 * @return true - if the Patient has beeb deleted (logically)
	 * @throws OHServiceException 
	 */
	public boolean deletePatient(Patient patient) throws OHServiceException {
        return ioOperations.deletePatient(patient);
	}
	
	/**
	 * method that check if a Patient is already present in the DB by his/her name
	 * 
	 * @param name
	 * @return true - if the patient is already present
	 * @throws OHServiceException 
	 */
	public boolean isPatientPresent(String name) throws OHServiceException {
        return ioOperations.isPatientPresent(name);
	}
	
	/**
	 * method that returns the full list of Patients not logically deleted with Height and Weight 
	 * 
	 * @param regex
	 * @return the full list of Patients with Height and Weight (could be empty)
	 * @throws OHServiceException 
	 * @throws OHException
	 */
	public ArrayList<Patient> getPatientWithHeightAndWeight(String regex) throws OHServiceException{
        return ioOperations.getPatientsWithHeightAndWeight(regex);
	}

	/**
	 * method that merge patients and all clinic details under the same PAT_ID
	 * 
	 * @param mergedPatient
	 * @param patient2
	 * @return true - if no OHServiceException occurred
	 */
	public boolean mergePatient(Patient mergedPatient, Patient patient2) throws OHServiceException {
			if (mergedPatient.getBirthDate() != null &&
					mergedPatient.getAgetype().compareTo("") == 0) {
				//mergedPatient only Age
				Date bdate2 = patient2.getBirthDate();
				int age2 = patient2.getAge();
				String ageType2 = patient2.getAgetype();
				if (bdate2 != null) {
					//patient2 has BirthDate
					mergedPatient.setAge(age2);
					mergedPatient.setBirthDate(bdate2);
				}
				if (bdate2 != null && ageType2.compareTo("") != 0) {
					//patient2 has AgeType 
					mergedPatient.setAge(age2);
					mergedPatient.setAgetype(ageType2);
				}
			}
			
			if (mergedPatient.getAddress().compareTo("") == 0)
				mergedPatient.setAddress(patient2.getAddress());
			
			if (mergedPatient.getCity().compareTo("") == 0)
				mergedPatient.setCity(patient2.getCity());
			
			if (mergedPatient.getNextKin().compareTo("") == 0)
				mergedPatient.setNextKin(patient2.getNextKin());
			
			if (mergedPatient.getTelephone().compareTo("") == 0)
				mergedPatient.setTelephone(patient2.getTelephone());
			
			if (mergedPatient.getMother_name().compareTo("") == 0)
				mergedPatient.setMother_name(patient2.getMother_name());
			
			if (mergedPatient.getMother() == 'U')
				mergedPatient.setMother(patient2.getMother());
			
			if (mergedPatient.getFather_name().compareTo("") == 0)
				mergedPatient.setFather_name(patient2.getFather_name());
			
			if (mergedPatient.getFather() == 'U')
				mergedPatient.setFather(patient2.getFather());
			
			if (mergedPatient.getBloodType().compareTo("") == 0)
				mergedPatient.setBloodType(patient2.getBloodType());
			
			if (mergedPatient.getHasInsurance() == 'U')
				mergedPatient.setHasInsurance(patient2.getHasInsurance());
			
			if (mergedPatient.getParentTogether() == 'U')
				mergedPatient.setParentTogether(patient2.getParentTogether());
			
			if (mergedPatient.getNote().compareTo("") == 0)
				mergedPatient.setNote(patient2.getNote());
			else {
				String note = mergedPatient.getNote();
				mergedPatient.setNote(patient2.getNote()+"\n\n"+note);
			}

            List<OHExceptionMessage> errors = validateMergePatients(mergedPatient, patient2);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
            return ioOperations.mergePatientHistory(mergedPatient, patient2);
	}

    protected List<OHExceptionMessage> validatePatient(Patient patient){
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();

        if (StringUtils.isEmpty(patient.getFirstName())) {
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.patient.insertfirstname"),
                    OHSeverityLevel.ERROR));
        }
        if (StringUtils.isEmpty(patient.getSecondName())) {
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.patient.insertsecondname"),
                    OHSeverityLevel.ERROR));
        }
        if (!checkAge(patient)) {
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.patient.insertvalidage"),
                    OHSeverityLevel.ERROR));
        }
        if (StringUtils.isEmpty(String.valueOf(patient.getSex()))) {
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), "Please select a sex",
                    OHSeverityLevel.ERROR));
        }

        return errors;
    }

    private boolean checkAge(Patient patient) {
	    Date now = new Date();
        Date birthDate = patient.getBirthDate();

        if(birthDate == null || birthDate.after(now)){
            return false;
        }
        if(patient.getAge() < 0 || patient.getAge() > 200){
            return false;
        }
        return true;
    }
}
