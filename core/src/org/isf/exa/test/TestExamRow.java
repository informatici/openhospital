package org.isf.exa.test;


import static org.junit.Assert.assertEquals;

import org.isf.exa.model.Exam;
import org.isf.exa.model.ExamRow;
import org.isf.utils.exception.OHException;

public class TestExamRow 
{	
    private String description = "TestDescription";
    
			
	public ExamRow setup(
			Exam exam,
			boolean usingSet) throws OHException 
	{
		ExamRow examRow;
	
				
		if (usingSet)
		{
			examRow = new ExamRow();
			_setParameters(examRow, exam);
		}
		else
		{
			// Create ExamRow with all parameters 
			examRow = new ExamRow(exam, description);
		}
				    	
		return examRow;
	}
	
	public void _setParameters(
			ExamRow examRow,
			Exam exam) 
	{	
		examRow.setDescription(description);
		examRow.setExamCode(exam);
		
		return;
	}
	
	public void check(
			ExamRow examRow) 
	{		
    	assertEquals(description, examRow.getDescription());
		
		return;
	}
}
