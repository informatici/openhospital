package org.isf.dicom.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.sql.rowset.serial.SerialBlob;

/**
 * Model for contain light DICOM Data
 * 
 * @author Pietro Castellucci
 * @version 1.0.0
 * 
 */

public class FileDicomBase {

	private long idFile = -1;
	private int patId = -1;
	private String fileName = "";
	private String dicomAccessionNumber = "";
	private String dicomInstitutionName = "";
	private String dicomPatientID = "";
	private String dicomPatientName = "";
	private String dicomPatientAddress = "";
	private String dicomPatientAge = "";
	private String dicomPatientSex = "";
	private String dicomPatientBirthDate = "";
	private String dicomStudyId = "";
	private String dicomStudyDate = "";
	private String dicomStudyDescription = "";
	private String dicomSeriesUID = "";
	private String dicomSeriesInstanceUID = "";
	private String dicomSeriesNumber = "";
	private String dicomSeriesDescriptionCodeSequence = "";
	private String dicomSeriesDate = "";
	private String dicomSeriesDescription = "";
	private String dicomInstanceUID = "";
	private String modality = "";
	private int frameCount = -1;
	
	@Lob
	@Column(name = "DM_THUMBNAIL")
	private Blob dicomThumbnail;

	public FileDicomBase() {

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
}
