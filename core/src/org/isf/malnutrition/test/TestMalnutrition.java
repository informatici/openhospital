package org.isf.malnutrition.test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.isf.admission.model.Admission;
import org.isf.malnutrition.model.Malnutrition;
import org.isf.utils.exception.OHException;

public class TestMalnutrition 
{	 
	private int code = 0;
	private GregorianCalendar now = new GregorianCalendar();
	private GregorianCalendar dateSupp = new GregorianCalendar(now.get(Calendar.YEAR), 1, 1);
    private GregorianCalendar dateConf = new GregorianCalendar(now.get(Calendar.YEAR), 10, 11);
    private float height = (float)185.47;
	private float weight = (float)70.70;
	
	public Malnutrition setup(Admission admission,
			boolean usingSet) throws OHException 
	{
		Malnutrition malnutrition;
				
		if (usingSet)
		{
			malnutrition = new Malnutrition();
			_setParameters(admission, malnutrition);
		}
		else
		{
			// Create Malnutrition with all parameters 
			malnutrition = new Malnutrition(code, dateSupp, dateConf, admission, height, weight);
		}
				    	
		return malnutrition;
	}
	
	public void _setParameters(Admission admission,
			Malnutrition malnutrition) 
	{	
		malnutrition.setAdmission(admission);
		malnutrition.setDateConf(dateConf);
		malnutrition.setDateSupp(dateSupp);
		malnutrition.setHeight(height);
		malnutrition.setWeight(weight);
		
		return;
	}
	
	public void check(
			Malnutrition malnutrition) 
	{		
    	assertEquals(dateConf, malnutrition.getDateConf());
    	assertEquals(dateSupp, malnutrition.getDateSupp());
    	assertEquals(height, malnutrition.getHeight(), 0.1);
    	assertEquals(weight, malnutrition.getWeight(), 0.1);
		
		return;
	}
}
