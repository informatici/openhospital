package org.isf.medicals.test;


import java.util.List;

import org.isf.medicals.model.Medical;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestMedicalContext 
{		
	private static List<Medical> savedMedical;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM MEDICALDSR", Medical.class, false);
		savedMedical = (List<Medical>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Medical> getAllSaved() throws OHException 
    {	        		
        return savedMedical;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM MEDICALDSR", Medical.class, false);
		List<Medical> Medicals = (List<Medical>)jpa.getList();
		for (Medical medical: Medicals) 
		{    		
			int index = savedMedical.indexOf(medical);
			
			
			if (index == -1)
			{				
				jpa.remove(medical);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
