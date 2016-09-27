package org.isf.dicom.manager;

import org.isf.dicom.model.FileDicom;
import org.isf.dicom.service.DicomIoOperations;
import org.isf.utils.exception.OHException;

/**
 * Interface for definitions IO for Dicom acquired files
 * @author Pietro Castellucci
 * @version 1.0.0 
 */
public class SqlDicomManager implements DicomManagerInterface
{   
    /**
     * Load a list of idfile for series
     * @param idPaziente, the patient id
     * @param numeroSerie, the series number
     * @return
     */
    public Long[] getSerieDetail(int idPaziente, String numeroSerie)
    {
    	DicomIoOperations ioOperations = new DicomIoOperations();
    	Long[] serieDetails = null;
    	
		
    	try {
    		serieDetails = ioOperations.getSerieDetail(idPaziente, numeroSerie);
		} catch (OHException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return serieDetails;
    }

    /**
     * delete series 
     * @param idPaziente, the id of patient
     * @param numeroSerie, the seres number to delete
     * @return, true if success
     */
    public boolean deleteSerie(int idPaziente, String numeroSerie) 
    {
    	DicomIoOperations ioOperations = new DicomIoOperations();
    	boolean result = false;
    	
		
    	try {
    		result = ioOperations.deleteSerie(idPaziente, numeroSerie);
		} catch (OHException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return result;
    }    
    
    /**
    * ceck if dicom is loaded
    * @param idPaziente, the id of patient
    * @param numeroSerie, the seres number
    * @param dicom, the detail od dicom
    * @return true if file exist
    */
    public boolean exist(FileDicom dicom)
    {
    	DicomIoOperations ioOperations = new DicomIoOperations();
    	boolean result = false;
    	
		
    	try {
    		result = ioOperations.exist(dicom);
		} catch (OHException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return result;
    }

    /**
     * load the Detail of DICOM
     * @param, idFile
     * @return, FileDicomDettaglio
     */
    public FileDicom loadDettaglio(Long idFile,int idPaziente, String numeroSerie)
    {
    	DicomIoOperations ioOperations = new DicomIoOperations();
    	FileDicom dicom = null;
    	
		
    	try {
			dicom = ioOperations.loadDettaglio(idFile, idPaziente, numeroSerie);
		} catch (OHException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return dicom;
    }  
    
    /**
     * Load detail
     * @param idPaziente, the id of patient
     * @param numeroSerie, numero della serie
     * @return, details
     */
    public FileDicom loadDettaglio(long idFile,int idPaziente, String numeroSerie)
    {
    	DicomIoOperations ioOperations = new DicomIoOperations();
    	FileDicom dicom = null;
    	
		
    	try {
    		dicom = ioOperations.loadDettaglio(idFile, idPaziente, numeroSerie);
		} catch (OHException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return dicom;
    }  

    /**
     * load metadata from DICOM files fo the patient
     * @param idPaziente
     * @return
     */
    public FileDicom[] loadFilesPaziente(int idPaziente)
    {
    	DicomIoOperations ioOperations = new DicomIoOperations();
    	FileDicom[] dicoms = null;
    	
		
    	try {
			dicoms = ioOperations.loadFilesPaziente(idPaziente);
		} catch (OHException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return dicoms;
    }  

    /**
     * save the DICOM file and metadata
     * @param dicom
     */
    public void saveFile(FileDicom dicom)
    {
    	DicomIoOperations ioOperations = new DicomIoOperations();
    	
		
    	try {
			ioOperations.saveFile(dicom);
		} catch (OHException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
}
