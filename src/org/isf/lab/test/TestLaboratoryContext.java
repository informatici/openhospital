package org.isf.lab.test;


import org.isf.lab.model.Laboratory;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

import java.util.List;

public class TestLaboratoryContext 
{		
	private static List<Laboratory> savedLaboratory;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM LABORATORY", Laboratory.class, false);
		savedLaboratory = (List<Laboratory>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Laboratory> getAllSaved() throws OHException 
    {	        		
        return savedLaboratory;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM LABORATORY", Laboratory.class, false);
		List<Laboratory> Laboratorys = (List<Laboratory>)jpa.getList();
		for (Laboratory laboratory: Laboratorys) 
		{    		
			int index = savedLaboratory.indexOf(laboratory);
			
			
			if (index == -1)
			{				
				jpa.remove(laboratory);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
