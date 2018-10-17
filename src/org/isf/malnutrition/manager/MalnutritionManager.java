package org.isf.malnutrition.manager;

import java.util.ArrayList;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.malnutrition.model.Malnutrition;
import org.isf.malnutrition.service.MalnutritionIoOperation;
import org.isf.menu.gui.Menu;
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
	 * Verify if the object is valid for CRUD and return a list of errors, if any
	 * @param malnutrition
	 * @return list of {@link OHExceptionMessage}
	 */
	protected List<OHExceptionMessage> validateMalnutrition(Malnutrition malnutrition) {
		List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
		if(malnutrition.getDateSupp()==null) {
			errors.add(new OHExceptionMessage("visitDateNullError", 
	        		MessageBundle.getMessage("angal.malnutrition.pleaseinsertavalidvisitdate"), 
	        		OHSeverityLevel.ERROR));
		}
		if(malnutrition.getDateConf()==null) {
			errors.add(new OHExceptionMessage("controlDateNullError", 
	        		MessageBundle.getMessage("angal.malnutrition.pleaseinsertavalidcontroldate"), 
	        		OHSeverityLevel.ERROR));
		}
		if(malnutrition.getDateSupp()!=null && 
				malnutrition.getDateConf()!=null &&
				malnutrition.getDateConf().before(malnutrition.getDateSupp())) {
			errors.add(new OHExceptionMessage("controlBeforeVisitError", 
	        		MessageBundle.getMessage("angal.malnutrition.controldatemustbeaftervisitdate"), 
	        		OHSeverityLevel.ERROR));
		}
		if(malnutrition.getWeight()==0) {
			errors.add(new OHExceptionMessage("zeroWeightError", 
	        		MessageBundle.getMessage("angal.malnutrition.insertcorrectvalueinweightfield"), 
	        		OHSeverityLevel.ERROR));
		}
		if(malnutrition.getHeight()==0) {
			errors.add(new OHExceptionMessage("zeroHeightError", 
	        		MessageBundle.getMessage("angal.malnutrition.insertcorrectvalueinheightfield"), 
	        		OHSeverityLevel.ERROR));
		}
        return errors;
    }

	/**
	 * Retrieves all the {@link Malnutrition} associated to the given admission id.
	 * In case of wrong parameters an error message is shown and <code>null</code> value is returned.
	 * In case of error a message error is shown and an empty list is returned.
	 * @param admissionID the admission id to use as filter.
	 * @return all the retrieved malnutrition or <code>null</code> if the specified admission id is <code>null</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<Malnutrition> getMalnutrition(String admissionID) throws OHServiceException{
		return ioOperation.getMalnutritions(admissionID);
	}
	
	/**
	 * returns the last {@link Malnutrition} entry for specified patient ID
	 * @param patientID - the patient ID
	 * @return the last {@link Malnutrition} for specified patient ID. <code>null</code> if none.
	 * @throws OHServiceException 
	 */
	public Malnutrition getLastMalnutrition(int patientID) throws OHServiceException {
		return ioOperation.getLastMalnutrition(patientID);
	}

	/**
	 * Stores a new {@link Malnutrition}. The malnutrition object is updated with the generated id.
	 * @param malnutrition the malnutrition to store.
	 * @return <code>true</code> if the malnutrition has been stored
	 * @throws OHServiceException 
	 */
	public boolean newMalnutrition(Malnutrition malnutrition) throws OHServiceException{
		List<OHExceptionMessage> errors = validateMalnutrition(malnutrition);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
		return ioOperation.newMalnutrition(malnutrition);
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
	 * @param the {@link Malnutrition} to update
	 * @return the updated {@link Malnutrition}
	 * @throws OHServiceException
	 */
	public Malnutrition updateMalnutrition(Malnutrition malnutrition) throws OHServiceException {
		List<OHExceptionMessage> errors = validateMalnutrition(malnutrition);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
		return ioOperation.updateMalnutrition(malnutrition);
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
		return ioOperation.deleteMalnutrition(malnutrition);
	}
}
