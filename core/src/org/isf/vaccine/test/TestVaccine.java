package org.isf.vaccine.test;


import static org.junit.Assert.assertEquals;

import org.isf.utils.exception.OHException;
import org.isf.vaccine.model.Vaccine;
import org.isf.vactype.model.VaccineType;

public class TestVaccine 
{	
    private String code = "Z";
    private String description = "TestDescription";
    
			
	public Vaccine setup(
			VaccineType vaccineType,
			boolean usingSet) throws OHException 
	{
		Vaccine vaccine;
	
				
		if (usingSet)
		{
			vaccine = new Vaccine();
			_setParameters(vaccineType, vaccine);
		}
		else
		{
			// Create Vaccine with all parameters 
			vaccine = new Vaccine(code, description, vaccineType);
		}
				    	
		return vaccine;
	}
	
	public void _setParameters(
			VaccineType vaccineType,
			Vaccine vaccine) 
	{	
		vaccine.setCode(code);
		vaccine.setDescription(description);
		vaccine.setVaccineType(vaccineType);
		
		return;
	}
	
	public void check(
			Vaccine vaccine) 
	{		
    	assertEquals(code, vaccine.getCode());
    	assertEquals(description, vaccine.getDescription());
		
		return;
	}
}
