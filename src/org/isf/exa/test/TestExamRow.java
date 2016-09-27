package org.isf.exa.test;


import org.isf.utils.exception.OHException;
import org.isf.exa.model.Exam;
import org.isf.exa.model.ExamRow;

import static org.junit.Assert.assertEquals;

public class TestExamRow 
{	
    private int code = 11;
    private String description = "TestDescription";
    
			
	public ExamRow setup(
			Exam exam,
			boolean usingSet) throws OHException 
	{
		ExamRow examRow;
	
				
		if (usingSet == true)
		{
			examRow = new ExamRow();
			_setParameters(examRow, exam);
		}
		else
		{
			// Create ExamRow with all parameters 
			examRow = new ExamRow(code, exam, description);
		}
				    	
		return examRow;
	}
	
	public void _setParameters(
			ExamRow examRow,
			Exam exam) 
	{	
		examRow.setCode(code);
		examRow.setDescription(description);
		examRow.setExamCode(exam);
		
		return;
	}
	
	public void check(
			ExamRow examRow) 
	{		
    	System.out.println("Check ExamRow: " + examRow.getCode());
    	assertEquals(code, examRow.getCode());
    	assertEquals(description, examRow.getDescription());
		
		return;
	}
}
