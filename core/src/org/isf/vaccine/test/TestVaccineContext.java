package org.isf.vaccine.test;


import java.util.List;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.vaccine.model.Vaccine;

public class TestVaccineContext 
{		
	private static List<Vaccine> savedVaccine;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM VACCINE", Vaccine.class, false);
		savedVaccine = (List<Vaccine>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Vaccine> getAllSaved() throws OHException 
    {	        		
        return savedVaccine;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM VACCINE", Vaccine.class, false);
		List<Vaccine> Vaccines = (List<Vaccine>)jpa.getList();
		for (Vaccine vaccine: Vaccines) 
		{    		
			int index = savedVaccine.indexOf(vaccine);
			
			
			if (index == -1)
			{				
				jpa.remove(vaccine);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
