package org.isf.hospital.test;


import java.util.List;

import org.isf.hospital.model.Hospital;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestHospitalContext 
{		
	private static List<Hospital> savedHospital;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM HOSPITAL", Hospital.class, false);
		savedHospital = (List<Hospital>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Hospital> getAllSaved() throws OHException 
    {	        		
        return savedHospital;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM HOSPITAL", Hospital.class, false);
		List<Hospital> Hospitals = (List<Hospital>)jpa.getList();
		for (Hospital hospital: Hospitals) 
		{    		
			int index = savedHospital.indexOf(hospital);
			
			
			if (index == -1)
			{				
				jpa.remove(hospital);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
