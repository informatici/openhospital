package org.isf.patient.service;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.isf.patient.model.Patient;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
public class PatientIoOperations 
{
	@Autowired
	private PatientIoOperationRepository repository;
	
	/**
	 * method that returns the full list of Patients not logically deleted
	 * 
	 * @return the list of patients
	 * @throws OHServiceException
	 */
	public ArrayList<Patient> getPatients() throws OHServiceException 
	{
		ArrayList<Patient> pPatient = null;
		
		
		pPatient = new ArrayList<Patient>(repository.findAllWhereDeleted());			
					
		return pPatient;
	}
	
	/**
	 * method that returns the full list of Patients not logically deleted by page
	 * 
	 * @return the list of patients
	 * @throws OHServiceException
	 */
	public ArrayList<Patient> getPatients(Pageable pageable) throws OHServiceException 
	{
		ArrayList<Patient> pPatient = null;
		
		
		pPatient = new ArrayList<Patient>(repository.findAllByDeletedIsNullOrDeletedEqualsOrderByName("N", pageable));
					
		return pPatient;
	}

	/**
	 * method that returns the full list of Patients not logically deleted with Height and Weight 
	 * 
	 * @param regex
	 * @return the full list of Patients with Height and Weight
	 * @throws OHServiceException
	 */
	public ArrayList<Patient> getPatientsWithHeightAndWeight(
			String regex) throws OHServiceException 
	{
		ArrayList<Integer> pPatientCode = null;
		ArrayList<Patient> pPatient = new ArrayList<Patient>();
		
		
		pPatientCode = new ArrayList<Integer>(repository.findAllByHeightAndWeight(regex));			
		for (int i=0; i<pPatientCode.size(); i++)
		{
			Integer code = pPatientCode.get(i);
			Patient patient = repository.findOne(code);
			
			
			pPatient.add(i, patient);
		}
					
		return pPatient;
	}	

	/**
	 * method that get a Patient by his/her name
	 * 
	 * @param name
	 * @return the Patient that match specified name
	 * @throws OHServiceException
	 */
	public Patient getPatient(
			String name) throws OHServiceException 
	{
		ArrayList<Patient> pPatient = null;
		Patient patient = null;	
		
		
		pPatient = new ArrayList<Patient>(repository.findAllWhereNameAndDeletedOrderedByName(name));
		if (pPatient.size() > 0)
		{			
			patient = pPatient.get(pPatient.size()-1);			
		}
					
		return patient;
	}

	/**
	 * method that get a Patient by his/her ID
	 * 
	 * @param code
	 * @return the Patient
	 * @throws OHServiceException
	 */
	public Patient getPatient(
			Integer code) throws OHServiceException 
	{
		ArrayList<Patient> pPatient = null;
		Patient patient = null;	
		
		
		pPatient = new ArrayList<Patient>(repository.findAllWhereIdAndDeleted(code));
		if (pPatient.size() > 0)
		{			
			patient = pPatient.get(pPatient.size()-1);			
		}
					
		return patient;
	}

	/**
	 * get a Patient by his/her ID, even if he/her has been logically deleted
	 * 
	 * @param code
	 * @return the list of Patients
	 * @throws OHServiceException
	 */
	public Patient getPatientAll(
			Integer code) throws OHServiceException 
	{
		ArrayList<Patient> pPatient = null;
		Patient patient = null;	
		
		
		pPatient = new ArrayList<Patient>(repository.findAllWhereId(code));
		if (pPatient.size() > 0)
		{			
			patient = pPatient.get(pPatient.size()-1);			
		}
					
		return patient;
	}

	/**
	 * Method that insert a new Patient in the dB
	 * 
	 * @param patient
	 * @return true - if the new Patient has been inserted
	 * @throws OHServiceException
	 */
	public boolean newPatient(
			Patient patient) throws OHServiceException 
	{
		boolean result = true;
	

		Patient patientVaccine = repository.save(patient);
		result = (patientVaccine != null);
		
		return result;
	}
	
	/**
	 * 
	 * method that update an existing {@link Patient} in the db
	 * 
	 * @param patient - the {@link Patient} to update
	 * @return true - if the existing {@link Patient} has been updated
	 * @throws OHServiceException
	 */
	public boolean updatePatient(
			Patient patient) throws OHServiceException 
	{
		int lock = 0;
		boolean result = false;
				

		if (repository.updateLockByCode(
				patient.getFirstName(), patient.getSecondName(), patient.getName(),
				patient.getBirthDate(), patient.getAge(), patient.getAgetype(), patient.getSex(), patient.getAddress(), patient.getCity(),
				patient.getNextKin(), patient.getTelephone(), patient.getMother(), patient.getMother_name(),
				patient.getFather(), patient.getFather_name(), patient.getBloodType(), patient.getHasInsurance(), patient.getParentTogether(),
				patient.getNote(), patient.getTaxCode(), (lock + 1), _createPatientPhotoInputStream(patient.getPhoto()),
				patient.getCode()) > 0)
		{
			result = true;
		}
	
		return result;
	}
	
