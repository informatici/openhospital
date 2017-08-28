package org.isf.dlvrrestype.manager;

import java.util.ArrayList;

import org.isf.dlvrrestype.model.DeliveryResultType;
import org.isf.dlvrrestype.service.DeliveryResultTypeIoOperation;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager class for DeliveryResultTypeModule.
 */
public class DeliveryResultTypeBrowserManager {

	private final Logger logger = LoggerFactory.getLogger(DeliveryResultTypeBrowserManager.class);
	
	private DeliveryResultTypeIoOperation ioOperations = Menu.getApplicationContext().getBean(DeliveryResultTypeIoOperation.class);

	/**
	 * Returns all stored {@link DeliveryResultType}s.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the stored {@link DeliveryResultType}s, <code>null</code> if an error occurred.
	 * @throws OHServiceException 
	 */
	public ArrayList<DeliveryResultType> getDeliveryResultType() throws OHServiceException {
		try {
			return ioOperations.getDeliveryResultType();
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
					MessageBundle.getMessage("angal.dlvrrestype.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Stores the specified {@link DeliveryResultType}.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param deliveryresultType the delivery result type to store.
	 * @return <code>true</code> if the delivery result type has been stored.
	 * @throws OHServiceException 
	 */
	public boolean newDeliveryResultType(DeliveryResultType deliveryresultType) throws OHServiceException {
		try {
			return ioOperations.newDeliveryResultType(deliveryresultType);
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
					MessageBundle.getMessage("angal.dlvrrestype.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Updates the specified {@link DeliveryResultType}.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param deliveryresultType the delivery result type to update.
	 * @return <code>true</code> if the delivery result type has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateDeliveryResultType(DeliveryResultType deliveryresultType) throws OHServiceException {
		try {
			return ioOperations.updateDeliveryResultType(deliveryresultType);
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
					MessageBundle.getMessage("angal.dlvrrestype.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Checks if the specified code is already used by others {@link DeliveryResultType}s.
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
					MessageBundle.getMessage("angal.dlvrrestype.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Deletes the specified {@link DeliveryResultType}.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param deliveryresultType the delivery result type to delete.
	 * @return <code>true</code> if the delivery result type has been deleted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteDeliveryResultType(DeliveryResultType deliveryresultType) throws OHServiceException {
		try {
			return ioOperations.deleteDeliveryResultType(deliveryresultType);
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
					MessageBundle.getMessage("angal.dlvrrestype.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

}
