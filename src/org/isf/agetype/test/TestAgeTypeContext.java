package org.isf.agetype.test;


import java.util.List;

import org.isf.agetype.model.AgeType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestAgeTypeContext 
{		
	private static List<AgeType> savedAgeType;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM AGETYPE", AgeType.class, false);
		savedAgeType = (List<AgeType>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<AgeType> getAllSaved() throws OHException 
    {	        		
        return savedAgeType;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM AGETYPE", AgeType.class, false);
		List<AgeType> AgeTypes = (List<AgeType>)jpa.getList();
		for (AgeType ageType: AgeTypes) 
		{    		
			int index = savedAgeType.indexOf(ageType);
			
			
			if (index == -1)
			{				
				jpa.remove(ageType);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
