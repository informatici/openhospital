package org.isf.dicom.service;

import java.util.List;

import org.isf.dicom.model.FileDicom;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manager for hybernate database communication
 * 
 * @author Pietro Castellucci
 * @version 1.0.0
 */
/*------------------------------------------
 * Dicom - IO operations for the DICOM entity
 * -----------------------------------------
 * modification history
 * ? -  Pietro Castellucci - first version 
 * 29/08/2016 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Component
@Transactional
public class DicomIoOperations 
{
	@Autowired
	private DicomIoOperationRepository repository;
	
	/**
	 * Load a list of idfile for series
	 * 
	 * @param idPaziente, the patient id
	 * @param numeroSerie, the series number
	 * @return
	 * @throws OHException 
	 */
	public Long[] getSerieDetail(
			int idPaziente, 
			String numeroSerie) throws OHException 
	{
		List<FileDicom> dicomList  = repository.findAllWhereIdAndNumberByOrderNameAsc((long)idPaziente, numeroSerie);
		Long[] dicomIdArray = new Long[dicomList.size()];	
		
		
		for (int i=0; i<dicomList.size(); i++)
		{
			dicomIdArray[i] = dicomList.get(i).getIdFile();
		}
		
		return dicomIdArray;
	}

	/**
	 * delete series from DB
	 * 
	 * @param idPaziente, the id of patient
	 * @param numeroSerie, the series number to delete
	 * @return true if success
	 * @throws OHException 
	 */
	public boolean deleteSerie(
			int idPaziente, 
			String numeroSerie) throws OHException 
	{
		boolean result = true;
        

		repository.deleteByIdAndNumber((long)idPaziente, numeroSerie);
				
        return result;
	}

	/**
	 * load the Detail of DICOM
	 * 
	 * @param, idFile
	 * @return, FileDicomDettaglio
	 * @throws OHException 
	 */
	public FileDicom loadDettaglio(
			Long idFile, 
			int idPaziente, 
			String numeroSerie) throws OHException 
	{
		if (idFile == null)
			return null;
		else
			return loadDettaglio(idFile.longValue(), idPaziente, numeroSerie);
	}

	/**
	 * Load detail
	 * 
	 * @param idPaziente, the id of patient
	 * @param numeroSerie, numero della serie
	 * @return details
	 * @throws OHException 
	 */
	public FileDicom loadDettaglio(
			long idFile, 
			int idPaziente, 
			String numeroSerie) throws OHException 
	{
		FileDicom dicom = repository.findOne(idFile);
				
		return dicom;
	}

	/**
	 * load metadata from DICOM files stored in database fot the patient
	 * 
	 * @param idPaziente
	 * @return
	 * @throws OHException 
	 */
	public FileDicom[] loadFilesPaziente(
			int idPaziente) throws OHException 
	{
		List<FileDicom> dicomList = repository.findAllWhereIdGroupByUid((long) idPaziente);

		FileDicom[] dicoms = new FileDicom[dicomList.size()];	
		for (int i=0; i<dicomList.size(); i++)
		{
			dicoms[i] = dicomList.get(i);
		}
		
		return dicoms;
	}

	/**
	 * check if dicom is loaded
	 * 
	 * @param idPaziente, the id of patient
	 * @param numeroSerie, the seres number
	 * @param dicom, the detail od dicom
	 * @return true if file exist
	 * @throws OHException 
	 */
	public boolean exist(
			FileDicom dicom) throws OHException 
	{
		List<FileDicom> dicomList = repository.findAllWhereIdAndFileAndUid((long) dicom.getPatId(), dicom.getDicomSeriesNumber(), dicom.getDicomInstanceUID());
	
		return (dicomList.size() > 0);
	}

	/**
	 * save the DICOM file and metadata in the database
	 * 
	 * @param dicom
	 * @throws OHException 
	 */
	public void saveFile(
			FileDicom dicom) throws OHException 
	{
		repository.save(dicom);
		
		return;
	}

	/**
	 * checks if the code is already in use
	 *
	 * @param code - the DICOM code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean isCodePresent(
			Long code) throws OHException
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;	
	}
}
