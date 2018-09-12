package org.isf.disease.manager;

/*------------------------------------------
 * DiseaseBrowserManager - Class that provides gui separation from database operations and gives some
 * 						   useful logic manipulations of the dinamic data (memory)
 * -----------------------------------------
 * modification history
 * 25/01/2006 - Rick, Vero, Pupo  - first beta version 
 * 08/11/2006 - ross - added getDiseaseOpd members, and getDiseaseIpd  
 * 					   to get only opd/ipd related diseases
 *------------------------------------------*/

import java.util.ArrayList;

import org.isf.disease.model.Disease;
import org.isf.disease.service.DiseaseIoOperations;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The manage class for the disease module.
 */
public class DiseaseBrowserManager {

	private final Logger logger = LoggerFactory.getLogger(DiseaseBrowserManager.class);
	
	private DiseaseIoOperations ioOperations = Menu.getApplicationContext().getBean(DiseaseIoOperations.class);

	/**
	 * Returns all the stored {@link Disease} with ODP flag <code>true</code>.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the stored diseases with ODP flag true.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDiseaseOpd() throws OHServiceException {
		try {
			return ioOperations.getDiseases(null,true,false,false);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Returns all diseases, deleted ones also
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the stored diseases.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDiseaseAll() throws OHServiceException {
		try {
			return ioOperations.getDiseases(null,false,false,false);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Returns all the stored {@link Disease} with the specified typecode and flag ODP true.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param typecode the filter typecode.
	 * @return the retrieved diseases.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDiseaseOpd(String typecode) throws OHServiceException {
		try {
			return ioOperations.getDiseases(typecode,true,false,false);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Returns all the stored {@link Disease} with IPD_OUT flag <code>true</code>.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the stored disease with IPD flag <code>true</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDiseaseIpdOut() throws OHServiceException {
		try {
			return ioOperations.getDiseases(null,false,false,true);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Returns all the stored {@link Disease} with the specified typecode and the flag IPD_OUT <code>true</code>.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param typecode the filter typecode.
	 * @return the retrieved diseases.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDiseaseIpdOut(String typecode) throws OHServiceException {
		try {
			return ioOperations.getDiseases(typecode,false,false,true);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * Returns all the stored {@link Disease} with IPD_IN flag <code>true</code>.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the stored disease with IPD flag <code>true</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDiseaseIpdIn() throws OHServiceException {
		try {
			return ioOperations.getDiseases(null,false,true,false);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Returns all the stored {@link Disease} with the specified typecode and the flag IPD_IN <code>true</code>.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param typecode the filter typecode.
	 * @return the retrieved diseases.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDiseaseIpdIn(String typecode) throws OHServiceException {
		try {
			return ioOperations.getDiseases(typecode,false,true,false);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * Returns both OPD and IPDs diseases.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the stored diseases.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDisease() throws OHServiceException {
		try {
			return ioOperations.getDiseases(null,true,true,true);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Retrieves all OPD and IPDs {@link Disease} with the specified typecode.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param typecode the filter typecode.
	 * @return all the diseases with the specified typecode.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDisease(String typecode) throws OHServiceException {
		try {
			return ioOperations.getDiseases(typecode,false,false,false);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Gets a {@link Disease} with the specified code.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param code the disease code.
	 * @return the found disease, <code>null</code> if no disease has found.
	 * @throws OHServiceException 
	 */
	public Disease getDiseaseByCode(int code) throws OHServiceException {
		try {
			return ioOperations.getDiseaseByCode(code);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}		
	}

	/**
	 * Stores the specified {@link Disease}. 
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param disease the disease to store.
	 * @return <code>true</code> if the disease has been stored, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newDisease(Disease disease) throws OHServiceException {
		try {
			return ioOperations.newDisease(disease);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}		
	}
	
	/**
	 * Checks if the specified {@link Disease} has been modified.
	 * @param disease the disease to check.
	 * @return <code>true</code> if has been modified, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurred during the check.
	 */
	public boolean hasDiseaseModified(Disease disease) throws OHServiceException {
		try {
			return ioOperations.hasDiseaseModified(disease);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}	
	}

	/**
	 * Updates the specified {@link Disease}.
	 * If the disease has been updated concurrently a overwrite confirmation message is shown.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param disease the disease to update.
	 * @return <code>true</code> if the disease has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateDisease(Disease disease) throws OHServiceException {
		try {
			return ioOperations.updateDisease(disease);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}	
	}

	/**
	 * Mark as deleted the specified {@link Disease}.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param disease the disease to make delete.
	 * @return <code>true</code> if the disease has been marked, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteDisease(Disease disease) throws OHServiceException {

		try {
			return ioOperations.deleteDisease(disease);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}	
	}

	/**
	 * Check if the specified code is used by other {@link Disease}s.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param code the code to check.
	 * @return <code>true</code> if it is already used, <code>false</code> otherwise.
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}	
	}

	/**
	 * Checks if the specified description is used by a disease with the specified type code.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param description the description to check.
	 * @param typeCode the disease type code.
	 * @return <code>true</code> if is used, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean descriptionControl(String description,String typeCode) throws OHServiceException {
		try {
			return ioOperations.isDescriptionPresent(description,typeCode);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}	
	}

}
