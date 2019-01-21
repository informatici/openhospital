package org.isf.examination.test;


import java.util.List;

import org.isf.examination.model.PatientExamination;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestPatientExaminationContext 
{
	private static List<PatientExamination> savedPatientExaminations;
	
	
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM PATIENTEXAMINATION", PatientExamination.class, false);
		savedPatientExaminations = (List<PatientExamination>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<PatientExamination> getAllSaved() throws OHException 
    {	        		
        return savedPatientExaminations;
    }
	
    @SuppressWarnings("unchecked")
    public void deleteNews(
			DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM PATIENTEXAMINATION", PatientExamination.class, false);
		List<PatientExamination> PatientExaminations = (List<PatientExamination>)jpa.getList();
		for (PatientExamination patientExamination: PatientExaminations) 
		{    		
			int index = savedPatientExaminations.indexOf(patientExamination);
			if (index == -1)
			{				
				jpa.remove(patientExamination);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}