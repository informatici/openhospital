/**
 * 19-dec-2005
 * 14-jan-2006
 */
package org.isf.medicals.manager;

import java.util.ArrayList;

//import javax.swing.JOptionPane;

import org.isf.generaldata.MessageBundle;
import org.isf.medicals.model.Medical;
import org.isf.medicals.service.MedicalsIoOperations;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
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
		try {
			return ioOperations.getMedical(code);
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.medicals.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Returns all the medicals.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return all the medicals.
	 * @throws OHServiceException 
	 */
	public ArrayList<Medical> getMedicals() throws OHServiceException {
		try {
			return ioOperations.getMedicals();
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.medicals.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Returns all the medicals with the specified description.
	 * @param description the medical description.
	 * @return all the medicals with the specified description.
	 * @throws OHServiceException 
	 */
	public ArrayList<Medical> getMedicals(String description) throws OHServiceException {
		try {
			return ioOperations.getMedicals(description);
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.medicals.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Return all the medicals with the specified criteria.
	 * @param description the medical description or <code>null</code>
	 * @param type the medical type or <code>null</code>.
	 * @param expiring <code>true</code> to include only expiring medicals.
	 * @return the retrieved medicals.
	 * @throws OHServiceException
	 */
	public ArrayList<Medical> getMedicals(String description, String type, boolean expiring) throws OHServiceException {
		try {
			return ioOperations.getMedicals(description, type, expiring);
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.medicals.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Saves the specified {@link Medical}. The medical is updated with the generated id.
	 * In case of wrong parameters values a message error is shown and a <code>false</code> value is returned.
	 * @param medical - the medical to store.
	 * @param validate - if <code>true</code> performs validation method checkMedicalForInsert(medical).
	 * @return <code>true</code> if the medical has been stored, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newMedical(Medical medical, boolean validate) throws OHServiceException {
		try {
			if (validate) checkMedicalForInsert(medical);
			return ioOperations.newMedical(medical);
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.medicals.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Updates the specified medical.
	 * @param newMedical - the medical to update.
	 * @param abortIfLocked - if <code>false</code> the {@link Medical} will be overwritten when already updated from someone else.
	 * @return <code>true</code> if update is successful, false if abortIfLocked == true and the record is locked. Otherwise throws an OHServiceException
	 * @throws OHServiceException 
	 */
	public boolean updateMedical(Medical oldMedical, Medical newMedical, boolean abortIfLocked) throws OHServiceException {
		try {
			int lock = ioOperations.getMedicalLock(newMedical.getCode());
			if (lock>=0) {
				//ok the record is present, it was not deleted
				if (lock!=newMedical.getLock()) {
					if (true == abortIfLocked) {
						return false;
					} else {
						return ioOperations.updateMedical(newMedical);
					}
				} else {
					//ok it was not updated
					return ioOperations.updateMedical(newMedical);
				}
			} else {
				//the record was deleted since the last read
				throw new OHException(MessageBundle.getMessage("angal.medicals.couldntfindthedataithasprobablybeendeleted"));
			}
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.medicals.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Deletes the specified medical.
	 * @param medical the medical to delete.
	 * @return <code>true</code> if the medical has been deleted.
	 * @throws OHServiceException
	 */
	public boolean deleteMedical(Medical medical) throws OHServiceException {
		try {
			boolean inStockMovement = ioOperations.isMedicalReferencedInStockMovement(medical.getCode());

			if(inStockMovement){
				throw new OHException(MessageBundle.getMessage("angal.medicals.therearestockmovementsreferredtothismedical"));
			}
			
			return ioOperations.deleteMedical(medical);
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.medicals.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}
	
	private void checkMedicalCommon(Medical medical) throws OHException {
		if(medical.getMinqty()<0){
			throw new OHException(MessageBundle.getMessage("angal.medicals.minquantitycannotbelessthan0"));
		}
		
		if(medical.getPcsperpck()<0){
			throw new OHException(MessageBundle.getMessage("angal.medicals.insertavalidpackaging"));
		}

		if(medical.getDescription().equalsIgnoreCase("")){
			throw new OHException(MessageBundle.getMessage("angal.medicals.inseravaliddescription"));
		}
	}
	
	/**
	 * Perform several checks on the provided medical, useful for insert
	 * @param medical
	 * @return <code>true</code> if the {@link Medical} is ok for inserting, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean checkMedicalForInsert(Medical medical) throws OHException {
		checkMedicalCommon(medical);
		boolean productCodeExists = !medical.getProd_code().equals("") && ioOperations.productCodeExists(medical, false);
		boolean medicalExists = ioOperations.medicalExists(medical, false);
		ArrayList<Medical> similarMedicals = ioOperations.medicalCheck(medical, false);
		if (productCodeExists) {
			throw new OHException(MessageBundle.getMessage("angal.medicals.thecodeisalreadyused"));
		} else if (medicalExists) {
			StringBuilder message = new StringBuilder(MessageBundle.getMessage("angal.medicals.thetypemedicalyouinsertedwasalreadyinuse")).append("\n");
			message.append("[").append(medical.getType().getDescription()).append("] ");
			message.append(medical.toString()).append("\n");
			throw new OHException(message.toString());
		} else if (!similarMedicals.isEmpty()) {
			StringBuilder message = new StringBuilder(MessageBundle.getMessage("angal.medicals.themedicalyouinsertedseemsalreadyinuse")).append("\n");
			for (Medical med : similarMedicals) {
				String prod_code = med.getProd_code() == null || med.getProd_code().equals("") ? MessageBundle.getMessage("angal.common.notapplicable") : med.getProd_code();
				message.append("[").append(med.getType().getDescription()).append("] ");
				if (!med.getProd_code().equals("")) message.append("[").append(prod_code).append("] ");
				message.append(med.toString()).append("\n");
			}
			throw new OHException(message.toString(), new Throwable("similarsFound"));
		} else return true;
	}
	
	/**
	 * Perform several checks on the provided medical, useful for update
	 * @param medical
	 * @return <code>true</code> if the {@link Medical} is ok for updating, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean checkMedicalForUpdate(Medical oldMedical, Medical medical) throws OHException {
		checkMedicalCommon(medical);		
		boolean productCodeExists = !medical.getProd_code().equals("") && ioOperations.productCodeExists(medical, true);
		boolean medicalExists = ioOperations.medicalExists(medical, true);
		ArrayList<Medical> similarMedicals = ioOperations.medicalCheck(medical, true);
		if (productCodeExists) {
			throw new OHException(MessageBundle.getMessage("angal.medicals.thecodeisalreadyused"));
		} else if (medicalExists) {
			StringBuilder message = new StringBuilder(MessageBundle.getMessage("angal.medicals.thetypemedicalyouinsertedwasalreadyinuse")).append("\n");
			message.append("[").append(medical.getType().getDescription()).append("] ");
			message.append(medical.toString()).append("\n");
			throw new OHException(message.toString());
		} else if (!similarMedicals.isEmpty()) {
			StringBuilder message = new StringBuilder(MessageBundle.getMessage("angal.medicals.themedicalyouinsertedseemsalreadyinuse")).append("\n");
			for (Medical med : similarMedicals) {
				message.append("[").append(med.getType().getDescription()).append("] ");
				if (!med.getProd_code().equals("")) message.append("[").append(med.getProd_code()).append("] ");
				message.append(med.toString()).append("\n");
			}
			throw new OHException(message.toString(), new Throwable("similarsFound"));
		} else return true;
	}
	
}
