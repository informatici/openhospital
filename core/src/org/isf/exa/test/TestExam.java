package org.isf.exa.test;

import static org.junit.Assert.assertEquals;

import org.isf.exa.model.Exam;
import org.isf.exatype.model.ExamType;
import org.isf.utils.exception.OHException;

public class TestExam 
{	
    private String code = "ZZ";
    private String description = "TestDescription";
	private String defaultResult = "TestDefaultResult";
			
	public Exam setup(
			ExamType examtype,
			int procedure,
			boolean usingSet) throws OHException 
	{
		Exam exam;
	
				
		if (usingSet)
		{
			exam = new Exam();
			_setParameters(exam, procedure, examtype);
		}
		else
		{
			// Create Exam with all parameters 
			exam = new Exam(code, description, examtype, procedure, defaultResult);
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
		exam.setDefaultResult(defaultResult);
		
		return;
	}
	
	public void check(
			Exam exam) 
	{		
    	assertEquals(code, exam.getCode());
    	assertEquals(description, exam.getDescription());
    	assertEquals(defaultResult, exam.getDefaultResult());
		
		return;
	}
}
