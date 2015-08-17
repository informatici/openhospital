package org.isf.dicom.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.sql.Blob;
import java.util.Arrays;
import java.util.Properties;
import java.util.Vector;

import javax.sql.rowset.serial.SerialBlob;
import javax.swing.JOptionPane;

import org.isf.dicom.model.FileDicomBase;
import org.isf.dicom.model.FileDicomDetail;
import org.isf.generaldata.MessageBundle;

/**
 * Manager for filesystem
 * 
 * @author Pietro Castellucci
 * @version 1.0.0
 */
public class FileSystemDicomManager implements DicomManagerIntf {

	private static String NOSERIE = "_NOSERIE";

	/**
	 * Root dir for data storage
	 */
	private File dir = null;
	private filterSerieDetail dsf = new filterSerieDetail();

	/**
	 * Constructor
	 */
	public FileSystemDicomManager(Properties externalPrp) {
		try {
			dir = new File(externalPrp.getProperty("dicom.storage.filesystem"));

			recourse(dir);

		} catch (Exception ecc) {
			JOptionPane.showMessageDialog(null, "FileSystemDicomManager " + MessageBundle.getMessage("angal.dicom.manager.nodir"));

			System.exit(-100);
		}
	}

	/**
	 * Load a list of idfile for series
	 * 
	 * @param idPaziente, the patient id
	 * @param numeroSerie, the series number
	 * @return
	 */
	public Long[] getSerieDetail(int idPaziente, String numeroSerie) {
		try {

			// a volte numero di serie pu� essere NULL, � errato, ma aggiungo
			// questa linea per non creare eccezione
			if (numeroSerie == null || numeroSerie.trim().length() == 0 || numeroSerie.equalsIgnoreCase("null"))
				numeroSerie = NOSERIE;

			// System.out.println("FS getDettaglioSerie "+idPaziente+","+numeroSerie);

			File df = getSerieDir(idPaziente, numeroSerie, false);

			File[] files = df.listFiles(dsf);

			long[] _longs = new long[files.length];

			for (int i = 0; i < _longs.length; i++) {

				try {
					_longs[i] = Long.parseLong(files[i].getName().substring(0, files[i].getName().indexOf(".")));
				} catch (Exception e) {
				}
			}

			Arrays.sort(_longs);

			Long[] _Longs = new Long[_longs.length];

			for (int i = 0; i < _Longs.length; i++) {
				_Longs[i] = new Long(_longs[i]);
				// System.out.println(" getDettaglioSerie("+idPaziente+","+numeroSerie+") = "+_longs[i]);
			}

			return _Longs;
		} catch (Exception ecc) {
			ecc.printStackTrace();
			return new Long[0];
		}
	}

	/**
	 * delete series
	 * 
	 * @param idPaziente
	 *            , the id of patient
	 * @param numeroSerie
	 *            , the seres number to delete
	 * @return, true if success
	 */
	public boolean deleteSerie(int idPaziente, String numeroSerie) {
		try {

			// a volte numero di serie pu� essere NULL, � errato, ma aggiungo
			// questa linea per non creare eccezione
			if (numeroSerie == null || numeroSerie.trim().length() == 0 || numeroSerie.equalsIgnoreCase("null"))
				numeroSerie = NOSERIE;

			// System.out.println("FS deleteSerie "+idPaziente+","+numeroSerie);
			File deleteFolder = getSerieDir(idPaziente, numeroSerie, false);
			File[] f = deleteFolder.listFiles();
			boolean deleted = true;

			for (int i = 0; i < f.length; i++) {
				deleted = deleted && f[i].delete();
				// System.out.println(f[i].getAbsolutePath()+" del "+dl);
			}
			return deleted && deleteFolder.delete();

		} catch (Exception ecc) {
			ecc.printStackTrace();
			return false;
		}
	}

