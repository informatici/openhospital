package org.isf.visits.test;


import java.util.List;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.visits.model.Visit;

public class TestVisitContext 
{		
	private static List<Visit> savedVisit;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM VISITS", Visit.class, false);
		savedVisit = (List<Visit>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Visit> getAllSaved() throws OHException 
    {	        		
        return savedVisit;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM VISITS", Visit.class, false);
		List<Visit> Visits = (List<Visit>)jpa.getList();
		for (Visit visit: Visits) 
		{    		
			int index = savedVisit.indexOf(visit);
			
			
			if (index == -1)
			{				
				jpa.remove(visit);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
