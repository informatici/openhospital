package org.isf.menu.test;


import static org.junit.Assert.assertEquals;

import org.isf.menu.model.GroupMenu;
import org.isf.utils.exception.OHException;

public class TestGroupMenu 
{	
    private Integer code = 999;
    private String userGroup = "TestDescription";
    private String menuItem = "TestDescription";
    private char active = 'Y';
    
			
	public GroupMenu setup(
			boolean usingSet) throws OHException 
	{
		GroupMenu groupMenu;
	
				
		if (usingSet)
		{
			groupMenu = new GroupMenu();
			_setParameters(groupMenu);
		}
		else
		{
			// Create GroupMenu with all parameters 
			groupMenu = new GroupMenu(code, userGroup, menuItem, active);
		}
				    	
		return groupMenu;
	}
	
	public void _setParameters(
			GroupMenu groupMenu) 
	{	
		groupMenu.setCode(code);
		groupMenu.setUserGroup(userGroup);
		groupMenu.setMenuItem(menuItem);
		groupMenu.setActive(active);
		
		return;
	}
	
	public void check(
			GroupMenu groupMenu) 
	{		
    	assertEquals(code, groupMenu.getCode());
    	assertEquals(userGroup, groupMenu.getUserGroup());
    	assertEquals(menuItem, groupMenu.getMenuItem());
    	assertEquals(active, groupMenu.getActive());
		
		return;
	}
}
