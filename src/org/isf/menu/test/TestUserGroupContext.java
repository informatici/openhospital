package org.isf.menu.test;


import java.util.List;

import org.isf.menu.model.UserGroup;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestUserGroupContext 
{		
	private static List<UserGroup> savedUserGroup;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM USERGROUP", UserGroup.class, false);
		savedUserGroup = (List<UserGroup>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<UserGroup> getAllSaved() throws OHException 
    {	        		
        return savedUserGroup;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM USERGROUP", UserGroup.class, false);
		List<UserGroup> UserGroups = (List<UserGroup>)jpa.getList();
		for (UserGroup userGroup: UserGroups) 
		{    		
			int index = savedUserGroup.indexOf(userGroup);
			
			
			if (index == -1)
			{				
				jpa.remove(userGroup);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
