package org.isf.disease.test;


import static org.junit.Assert.assertEquals;

import org.isf.disease.model.Disease;
import org.isf.distype.model.DiseaseType;
import org.isf.utils.exception.OHException;

public class TestDisease 
{	
    private String code = "999";
    private String description = "TestDescription";
    
			
	public Disease setup(
			DiseaseType diseaseType,
			boolean usingSet) throws OHException 
	{
		Disease disease;
	
				
		if (usingSet)
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
    	assertEquals(code, disease.getCode());
    	assertEquals(description, disease.getDescription());
		
		return;
	}
}
