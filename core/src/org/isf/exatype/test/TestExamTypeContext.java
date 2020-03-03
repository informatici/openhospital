package org.isf.exatype.test;


import java.util.List;

import org.isf.exatype.model.ExamType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestExamTypeContext 
{		
	private static List<ExamType> savedExamType;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM EXAMTYPE", ExamType.class, false);
		savedExamType = (List<ExamType>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<ExamType> getAllSaved() throws OHException 
    {	        		
        return savedExamType;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM EXAMTYPE", ExamType.class, false);
		List<ExamType> ExamTypes = (List<ExamType>)jpa.getList();
		for (ExamType examType: ExamTypes) 
		{    		
			int index = savedExamType.indexOf(examType);
			
			
			if (index == -1)
			{				
				jpa.remove(examType);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
