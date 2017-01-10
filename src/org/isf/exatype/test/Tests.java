package org.isf.exatype.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.exatype.model.ExamType;
import org.isf.exatype.service.ExamTypeIoOperation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestExamType testExamType;
	private static TestExamTypeContext testExamTypeContext;
		
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testExamType = new TestExamType();
    	testExamTypeContext = new TestExamTypeContext();
    	
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
    	jpa.destroy();
    	testExamType = null;
    	testExamTypeContext = null;

    	return;
    }
	
		
	@Test
	public void testExamTypeGets() 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestExamType(false);
			_checkExamTypeIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testExamTypeSets() 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestExamType(true);
			_checkExamTypeIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetExamType()
	{
		String code = "";
		ExamTypeIoOperation ioOperations = new ExamTypeIoOperation();
		
		
		try 
		{		
			code = _setupTestExamType(false);
			ExamType foundExamType = (ExamType)jpa.find(ExamType.class, code); 
			ArrayList<ExamType> examTypes = ioOperations.getExamType();
			
			assertEquals(foundExamType.getDescription(), examTypes.get(examTypes.size()-1).getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateExamType() 
	{
		String code = "";
		ExamTypeIoOperation ioOperations = new ExamTypeIoOperation();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestExamType(false);
			ExamType foundExamType = (ExamType)jpa.find(ExamType.class, code); 
			foundExamType.setDescription("Update");
			result = ioOperations.updateExamType(foundExamType);
			ExamType updateExamType = (ExamType)jpa.find(ExamType.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateExamType.getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewExamType()
	{
		ExamTypeIoOperation ioOperations = new ExamTypeIoOperation();
		boolean result = false;
		
		
		try 
		{		
			ExamType examType = testExamType.setup(true);
			result = ioOperations.newExamType(examType);
			
			assertEquals(true, result);
			_checkExamTypeIntoDb(examType.getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeleteExamType() 
	{
		String code = "";
		ExamTypeIoOperation ioOperations = new ExamTypeIoOperation();
		boolean result = false;
		

		try 
		{		
			code = _setupTestExamType(false);
			ExamType foundExamType = (ExamType)jpa.find(ExamType.class, code); 
			result = ioOperations.deleteExamType(foundExamType);
			
			assertEquals(true, result);
			ExamType deletedExamType = (ExamType)jpa.find(ExamType.class, code); 
			assertEquals(null, deletedExamType);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoIsCodePresent()  
	{
		String code = "";
		ExamTypeIoOperation ioOperations = new ExamTypeIoOperation();
		boolean result = false;
		

		try 
		{		
			code = _setupTestExamType(false);
			result = ioOperations.isCodePresent(code);
			
			assertEquals(true, result);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	
	private void _saveContext() throws OHException 
    {	
		testExamTypeContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		System.out.println(testExamTypeContext.getAllSaved());
		testExamTypeContext.deleteNews(jpa);
        
        return;
    }
        
	private String _setupTestExamType(
			boolean usingSet) throws OHException 
	{
		ExamType examType;
		

    	jpa.beginTransaction();	
    	examType = testExamType.setup(usingSet);
		jpa.persist(examType);
    	jpa.commitTransaction();
    	
		return examType.getCode();
	}
		
	private void  _checkExamTypeIntoDb(
			String code) throws OHException 
	{
		ExamType foundExamType;
		

		foundExamType = (ExamType)jpa.find(ExamType.class, code); 
		testExamType.check(foundExamType);
		
		return;
	}	
}