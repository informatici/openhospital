package org.isf.malnutrition.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.malnutrition.model.Malnutrition;
import org.isf.malnutrition.test.TestMalnutrition;
import org.isf.malnutrition.service.MalnutritionIoOperation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestMalnutrition testMalnutrition;
	private static TestMalnutritionContext testMalnutritionContext;
		
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testMalnutrition = new TestMalnutrition();
    	testMalnutritionContext = new TestMalnutritionContext();
    	
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

    	return;
    }
	
	
	@Test
	public void testMalnutritionGets() 
	{
		int code = 0;
			
		
		try 
		{		
			code = _setupTestMalnutrition(false);
			_checkMalnutritionIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testMalnutritionSets() 
	{
		int code = 0;
			

		try 
		{		
			code = _setupTestMalnutrition(true);
			_checkMalnutritionIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetMalnutrition() 
	{
		int code = 0;
		MalnutritionIoOperation ioOperations = new MalnutritionIoOperation();
		
		
		try 
		{		
			code = _setupTestMalnutrition(false);
			Malnutrition foundMalnutrition = (Malnutrition)jpa.find(Malnutrition.class, code); 
			ArrayList<Malnutrition> malnutritions = ioOperations.getMalnutritions(String.valueOf(foundMalnutrition.getAdmId()));
			
			assertEquals(code, malnutritions.get(malnutritions.size()-1).getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetMalnutritionLock() 
	{
		int code = 0;
		MalnutritionIoOperation ioOperations = new MalnutritionIoOperation();
		
		
		try 
		{		
			code = _setupTestMalnutrition(false);
			Malnutrition foundMalnutrition = (Malnutrition)jpa.find(Malnutrition.class, code); 
			int lock = ioOperations.getMalnutritionLock(code);
			
			assertEquals(foundMalnutrition.getLock(), lock);
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
		
		return;
	}	

	@Test
	public void testIoGetLastMalnutrition() 
	{
		int code = 0;
		MalnutritionIoOperation ioOperations = new MalnutritionIoOperation();
		
		
		try 
		{		
			code = _setupTestMalnutrition(false);
			Malnutrition foundMalnutrition = (Malnutrition)jpa.find(Malnutrition.class, code); 
			Malnutrition malnutrition = ioOperations.getLastMalnutrition(foundMalnutrition.getAdmId());
			
			assertEquals(code, malnutrition.getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateMalnutrition() 
	{
		int code = 0;
		MalnutritionIoOperation ioOperations = new MalnutritionIoOperation();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestMalnutrition(false);
			Malnutrition foundMalnutrition = (Malnutrition)jpa.find(Malnutrition.class, code); 
			foundMalnutrition.setHeight(200);
			result = ioOperations.updateMalnutrition(foundMalnutrition);
			Malnutrition updateMalnutrition = (Malnutrition)jpa.find(Malnutrition.class, code); 
			
			assertEquals(true, result);
			assertEquals(200, updateMalnutrition.getHeight());
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewMalnutrition() 
	{
		MalnutritionIoOperation ioOperations = new MalnutritionIoOperation();
		boolean result = false;
		
		
		try 
		{		
			Malnutrition movementType = testMalnutrition.setup(true);
			result = ioOperations.newMalnutrition(movementType);
			
			assertEquals(true, result);
			_checkMalnutritionIntoDb(movementType.getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeleteMalnutrition() 
	{
		int code = 0;
		MalnutritionIoOperation ioOperations = new MalnutritionIoOperation();
		boolean result = false;
		

		try 
		{		
			code = _setupTestMalnutrition(false);
			Malnutrition foundMalnutrition = (Malnutrition)jpa.find(Malnutrition.class, code); 
			result = ioOperations.deleteMalnutrition(foundMalnutrition);
			
			assertEquals(true, result);
			Malnutrition deletedMalnutrition = (Malnutrition)jpa.find(Malnutrition.class, code); 
			assertEquals(null, deletedMalnutrition);
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
		
		return;
	}
		
	
	private void _saveContext() throws OHException 
    {	
		testMalnutritionContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		System.out.println(testMalnutritionContext.getAllSaved());
		testMalnutritionContext.deleteNews(jpa);
        
        return;
    }
    
	private int _setupTestMalnutrition(
			boolean usingSet) throws OHException 
	{
		Malnutrition malnutrition;
		
	
		jpa.beginTransaction();	
		malnutrition = testMalnutrition.setup(usingSet);
		jpa.persist(malnutrition);
		jpa.commitTransaction();
		
		return malnutrition.getCode();
	}
		
	private void  _checkMalnutritionIntoDb(
			int code) throws OHException 
	{
		Malnutrition foundMalnutrition;
		
	
		foundMalnutrition = (Malnutrition)jpa.find(Malnutrition.class, code); 
		testMalnutrition.check(foundMalnutrition);
		
		return;
	}	
}