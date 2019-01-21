package org.isf.exa.test;


import java.util.List;

import org.isf.exa.model.Exam;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestExamContext 
{		
	private static List<Exam> savedExam;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM EXAM", Exam.class, false);
		savedExam = (List<Exam>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Exam> getAllSaved() throws OHException 
    {	        		
        return savedExam;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM EXAM", Exam.class, false);
		List<Exam> Exams = (List<Exam>)jpa.getList();
		for (Exam exam: Exams) 
		{    		
			int index = savedExam.indexOf(exam);
			
			
			if (index == -1)
			{				
				jpa.remove(exam);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
