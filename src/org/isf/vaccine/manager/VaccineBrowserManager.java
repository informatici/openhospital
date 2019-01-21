package org.isf.vaccine.manager;

import java.util.ArrayList;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.isf.vaccine.model.Vaccine;
import org.isf.vaccine.service.VaccineIoOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that provides gui separation from database operations and gives some
 * useful logic manipulations of the dinamic data (memory)
 *
 * @author Eva
 *
 *
 * modification history
 *  20/10/2011 - Cla - insert vaccinetype managment
 *
 */
public class VaccineBrowserManager {

    private final Logger logger = LoggerFactory.getLogger(VaccineBrowserManager.class);
	private VaccineIoOperations ioOperations = Context.getApplicationContext().getBean(VaccineIoOperations.class);
	
	/**
	 * Verify if the object is valid for CRUD and return a list of errors, if any
	 * @param vaccine
	 * @return list of {@link OHExceptionMessage}
	 */
	protected List<OHExceptionMessage> validateVaccine(Vaccine vaccine) {
		String key = vaccine.getCode();
		String description = vaccine.getDescription();
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        if(key.isEmpty() ){
	        errors.add(new OHExceptionMessage("codeEmptyError", 
	        		MessageBundle.getMessage("angal.vaccine.pleaseinsertacode"), 
	        		OHSeverityLevel.ERROR));
        }
        if(key.length()>10){
	        errors.add(new OHExceptionMessage("codeTooLongError", 
	        		MessageBundle.getMessage("angal.vaccine.codemaxchars"), 
	        		OHSeverityLevel.ERROR));
        }
        if(description.isEmpty() ){
            errors.add(new OHExceptionMessage("descriptionEmptyError", 
            		MessageBundle.getMessage("angal.vaccine.pleaseinsertadescription"), 
            		OHSeverityLevel.ERROR));
        }
        return errors;
    }

	/**
	 * returns the list of {@link Vaccine}s in the DB
	 *
	 * @return the list of {@link Vaccine}s
	 */
	public ArrayList<Vaccine> getVaccine() throws OHServiceException {
		return getVaccine(null);
	}

	/**
	 * returns the list of {@link Vaccine}s based on vaccine type code
	 *
	 * @param vaccineTypeCode - the type code.
	 * @return the list of {@link Vaccine}s
	 */
	public ArrayList<Vaccine> getVaccine(String vaccineTypeCode) throws OHServiceException {
		return ioOperations.getVaccine(vaccineTypeCode);
	}
	
	/**
	 * inserts a new {@link Vaccine} in the DB
	 *
	 * @param vaccine - the item to insert
	 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise
	 */
	public boolean newVaccine(Vaccine vaccine) throws OHServiceException {
		List<OHExceptionMessage> errors = validateVaccine(vaccine);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        if (codeControl(vaccine.getCode())){
			throw new OHServiceException(new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.common.codealreadyinuse"), 
					OHSeverityLevel.ERROR));
		}
		return ioOperations.newVaccine(vaccine);
	}

	/**
	 * updates the specified {@link Vaccine} object.
	 * @param vaccine - the {@link Vaccine} object to update.
	 * @return <code>true</code> if has been updated, <code>false</code> otherwise.
	 */
	public boolean updateVaccine(Vaccine vaccine) throws OHServiceException {
		List<OHExceptionMessage> errors = validateVaccine(vaccine);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        return ioOperations.updateVaccine(vaccine);
    }
	
	/**
	 * deletes a {@link Vaccine} in the DB
	 *
	 * @param vaccine - the item to delete
	 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise
	 */
	public boolean deleteVaccine(Vaccine vaccine) throws OHServiceException {
		return ioOperations.deleteVaccine(vaccine);
	}
	
	/**
	 * checks if the code is already in use
	 *
	 * @param code - the vaccine code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 */
	public boolean codeControl(String code) throws OHServiceException {
		return ioOperations.isCodePresent(code);
	}
}
