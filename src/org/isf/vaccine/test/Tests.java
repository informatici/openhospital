package org.isf.vaccine.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.vaccine.model.Vaccine;
import org.isf.vaccine.service.VaccineIoOperations;
import org.isf.vactype.model.VaccineType;
import org.isf.vactype.test.TestVaccineType;
import org.isf.vactype.test.TestVaccineTypeContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class Tests  
{
	@Autowired
	private static DbJpaUtil jpa;
	private static TestVaccine testVaccine;
	private static TestVaccineContext testVaccineContext;
	private static TestVaccineType testVaccineType;
	private static TestVaccineTypeContext testVaccineTypeContext;
		
	
	@BeforeClass
    public static void setUpClass()  
    {
    	
    	testVaccine = new TestVaccine();
    	testVaccineContext = new TestVaccineContext();
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
    	jpa.destroy();
    	testVaccine = null;
    	testVaccineContext = null;
    	testVaccineType = null;
    	testVaccineTypeContext = null;

    	return;
    }
	
		
	@Test
	public void testVaccineGets() throws OHException 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestVaccine(false);
			_checkVaccineIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testVaccineSets() 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestVaccine(true);
			_checkVaccineIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetVaccine() 
	{
		String code = "";
		VaccineIoOperations ioOperations = new VaccineIoOperations();
		
		
		try 
		{		
			code = _setupTestVaccine(false);
			Vaccine foundVaccine = (Vaccine)jpa.find(Vaccine.class, code); 
			ArrayList<Vaccine> vaccines = ioOperations.getVaccine(foundVaccine.getVaccineType().getCode());
			
			assertEquals(foundVaccine.getDescription(), vaccines.get(vaccines.size()-1).getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateVaccine() 
	{
		String code = "";
		VaccineIoOperations ioOperations = new VaccineIoOperations();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestVaccine(false);
			Vaccine foundVaccine = (Vaccine)jpa.find(Vaccine.class, code); 
			foundVaccine.setDescription("Update");
			result = ioOperations.updateVaccine(foundVaccine);
			Vaccine updateVaccine = (Vaccine)jpa.find(Vaccine.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateVaccine.getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewVaccine() 
	{
		VaccineIoOperations ioOperations = new VaccineIoOperations();
		boolean result = false;
		
		
		try 
		{		
	    	jpa.beginTransaction();	
	    	VaccineType vaccineType = testVaccineType.setup(false);
	    	jpa.persist(vaccineType);
			jpa.commitTransaction();
	    	
			Vaccine vaccine = testVaccine.setup(vaccineType, true);
			result = ioOperations.newVaccine(vaccine);
			
			assertEquals(true, result);
			_checkVaccineIntoDb(vaccine.getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeleteVaccine() 
	{
		String code = "";
		VaccineIoOperations ioOperations = new VaccineIoOperations();
		boolean result = false;
		

		try 
		{		
			code = _setupTestVaccine(false);
			Vaccine foundVaccine = (Vaccine)jpa.find(Vaccine.class, code); 
			result = ioOperations.deleteVaccine(foundVaccine);
			
			assertEquals(true, result);
			Vaccine deletedVaccine = (Vaccine)jpa.find(Vaccine.class, code); 
			assertEquals(null, deletedVaccine);
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
		VaccineIoOperations ioOperations = new VaccineIoOperations();
		boolean result = false;
		

		try 
		{		
			code = _setupTestVaccine(false);
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
		testVaccineContext.saveAll(jpa);
		testVaccineTypeContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		System.out.println(testVaccineContext.getAllSaved());
		testVaccineContext.deleteNews(jpa);
		testVaccineTypeContext.deleteNews(jpa);
        
        return;
    }
        
	private String _setupTestVaccine(
			boolean usingSet) throws OHException 
	{
		Vaccine vaccine;
		VaccineType vaccineType = testVaccineType.setup(false);
		

    	jpa.beginTransaction();	
    	vaccine = testVaccine.setup(vaccineType, usingSet);
		jpa.persist(vaccineType);
		jpa.persist(vaccine);
    	jpa.commitTransaction();
    	
		return vaccine.getCode();
	}
		
	private void  _checkVaccineIntoDb(
			String code) throws OHException 
	{
		Vaccine foundVaccine;
		

		foundVaccine = (Vaccine)jpa.find(Vaccine.class, code); 
		testVaccine.check(foundVaccine);
		
		return;
	}	
}