package org.isf.lab.test;


import java.util.List;

import org.isf.lab.model.Laboratory;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

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
		List<Laboratory> Laboratories = (List<Laboratory>)jpa.getList();
		for (Laboratory laboratory: Laboratories) 
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
