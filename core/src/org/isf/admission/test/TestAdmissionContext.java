package org.isf.admission.test;


import java.util.List;

import org.isf.admission.model.Admission;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestAdmissionContext 
{		
	private static List<Admission> savedAdmission;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM ADMISSION", Admission.class, false);
		savedAdmission = (List<Admission>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Admission> getAllSaved() throws OHException 
    {	        		
        return savedAdmission;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM ADMISSION", Admission.class, false);
		List<Admission> Admissions = (List<Admission>)jpa.getList();
		for (Admission admission: Admissions) 
		{    		
			int index = savedAdmission.indexOf(admission);
			
			
			if (index == -1)
			{				
				jpa.remove(admission);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
