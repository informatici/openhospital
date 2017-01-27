package org.isf.dicom.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.sql.rowset.serial.SerialBlob;
import javax.validation.constraints.NotNull;


/**
 * Model for contain Detailed DICOM Data
 * 
 * @author Pietro Castellucci
 * @version 1.0.0
 * 
 */
/*------------------------------------------
 * Dicom - model for the DICOM entity
 * -----------------------------------------
 * modification history
 * ? -  Pietro Castellucci - first version 
 * 29/08/2016 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name = "DICOM")
public class FileDicom 
{
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "DM_FILE_ID")
	private long idFile;
	
	@Column(name = "DM_DATA")
	@Lob
	private Blob dicomData;

	@NotNull
	@Column(name="DM_PAT_ID")
	private int patId;

	@NotNull
	@Column(name = "DM_FILE_NOME")
	private String fileName = "";
	
	@Column(name = "DM_FILE_ACCESSION_NUMBER")
	private String dicomAccessionNumber = "";

	@Column(name = "DM_FILE_INSTITUTION_NAME")
	private String dicomInstitutionName = "";

	@Column(name = "DM_FILE_PAT_UID")
	private String dicomPatientID = "";

	@Column(name = "DM_FILE_PAT_NAME")
	private String dicomPatientName = "";

	@Column(name = "DM_FILE_PAT_ADDR")
	private String dicomPatientAddress = "";

	@Column(name = "DM_FILE_PAT_AGE")
	private String dicomPatientAge = "";

	@Column(name = "DM_FILE_PAT_SEX")
	private String dicomPatientSex = "";
	
	@Column(name = "DM_FILE_PAT_BIRTHDATE")
	private String dicomPatientBirthDate = "";

	@NotNull
	@Column(name = "DM_FILE_ST_UID")
	private String dicomStudyId = "";

	@Column(name = "DM_FILE_ST_DATE")
	private String dicomStudyDate = "";

	@Column(name = "DM_FILE_ST_DESCR")
	private String dicomStudyDescription = "";

	@NotNull
	@Column(name = "DM_FILE_SER_UID")
	private String dicomSeriesUID = "";

	@NotNull
	@Column(name = "DM_FILE_SER_INST_UID")
	private String dicomSeriesInstanceUID = "";

	@Column(name = "DM_FILE_SER_NUMBER")
	private String dicomSeriesNumber = "";

	@Column(name = "DM_FILE_SER_DESC_COD_SEQ")
	private String dicomSeriesDescriptionCodeSequence = "";

	@Column(name = "DM_FILE_SER_DATE")
	private String dicomSeriesDate = "";

	@Column(name = "DM_FILE_SER_DESC")
	private String dicomSeriesDescription = "";

	@NotNull
	@Column(name = "DM_FILE_INST_UID")
	private String dicomInstanceUID = "";

	@Column(name = "DM_FILE_MODALIITY")
	private String modality = "";

	@Column(name = "DM_THUMBNAIL")
	@Lob
	private Blob dicomThumbnail;
	

	@Transient
	private int frameCount = -1;
	
	@Transient
	private volatile int hashCode = 0;

	
	/**
	 * Construct an empty Detailed DICOM Data Model
	 */

	public FileDicom() {
		super();
		this.patId = 0;
	}

	/**
	 * Construct an Detailed DICOM Data Model
	 */

	public FileDicom(int patId, Blob dicomData, long idFile, String fileName, String dicomAccessionNumber, String dicomInstitutionName, String dicomPatientID, 
			String dicomPatientName, String dicomPatientAddress, String dicomPatientAge, String dicomPatientSex, String dicomPatientBirthDate, 
			String dicomStudyId, String dicomStudyDate, String dicomStudyDescription, String dicomSeriesUID, String dicomSeriesInstanceUID, 
			String dicomSeriesNumber, String dicomSeriesDescriptionCodeSequence, String dicomSeriesDate, String dicomSeriesDescription, 
			String dicomInstanceUID, String modality, Blob dicomThumbnail) 
	{		
		super();
		this.patId = patId;
		this.dicomData = dicomData;
		this.idFile = idFile;
		this.fileName = fileName;
		this.dicomAccessionNumber = dicomAccessionNumber;
		this.dicomInstitutionName = dicomInstitutionName;
		this.dicomPatientID = dicomPatientID;
		this.dicomPatientName = dicomPatientName;
		this.dicomPatientAddress = dicomPatientAddress;
		this.dicomPatientAge = dicomPatientAge;
		this.dicomPatientSex = dicomPatientSex;
		this.dicomPatientBirthDate = dicomPatientBirthDate;
		this.dicomStudyId = dicomStudyId;
		this.dicomStudyDate = dicomStudyDate;
		this.dicomStudyDescription = dicomStudyDescription;
		this.dicomSeriesUID = dicomSeriesUID;
		this.dicomSeriesInstanceUID = dicomSeriesInstanceUID;
		this.dicomSeriesNumber = dicomSeriesNumber;
		this.dicomSeriesDescriptionCodeSequence = dicomSeriesDescriptionCodeSequence;
		this.dicomSeriesDate = dicomSeriesDate;
		this.dicomSeriesDescription = dicomSeriesDescription;
		this.dicomInstanceUID = dicomInstanceUID;
		this.modality = modality;
		this.dicomThumbnail = dicomThumbnail;
	}
	
	/**
	 * @return the dicomData
	 */
	public Blob getDicomData() {
		return dicomData;
	}

	/**
	 * @param dicomData
	 *            the dicomData to set
	 */
	public void setDicomData(Blob dicomData) {
		this.dicomData = dicomData;
	}

	/**
	 * Load bytes of DICOM file and store it in a Blob type
	 * 
	 * @param dicomData
	 *            the dicomFile to set
	 */
	public void setDicomData(File dicomFile) {
		try {

			FileInputStream fis = new FileInputStream(dicomFile);
			byte[] byteArray = new byte[fis.available()];
			fis.read(byteArray);
			fis.close();
			Blob blob = new SerialBlob(byteArray);
			this.dicomData = blob;

		} catch (Exception ecc) {
			ecc.printStackTrace();
		}
	}

	/**
	 * @return fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the dicomAccessionNumber
	 */
	public String getDicomAccessionNumber() {
		return dicomAccessionNumber;
	}

	/**
	 * @param dicomAccessionNumber
	 *            the dicomAccessionNumber to set
	 */
	public void setDicomAccessionNumber(String dicomAccessionNumber) {
		this.dicomAccessionNumber = dicomAccessionNumber;
	}

	/**
	 * @return the dicomInstitutionName
	 */
	public String getDicomInstitutionName() {
		return dicomInstitutionName;
	}

	/**
	 * @param dicomInstitutionName
	 *            the dicomInstitutionName to set
	 */
	public void setDicomInstitutionName(String dicomInstitutionName) {
		this.dicomInstitutionName = dicomInstitutionName;
	}

	/**
	 * @return the dicomPatientID
	 */
	public String getDicomPatientID() {
		return dicomPatientID;
	}

	/**
	 * @param dicomPatientID
	 *            the dicomPatientID to set
	 */
	public void setDicomPatientID(String dicomPatientID) {
		this.dicomPatientID = dicomPatientID;
	}

	/**
	 * @return the dicomPatientName
	 */
	public String getDicomPatientName() {
		return dicomPatientName;
	}

	/**
	 * @param dicomPatientName
	 *            the dicomPatientName to set
	 */
	public void setDicomPatientName(String dicomPatientName) {
		this.dicomPatientName = dicomPatientName;
	}

	/**
	 * @return the dicomPatientAddress
	 */
	public String getDicomPatientAddress() {
		return dicomPatientAddress;
	}

	/**
	 * @param dicomPatientAddress
	 *            the dicomPatientAddress to set
	 */
	public void setDicomPatientAddress(String dicomPatientAddress) {
		this.dicomPatientAddress = dicomPatientAddress;
	}

	/**
	 * @return the dicomPatientAge
	 */
	public String getDicomPatientAge() {
		return dicomPatientAge;
	}

	/**
	 * @param dicomPatientAge
	 *            the dicomPatientAge to set
	 */
	public void setDicomPatientAge(String dicomPatientAge) {
		this.dicomPatientAge = dicomPatientAge;
	}

	/**
	 * @return the dicomPatientSex
	 */
	public String getDicomPatientSex() {
		return dicomPatientSex;
	}

	/**
	 * @param dicomPatientSex
	 *            the dicomPatientSex to set
	 */
	public void setDicomPatientSex(String dicomPatientSex) {
		this.dicomPatientSex = dicomPatientSex;
	}

	/**
	 * @return the dicomPatientBirthDate
	 */
	public String getDicomPatientBirthDate() {
		return dicomPatientBirthDate;
	}

	/**
	 * @param dicomPatientBirthDate
	 *            the dicomPatientBirthDate to set
	 */
	public void setDicomPatientBirthDate(String dicomPatientBirthDate) {
		this.dicomPatientBirthDate = dicomPatientBirthDate;
	}

	/**
	 * @return the dicomStudyId
	 */
	public String getDicomStudyId() {
		return dicomStudyId;
	}

	/**
	 * @param dicomStudyId
	 *            the dicomStudyId to set
	 */
	public void setDicomStudyId(String dicomStudyId) {
		this.dicomStudyId = dicomStudyId;
	}

	/**
	 * @return the dicomStudyDate
	 */
	public String getDicomStudyDate() {
		return dicomStudyDate;
	}

	/**
	 * @param dicomStudyDate
	 *            the dicomStudyDate to set
	 */
	public void setDicomStudyDate(String dicomStudyDate) {
		this.dicomStudyDate = dicomStudyDate;
	}

	/**
	 * @return the dicomStudyDescription
	 */
	public String getDicomStudyDescription() {
		return dicomStudyDescription;
	}

	/**
	 * @param dicomStudyDescription
	 *            the dicomStudyDescription to set
	 */
	public void setDicomStudyDescription(String dicomStudyDescription) {
		this.dicomStudyDescription = dicomStudyDescription;
	}

	/**
	 * @return the dicomSeriesUID
	 */
	public String getDicomSeriesUID() {
		return dicomSeriesUID;
	}

	/**
	 * @param dicomSeriesUID
	 *            the dicomSeriesUID to set
	 */
	public void setDicomSeriesUID(String dicomSeriesUID) {
		this.dicomSeriesUID = dicomSeriesUID;
	}

	/**
	 * @return the dicomSeriesInstanceUID
	 */
	public String getDicomSeriesInstanceUID() {
		return dicomSeriesInstanceUID;
	}

	/**
	 * @param dicomSeriesInstanceUID
	 *            the dicomSeriesInstanceUID to set
	 */
	public void setDicomSeriesInstanceUID(String dicomSeriesInstanceUID) {
		this.dicomSeriesInstanceUID = dicomSeriesInstanceUID;
	}

	/**
	 * @return the dicomSeriesNumber
	 */
	public String getDicomSeriesNumber() {
		return dicomSeriesNumber;
	}

	/**
	 * @param dicomSeriesNumber
	 *            the dicomSeriesNumber to set
	 */
	public void setDicomSeriesNumber(String dicomSeriesNumber) {
		this.dicomSeriesNumber = dicomSeriesNumber;
	}

	/**
	 * @return the dicomSeriesDescriptionCodeSequence
	 */
	public String getDicomSeriesDescriptionCodeSequence() {
		return dicomSeriesDescriptionCodeSequence;
	}

	/**
	 * @param dicomSeriesDescriptionCodeSequence
	 *            the dicomSeriesDescriptionCodeSequence to set
	 */
	public void setDicomSeriesDescriptionCodeSequence(String dicomSeriesDescriptionCodeSequence) {
		this.dicomSeriesDescriptionCodeSequence = dicomSeriesDescriptionCodeSequence;
	}

	/**
	 * @return the dicomSeriesDate
	 */
	public String getDicomSeriesDate() {
		return dicomSeriesDate;
	}

	/**
	 * @param dicomSeriesDate
	 *            the dicomSeriesDate to set
	 */
	public void setDicomSeriesDate(String dicomSeriesDate) {
		this.dicomSeriesDate = dicomSeriesDate;
	}

	/**
	 * @return the dicomSeriesDescription
	 */
	public String getDicomSeriesDescription() {
		return dicomSeriesDescription;
	}

	/**
	 * @param dicomSeriesDescription
	 *            the dicomSeriesDescription to set
	 */
	public void setDicomSeriesDescription(String dicomSeriesDescription) {
		this.dicomSeriesDescription = dicomSeriesDescription;
	}

	/**
	 * @return the dicomInstanceUID
	 */
	public String getDicomInstanceUID() {
		return dicomInstanceUID;
	}

	/**
	 * @param dicomInstanceUID
	 *            the dicomInstanceUID to set
	 */
	public void setDicomInstanceUID(String dicomInstanceUID) {
		this.dicomInstanceUID = dicomInstanceUID;
	}

	/**
	 * @return the idFile
	 */
	public long getIdFile() {
		return idFile;
	}

	/**
	 * @param idFile
	 *            the idFile to set
	 */
	public void setIdFile(long idFile) {
		this.idFile = idFile;
	}

	/**
	 * @return the patId
	 */
	public int getPatId() {
		return patId;
	}

	/**
	 * @param patId
	 *            the patId to set
	 */
	public void setPatId(int patId) {
		this.patId = patId;
	}

	/**
	 * @return the dicomThumbnail
	 */
	public Blob getDicomThumbnail() {
		return dicomThumbnail;
	}

	/**
	 * @param dicomThumbnail
	 *            the dicomThumbnail to set
	 */
	public void setDicomThumbnail(Blob dicomThumbnail) {
		this.dicomThumbnail = dicomThumbnail;
	}

	/**
	 * Load bytes of Image and store it in a Blob type
	 * 
	 * @param dicomData
	 *            the dicomFile to set
	 */
	public void setDicomThumbnail(BufferedImage dicomThumbnail) {
		try {
			
			byte[] byteArray = null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (ImageIO.write(dicomThumbnail, "JPEG", baos)) {
				byteArray = baos.toByteArray();
			}
			Blob blob = new SerialBlob(byteArray);
			this.dicomThumbnail = blob;

		} catch (Exception ecc) {
			ecc.printStackTrace();
		}
	}

	/**
	 * Convert Blob data in BufferedImage object
	 * 
	 * @return
	 */
	public BufferedImage getDicomThumbnailAsImage() {

		BufferedImage bi = null;
		try {
			bi = ImageIO.read(dicomThumbnail.getBinaryStream());
		} catch (Exception ecc) {
			ecc.printStackTrace();
		}
		return bi;
	}

	/**
	 * @return the modality
	 */
	public String getModality() {
		return modality;
	}

	/**
	 * @param modality
	 *            the modality to set
	 */
	public void setModality(String modality) {
		this.modality = modality;
	}
	
	/**
	 * @return the frameCount
	 */
	public int getFrameCount() {
		return frameCount;
	}

	/**
	 * @param frameCount
	 *            the frameCount to set
	 */
	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}
		
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof FileDicom)) {
			return false;
		}
		
		FileDicom dicom = (FileDicom)obj;
		return (idFile == dicom.getIdFile());
	}
	
	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = (int) (m * c + idFile);
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}	
}
