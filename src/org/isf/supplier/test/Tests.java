package org.isf.supplier.test;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.supplier.model.Supplier;
import org.isf.supplier.service.SupplierOperations;
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
		SupplierOperations ioOperations = new SupplierOperations();
		boolean result = false;
		
		
		try 
		{		
			Supplier supplier = testSupplier.setup(true);
			result = ioOperations.saveOrUpdate(supplier);
			
			assertEquals(true, result);
			_checkSupplierIntoDb(supplier.getSupId());
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testSupplierGetByID() 
	{	
		SupplierOperations ioOperations = new SupplierOperations();
		int code = 0;
		
		
		try 
		{		
			code = _setupTestSupplier(false);
			Supplier foundSupplier = ioOperations.getByID(code);
			
			_checkSupplierIntoDb(foundSupplier.getSupId());
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testSupplierGetAll() 
	{		
		int code = 0;
		SupplierOperations ioOperations = new SupplierOperations();
		
		
		try 
		{		
			code = _setupTestSupplier(false);
			Supplier foundSupplier = (Supplier)jpa.find(Supplier.class, code); 
			List<Supplier> suppliers = ioOperations.getAll();			
			
			assertEquals(foundSupplier.getSupNote(), suppliers.get(0).getSupNote());
		} 
		catch (Exception e) 
		{
			System.out.println("Test Exception" + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testSupplierGetList() 
	{	
		int code = 0;
		SupplierOperations ioOperations = new SupplierOperations();
		
		
		try 
		{		
			code = _setupTestSupplier(false);
			Supplier foundSupplier = (Supplier)jpa.find(Supplier.class, code); 
			List<Supplier> suppliers = ioOperations.getList();			
			
			assertEquals(foundSupplier.getSupNote(), suppliers.get(0).getSupNote());
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