package org.isf.menu.test;


import java.util.List;

import org.isf.menu.model.UserMenuItem;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestUserMenuContext 
{		
	private static List<UserMenuItem> savedUserMenuItem;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM MENUITEM", UserMenuItem.class, false);
		savedUserMenuItem = (List<UserMenuItem>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<UserMenuItem> getAllSaved() throws OHException 
    {	        		
        return savedUserMenuItem;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM MENUITEM", UserMenuItem.class, false);
		List<UserMenuItem> UserMenuItems = (List<UserMenuItem>)jpa.getList();
		for (UserMenuItem userMenuItem: UserMenuItems) 
		{    		
			int index = savedUserMenuItem.indexOf(userMenuItem);
			
			
			if (index == -1)
			{				
				jpa.remove(userMenuItem);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
