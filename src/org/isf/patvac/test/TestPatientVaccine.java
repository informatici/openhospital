package org.isf.patvac.test;


import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.isf.patient.model.Patient;
import org.isf.patvac.model.PatientVaccine;
import org.isf.utils.exception.OHException;
import org.isf.vaccine.model.Vaccine;

public class TestPatientVaccine 
{	
	private int code = 0;
	private int progr = 10;
	private GregorianCalendar vaccineDate = new GregorianCalendar(1984, Calendar.AUGUST, 14);
    private int lock = 0;
    
			
	public PatientVaccine setup(
			Patient patient,
			Vaccine vaccine,
			boolean usingSet) throws OHException 
	{
		PatientVaccine patientVaccine;
	
				
		if (usingSet)
		{
			patientVaccine = new PatientVaccine();
			_setParameters(patient, vaccine, patientVaccine);
		}
		else
		{
			// Create PatientVaccine with all parameters 
			patientVaccine = new PatientVaccine(code, progr, vaccineDate, patient, vaccine, lock);
		}
				    	
		return patientVaccine;
	}
	
	public void _setParameters(
			Patient patient,
			Vaccine vaccine,
			PatientVaccine patientVaccine) 
	{	
		patientVaccine.setCode(code);
		patientVaccine.setProgr(progr);
		patientVaccine.setVaccineDate(vaccineDate);
		patientVaccine.setPatient(patient);
		patientVaccine.setVaccine(vaccine);
		patientVaccine.setLock(lock);
		
		return;
	}
	
	public void check(
			PatientVaccine patientVaccine) 
	{		
    	assertEquals(lock, patientVaccine.getLock());
    	assertEquals(progr, patientVaccine.getProgr());
    	assertEquals(vaccineDate, patientVaccine.getVaccineDate());
		
		return;
	}
}
