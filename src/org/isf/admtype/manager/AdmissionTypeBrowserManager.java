package org.isf.admtype.manager;

import java.util.ArrayList;
import java.util.List;

import org.isf.admtype.model.AdmissionType;
import org.isf.admtype.service.AdmissionTypeIoOperation;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;

public class AdmissionTypeBrowserManager {

	private AdmissionTypeIoOperation ioOperations = Menu.getApplicationContext().getBean(AdmissionTypeIoOperation.class);

	/**
	 * Returns all the available {@link AdmissionType}s.
	 * @return a list of admission types or <code>null</code> if the operation fails.
	 * @throws OHServiceException 
	 */
	public ArrayList<AdmissionType> getAdmissionType() throws OHServiceException {
        return ioOperations.getAdmissionType();
	}

	/**
	 * Stores a new {@link AdmissionType}.
	 * @param admissionType the admission type to store.
	 * @return <code>true</code> if the admission type has been stored, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newAdmissionType(AdmissionType admissionType) throws OHServiceException {
        List<OHExceptionMessage> errors = validateAdmissionType(admissionType, true);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        return ioOperations.newAdmissionType(admissionType);
	}

	/**
	 * Updates the specified {@link AdmissionType}.
	 * @param admissionType the admission type to update.
	 * @return <code>true</code> if the admission type has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateAdmissionType(AdmissionType admissionType) throws OHServiceException {
        List<OHExceptionMessage> errors = validateAdmissionType(admissionType, false);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        return ioOperations.updateAdmissionType(admissionType);
	}

	/**
	 * Checks if the specified Code is already used by others {@link AdmissionType}s.
	 * @param code the admission type code to check.
	 * @return <code>true</code> if the code is already used, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean codeControl(String code) throws OHServiceException {
        return ioOperations.isCodePresent(code);
	}

	/**
	 * Deletes the specified {@link AdmissionType}.
	 * @param admissionType the admission type to delete.
	 * @return <code>true</code> if the admission type has been deleted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteAdmissionType(AdmissionType admissionType) throws OHServiceException {
        return ioOperations.deleteAdmissionType(admissionType);
	}

    protected List<OHExceptionMessage> validateAdmissionType(AdmissionType admissionType, boolean insert) throws OHServiceException {
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        String key = admissionType.getCode();
        if (key.equals("")){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),MessageBundle.getMessage("angal.admtype.pleaseinsertacode"),
                    OHSeverityLevel.ERROR));
        }
        if (key.length()>10){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),MessageBundle.getMessage("angal.admtype.codetoolongmaxchars"),
                    OHSeverityLevel.ERROR));
        }

            if(insert){
                if (codeControl(key)){
                    errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),MessageBundle.getMessage("angal.common.codealreadyinuse"),
                            OHSeverityLevel.ERROR));
                }
            }
        if (admissionType.getDescription().equals("")){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),MessageBundle.getMessage("angal.admtype.pleaseinsertavaliddescription"),
                    OHSeverityLevel.ERROR));
        }
        return errors;
    }
}
