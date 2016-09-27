package org.isf.opetype.test;


import org.isf.utils.exception.OHException;
import org.isf.opetype.model.OperationType;

import static org.junit.Assert.assertEquals;

public class TestOperationType 
{	
    private String code = "ZZ";
    private String description = "TestDescription";
    
			
	public OperationType setup(
			boolean usingSet) throws OHException 
	{
		OperationType operationType;
	
				
		if (usingSet == true)
		{
			operationType = new OperationType();
			_setParameters(operationType);
		}
		else
		{
			// Create OperationType with all parameters 
			operationType = new OperationType(code, description);
		}
				    	
		return operationType;
	}
	
	public void _setParameters(
			OperationType operationType) 
	{	
		operationType.setCode(code);
		operationType.setDescription(description);
		
		return;
	}
	
	public void check(
			OperationType operationType) 
	{		
    	System.out.println("Check OperationType: " + operationType.getCode());
    	assertEquals(code, operationType.getCode());
    	assertEquals(description, operationType.getDescription());
		
		return;
	}
}
