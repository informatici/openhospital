package org.isf.dlvrtype.test;


import org.isf.utils.exception.OHException;
import org.isf.dlvrtype.model.DeliveryType;

import static org.junit.Assert.assertEquals;

public class TestDeliveryType 
{	
    private String code = "Z";
    private String description = "TestDescription";
    
			
	public DeliveryType setup(
			boolean usingSet) throws OHException 
	{
		DeliveryType deliveryType;
	
				
		if (usingSet == true)
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
