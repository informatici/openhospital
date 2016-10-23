package org.isf.dlvrtype.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.dlvrtype.service.DeliveryTypeIoOperation;
import org.isf.dlvrtype.model.DeliveryType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestDeliveryType testDeliveryType;
	private static TestDeliveryTypeContext testDeliveryTypeContext;
		
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testDeliveryType = new TestDeliveryType();
    	testDeliveryTypeContext = new TestDeliveryTypeContext();
    	
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
	public void testDeliveryTypeGets() 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestDeliveryType(false);
			_checkDeliveryTypeIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testDeliveryTypeSets() 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestDeliveryType(true);
			_checkDeliveryTypeIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetDeliveryType() 
	{
		String code = "";
		DeliveryTypeIoOperation ioOperations = new DeliveryTypeIoOperation();
		
		
		try 
		{		
			code = _setupTestDeliveryType(false);
			DeliveryType foundDeliveryType = (DeliveryType)jpa.find(DeliveryType.class, code); 
			ArrayList<DeliveryType> deliveryTypes = ioOperations.getDeliveryType();
			
			assertEquals(foundDeliveryType.getDescription(), deliveryTypes.get(deliveryTypes.size()-1).getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateDeliveryType() 
	{
		String code = "";
		DeliveryTypeIoOperation ioOperations = new DeliveryTypeIoOperation();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestDeliveryType(false);
			DeliveryType foundDeliveryType = (DeliveryType)jpa.find(DeliveryType.class, code); 
			foundDeliveryType.setDescription("Update");
			result = ioOperations.updateDeliveryType(foundDeliveryType);
			DeliveryType updateDeliveryType = (DeliveryType)jpa.find(DeliveryType.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateDeliveryType.getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewDeliveryType() 
	{
		DeliveryTypeIoOperation ioOperations = new DeliveryTypeIoOperation();
		boolean result = false;
		
		
		try 
		{		
			DeliveryType deliveryType = testDeliveryType.setup(true);
			result = ioOperations.newDeliveryType(deliveryType);
			
			assertEquals(true, result);
			_checkDeliveryTypeIntoDb(deliveryType.getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeleteDeliveryType() 
	{
		String code = "";
		DeliveryTypeIoOperation ioOperations = new DeliveryTypeIoOperation();
		boolean result = false;
		

		try 
		{		
			code = _setupTestDeliveryType(false);
			DeliveryType foundDeliveryType = (DeliveryType)jpa.find(DeliveryType.class, code); 
			result = ioOperations.deleteDeliveryType(foundDeliveryType);
			
			assertEquals(true, result);
			DeliveryType deletedDeliveryType = (DeliveryType)jpa.find(DeliveryType.class, code); 
			assertEquals(null, deletedDeliveryType);
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
		DeliveryTypeIoOperation ioOperations = new DeliveryTypeIoOperation();
		boolean result = false;
		

		try 
		{		
			code = _setupTestDeliveryType(false);
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
		testDeliveryTypeContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		System.out.println(testDeliveryTypeContext.getAllSaved());
		testDeliveryTypeContext.deleteNews(jpa);
        
        return;
    }
        
	private String _setupTestDeliveryType(
			boolean usingSet) throws OHException 
	{
		DeliveryType deliveryType;
		

    	jpa.beginTransaction();	
    	deliveryType = testDeliveryType.setup(usingSet);
		jpa.persist(deliveryType);
    	jpa.commitTransaction();
    	
		return deliveryType.getCode();
	}
		
	private void  _checkDeliveryTypeIntoDb(
			String code) throws OHException 
	{
		DeliveryType foundDeliveryType;
		

		foundDeliveryType = (DeliveryType)jpa.find(DeliveryType.class, code); 
		testDeliveryType.check(foundDeliveryType);
		
		return;
	}	
}