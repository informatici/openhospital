package org.isf.exatype.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.exatype.model.ExamType;
import org.isf.exatype.service.ExamTypeIoOperation;
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
	private static TestExamType testExamType;
	private static TestExamTypeContext testExamTypeContext;

    @Autowired
    ExamTypeIoOperation examTypeIoOperation;

    
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
			e.printStackTrace();		
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
			ArrayList<ExamType> examTypes = examTypeIoOperation.getExamType();
			
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
	public void testIoUpdateExamType() 
	{
		String code = "";
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestExamType(false);
			ExamType foundExamType = (ExamType)jpa.find(ExamType.class, code); 
			foundExamType.setDescription("Update");
			result = examTypeIoOperation.updateExamType(foundExamType);
			ExamType updateExamType = (ExamType)jpa.find(ExamType.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateExamType.getDescription());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewExamType()
	{
		boolean result = false;
		
		
		try 
		{		
			ExamType examType = testExamType.setup(true);
			result = examTypeIoOperation.newExamType(examType);
			
			assertEquals(true, result);
			_checkExamTypeIntoDb(examType.getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoIsCodePresent()  
	{
		String code = "";
		boolean result = false;
		

		try 
		{		
			code = _setupTestExamType(false);
			result = examTypeIoOperation.isCodePresent(code);
			
			assertEquals(true, result);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeleteExamType() 
	{
		String code = "";
		boolean result = false;
		

		try 
		{		
			code = _setupTestExamType(false);
			ExamType foundExamType = (ExamType)jpa.find(ExamType.class, code); 
			result = examTypeIoOperation.deleteExamType(foundExamType);
			assertEquals(true, result);

			result = examTypeIoOperation.isCodePresent(code);
			assertEquals(false, result);
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
		testExamTypeContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
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