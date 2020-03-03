package org.isf.medtype.test;


import static org.junit.Assert.assertEquals;

import org.isf.medtype.model.MedicalType;
import org.isf.utils.exception.OHException;

public class TestMedicalType 
{	
    private String code = "Z";
    private String description = "TestDescription";
    
			
	public MedicalType setup(
			boolean usingSet) throws OHException 
	{
		MedicalType medicalType;
	
				
		if (usingSet)
		{
			medicalType = new MedicalType();
			_setParameters(medicalType);
		}
		else
		{
			// Create MedicalType with all parameters 
			medicalType = new MedicalType(code, description);
		}
				    	
		return medicalType;
	}
	
	public void _setParameters(
			MedicalType medicalType) 
	{	
		medicalType.setCode(code);
		medicalType.setDescription(description);
		
		return;
	}
	
	public void check(
			MedicalType medicalType) 
	{		
    	assertEquals(code, medicalType.getCode());
    	assertEquals(description, medicalType.getDescription());
		
		return;
	}
}
