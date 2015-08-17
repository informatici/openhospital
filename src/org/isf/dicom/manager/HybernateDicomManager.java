package org.isf.dicom.manager;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.hibernate.Query;
import org.hibernate.Session;
import org.isf.dicom.model.FileDicomBase;
import org.isf.dicom.model.FileDicomDetail;
import org.isf.utils.db.HybernateSessions;

/**
 * Manager for hybernate database communication
 * 
 * @author Pietro Castellucci
 * @version 1.0.0
 */
public class HybernateDicomManager implements DicomManagerIntf {

	private final static String PRP_FILE = "h8";

	public HybernateDicomManager(Properties externalPrp) {
	}

	/**
	 * Load a list of idfile for series
	 * 
	 * @param idPaziente, the patient id
	 * @param numeroSerie, the series number
	 * @return
	 */
	public Long[] getSerieDetail(int idPaziente, String numeroSerie) {
		Vector<Long> v = new Vector<Long>(0);
		Session mySess = HybernateSessions.getSession(PRP_FILE);
		mySess.beginTransaction();
		Query query = mySess.createQuery("select idFile from FileDicomBase where  DM_PAT_ID = :pid and DM_FILE_SER_NUMBER = :serNumb order by dm_file_nome");
		query.setParameter("pid", idPaziente);
		query.setParameter("serNumb", numeroSerie);
		List<?> list = query.list();
		mySess.getTransaction().commit();
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			Long numero = (Long) it.next();
			v.addElement(numero);
		}
		Long[] rv = new Long[v.size()];
		v.copyInto(rv);
		return rv;
	}

	/**
	 * delete series from DB
	 * 
	 * @param idPaziente, the id of patient
	 * @param numeroSerie, the seres number to delete
	 * @return true if success
	 */
	public boolean deleteSerie(int idPaziente, String numeroSerie) {

		Session mySess = HybernateSessions.getSession(PRP_FILE);
		mySess.beginTransaction();
		Query query = mySess.createQuery("delete from FileDicomBase where  DM_PAT_ID = :pid and DM_FILE_SER_NUMBER = :serNumb");
		query.setParameter("pid", idPaziente);
		query.setParameter("serNumb", numeroSerie);
		int i = query.executeUpdate();
		mySess.getTransaction().commit();
		// System.out.println("DELETE idPaziente "+idPaziente+" numeroSerie "+numeroSerie);
		return (i > 0);
	}

	/**
	 * load the Detail of DICOM
	 * 
	 * @param, idFile
	 * @return, FileDicomDettaglio
	 */
	public FileDicomDetail loadDettaglio(Long idFile, int idPaziente, String numeroSerie) {
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
	 */
	public FileDicomDetail loadDettaglio(long idFile, int idPaziente, String numeroSerie) {

		// System.out.println("Load "+idFile);
		Session mySess = HybernateSessions.getSession(PRP_FILE);
		mySess.beginTransaction();
		Query query = mySess.createQuery("from FileDicomDetail where DM_FILE_ID = :fid");
		query.setParameter("fid", idFile);
		List<?> list = query.list();

		mySess.getTransaction().commit();
		FileDicomDetail dett = null;

		if (list.size() > 0)
			dett = (FileDicomDetail) list.get(0);

		return dett;
	}

	/**
	 * load metadata from DICOM files stored in database fot the patient
	 * 
	 * @param idPaziente
	 * @return
	 */
	public FileDicomBase[] loadFilesPaziente(int idPaziente) {

		Session mySess = HybernateSessions.getSession(PRP_FILE);
		mySess.beginTransaction();

		// select count(DM_FILE_SER_INST_UID),idFile, patId, nomeFile,
		// dicomAccessionNumber, dicomInstitutionName, dicomPatientID,
		// dicomPatientName, dicomPatientAddress, dicomPatientAge,
		// dicomPatientSex, dicomPatientBirthDate, dicomStudyId, dicomStudyDate,
		// dicomStudyDescription, dicomSeriesUID, dicomSeriesInstanceUID,
		// dicomSeriesNumber, dicomSeriesDescriptionCodeSequence,
		// dicomSeriesDate, dicomSeriesDescription, dicomInstanceUID,
		// dicomThumbnail

		Query query = mySess.createQuery("from FileDicomBase where DM_PAT_ID = :pid group by dm_file_ser_inst_uid");
		query.setParameter("pid", idPaziente);

		List<?> list = query.list();

		Vector<?> v = new Vector(list);

		getImagesCounts(v, mySess);
		mySess.getTransaction().commit();

		// System.out.println("v "+v.size());

		// System.out.println("classe "+v.elementAt(0).getClass());

		FileDicomBase[] ret = new FileDicomBase[v.size()];
		v.copyInto(ret);

		return ret;
	}

	/**
	 * ceck if dicom is loaded
	 * 
	 * @param idPaziente, the id of patient
	 * @param numeroSerie, the seres number
	 * @param dicom, the detail od dicom
	 * @return true if file exist
	 */

	public boolean exist(FileDicomDetail dicom) {

		Session mySess = HybernateSessions.getSession(PRP_FILE);
		mySess.beginTransaction();
		Query query = mySess.createQuery("select count(*) from FileDicomBase where DM_PAT_ID = :pid  and DM_FILE_SER_NUMBER = :sn and  DM_FILE_INST_UID = :siu");
		query.setParameter("pid", dicom.getPatId());
		query.setParameter("sn", dicom.getDicomSeriesNumber());
		query.setParameter("siu", dicom.getDicomInstanceUID());
		int count = ((Long) query.uniqueResult()).intValue();
		mySess.getTransaction().commit();

		// System.out.println("exists pid="+ dicom.getPatId()+" sn="+
		// dicom.getDicomSeriesNumber()+" siu="+
		// dicom.getDicomSeriesInstanceUID()+" "+count);

		return (count > 0);
	}

	private static void getImagesCounts(Vector immagini, Session mySess) {
		// System.out.println("getImagesCounts");

		if (immagini == null || immagini.size() == 0)
			return;

		try {

			String serie = "";
			for (int i = 0; i < immagini.size(); i++) {
				serie = serie + ((FileDicomBase) immagini.elementAt(i)).getDicomSeriesNumber() + ",";
			}
			serie = serie.substring(0, serie.length() - 1);
			// System.out.println("serie "+serie);
			Query query = mySess.createQuery("select count(DM_FILE_SER_INST_UID),dicomSeriesNumber from FileDicomBase where DM_FILE_ser_number in (" + serie + ") group by DM_FILE_SER_INST_UID");
			List<?> list = query.list();
			Iterator<?> it = list.iterator();
			while (it.hasNext()) {
				Object[] po = (Object[]) it.next();
				Long numero = (Long) po[0];
				String numeroSerie = (String) po[1];

				// System.out.println(numero+" "+numeroSerie);

				boolean ok = false;
				int ind = 0;
				while (!ok && ind < immagini.size()) {

					FileDicomBase immagine = (FileDicomBase) immagini.elementAt(ind);

					if (numeroSerie.equals(immagine.getDicomSeriesNumber())) {
						immagine.setFrameCount(numero.intValue());
						ok = true;
					}

					ind++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * save the DICOM file and metadata in the database
	 * 
	 * @param dicom
	 */
	public void saveFile(FileDicomDetail dicom) {
		if (exist(dicom))
			return;
		Session mySess = HybernateSessions.getSession(PRP_FILE);
		mySess.beginTransaction();
		mySess.save(dicom);
		mySess.getTransaction().commit();
	}
}
