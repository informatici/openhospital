package org.isf.malnutrition.manager;

import java.util.ArrayList;

import org.isf.generaldata.MessageBundle;
import org.isf.malnutrition.model.Malnutrition;
import org.isf.malnutrition.service.MalnutritionIoOperation;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager for malnutrition module.
 *
 */
public class MalnutritionManager {

	private final Logger logger = LoggerFactory.getLogger(MalnutritionManager.class);
	
	private MalnutritionIoOperation ioOperation = Menu.getApplicationContext().getBean(MalnutritionIoOperation.class);

	/**
	 * Retrieves all the {@link Malnutrition} associated to the given admission id.
	 * In case of wrong parameters an error message is shown and <code>null</code> value is returned.
	 * In case of error a message error is shown and an empty list is returned.
	 * @param aId the admission id to use as filter.
	 * @return all the retrieved malnutrition or <code>null</code> if the specified admission id is <code>null</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<Malnutrition> getMalnutrition(String aId) throws OHServiceException{
		try {
			return ioOperation.getMalnutritions(aId);
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.malnutrition.malnutrition"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.malnutrition.malnutrition"), 
					MessageBundle.getMessage("angal.malnutrition.problemsoccuredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * returns the last {@link Malnutrition} entry for specified patient ID
	 * @param patientID - the patient ID
	 * @return the last {@link Malnutrition} for specified patient ID. <code>null</code> if none.
	 * @throws OHServiceException 
	 */
	public Malnutrition getLastMalnutrition(int patientID) throws OHServiceException {
		try {
			return ioOperation.getLastMalnutrition(patientID);
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.malnutrition.malnutrition"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.malnutrition.malnutrition"), 
					MessageBundle.getMessage("angal.malnutrition.problemsoccuredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Stores a new {@link Malnutrition}. The malnutrition object is updated with the generated id.
	 * @param malnutrition the malnutrition to store.
	 * @return <code>true</code> if the malnutrition has been stored
	 * @throws OHServiceException 
	 */
	public boolean newMalnutrition(Malnutrition malnutrition) throws OHServiceException{
		try {
			return ioOperation.newMalnutrition(malnutrition);
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.malnutrition.malnutrition"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.malnutrition.malnutrition"), 
					MessageBundle.getMessage("angal.malnutrition.problemsoccuredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Updates the specified malnutrition
	 * 
	 * returns false if abortIfLocked == true and there is a lock from another user on the record 
	 * (someone else updated it during this update)
	 * 
	 *  true if the record was updated
	 *  
	 *  throws an exception if the update fails for some other reason
	 *  
	 * @param malnutrition
	 * @param abortIfLocked
	 * @return
	 * @throws OHServiceException
	 */
	public boolean updateMalnutrition(Malnutrition malnutrition, boolean abortIfLocked) throws OHServiceException {
		try {
			int currentLock = ioOperation.getMalnutritionLock(malnutrition.getCode());
			if (currentLock>=0) {
				if (currentLock!=malnutrition.getLock()) {
					if (true == abortIfLocked) {
						return false;
					} else {
						return ioOperation.updateMalnutrition(malnutrition);
					}
				} else {
					return ioOperation.updateMalnutrition(malnutrition);
				}
			}else{
				throw new OHException(MessageBundle.getMessage("angal.malnutrition.couldntfindthedataithasporbablybeendelete"));
			}

		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.malnutrition.malnutrition"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		} catch (Exception e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.malnutrition.malnutrition"), 
					MessageBundle.getMessage("angal.malnutrition.problemsoccuredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * Deletes the specified {@link Malnutrition}.
	 * In case of wrong parameters an error message is shown and <code>false</code> value is returned.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param malnutrition the malnutrition to delete.
	 * @return <code>true</code> if the malnutrition has been deleted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteMalnutrition(Malnutrition malnutrition) throws OHServiceException{
		try {
			return ioOperation.deleteMalnutrition(malnutrition);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.malnutrition.malnutrition"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		} catch (Exception e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.malnutrition.malnutrition"), 
					MessageBundle.getMessage("angal.malnutrition.problemsoccuredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}
}