	/**
	 * load the Detail of DICOM
	 * 
	 * @param, idFile
	 * @return, FileDicomDettaglio
	 */
	public FileDicomDetail loadDettaglio(Long idFile, int idPaziente, String numeroSerie) {
		// a volte numero di serie pu� essere NULL, � errato, ma aggiungo questa
		// linea per non creare eccezione
		if (numeroSerie == null || numeroSerie.trim().length() == 0 || numeroSerie.equalsIgnoreCase("null"))
			numeroSerie = NOSERIE;

		if (idFile == null)
			return null;
		else
			return loadDettaglio(idFile.longValue(), idPaziente, numeroSerie);
	}

	/**
	 * Load detail
	 * 
	 * @param idFile
	 * @return, details
	 */
	public FileDicomDetail loadDettaglio(long idFile, int idPaziente, String numeroSerie) {
		try {
			// System.out.println("FS loadDettaglio "+idFile+","+idPaziente+","+numeroSerie);
			return loadData(idFile, idPaziente, numeroSerie);
		} catch (Exception ecc) {
			ecc.printStackTrace();
		}
		return null;
	}

	/**
	 * load metadata from DICOM files fo the patient
	 * 
	 * @param idPaziente
	 * @return
	 */
	public FileDicomBase[] loadFilesPaziente(int idPaziente) {
		try {
			// System.out.println("FS loadFilesPaziente "+idPaziente);
			File df = getPatientDir(idPaziente);
			File[] serie = df.listFiles();
			FileDicomBase[] db = new FileDicomBase[serie.length];

			for (int i = 0; i < serie.length; i++) {
				long idFile = getFirst(serie[i]);
				db[i] = loadMetadata(idFile, idPaziente, serie[i].getName());
			}

			db = compact(db);
			return db;

		} catch (Exception ecc) {
			ecc.printStackTrace();

			return new FileDicomBase[0];
		}
	}

