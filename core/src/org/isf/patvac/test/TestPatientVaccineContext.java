package org.isf.patvac.test;


import java.util.List;

import org.isf.patvac.model.PatientVaccine;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestPatientVaccineContext 
{		
	private static List<PatientVaccine> savedPatientVaccine;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM PATIENTVACCINE", PatientVaccine.class, false);
		savedPatientVaccine = (List<PatientVaccine>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<PatientVaccine> getAllSaved() throws OHException 
    {	        		
        return savedPatientVaccine;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM PATIENTVACCINE", PatientVaccine.class, false);
		List<PatientVaccine> PatientVaccines = (List<PatientVaccine>)jpa.getList();
		for (PatientVaccine patientVaccine: PatientVaccines) 
		{    		
			int index = savedPatientVaccine.indexOf(patientVaccine);
			
			
			if (index == -1)
			{				
				jpa.remove(patientVaccine);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
