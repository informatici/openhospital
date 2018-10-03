package org.isf.disease.test;


import org.isf.utils.exception.OHException;
import org.isf.disease.model.Disease;
import org.isf.distype.model.DiseaseType;

import static org.junit.Assert.assertEquals;

public class TestDisease 
{	
    private String code = "999";
    private String description = "TestDescription";
    
			
	public Disease setup(
			DiseaseType diseaseType,
			boolean usingSet) throws OHException 
	{
		Disease disease;
	
				
		if (usingSet == true)
		{
			disease = new Disease();
			_setParameters(disease, diseaseType);
		}
		else
		{
			// Create Disease with all parameters 
			disease = new Disease(code, description, diseaseType);
		}
				    	
		return disease;
	}
	
	public void _setParameters(
			Disease disease,
			DiseaseType diseaseType) 
	{	
		disease.setCode(code);
		disease.setDescription(description);
		disease.setType(diseaseType);
		
		return;
	}
	
	public void check(
			Disease disease) 
	{		
    	System.out.println("Check Disease: " + disease.getCode());
    	assertEquals(code, disease.getCode());
    	assertEquals(description, disease.getDescription());
		
		return;
	}
}
