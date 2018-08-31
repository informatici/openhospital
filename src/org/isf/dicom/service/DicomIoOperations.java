package org.isf.dicom.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.isf.dicom.model.FileDicom;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

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
public class DicomIoOperations 
{
	/**
	 * Load a list of idfile for series
	 * 
	 * @param idPaziente, the patient id
	 * @param numeroSerie, the series number
	 * @return
	 * @throws OHException 
	 */
	@SuppressWarnings("unchecked")
	public Long[] getSerieDetail(
			int idPaziente, 
			String numeroSerie) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Object> params = new ArrayList<Object>();
		Long[] dicomIdArray = null;	

		try{
			jpa.beginTransaction();

			String query = "SELECT * FROM DICOM" +
					" WHERE DM_PAT_ID = ?" +
					" AND DM_FILE_SER_NUMBER = ?" +
					" ORDER BY DM_FILE_NOME";		
			jpa.createQuery(query, FileDicom.class, false);
			params.add(idPaziente);
			params.add(numeroSerie);
			jpa.setParameters(params, false);
			List<FileDicom> dicomList = (List<FileDicom>)jpa.getList();
			dicomIdArray = new Long[dicomList.size()];	
			for (int i=0; i<dicomList.size(); i++)
			{
				dicomIdArray[i] = dicomList.get(i).getIdFile();
			}

			jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true;

		try{
			jpa.beginTransaction();		
			jpa.createQuery("DELETE FROM DICOM WHERE DM_PAT_ID = ? AND DM_FILE_SER_NUMBER = ?", FileDicom.class, false);
			params.add(idPaziente);
			params.add(numeroSerie);
			jpa.setParameters(params, false);
			jpa.executeUpdate();

			jpa.commitTransaction();	
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
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

		DbJpaUtil jpa = new DbJpaUtil(); 
		FileDicom dicom = null;

		try{
			jpa.beginTransaction();

			dicom = (FileDicom)jpa.find(FileDicom.class, idFile); 

			jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return dicom;
	}

	/**
	 * load metadata from DICOM files stored in database fot the patient
	 * 
	 * @param idPaziente
	 * @return
	 * @throws OHException 
	 */
	@SuppressWarnings("unchecked")
	public FileDicom[] loadFilesPaziente(
			int idPaziente) throws OHException 
	{

		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Object> params = new ArrayList<Object>();
		FileDicom[] dicoms = null;	
				
		try{
			jpa.beginTransaction();

			String query = "SELECT * FROM DICOM" +
					" WHERE DM_PAT_ID = ?" +
					" GROUP BY DM_FILE_SER_INST_UID";		
			jpa.createQuery(query, FileDicom.class, false);
			params.add(idPaziente);
			jpa.setParameters(params, false);
			List<FileDicom> dicomList = (List<FileDicom>)jpa.getList();
			dicoms = new FileDicom[dicomList.size()];	
			for (int i=0; i<dicomList.size(); i++)
			{
				dicoms[i] = dicomList.get(i);
			}

			jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
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

	@SuppressWarnings("unchecked")
	public boolean exist(
			FileDicom dicom) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Object> params = new ArrayList<Object>();
		List<FileDicom> dicomList = null;
				
		try{
			jpa.beginTransaction();

			String query = "SELECT * FROM DICOM" +
					" WHERE DM_PAT_ID = ?" +
					" AND DM_FILE_SER_NUMBER = ?" +
					" AND DM_FILE_INST_UID = ?";		
			jpa.createQuery(query, FileDicom.class, false);
			params.add(dicom.getPatId());
			params.add(dicom.getDicomSeriesNumber());
			params.add(dicom.getDicomInstanceUID());
			jpa.setParameters(params, false);
			dicomList = (List<FileDicom>)jpa.getList();

			jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return (dicomList != null && dicomList.size() > 0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private static void getImagesCounts(
			Vector immagini, 
			DbJpaUtil jpa) throws OHException 
	{
		ArrayList<Object> params = new ArrayList<Object>();
		
		
		if (immagini != null && immagini.size() != 0)
		{	
			try {	
				String serie = "";
				jpa.beginTransaction();
				for (int i = 0; i < immagini.size(); i++) {
					String query = "SELECT * FROM DICOM" +
							   " AND DM_FILE_SER_NUMBER = ?" +
							   " GROUP BY DM_FILE_SER_INST_UID";		
					jpa.createQuery(query, FileDicom.class, false);
					params.add(((FileDicom)immagini.elementAt(i)).getDicomSeriesNumber());
					jpa.setParameters(params, false);
					List<FileDicom> dicomList = (List<FileDicom>)jpa.getList();	
					Iterator<FileDicom> dicomIterator = dicomList.iterator();
					while (dicomIterator.hasNext()) 
					{
						FileDicom dicom = dicomIterator.next();
						dicom.setFrameCount(dicomList.size());						
					}
				}
				jpa.commitTransaction();
			}catch (OHException e) {
				//DbJpaUtil managed exception
				jpa.rollbackTransaction();
				throw e;
			}
		}
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
		DbJpaUtil jpa = new DbJpaUtil(); 

		try{
			jpa.beginTransaction();	
			jpa.merge(dicom);
			jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
    	return;
	}
}
