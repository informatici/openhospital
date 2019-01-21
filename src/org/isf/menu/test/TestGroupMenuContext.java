package org.isf.menu.test;


import java.util.List;

import org.isf.menu.model.GroupMenu;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestGroupMenuContext 
{		
	private static List<GroupMenu> savedGroupMenu;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM GROUPMENU", GroupMenu.class, false);
		savedGroupMenu = (List<GroupMenu>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<GroupMenu> getAllSaved() throws OHException 
    {	        		
        return savedGroupMenu;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM GROUPMENU", GroupMenu.class, false);
		List<GroupMenu> GroupMenus = (List<GroupMenu>)jpa.getList();
		for (GroupMenu groupMenu: GroupMenus) 
		{    		
			int index = savedGroupMenu.indexOf(groupMenu);
			
			
			if (index == -1)
			{				
				jpa.remove(groupMenu);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
