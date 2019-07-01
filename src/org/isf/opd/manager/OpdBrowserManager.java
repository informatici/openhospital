package org.isf.opd.manager;


import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.disease.model.Disease;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.opd.model.Opd;
import org.isf.opd.service.OpdIoOperations;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Vero
 * 
 */
public class OpdBrowserManager {
	
	private final Logger logger = LoggerFactory.getLogger(OpdBrowserManager.class);
	
	private OpdIoOperations ioOperations = Context.getApplicationContext().getBean(OpdIoOperations.class);
	
	/**
	 * Verify if the object is valid for CRUD and return a list of errors, if any
	 * @param opd
	 * @return list of {@link OHExceptionMessage}
	 */
	protected List<OHExceptionMessage> validateOpd(Opd opd, boolean insert) {
		
		Disease disease=opd.getDisease();
		Disease disease2=opd.getDisease2();
		Disease disease3=opd.getDisease3();
		if (opd.getDate() == null) opd.setDate(new Date());
		if (opd.getUserID() == null) opd.setUserID(UserBrowsingManager.getCurrentUser());
		
		List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
		//Check Visit Date
				if (opd.getVisitDate() == null) {
					errors.add(new OHExceptionMessage("noVisitDateError", 
			        		MessageBundle.getMessage("angal.opd.pleaseinsertattendancedate"), 
			        		OHSeverityLevel.ERROR));
				}
		//Check Patient
		if (GeneralData.OPDEXTENDED && opd.getPatient() == null) {
			errors.add(new OHExceptionMessage("patientNullError", 
	        		MessageBundle.getMessage("angal.opd.pleaseselectapatient"), 
	        		OHSeverityLevel.ERROR));
		}
		if (GeneralData.OPDEXTENDED && opd.getPatient() != null) {
			/*
			 * Age and Sex has not to be updated 
			 * for reporting purposes
			 */
			if (insert) {
				opd.setAge(opd.getPatient().getAge());
				opd.setSex(opd.getPatient().getSex());
			}
		}
		//Check Sex and Age
		if (opd.getAge() < 0) {
			errors.add(new OHExceptionMessage("invalidAgeError", 
	        		MessageBundle.getMessage("angal.opd.insertage"), 
	        		OHSeverityLevel.ERROR));
		}
		if (opd.getSex() == ' ') {
			errors.add(new OHExceptionMessage("noSexError", 
	        		MessageBundle.getMessage("angal.opd.selectsex"), 
	        		OHSeverityLevel.ERROR));
		}
		//Check Disease n.1
		if (disease == null) {
			errors.add(new OHExceptionMessage("disease1NullOrEmptyError", 
	        		MessageBundle.getMessage("angal.opd.pleaseselectadisease"), 
	        		OHSeverityLevel.ERROR));
		} else {
			//Check double diseases
			if (disease2 != null && disease.getCode().equals(disease2.getCode())) {
				errors.add(new OHExceptionMessage("disease2equals1Error", 
		        		MessageBundle.getMessage("angal.opd.duplicatediseasesnotallowed"), 
		        		OHSeverityLevel.ERROR));
			}
			if (disease3 != null && disease.getCode().equals(disease3.getCode())) {
				errors.add(new OHExceptionMessage("disease3equals1Error", 
		        		MessageBundle.getMessage("angal.opd.duplicatediseasesnotallowed"), 
		        		OHSeverityLevel.ERROR));
			}
			if (disease2 != null && disease3 != null && disease2.getCode().equals(disease3.getCode())) {
				errors.add(new OHExceptionMessage("disease3equals2Error", 
		        		MessageBundle.getMessage("angal.opd.duplicatediseasesnotallowed"), 
		        		OHSeverityLevel.ERROR));
			}
		}
		
        return errors;
    }
	
	/**
	 * return all Opds of today or since one week
	 * 
	 * @param oneWeek - if <code>true</code> return the last week, only today otherwise.
	 * @return the list of Opds. It could be <code>null</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<Opd> getOpd(boolean oneWeek) throws OHServiceException{
		return ioOperations.getOpdList(oneWeek);
	}
	
	/**
	 * 
	 * return all Opds within specified dates
	 * 
	 * @param diseaseTypeCode
	 * @param diseaseCode
	 * @param dateFrom
	 * @param dateTo
	 * @param ageFrom
	 * @param ageTo
	 * @param sex
	 * @param newPatient
	 * @return the list of Opds. It could be <code>null</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<Opd> getOpd(String diseaseTypeCode,String diseaseCode, GregorianCalendar dateFrom,GregorianCalendar dateTo,int ageFrom, int ageTo,char sex,char newPatient) throws OHServiceException {
		return ioOperations.getOpdList(diseaseTypeCode,diseaseCode,dateFrom,dateTo,ageFrom,ageTo,sex,newPatient);
	}
	
	/**
	 * returns all {@link Opd}s associated to specified patient ID
	 * 
	 * @param patID - the patient ID
	 * @return the list of {@link Opd}s associated to specified patient ID.
	 * 		   the whole list of {@link Opd}s if <code>0</code> is passed.
	 * @throws OHServiceException 
	 */
	public ArrayList<Opd> getOpdList(int patientcode) throws OHServiceException {
		return ioOperations.getOpdList(patientcode);
	}

	/**
	 * insert a new item in the db
	 * 
	 * @param an {@link Opd}
	 * @return <code>true</code> if the item has been inserted
	 * @throws OHServiceException 
	 */
	public boolean newOpd(Opd opd) throws OHServiceException {
		List<OHExceptionMessage> errors = validateOpd(opd, true);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
		return ioOperations.newOpd(opd);
	}

	/**
	 * Updates the specified {@link Opd} object.
	 * @param opd - the {@link Opd} object to update.
	 * @return the updated {@link Opd}
	 * @throws OHServiceException 
	 */
	public Opd updateOpd(Opd opd) throws OHServiceException{
		List<OHExceptionMessage> errors = validateOpd(opd, false);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
		return ioOperations.updateOpd(opd);
	}

	/**
	 * delete an {@link Opd} from the db
	 * 
	 * @param opd - the {@link Opd} to delete
	 * @return <code>true</code> if the item has been deleted. <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteOpd(Opd opd) throws OHServiceException {
		return ioOperations.deleteOpd(opd);
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
	
	/**
	 * return the last Opd in time associated with specified patient ID. 
	 * 
	 * @param patID - the patient ID
	 * @return last Opd associated with specified patient ID or <code>null</code>
	 * @throws OHServiceException 
	 */
	public Opd getLastOpd(int patientcode) throws OHServiceException {
		return ioOperations.getLastOpd(patientcode);
	}
	
	/**
	 * Check if the given <param>opdNum<param> does already exist for the give <param>year<param>
	 * @param opdNum - the OPD progressive in year
	 * @param year - the year
	 * @return <code>true<code> if the given number exists in year, <code>false</code> otherwise
	 */
	public Boolean isExistOpdNum(int opdNum, int year)  throws OHServiceException {
		return ioOperations.isExistOpdNum(opdNum, year);
	}
}
