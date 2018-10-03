package org.isf.disctype.manager;

import java.util.ArrayList;
import java.util.List;

import org.isf.disctype.model.DischargeType;
import org.isf.disctype.service.DischargeTypeIoOperation;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DischargeTypeBrowserManager {

	private final Logger logger = LoggerFactory.getLogger(DischargeTypeBrowserManager.class);
	
	private DischargeTypeIoOperation ioOperations = Menu.getApplicationContext().getBean(DischargeTypeIoOperation.class);

	/**
	 * method that returns all DischargeTypes in a list
	 * 
	 * @return the list of all DischargeTypes (could be null)
	 * @throws OHServiceException 
	 */
	public ArrayList<DischargeType> getDischargeType() throws OHServiceException {
		try {
			return ioOperations.getDischargeType();
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null,
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * method that create a new DischargeType
	 * 
	 * @param dischargeType
	 * @return true - if the new DischargeType has been inserted
	 * @throws OHServiceException 
	 */
	public boolean newDischargeType(DischargeType dischargeType) throws OHServiceException {
		try {
            List<OHExceptionMessage> errors = validateDischargeType(dischargeType, true);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
			return ioOperations.newDischargeType(dischargeType);
        } catch (OHServiceException e) {
            throw e;
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null,
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * method that updates an already existing DischargeType
	 * 
	 * @param dischargeType
	 * @return true - if the existing DischargeType has been updated
	 * @throws OHServiceException 
	 */
	public boolean updateDischargeType(DischargeType dischargeType) throws OHServiceException {
		try {
            List<OHExceptionMessage> errors = validateDischargeType(dischargeType, false);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
			return ioOperations.newDischargeType(dischargeType);
        } catch (OHServiceException e) {
            throw e;
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null,
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * method that check if a DischargeType already exists
	 * 
	 * @param code
	 * @return true - if the DischargeType already exists
	 * @throws OHServiceException 
	 */
	public boolean codeControl(String code) throws OHServiceException {
		try {
			return ioOperations.isCodePresent(code);
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null,
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * method that delete a DischargeType
	 * 
	 * @param dischargeType
	 * @return true - if the DischargeType has been deleted
	 * @throws OHServiceException 
	 */
	public boolean deleteDischargeType(DischargeType dischargeType) throws OHServiceException {
		try {
            List<OHExceptionMessage> errors = validateDeleteDischargeType(dischargeType);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
			return ioOperations.deleteDischargeType(dischargeType);
        } catch (OHServiceException e) {
            throw e;
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null,
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
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
