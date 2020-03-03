package org.isf.medstockmovtype.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.medstockmovtype.model.MovementType;
import org.isf.medstockmovtype.service.MedicalStockMovementTypeIoOperation;
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
	private static TestMovementType testMovementType;
	private static TestMovementTypeContext testMovementTypeContext;

    @Autowired
    MedicalStockMovementTypeIoOperation medicalStockMovementTypeIoOperation;
    
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testMovementType = new TestMovementType();
    	testMovementTypeContext = new TestMovementTypeContext();
    	
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
	public void testMovementTypeGets()
	{
		String code = "";
			

		try 
		{		
			code = _setupTestMovementType(false);
			_checkMovementTypeIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testMovementTypeSets() 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestMovementType(true);
			_checkMovementTypeIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetMovementType() 
	{
		String code = "";
		
		
		try 
		{		
			code = _setupTestMovementType(false);
			MovementType foundMovementType = (MovementType)jpa.find(MovementType.class, code); 
			ArrayList<MovementType> movementTypes = medicalStockMovementTypeIoOperation.getMedicaldsrstockmovType();
			
			assertEquals(foundMovementType.getDescription(), movementTypes.get(movementTypes.size()-1).getDescription());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateMovementType() 
	{
		String code = "";
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestMovementType(false);
			MovementType foundMovementType = (MovementType)jpa.find(MovementType.class, code); 
			foundMovementType.setDescription("Update");
			result = medicalStockMovementTypeIoOperation.updateMedicaldsrstockmovType(foundMovementType);
			MovementType updateMovementType = (MovementType)jpa.find(MovementType.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateMovementType.getDescription());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewMovementType() 
	{
		boolean result = false;
		
		
		try 
		{		
			MovementType movementType = testMovementType.setup(true);
			result = medicalStockMovementTypeIoOperation.newMedicaldsrstockmovType(movementType);
			
			assertEquals(true, result);
			_checkMovementTypeIntoDb(movementType.getCode());
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
			code = _setupTestMovementType(false);
			result = medicalStockMovementTypeIoOperation.isCodePresent(code);
			
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
	public void testIoDeleteMovementType() 
	{
		String code = "";
		boolean result = false;
		

		try 
		{		
			code = _setupTestMovementType(false);
			MovementType foundMovementType = (MovementType)jpa.find(MovementType.class, code); 
			result = medicalStockMovementTypeIoOperation.deleteMedicaldsrstockmovType(foundMovementType);
			
			assertEquals(true, result);
			result = medicalStockMovementTypeIoOperation.isCodePresent(code);			
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
		testMovementTypeContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		testMovementTypeContext.deleteNews(jpa);
        
        return;
    }
        
	private String _setupTestMovementType(
			boolean usingSet) throws OHException 
	{
		MovementType movementType;
		

    	jpa.beginTransaction();	
    	movementType = testMovementType.setup(usingSet);
		jpa.persist(movementType);
    	jpa.commitTransaction();
    	
		return movementType.getCode();
	}
		
	private void  _checkMovementTypeIntoDb(
			String code) throws OHException 
	{
		MovementType foundMovementType;
		

		foundMovementType = (MovementType)jpa.find(MovementType.class, code); 
		testMovementType.check(foundMovementType);
		
		return;
	}	
}