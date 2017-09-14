package org.isf.operation.test;


import static org.junit.Assert.*;

import java.util.ArrayList;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.operation.model.Operation;
import org.isf.operation.service.OperationIoOperations;
import org.isf.opetype.model.OperationType;
import org.isf.opetype.test.TestOperationType;
import org.isf.opetype.test.TestOperationTypeContext;
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
	private static TestOperation testOperation;
	private static TestOperationType testOperationType;
	private static TestOperationContext testOperationContext;
	private static TestOperationTypeContext testOperationTypeContext;
		
	
	@BeforeClass
    public static void setUpClass()  
    {
    	
    	testOperation = new TestOperation();
    	testOperationType = new TestOperationType();
    	testOperationContext = new TestOperationContext();
    	testOperationTypeContext = new TestOperationTypeContext();
    	
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
    	testOperation = null;
    	testOperationType = null;
    	testOperationContext = null;
    	testOperationTypeContext = null;

    	return;
    }
	
		
	@Test
	public void testOperationGets() throws OHException 
	{
		String code = "";
	
		
		try 
		{		
			code = _setupTestOperation(false);
			_checkOperationIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testOperationSets() throws OHException 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestOperation(true);
			_checkOperationIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetOperations() throws OHException 
	{
		String code = "";
		OperationIoOperations ioOperations = new OperationIoOperations();
		
		
		try 
		{		
			code = _setupTestOperation(false);
			Operation foundOperation = (Operation)jpa.find(Operation.class, code); 
			ArrayList<Operation> operations = ioOperations.getOperation(foundOperation.getDescription());
			
			assertEquals(foundOperation.getDescription(), operations.get(0).getDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewOperation() throws OHException 
	{
		OperationIoOperations ioOperations = new OperationIoOperations();
		OperationType operationType = testOperationType.setup(false);
		boolean result = false;
		
		
		try 
		{		
			jpa.beginTransaction();	
			Operation operation = testOperation.setup(operationType, true);
			jpa.persist(operationType);
			jpa.commitTransaction();
			result = ioOperations.newOperation(operation);
			
			assertEquals(true, result);
			_checkOperationIntoDb(operation.getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoHasOperationModified() throws OHException 
	{
		String code = "";
		OperationIoOperations ioOperations = new OperationIoOperations();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestOperation(false);
			Operation foundOperation = (Operation)jpa.find(Operation.class, code);
			result = ioOperations.hasOperationModified(foundOperation);
			
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
	public void testIoUpdateOperation() throws OHException 
	{
		String code = "";
		OperationIoOperations ioOperations = new OperationIoOperations();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestOperation(false);
			Operation foundOperation = (Operation)jpa.find(Operation.class, code); 
			int lock = foundOperation.getLock().intValue();
			foundOperation.setDescription("Update");
			result = ioOperations.updateOperation(foundOperation);
			Operation updateOperation = (Operation)jpa.find(Operation.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateOperation.getDescription());
			assertEquals(lock + 1, updateOperation.getLock().intValue());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoDeleteOperation() throws OHException 
	{
		String code = "";
		OperationIoOperations ioOperations = new OperationIoOperations();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestOperation(false);
			Operation foundOperation = (Operation)jpa.find(Operation.class, code); 
			result = ioOperations.deleteOperation(foundOperation);
			
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
	public void testIoIsCodePresent() throws OHException 
	{
		String code = "";
		OperationIoOperations ioOperations = new OperationIoOperations();
		boolean result = false;
		

		try 
		{		
			code = _setupTestOperation(false);
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
	public void testIoIsDescriptionPresent() throws OHException 
	{
		String code = "";
		OperationIoOperations ioOperations = new OperationIoOperations();
		boolean result = false;
		

		try 
		{		
			code = _setupTestOperation(false);
			Operation foundOperation = (Operation)jpa.find(Operation.class, code); 
			result = ioOperations.isDescriptionPresent(foundOperation.getDescription(), foundOperation.getType().getCode());
			
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
		testOperationContext.saveAll(jpa);
		testOperationTypeContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		testOperationContext.deleteNews(jpa);
		testOperationTypeContext.deleteNews(jpa);
        
        return;
    }
        
	private String _setupTestOperation(
			boolean usingSet) throws OHException 
	{
		Operation operation;
		OperationType operationType = testOperationType.setup(false);
		

    	jpa.beginTransaction();	
    	operation = testOperation.setup(operationType, usingSet);
    	jpa.persist(operationType);
		jpa.persist(operation);
    	jpa.commitTransaction();
    	
		return operation.getCode();
	}
		
	private void  _checkOperationIntoDb(
			String code) throws OHException 
	{
		Operation foundOperation;
		

		foundOperation = (Operation)jpa.find(Operation.class, code); 
		testOperation.check(foundOperation);
		
		return;
	}	
}