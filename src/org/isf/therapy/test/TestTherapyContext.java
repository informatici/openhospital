package org.isf.therapy.test;


import org.isf.therapy.model.TherapyRow;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

import java.util.List;

public class TestTherapyContext 
{		
	private static List<TherapyRow> savedTherapyRow;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM THERAPIES", TherapyRow.class, false);
		savedTherapyRow = (List<TherapyRow>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<TherapyRow> getAllSaved() throws OHException 
    {	        		
        return savedTherapyRow;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM THERAPIES", TherapyRow.class, false);
		List<TherapyRow> TherapyRows = (List<TherapyRow>)jpa.getList();
		for (TherapyRow therapyRow: TherapyRows) 
		{    		
			int index = savedTherapyRow.indexOf(therapyRow);
			
			
			if (index == -1)
			{				
				jpa.remove(therapyRow);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
