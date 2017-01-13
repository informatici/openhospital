package org.isf.hospital.test;


import static org.junit.Assert.assertEquals;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.hospital.model.Hospital;
import org.isf.hospital.service.HospitalIoOperations;
import org.isf.hospital.test.TestHospital;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestHospital testHospital;
	private static TestHospitalContext testHospitalContext;
		
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testHospital = new TestHospital();
    	testHospitalContext = new TestHospitalContext();
    	
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
    	testHospital = null;
    	testHospitalContext = null;

    	return;
    }
	
	
	@Test
	public void testHospitalGets() 
	{
		String code = "";
			
		
		try 
		{		
			code = _setupTestHospital(false);
			_checkHospitalIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testHospitalSets() 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestHospital(true);
			_checkHospitalIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateHospital() 
	{
		String code = "";
		HospitalIoOperations ioOperations = new HospitalIoOperations();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestHospital(false);
			Hospital foundHospital = (Hospital)jpa.find(Hospital.class, code); 
			foundHospital.setDescription("Update");
			result = ioOperations.updateHospital(foundHospital);
			Hospital updateHospital = (Hospital)jpa.find(Hospital.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateHospital.getDescription());
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
		testHospitalContext.saveAll(jpa);
		System.out.println("Saved: " + testHospitalContext.getAllSaved());
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		System.out.println("Restore: " + testHospitalContext.getAllSaved());
		testHospitalContext.deleteNews(jpa);
        
        return;
    }
    
	private String _setupTestHospital(
			boolean usingSet) throws OHException 
	{
		Hospital hospital;
		
	
		jpa.beginTransaction();	
		hospital = testHospital.setup(usingSet);
		jpa.persist(hospital);
		jpa.commitTransaction();
		
		return hospital.getCode();
	}
		
	private void  _checkHospitalIntoDb(
			String code) throws OHException 
	{
		Hospital foundHospital;
		
	
		foundHospital = (Hospital)jpa.find(Hospital.class, code); 
		testHospital.check(foundHospital);
		
		return;
	}	
}