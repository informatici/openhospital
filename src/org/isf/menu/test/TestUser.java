package org.isf.menu.test;


import org.isf.utils.exception.OHException;
import org.isf.menu.model.User;
import org.isf.menu.model.UserGroup;

import static org.junit.Assert.assertEquals;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class TestUser 
{	
    private String name = "TestName";
    private String description = "TestDescription";	
	private String passwd = "TestPaswd";
	private String desc = "TestDesc";
    
			
	public User setup(
			UserGroup userGroupName,
			boolean usingSet) throws OHException 
	{
		User user;
	
				
		if (usingSet == true)
		{
			user = new User();
			_setParameters(user, userGroupName);
		}
		else
		{
			// Create User with all parameters 
			user = new User(name, userGroupName, passwd, desc);
		}
				    	
		return user;
	}
	
	public void _setParameters(
			User user,
			UserGroup userGroupName) 
	{	
		user.setUserName(name);
		user.setDesc(desc);
		user.setUserGroupName(userGroupName);
		user.setPasswd(passwd);
		
		return;
	}
	
	public void check(
			User user) 
	{		
    	System.out.println("Check User: " + user.getUserName());
    	assertEquals(name, user.getUserName());
    	assertEquals(desc, user.getDesc());
    	assertEquals(passwd, user.getPasswd());
		
		return;
	}
}
