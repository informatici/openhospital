package org.isf.opetype.manager;

import java.util.ArrayList;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.medtype.model.MedicalType;
import org.isf.menu.gui.Menu;
import org.isf.opetype.model.OperationType;
import org.isf.opetype.service.OperationTypeIoOperation;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;

public class OperationTypeBrowserManager {

	private OperationTypeIoOperation ioOperations = Menu.getApplicationContext().getBean(OperationTypeIoOperation.class);
	
	/**
	 * return the list of {@link OperationType}s
	 * 
	 * @return the list of {@link OperationType}s. It could be <code>empty</code> or <code>null</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<OperationType> getOperationType() throws OHServiceException {
        return ioOperations.getOperationType();
	}
	
	/**
	 * insert an {@link OperationType} in the DB
	 * 
	 * @param operationType - the {@link OperationType} to insert
	 * @return <code>true</code> if the {@link OperationType} has been inserted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newOperationType(OperationType operationType) throws OHServiceException {
        List<OHExceptionMessage> errors = validateOperationType(operationType);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        if (codeControl(operationType.getCode())){
            throw new OHServiceException(new OHExceptionMessage(null,
                    MessageBundle.getMessage("angal.common.codealreadyinuse"),
                    OHSeverityLevel.ERROR));
        }
        return ioOperations.newOperationType(operationType);
	}

	/**
	 * update an {@link OperationType}
	 * 
	 * @param operationType - the {@link OperationType} to update
	 * @return <code>true</code> if the {@link OperationType} has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateOperationType(OperationType operationType) throws OHServiceException {
        List<OHExceptionMessage> errors = validateOperationType(operationType);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        return ioOperations.updateOperationType(operationType);
	}
	
	/**
	 * delete an {@link OperationType}
	 * 
	 * @param operationType - the {@link OperationType} to delete
	 * @return <code>true</code> if the {@link OperationType} has been delete, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteOperationType(OperationType operationType) throws OHServiceException {
        return ioOperations.deleteOperationType(operationType);
	}
	
	/**
	 * checks if an {@link OperationType} code has already been used
	 * @param code - the code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean codeControl(String code) throws OHServiceException {
        return ioOperations.isCodePresent(code);
	}

    protected List<OHExceptionMessage> validateOperationType(OperationType operationType) {
        String key = operationType.getCode();
        String description = operationType.getDescription();
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        if(key == null || key.isEmpty() ){
            errors.add(new OHExceptionMessage("codeEmptyError",
                    MessageBundle.getMessage("angal.opetype.pleaseinsertacode"),
                    OHSeverityLevel.ERROR));
        }
        if(key.length()>1){
            errors.add(new OHExceptionMessage("codeTooLongError",
                    MessageBundle.getMessage("angal.opetype.codetoolongmaxchars"),
                    OHSeverityLevel.ERROR));
        }
        if(description == null || description.isEmpty() ){
            errors.add(new OHExceptionMessage("descriptionEmptyError",
                    MessageBundle.getMessage("angal.opetype.pleaseinsertavaliddescription"),
                    OHSeverityLevel.ERROR));
        }
        return errors;
    }
}
