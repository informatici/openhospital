package org.isf.patient.service;

/*------------------------------------------
 * IoOperations - db operations for the patient entity
 * -----------------------------------------
 * modification history
 * 05/05/2005 - giacomo  - first beta version 
 * 03/11/2006 - ross - added toString method. Gestione apici per
 *                     nome, cognome, citta', indirizzo e note
 * 11/08/2008 - alessandro - added father & mother's names
 * 26/08/2008 - claudio    - added birth date
 * 							 modififed age
 * 01/01/2009 - Fabrizio   - changed the calls to PAT_AGE fields to
 *                           return again an int type
 * 03/12/2009 - Alex       - added method for merge two patients history
 *------------------------------------------*/

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.isf.generaldata.MessageBundle;
import org.isf.patient.model.Patient;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;

public class IoOperations {

	/**
	 * method that returns the full list of Patients not logically deleted
	 * 
	 * @return the list of patients
	 * @throws OHException
	 */
	public ArrayList<Patient> getPatients() throws OHException {
		ArrayList<Patient> pPatient = null;
		String query = "SELECT * FROM PATIENT WHERE (PAT_DELETED='N' OR PAT_DELETED IS NULL) ORDER BY PAT_NAME";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			pPatient = new ArrayList<Patient>(resultSet.getFetchSize());
			Patient patient;
			while (resultSet.next()) {
				patient = new Patient();
				patient.setCode(resultSet.getInt("PAT_ID"));
				patient.setFirstName(resultSet.getString("PAT_FNAME"));
				patient.setSecondName(resultSet.getString("PAT_SNAME"));
				patient.setAddress(resultSet.getString("PAT_ADDR"));
				patient.setBirthDate(resultSet.getDate("PAT_BDATE"));
				patient.setAge(resultSet.getInt("PAT_AGE"));
				patient.setAgetype(resultSet.getString("PAT_AGETYPE"));
				patient.setSex(resultSet.getString("PAT_SEX").charAt(0));
				patient.setCity(resultSet.getString("PAT_CITY"));
				patient.setTelephone(resultSet.getString("PAT_TELE"));
				patient.setNextKin(resultSet.getString("PAT_NEXT_KIN"));
				patient.setBloodType(resultSet.getString("PAT_BTYPE"));
				patient.setFather(resultSet.getString("PAT_FATH").charAt(0));
				patient.setFather_name(resultSet.getString("PAT_FATH_NAME"));
				patient.setMother(resultSet.getString("PAT_MOTH").charAt(0));
				patient.setMother_name(resultSet.getString("PAT_MOTH_NAME"));
				patient.setHasInsurance(resultSet.getString("PAT_ESTA").charAt(0));
				patient.setParentTogether(resultSet.getString("PAT_PTOGE").charAt(0));
				patient.setNote(resultSet.getString("PAT_NOTE"));
				patient.setTaxCode(resultSet.getString("PAT_TAXCODE"));
				patient.setLock(resultSet.getInt("PAT_LOCK"));
				Blob photoBlob = resultSet.getBlob("PAT_PHOTO");
				if (photoBlob != null) {
					BufferedInputStream is = new BufferedInputStream(photoBlob.getBinaryStream());
					Image image = ImageIO.read(is);
					patient.setPhoto(image);
				} else {
					patient.setPhoto(null);
				}
				pPatient.add(patient);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (IOException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return pPatient;
	}

	/**
	 * method that returns the full list of Patients not logically deleted with Height and Weight 
	 * 
	 * @param regex
	 * @return the full list of Patients with Height and Weight
	 * @throws OHException
	 */
	public ArrayList<Patient> getPatientsWithHeightAndWeight(String regex) throws OHException {
		ArrayList<Patient> pPatient = null;
		ArrayList<Object> params = new ArrayList<Object>();
		// Recupera i pazienti arricchiti con Peso e Altezza
		StringBuilder queryBld = new StringBuilder(
				"SELECT * FROM PATIENT LEFT JOIN (SELECT PEX_PAT_ID, PEX_HEIGHT AS PAT_HEIGHT, PEX_WEIGHT AS PAT_WEIGHT FROM PATIENTEXAMINATION GROUP BY PEX_PAT_ID ORDER BY PEX_DATE DESC) AS HW ON PAT_ID = HW.PEX_PAT_ID WHERE (PAT_DELETED='N' or PAT_DELETED is null) ");

		if (regex != null && !regex.equals("")) {
			String s = regex.trim().toLowerCase();
			String[] s1 = s.split(" ");

			for (int i = 0; i < s1.length; i++) {
				queryBld.append("AND CONCAT(PAT_ID, LOWER(PAT_SNAME), LOWER(PAT_FNAME), LOWER(PAT_NOTE), LOWER(PAT_TAXCODE)) ");
				//queryBld.append("LIKE ? ");
				//params.add("%" + s1[i] + "%");
				queryBld.append("LIKE CONCAT('%', ? , '%') ");
				params.add(s1[i]);
			}
		}
		queryBld.append(" ORDER BY PAT_ID DESC");
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(queryBld.toString(), params, true);
			pPatient = new ArrayList<Patient>(resultSet.getFetchSize());
			Patient patient;
			while (resultSet.next()) {
				patient = new Patient();
				patient.setCode(resultSet.getInt("PAT_ID"));
				patient.setFirstName(resultSet.getString("PAT_FNAME"));
				patient.setSecondName(resultSet.getString("PAT_SNAME"));
				patient.setAddress(resultSet.getString("PAT_ADDR"));
				patient.setBirthDate(resultSet.getDate("PAT_BDATE"));
				patient.setAge(resultSet.getInt("PAT_AGE"));
				patient.setAgetype(resultSet.getString("PAT_AGETYPE"));
				patient.setSex(resultSet.getString("PAT_SEX").charAt(0));
				patient.setCity(resultSet.getString("PAT_CITY"));
				patient.setTelephone(resultSet.getString("PAT_TELE"));
				patient.setNextKin(resultSet.getString("PAT_NEXT_KIN"));
				patient.setBloodType(resultSet.getString("PAT_BTYPE")); // added
				patient.setFather(resultSet.getString("PAT_FATH").charAt(0));
				patient.setFather_name(resultSet.getString("PAT_FATH_NAME"));
				patient.setMother(resultSet.getString("PAT_MOTH").charAt(0));
				patient.setMother_name(resultSet.getString("PAT_MOTH_NAME"));
				patient.setHasInsurance(resultSet.getString("PAT_ESTA").charAt(0));
				patient.setParentTogether(resultSet.getString("PAT_PTOGE").charAt(0));
				patient.setNote(resultSet.getString("PAT_NOTE"));
				patient.setTaxCode(resultSet.getString("PAT_TAXCODE"));
				patient.setHeight(resultSet.getFloat("PAT_HEIGHT"));
				patient.setWeight(resultSet.getFloat("PAT_WEIGHT"));
				patient.setLock(resultSet.getInt("PAT_LOCK"));
				Blob photoBlob = resultSet.getBlob("PAT_PHOTO");

				if (photoBlob != null) {
					BufferedInputStream is = new BufferedInputStream(photoBlob.getBinaryStream());
					Image image = ImageIO.read(is);
					patient.setPhoto(image);
				} else {
					patient.setPhoto(null);
				}

				pPatient.add(patient);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (IOException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return pPatient;
	}

	/**
	 * method that get a Patient by his/her name
	 * 
	 * @param name
	 * @return the Patient that match specified name
	 * @throws OHException
	 */
	public Patient getPatient(String name) throws OHException {
		Patient patient = null;
		String query = "SELECT * FROM PATIENT WHERE PAT_NAME = ? AND (PAT_DELETED='N' OR PAT_DELETED IS NULL) ORDER BY PAT_SNAME,PAT_FNAME";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ArrayList<Object> params = new ArrayList<Object>();
			params.add(name);
			ResultSet resultSet = dbQuery.getDataWithParams(query, params, true);
			patient = new Patient();
			while (resultSet.next()) {
				patient = new Patient();
				patient.setCode(resultSet.getInt("PAT_ID"));
				patient.setFirstName(resultSet.getString("PAT_FNAME"));
				patient.setSecondName(resultSet.getString("PAT_SNAME"));
				patient.setAddress(resultSet.getString("PAT_ADDR"));
				patient.setBirthDate(resultSet.getDate("PAT_BDATE"));
				patient.setAge(resultSet.getInt("PAT_AGE"));
				patient.setAgetype(resultSet.getString("PAT_AGETYPE"));
				patient.setSex(resultSet.getString("PAT_SEX").charAt(0));
				patient.setCity(resultSet.getString("PAT_CITY"));
				patient.setTelephone(resultSet.getString("PAT_TELE"));
				patient.setNextKin(resultSet.getString("PAT_NEXT_KIN"));
				patient.setBloodType(resultSet.getString("PAT_BTYPE")); // added
				patient.setFather(resultSet.getString("PAT_FATH").charAt(0));
				patient.setFather_name(resultSet.getString("PAT_FATH_NAME"));
				patient.setMother(resultSet.getString("PAT_MOTH").charAt(0));
				patient.setMother_name(resultSet.getString("PAT_MOTH_NAME"));
				patient.setHasInsurance(resultSet.getString("PAT_ESTA").charAt(0));
				patient.setParentTogether(resultSet.getString("PAT_PTOGE").charAt(0));
				patient.setNote(resultSet.getString("PAT_NOTE"));
				patient.setTaxCode(resultSet.getString("PAT_TAXCODE"));
				patient.setLock(resultSet.getInt("PAT_LOCK"));
				Blob photoBlob = resultSet.getBlob("PAT_PHOTO");
				// System.out.println("blob size: " + photoBlob.length());
				if (photoBlob != null) {
					BufferedInputStream is = new BufferedInputStream(photoBlob.getBinaryStream());
					Image image = ImageIO.read(is);
					patient.setPhoto(image);
				} else {
					patient.setPhoto(null);
				}
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (IOException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return patient;
	}

	/**
	 * method that get a Patient by his/her ID
	 * 
	 * @param code
	 * @return the Patient
	 * @throws OHException
	 */
	public Patient getPatient(Integer code) throws OHException {
		Patient patient = null;
		String query = "SELECT * FROM PATIENT WHERE PAT_ID = ? AND (PAT_DELETED='N' OR PAT_DELETED IS NULL)";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ArrayList<Object> params = new ArrayList<Object>();
			params.add(code);
			ResultSet resultSet = dbQuery.getDataWithParams(query, params, true);
			patient = new Patient();
			while (resultSet.next()) {
				patient = new Patient();
				patient.setCode(resultSet.getInt("PAT_ID"));
				patient.setFirstName(resultSet.getString("PAT_FNAME"));
				patient.setSecondName(resultSet.getString("PAT_SNAME"));
				patient.setAddress(resultSet.getString("PAT_ADDR"));
				patient.setBirthDate(resultSet.getDate("PAT_BDATE"));
				patient.setAge(resultSet.getInt("PAT_AGE"));
				patient.setAgetype(resultSet.getString("PAT_AGETYPE"));
				patient.setSex(resultSet.getString("PAT_SEX").charAt(0));
				patient.setCity(resultSet.getString("PAT_CITY"));
				patient.setTelephone(resultSet.getString("PAT_TELE"));
				patient.setNextKin(resultSet.getString("PAT_NEXT_KIN"));
				patient.setBloodType(resultSet.getString("PAT_BTYPE")); // added
				patient.setFather(resultSet.getString("PAT_FATH").charAt(0));
				patient.setFather_name(resultSet.getString("PAT_FATH_NAME"));
				patient.setMother(resultSet.getString("PAT_MOTH").charAt(0));
				patient.setMother_name(resultSet.getString("PAT_MOTH_NAME"));
				patient.setHasInsurance(resultSet.getString("PAT_ESTA").charAt(0));
				patient.setParentTogether(resultSet.getString("PAT_PTOGE").charAt(0));
				patient.setNote(resultSet.getString("PAT_NOTE"));
				patient.setTaxCode(resultSet.getString("PAT_TAXCODE"));
				patient.setLock(resultSet.getInt("PAT_LOCK"));
				Blob photoBlob = resultSet.getBlob("PAT_PHOTO");

				if (photoBlob != null) {
					BufferedInputStream is = new BufferedInputStream(photoBlob.getBinaryStream());
					Image image = ImageIO.read(is);
					patient.setPhoto(image);
				} else {
					patient.setPhoto(null);
				}
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (IOException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return patient;
	}

	/**
	 * get a Patient by his/her ID, even if he/her has been logically deleted
	 * 
	 * @param code
	 * @return the list of Patients
	 * @throws OHException
	 */
	public Patient getPatientAll(Integer code) throws OHException {
		Patient patient = null;
		String query = "SELECT * FROM PATIENT WHERE PAT_ID = ?";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ArrayList<Object> params = new ArrayList<Object>();
			params.add(code);
			ResultSet resultSet = dbQuery.getDataWithParams(query, params, true);
			patient = new Patient();
			while (resultSet.next()) {
				patient = new Patient();
				patient.setCode(resultSet.getInt("PAT_ID"));
				patient.setFirstName(resultSet.getString("PAT_FNAME"));
				patient.setSecondName(resultSet.getString("PAT_SNAME"));
				patient.setAddress(resultSet.getString("PAT_ADDR"));
				patient.setBirthDate(resultSet.getDate("PAT_BDATE"));
				patient.setAge(resultSet.getInt("PAT_AGE"));
				patient.setAgetype(resultSet.getString("PAT_AGETYPE"));
				patient.setSex(resultSet.getString("PAT_SEX").charAt(0));
				patient.setCity(resultSet.getString("PAT_CITY"));
				patient.setTelephone(resultSet.getString("PAT_TELE"));
				patient.setNextKin(resultSet.getString("PAT_NEXT_KIN"));
				patient.setBloodType(resultSet.getString("PAT_BTYPE")); // added
				patient.setFather(resultSet.getString("PAT_FATH").charAt(0));
				patient.setFather_name(resultSet.getString("PAT_FATH_NAME"));
				patient.setMother(resultSet.getString("PAT_MOTH").charAt(0));
				patient.setMother_name(resultSet.getString("PAT_MOTH_NAME"));
				patient.setHasInsurance(resultSet.getString("PAT_ESTA").charAt(0));
				patient.setParentTogether(resultSet.getString("PAT_PTOGE").charAt(0));
				patient.setNote(resultSet.getString("PAT_NOTE"));
				patient.setTaxCode(resultSet.getString("PAT_TAXCODE"));
				patient.setLock(resultSet.getInt("PAT_LOCK"));
				Blob photoBlob = resultSet.getBlob("PAT_PHOTO");

				if (photoBlob != null) {
					BufferedInputStream is = new BufferedInputStream(photoBlob.getBinaryStream());
					Image image = ImageIO.read(is);
					patient.setPhoto(image);
				} else {
					patient.setPhoto(null);
				}
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (IOException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return patient;
	}

	/**
	 * Internal method that prepares the ByteArray for the Image
	 * 
	 * @param anImage
	 * @return
	 */
	private ByteArrayInputStream createPatientPhotoInputStream(Image anImage) {

		ByteArrayInputStream inStream = null;
		try {
			// Paint the image onto the buffered image
			BufferedImage bu = new BufferedImage(anImage.getWidth(null), anImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics g = bu.createGraphics();
			g.drawImage(anImage, 0, 0, null);
			g.dispose();
			// Create the ByteArrayOutputStream
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();

			ImageIO.write(bu, "jpg", outStream);
			System.out.println("dimensione output stream: " + outStream.size());
			// Create the ByteArrayInputStream
			inStream = new ByteArrayInputStream(outStream.toByteArray());
		} catch (IOException ioe) {

		} catch (Exception ioe) {

		}
		return inStream;
	}

	/**
	 * methot that insert a new Patient in the db
	 * 
	 * @param patient
	 * @return true - if the new Patient has been inserted
	 * @throws OHException
	 */
	public boolean newPatient(Patient patient) throws OHException {

		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			String query = "INSERT INTO PATIENT (PAT_NAME, PAT_FNAME, PAT_SNAME, PAT_BDATE, PAT_AGE, PAT_AGETYPE, PAT_SEX, PAT_ADDR, PAT_CITY, PAT_NEXT_KIN, PAT_TELE, PAT_MOTH_NAME, PAT_MOTH, PAT_FATH_NAME, PAT_FATH, PAT_BTYPE, PAT_ESTA, PAT_PTOGE, PAT_NOTE, PAT_TAXCODE, PAT_PHOTO) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			ArrayList<Object> params = new ArrayList<Object>();
			params.add(patient.getName());
			params.add(patient.getFirstName());
			params.add(patient.getSecondName());
			params.add(patient.getBirthDate());
			params.add(patient.getAge());
			params.add(patient.getAgetype());
			params.add(String.valueOf(patient.getSex()));
			params.add(patient.getAddress());
			params.add(patient.getCity());
			params.add(patient.getNextKin());
			params.add(patient.getTelephone());
			params.add(patient.getMother_name());
			params.add(String.valueOf(patient.getMother()));
			params.add(patient.getFather_name());
			params.add(String.valueOf(patient.getFather()));
			params.add(patient.getBloodType());
			params.add(String.valueOf(patient.getHasInsurance()));
			params.add(String.valueOf(patient.getParentTogether()));
			params.add(patient.getNote());
			params.add(patient.getTaxCode());
			params.add(createPatientPhotoInputStream(patient.getPhoto()));

			ResultSet r = dbQuery.setDataReturnGeneratedKeyWithParams(query, params, true);

			if (r.first()) {
				patient.setCode(r.getInt(1));
				result = true;
			}

		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * 
	 * method that update an existing {@link Patient} in the db
	 * 
	 * @param patient - the {@link Patient} to update
	 * @param check - if <code>true</code> it will performs an integrity check
	 * @return true - if the existing {@link Patient} has been updated
	 * @throws OHException
	 */
	public boolean updatePatient(Patient patient, boolean check) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		ResultSet set = null;
		int lock = 0;
		try {
			if (check) { 
			
				// we establish if someone else has updated/deleted the record
				// since the last read
				String query = "SELECT PAT_LOCK FROM PATIENT " + " WHERE PAT_ID = ?";
				ArrayList<Object> params = new ArrayList<Object>();
				params.add(patient.getCode());
				set = dbQuery.getDataWithParams(query, params, false); // we use manual commit of the transaction
				if (set.first()) {
					lock = set.getInt("PAT_LOCK");
					// ok the record is present, it was not deleted
					if (lock != patient.getLock()) {
						//the patient has been update by someone else
						return false;
					}
				}
			}

			String query = "UPDATE PATIENT SET PAT_FNAME = ?, PAT_SNAME = ?, PAT_NAME  = ?, PAT_BDATE = ?, PAT_AGE = ?, PAT_AGETYPE = ?, PAT_SEX = ?, PAT_ADDR = ?, PAT_CITY = ?, PAT_NEXT_KIN = ?, PAT_TELE = ?, PAT_MOTH = ?, PAT_MOTH_NAME = ?, PAT_FATH = ?, PAT_FATH_NAME = ?, PAT_BTYPE = ?, PAT_ESTA = ?, PAT_PTOGE = ?, PAT_NOTE = ?, PAT_TAXCODE = ?, PAT_LOCK = ?, PAT_PHOTO = ? WHERE PAT_ID = ?";

			ArrayList<Object> params = new ArrayList<Object>();
			params.add(patient.getFirstName());
			params.add(patient.getSecondName());
			params.add(patient.getName());
			params.add(patient.getBirthDate());
			params.add(patient.getAge());
			params.add(patient.getAgetype());
			params.add(String.valueOf(patient.getSex()));
			params.add(patient.getAddress());
			params.add(patient.getCity());
			params.add(patient.getNextKin());
			params.add(patient.getTelephone());
			params.add(String.valueOf(patient.getMother()));
			params.add(patient.getMother_name());
			params.add(String.valueOf(patient.getFather()));
			params.add(patient.getFather_name());
			params.add(patient.getBloodType());
			params.add(String.valueOf(patient.getHasInsurance()));
			params.add(String.valueOf(patient.getParentTogether()));
			params.add(patient.getNote());
			params.add(patient.getTaxCode());
			params.add(lock + 1);
			params.add(createPatientPhotoInputStream(patient.getPhoto()));
			params.add(patient.getCode());

			result = dbQuery.setDataWithParams(query.toString(), params, true);

			/*
			 * Occorre aggiornare il model perch� il paziente non viene riletto dal DB.
			 */
			if (result)
				patient.setLock(lock + 1);
			else throw new OHException(MessageBundle.getMessage("angal.patient.thedataisnomorepresent")); // the record has been deleted since the last read
				
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * method that logically delete a Patient (not phisically deleted)
	 * 
	 * @param aPatient
	 * @return true - if the Patient has beeb deleted (logically)
	 * @throws OHException
	 */
	public boolean deletePatient(Patient aPatient) throws OHException {

		boolean result = false;
		String sqlString = "UPDATE PATIENT SET PAT_DELETED = 'Y' WHERE PAT_ID = ?";

		ArrayList<Object> params = new ArrayList<Object>();
		params.add(aPatient.getCode());

		// System.out.println(sqlString);
		DbQueryLogger dbQuery = new DbQueryLogger();
		result = dbQuery.setDataWithParams(sqlString, params, true);
		return result;
	}

	/**
	 * method that check if a Patient is already present in the DB by his/her name
	 * 
	 * @param name
	 * @return true - if the patient is already present
	 * @throws OHException
	 */
	public boolean isPatientPresent(String name) throws OHException {
		boolean result = false;
		String string = "SELECT PAT_ID FROM PATIENT WHERE PAT_NAME = ? AND PAT_DELETED='N'";
		DbQueryLogger dbQuery = new DbQueryLogger();

		ArrayList<Object> params = new ArrayList<Object>();
		params.add(name);

		try {
			ResultSet set = dbQuery.getDataWithParams(string, params, true);
			if (set.first()) {
				result = true;
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * methot that get next PAT_ID is going to be used.
	 * 
	 * @return code
	 * @throws OHException
	 */
	public int getNextPatientCode() throws OHException {
		int code = 0;
		String string = "SELECT MAX(PAT_ID) FROM PATIENT";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet set = dbQuery.getData(string, false);
			if (set.first()) {
				code = set.getInt(1) + 1;
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return code;
	}

	/**
	 * method that merge all clinic details under the same PAT_ID
	 * 
	 * @param mergedPatient
	 * @param patient2
	 * @return true - if no OHExceptions occurred
	 * @throws OHException 
	 */
	public boolean mergePatientHistory(Patient mergedPatient, Patient patient2) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		int mergedID = mergedPatient.getCode();
		int obsoleteID = patient2.getCode();
		String query = "";
		ArrayList<Object> params = new ArrayList<Object>();
		
		// ADMISSION HISTORY
		query = "UPDATE ADMISSION SET ADM_PAT_ID = ? WHERE ADM_PAT_ID = ?";
		params.clear();
		params.add(mergedID);
		params.add(obsoleteID);
		dbQuery.setDataWithParams(query, params, false);
		
		// HEIGHT & WEIGHT HISTORY
		query = "UPDATE MALNUTRITIONCONTROL SET MLN_PAT_ID = ? WHERE MLN_PAT_ID = ?";
		params.clear();
		params.add(mergedID);
		params.add(obsoleteID);
		dbQuery.setDataWithParams(query, params, false);
		
		// LABORATORY HISTORY
		query = "UPDATE LABORATORY SET LAB_PAT_ID = ?, LAB_PAT_NAME = ?, LAB_AGE = ?, LAB_SEX = ? WHERE LAB_PAT_ID = ?";
		params.clear();
		params.add(mergedID);
		params.add(mergedPatient.getName());
		params.add(mergedPatient.getAge());
		params.add(String.valueOf(mergedPatient.getSex()));
		params.add(obsoleteID);
		dbQuery.setDataWithParams(query, params, false);

		// OPD HISTORY
		query = "UPDATE OPD SET OPD_PAT_ID = ?, OPD_PAT_FULLNAME = ?, OPD_AGE = ?, OPD_SEX = ?, OPD_PAT_FNAME = ?, OPD_PAT_SNAME = ?, OPD_PAT_NEXT_KIN = ?, OPD_PAT_ADDR = ?, OPD_PAT_CITY = ? WHERE OPD_PAT_ID = ?";
		params.clear();
		params.add(mergedID);
		params.add(mergedPatient.getName());
		params.add(mergedPatient.getAge());
		params.add(String.valueOf(mergedPatient.getSex()));
		params.add(mergedPatient.getFirstName());
		params.add(mergedPatient.getSecondName());
		params.add(mergedPatient.getNextKin());
		params.add(mergedPatient.getAddress());
		params.add(mergedPatient.getCity());
		params.add(obsoleteID);
		dbQuery.setDataWithParams(query, params, false);

		// BILLS HISTORY
		query = "UPDATE BILLS SET BLL_ID_PAT = ?, BLL_PAT_NAME = ? WHERE BLL_ID_PAT = ?";
		params.clear();
		params.add(mergedID);
		params.add(mergedPatient.getName());
		params.add(obsoleteID);
		dbQuery.setDataWithParams(query, params, false);

		// MEDICALDSRSTOCKMOVWARD HISTORY
		query = "UPDATE MEDICALDSRSTOCKMOVWARD SET MMVN_PAT_ID = ? WHERE MMVN_PAT_ID = ?";
		params.clear();
		params.add(mergedID);
		params.add(obsoleteID);
		dbQuery.setDataWithParams(query, params, false);
		
		// THERAPY HISTORY
		query = "UPDATE THERAPIES SET THR_PAT_ID = ? WHERE THR_PAT_ID = ?";
		params.clear();
		params.add(mergedID);
		params.add(obsoleteID);
		dbQuery.setDataWithParams(query, params, false);
		
		// VISITS HISTORY
		query = "UPDATE VISITS SET VST_PAT_ID = ? WHERE VST_PAT_ID = ?";
		params.clear();
		params.add(mergedID);
		params.add(obsoleteID);
		dbQuery.setDataWithParams(query, params, false);
		
		// PATIENTVACCINE HISTORY
		query = "UPDATE PATIENTVACCINE SET PAV_PAT_ID = ? WHERE PAV_PAT_ID = ?";
		params.clear();
		params.add(mergedID);
		params.add(obsoleteID);
		dbQuery.setDataWithParams(query, params, false);
		
		// DELETE OLD PATIENT (patient2)
		query = "UPDATE PATIENT SET PAT_DELETED = 'Y' WHERE PAT_ID = ?";
		params.clear();
		params.add(obsoleteID);
		
		// FINAL CHECK
		boolean result = dbQuery.setDataWithParams(query, params, false);
		if (result) dbQuery.commit();
		else dbQuery.rollback();
		
		return result;
	}
}
