package org.isf.therapy.test;


import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.isf.medicals.model.Medical;
import org.isf.patient.model.Patient;
import org.isf.therapy.model.TherapyRow;
import org.isf.utils.exception.OHException;


public class TestTherapy 
{	
	private GregorianCalendar startDate = new GregorianCalendar(10, 9, 8);
	private GregorianCalendar endDate = new GregorianCalendar(11, 10, 9);
	private Double qty = 9.9;
	private int unitID = 10;
	private int freqInDay = 11;
	private int freqInPeriod = 12;
	private String note = "TestNote";
	private boolean notify = false;
	private boolean sms = true;
    
			
	public TherapyRow setup(
			Patient patient,
			Medical medical,
			boolean usingSet) throws OHException 
	{
		TherapyRow therapyRow;
	
				
		if (usingSet)
		{
			therapyRow = new TherapyRow();
			_setParameters(patient, medical, therapyRow);
		}
		else
		{
			// Create TherapyRow with all parameters 
			therapyRow = new TherapyRow(0, patient, startDate, endDate,
					medical, qty, unitID, freqInDay, freqInPeriod, note, notify, sms);
		}
				    	
		return therapyRow;
	}
	
	public void _setParameters(
			Patient patient,
			Medical medical,
			TherapyRow therapyRow) 
	{	
		therapyRow.setEndDate(endDate);
		therapyRow.setFreqInDay(freqInDay);
		therapyRow.setFreqInPeriod(freqInPeriod);
		therapyRow.setMedical(medical);
		therapyRow.setNote(note);
		therapyRow.setNotify(notify);
		therapyRow.setPatID(patient);
		therapyRow.setQty(qty);
		therapyRow.setSms(sms);
		therapyRow.setStartDate(startDate);
		therapyRow.setUnitID(unitID);
				
		return;
	}
	
	public void check(
			TherapyRow therapyRow) 
	{		
    	assertEquals(endDate,therapyRow.getEndDate());
    	assertEquals(freqInDay,therapyRow.getFreqInDay());
    	assertEquals(freqInPeriod,therapyRow.getFreqInPeriod());
    	assertEquals(note,therapyRow.getNote());
    	assertEquals(notify,therapyRow.isNotify());
    	assertEquals(qty,therapyRow.getQty());
    	assertEquals(sms,therapyRow.isSms());
    	assertEquals(startDate,therapyRow.getStartDate());
    	assertEquals(unitID,therapyRow.getUnitID());	
		
		return;
	}
}
