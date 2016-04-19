package org.isf.medstockmovtype.test;


import org.isf.utils.exception.OHException;
import org.isf.medstockmovtype.model.MovementType;

import static org.junit.Assert.assertEquals;

public class TestMovementType 
{	
    private String code = "ZZABCD";
    private String description = "TestDescription";
    private String type = "+";
    
			
	public MovementType setup(
			boolean usingSet) throws OHException 
	{
		MovementType movementType;
	
				
		if (usingSet == true)
		{
			movementType = new MovementType();
			_setParameters(movementType);
		}
		else
		{
			// Create MovementType with all parameters 
			movementType = new MovementType(code, description, type);
		}
				    	
		return movementType;
	}
	
	public void _setParameters(
			MovementType movementType) 
	{	
		movementType.setCode(code);
		movementType.setDescription(description);
		movementType.setType(type);
		
		return;
	}
	
	public void check(
			MovementType movementType) 
	{		
    	System.out.println("Check MovementType: " + movementType.getCode());
    	assertEquals(code, movementType.getCode());
    	assertEquals(description, movementType.getDescription());
    	assertEquals(type, movementType.getType());
		
		return;
	}
}
