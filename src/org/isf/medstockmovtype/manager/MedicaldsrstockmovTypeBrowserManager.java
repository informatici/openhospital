package org.isf.medstockmovtype.manager;

import java.util.ArrayList;

import org.isf.generaldata.MessageBundle;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.medstockmovtype.service.MedicalStockMovementTypeIoOperation;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
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
	 * Returns all the medical stock movement types.
	 * @return all the medical stock movement types.
	 * @throws OHServiceException 
	 */
	public ArrayList<MovementType> getMedicaldsrstockmovType() throws OHServiceException {
		try {
			return ioOperations.getMedicaldsrstockmovType();
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
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Save the specified {@link MovementType}.
	 * @param medicaldsrstockmovType the medical stock movement type to save.
	 * @return <code>true</code> if the medical stock movement type has been saved, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newMedicaldsrstockmovType(MovementType medicaldsrstockmovType) throws OHServiceException {
		try {
			return ioOperations.newMedicaldsrstockmovType(medicaldsrstockmovType);
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
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Updates the specified {@link MovementType}.
	 * @param medicaldsrstockmovType the medical stock movement type to update.
	 * @return <code>true</code> if the medical stock movement type has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateMedicaldsrstockmovType(MovementType medicaldsrstockmovType) throws OHServiceException {
		try {	
			return ioOperations.updateMedicaldsrstockmovType(medicaldsrstockmovType);
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
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Checks if the specified {@link MovementType} code is already used.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, <code>false</code> otherwise.
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
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Deletes the specified {@link MovementType}.
	 * @param medicaldsrstockmovType the medical stock movement type to delete.
	 * @return <code>true</code> if the medical stock movement type has been deleted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteMedicaldsrstockmovType(MovementType medicaldsrstockmovType) throws OHServiceException {
		try {
			return ioOperations.deleteMedicaldsrstockmovType(medicaldsrstockmovType);
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
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

}
