package org.isf.menu.test;


import static org.junit.Assert.assertEquals;

import org.isf.menu.model.UserGroup;
import org.isf.utils.exception.OHException;

public class TestUserGroup 
{	
    private String code = "Z";
    private String description = "TestDescription";
    
			
	public UserGroup setup(
			boolean usingSet) throws OHException 
	{
		UserGroup userGroup;
	
				
		if (usingSet)
		{
			userGroup = new UserGroup();
			_setParameters(userGroup);
		}
		else
		{
			// Create UserGroup with all parameters 
			userGroup = new UserGroup(code, description);
		}
				    	
		return userGroup;
	}
	
	public void _setParameters(
			UserGroup userGroup) 
	{	
		userGroup.setCode(code);
		userGroup.setDesc(description);
		
		return;
	}
	
	public void check(
			UserGroup userGroup) 
	{		
    	assertEquals(code, userGroup.getCode());
    	assertEquals(description, userGroup.getDesc());
		
		return;
	}
}
