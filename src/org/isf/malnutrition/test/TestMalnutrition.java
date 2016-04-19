package org.isf.malnutrition.test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.isf.utils.exception.OHException;
import org.isf.malnutrition.model.Malnutrition;

import static org.junit.Assert.assertEquals;

public class TestMalnutrition 
{	 
	private int code = 0;
	private GregorianCalendar now = new GregorianCalendar();
	private GregorianCalendar dateSupp = new GregorianCalendar(now.get(Calendar.YEAR), 1, 1);;
	private GregorianCalendar dateConf = new GregorianCalendar(now.get(Calendar.YEAR), 10, 11);;
	private int admId = 11;
	private float height = (float)185.47;
	private float weight = (float)70.70;
	private int lock = 1;
    
			
	public Malnutrition setup(
			boolean usingSet) throws OHException 
	{
		Malnutrition malnutrition;
	
				
		if (usingSet == true)
		{
			malnutrition = new Malnutrition();
			_setParameters(malnutrition);
		}
		else
		{
			// Create Malnutrition with all parameters 
			malnutrition = new Malnutrition(code, dateSupp, dateConf, admId, height, weight, lock);
		}
				    	
		return malnutrition;
	}
	
	public void _setParameters(
			Malnutrition malnutrition) 
	{	
		malnutrition.setAdmId(admId);
		malnutrition.setDateConf(dateConf);
		malnutrition.setDateSupp(dateSupp);
		malnutrition.setHeight(height);
		malnutrition.setWeight(weight);
		malnutrition.setLock(lock);
		
		return;
	}
	
	public void check(
			Malnutrition malnutrition) 
	{		
    	System.out.println("Check Malnutrition: " + malnutrition.getCode());	
    	assertEquals(admId, malnutrition.getAdmId());
    	assertEquals(dateConf, malnutrition.getDateConf());
    	assertEquals(dateSupp, malnutrition.getDateSupp());
    	assertEquals(height, malnutrition.getHeight());
    	assertEquals(weight, malnutrition.getWeight());
    	assertEquals(lock, malnutrition.getLock());
		
		return;
	}
}
