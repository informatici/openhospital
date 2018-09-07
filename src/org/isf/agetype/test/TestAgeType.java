package org.isf.agetype.test;


import org.isf.utils.exception.OHException;
import org.isf.agetype.model.AgeType;

import static org.junit.Assert.assertEquals;

public class TestAgeType 
{	
    private String code = "d8";
    private String description = "TestDescription";
    private int from = 1;
    private int to = 100;
    
			
	public AgeType setup(
			boolean usingSet) throws OHException 
	{
		AgeType ageType;
	
				
		if (usingSet == true)
		{
			ageType = new AgeType();
			_setParameters(ageType);
		}
		else
		{
			// Create AgeType with all parameters 
			ageType = new AgeType(code, from, to, description);
		}
				    	
		return ageType;
	}
	
	public void _setParameters(
			AgeType ageType) 
	{	
		ageType.setCode(code);
		ageType.setFrom(from);
		ageType.setTo(to);
		ageType.setDescription(description);
		
		return;
	}
	
	public void check(
			AgeType ageType) 
	{		
    	System.out.println("Check AgeType: " + ageType.getCode());
    	assertEquals(code, ageType.getCode());
    	assertEquals(from, ageType.getFrom());
    	assertEquals(to, ageType.getTo());
    	assertEquals(description, ageType.getDescription());
		
		return;
	}
}
