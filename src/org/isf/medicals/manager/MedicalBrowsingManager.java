/**
 * 19-dec-2005
 * 14-jan-2006
 */
package org.isf.medicals.manager;

import java.util.ArrayList;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.medicals.model.Medical;
import org.isf.medicals.service.MedicalsIoOperations;
import org.isf.medtype.model.MedicalType;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that provides gui separation from database operations and gives some
 * useful logic manipulations of the dinamic data (memory)
 * 
 * @author bob
 * 
 */
public class MedicalBrowsingManager {

	private final Logger logger = LoggerFactory.getLogger(MedicalBrowsingManager.class);
	
	private MedicalsIoOperations ioOperations = Menu.getApplicationContext().getBean(MedicalsIoOperations.class);
	
	/**
	 * Returns the requested medical.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param code the medical code.
	 * @return the retrieved medical.
	 * @throws OHServiceException 
	 */
	public Medical getMedical(int code) throws OHServiceException {
		return ioOperations.getMedical(code);
	}

	/**
	 * Returns all the medicals.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return all the medicals.
	 * @throws OHServiceException 
	 */
	public ArrayList<Medical> getMedicals() throws OHServiceException {
		return ioOperations.getMedicals();
	}

	/**
	 * Returns all the medicals with the specified description.
	 * @param description the medical description.
	 * @return all the medicals with the specified description.
	 * @throws OHServiceException 
	 */
	public ArrayList<Medical> getMedicals(String description) throws OHServiceException {
		return ioOperations.getMedicals(description);
	}

	/**
	 * Return all the medicals with the specified criteria.
	 * @param description the medical description or <code>null</code>
	 * @param type the medical type or <code>null</code>.
	 * @param critical <code>true</code> to include only medicals under critical level.
	 * @return the retrieved medicals.
	 * @throws OHServiceException
	 */
	public ArrayList<Medical> getMedicals(String description, String type, boolean critical) throws OHServiceException {
		return ioOperations.getMedicals(description, type, critical);
	}
	
	/**
	 * Saves the specified {@link Medical}. The medical is updated with the generated id.
	 * In case of wrong parameters values a message error is shown and a <code>false</code> value is returned.
	 * @param medical - the medical to store.
	 * @return <code>true</code> if the medical has been stored, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newMedical(Medical medical) throws OHServiceException {
		return newMedical(medical, false);
	}

	/**
	 * Saves the specified {@link Medical}. The medical is updated with the generated id.
	 * In case of wrong parameters values a message error is shown and a <code>false</code> value is returned.
	 * @param medical - the medical to store.
	 * @param ignoreSimilar - if <code>true</code>, it ignore the warning "similarsFoundWarning".
	 * @return <code>true</code> if the medical has been stored, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newMedical(Medical medical, boolean ignoreSimilar) throws OHServiceException {
		List<OHExceptionMessage> errors = checkMedicalForInsert(medical, ignoreSimilar);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
		return ioOperations.newMedical(medical);
	}
	
	/**
	 * Updates the specified medical.
	 * @param medical - the medical to update.
	 * @return <code>true</code> if update is successful, false if abortIfLocked == true and the record is locked.
	 * 		 Otherwise throws an OHServiceException
	 * @throws OHServiceException 
	 */
	public boolean updateMedical(Medical medical) throws OHServiceException {
		return updateMedical(medical, false);
	}

