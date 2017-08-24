package org.isf.distype.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.distype.model.DiseaseType;
import org.isf.distype.service.DiseaseTypeIoOperation;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager class for DisType module.
 */
public class DiseaseTypeBrowserManager {

	private final Logger logger = LoggerFactory.getLogger(DiseaseTypeBrowserManager.class);
	
	private DiseaseTypeIoOperation ioOperations = Menu.getApplicationContext().getBean(DiseaseTypeIoOperation.class);

	/**
	 * Returns all the stored {@link DiseaseType}s.
	 * @return a list of disease type, <code>null</code> if the operation is failed.
	 * @throws OHServiceException 
	 */
	public ArrayList<DiseaseType> getDiseaseType() throws OHServiceException {
		try {
			return ioOperations.getDiseaseTypes();
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
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Store the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to store.
	 * @return <code>true</code> if the {@link DiseaseType} has been stored, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newDiseaseType(DiseaseType diseaseType) throws OHServiceException {
		try {
			return ioOperations.newDiseaseType(diseaseType);
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
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Updates the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to update.
	 * @return <code>true</code> if the disease type has been updated, false otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateDiseaseType(DiseaseType diseaseType) throws OHServiceException {
		try {
			return ioOperations.updateDiseaseType(diseaseType);
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
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Checks if the specified code is already used by any {@link DiseaseType}.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, false otherwise.
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
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Deletes the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to remove.
	 * @return <code>true</code> if the disease has been removed, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteDiseaseType(DiseaseType diseaseType) throws OHServiceException {
		try {
			return ioOperations.deleteDiseaseType(diseaseType);
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
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}
}
