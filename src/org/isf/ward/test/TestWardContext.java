package org.isf.ward.test;


import org.isf.ward.model.Ward;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

import java.util.List;

public class TestWardContext 
{		
	private static List<Ward> savedWard;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM WARD", Ward.class, false);
		savedWard = (List<Ward>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Ward> getAllSaved() throws OHException 
    {	        		
        return savedWard;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM WARD", Ward.class, false);
		List<Ward> Wards = (List<Ward>)jpa.getList();
		for (Ward ward: Wards) 
		{    		
			int index = savedWard.indexOf(ward);
			
			
			if (index == -1)
			{				
				jpa.remove(ward);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
