package org.isf.patient.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.isf.accounting.manager.BillBrowserManager;
import org.isf.accounting.model.Bill;
import org.isf.accounting.service.AccountingIoOperations;
import org.isf.admission.manager.AdmissionBrowserManager;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.patient.model.Patient;
import org.isf.patient.service.PatientIoOperations;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PatientBrowserManager {
	
	private final Logger logger = LoggerFactory.getLogger(PatientBrowserManager.class);

	private PatientIoOperations ioOperations = Menu.getApplicationContext().getBean(PatientIoOperations.class);
	
	/**
	 * methot that insert a new Patient in the db
	 * 
	 * @param patient
	 * @return true - if the new Patient has been inserted
	 * @throws OHServiceException 
	 */
	public boolean newPatient(Patient patient) throws OHServiceException {
		try {
			return ioOperations.newPatient(patient);
        } catch (OHServiceException e) {
            throw e;
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
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
		try {
			return ioOperations.updatePatient(patient);
        } catch (OHServiceException e) {
            throw e;
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}	
	}
	
	/**
	 * 
	 * method that update an existing Patient in the db
	 * 
	 * @param patient
	 * @param check - if true check if the Patient has been modified since last read
	 * @return true - if the existing Patient has been updated
	 * @throws OHServiceException 
	 */
//	private boolean updatePatient(Patient patient, boolean check) throws OHServiceException {
//		try {
//			if (!ioOperations.updatePatient(patient, check)) {
//				int ok = JOptionPane.showConfirmDialog(null,
//						MessageBundle.getMessage("angal.sql.thedatahasbeenupdatedbysomeoneelse") + ".\n" + MessageBundle.getMessage("angal.patient.doyouwanttooverwritethedata") + "?",
//						MessageBundle.getMessage("angal.patient.select"), JOptionPane.YES_NO_OPTION);
//				if (ok != JOptionPane.OK_OPTION)
//					return false;
//				else 
//					return this.updatePatient(patient, false);
//			} else 
//				return true;
//		} catch(OHException e){
//			/*Already cached exception with OH specific error message - 
//			 * create ready to return OHServiceException and keep existing error message
//			 */
//			logger.error("", e);
//			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.patient.titleedit"), 
//					e.getMessage(), OHSeverityLevel.ERROR));
//		}catch(Exception e){
//			//Any exception
//			logger.error("", e);
//			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.patient.titleedit"), 
//					MessageBundle.getMessage("angal.admission.cannotmergeadmittedpatients"), OHSeverityLevel.ERROR));
//		}	
//	}
	
	/**
	 * method that returns the full list of Patients not logically deleted
	 * 
	 * @return the list of patients (could be empty)
	 * @throws OHServiceException 
	 */
	public ArrayList<Patient> getPatient() throws OHServiceException {
		try {
			return ioOperations.getPatients();
        } catch (OHServiceException e) {
            throw e;
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}	
	}
	
	/**
	 * method that get a Patient by his/her name
	 * 
	 * @param name
	 * @return the Patient that match specified name (could be null)
	 * @throws OHServiceException 
	 */
	public Patient getPatient(String name) throws OHServiceException {
		try {
			return ioOperations.getPatient(name);
        } catch (OHServiceException e) {
            throw e;
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * method that get a Patient by his/her ID
	 * 
	 * @param code
	 * @return the Patient (could be null)
	 * @throws OHServiceException 
	 */
	public Patient getPatient(Integer code) throws OHServiceException {
		try {
			return ioOperations.getPatient(code);
        } catch (OHServiceException e) {
            throw e;
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * Retrieve patient's photo for the specified patient
	 * 
	 * @param patient - the specified patient
	 * @return {@link Patient} with photo (if any)
	 * @throws OHServiceException
	 */
//	public Patient getPatientPhoto(Patient patient) throws OHServiceException {
//		try {
//			return ioOperations.getPatientPhoto(patient);
//		}  catch(OHException e){
//			/*Already cached exception with OH specific error message - 
//			 * create ready to return OHServiceException and keep existing error message
//			 */
//			logger.error("", e);
//			throw new OHServiceException(e, new OHExceptionMessage(null, 
//					e.getMessage(), OHSeverityLevel.ERROR));
//		}catch(Exception e){
//			//Any exception
//			logger.error("", e);
//			throw new OHServiceException(e, new OHExceptionMessage(null, 
//					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
//		}
//	}
	
	/**
	 * get a Patient by his/her ID, even if he/her has been logically deleted
	 * 
	 * @param code
	 * @return the list of Patients (could be null)
	 * @throws OHServiceException 
	 */
	public Patient getPatientAll(Integer code) throws OHServiceException {
		try {
			return ioOperations.getPatientAll(code);
        } catch (OHServiceException e) {
            throw e;
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * methot that get next PAT_ID is going to be used.
	 * 
	 * @return code
	 * @throws OHServiceException 
	 */
	public int getNextPatientCode() throws OHServiceException {
		try {
			return ioOperations.getNextPatientCode();
        } catch (OHServiceException e) {
            throw e;
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
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
        BillBrowserManager billMan = new BillBrowserManager(Menu.getApplicationContext().getBean(AccountingIoOperations.class));
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
		try {
			return ioOperations.deletePatient(patient);
        } catch (OHServiceException e) {
            throw e;
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * method that check if a Patient is already present in the DB by his/her name
	 * 
	 * @param name
	 * @return true - if the patient is already present
	 * @throws OHServiceException 
	 */
	public boolean isPatientPresent(String name) throws OHServiceException {
		try {
			return ioOperations.isPatientPresent(name);
        } catch (OHServiceException e) {
            throw e;
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
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
		try {
			return ioOperations.getPatientsWithHeightAndWeight(regex);
        } catch (OHServiceException e) {
            throw e;
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * method that merge patients and all clinic details under the same PAT_ID
	 * 
	 * @param mergedPatient
	 * @param patient2
	 * @return true - if no OHServiceException occurred
	 */
	public boolean mergePatient(Patient mergedPatient, Patient patient2) throws OHServiceException {
		
		try{
			
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
        } catch (OHServiceException e) {
            throw e;
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.admission.merge"), 
					MessageBundle.getMessage("angal.admission.cannotmergeadmittedpatients"), OHSeverityLevel.ERROR));
		}
	}
}
