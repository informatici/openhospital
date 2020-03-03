package org.isf.medicalstockward.test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.isf.medicals.model.Medical;
import org.isf.medicalstockward.model.MovementWard;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;

public class TestMovementWard 
{	 
	private GregorianCalendar now = new GregorianCalendar();
	private GregorianCalendar date = new GregorianCalendar(now.get(Calendar.YEAR), 2, 2);
	private boolean isPatient = false;
	private int age = 10;
	private float weight = 78;
	private String description = "TestDescriptionm";
	private Double quantity = 46.;
	private String units = "TestUni";
    
			
	public MovementWard setup(
			Ward ward,
			Patient patient,
			Medical medical,
			boolean usingSet) throws OHException 
	{
		MovementWard movementWard;
	
				
		if (usingSet)
		{
			movementWard = new MovementWard();
			_setParameters(movementWard, ward, patient, medical);
		}
		else
		{
			// Create MovementWard with all parameters 
			movementWard = new MovementWard(ward, date, isPatient, patient, age, weight, description, medical, quantity, units);
		}
				    	
		return movementWard;
	}
	
	public void _setParameters(
			MovementWard movementWard,
			Ward ward,
			Patient patient,
			Medical medical) 
	{	
		movementWard.setAge(age);
		movementWard.setDate(date);
		movementWard.setDescription(description);
		movementWard.setMedical(medical);
		movementWard.setPatient(isPatient);
		movementWard.setPatient(patient);
		movementWard.setQuantity(quantity);
		movementWard.setUnits(units);
		movementWard.setWard(ward);
		movementWard.setWeight(weight);
		
		return;
	}
	
	public void check(
			MovementWard movementWard) 
	{		
    	assertEquals(age, movementWard.getAge());
    	assertEquals(date, movementWard.getDate());
    	assertEquals(description, movementWard.getDescription());
    	assertEquals(isPatient, movementWard.isPatient());
    	assertEquals(quantity, movementWard.getQuantity());
    	assertEquals(units, movementWard.getUnits());
    	assertEquals(weight, movementWard.getWeight(), 0.1);
		
		return;
	}
}
