package org.isf.dicom.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.isf.generaldata.MessageBundle;
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
		Long[] dicomIdArray = new Long[dicomList.size()];	
		for (int i=0; i<dicomList.size(); i++)
		{
			dicomIdArray[i] = dicomList.get(i).getIdFile();
		}
		
		jpa.commitTransaction();

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
        		
		
		jpa.beginTransaction();		

		try {
			jpa.createQuery("DELETE FROM DICOM WHERE DM_PAT_ID = ? AND DM_FILE_SER_NUMBER = ?", FileDicom.class, false);
			params.add(idPaziente);
			params.add(numeroSerie);
			jpa.setParameters(params, false);
			jpa.executeUpdate();
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 	

		jpa.commitTransaction();	
		
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
				
		
		jpa.beginTransaction();
		
		dicom = (FileDicom)jpa.find(FileDicom.class, idFile); 
		
		jpa.commitTransaction();

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
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM DICOM" +
					   " WHERE DM_PAT_ID = ?" +
					   " GROUP BY DM_FILE_SER_INST_UID";		
		jpa.createQuery(query, FileDicom.class, false);
		params.add(idPaziente);
		jpa.setParameters(params, false);
		List<FileDicom> dicomList = (List<FileDicom>)jpa.getList();
		FileDicom[] dicoms = new FileDicom[dicomList.size()];	
		for (int i=0; i<dicomList.size(); i++)
		{
			dicoms[i] = dicomList.get(i);
		}
		
		jpa.commitTransaction();

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
		List<FileDicom> dicomList = (List<FileDicom>)jpa.getList();
		
		jpa.commitTransaction();

		return (dicomList.size() > 0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private static void getImagesCounts(
			Vector immagini, 
			DbJpaUtil jpa) 
	{
		ArrayList<Object> params = new ArrayList<Object>();
		
		
		if (immagini != null && immagini.size() != 0)
		{	
			try {	
				String serie = "";
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
			} catch (Exception e) {
				e.printStackTrace();
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
		
		
		jpa.beginTransaction();	
		jpa.merge(dicom);
    	jpa.commitTransaction();
    	
    	return;
	}
}