	/**
	 * Updates the specified medical.
	 * @param medical - the medical to update.
	 * @param ignoreSimilar - if <code>true</code>, it ignore the warning "similarsFoundWarning".
	 * @return <code>true</code> if update is successful, false if abortIfLocked == true and the record is locked.
	 * 		 Otherwise throws an OHServiceException
	 * @throws OHServiceException 
	 */
	public boolean updateMedical(Medical medical, boolean ignoreSimilar) throws OHServiceException {
		List<OHExceptionMessage> errors = checkMedicalForUpdate(medical, ignoreSimilar);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
		int lock = ioOperations.getMedicalLock(medical.getCode());
		if (lock>=0) {
			//ok the record is present, it was not deleted
			if (lock!=medical.getLock()) {
				//error: the record has been updated since last read
				throw new OHServiceException(new OHExceptionMessage("recordUpdatedError", 
						MessageBundle.getMessage("angal.sql.thedatahasbeenupdatedbysomeoneelse"), 
						OHSeverityLevel.ERROR));
			} else {
				//ok it was not updated
				return ioOperations.updateMedical(medical);
			}
		} else {
			//the record was deleted since the last read
			throw new OHServiceException(new OHExceptionMessage("recordDeletedError", 
					MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"), 
					OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Deletes the specified medical.
	 * @param medical the medical to delete.
	 * @return <code>true</code> if the medical has been deleted.
	 * @throws OHServiceException
	 */
	public boolean deleteMedical(Medical medical) throws OHServiceException {
		boolean inStockMovement = ioOperations.isMedicalReferencedInStockMovement(medical.getCode());

		if(inStockMovement){
			throw new OHServiceException(new OHExceptionMessage("existingReferencesError", 
					MessageBundle.getMessage("angal.medicals.therearestockmovementsreferredtothismedical"), 
					OHSeverityLevel.ERROR));
		}
		
		return ioOperations.deleteMedical(medical);
	}
	
	/**
	 * Common checks to validate a {@link Medical} for insert or update
	 * @param medical - the {@link Medical} to insert or update
	 * @return list of {@link OHExceptionMessage}
	 */
	private List<OHExceptionMessage> checkMedicalCommon(Medical medical) {
		List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
		if(medical.getMinqty()<0)
		{
			errors.add(new OHExceptionMessage("quantityLesserThanZeroError", 
					MessageBundle.getMessage("angal.medicals.minquantitycannotbelessthan0"), 
					OHSeverityLevel.ERROR));
		}
		if(medical.getPcsperpck()<0)
		{
			errors.add(new OHExceptionMessage("packagingLesserThanZeroError", 
					MessageBundle.getMessage("angal.medicals.insertavalidpackaging"), 
					OHSeverityLevel.ERROR));
		}
		if(medical.getDescription().equalsIgnoreCase(""))
		{
			errors.add(new OHExceptionMessage("nullOrEmptyDescriptionError", 
					MessageBundle.getMessage("angal.medicals.inseravaliddescription"), 
					OHSeverityLevel.ERROR));
		}
		return errors;
	}
	
	/**
	 * Perform several checks on the provided medical, useful for insert
	 * @param medical - the {@link Medical} to check
	 * @param ignoreSimilar - if <code>true</code>, it will not perform a similiraty check. 
	 * 	{@code warning}: same Medical description in the same {@link MedicalType} category is not allowed anyway
	 * @return <code>true</code> if the {@link Medical} is ok for inserting, <code>false</code> otherwise
	 * @throws OHServiceException
	 */
	private List<OHExceptionMessage> checkMedicalForInsert(Medical medical, boolean ignoreSimilar) throws OHServiceException {
		return checkMedical(medical, ignoreSimilar, false);
	}
	
	/**
	 * Perform several checks on the provided medical, useful for update
	 * @param medical - the {@link Medical} to check
	 * @param ignoreSimilar - if <code>true</code>, it will not perform a similiraty check. 
	 * 	{@code warning}: same Medical description in the same {@link MedicalType} category is not allowed anyway
	 * @return <code>true</code> if the {@link Medical} is ok for updating, <code>false</code> otherwise
	 * @throws OHServiceException
	 */
	public List<OHExceptionMessage> checkMedicalForUpdate(Medical medical, boolean ignoreSimilar) throws OHServiceException {
		return checkMedical(medical, ignoreSimilar, true);
	}
	
	/**
	 * Perform several checks on the provided medical, useful for update
	 * @param medical - the {@link Medical} to check
	 * @param ignoreSimilar - if <code>true</code>, it will not perform a similiraty check. 
	 * 	{@code warning}: same Medical description in the same {@link MedicalType} category is not allowed anyway
	 * @param update - if <code>true</code>, it will not consider the actual {@link Medical}
	 * @return <code>true</code> if the {@link Medical} is ok for updating, <code>false</code> otherwise
	 * @throws OHServiceException
	 */
	public List<OHExceptionMessage> checkMedical(Medical medical, boolean ignoreSimilar, boolean update) throws OHServiceException {
		List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
		
		//check commons
		errors.addAll(checkMedicalCommon(medical));
		
		//check existing data
		boolean productCodeExists = !medical.getProd_code().equals("") && ioOperations.productCodeExists(medical, update);
		boolean medicalExists = ioOperations.medicalExists(medical, update);
		ArrayList<Medical> similarMedicals = ioOperations.medicalCheck(medical, update);
		
		if (productCodeExists) 
		{
			errors.add(new OHExceptionMessage("productCodeExistsError", 
					MessageBundle.getMessage("angal.medicals.thecodeisalreadyused"), 
					OHSeverityLevel.ERROR));
		} 
		else if (medicalExists) 
		{
			StringBuilder message = new StringBuilder(MessageBundle.getMessage("angal.medicals.thepairtypemedicalalreadyexists")).append("\n");
			message.append("[").append(medical.getType().getDescription()).append("] ");
			message.append(medical.toString()).append("\n");
			errors.add(new OHExceptionMessage("pairTypeMedicalExistsError", 
					message.toString(), 
					OHSeverityLevel.ERROR));
		} 
		else if (!ignoreSimilar && !similarMedicals.isEmpty()) 
		{
			StringBuilder message = new StringBuilder(MessageBundle.getMessage("angal.medicals.themedicalyouinsertedseemsalreadyinuse")).append("\n");
			for (Medical med : similarMedicals) {
				message.append("[").append(med.getType().getDescription()).append("] ");
				if (!med.getProd_code().equals("")) message.append("[").append(med.getProd_code()).append("] ");
				message.append(med.toString()).append("\n");
			}
			errors.add(new OHExceptionMessage("similarsFoundWarning", 
					message.toString(), 
					OHSeverityLevel.WARNING));
		};
		return errors;
	}
	
}
