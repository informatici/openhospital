package org.isf.distype.manager;

import java.util.ArrayList;
import java.util.List;

import org.isf.distype.model.DiseaseType;
import org.isf.distype.service.DiseaseTypeIoOperation;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Manager class for DisType module.
 */
@Component
public class DiseaseTypeBrowserManager {

	@Autowired
	private DiseaseTypeIoOperation ioOperations;

	/**
	 * Returns all the stored {@link DiseaseType}s.
	 * @return a list of disease type, <code>null</code> if the operation is failed.
	 * @throws OHServiceException 
	 */
	public ArrayList<DiseaseType> getDiseaseType() throws OHServiceException {
        return ioOperations.getDiseaseTypes();
	}

	/**
	 * Store the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to store.
	 * @return <code>true</code> if the {@link DiseaseType} has been stored, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newDiseaseType(DiseaseType diseaseType) throws OHServiceException {
        List<OHExceptionMessage> errors = validateDiseaseType(diseaseType, true);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        return ioOperations.newDiseaseType(diseaseType);
	}

	/**
	 * Updates the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to update.
	 * @return <code>true</code> if the disease type has been updated, false otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateDiseaseType(DiseaseType diseaseType) throws OHServiceException {
        List<OHExceptionMessage> errors = validateDiseaseType(diseaseType, false);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        return ioOperations.updateDiseaseType(diseaseType);
	}

    /**
	 * Checks if the specified code is already used by any {@link DiseaseType}.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, false otherwise.
	 * @throws OHServiceException 
	 */
	public boolean codeControl(String code) throws OHServiceException {
        return ioOperations.isCodePresent(code);
	}

	/**
	 * Deletes the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to remove.
	 * @return <code>true</code> if the disease has been removed, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteDiseaseType(DiseaseType diseaseType) throws OHServiceException {
        return ioOperations.deleteDiseaseType(diseaseType);
	}

    private List<OHExceptionMessage> validateDiseaseType(DiseaseType diseaseType, boolean insert) throws OHServiceException {
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        String key = diseaseType.getCode();
        if (key.equals("")){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),  MessageBundle.getMessage("angal.distype.pleaseinsertacode"),
                    OHSeverityLevel.ERROR));
        }
        if (key.length()>2){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),  MessageBundle.getMessage("angal.distype.codetoolongmaxchars"),
                    OHSeverityLevel.ERROR));
        }

        if(insert){
            if (codeControl(key)){
                errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),  MessageBundle.getMessage("angal.common.codealreadyinuse"),
                        OHSeverityLevel.ERROR));
            }
        }
        if (diseaseType.getDescription().equals("")){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),  MessageBundle.getMessage("angal.distype.pleaseinsertavaliddescription"),
                    OHSeverityLevel.ERROR));
        }
        return errors;
    }
}
