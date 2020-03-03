package org.isf.lab.test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.isf.exa.model.Exam;
import org.isf.lab.model.Laboratory;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHException;

public class TestLaboratory 
{	 
	private Integer code = 0;
	private String material = "TestMaterial";
	private GregorianCalendar now = new GregorianCalendar();
	private GregorianCalendar registrationDate = new GregorianCalendar(now.get(Calendar.YEAR), 1, 1);
	private GregorianCalendar examDate = new GregorianCalendar(now.get(Calendar.YEAR), 10, 11);
	private String result = "TestResult";
	private String note = "TestNote";
	private String patName = "TestPatientName";
	private String InOutPatient = "O";
	private Integer age = 37;
	private String sex = "F";
    
			
	public Laboratory setup(
			Exam exam,
			Patient patient,
			boolean usingSet) throws OHException 
	{
		Laboratory laboratory;
	
				
		if (usingSet)
		{
			laboratory = new Laboratory();
			_setParameters(laboratory, exam, patient);
		}
		else
		{
			// Create Laboratory with all parameters 
			laboratory = new Laboratory(exam, registrationDate, result, note, patient, patName);
			laboratory.setAge(age);
			laboratory.setExamDate(examDate);
			laboratory.setInOutPatient(InOutPatient);
			laboratory.setMaterial(material);
			laboratory.setResult(result);
			laboratory.setSex(sex);
		}
				    	
		return laboratory;
	}
	
	public void _setParameters(
			Laboratory laboratory,
			Exam exam,
			Patient patient) 
	{	
		laboratory.setAge(age);
		laboratory.setDate(registrationDate);
		laboratory.setExam(exam);
		laboratory.setExamDate(examDate);
		laboratory.setInOutPatient(InOutPatient);
		laboratory.setMaterial(material);
		laboratory.setNote(note);
		laboratory.setPatient(patient);
		laboratory.setPatName(patName);
		laboratory.setResult(result);
		laboratory.setSex(sex);
		
		return;
	}
	
	public void check(
			Laboratory laboratory) 
	{		
    	assertEquals(age, laboratory.getAge());		
    	assertEquals(registrationDate, laboratory.getDate());	
    	assertEquals(examDate, laboratory.getExamDate());
    	assertEquals(InOutPatient, laboratory.getInOutPatient());
    	assertEquals(material, laboratory.getMaterial());		
    	assertEquals(note, laboratory.getNote());	
    	assertEquals(patName, laboratory.getPatName());		
    	assertEquals(result, laboratory.getResult());	
    	assertEquals(sex, laboratory.getSex());		
		
		return;
	}
}
