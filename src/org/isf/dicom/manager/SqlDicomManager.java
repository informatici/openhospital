package org.isf.dicom.manager;

import java.util.Properties;

import org.isf.dicom.model.FileDicom;
import org.isf.dicom.service.DicomIoOperations;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;

/**
 * Interface for definitions IO for Dicom acquired files
 * @author Pietro Castellucci
 * @version 1.0.0 
 */
public class SqlDicomManager implements DicomManagerInterface{   
	
	private DicomIoOperations ioOperations = Context.getApplicationContext().getBean(DicomIoOperations.class);
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
        return ioOperations.getSerieDetail(idPaziente, numeroSerie);
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
    	return ioOperations.deleteSerie(idPaziente, numeroSerie);
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
    	return ioOperations.exist(dicom);
    }

    /**
     * load the Detail of DICOM
     * @param, idFile
     * @return, FileDicomDettaglio
     * @throws OHServiceException 
     */
    public FileDicom loadDettaglio(Long idFile,int idPaziente, String numeroSerie) throws OHServiceException
    {
    	return  ioOperations.loadDettaglio(idFile, idPaziente, numeroSerie);
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
    	return  ioOperations.loadDettaglio(idFile, idPaziente, numeroSerie);
    }

    /**
     * load metadata from DICOM files fo the patient
     * @param idPaziente
     * @return
     * @throws OHServiceException 
     */
    public FileDicom[] loadFilesPaziente(int idPaziente) throws OHServiceException
    {
    	return  ioOperations.loadFilesPaziente(idPaziente);
    }

    /**
     * save the DICOM file and metadata
     * @param dicom
     * @throws OHServiceException 
     */
    public void saveFile(FileDicom dicom) throws OHServiceException
    {
			ioOperations.saveFile(dicom);
    }
}
