package org.isf.patient.test;


import java.util.List;

import org.isf.patient.model.Patient;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestPatientContext 
{		
	private static List<Patient> savedPatients;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM PATIENT", Patient.class, false);
		savedPatients = (List<Patient>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Patient> getAllSaved() throws OHException 
    {	        		
        return savedPatients;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM PATIENT", Patient.class, false);
		List<Patient> Patients = (List<Patient>)jpa.getList();
		for (Patient patient: Patients) 
		{    		
			int index = savedPatients.indexOf(patient);
			if (index == -1)
			{				
				jpa.remove(patient);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
		
	public Integer getMaxCode()
	{
		Integer max = 0;
		
		
		for (Patient patient: savedPatients) 
		{    		        	
			if (max < patient.getCode())
			{
				max = patient.getCode();	
			}
		}
		
		return max;
	}
}
