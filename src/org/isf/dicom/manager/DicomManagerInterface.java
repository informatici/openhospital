package org.isf.dicom.manager;

import org.isf.dicom.model.FileDicom;

/**
 * Interface for definitions IO for Dicom acquired files
 * @author Pietro Castellucci
 * @version 1.0.0 
 */
public interface DicomManagerInterface 
{   
    /**
     * Load a list of idfile for series
     * @param idPaziente, the patient id
     * @param numeroSerie, the series number
     * @return
     */
    public Long[] getSerieDetail(int idPaziente, String numeroSerie);

    /**
     * delete series 
     * @param idPaziente, the id of patient
     * @param numeroSerie, the seres number to delete
     * @return, true if success
     */
    public boolean deleteSerie(int idPaziente, String numeroSerie) ;
    
    /**
    * ceck if dicom is loaded
    * @param idPaziente, the id of patient
    * @param numeroSerie, the seres number
    * @param dicom, the detail od dicom
    * @return true if file exist
    */
    public boolean exist(FileDicom dicom);

    /**
     * load the Detail of DICOM
     * @param, idFile
     * @return, FileDicomDettaglio
     */
    public FileDicom loadDettaglio(Long idFile,int idPaziente, String numeroSerie);
    
    /**
     * Load detail
     * @param idPaziente, the id of patient
     * @param numeroSerie, numero della serie
     * @return, details
     */
    public FileDicom loadDettaglio(long idFile,int idPaziente, String numeroSerie);

    /**
     * load metadata from DICOM files fo the patient
     * @param idPaziente
     * @return
     */
    public FileDicom[] loadFilesPaziente(int idPaziente);

    /**
     * save the DICOM file and metadata
     * @param dicom
     */
    public void saveFile(FileDicom dicom);
}
