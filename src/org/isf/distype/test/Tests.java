package org.isf.distype.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.distype.service.DiseaseTypeIoOperation;
import org.isf.distype.model.DiseaseType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestDiseaseType testDiseaseType;
	private static TestDiseaseTypeContext testDiseaseTypeContext;
		
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testDiseaseType = new TestDiseaseType();
    	testDiseaseTypeContext = new TestDiseaseTypeContext();
    	
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
	public void testDiseaseTypeGets() 
	{
		String code = "";
			
		
		try 
		{		
			code = _setupTestDiseaseType(false);
			_checkDiseaseTypeIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testDiseaseTypeSets() 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestDiseaseType(true);
			_checkDiseaseTypeIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetDiseaseType()  
	{
		String code = "";
		DiseaseTypeIoOperation ioOperations = new DiseaseTypeIoOperation();
		
		
		try 
		{		
			code = _setupTestDiseaseType(false);
			DiseaseType foundDiseaseType = (DiseaseType)jpa.find(DiseaseType.class, code); 
			ArrayList<DiseaseType> diseaseTypes = ioOperations.getDiseaseTypes();
			
			assertEquals(foundDiseaseType.getDescription(), diseaseTypes.get(diseaseTypes.size()-1).getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateDiseaseType() 
	{
		String code = "";
		DiseaseTypeIoOperation ioOperations = new DiseaseTypeIoOperation();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestDiseaseType(false);
			DiseaseType foundDiseaseType = (DiseaseType)jpa.find(DiseaseType.class, code); 
			foundDiseaseType.setDescription("Update");
			result = ioOperations.updateDiseaseType(foundDiseaseType);
			DiseaseType updateDiseaseType = (DiseaseType)jpa.find(DiseaseType.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateDiseaseType.getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewDiseaseType() 
	{
		DiseaseTypeIoOperation ioOperations = new DiseaseTypeIoOperation();
		boolean result = false;
		
		
		try 
		{		
			DiseaseType diseaseType = testDiseaseType.setup(true);
			result = ioOperations.newDiseaseType(diseaseType);
			
			assertEquals(true, result);
			_checkDiseaseTypeIntoDb(diseaseType.getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeleteDiseaseType() 
	{
		String code = "";
		DiseaseTypeIoOperation ioOperations = new DiseaseTypeIoOperation();
		boolean result = false;
		

		try 
		{		
			code = _setupTestDiseaseType(false);
			DiseaseType foundDiseaseType = (DiseaseType)jpa.find(DiseaseType.class, code); 
			result = ioOperations.deleteDiseaseType(foundDiseaseType);
			
			assertEquals(true, result);
			DiseaseType deletedDiseaseType = (DiseaseType)jpa.find(DiseaseType.class, code); 
			assertEquals(null, deletedDiseaseType);
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoIsCodePresent()
	{
		String code = "";
		DiseaseTypeIoOperation ioOperations = new DiseaseTypeIoOperation();
		boolean result = false;
		

		try 
		{		
			code = _setupTestDiseaseType(false);
			result = ioOperations.isCodePresent(code);
			
			assertEquals(true, result);
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
		testDiseaseTypeContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		System.out.println(testDiseaseTypeContext.getAllSaved());
		testDiseaseTypeContext.deleteNews(jpa);
        
        return;
    }
        
	private String _setupTestDiseaseType(
			boolean usingSet) throws OHException 
	{
		DiseaseType diseaseType;
		

    	jpa.beginTransaction();	
    	diseaseType = testDiseaseType.setup(usingSet);
		jpa.persist(diseaseType);
    	jpa.commitTransaction();
    	
		return diseaseType.getCode();
	}
		
	private void  _checkDiseaseTypeIntoDb(
			String code) throws OHException 
	{
		DiseaseType foundDiseaseType;
		

		foundDiseaseType = (DiseaseType)jpa.find(DiseaseType.class, code); 
		testDiseaseType.check(foundDiseaseType);
		
		return;
	}	
}