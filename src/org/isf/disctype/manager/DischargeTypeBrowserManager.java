package org.isf.disctype.manager;

import java.util.ArrayList;
import java.util.List;

import org.isf.disctype.model.DischargeType;
import org.isf.disctype.service.DischargeTypeIoOperation;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.springframework.stereotype.Component;

@Component
public class DischargeTypeBrowserManager {

	private DischargeTypeIoOperation ioOperations = Context.getApplicationContext().getBean(DischargeTypeIoOperation.class);

	/**
	 * method that returns all DischargeTypes in a list
	 * 
	 * @return the list of all DischargeTypes (could be null)
	 * @throws OHServiceException 
	 */
	public ArrayList<DischargeType> getDischargeType() throws OHServiceException {
        return ioOperations.getDischargeType();
	}

	/**
	 * method that create a new DischargeType
	 * 
	 * @param dischargeType
	 * @return true - if the new DischargeType has been inserted
	 * @throws OHServiceException 
	 */
	public boolean newDischargeType(DischargeType dischargeType) throws OHServiceException {
        List<OHExceptionMessage> errors = validateDischargeType(dischargeType, true);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        return ioOperations.newDischargeType(dischargeType);
	}

	/**
	 * method that updates an already existing DischargeType
	 * 
	 * @param dischargeType
	 * @return true - if the existing DischargeType has been updated
	 * @throws OHServiceException 
	 */
	public boolean updateDischargeType(DischargeType dischargeType) throws OHServiceException {
        List<OHExceptionMessage> errors = validateDischargeType(dischargeType, false);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        return ioOperations.newDischargeType(dischargeType);
	}

	/**
	 * method that check if a DischargeType already exists
	 * 
	 * @param code
	 * @return true - if the DischargeType already exists
	 * @throws OHServiceException 
	 */
	public boolean codeControl(String code) throws OHServiceException {
        return ioOperations.isCodePresent(code);
	}

	/**
	 * method that delete a DischargeType
	 * 
	 * @param dischargeType
	 * @return true - if the DischargeType has been deleted
	 * @throws OHServiceException 
	 */
	public boolean deleteDischargeType(DischargeType dischargeType) throws OHServiceException {
        List<OHExceptionMessage> errors = validateDeleteDischargeType(dischargeType);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        return ioOperations.deleteDischargeType(dischargeType);
	}

    private List<OHExceptionMessage> validateDeleteDischargeType(DischargeType dischargeType) throws OHServiceException {
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        if(dischargeType.getCode().equals("D")){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.disctype.youcannotdeletethisrecord"),
                    OHSeverityLevel.ERROR));
        }
        return errors;
	}
    private List<OHExceptionMessage> validateDischargeType(DischargeType dischargeType, boolean insert) throws OHServiceException {
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        String key = dischargeType.getCode();
        if (key.equals("")){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.disctype.pleaseinsertacode"),
                    OHSeverityLevel.ERROR));
        }
        if (key.length()>10){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.disctype.codetoolongmaxchars"),
                    OHSeverityLevel.ERROR));
        }

        if(insert){
            if(codeControl(key)){
                errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.common.codealreadyinuse"),
                        OHSeverityLevel.ERROR));
            }
        }
        if (dischargeType.getDescription().equals("")){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.disctype.pleaseinsertavaliddescription"),
                    OHSeverityLevel.ERROR));
        }
        return errors;
	}
}
