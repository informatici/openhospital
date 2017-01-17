package org.isf.patient.service;

/*------------------------------------------
 * IoOperations - dB operations for the patient entity
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.isf.generaldata.MessageBundle;
import org.isf.patient.model.Patient;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

@Component
public class PatientIoOperations 
{
	/**
	 * method that returns the full list of Patients not logically deleted
	 * 
	 * @return the list of patients
	 * @throws OHException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Patient> getPatients() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Patient> pPatient = null;
				
		
		jpa.beginTransaction();
		
		jpa.createQuery("SELECT * FROM PATIENT WHERE (PAT_DELETED='N' OR PAT_DELETED IS NULL) ORDER BY PAT_NAME", Patient.class, false);
		List<Patient> patients = (List<Patient>)jpa.getList();
		pPatient = new ArrayList<Patient>(patients);			
		
		jpa.commitTransaction();
			
		return pPatient;
	}

	/**
	 * method that returns the full list of Patients not logically deleted with Height and Weight 
	 * 
	 * @param regex
	 * @return the full list of Patients with Height and Weight
	 * @throws OHException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Patient> getPatientsWithHeightAndWeight(
			String regex) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		ArrayList<Patient> pPatient = null;	
		String[] words = _getPatientsWithHeightAndWeightRegex(regex);
		String query = _getPatientsWithHeightAndWeightQuery(words);
		ArrayList<Object> params = _getPatientsWithHeightAndWeightParameters(words);
		
		
		jpa.beginTransaction();		

		jpa.createQuery(query, Patient.class, false);
		jpa.setParameters(params, false);
		List<Patient> patients = (List<Patient>)jpa.getList();
		pPatient = new ArrayList<Patient>(patients);			
		
		jpa.commitTransaction();	
		
		return pPatient;
	}
	
	private String[] _getPatientsWithHeightAndWeightRegex(
			String regex) throws OHException 
	{
		String string = null;
		String[] words = new String[0];
		
		
		if ((regex != null) 
			&& (!regex.equals(""))) 
		{
			string = regex.trim().toLowerCase();
			words = string.split(" ");
		}

		return words;
	}
		
	private String _getPatientsWithHeightAndWeightQuery(
			String[] words) throws OHException 
	{
		StringBuilder queryBld = new StringBuilder("SELECT * FROM PATIENT LEFT JOIN (SELECT PEX_PAT_ID, PEX_HEIGHT AS PAT_HEIGHT, PEX_WEIGHT AS PAT_WEIGHT FROM PATIENTEXAMINATION GROUP BY PEX_PAT_ID ORDER BY PEX_DATE DESC) AS HW ON PAT_ID = HW.PEX_PAT_ID WHERE (PAT_DELETED='N' or PAT_DELETED is null) ");
		
		
		for (int i=0; i<words.length; i++) 
		{
			queryBld.append("AND CONCAT(PAT_ID, LOWER(PAT_SNAME), LOWER(PAT_FNAME), LOWER(PAT_NOTE), LOWER(PAT_TAXCODE)) ");
			queryBld.append("LIKE CONCAT('%', ? , '%') ");
		}
		queryBld.append(" ORDER BY PAT_ID DESC");

		return queryBld.toString();
	}
	
	private ArrayList<Object> _getPatientsWithHeightAndWeightParameters(
			String[] words) throws OHException 
	{
		ArrayList<Object> params = new ArrayList<Object>();
		
		
		for (int i=0; i<words.length; i++) 
		{
			params.add(words[i]);
		}

		return params;
	}

	/**
	 * method that get a Patient by his/her name
	 * 
	 * @param name
	 * @return the Patient that match specified name
	 * @throws OHException
	 */
	@SuppressWarnings("unchecked")
	public Patient getPatient(
			String name) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		ArrayList<Object> params = new ArrayList<Object>();
		Patient patient = null;	
		
		
		jpa.beginTransaction();		

		jpa.createQuery("SELECT * FROM PATIENT WHERE PAT_NAME = ? AND (PAT_DELETED='N' OR PAT_DELETED IS NULL) ORDER BY PAT_SNAME,PAT_FNAME", Patient.class, false);
		params.add(name);
		jpa.setParameters(params, false);
		List<Patient> patients = (List<Patient>)jpa.getList();
		if (patients.size() > 0)
		{			
			patient = patients.get(patients.size()-1);			
		}
		
		jpa.commitTransaction();	
		
		return patient;
	}

	/**
	 * method that get a Patient by his/her ID
	 * 
	 * @param code
	 * @return the Patient
	 * @throws OHException
	 */
	@SuppressWarnings("unchecked")
	public Patient getPatient(
			Integer code) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		ArrayList<Object> params = new ArrayList<Object>();
		Patient patient = null;	
		
		
		jpa.beginTransaction();		

		jpa.createQuery("SELECT * FROM PATIENT WHERE PAT_ID = ? AND (PAT_DELETED='N' OR PAT_DELETED IS NULL)", Patient.class, false);
		params.add(code);
		jpa.setParameters(params, false);
		List<Patient> patients = (List<Patient>)jpa.getList();
		if (patients.size() > 0)
		{			
			patient = patients.get(patients.size()-1);			
		}
		
		jpa.commitTransaction();	
		
		return patient;
	}

	/**
	 * get a Patient by his/her ID, even if he/her has been logically deleted
	 * 
	 * @param code
	 * @return the list of Patients
	 * @throws OHException
	 */
	@SuppressWarnings("unchecked")
	public Patient getPatientAll(
			Integer code) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		ArrayList<Object> params = new ArrayList<Object>();
		Patient patient = null;	
		
		
		jpa.beginTransaction();		

		jpa.createQuery("SELECT * FROM PATIENT WHERE PAT_ID = ?", Patient.class, false);
		params.add(code);
		jpa.setParameters(params, false);
		List<Patient> patients = (List<Patient>)jpa.getList();
		if (patients.size() > 0)
		{			
			patient = patients.get(patients.size()-1);			
		}
		
		jpa.commitTransaction();	
		
		return patient;
	}

	/**
	 * Internal method that prepares the ByteArray for the Image
	 * 
	 * @param anImage
	 * @return
	 */
	private ByteArrayInputStream createPatientPhotoInputStream(
			Image anImage) 
	{
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
			//TODO: handle exception
		} catch (Exception ioe) {
			//TODO: handle exception
		}
		
		return inStream;
	}

	/**
	 * Method that insert a new Patient in the dB
	 * 
	 * @param patient
	 * @return true - if the new Patient has been inserted
	 * @throws OHException
	 */
	public boolean newPatient(
			Patient patient) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		boolean result = true;
		
