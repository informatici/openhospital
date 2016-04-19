package org.isf.supplier.test;


import static org.junit.Assert.assertEquals;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.supplier.model.Supplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestSupplier testSupplier;
	private static TestSupplierContext testSupplierContext;
		
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testSupplier = new TestSupplier();
    	testSupplierContext = new TestSupplierContext();
    	
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
	public void testSupplierGets() 
	{
		int code = 0;
			
		
		try 
		{		
			code = _setupTestSupplier(false);
			_checkSupplierIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testSupplierSets() 
	{
		int code = 0;
			

		try 
		{		
			code = _setupTestSupplier(true);
			_checkSupplierIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testSupplierSaveOrUpdate() 
	{
		assertEquals(true, false);
		
		return;
	}
	
	@Test
	public void testSupplierGetByID() 
	{
		assertEquals(true, false);
		
		return;
	}
	
	@Test
	public void testSupplierGetAll() 
	{
		assertEquals(true, false);
		
		return;
	}
	
	@Test
	public void testSupplierGetList() 
	{
		assertEquals(true, false);
		
		return;
	}
		
	
	private void _saveContext() throws OHException 
    {	
		testSupplierContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		System.out.println(testSupplierContext.getAllSaved());
		testSupplierContext.deleteNews(jpa);
        
        return;
    }
    
	private int _setupTestSupplier(
			boolean usingSet) throws OHException 
	{
		Supplier supplier;
		
	
		jpa.beginTransaction();	
		supplier = testSupplier.setup(usingSet);
		jpa.persist(supplier);
		jpa.commitTransaction();
		
		return supplier.getSupId();
	}
		
	private void  _checkSupplierIntoDb(
			int code) throws OHException 
	{
		Supplier foundSupplier;
		
	
		foundSupplier = (Supplier)jpa.find(Supplier.class, code); 
		testSupplier.check(foundSupplier);
		
		return;
	}	
}