package org.isf.medstockmovtype.manager;

import java.util.ArrayList;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.medstockmovtype.service.MedicalStockMovementTypeIoOperation;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager class for the medical stock movement type.
 *
 */
public class MedicaldsrstockmovTypeBrowserManager {

	private final Logger logger = LoggerFactory.getLogger(MedicaldsrstockmovTypeBrowserManager.class);
	
	private MedicalStockMovementTypeIoOperation ioOperations = Menu.getApplicationContext().getBean(MedicalStockMovementTypeIoOperation.class);

	/**
	 * Verify if the object is valid for CRUD and return a list of errors, if any
	 * @param movementType
	 * @return list of {@link OHExceptionMessage}
	 */
	protected List<OHExceptionMessage> validateMovementType(MovementType movementType) {
		String key = movementType.getCode();
		String key2 = movementType.getType();
		String description = movementType.getDescription();
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        if(key.isEmpty() ){
	        errors.add(new OHExceptionMessage("codeEmptyError", 
	        		MessageBundle.getMessage("angal.medstockmovtype.pleaseinsertacode"), 
	        		OHSeverityLevel.ERROR));
        }
        if(key.length()>10){
	        errors.add(new OHExceptionMessage("codeTooLongError", 
	        		MessageBundle.getMessage("angal.medstockmovtype.codetoolongmaxchar"), 
	        		OHSeverityLevel.ERROR));
        }
        if(key2.length()>2){
	        errors.add(new OHExceptionMessage("codeTypeTooLongError", 
	        		MessageBundle.getMessage("angal.medstockmovtype.typetoolongmaxchars"), 
	        		OHSeverityLevel.ERROR));
        }
        if(description.isEmpty() ){
            errors.add(new OHExceptionMessage("descriptionEmptyError", 
            		MessageBundle.getMessage("angal.medstockmovtype.pleaseinsertavaliddescription"), 
            		OHSeverityLevel.ERROR));
        }
        return errors;
    }
	
	/**
	 * Returns all the medical stock movement types.
	 * @return all the medical stock movement types.
	 * @throws OHServiceException 
	 */
	public ArrayList<MovementType> getMedicaldsrstockmovType() throws OHServiceException {
		return ioOperations.getMedicaldsrstockmovType();
	}

	/**
	 * Save the specified {@link MovementType}.
	 * @param medicaldsrstockmovType the medical stock movement type to save.
	 * @return <code>true</code> if the medical stock movement type has been saved, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newMedicaldsrstockmovType(MovementType medicaldsrstockmovType) throws OHServiceException {
		List<OHExceptionMessage> errors = validateMovementType(medicaldsrstockmovType);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
		if (codeControl(medicaldsrstockmovType.getCode())){
			throw new OHServiceException(new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.common.codealreadyinuse"), 
					OHSeverityLevel.ERROR));
		}
		return ioOperations.newMedicaldsrstockmovType(medicaldsrstockmovType);
	}

	/**
	 * Updates the specified {@link MovementType}.
	 * @param medicaldsrstockmovType the medical stock movement type to update.
	 * @return <code>true</code> if the medical stock movement type has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateMedicaldsrstockmovType(MovementType medicaldsrstockmovType) throws OHServiceException {
		List<OHExceptionMessage> errors = validateMovementType(medicaldsrstockmovType);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
		return ioOperations.updateMedicaldsrstockmovType(medicaldsrstockmovType);
	}

	/**
	 * Checks if the specified {@link MovementType} code is already used.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean codeControl(String code) throws OHServiceException {
		return ioOperations.isCodePresent(code);
	}

	/**
	 * Deletes the specified {@link MovementType}.
	 * @param medicaldsrstockmovType the medical stock movement type to delete.
	 * @return <code>true</code> if the medical stock movement type has been deleted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteMedicaldsrstockmovType(MovementType medicaldsrstockmovType) throws OHServiceException {
		return ioOperations.deleteMedicaldsrstockmovType(medicaldsrstockmovType);
	}

}
