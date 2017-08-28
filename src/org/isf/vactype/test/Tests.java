package org.isf.vactype.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.vactype.model.VaccineType;
import org.isf.vactype.service.VacTypeIoOperation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestVaccineType testVaccineType;
	private static TestVaccineTypeContext testVaccineTypeContext;
		
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testVaccineType = new TestVaccineType();
    	testVaccineTypeContext = new TestVaccineTypeContext();
    	
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
    	//jpa.destroy();

    	return;
    }
	
		
	@Test
	public void testVaccineTypeGets() throws OHException 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestVaccineType(false);
			_checkVaccineTypeIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testVaccineTypeSets() 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestVaccineType(true);
			_checkVaccineTypeIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetVaccineType() 
	{
		String code = "";
		VacTypeIoOperation ioOperations = new VacTypeIoOperation();
		
		
		try 
		{		
			code = _setupTestVaccineType(false);
			VaccineType foundVaccineType = (VaccineType)jpa.find(VaccineType.class, code); 
			ArrayList<VaccineType> vaccineTypes = ioOperations.getVaccineType();
			
			assertEquals(foundVaccineType.getDescription(), vaccineTypes.get(vaccineTypes.size()-1).getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateVaccineType() 
	{
		String code = "";
		VacTypeIoOperation ioOperations = new VacTypeIoOperation();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestVaccineType(false);
			VaccineType foundVaccineType = (VaccineType)jpa.find(VaccineType.class, code); 
			foundVaccineType.setDescription("Update");
			result = ioOperations.updateVaccineType(foundVaccineType);
			VaccineType updateVaccineType = (VaccineType)jpa.find(VaccineType.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateVaccineType.getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewVaccineType() 
	{
		VacTypeIoOperation ioOperations = new VacTypeIoOperation();
		boolean result = false;
		
		
		try 
		{		
			VaccineType vaccineType = testVaccineType.setup(true);
			result = ioOperations.newVaccineType(vaccineType);
			
			assertEquals(true, result);
			_checkVaccineTypeIntoDb(vaccineType.getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeleteVaccineType() 
	{
		String code = "";
		VacTypeIoOperation ioOperations = new VacTypeIoOperation();
		boolean result = false;
		

		try 
		{		
			code = _setupTestVaccineType(false);
			VaccineType foundVaccineType = (VaccineType)jpa.find(VaccineType.class, code); 
			result = ioOperations.deleteVaccineType(foundVaccineType);
			
			assertEquals(true, result);
			VaccineType deletedVaccineType = (VaccineType)jpa.find(VaccineType.class, code); 
			assertEquals(null, deletedVaccineType);
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
		VacTypeIoOperation ioOperations = new VacTypeIoOperation();
		boolean result = false;
		

		try 
		{		
			code = _setupTestVaccineType(false);
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
		testVaccineTypeContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		System.out.println(testVaccineTypeContext.getAllSaved());
		testVaccineTypeContext.deleteNews(jpa);
        
        return;
    }
        
	private String _setupTestVaccineType(
			boolean usingSet) throws OHException 
	{
		VaccineType vaccineType;
		

    	jpa.beginTransaction();	
    	vaccineType = testVaccineType.setup(usingSet);
		jpa.persist(vaccineType);
    	jpa.commitTransaction();
    	
		return vaccineType.getCode();
	}
		
	private void  _checkVaccineTypeIntoDb(
			String code) throws OHException 
	{
		VaccineType foundVaccineType;
		

		foundVaccineType = (VaccineType)jpa.find(VaccineType.class, code); 
		testVaccineType.check(foundVaccineType);
		
		return;
	}	
}