	private byte[] _createPatientPhotoInputStream(
			Image anImage) 
	{
		byte[] byteArray = null;
		
		try {
			// Paint the image onto the buffered image
			BufferedImage bu = new BufferedImage(anImage.getWidth(null), anImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics g = bu.createGraphics();
			g.drawImage(anImage, 0, 0, null);
			g.dispose();
			// Create the ByteArrayOutputStream
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();

			ImageIO.write(bu, "jpg", outStream);
			
			if (outStream != null) byteArray = outStream.toByteArray();
			
		} catch (IOException ioe) {
			//TODO: handle exception
		} catch (Exception ioe) {
			//TODO: handle exception
		}
		
		return byteArray;
	}
	
	/**
	 * method that logically delete a Patient (not physically deleted)
	 * 
	 * @param aPatient
	 * @return true - if the Patient has been deleted (logically)
	 * @throws OHServiceException
	 */
	public boolean deletePatient(
			Patient patient) throws OHServiceException 
	{
		boolean result = false;
		int updates = 0;
		
	
		updates = repository.updateDeleted(patient.getCode());
		if (updates > 0)
		{
			result = true;
		} 
		
		return result;
	}

	/**
	 * method that check if a Patient is already present in the DB by his/her name
	 * 
	 * @param name
	 * @return true - if the patient is already present
	 * @throws OHServiceException
	 */
	public boolean isPatientPresent(
			String name) throws OHServiceException 
	{
		boolean result = false;
		
		
		ArrayList<Patient> pPatient = null;
		
		
		pPatient = new ArrayList<Patient>(repository.findAllWhereName(name));
		if (pPatient.size() > 0)
		{			
			result = true;				
		}
					
		return result;
	}

	/**
	 * Method that get next PAT_ID is going to be used.
	 * 
	 * @return code
	 * @throws OHServiceException
	 */
	public int getNextPatientCode() throws OHServiceException 
	{
		Integer code = repository.findMaxCode();

		return (code + 1);
	}

	/**
	 * method that merge all clinic details under the same PAT_ID
	 * 
	 * @param mergedPatient
	 * @param patient2
	 * @return true - if no OHServiceExceptions occurred
	 * @throws OHServiceException 
	 */
	public boolean mergePatientHistory(
			Patient mergedPatient, 
			Patient patient2) throws OHServiceException {
		int mergedID = mergedPatient.getCode();
		int obsoleteID = patient2.getCode();
		boolean result = false;
		int updates = 0;
		
		
		updates = repository.updateAdmission(mergedID, obsoleteID);
		updates += repository.updateExamination(mergedID, obsoleteID);	    
		updates += repository.updateLaboratory(mergedID, mergedPatient.getName(), mergedPatient.getAge(), String.valueOf(mergedPatient.getSex()), obsoleteID);
		updates += repository.updateOpd(mergedID, mergedPatient.getAge(), String.valueOf(mergedPatient.getSex()), obsoleteID);
		updates += repository.updateBill(mergedID, mergedPatient.getName(), obsoleteID);
		updates += repository.updateMedicalStock(mergedID, obsoleteID);
		updates += repository.updateTherapy(mergedID, obsoleteID);
		updates += repository.updateVisit(mergedID, obsoleteID);
		updates += repository.updatePatientVaccine(mergedID, obsoleteID);
		updates += repository.updateDelete(obsoleteID); 	
		if (updates > 0)
		{
			result = true;
		}
		
		
		return result;
	}

	/**
	 * checks if the code is already in use
	 *
	 * @param code - the patient code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean isCodePresent(
			Integer code) throws OHServiceException
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;	
	}
	/**
	 * Get the patient list filter by head patient
	 * @return patient list
	 * @throws OHException
	 */
	
	public ArrayList<Patient> getPatientsHeadWithHeightAndWeight() throws OHException {
		
		 ArrayList<Patient> pPatient = repository.getPatientsHeadWithHeightAndWeight();
		
		
		// Recupera i pazienti arricchiti con Peso e Altezza
		//StringBuilder queryBld = new StringBuilder(
		//		"SELECT * FROM PATIENT LEFT JOIN (SELECT PEX_PAT_ID, PEX_HEIGHT AS PAT_HEIGHT, PEX_WEIGHT AS PAT_WEIGHT FROM PATIENTEXAMINATION GROUP BY PEX_PAT_ID ORDER BY PEX_DATE DESC) AS HW ON PAT_ID = HW.PEX_PAT_ID WHERE (PAT_DELETED='N' or PAT_DELETED is null) ");

		/***** filter only head patient *****/
		//queryBld.append(" AND (PAT_AFFILIATED_PERSON < 1 OR PAT_AFFILIATED_PERSON is null)");
		//queryBld.append(" AND (PAT_IS_HEAD_AFFILIATION = 1)");
		/*** *******/
		/*queryBld.append(" ORDER BY PAT_ID DESC");
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(queryBld.toString(), params, true);
			pPatient = new ArrayList<Patient>(resultSet.getFetchSize());
			Patient patient;
			while (resultSet.next()) {
				patient = this.buildPatient(resultSet);
				pPatient.add(patient);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
		// catch (IOException e) {
		// throw new OHException(
		// MessageBundle
		// .getMessage("angal.sql.problemsoccurredwithserverconnection"),
		// e);
		// }
		finally {
			//dbQuery.releaseConnection();
		} */
		return pPatient;
	}
}
