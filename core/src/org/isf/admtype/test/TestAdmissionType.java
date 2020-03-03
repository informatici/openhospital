package org.isf.admtype.test;


import static org.junit.Assert.assertEquals;

import org.isf.admtype.model.AdmissionType;
import org.isf.utils.exception.OHException;

public class TestAdmissionType 
{	
    private String code = "ZZ";
    private String description = "TestDescription";
    
			
	public AdmissionType setup(
			boolean usingSet) throws OHException 
	{
		AdmissionType admissionType;
	
				
		if (usingSet)
		{
			admissionType = new AdmissionType();
			_setParameters(admissionType);
		}
		else
		{
			// Create AdmissionType with all parameters 
			admissionType = new AdmissionType(code, description);
		}
				    	
		return admissionType;
	}
	
	public void _setParameters(
			AdmissionType admissionType) 
	{	
		admissionType.setCode(code);
		admissionType.setDescription(description);
		
		return;
	}
	
	public void check(
			AdmissionType admissionType) 
	{		
    	assertEquals(code, admissionType.getCode());
    	assertEquals(description, admissionType.getDescription());
		
		return;
	}
}
