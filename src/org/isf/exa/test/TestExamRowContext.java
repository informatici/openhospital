package org.isf.exa.test;


import java.util.List;

import org.isf.exa.model.ExamRow;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestExamRowContext 
{		
	private static List<ExamRow> savedExamRow;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM EXAMROW", ExamRow.class, false);
		savedExamRow = (List<ExamRow>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<ExamRow> getAllSaved() throws OHException 
    {	        		
        return savedExamRow;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM EXAMROW", ExamRow.class, false);
		List<ExamRow> ExamRows = (List<ExamRow>)jpa.getList();
		for (ExamRow examRow: ExamRows) 
		{    		
			int index = savedExamRow.indexOf(examRow);
			
			
			if (index == -1)
			{				
				jpa.remove(examRow);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
