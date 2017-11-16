package org.isf.patient.service;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHException;


public class PatientIoOperationRepositoryImplementation implements PatientIoOperationRepositoryCustom{
	
	@PersistenceContext
	private EntityManager entityManager;

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Patient> findAllPatientsWithHeightAndWeight(String regex) throws OHException {
		return this.entityManager.
				createQuery(_getPatientsWithHeightAndWeightQueryByRegex(regex)).
					getResultList();
	}
	
	public void updatePatientLock(Patient patient, int lock) throws OHException {
		this.entityManager.
			createQuery(_getPatientLockQuery(patient, lock)).
			executeUpdate();
		
		return;
	}
	

	
	private String _getPatientsWithHeightAndWeightQueryByRegex(
			String regex) throws OHException 
	{
		String[] words = _getPatientsWithHeightAndWeightRegex(regex);
		String query = _getPatientsWithHeightAndWeightQuery(words);
		
		
		return query;
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
			queryBld.append("AND CONCAT_WS(PAT_ID, LOWER(PAT_SNAME), LOWER(PAT_FNAME), LOWER(PAT_NOTE), LOWER(PAT_TAXCODE)) ");
			queryBld.append("LIKE CONCAT('%', " + words[i] + " , '%') ");
		}
		queryBld.append(" ORDER BY PAT_ID DESC");

		return queryBld.toString();
	}
	
	private String _getPatientLockQuery(
			Patient patient,
			int lock) throws OHException 
	{
		String query = "UPDATE PATIENT SET "
				+ "PAT_FNAME = " + patient.getFirstName() + ", "
				+ "PAT_SNAME = " + patient.getSecondName() + ", "
				+ "PAT_NAME  = " + patient.getName() + ", "
				+ "PAT_BDATE = " + patient.getBirthDate() + ", "
				+ "PAT_AGE = " + patient.getAge() + ", "
				+ "PAT_AGETYPE = " + patient.getAgetype() + ", "
				+ "PAT_SEX = " + String.valueOf(patient.getSex()) + ", "
				+ "PAT_ADDR = " + patient.getAddress() + ", "
				+ "PAT_CITY = " + patient.getCity() + ", "
				+ "PAT_NEXT_KIN = " + patient.getNextKin() + ", "
				+ "PAT_TELE = " + patient.getTelephone() + ", "
				+ "PAT_MOTH = " + String.valueOf(patient.getMother()) + ", "
				+ "PAT_MOTH_NAME = " + patient.getMother_name() + ", "
				+ "PAT_FATH = " + String.valueOf(patient.getFather()) + ", "
				+ "PAT_FATH_NAME = " + patient.getFather_name() + ", "
				+ "PAT_BTYPE = " + patient.getBloodType() + ", "
				+ "PAT_ESTA = " + String.valueOf(patient.getHasInsurance()) + ", "
				+ "PAT_PTOGE = " + String.valueOf(patient.getParentTogether()) + ", "
				+ "PAT_NOTE = " + patient.getNote() + ", "
				+ "PAT_TAXCODE = " + patient.getTaxCode() + ", "
				+ "PAT_LOCK = " + lock + 1 + ", "
				+ "PAT_PHOTO = " + _createPatientPhotoInputStream(patient.getPhoto())
				+ " WHERE PAT_ID = " + patient.getCode();		
		
		return query;
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
			System.out.println("dimensione output stream: " + outStream.size());
			
			if (outStream != null) byteArray = outStream.toByteArray();
			
		} catch (IOException ioe) {
			//TODO: handle exception
		} catch (Exception ioe) {
			//TODO: handle exception
		}
		
		return byteArray;
	}
}