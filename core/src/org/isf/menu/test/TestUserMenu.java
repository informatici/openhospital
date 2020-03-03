package org.isf.menu.test;


import static org.junit.Assert.assertEquals;

import org.isf.menu.model.UserMenuItem;
import org.isf.utils.exception.OHException;

public class TestUserMenu 
{	
    private String code = "Z";
	private String buttonLabel = "TestButtonLabel";
	private String altLabel = "TestAltLabel";
	private String tooltip = "TestToolTip";
	private char shortcut = 'Y';
	private String mySubmenu = "TestMySubmenu";
	private String myClass = "TestMyClass";
	private boolean isASubMenu = true;
	private int position = 11;
    
			
	public UserMenuItem setup(
			boolean usingSet) throws OHException 
	{
		UserMenuItem userMenuItem;
	
				
		if (usingSet)
		{
			userMenuItem = new UserMenuItem();
			_setParameters(userMenuItem);
		}
		else
		{
			// Create UserMenuItem with all parameters 
			userMenuItem = new UserMenuItem(code, buttonLabel, altLabel, tooltip, shortcut, mySubmenu, myClass, isASubMenu, position, true);
		}
				    	
		return userMenuItem;
	}
	
	public void _setParameters(
			UserMenuItem userMenuItem) 
	{	
		userMenuItem.setCode(code);
		userMenuItem.setAltLabel(altLabel);
		userMenuItem.setButtonLabel(buttonLabel);
		userMenuItem.setActive(true);
		userMenuItem.setASubMenu(isASubMenu);
		userMenuItem.setMyClass(myClass);
		userMenuItem.setMySubmenu(mySubmenu);
		userMenuItem.setPosition(position);
		userMenuItem.setShortcut(shortcut);
		userMenuItem.setTooltip(tooltip);
		
		return;
	}
	
	public void check(
			UserMenuItem userMenuItem) 
	{		
    	assertEquals(code, userMenuItem.getCode());
    	assertEquals(altLabel, userMenuItem.getAltLabel());
    	assertEquals(buttonLabel, userMenuItem.getButtonLabel());
    	assertEquals(isASubMenu, userMenuItem.isASubMenu());
    	assertEquals(myClass, userMenuItem.getMyClass());
    	assertEquals(mySubmenu, userMenuItem.getMySubmenu());
    	assertEquals(position, userMenuItem.getPosition());
    	assertEquals(shortcut, userMenuItem.getShortcut());
    	assertEquals(tooltip, userMenuItem.getTooltip());
		
		return;
	}
}
