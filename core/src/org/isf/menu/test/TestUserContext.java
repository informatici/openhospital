package org.isf.menu.test;


import java.util.List;

import org.isf.menu.model.User;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestUserContext 
{		
	private static List<User> savedUser;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM USER", User.class, false);
		savedUser = (List<User>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<User> getAllSaved() throws OHException 
    {	        		
        return savedUser;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM USER", User.class, false);
		List<User> Users = (List<User>)jpa.getList();
		for (User user: Users) 
		{    		
			int index = savedUser.indexOf(user);
			
			
			if (index == -1)
			{				
				jpa.remove(user);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