//		try {
//				ArrayList<Object> params = new ArrayList<Object>();
//				String query = "INSERT INTO PATIENT (PAT_NAME, PAT_FNAME, PAT_SNAME, PAT_BDATE, PAT_AGE, PAT_AGETYPE, PAT_SEX, PAT_ADDR, PAT_CITY, PAT_NEXT_KIN, PAT_TELE, PAT_MOTH_NAME, PAT_MOTH, PAT_FATH_NAME, PAT_FATH, PAT_BTYPE, PAT_ESTA, PAT_PTOGE, PAT_NOTE, PAT_TAXCODE, PAT_PHOTO) " +
//				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//			jpa.createQuery(query, Patient.class, false);
//			params = _addNewPatientParameters(patient);
//			jpa.setParameters(params, false);
//			jpa.executeUpdate();
//			
//		}  catch (OHException e) {
//			result = false;
//			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
//		} finally {
//			jpa.commitTransaction();
//		}
		
		jpa.beginTransaction();	
		jpa.persist(patient);
		jpa.commitTransaction();

		return result;
	}
	
	private ArrayList<Object> _addNewPatientParameters(
			Patient patient) throws OHException 
	{
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

		return params;
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
	public boolean updatePatient(
			Patient patient, 
			boolean check) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		ArrayList<Object> params = new ArrayList<Object>();
		String query = null;
		int lock = 0;
		boolean result = true;
				

		lock = _getUpdatePatientLock(patient.getCode(), check);

		jpa.beginTransaction();		
		
		try {
			query = "UPDATE PATIENT SET PAT_FNAME = ?, PAT_SNAME = ?, PAT_NAME  = ?, PAT_BDATE = ?, PAT_AGE = ?, PAT_AGETYPE = ?, PAT_SEX = ?, PAT_ADDR = ?, PAT_CITY = ?, PAT_NEXT_KIN = ?, PAT_TELE = ?, PAT_MOTH = ?, PAT_MOTH_NAME = ?, PAT_FATH = ?, PAT_FATH_NAME = ?, PAT_BTYPE = ?, PAT_ESTA = ?, PAT_PTOGE = ?, PAT_NOTE = ?, PAT_TAXCODE = ?, PAT_LOCK = ?, PAT_PHOTO = ? WHERE PAT_ID = ?";
			jpa.createQuery(query, Patient.class, false);
			params = _addUpdatePatientParameters(patient, lock);
			jpa.setParameters(params, false);
			jpa.executeUpdate();
			
			_updatePatientLock(patient, check, lock);
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 				
	
		jpa.commitTransaction();			

		return result;
	}
	
	@SuppressWarnings("unchecked")
	private int _getUpdatePatientLock(
			Integer code, 
			boolean check) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		ArrayList<Object> params = new ArrayList<Object>();
		String query = null;
		int lock = 0;

		
		jpa.beginTransaction();		

		if (check == true) 
		{ 
			query = "SELECT * FROM PATIENT WHERE PAT_ID = ?";
			jpa.createQuery(query, Patient.class, false);
			params.add(code);
			jpa.setParameters(params, false);
			List<Patient> patients = (List<Patient>)jpa.getList();
			lock = patients.get(0).getLock();
		}
				
		jpa.commitTransaction();
		
		return lock;
	}
	
	private ArrayList<Object> _addUpdatePatientParameters(
			Patient patient,
			int lock) throws OHException 
	{
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

		return params;
	}
	
	public void _updatePatientLock(
			Patient patient, 
			boolean check,
			int lock) throws OHException 
	{
		if (check == true) 
		{
			patient.setLock(lock + 1);
		}		

		return;
	}

	/**
	 * method that logically delete a Patient (not physically deleted)
	 * 
	 * @param aPatient
	 * @return true - if the Patient has been deleted (logically)
	 * @throws OHException
	 */
	public boolean deletePatient(
			Patient patient) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		ArrayList<Object> params = new ArrayList<Object>();
		String query = null;
		boolean result = true;
				

		jpa.beginTransaction();		
		
		try {
			query = "UPDATE PATIENT SET PAT_DELETED = 'Y' WHERE PAT_ID = ?";
			jpa.createQuery(query, Patient.class, false);
			params.add(patient.getCode());
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
	 * method that check if a Patient is already present in the DB by his/her name
	 * 
	 * @param name
	 * @return true - if the patient is already present
	 * @throws OHException
	 */
	@SuppressWarnings("unchecked")
	public boolean isPatientPresent(
			String name) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		ArrayList<Object> params = new ArrayList<Object>();
		String query = null;
		boolean result = false;
				

		jpa.beginTransaction();		
		
		try {
			query = "SELECT * FROM PATIENT WHERE PAT_NAME = ? AND PAT_DELETED='N'";
			jpa.createQuery(query, Patient.class, false);
			params.add(name);
			jpa.setParameters(params, false);
			List<Patient> patients = (List<Patient>)jpa.getList();
			if (patients.isEmpty() == false)
			{
				result = true;	
			}
		}  catch (OHException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 				
	
		jpa.commitTransaction();

		return result;
	}

	/**
	 * Method that get next PAT_ID is going to be used.
	 * 
	 * @return code
	 * @throws OHException
	 */
	public int getNextPatientCode() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		String query = null;
		Integer code = 0;
				

		jpa.beginTransaction();		
		
		try {
			query = "SELECT MAX(PAT_ID) FROM PATIENT";
			jpa.createQuery(query, null, false);
			code = (Integer)jpa.getResult();
		}  catch (OHException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 				
	
		jpa.commitTransaction();

		return code+1;
	}

	/**
	 * method that merge all clinic details under the same PAT_ID
	 * 
	 * @param mergedPatient
	 * @param patient2
	 * @return true - if no OHExceptions occurred
	 * @throws OHException 
	 */
	public boolean mergePatientHistory(
			Patient mergedPatient, 
			Patient patient2) throws OHException {
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
		
		// EXAMINATION HISTORY
		query = "UPDATE PATIENTEXAMINATION SET PEX_PAT_ID = ? WHERE PEX_PAT_ID = ?";
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
		query = "UPDATE OPD SET OPD_PAT_ID = ?, OPD_AGE = ?, OPD_SEX = ? WHERE OPD_PAT_ID = ?";
		params.clear();
		params.add(mergedID);
		params.add(mergedPatient.getAge());
		params.add(String.valueOf(mergedPatient.getSex()));
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
