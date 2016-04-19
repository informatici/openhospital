package org.isf.malnutrition.test;


import org.isf.malnutrition.model.Malnutrition;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

import java.util.List;

public class TestMalnutritionContext 
{		
	private static List<Malnutrition> savedMalnutrition;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM MALNUTRITIONCONTROL", Malnutrition.class, false);
		savedMalnutrition = (List<Malnutrition>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Malnutrition> getAllSaved() throws OHException 
    {	        		
        return savedMalnutrition;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM MALNUTRITIONCONTROL", Malnutrition.class, false);
		List<Malnutrition> Malnutritions = (List<Malnutrition>)jpa.getList();
		for (Malnutrition malnutrition: Malnutritions) 
		{    		
			int index = savedMalnutrition.indexOf(malnutrition);
			
			
			if (index == -1)
			{				
				jpa.remove(malnutrition);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
