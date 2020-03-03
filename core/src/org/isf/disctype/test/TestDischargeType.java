package org.isf.disctype.test;


import static org.junit.Assert.assertEquals;

import org.isf.disctype.model.DischargeType;
import org.isf.utils.exception.OHException;

public class TestDischargeType 
{	
    private String code = "ZZ";
    private String description = "TestDescription";
    
			
	public DischargeType setup(
			boolean usingSet) throws OHException 
	{
		DischargeType dischargeType;
	
				
		if (usingSet)
		{
			dischargeType = new DischargeType();
			_setParameters(dischargeType);
		}
		else
		{
			// Create DischargeType with all parameters 
			dischargeType = new DischargeType(code, description);
		}
				    	
		return dischargeType;
	}
	
	public void _setParameters(
			DischargeType dischargeType) 
	{	
		dischargeType.setCode(code);
		dischargeType.setDescription(description);
		
		return;
	}
	
	public void check(
			DischargeType dischargeType) 
	{		
    	assertEquals(code, dischargeType.getCode());
    	assertEquals(description, dischargeType.getDescription());
		
		return;
	}
}
