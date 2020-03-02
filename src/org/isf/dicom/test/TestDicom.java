package org.isf.dicom.test;


import static org.junit.Assert.assertEquals;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.Random;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.isf.dicom.model.FileDicom;
import org.isf.utils.exception.OHException;

public class TestDicom 
{	    
	private Blob dicomData = _createRandomBlob(100);
	private int patId = 0;
	private String fileName = "TestFileName";
	private String dicomAccessionNumber = "TestAccessionNumber";
	private String dicomInstitutionName = "TestInsitutionName";
	private String dicomPatientID = "TestPatientId";
	private String dicomPatientName = "TestPatientName";
	private String dicomPatientAddress = "TestPatientAddress";
	private String dicomPatientAge = "TestPatientAge";
	private String dicomPatientSex = "TestPatientSex";
	private String dicomPatientBirthDate = "TestPatientBirth";
	private String dicomStudyId = "TestStudyId";
	private String dicomStudyDate = "TestStudyDate";
	private String dicomStudyDescription = "TestStudyDescription";
	private String dicomSeriesUID = "TestSeriesUid";
	private String dicomSeriesInstanceUID = "TestSeriesInstanceUid";
	private String dicomSeriesNumber = "TestSeriesNumber";
	private String dicomSeriesDescriptionCodeSequence = "TestSeriesDescription";
	private String dicomSeriesDate = "TestSeriesDate";
	private String dicomSeriesDescription = "TestSeriesDescription";
	private String dicomInstanceUID = "TestInteanceUid";
	private String modality = "TestModality";
	private Blob dicomThumbnail = _createRandomBlob(66);
    
			
	public FileDicom setup(
			boolean usingSet) throws OHException 
	{
		FileDicom dicom;
	
				
		if (usingSet)
		{
			dicom = new FileDicom();
			_setParameters(dicom);
		}
		else
		{
			// Create FileDicom with all parameters 
			dicom = new FileDicom(patId, dicomData, 0, fileName, dicomAccessionNumber, dicomInstitutionName, dicomPatientID, 
					dicomPatientName, dicomPatientAddress, dicomPatientAge, dicomPatientSex, dicomPatientBirthDate, 
					dicomStudyId, dicomStudyDate, dicomStudyDescription, dicomSeriesUID, dicomSeriesInstanceUID, 
					dicomSeriesNumber, dicomSeriesDescriptionCodeSequence, dicomSeriesDate, dicomSeriesDescription, 
					dicomInstanceUID, modality, dicomThumbnail);
		}			
		
		return dicom;
	}
	
	public void _setParameters(
			FileDicom dicom) 
	{	
		dicom.setDicomAccessionNumber(dicomAccessionNumber);
		dicom.setDicomData(dicomData);
		dicom.setDicomInstanceUID(dicomInstanceUID);
		dicom.setDicomInstitutionName(dicomInstitutionName);
		dicom.setDicomPatientAddress(dicomPatientAddress);
		dicom.setDicomPatientAge(dicomPatientAge);
		dicom.setDicomPatientBirthDate(dicomPatientBirthDate);
		dicom.setDicomPatientID(dicomPatientID);
		dicom.setDicomPatientName(dicomPatientName);
		dicom.setDicomPatientSex(dicomPatientSex);
		dicom.setDicomSeriesDate(dicomSeriesDate);
		dicom.setDicomSeriesDescription(dicomSeriesDescription);
		dicom.setDicomSeriesDescriptionCodeSequence(dicomSeriesDescriptionCodeSequence);
		dicom.setDicomSeriesInstanceUID(dicomSeriesInstanceUID);
		dicom.setDicomSeriesNumber(dicomSeriesNumber);
		dicom.setDicomSeriesUID(dicomSeriesUID);
		dicom.setDicomStudyDate(dicomStudyDate);
		dicom.setDicomStudyDescription(dicomStudyDescription);
		dicom.setDicomStudyId(dicomStudyId);
		dicom.setDicomThumbnail(dicomThumbnail);
		dicom.setFileName(fileName);
		dicom.setPatId(patId);
		dicom.setModality(modality);
		
		return;
	}
	
	public void check(
			FileDicom dicom) 
	{		
    	assertEquals(dicomAccessionNumber, dicom.getDicomAccessionNumber());
    	assertEquals(dicomInstanceUID, dicom.getDicomInstanceUID());
    	assertEquals(dicomInstitutionName, dicom.getDicomInstitutionName());
    	assertEquals(dicomPatientAddress, dicom.getDicomPatientAddress());
    	assertEquals(dicomPatientAge, dicom.getDicomPatientAge());
    	assertEquals(dicomPatientBirthDate, dicom.getDicomPatientBirthDate());
    	assertEquals(dicomPatientID, dicom.getDicomPatientID());
    	assertEquals(dicomPatientName, dicom.getDicomPatientName());
    	assertEquals(dicomPatientSex, dicom.getDicomPatientSex());
    	assertEquals(dicomSeriesDate, dicom.getDicomSeriesDate());
    	assertEquals(dicomSeriesDescription, dicom.getDicomSeriesDescription());
    	assertEquals(dicomSeriesDescriptionCodeSequence, dicom.getDicomSeriesDescriptionCodeSequence());
    	assertEquals(dicomSeriesInstanceUID, dicom.getDicomSeriesInstanceUID());
    	assertEquals(dicomSeriesNumber, dicom.getDicomSeriesNumber());
    	assertEquals(dicomSeriesUID, dicom.getDicomSeriesUID());
    	assertEquals(dicomStudyDate, dicom.getDicomStudyDate());
    	assertEquals(dicomStudyDescription, dicom.getDicomStudyDescription());
    	assertEquals(dicomStudyId, dicom.getDicomStudyId());
    	assertEquals(fileName, dicom.getFileName());
    	assertEquals(patId, dicom.getPatId());
    	assertEquals(modality, dicom.getModality());
		
		return;
	}

	public Blob _createRandomBlob(
			int byteCount)
	{	
		Blob blob = null;
		byte[] data;
		
		
		data = new byte[byteCount];		
		new Random().nextBytes(data);		 
		try {
			blob = new SerialBlob(data);
		} catch (SerialException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return blob;
	}
}