	/**
	 * save the DICOM file and metadata
	 * 
	 * @param dicom
	 */
	public void saveFile(FileDicomDetail dicom) {
		if (exist(dicom))
			return;

		try {
			// System.out.println("FS saveFile");
			int patId = dicom.getPatId();
			String numeroSerie = dicom.getDicomSeriesNumber();

			// a volte numero di serie pu� essere NULL, � errato, ma aggiungo
			// questa linea per non creare eccezione

			if (numeroSerie == null || numeroSerie.trim().length() == 0 || numeroSerie.equalsIgnoreCase("null"))
				numeroSerie = NOSERIE;

			File df = getSerieDir(patId, numeroSerie, true);
			long idFile = nextId();
			File properties = new File(df, idFile + ".properties");
			FileOutputStream fos = new FileOutputStream(properties, false);
			PrintStream ps = new PrintStream(fos);
			ps.println("idFile=" + idFile);
			ps.println("patId=" + patId);
			ps.println("fileName=" + dicom.getFileName());
			ps.println("dicomAccessionNumber=" + dicom.getDicomAccessionNumber());
			ps.println("dicomInstitutionName=" + dicom.getDicomInstitutionName());
			ps.println("dicomPatientID=" + dicom.getDicomPatientID());
			ps.println("dicomPatientName=" + dicom.getDicomPatientName());
			ps.println("dicomPatientAddress=" + dicom.getDicomPatientAddress());
			ps.println("dicomPatientAge=" + dicom.getDicomPatientAge());
			ps.println("dicomPatientSex=" + dicom.getDicomPatientSex());
			ps.println("dicomPatientBirthDate=" + dicom.getDicomPatientBirthDate());
			ps.println("dicomStudyId=" + dicom.getDicomStudyId());
			ps.println("dicomStudyDate=" + dicom.getDicomStudyDate());
			ps.println("dicomStudyDescription=" + dicom.getDicomStudyDescription());
			ps.println("dicomSeriesUID=" + dicom.getDicomSeriesUID());
			ps.println("dicomSeriesInstanceUID=" + dicom.getDicomSeriesInstanceUID());
			ps.println("dicomSeriesNumber=" + dicom.getDicomSeriesNumber());
			ps.println("dicomSeriesDescriptionCodeSequence=" + dicom.getDicomSeriesDescriptionCodeSequence());
			ps.println("dicomSeriesDate=" + dicom.getDicomSeriesDate());
			ps.println("dicomSeriesDescription=" + dicom.getDicomSeriesDescription());
			ps.println("dicomInstanceUID=" + dicom.getDicomInstanceUID());
			ps.println("modality=" + dicom.getModality());
			
			ps.flush();
			ps.close();
			fos.close();

			File data = new File(df, idFile + ".data");
			Blob blob = dicom.getDicomData();
			int blobLength = (int) blob.length();
			byte[] blobAsBytes = blob.getBytes(1, blobLength);
			save(data, blobAsBytes);
			File thumn = new File(df, idFile + ".thumn");
			blob = dicom.getDicomThumbnail();
			blobLength = (int) blob.length();
			blobAsBytes = blob.getBytes(1, blobLength);
			save(thumn, blobAsBytes);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private FileDicomBase loadMetadata(long idFile, int idPaziente, String serie) throws IOException {
		// System.out.println("loadMetadata "+idFile+","+idPaziente+","+serie);
		FileDicomBase rv = null;

		try {
			rv = new FileDicomBase();
			File sd = getSerieDir(idPaziente, serie, false);
			rv.setFrameCount(getFramesCount(idPaziente, serie));
			Properties p = loadMetadata(sd, idFile);
			try {
				rv.setIdFile(Long.parseLong(p.getProperty("idFile")));
			} catch (Exception e) {
			}

			try {
				rv.setPatId(Integer.parseInt(p.getProperty("patId")));
			} catch (Exception e) {
			}
			rv.setFileName(p.getProperty("fileName"));
			rv.setDicomAccessionNumber(p.getProperty("dicomAccessionNumber"));
			rv.setDicomInstitutionName(p.getProperty("dicomInstitutionName"));
			rv.setDicomPatientID(p.getProperty("dicomPatientID"));
			rv.setDicomPatientName(p.getProperty("dicomPatientName"));
			rv.setDicomPatientAddress(p.getProperty("dicomPatientAddress"));
			rv.setDicomPatientAge(p.getProperty("dicomPatientAge"));
			rv.setDicomPatientSex(p.getProperty("dicomPatientSex"));
			rv.setDicomPatientBirthDate(p.getProperty("dicomPatientBirthDate"));
			rv.setDicomStudyId(p.getProperty("dicomStudyId"));
			rv.setDicomStudyDate(p.getProperty("dicomStudyDate"));
			rv.setDicomStudyDescription(p.getProperty("dicomStudyDescription"));
			rv.setDicomSeriesUID(p.getProperty("dicomSeriesUID"));
			rv.setDicomSeriesInstanceUID(p.getProperty("dicomSeriesInstanceUID"));
			rv.setDicomSeriesNumber(p.getProperty("dicomSeriesNumber"));
			rv.setDicomSeriesDescriptionCodeSequence(p.getProperty("dicomSeriesDescriptionCodeSequence"));
			rv.setDicomSeriesDate(p.getProperty("dicomSeriesDate"));
			rv.setDicomSeriesDescription(p.getProperty("dicomSeriesDescription"));
			rv.setDicomInstanceUID(p.getProperty("dicomInstanceUID"));
			rv.setModality(p.getProperty("modality"));
			rv.setDicomThumbnail(loadThumbnail(sd, idFile));
		} catch (Exception e) {
			rv = null;
		}
		return rv;
	}

	private FileDicomDetail loadData(long idFile, int idPaziente, String serie) throws IOException {
		// a volte numero di serie pu� essere NULL, � errato, ma aggiungo questa
		// linea per non creare eccezione
		if (serie == null || serie.trim().length() == 0 || serie.equalsIgnoreCase("null"))
			serie = NOSERIE;

		// System.out.println("loadData "+idFile+","+idPaziente+","+serie);
		FileDicomDetail rv = new FileDicomDetail();
		File sd = getSerieDir(idPaziente, serie, false);

		Properties p = loadMetadata(sd, idFile);
		try {
			rv.setIdFile(Long.parseLong(p.getProperty("idFile")));
		} catch (Exception e) {
		}
		try {
			rv.setPatId(Integer.parseInt(p.getProperty("patId")));
		} catch (Exception e) {
		}
		rv.setFileName(p.getProperty("fileName"));
		rv.setDicomAccessionNumber(p.getProperty("dicomAccessionNumber"));
		rv.setDicomInstitutionName(p.getProperty("dicomInstitutionName"));
		rv.setDicomPatientID(p.getProperty("dicomPatientID"));
		rv.setDicomPatientName(p.getProperty("dicomPatientName"));
		rv.setDicomPatientAddress(p.getProperty("dicomPatientAddress"));
		rv.setDicomPatientAge(p.getProperty("dicomPatientAge"));
		rv.setDicomPatientSex(p.getProperty("dicomPatientSex"));
		rv.setDicomPatientBirthDate(p.getProperty("dicomPatientBirthDate"));
		rv.setDicomStudyId(p.getProperty("dicomStudyId"));
		rv.setDicomStudyDate(p.getProperty("dicomStudyDate"));
		rv.setDicomStudyDescription(p.getProperty("dicomStudyDescription"));
		rv.setDicomSeriesUID(p.getProperty("dicomSeriesUID"));
		rv.setDicomSeriesInstanceUID(p.getProperty("dicomSeriesInstanceUID"));
		rv.setDicomSeriesNumber(p.getProperty("dicomSeriesNumber"));
		rv.setDicomSeriesDescriptionCodeSequence(p.getProperty("dicomSeriesDescriptionCodeSequence"));
		rv.setDicomSeriesDate(p.getProperty("dicomSeriesDate"));
		rv.setDicomSeriesDescription(p.getProperty("dicomSeriesDescription"));
		rv.setDicomInstanceUID(p.getProperty("dicomInstanceUID"));
		rv.setModality(p.getProperty("modality"));
		rv.setDicomData(loadDicomData(sd, idFile));
		return rv;
	}

	/**
	 * load image for thumnail
	 */
	private Blob loadThumbnail(File sd, long idFile) throws IOException {
		// System.out.println("loadThumbnail "+sd.getAbsolutePath()+","+idFile);
		try {
			File fdc = new File(sd, idFile + ".thumn");
			FileInputStream fis = new FileInputStream(fdc);
			byte[] byteArray = new byte[fis.available()];
			fis.read(byteArray);
			fis.close();
			Blob blob = new SerialBlob(byteArray);
			return blob;
		} catch (Exception ecc) {
			ecc.printStackTrace();
			return null;
		}
	}

	/**
	 * load image for thumnail
	 */
	private Blob loadDicomData(File sd, long idFile) throws IOException {
		// System.out.println("loadDicomData "+sd.getAbsolutePath()+","+idFile);
		try {
			File fdc = new File(sd, idFile + ".data");
			FileInputStream fis = new FileInputStream(fdc);
			byte[] byteArray = new byte[fis.available()];
			fis.read(byteArray);
			fis.close();
			Blob blob = new SerialBlob(byteArray);
			return blob;
		} catch (Exception ecc) {
			ecc.printStackTrace();
			return null;
		}
	}

	public boolean exist(FileDicomDetail dicom) {
		// System.out.println("exists "+dicom.getPatId()+" - "+dicom.getDicomSeriesNumber()+" - "+
		// dicom.getDicomSeriesInstanceUID());
		boolean rv = false;
		try {

			int patId = dicom.getPatId();
			String serieNumber = dicom.getDicomSeriesNumber();
			String diuid = dicom.getDicomInstanceUID();
			if (serieNumber == null || serieNumber.trim().length() == 0 || serieNumber.equalsIgnoreCase("null"))
				serieNumber = NOSERIE;

			File df = getSerieDir(patId, serieNumber, true);
			File[] files = df.listFiles();
			int i = 0;
			while (!rv && i < files.length) {
				String nf = files[i].getName();
				// System.out.println(files[i].getAbsolutePath());
				if (nf.endsWith(".properties")) {
					Properties p = loadMetadata(files[i]);
					String vl = p.getProperty("dicomInstanceUID");
					rv = diuid.equals(vl);
					// System.out.println("diuid "+diuid+" == "+vl);
				}
				i++;
			}
		} catch (Exception ecc) {
		}
		return rv;
	}

	private Properties loadMetadata(File sd, long idFile) throws IOException {

		Properties p = new Properties();
		FileReader fr = new FileReader(new File(sd, idFile + ".properties"));
		p.load(fr);
		fr.close();
		return p;
	}

	private Properties loadMetadata(File mdf) throws IOException {
		Properties p = new Properties();
		FileReader fr = new FileReader(mdf);
		p.load(fr);
		fr.close();
		return p;
	}

	/**
	 * frames counter
	 */
	private int getFramesCount(int patID, String serieNumber) throws IOException {
		File df = getSerieDir(patID, serieNumber, false);
		File[] files = df.listFiles(dsf);
		return files.length;
	}

	/**
	 * emulate SQL sequence on filesystem
	 */
	private synchronized long nextId() throws IOException {
		long rv = 0;

		try {

			File i = new File(dir, "dicom.storage");
			if (i.exists()) {
				FileInputStream fis = new FileInputStream(i);
				ObjectInputStream ois = new ObjectInputStream(fis);
				rv = ois.readLong();
				ois.close();
				fis.close();
			}

			i.delete();

			i = new File(dir, "dicom.storage");

			rv++;

			FileOutputStream fos = new FileOutputStream(i);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeLong(rv);
			oos.flush();
			fos.flush();
			oos.close();
			fos.close();

		} catch (Exception ecc) {

		}
		return rv;
	}

	/**
	 * retrieve patient's serie folder
	 */
	private File getSerieDir(int patId, String serie, boolean recourse) throws IOException {
		File fm = getPatientDir(patId);

		// System.out.println(fm.getAbsolutePath()+" "+serie);

		File f = new File(fm, serie);

		if (recourse)
			recourse(f);

		return f;
	}

	/**
	 * retrieve patient folder
	 */
	private File getPatientDir(int patId) throws IOException {
		File f = new File(dir, "" + patId);
		recourse(f);
		return f;
	}

	/**
	 * 
	 * recorsive function for create folder structure if missing
	 */
	private void recourse(File f) throws IOException {
		if (f.exists())
			return;
		// System.out.println("recourse "+f.getAbsolutePath());
		File fp = f.getParentFile();

		if (fp != null)
			recourse(fp);

		if (!f.exists())
			f.mkdir();
	}

	/**
	 * save content in specified file
	 * 
	 * @param outFile - the file to write into
	 * @param content - byte vector to write
	 */
	private void save(File outFile, byte[] content) throws IOException {
		// System.out.println("FileSystemDicomManager: creo "+outFile.getAbsolutePath());
		FileOutputStream fos = new FileOutputStream(outFile);
		fos.write(content);
		fos.flush();
		fos.close();
	}

	/**
	 * return first idFile in the serie
	 */
	private long getFirst(File df) {
		File[] files = df.listFiles(dsf);

		long[] _longs = new long[files.length];

		for (int i = 0; i < _longs.length; i++) {
			try {
				_longs[i] = Long.parseLong(files[i].getName().substring(0, files[i].getName().indexOf(".")));
			} catch (Exception e) {
			}
		}

		Arrays.sort(_longs);

		if (_longs.length > 0)
			return _longs[0];
		else
			return -1;
	}

	private FileDicomBase[] compact(FileDicomBase[] db) {
		Vector<FileDicomBase> rv = new Vector<FileDicomBase>(0);

		for (int i = 0; i < db.length; i++)
			if (db[i] != null)
				rv.addElement(db[i]);

		FileDicomBase[] ret = new FileDicomBase[rv.size()];

		rv.copyInto(ret);

		return ret;
	}

	/**
	 * Filter for files .thumn
	 */
	class filterSerieDetail implements FilenameFilter {

		public boolean accept(File dir, String name) {
			if (name == null)
				return false;
			return name.endsWith(".thumn");
		}
	}
}
