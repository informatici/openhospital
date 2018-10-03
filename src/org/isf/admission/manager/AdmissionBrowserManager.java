package org.isf.admission.manager;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.isf.admission.model.Admission;
import org.isf.admission.model.AdmittedPatient;
import org.isf.admission.service.AdmissionIoOperations;
import org.isf.admtype.model.AdmissionType;
import org.isf.disctype.model.DischargeType;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdmissionBrowserManager {

	private final Logger logger = LoggerFactory.getLogger(AdmissionBrowserManager.class);
	
	private AdmissionIoOperations ioOperations = Menu.getApplicationContext().getBean(AdmissionIoOperations.class);

	/**
	 * Returns all patients with ward in which they are admitted.
	 * @return the patient list with associated ward or <code>null</code> if the operation fails.
	 * @throws OHServiceException 
	 */
	public ArrayList<AdmittedPatient> getAdmittedPatients() throws OHServiceException{
		try {
			return ioOperations.getAdmittedPatients();
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Returns all patients with ward in which they are admitted filtering the list using the passed search term.
	 * @param searchTerms the search terms to use for filter the patient list, <code>null</code> if no filter have to be applied.
	 * @return the filtered patient list or <code>null</code> if the operation fails.
	 * @throws OHServiceException 
	 */
	public ArrayList<AdmittedPatient> getAdmittedPatients(String searchTerms) throws OHServiceException{
		try {
			return ioOperations.getAdmittedPatients(searchTerms);
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Returns the admission with the selected id.
	 * @param id the admission id.
	 * @return the admission with the specified id, <code>null</code> otherwise.
	 * @throws OHServiceException 
	 */
	public Admission getAdmission(int id) throws OHServiceException{
		try {
			return ioOperations.getAdmission(id);
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Returns the only one admission without dimission date (or null if none) for the specified patient.
	 * @param patient the patient target of the admission.
	 * @return the patient admission or <code>null</code> if the operation fails.
	 * @throws OHServiceException 
	 */
	public Admission getCurrentAdmission(Patient patient) throws OHServiceException{
		try {
			return ioOperations.getCurrentAdmission(patient);
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Returns all the admissions for the specified patient.
	 * @param patient the patient.
	 * @return the admission list or <code>null</code> if the operation fails.
	 * @throws OHServiceException 
	 */
	public ArrayList<Admission> getAdmissions(Patient patient) throws OHServiceException{
		try {
			return ioOperations.getAdmissions(patient);
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Returns the next prog in the year for a certain ward.
	 * @param wardId the ward id.
	 * @return the next prog
	 * @throws OHServiceException 
	 */
	public int getNextYProg(String wardId) throws OHServiceException{
		try {
			return ioOperations.getNextYProg(wardId);
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Lists the {@link AdmissionType}s.
	 * @return the admission types  or <code>null</code> if the operation fails.
	 * @throws OHServiceException 
	 */
	public ArrayList<AdmissionType> getAdmissionType() throws OHServiceException{	
		try {
			return ioOperations.getAdmissionType();
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Lists the {@link DischargeType}s.
	 * @return the discharge types  or <code>null</code> if the operation fails.
	 * @throws OHServiceException 
	 */
	public ArrayList<DischargeType> getDischargeType() throws OHServiceException{	
		try {
			return ioOperations.getDischargeType();
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Inserts a new admission.
	 * @param admission the admission to insert.
	 * @return <code>true</code> if the admission has been successfully inserted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newAdmission(Admission admission) throws OHServiceException{
		try {
            List<OHExceptionMessage> errors = validateAdmission(admission, true);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
			return ioOperations.newAdmission(admission);
        } catch (OHServiceException e) {
            throw e;
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Inserts a new {@link Admission} and the returns the generated id.
	 * @param admission the admission to insert.
	 * @return the generated id or <code>null</code> if the operation fails.
	 * @throws OHServiceException 
	 */
	public int newAdmissionReturnKey(Admission admission) throws OHServiceException{
		try {
            List<OHExceptionMessage> errors = validateAdmission(admission, true);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
			return ioOperations.newAdmissionReturnKey(admission);
        } catch (OHServiceException e) {
            throw e;
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Updates the specified {@link Admission} object.
	 * @param admission the admission object to update.
	 * @return <code>true</code> if has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateAdmission(Admission admission) throws OHServiceException{
		try {
            List<OHExceptionMessage> errors = validateAdmission(admission, false);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
			return ioOperations.updateAdmission(admission);
        } catch (OHServiceException e) {
            throw e;
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Sets an admission record to deleted.
	 * @param admissionId the admission id.
	 * @return <code>true</code> if the record has been set to delete.
	 * @throws OHServiceException 
	 */
	public boolean setDeleted(int admissionId) throws OHServiceException{
		try {
			return ioOperations.setDeleted(admissionId);
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Counts the number of used bed for the specified ward.
	 * @param wardId the ward id.
	 * @return the number of used beds.
	 * @throws OHServiceException 
	 */
	public int getUsedWardBed(String wardId) throws OHServiceException {
		try {
			return ioOperations.getUsedWardBed(wardId);
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Deletes the patient photo.
	 * @param patientId the patient id.
	 * @return <code>true</code> if the photo has been deleted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deletePatientPhoto(int id) throws OHServiceException {
		try {
			return ioOperations.deletePatientPhoto(id);
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

    protected List<OHExceptionMessage> validateAdmission(Admission admission, boolean insert) throws OHServiceException {
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();

        /*
         * Initizalize AdmissionBrowserManager
         */
        Patient patient = admission.getPatient();
        AdmissionBrowserManager abm = new AdmissionBrowserManager();
        ArrayList<Admission> admList = abm.getAdmissions(patient);

        /*
         * Today Gregorian Calendar
         */
        GregorianCalendar today = new GregorianCalendar();
        DateFormat currentDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, new Locale(GeneralData.LANGUAGE));
        // get year prog ( not null)
        if (admission.getYProg() < 0) {
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.pleaseinsertacorrectprogressiveid"),
                    OHSeverityLevel.ERROR));
        }

        GregorianCalendar dateIn = admission.getAdmDate();
        if (dateIn.after(today)) {
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.futuredatenotallowed"),
                    OHSeverityLevel.ERROR));
        }
        if (dateIn.before(today)) {
            // check for invalid date
            for (Admission ad : admList) {
                if (!insert && ad.getId() == admission.getId()) {
                    continue;
                }
                if ((ad.getAdmDate().before(dateIn) || ad.getAdmDate().compareTo(dateIn) == 0)
                        && (ad.getDisDate() != null && ad.getDisDate().after(dateIn))) {
                    errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.ininserteddatepatientwasalreadyadmitted"),
                            OHSeverityLevel.ERROR));
                }
            }
        }

        Admission last = null;
        if (admList.size() > 0) {
            last = admList.get(admList.size() - 1);
        } else {
            last = admission;
        }
        if (admission.getDisDate() == null && !insert && admission.getId() != last.getId()) {
            // if we are editing an old admission
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.youareeditinganoldadmission"),
                    OHSeverityLevel.ERROR));
        }
        else if (admission.getDisDate() != null){
            GregorianCalendar dateOut = admission.getDisDate();
            // date control
            if (dateOut.before(dateIn)) {
                errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.dischargedatemustbeafteradmissiondate"),
                        OHSeverityLevel.ERROR));
            }
            if (dateOut.after(today)) {
                errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.futuredatenotallowed"),
                        OHSeverityLevel.ERROR));
            } else {
                // check for invalid date
                boolean invalidDate = false;
                Date invalidStart = new Date();
                Date invalidEnd = new Date();
                for (Admission ad : admList) {
                    // case current admission : let it be
                    if (!insert && ad.getId() == admission.getId()) {
                        continue;
                    }
                    // found an open admission
                    // only if i close my own first of it
                    if (ad.getDisDate() == null) {
                        if (!dateOut.after(ad.getAdmDate()))
                            ;// ok
                        else {
                            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.intheselecteddatepatientwasadmittedagain"),
                                    OHSeverityLevel.ERROR));
                        }
                    }
                    // general case
                    else {
                        // DateIn >= adOut
                        if (dateIn.after(ad.getDisDate()) || dateIn.equals(ad.getDisDate())) {
                            // ok
                        }
                        // dateOut <= adIn
                        else if (dateOut.before(ad.getAdmDate()) || dateOut.equals(ad.getAdmDate())) {
                            // ok
                        } else {
                            invalidDate = true;
                            invalidStart = ad.getAdmDate().getTime();
                            invalidEnd = ad.getDisDate().getTime();
                            break;
                        }
                    }
                }
                if (invalidDate) {
                    errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.invalidadmissionperiod") + MessageBundle.getMessage("angal.admission.theadmissionbetween") + " "
                            + currentDateFormat.format(invalidStart) + " " + MessageBundle.getMessage("angal.admission.and") + " " + currentDateFormat.format(invalidEnd) + " "
                            + MessageBundle.getMessage("angal.admission.alreadyexists"),
                            OHSeverityLevel.ERROR));
                }

            }

            GregorianCalendar operationDate = admission.getOpDate();
            if(operationDate != null) {
                GregorianCalendar limit;
                if (admission.getDisDate() == null) {
                    limit = today;
                } else {
                    limit = admission.getDisDate();
                }

                if (operationDate.before(dateIn) || operationDate.after(limit)) {
                    errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.pleaseinsertavalidvisitdate"),
                            OHSeverityLevel.ERROR));
                }
            }

            if (admission.getDiseaseOut1() == null && admission.getDisDate() != null) {
                errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.pleaseselectatleastfirstdiagnosisout"),
                        OHSeverityLevel.ERROR));
            }else if (admission.getDiseaseOut1() != null && admission.getDisDate() == null) {
                errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.pleaseinsertadischargedate"),
                        OHSeverityLevel.ERROR));
            }

            GregorianCalendar visitDate = admission.getVisitDate();
            if (admission.getWard().getCode().equalsIgnoreCase("M")) {
                GregorianCalendar limit;
                if (admission.getDisDate() == null) {
                    limit = today;
                } else {
                    limit = admission.getDisDate();
                }

                if (visitDate.before(dateIn) || visitDate.after(limit)) {
                    errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.pleaseinsertavalidvisitdate"),
                            OHSeverityLevel.ERROR));
                }
            }

            Float f = admission.getWeight();
            if (f != null && f < 0.0f) {
                errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),  MessageBundle.getMessage("angal.admission.pleaseinsertavalidweightvalue"),
                        OHSeverityLevel.ERROR));
            }

            if(admission.getDeliveryDate() != null){
                GregorianCalendar deliveryDate = admission.getDeliveryDate();

                // date control
                GregorianCalendar start;
                if (admission.getVisitDate() == null) {
                    start = admission.getAdmDate();
                } else {
                    start = admission.getVisitDate();
                }

                GregorianCalendar limit;
                if (admission.getDisDate() == null) {
                    limit = today;
                } else {
                    limit = admission.getDisDate();
                }

                if (deliveryDate.before(start) || deliveryDate.after(limit)) {
                    errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.pleaseinsertavaliddeliverydate"),
                            OHSeverityLevel.ERROR));
                }
            }

            GregorianCalendar ctrl1Date = admission.getCtrlDate1();
            if(ctrl1Date != null){
                // date control
                if (admission.getDeliveryDate() == null) {
                    errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.controln1datenodeliverydatefound"),
                            OHSeverityLevel.ERROR));
                }
                GregorianCalendar limit;
                if (admission.getDisDate() == null) {
                    limit = today;
                } else {
                    limit = admission.getDisDate();
                }
                if (ctrl1Date.before(admission.getDeliveryDate()) || ctrl1Date.after(limit)) {
                    errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.pleaseinsertavalidcontroln1date"),
                            OHSeverityLevel.ERROR));
                }
            }

            GregorianCalendar ctrl2Date = admission.getCtrlDate2();
            if(ctrl2Date != null){
                if (admission.getCtrlDate1() == null) {
                    errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.controldaten2controldaten1notfound"),
                            OHSeverityLevel.ERROR));
                }
                // date control
                GregorianCalendar limit;
                if (admission.getDisDate() == null) {
                    limit = today;
                } else {
                    limit = admission.getDisDate();
                }
                if (ctrl2Date.before(ctrl1Date) || ctrl2Date.after(limit)) {
                    errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.pleaseinsertavalidcontroln2date"),
                            OHSeverityLevel.ERROR));
                }
            }
            GregorianCalendar abortDate = admission.getAbortDate();
            if (abortDate != null) {
                // date control
                GregorianCalendar limit;
                if (admission.getDisDate() == null) {
                    limit = today;
                } else {
                    limit = admission.getDisDate();
                }
                if (ctrl2Date != null && abortDate.before(ctrl2Date) || ctrl1Date != null && abortDate.before(ctrl1Date) || abortDate.before(visitDate) || abortDate.after(limit)) {
                    errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.admission.pleaseinsertavalidabortdate"),
                            OHSeverityLevel.ERROR));
                }
            }
        }
        return errors;
    }
}
