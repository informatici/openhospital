package org.isf.pricesothers.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.pricesothers.model.PricesOthers;
import org.isf.pricesothers.service.PriceOthersIoOperations;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestPricesOthers testPricesOthers;
	private static TestPricesOthersContext testPricesOthersContext;
		
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testPricesOthers = new TestPricesOthers();
    	testPricesOthersContext = new TestPricesOthersContext();
    	
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
	public void testPricesOthersGets() 
	{
		int id = 0;
			

		try 
		{		
			id = _setupTestPricesOthers(false);
			_checkPricesOthersIntoDb(id);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testPricesOthersSets() 
	{
		int id = 0;
			

		try 
		{		
			id = _setupTestPricesOthers(true);
			_checkPricesOthersIntoDb(id);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetPricesOthers() 
	{
		int id = 0;
		PriceOthersIoOperations ioOperations = new PriceOthersIoOperations();
		
		
		try 
		{		
			id = _setupTestPricesOthers(false);
			PricesOthers foundPricesOthers = (PricesOthers)jpa.find(PricesOthers.class, id); 
			ArrayList<PricesOthers> pricesOtherss = ioOperations.getOthers();
			
			assertEquals(foundPricesOthers.getDescription(), pricesOtherss.get(1).getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdatePricesOthers() 
	{
		int id = 0;
		PriceOthersIoOperations ioOperations = new PriceOthersIoOperations();
		boolean result = false;
		
		
		try 
		{		
			id = _setupTestPricesOthers(false);
			PricesOthers foundPricesOthers = (PricesOthers)jpa.find(PricesOthers.class, id); 
			foundPricesOthers.setDescription("Update");
			result = ioOperations.updateOther(foundPricesOthers);
			PricesOthers updatePricesOthers = (PricesOthers)jpa.find(PricesOthers.class, id); 
			
			assertEquals(true, result);
			assertEquals("Update", updatePricesOthers.getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewPricesOthers() 
	{
		PriceOthersIoOperations ioOperations = new PriceOthersIoOperations();
		boolean result = false;
		
		
		try 
		{		
			PricesOthers pricesOthers = testPricesOthers.setup(true);
			result = ioOperations.newOthers(pricesOthers);
			
			assertEquals(true, result);
			_checkPricesOthersIntoDb(pricesOthers.getId());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeletePricesOthers() 
	{
		int id = 0;
		PriceOthersIoOperations ioOperations = new PriceOthersIoOperations();
		boolean result = false;
		

		try 
		{		
			id = _setupTestPricesOthers(false);
			PricesOthers foundPricesOthers = (PricesOthers)jpa.find(PricesOthers.class, id); 
			result = ioOperations.deleteOthers(foundPricesOthers);
			
			assertEquals(true, result);
			PricesOthers deletedPricesOthers = (PricesOthers)jpa.find(PricesOthers.class, id); 
			assertEquals(null, deletedPricesOthers);
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
		testPricesOthersContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		System.out.println(testPricesOthersContext.getAllSaved());
		testPricesOthersContext.deleteNews(jpa);
        
        return;
    }
        
	private int _setupTestPricesOthers(
			boolean usingSet) throws OHException 
	{
		PricesOthers pricesOthers;
		

    	jpa.beginTransaction();	
    	pricesOthers = testPricesOthers.setup(usingSet);
		jpa.persist(pricesOthers);
    	jpa.commitTransaction();
    	
		return pricesOthers.getId();
	}
		
	private void  _checkPricesOthersIntoDb(
			int id) throws OHException 
	{
		PricesOthers foundPricesOthers;
		

		foundPricesOthers = (PricesOthers)jpa.find(PricesOthers.class, id); 
		testPricesOthers.check(foundPricesOthers);
		
		return;
	}	
}