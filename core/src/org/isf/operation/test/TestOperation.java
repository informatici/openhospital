package org.isf.operation.test;


import static org.junit.Assert.assertEquals;

import org.isf.operation.model.Operation;
import org.isf.opetype.model.OperationType;
import org.isf.utils.exception.OHException;

public class TestOperation 
{	
    private String code = "999";
    private String description = "TestDescription";
    private Integer major = 99;
    
			
	public Operation setup(
			OperationType operationType,
			boolean usingSet) throws OHException 
	{
		Operation operation;
	
				
		if (usingSet)
		{
			operation = new Operation();
			_setParameters(operation, operationType);
		}
		else
		{
			// Create Operation with all parameters 
			operation = new Operation(code, description, operationType, major);
		}
				    	
		return operation;
	}
	
	public void _setParameters(
			Operation operation,
			OperationType operationType) 
	{	
		operation.setCode(code);
		operation.setDescription(description);
		operation.setType(operationType);
		operation.setMajor(major);
		
		return;
	}
	
	public void check(
			Operation operation) 
	{		
    	assertEquals(code, operation.getCode());
    	assertEquals(description, operation.getDescription());
		
		return;
	}
}
