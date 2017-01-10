package org.isf.ward.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.ward.service.WardIoOperations;
import org.isf.ward.model.Ward;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestWard testWard;
	private static TestWardContext testWardContext;
		
	
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
    	jpa.destroy();
    	

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
			System.out.println("==> Test Exception: " + e);		
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
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetWardsNoMaternity() 
	{
		String code = "";
		WardIoOperations ioOperations = new WardIoOperations();
		
		
		try 
		{		
			code = _setupTestWard(false);
			Ward foundWard = (Ward)jpa.find(Ward.class, code); 
			ArrayList<Ward> wards = ioOperations.getWardsNoMaternity();
			
			assertEquals(foundWard.getDescription(), wards.get(wards.size()-1).getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetWards() 
	{
		String code = "";
		WardIoOperations ioOperations = new WardIoOperations();
		
		
		try 
		{		
			code = _setupTestWard(false);
			Ward foundWard = (Ward)jpa.find(Ward.class, code); 
			ArrayList<Ward> wards = ioOperations.getWards(code);			
			
			assertEquals(foundWard.getDescription(), wards.get(0).getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewWard() 
	{
		WardIoOperations ioOperations = new WardIoOperations();
		boolean result = false;
		
		
		try 
		{		
			Ward ward = testWard.setup(true);
			result = ioOperations.newWard(ward);
			
			assertEquals(true, result);
			_checkWardIntoDb(ward.getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateWard() 
	{
		String code = "";
		WardIoOperations ioOperations = new WardIoOperations();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestWard(false);
			Ward foundWard = (Ward)jpa.find(Ward.class, code); 
			foundWard.setDescription("Update");
			result = ioOperations.updateWard(foundWard, false);
			Ward updateWard = (Ward)jpa.find(Ward.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateWard.getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateWardNoCodePresent() 
	{
		WardIoOperations ioOperations = new WardIoOperations();
		boolean result = false;
				

		try 
		{		
			Ward ward = testWard.setup(true);
			ward.setCode("X");
			result = ioOperations.updateWard(ward, false);
			
			assertEquals(false, result);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateWardConfirmetOverriden() 
	{
		String code = "";
		WardIoOperations ioOperations = new WardIoOperations();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestWard(false);
			Ward cloneWard= testWard.setup(false);
			cloneWard.setLock(cloneWard.getLock()+1);
			cloneWard.setDescription("Update");
			result = ioOperations.updateWard(cloneWard, true);
			Ward updateWard = (Ward)jpa.find(Ward.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateWard.getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeleteWard() 
	{
		String code = "";
		WardIoOperations ioOperations = new WardIoOperations();
		boolean result = false;
		

		try 
		{		
			code = _setupTestWard(false);
			Ward foundWard = (Ward)jpa.find(Ward.class, code); 
			result = ioOperations.deleteWard(foundWard);
			
			assertEquals(true, result);
			Ward deletedWard = (Ward)jpa.find(Ward.class, code); 
			assertEquals(null, deletedWard);
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
		WardIoOperations ioOperations = new WardIoOperations();
		boolean result = false;
				

		try 
		{		
			code = _setupTestWard(false);
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

	@Test
	public void testIoIsCodePresentFalse() 
	{
		WardIoOperations ioOperations = new WardIoOperations();
		boolean result = false;
				

		try 
		{		
			result = ioOperations.isCodePresent("X");
			
			assertEquals(false, result);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}	

	@Test
	public void testIoIsMaternityPresent() 
	{
		WardIoOperations ioOperations = new WardIoOperations();
		boolean result = false;
				

		try 
		{		
			result = ioOperations.isMaternityPresent();
			
			assertEquals(true, result);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoIsLockWard() 
	{
		String code = "";
		WardIoOperations ioOperations = new WardIoOperations();
		boolean result = false;
				

		try 
		{		
			code = _setupTestWard(false);
			Ward foundWard = (Ward)jpa.find(Ward.class, code); 
			result = ioOperations.isLockWard(foundWard);
			
			assertEquals(true, result);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}	

	@Test
	public void testIoIsLockWardFalse() 
	{
		WardIoOperations ioOperations = new WardIoOperations();
		boolean result = false;
				

		try 
		{		
			_setupTestWard(false);
			Ward cloneWard= testWard.setup(false);
			cloneWard.setLock(cloneWard.getLock()+1);
			result = ioOperations.isLockWard(cloneWard);
			
			assertEquals(false, result);
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
		testWardContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		System.out.println(testWardContext.getAllSaved());
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