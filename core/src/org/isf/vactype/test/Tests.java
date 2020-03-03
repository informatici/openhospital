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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestVaccineType testVaccineType;
	private static TestVaccineTypeContext testVaccineTypeContext;

    @Autowired
    VacTypeIoOperation vaccineTypeIoOperation;
    
	
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
			e.printStackTrace();		
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
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetVaccineType() 
	{
		String code = "";
		
		
		try 
		{		
			code = _setupTestVaccineType(false);
			VaccineType foundVaccineType = (VaccineType)jpa.find(VaccineType.class, code); 
			ArrayList<VaccineType> vaccineTypes = vaccineTypeIoOperation.getVaccineType();
			
			assertEquals(foundVaccineType.getDescription(), vaccineTypes.get(vaccineTypes.size()-1).getDescription());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateVaccineType() 
	{
		String code = "";
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestVaccineType(false);
			VaccineType foundVaccineType = (VaccineType)jpa.find(VaccineType.class, code); 
			foundVaccineType.setDescription("Update");
			result = vaccineTypeIoOperation.updateVaccineType(foundVaccineType);
			VaccineType updateVaccineType = (VaccineType)jpa.find(VaccineType.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateVaccineType.getDescription());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewVaccineType() 
	{
		boolean result = false;
		
		
		try 
		{		
			VaccineType vaccineType = testVaccineType.setup(true);
			result = vaccineTypeIoOperation.newVaccineType(vaccineType);
			
			assertEquals(true, result);
			_checkVaccineTypeIntoDb(vaccineType.getCode());
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
			code = _setupTestVaccineType(false);
			result = vaccineTypeIoOperation.isCodePresent(code);
			
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
	public void testIoDeleteVaccineType() 
	{
		String code = "";
		boolean result = false;
		

		try 
		{		
			code = _setupTestVaccineType(false);
			VaccineType foundVaccineType = (VaccineType)jpa.find(VaccineType.class, code); 
			result = vaccineTypeIoOperation.deleteVaccineType(foundVaccineType);
			
			assertEquals(true, result);
			result = vaccineTypeIoOperation.isCodePresent(code);			
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
		testVaccineTypeContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
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