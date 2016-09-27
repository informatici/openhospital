package org.isf.disease.test;


import org.isf.disease.model.Disease;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

import java.util.List;

public class TestDiseaseContext 
{		
	private static List<Disease> savedDisease;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM DISEASE", Disease.class, false);
		savedDisease = (List<Disease>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Disease> getAllSaved() throws OHException 
    {	        		
        return savedDisease;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM DISEASE", Disease.class, false);
		List<Disease> Diseases = (List<Disease>)jpa.getList();
		for (Disease disease: Diseases) 
		{    		
			int index = savedDisease.indexOf(disease);
			
			
			if (index == -1)
			{				
				jpa.remove(disease);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
