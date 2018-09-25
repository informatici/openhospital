package org.isf.exa.test;

import org.isf.utils.exception.OHException;
import org.isf.exa.model.Exam;
import org.isf.exatype.model.ExamType;

import static org.junit.Assert.assertEquals;

public class TestExam 
{	
    private String code = "ZZ";
    private String description = "TestDescription";
	private String defaultResult = "TestDefaultResult";
	private Integer lock = 1;
    
			
	public Exam setup(
			ExamType examtype,
			int procedure,
			boolean usingSet) throws OHException 
	{
		Exam exam;
	
				
		if (usingSet == true)
		{
			exam = new Exam();
			_setParameters(exam, procedure, examtype);
		}
		else
		{
			// Create Exam with all parameters 
			exam = new Exam(code, description, examtype, procedure, defaultResult, lock);
		}
				    	
		return exam;
	}
	
	public void _setParameters(
			Exam exam,
			int procedure,
			ExamType examtype) 
	{	
		exam.setCode(code);
		exam.setDescription(description);
		exam.setExamtype(examtype);
		exam.setProcedure(procedure);
		exam.setLock(lock);
		exam.setDefaultResult(defaultResult);
		
		return;
	}
	
	public void check(
			Exam exam) 
	{		
    	System.out.println("Check Exam: " + exam.getCode());
    	assertEquals(code, exam.getCode());
    	assertEquals(description, exam.getDescription());
    	assertEquals(lock, exam.getLock());
    	assertEquals(defaultResult, exam.getDefaultResult());
		
		return;
	}
}
