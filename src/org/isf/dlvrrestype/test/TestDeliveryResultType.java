package org.isf.dlvrrestype.test;


import org.isf.utils.exception.OHException;
import org.isf.dlvrrestype.model.DeliveryResultType;

import static org.junit.Assert.assertEquals;

public class TestDeliveryResultType 
{	
    private String code = "Z";
    private String description = "TestDescription";
    
			
	public DeliveryResultType setup(
			boolean usingSet) throws OHException 
	{
		DeliveryResultType deliveryResultType;
	
				
		if (usingSet == true)
		{
			deliveryResultType = new DeliveryResultType();
			_setParameters(deliveryResultType);
		}
		else
		{
			// Create DeliveryResultType with all parameters 
			deliveryResultType = new DeliveryResultType(code, description);
		}
				    	
		return deliveryResultType;
	}
	
	public void _setParameters(
			DeliveryResultType deliveryResultType) 
	{	
		deliveryResultType.setCode(code);
		deliveryResultType.setDescription(description);
		
		return;
	}
	
	public void check(
			DeliveryResultType deliveryResultType) 
	{		
    	assertEquals(code, deliveryResultType.getCode());
    	assertEquals(description, deliveryResultType.getDescription());
		
		return;
	}
}
