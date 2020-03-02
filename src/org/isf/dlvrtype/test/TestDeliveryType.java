package org.isf.dlvrtype.test;


import static org.junit.Assert.assertEquals;

import org.isf.dlvrtype.model.DeliveryType;
import org.isf.utils.exception.OHException;

public class TestDeliveryType 
{	
    private String code = "Z";
    private String description = "TestDescription";
    
			
	public DeliveryType setup(
			boolean usingSet) throws OHException 
	{
		DeliveryType deliveryType;
	
				
		if (usingSet)
		{
			deliveryType = new DeliveryType();
			_setParameters(deliveryType);
		}
		else
		{
			// Create DeliveryType with all parameters 
			deliveryType = new DeliveryType(code, description);
		}
				    	
		return deliveryType;
	}
	
	public void _setParameters(
			DeliveryType deliveryType) 
	{	
		deliveryType.setCode(code);
		deliveryType.setDescription(description);
		
		return;
	}
	
	public void check(
			DeliveryType deliveryType) 
	{		
    	assertEquals(code, deliveryType.getCode());
    	assertEquals(description, deliveryType.getDescription());
		
		return;
	}
}
