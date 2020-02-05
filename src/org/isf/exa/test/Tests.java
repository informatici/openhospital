package org.isf.exa.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.exa.model.Exam;
import org.isf.exa.model.ExamRow;
import org.isf.exa.service.ExamIoOperations;
import org.isf.exa.service.ExamRowIoOperations;
import org.isf.exatype.model.ExamType;
import org.isf.exatype.test.TestExamType;
import org.isf.exatype.test.TestExamTypeContext;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestExam testExam;
	private static TestExamRow testExamRow;
	private static TestExamType testExamType;
	private static TestExamContext testExamContext;
	private static TestExamTypeContext testExamTypeContext;
	private static TestExamRowContext testExamRowContext;

    @Autowired
    ExamIoOperations examIoOperation;
    
    @Autowired
    ExamRowIoOperations examRowIoOperation;
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testExam = new TestExam();
    	testExamType = new TestExamType();
    	testExamRow = new TestExamRow();
    	testExamContext = new TestExamContext();
    	testExamTypeContext = new TestExamTypeContext();
    	testExamRowContext = new TestExamRowContext();
    	
        return;
    }

    @Before
    public void setUp() throws OHException
    {
        jpa.open();
        
        _saveContext();
		
		return;
    }
        
    @After
    public void tearDown() throws Exception 
    {
        _restoreContext();   
        
        jpa.flush();
        jpa.close();
                
        return;
    }
    
    @AfterClass
    public static void tearDownClass() throws OHException 
    {
    	testExam = null;
    	testExamType = null;
    	testExamRow = null;
    	testExamContext = null;
    	testExamTypeContext = null;
    	testExamRowContext = null;

    	return;
    }
	
	
	@Test
	public void testExamGets() 
	{
		String code = "";
			
		
		try 
		{		
			code = _setupTestExam(false);
			_checkExamIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testExamSets() 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestExam(true);
			_checkExamIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testExamRowGets() 
	{
		int code = 0;
			
		
		try 
		{		
			code = _setupTestExamRow(false);
			_checkExamRowIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testExamRowSets() 
	{
		int code = 0;
			

		try 
		{		
			code = _setupTestExamRow(true);
			_checkExamRowIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetExamRow()  
	{
		int code = 0;
		
		
		try 
		{		
			code = _setupTestExamRow(false);
			ExamRow foundExamRow = (ExamRow)jpa.find(ExamRow.class, code); 
			ArrayList<ExamRow> examRows = examRowIoOperation.getExamRow(0, null);
			
			assertEquals(foundExamRow.getDescription(), examRows.get(examRows.size()-1).getDescription());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetExams()  
	{
		String code = "";
		
		
		try 
		{		
			code = _setupTestExam(false);
			Exam foundExam = (Exam)jpa.find(Exam.class, code); 
			ArrayList<Exam> exams = examIoOperation.getExams();
			
			assertEquals(foundExam.getDescription(), exams.get(exams.size()-1).getDescription());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetExamType()  
	{
		String code = "";
		
		
		try 
		{		
			code = _setupTestExamType(false);
			ExamType foundExamType = (ExamType)jpa.find(ExamType.class, code); 
			ArrayList<ExamType> examTypes = examIoOperation.getExamType();
			
			assertEquals(examTypes.contains(foundExamType), true);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewExamRow() 
	{
		boolean result = false;
		
		
		try 
		{		
			ExamType examType = testExamType.setup(false);
			Exam exam = testExam.setup(examType, 2, false);
			ExamRow examRow = testExamRow.setup(exam, true);
			
			jpa.beginTransaction();	
			jpa.persist(examType);
			jpa.persist(exam);
			jpa.commitTransaction();
			result = examIoOperation.newExamRow(examRow);
			
			assertEquals(true, result);
			_checkExamRowIntoDb(examRow.getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoNewExam() 
	{
		boolean result = false;
		
		
		try 
		{		
			ExamType examType = testExamType.setup(false);
			
			
			jpa.beginTransaction();	
			jpa.persist(examType);
			jpa.commitTransaction();
			Exam exam = testExam.setup(examType, 1, false);
			result = examIoOperation.newExam(exam);
			
			assertEquals(true, result);
			_checkExamIntoDb(exam.getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateExam() 
	{
		String code = "";
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestExam(false);
			Exam foundExam = (Exam)jpa.find(Exam.class, code); 
			jpa.flush();
			foundExam.setDescription("Update");
			result = examIoOperation.updateExam(foundExam);
			Exam updateExam = (Exam)jpa.find(Exam.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateExam.getDescription());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoDeleteExam() 
	{
		String code = "";
		boolean result = false;
		

		try 
		{		
			code = _setupTestExam(false);
			Exam foundExam = (Exam)jpa.find(Exam.class, code); 
			result = examIoOperation.deleteExam(foundExam);

			assertEquals(true, result);
			result = examIoOperation.isCodePresent(code);			
			assertEquals(false, result);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoDeleteExamRow() 
	{
		int code = 0;
		boolean result = false;
		

		try 
		{		
			code = _setupTestExamRow(false);
			ExamRow foundExamRow = (ExamRow)jpa.find(ExamRow.class, code); 
			result = examIoOperation.deleteExamRow(foundExamRow);
			
			assertEquals(true, result);
			result = examIoOperation.isRowPresent(code);			
			assertEquals(false, result);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoIsKeyPresent()
	{
		boolean result = false;
		

		try 
		{		
			ExamType examType = testExamType.setup(false);
			Exam exam = testExam.setup(examType, 1, false);
			
			jpa.beginTransaction();	
			jpa.persist(examType);
			jpa.persist(exam);
			jpa.commitTransaction();
			result = examIoOperation.isKeyPresent(exam);
			
			assertEquals(true, result);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
		
	
	private void _saveContext() throws OHException 
    {	
		testExamContext.saveAll(jpa);
		testExamTypeContext.saveAll(jpa);
		testExamRowContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		testExamRowContext.deleteNews(jpa);
		testExamContext.deleteNews(jpa);
		testExamTypeContext.deleteNews(jpa);
        
        return;
    }
    
	private String _setupTestExam(
			boolean usingSet) throws OHException 
	{
		ExamType examType = testExamType.setup(false);	
		Exam exam = testExam.setup(examType, 1, usingSet);
		
	
		jpa.beginTransaction();	
		jpa.persist(examType);
		jpa.persist(exam);
		jpa.commitTransaction();
		
		return exam.getCode();
	}
		
	private void  _checkExamIntoDb(
			String code) throws OHException 
	{
		Exam foundExam;
		
	
		foundExam = (Exam)jpa.find(Exam.class, code); 
		testExam.check(foundExam);
		
		return;
	}	

	private int _setupTestExamRow(
			boolean usingSet) throws OHException 
	{		
		
		ExamType examType = testExamType.setup(usingSet);
		Exam exam = testExam.setup(examType, 2, usingSet);
		ExamRow examRow = testExamRow.setup(exam, usingSet);
		
		jpa.beginTransaction();	
		jpa.persist(examType);
		jpa.persist(exam);
		jpa.persist(examRow);
    	jpa.commitTransaction();
    	
		return examRow.getCode();
	}
		
	private void  _checkExamRowIntoDb(
			int code) throws OHException 
	{
		ExamRow foundExamRow;
		

		foundExamRow = (ExamRow)jpa.find(ExamRow.class, code); 
		testExamRow.check(foundExamRow);
		
		return;
	}	

	private String _setupTestExamType(
			boolean usingSet) throws OHException 
	{	
		ExamType examType;
		
	
		jpa.beginTransaction();	
		examType = testExamType.setup(false);	
		jpa.persist(examType);
		jpa.commitTransaction();
		
		return examType.getCode();
	}
}