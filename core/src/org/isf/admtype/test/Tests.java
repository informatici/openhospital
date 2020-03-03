package org.isf.admtype.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.admtype.model.AdmissionType;
import org.isf.admtype.service.AdmissionTypeIoOperation;
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
	private static TestAdmissionType testAdmissionType;
	private static TestAdmissionTypeContext testAdmissionTypeContext;

    @Autowired
    AdmissionTypeIoOperation admissionTypeIoOperation;
    
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testAdmissionType = new TestAdmissionType();
    	testAdmissionTypeContext = new TestAdmissionTypeContext();
    	
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
    	testAdmissionType = null;
    	testAdmissionTypeContext = null;

    	return;
    }
	
		
	@Test
	public void testAdmissionTypeGets() 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestAdmissionType(false);
			_checkAdmissionTypeIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testAdmissionTypeSets()
	{
		String code = "";
			

		try 
		{		
			code = _setupTestAdmissionType(true);
			_checkAdmissionTypeIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetAdmissionType() 
	{		
		try 
		{		
			String code = _setupTestAdmissionType(false);
			AdmissionType foundAdmissionType = (AdmissionType)jpa.find(AdmissionType.class, code); 
			ArrayList<AdmissionType> admissionTypes = admissionTypeIoOperation.getAdmissionType();
			
			assertEquals(foundAdmissionType.getDescription(), admissionTypes.get(admissionTypes.size()-1).getDescription());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateAdmissionType() 
	{
		String code = "";
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestAdmissionType(false);
			AdmissionType foundAdmissionType = (AdmissionType)jpa.find(AdmissionType.class, code);
			jpa.flush();
			foundAdmissionType.setDescription("Update");
			result = admissionTypeIoOperation.updateAdmissionType(foundAdmissionType);
			AdmissionType updateAdmissionType = (AdmissionType)jpa.find(AdmissionType.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateAdmissionType.getDescription());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewAdmissionType() 
	{
		boolean result = false;
		
		
		try 
		{		
			AdmissionType admissionType = testAdmissionType.setup(true);
			result = admissionTypeIoOperation.newAdmissionType(admissionType);
			
			assertEquals(true, result);
			_checkAdmissionTypeIntoDb(admissionType.getCode());
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
			code = _setupTestAdmissionType(false);
			result = admissionTypeIoOperation.isCodePresent(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		assertEquals(true, result);
		
		return;
	}

	@Test
	public void testIoDeleteAdmissionType() 
	{
		String code = "";
		boolean result = false;
		

		try 
		{		
			code = _setupTestAdmissionType(false);
			AdmissionType foundAdmissionType = (AdmissionType)jpa.find(AdmissionType.class, code); 
			result = admissionTypeIoOperation.deleteAdmissionType(foundAdmissionType);
			
			assertEquals(true, result);
			result = admissionTypeIoOperation.isCodePresent(code);
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
		testAdmissionTypeContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		testAdmissionTypeContext.deleteNews(jpa);
        
        return;
    }
        
	private String _setupTestAdmissionType(
			boolean usingSet) throws OHException 
	{
		AdmissionType admissionType;
		

    	jpa.beginTransaction();	
    	admissionType = testAdmissionType.setup(usingSet);
		jpa.persist(admissionType);
    	jpa.commitTransaction();
    	
		return admissionType.getCode();
	}
		
	private void  _checkAdmissionTypeIntoDb(
			String code) throws OHException 
	{
		AdmissionType foundAdmissionType;
		

		foundAdmissionType = (AdmissionType)jpa.find(AdmissionType.class, code); 
		testAdmissionType.check(foundAdmissionType);
		
		return;
	}	
}