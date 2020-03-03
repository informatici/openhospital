package org.isf.pregtreattype.test;


import static org.junit.Assert.assertEquals;

import org.isf.pregtreattype.model.PregnantTreatmentType;
import org.isf.utils.exception.OHException;

public class TestPregnantTreatmentType 
{	
    private String code = "ZZ";
    private String description = "TestDescription";
    
			
	public PregnantTreatmentType setup(
			boolean usingSet) throws OHException 
	{
		PregnantTreatmentType pregnantTreatmentType;
	
				
		if (usingSet)
		{
			pregnantTreatmentType = new PregnantTreatmentType();
			_setParameters(pregnantTreatmentType);
		}
		else
		{
			// Create PregnantTreatmentType with all parameters 
			pregnantTreatmentType = new PregnantTreatmentType(code, description);
		}
				    	
		return pregnantTreatmentType;
	}
	
	public void _setParameters(
			PregnantTreatmentType pregnantTreatmentType) 
	{	
		pregnantTreatmentType.setCode(code);
		pregnantTreatmentType.setDescription(description);
		
		return;
	}
	
	public void check(
			PregnantTreatmentType pregnantTreatmentType) 
	{		
    	assertEquals(code, pregnantTreatmentType.getCode());
    	assertEquals(description, pregnantTreatmentType.getDescription());
		
		return;
	}
}
