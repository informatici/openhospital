package org.isf.medtype.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.generaldata.MessageBundle;
import org.isf.medtype.model.MedicalType;
import org.isf.medtype.service.MedicalTypeIoOperation;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager class for the medical type module.
 *
 */
public class MedicalTypeBrowserManager {

	private final Logger logger = LoggerFactory.getLogger(MedicalTypeBrowserManager.class);
	
	private MedicalTypeIoOperation ioOperations = Menu.getApplicationContext().getBean(MedicalTypeIoOperation.class);

	/**
	 * Retrieves all the medical types.
	 * @return all the medical types.
	 * @throws OHServiceException 
	 */
	public ArrayList<MedicalType> getMedicalType() throws OHServiceException {
		try {
			return ioOperations.getMedicalTypes();
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
	 * Saves the specified medical type.
	 * @param medicalType the medical type to save.
	 * @return <code>true</code> if the medical type has been saved, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newMedicalType(MedicalType medicalType) throws OHServiceException {
		try {
			return ioOperations.newMedicalType(medicalType);
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
	 * Updates the specified medical type.
	 * @param medicalType the medical type to update.
	 * @return <code>true</code> if the medical type has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateMedicalType(MedicalType medicalType) throws OHServiceException {
		try {
			return ioOperations.updateMedicalType(medicalType);
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
	 * Checks if the specified medical type code is already used.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, <code>false</code> owtherwise.
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
	 * Deletes the specified medical type.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param medicalType the medical type to delete.
	 * @return <code>true</code> if the medical type has been deleted, <code>false</code> otherwise.
	 */
	public boolean deleteMedicalType(MedicalType medicalType) {
		try {
			return ioOperations.deleteMedicalType(medicalType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
}
