package org.isf.dicom.manager;

import java.util.Properties;

import org.isf.dicom.model.FileDicom;
import org.isf.dicom.service.DicomIoOperations;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface for definitions IO for Dicom acquired files
 * @author Pietro Castellucci
 * @version 1.0.0 
 */
public class SqlDicomManager implements DicomManagerInterface{   
	
	private final Logger logger = LoggerFactory.getLogger(SqlDicomManager.class);
	
	private DicomIoOperations ioOperations = Menu.getApplicationContext().getBean(DicomIoOperations.class);
	/**
	 * Constructor
	 */
	public SqlDicomManager(Properties externalPrp) {
	}
	
    /**
     * Load a list of idfile for series
     * @param idPaziente, the patient id
     * @param numeroSerie, the series number
     * @return
     * @throws OHServiceException 
     */
    public Long[] getSerieDetail(int idPaziente, String numeroSerie) throws OHServiceException
    {
    	Long[] serieDetails = null;
    	
    	try {
    		serieDetails = ioOperations.getSerieDetail(idPaziente, numeroSerie);
    	}  catch(OHException e){
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
    	
    	return serieDetails;
    }

    /**
     * delete series 
     * @param idPaziente, the id of patient
     * @param numeroSerie, the seres number to delete
     * @return, true if success
     * @throws OHServiceException 
     */
    public boolean deleteSerie(int idPaziente, String numeroSerie) throws OHServiceException 
    {
    	boolean result = false;
    	try {
    		result = ioOperations.deleteSerie(idPaziente, numeroSerie);
    	}  catch(OHException e){
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
    	
    	return result;
    }    
    
    /**
    * ceck if dicom is loaded
    * @param idPaziente, the id of patient
    * @param numeroSerie, the seres number
    * @param dicom, the detail od dicom
    * @return true if file exist
     * @throws OHServiceException 
    */
    public boolean exist(FileDicom dicom) throws OHServiceException
    {
    	boolean result = false;

    	try {
    		result = ioOperations.exist(dicom);
    	}  catch(OHException e){
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
    	
    	return result;
    }

    /**
     * load the Detail of DICOM
     * @param, idFile
     * @return, FileDicomDettaglio
     * @throws OHServiceException 
     */
    public FileDicom loadDettaglio(Long idFile,int idPaziente, String numeroSerie) throws OHServiceException
    {
    	FileDicom dicom = null;
    	
    	try {
			dicom = ioOperations.loadDettaglio(idFile, idPaziente, numeroSerie);
    	}  catch(OHException e){
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
    	
    	return dicom;
    }  
    
    /**
     * Load detail
     * @param idPaziente, the id of patient
     * @param numeroSerie, numero della serie
     * @return, details
     * @throws OHServiceException 
     */
    public FileDicom loadDettaglio(long idFile,int idPaziente, String numeroSerie) throws OHServiceException
    {
    	FileDicom dicom = null;
    	
    	try {
    		dicom = ioOperations.loadDettaglio(idFile, idPaziente, numeroSerie);
    	}  catch(OHException e){
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
    	
    	return dicom;
    }  

    /**
     * load metadata from DICOM files fo the patient
     * @param idPaziente
     * @return
     * @throws OHServiceException 
     */
    public FileDicom[] loadFilesPaziente(int idPaziente) throws OHServiceException
    {
    	FileDicom[] dicoms = null;
    	
    	try {
			dicoms = ioOperations.loadFilesPaziente(idPaziente);
    	}  catch(OHException e){
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
    	
    	return dicoms;
    }  

    /**
     * save the DICOM file and metadata
     * @param dicom
     * @throws OHServiceException 
     */
    public void saveFile(FileDicom dicom) throws OHServiceException
    {
    	try {
			ioOperations.saveFile(dicom);
    	}  catch(OHException e){
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
    }  
}
