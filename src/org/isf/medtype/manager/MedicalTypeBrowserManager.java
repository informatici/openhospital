package org.isf.medtype.manager;

import java.util.ArrayList;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.medtype.model.MedicalType;
import org.isf.medtype.service.MedicalTypeIoOperation;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager class for the medical type module.
 *
 */
public class MedicalTypeBrowserManager {

	private final Logger logger = LoggerFactory.getLogger(MedicalTypeBrowserManager.class);
	
	private MedicalTypeIoOperation ioOperations = Context.getApplicationContext().getBean(MedicalTypeIoOperation.class);
	
	/**
	 * Verify if the object is valid for CRUD and return a list of errors, if any
	 * @param medicalType
	 * @return list of {@link OHExceptionMessage}
	 */
	protected List<OHExceptionMessage> validateMedicalType(MedicalType medicalType) {
		String key = medicalType.getCode();
		String description = medicalType.getDescription();
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        if(key.isEmpty() ){
	        errors.add(new OHExceptionMessage("codeEmptyError", 
	        		MessageBundle.getMessage("angal.medtype.pleaseinsertacode"), 
	        		OHSeverityLevel.ERROR));
        }
        if(key.length()>1){
	        errors.add(new OHExceptionMessage("codeTooLongError", 
	        		MessageBundle.getMessage("angal.medtype.codetoolongmaxchars"), 
	        		OHSeverityLevel.ERROR));
        }
        if(description.isEmpty() ){
            errors.add(new OHExceptionMessage("descriptionEmptyError", 
            		MessageBundle.getMessage("angal.medtype.pleaseinsertavaliddescription"), 
            		OHSeverityLevel.ERROR));
        }
        return errors;
    }

	/**
	 * Retrieves all the medical types.
	 * @return all the medical types.
	 * @throws OHServiceException 
	 */
	public ArrayList<MedicalType> getMedicalType() throws OHServiceException {
		return ioOperations.getMedicalTypes();
	}

	/**
	 * Saves the specified medical type.
	 * @param medicalType the medical type to save.
	 * @return <code>true</code> if the medical type has been saved, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newMedicalType(MedicalType medicalType) throws OHServiceException {
		List<OHExceptionMessage> errors = validateMedicalType(medicalType);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        if (codeControl(medicalType.getCode())){
			throw new OHServiceException(new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.common.codealreadyinuse"), 
					OHSeverityLevel.ERROR));
		}
		return ioOperations.newMedicalType(medicalType);
	}

	/**
	 * Updates the specified medical type.
	 * @param medicalType the medical type to update.
	 * @return <code>true</code> if the medical type has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateMedicalType(MedicalType medicalType) throws OHServiceException {
		List<OHExceptionMessage> errors = validateMedicalType(medicalType);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
		return ioOperations.updateMedicalType(medicalType);
	}

	/**
	 * Checks if the specified medical type code is already used.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, <code>false</code> owtherwise.
	 * @throws OHServiceException 
	 */
	public boolean codeControl(String code) throws OHServiceException {
		return ioOperations.isCodePresent(code);
	}

	/**
	 * Deletes the specified medical type.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param medicalType the medical type to delete.
	 * @return <code>true</code> if the medical type has been deleted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteMedicalType(MedicalType medicalType) throws OHServiceException {
		return ioOperations.deleteMedicalType(medicalType);
	}
}
