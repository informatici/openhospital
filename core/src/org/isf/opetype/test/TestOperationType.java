package org.isf.opetype.test;


import static org.junit.Assert.assertEquals;

import org.isf.opetype.model.OperationType;
import org.isf.utils.exception.OHException;

public class TestOperationType 
{	
    private String code = "ZZ";
    private String description = "TestDescription";
    
			
	public OperationType setup(
			boolean usingSet) throws OHException 
	{
		OperationType operationType;
	
				
		if (usingSet)
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
    	assertEquals(code, operationType.getCode());
    	assertEquals(description, operationType.getDescription());
		
		return;
	}
}
