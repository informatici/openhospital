package org.isf.dlvrtype.manager;

import java.util.ArrayList;
import java.util.List;

import org.isf.dlvrtype.model.DeliveryType;
import org.isf.dlvrtype.service.DeliveryTypeIoOperation;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The manager class for the DeliveryType module.
 */
public class DeliveryTypeBrowserManager {

    private final Logger logger = LoggerFactory.getLogger(DeliveryTypeBrowserManager.class);

    private DeliveryTypeIoOperation ioOperations = Menu.getApplicationContext().getBean(DeliveryTypeIoOperation.class);

    /**
     * Returns all stored {@link DeliveryType}s.
     * In case of error a message error is shown and a <code>null</code> value is returned.
     * @return all stored delivery types, <code>null</code> if an error occurred.
     * @throws OHServiceException
     */
    public ArrayList<DeliveryType> getDeliveryType() throws OHServiceException {
        try {
            return ioOperations.getDeliveryType();
        }  catch(OHException e){
            /*Already cached exception with OH specific error message -
             * create ready to return OHServiceException and keep existing error message
             */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(null,
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(null,
                    MessageBundle.getMessage("angal.dlvrtype.thdatacouldnotbesaved"), OHSeverityLevel.ERROR));
        }
    }

    /**
     * Stores the specified {@link DeliveryType}.
     * In case of error a message error is shown and a <code>false</code> value is returned.
     * @param deliveryType the delivery type to store.
     * @return <code>true</code> if the delivery type has been stored, <code>false</code> otherwise.
     * @throws OHServiceException
     */
    public boolean newDeliveryType(DeliveryType deliveryType) throws OHServiceException {
        try {
            List<OHExceptionMessage> errors = validateDeliveryType(deliveryType, true);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
            return ioOperations.newDeliveryType(deliveryType);
        } catch (OHServiceException e) {
            throw e;
        }  catch(OHException e){
            /*Already cached exception with OH specific error message -
             * create ready to return OHServiceException and keep existing error message
             */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(null,
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(null,
                    MessageBundle.getMessage("angal.dlvrtype.thdatacouldnotbesaved"), OHSeverityLevel.ERROR));
        }
    }

    /**
     * Updates the specified {@link DeliveryType}.
     * In case of error a message error is shown and a <code>false</code> value is returned.
     * @param deliveryType the delivery type to update.
     * @return <code>true</code> if the delivery type has been update.
     * @throws OHServiceException
     */
    public boolean updateDeliveryType(DeliveryType deliveryType) throws OHServiceException {
        try {
            List<OHExceptionMessage> errors = validateDeliveryType(deliveryType, false);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
            return ioOperations.updateDeliveryType(deliveryType);
        } catch (OHServiceException e) {
            throw e;
        }  catch(OHException e){
            /*Already cached exception with OH specific error message -
             * create ready to return OHServiceException and keep existing error message
             */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(null,
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(null,
                    MessageBundle.getMessage("angal.dlvrtype.thdatacouldnotbesaved"), OHSeverityLevel.ERROR));
        }
    }

    /**
     * Checks if the specified code is already used by others {@link DeliveryType}s.
     * In case of error a message error is shown and a <code>false</code> value is returned.
     * @param code the code to check.
     * @return <code>true</code> if the code is used, <code>false</code> otherwise.
     * @throws OHServiceException
     */
    public boolean codeControl(String code) throws OHServiceException {
        try {
            return ioOperations.isCodePresent(code);
        }  catch(OHException e){
            /*Already cached exception with OH specific error message -
             * create ready to return OHServiceException and keep existing error message
             */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(null,
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(null,
                    MessageBundle.getMessage("angal.dlvrtype.thdatacouldnotbesaved"), OHSeverityLevel.ERROR));
        }
    }

    /**
     * Delete the specified {@link DeliveryType}.
     * In case of error a message error is shown and a <code>false</code> value is returned.
     * @param deliveryType the delivery type to delete.
     * @return <code>true</code> if the delivery type has been deleted, <code>false</code> otherwise.
     * @throws OHServiceException
     */
    public boolean deleteDeliveryType(DeliveryType deliveryType) throws OHServiceException {
        try {
            return ioOperations.deleteDeliveryType(deliveryType);
        }  catch(OHException e){
            /*Already cached exception with OH specific error message -
             * create ready to return OHServiceException and keep existing error message
             */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(null,
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(null,
                    MessageBundle.getMessage("angal.dlvrtype.thdatacouldnotbesaved"), OHSeverityLevel.ERROR));
        }
    }

    private List<OHExceptionMessage> validateDeliveryType(DeliveryType deliveryType, boolean insert) throws OHServiceException {
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        String key = deliveryType.getCode();
        if (key.equals("")){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),  MessageBundle.getMessage("angal.dlvrtype.pleaseinsertacode"),
                    OHSeverityLevel.ERROR));
        }
        if (key.length()>1){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),  MessageBundle.getMessage("angal.dlvrtype.codetoolongmaxchar"),
                    OHSeverityLevel.ERROR));
        }
        if(insert){
            if (codeControl(key)){
                errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),  MessageBundle.getMessage("angal.common.codealreadyinuse"),
                        OHSeverityLevel.ERROR));
            }
        }
        if (deliveryType.getDescription().equals("")){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),  MessageBundle.getMessage("angal.dlvrtype.pleaseinsertavaliddescription"),
                    OHSeverityLevel.ERROR));
        }
        return errors;
    }
}
