package org.isf.ward.manager;

import java.util.ArrayList;
import java.util.List;

import org.isf.admission.manager.AdmissionBrowserManager;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.isf.ward.model.Ward;
import org.isf.ward.service.WardIoOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that provides gui separation from database operations and gives some
 * useful logic manipulations of the dinamic data (memory)
 * 
 * @author Rick
 * 
 */
public class WardBrowserManager {

	private final Logger logger = LoggerFactory.getLogger(WardBrowserManager.class);
	
	private AdmissionBrowserManager admManager = Context.getApplicationContext().getBean(AdmissionBrowserManager.class); 
	
	private WardIoOperations ioOperations = Context.getApplicationContext().getBean(WardIoOperations.class);

	/**
	 * Verify if the object is valid for CRUD and return a list of errors, if any
	 * @param ward
	 * @return list of {@link OHExceptionMessage}
	 */
	protected List<OHExceptionMessage> validateWard(Ward ward) {
		String key = ward.getCode();
		String description = ward.getDescription();
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        if(key.isEmpty() ){
	        errors.add(new OHExceptionMessage("codeEmptyError", 
	        		MessageBundle.getMessage("angal.ward.pleaseinsertacode"), 
	        		OHSeverityLevel.ERROR));
        }
        if(key.length()>1){
	        errors.add(new OHExceptionMessage("codeTooLongError", 
	        		MessageBundle.getMessage("angal.ward.codetoolongmaxchars"), 
	        		OHSeverityLevel.ERROR));
        }
        if(description.isEmpty() ){
            errors.add(new OHExceptionMessage("descriptionEmptyError", 
            		MessageBundle.getMessage("angal.ward.pleaseinsertavaliddescription"), 
            		OHSeverityLevel.ERROR));
        }
        if (ward.getBeds()<0) {
        	errors.add(new OHExceptionMessage("negativeBedsError", 
            		MessageBundle.getMessage("angal.ward.bedsnumbermustbepositive"), 
            		OHSeverityLevel.ERROR));
		}
		if (ward.getNurs()<0) {
			errors.add(new OHExceptionMessage("negativeNursesError", 
            		MessageBundle.getMessage("angal.ward.nursesnumbermustbepositive"), 
            		OHSeverityLevel.ERROR));
		}
		if (ward.getDocs()<0) {
			errors.add(new OHExceptionMessage("negativeDoctorsError", 
            		MessageBundle.getMessage("angal.ward.doctorsnumbermustbepositive"), 
            		OHSeverityLevel.ERROR));
		}
        return errors;
    }
	
	/**
	 * Returns all stored wards.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the stored wards.
	 * @throws OHServiceException 
	 */
	public ArrayList<Ward> getWards() throws OHServiceException {
		return ioOperations.getWards(null);
	}
	
	/**
	 * Returns all the stored {@link Ward} with maternity flag <code>false</code>.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the stored diseases with maternity flag false.
	 * @return
	 * @throws OHServiceException 
	 */
	public ArrayList<Ward> getWardsNoMaternity() throws OHServiceException {
		return ioOperations.getWardsNoMaternity();
	}

	/**
	 * Stores the specified {@link Ward}. 
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param ward the ward to store.
	 * @return <code>true</code> if the ward has been stored, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newWard(Ward ward) throws OHServiceException {
		List<OHExceptionMessage> errors = validateWard(ward);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
		if (codeControl(ward.getCode())){
			throw new OHServiceException(new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.common.codealreadyinuse"), 
					OHSeverityLevel.ERROR));
		}
		return ioOperations.newWard(ward);
	}

	/**
	 * Updates the specified {@link Ward}.
	 * If the ward has been updated concurrently a overwrite confirmation message is shown.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param ward the ward to update.
	 * @return <code>true</code> if the ward has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateWard(Ward ward) throws OHServiceException {
		List<OHExceptionMessage> errors = validateWard(ward);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
		return ioOperations.updateWard(ward);
	}

	/**
	 * Mark as deleted the specified {@link Ward}.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param ward the ward to make delete.
	 * @return <code>true</code> if the ward has been marked, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteWard(Ward ward) throws OHServiceException {
		if (ward.getCode().equals("M")) {
			throw new OHServiceException( new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.ward.cannotdeletematernityward"), OHSeverityLevel.ERROR));
		}
		int noPatients = admManager.getUsedWardBed(ward.getCode());
		
		if (noPatients > 0) {
			
			List<OHExceptionMessage> messages = new ArrayList<OHExceptionMessage>();
			messages.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.ward.selectedwardhaspatients1") +
					" " + noPatients + " " +
					MessageBundle.getMessage("angal.ward.selectedwardhaspatients2"), OHSeverityLevel.INFO));
			messages.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.ward.pleasecheckinadmissionpatients"), OHSeverityLevel.ERROR));
			throw new OHServiceException(messages);
		}
		return ioOperations.deleteWard(ward);
	}
	
	/**
	 * Check if the specified code is used by other {@link Ward}s.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param code the code to check.
	 * @return <code>true</code> if it is already used, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean codeControl(String code) throws OHServiceException {
		return ioOperations.isCodePresent(code);
	}
	
	/**
	 * Create default Maternity {@link Ward} as follow:
	 * {'code' : "M",
	 * 'Description' : MessageBundle.getMessage("angal.admission.maternity"),
	 * 'Telephone' : "234/52544",
	 * 'Fax' : "54324/5424",
	 * 'Mail' :  "maternity@stluke.org",
	 * 'Beds' : 20,
	 * 'Nurses' : 3,
	 * 'Doctors' : 2,
	 * 'isPharmacy' : false,
	 * 'isMale' : false,
	 * 'isFemale' : true,
	 * 'LOCK (version)' : 0,
	 * }
	 * 
	 * @return maternity ward
	 */
	private Ward getDefaultMaternityWard() {
		Ward maternity = new Ward(
				"M",
				MessageBundle.getMessage("angal.admission.maternity"),
				"234/52544", //Telephone
				"54324/5424", //Fax
				"maternity@stluke.org",
				20, //Beds
				3, //Nurses
				2, //Doctors
				false, //isPharmacy
				false, //isMale
				true); //isFemale
		return maternity;
	}
	
	/**
	 * Check if the Maternity {@link Ward} with code "M" exists or not.
	 * @param createIfNotExists - if {@code true} it will create the missing {@link Ward} (with default values)
	 * 	and will return {@link true} 
	 * @return <code>true</code> if the Maternity {@link Ward} exists, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean maternityControl(boolean createIfNotExists) throws OHServiceException {
		boolean exists = ioOperations.isMaternityPresent();
		if (!exists && createIfNotExists) {
			Ward maternity = getDefaultMaternityWard();
			newWard(maternity);
			return true;
		} else return exists;
	}
	
	/**
	 * Retrieves the number of patients currently admitted in the {@link Ward}
	 * @param ward - the ward
	 * @return the number of patients currently admitted, <code>-1</code> if an error occurs
	 * @throws OHServiceException 
	 */
	public int getCurrentOccupation(Ward ward) throws OHServiceException {
		return ioOperations.getCurrentOccupation(ward);
	}
	
}
