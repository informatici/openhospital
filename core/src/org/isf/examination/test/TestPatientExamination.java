package org.isf.examination.test;


import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;

import org.isf.examination.model.PatientExamination;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHException;

public class TestPatientExamination 
{	
	private Timestamp pex_date = new Timestamp(1000);
	private double pex_height = 0;	
	private int pex_weight = 60;	
	private int pex_pa_min = 80;	
	private int pex_pa_max = 120;	
	private int pex_fc = 60;	
	private double pex_temp = 36;	
	private double pex_sat = 1;	
	private String pex_note = "";
	
	
	public PatientExamination setup(
			Patient patient,
			boolean usingSet) throws OHException 
	{
		PatientExamination patientExamination;
	

		if (usingSet)
		{
			patientExamination = new PatientExamination();
			_setParameters(patientExamination, patient);
		}
		else
		{
			// Create Patient Examination with all parameters 
			patientExamination = new PatientExamination(pex_date, patient, pex_height, pex_weight, 
					pex_pa_min, pex_pa_max, pex_fc, pex_temp, pex_sat, pex_note);	
		}
		
		return patientExamination;
	}
	
	private void _setParameters(
			PatientExamination patientExamination,
			Patient patient) 
	{			
		patientExamination.setPatient(patient);
		patientExamination.setPex_date(pex_date);
		patientExamination.setPex_fc(pex_fc);
		patientExamination.setPex_height(pex_height);
		patientExamination.setPex_note(pex_note);
		patientExamination.setPex_pa_max(pex_pa_max);
		patientExamination.setPex_pa_min(pex_pa_min);
		patientExamination.setPex_sat(pex_sat);
		patientExamination.setPex_temp(pex_temp);
		patientExamination.setPex_weight(pex_weight);
		
		return;
	}
	
	public void check(
			PatientExamination patientExamination)  
	{		
		//assertEquals(pex_date, foundPatientExamination.getPex_date());
		assertEquals(pex_fc, patientExamination.getPex_fc());
		//assertEquals(pex_height, patientExamination.getPex_height());
		assertEquals(pex_note, patientExamination.getPex_note());
		assertEquals(pex_pa_max, patientExamination.getPex_pa_max());
		assertEquals(pex_pa_min, patientExamination.getPex_pa_min());
		//assertEquals(pex_sat, foundPatientExamination.getPex_sat());
		//assertEquals(pex_temp, foundPatientExamination.getPex_temp());
		assertEquals(pex_weight, patientExamination.getPex_weight());
		
		return;
	}	
}