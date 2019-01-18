package org.isf.distype.test;


import org.isf.utils.exception.OHException;
import org.isf.distype.model.DiseaseType;

import static org.junit.Assert.assertEquals;

public class TestDiseaseType 
{	
    private String code = "ZZ";
    private String description = "TestDescription";
    
			
	public DiseaseType setup(
			boolean usingSet) throws OHException 
	{
		DiseaseType diseaseType;
	
				
		if (usingSet == true)
		{
			diseaseType = new DiseaseType();
			_setParameters(diseaseType);
		}
		else
		{
			// Create DiseaseType with all parameters 
			diseaseType = new DiseaseType(code, description);
		}
				    	
		return diseaseType;
	}
	
	public void _setParameters(
			DiseaseType diseaseType) 
	{	
		diseaseType.setCode(code);
		diseaseType.setDescription(description);
		
		return;
	}
	
	public void check(
			DiseaseType diseaseType) 
	{		
    	assertEquals(code, diseaseType.getCode());
    	assertEquals(description, diseaseType.getDescription());
		
		return;
	}
}
