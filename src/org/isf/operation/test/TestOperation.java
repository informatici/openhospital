package org.isf.operation.test;


import org.isf.utils.exception.OHException;
import org.isf.operation.model.Operation;
import org.isf.opetype.model.OperationType;

import static org.junit.Assert.assertEquals;

public class TestOperation 
{	
    private String code = "999";
    private String description = "TestDescription";
    private Integer major = 99;
    private Integer lock = 10;
    
			
	public Operation setup(
			OperationType operationType,
			boolean usingSet) throws OHException 
	{
		Operation operation;
	
				
		if (usingSet == true)
		{
			operation = new Operation();
			_setParameters(operation, operationType);
		}
		else
		{
			// Create Operation with all parameters 
			operation = new Operation(code, description, operationType, major, lock);
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
		operation.setLock(lock);
		operation.setMajor(major);
		
		return;
	}
	
	public void check(
			Operation operation) 
	{		
    	System.out.println("Check Operation: " + operation.getCode());
    	assertEquals(code, operation.getCode());
    	assertEquals(description, operation.getDescription());
		
		return;
	}
}
