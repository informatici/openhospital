package org.isf.ward.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;
import org.isf.ward.service.WardIoOperations;
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
	private static TestWard testWard;
	private static TestWardContext testWardContext;

    @Autowired
    WardIoOperations wardIoOperation;
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testWard = new TestWard();
    	testWardContext = new TestWardContext();
    	
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
	public void testWardGets() 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestWard(false);
			_checkWardIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testWardSets() throws OHException 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestWard(true);
			_checkWardIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetWardsNoMaternity() 
	{
		String code = "";
		
		
		try 
		{		
			code = _setupTestWard(false);
			Ward foundWard = (Ward)jpa.find(Ward.class, code); 
			ArrayList<Ward> wards = wardIoOperation.getWardsNoMaternity();
			
			assertEquals(foundWard.getDescription(), wards.get(wards.size()-1).getDescription());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetWards() 
	{
		String code = "";
		
		
		try 
		{		
			code = _setupTestWard(false);
			Ward foundWard = (Ward)jpa.find(Ward.class, code); 
			ArrayList<Ward> wards = wardIoOperation.getWards(code);			
			
			assertEquals(foundWard.getDescription(), wards.get(0).getDescription());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewWard() 
	{
		boolean result = false;
		
		
		try 
		{		
			Ward ward = testWard.setup(true);
			result = wardIoOperation.newWard(ward);
			
			assertEquals(true, result);
			_checkWardIntoDb(ward.getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateWard() 
	{
		String code = "";
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestWard(false);
			Ward foundWard = (Ward)jpa.find(Ward.class, code);
			jpa.flush();
			foundWard.setDescription("Update");
			result = wardIoOperation.updateWard(foundWard);
			Ward updateWard = (Ward)jpa.find(Ward.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateWard.getDescription());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateWardNoCodePresent() 
	{
		boolean result = false;
				

		try 
		{		
			Ward ward = testWard.setup(true);
			ward.setCode("X");
			result = wardIoOperation.updateWard(ward);
			
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
	public void testIoDeleteWard() 
	{
		String code = "";
		boolean result = false;
		

		try 
		{		
			code = _setupTestWard(false);
			Ward foundWard = (Ward)jpa.find(Ward.class, code); 
			result = wardIoOperation.deleteWard(foundWard);

			assertEquals(true, result);
			result = wardIoOperation.isCodePresent(code);			
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
	public void testIoIsCodePresent() 
	{
		String code = "";
		boolean result = false;
				

		try 
		{		
			code = _setupTestWard(false);
			result = wardIoOperation.isCodePresent(code);
			
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
	public void testIoIsCodePresentFalse() 
	{
		boolean result = false;
				

		try 
		{		
			result = wardIoOperation.isCodePresent("X");
			
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
	public void testIoIsMaternityPresent() 
	{
		boolean result = false;
				

		try 
		{		
			result = wardIoOperation.isMaternityPresent();
			
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
		testWardContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		testWardContext.deleteNews(jpa);
        
        return;
    }
        
	private String _setupTestWard(
			boolean usingSet) throws OHException 
	{
		Ward ward;
		

    	jpa.beginTransaction();	
    	ward = testWard.setup(usingSet);
		jpa.persist(ward);
    	jpa.commitTransaction();
    	
		return ward.getCode();
	}
		
	private void  _checkWardIntoDb(
			String code) throws OHException 
	{
		Ward foundWard;
		

		foundWard = (Ward)jpa.find(Ward.class, code); 
		testWard.check(foundWard);
		
		return;
	}	
}