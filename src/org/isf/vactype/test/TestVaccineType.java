package org.isf.vactype.test;


import static org.junit.Assert.assertEquals;

import org.isf.utils.exception.OHException;
import org.isf.vactype.model.VaccineType;

public class TestVaccineType 
{	
    private String code = "Z";
    private String description = "TestDescription";
    
			
	public VaccineType setup(
			boolean usingSet) throws OHException 
	{
		VaccineType vaccineType;
	
				
		if (usingSet)
		{
			vaccineType = new VaccineType();
			_setParameters(vaccineType);
		}
		else
		{
			// Create VaccineType with all parameters 
			vaccineType = new VaccineType(code, description);
		}
				    	
		return vaccineType;
	}
	
	public void _setParameters(
			VaccineType vaccineType) 
	{	
		vaccineType.setCode(code);
		vaccineType.setDescription(description);
		
		return;
	}
	
	public void check(
			VaccineType vaccineType) 
	{		
    	assertEquals(code, vaccineType.getCode());
    	assertEquals(description, vaccineType.getDescription());
		
		return;
	}
}
