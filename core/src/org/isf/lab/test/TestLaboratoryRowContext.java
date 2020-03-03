package org.isf.lab.test;


import java.util.List;

import org.isf.lab.model.LaboratoryRow;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestLaboratoryRowContext 
{		
	private static List<LaboratoryRow> savedLaboratoryRow;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM LABORATORYROW", LaboratoryRow.class, false);
		savedLaboratoryRow = (List<LaboratoryRow>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<LaboratoryRow> getAllSaved() throws OHException 
    {	        		
        return savedLaboratoryRow;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM LABORATORYROW", LaboratoryRow.class, false);
		List<LaboratoryRow> LaboratoryRows = (List<LaboratoryRow>)jpa.getList();
		for (LaboratoryRow laboratoryRow: LaboratoryRows) 
		{    		
			int index = savedLaboratoryRow.indexOf(laboratoryRow);
			
			
			if (index == -1)
			{				
				jpa.remove(laboratoryRow);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
