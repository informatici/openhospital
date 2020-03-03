package org.isf.vactype.test;


import java.util.List;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.vactype.model.VaccineType;

public class TestVaccineTypeContext 
{		
	private static List<VaccineType> savedVaccineType;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM VACCINETYPE", VaccineType.class, false);
		savedVaccineType = (List<VaccineType>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<VaccineType> getAllSaved() throws OHException 
    {	        		
        return savedVaccineType;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM VACCINETYPE", VaccineType.class, false);
		List<VaccineType> VaccineTypes = (List<VaccineType>)jpa.getList();
		for (VaccineType vaccineType: VaccineTypes) 
		{    		
			int index = savedVaccineType.indexOf(vaccineType);
			
			
			if (index == -1)
			{				
				jpa.remove(vaccineType);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
