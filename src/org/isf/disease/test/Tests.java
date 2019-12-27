package org.isf.disease.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.disease.model.Disease;
import org.isf.disease.service.DiseaseIoOperations;
import org.isf.distype.model.DiseaseType;
import org.isf.distype.test.TestDiseaseType;
import org.isf.distype.test.TestDiseaseTypeContext;
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
	private static TestDisease testDisease;
	private static TestDiseaseType testDiseaseType;
	private static TestDiseaseContext testDiseaseContext;
	private static TestDiseaseTypeContext testDiseaseTypeContext;

    @Autowired
    DiseaseIoOperations diseaseIoOperation;
    
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testDisease = new TestDisease();
    	testDiseaseType = new TestDiseaseType();
    	testDiseaseContext = new TestDiseaseContext();
    	testDiseaseTypeContext = new TestDiseaseTypeContext();
    	
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
    	testDisease = null;
    	testDiseaseType = null;
    	testDiseaseContext = null;
    	testDiseaseTypeContext = null;

    	return;
    }
	
		
	@Test
	public void testDiseaseGets() 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestDisease(false);
			_checkDiseaseIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testDiseaseSets() 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestDisease(true);
			_checkDiseaseIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetDiseaseByCode()  
	{
		String code = "";
		
		
		try 
		{		
			code = _setupTestDisease(false);
			Disease foundDisease = diseaseIoOperation.getDiseaseByCode(Integer.parseInt(code));
			
			testDisease.check(foundDisease);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetDiseases() 
	{
		String code = "";
		
		
		try 
		{		
			code = _setupTestDisease(false);
			Disease foundDisease = (Disease)jpa.find(Disease.class, code); 

			ArrayList<Disease> diseases = diseaseIoOperation.getDiseases(foundDisease.getType().getCode(), false, false, false);
			assertEquals(true, diseases.contains(foundDisease));
			
			diseases = diseaseIoOperation.getDiseases(foundDisease.getType().getCode(), true, false, false);
			assertEquals(false, diseases.contains(foundDisease));
			foundDisease.setOpdInclude(true);
			jpa.beginTransaction();	
			jpa.persist(foundDisease);
			jpa.commitTransaction();
			diseases = diseaseIoOperation.getDiseases(foundDisease.getType().getCode(), true, false, false);
			assertEquals(true, diseases.contains(foundDisease));

			foundDisease = (Disease)jpa.find(Disease.class, code);
			diseases = diseaseIoOperation.getDiseases(foundDisease.getType().getCode(), true, true, false);
			assertEquals(false, diseases.contains(foundDisease));
			foundDisease.setOpdInclude(true);
			foundDisease.setIpdInInclude(true);
			jpa.beginTransaction();	
			jpa.persist(foundDisease);
			jpa.commitTransaction();
			diseases = diseaseIoOperation.getDiseases(foundDisease.getType().getCode(), true, true, false);
			assertEquals(true, diseases.contains(foundDisease));

			foundDisease = (Disease)jpa.find(Disease.class, code);
			diseases = diseaseIoOperation.getDiseases(foundDisease.getType().getCode(), true, true, true);
			assertEquals(false, diseases.contains(foundDisease));
			foundDisease.setOpdInclude(true);
			foundDisease.setIpdInInclude(true);
			foundDisease.setIpdOutInclude(true);
			jpa.beginTransaction();	
			jpa.persist(foundDisease);
			jpa.commitTransaction();
			diseases = diseaseIoOperation.getDiseases(foundDisease.getType().getCode(), true, true, true);
			assertEquals(true, diseases.contains(foundDisease));
			
			diseases = diseaseIoOperation.getDiseases(foundDisease.getType().getCode(), false, true, true);
			assertEquals(true, diseases.contains(foundDisease));
			
			diseases = diseaseIoOperation.getDiseases(foundDisease.getType().getCode(), false, false, true);
			assertEquals(true, diseases.contains(foundDisease));
			
			diseases = diseaseIoOperation.getDiseases(foundDisease.getType().getCode(), false, true, false);
			assertEquals(true, diseases.contains(foundDisease));
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewDisease() 
	{
		boolean result = false; 
		
		
		try 
		{		
			DiseaseType diseaseType = testDiseaseType.setup(false);

			
			jpa.beginTransaction();	
			Disease disease = testDisease.setup(diseaseType, true);
			jpa.persist(diseaseType);
			jpa.commitTransaction();
			result = diseaseIoOperation.newDisease(disease);
			
			assertEquals(true, result);
			_checkDiseaseIntoDb(disease.getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateDisease()
	{
		String code = "";
		boolean result = false;
		
		
		try 
		{		
 			code = _setupTestDisease(false);
 			Disease foundDisease = (Disease)jpa.find(Disease.class, code); 
			jpa.flush();
 			foundDisease.setDescription("Update");
 			result = diseaseIoOperation.updateDisease(foundDisease);
			jpa.open();
 			Disease updateDisease = (Disease)jpa.find(Disease.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateDisease.getDescription());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoHasDiseaseModified() 
	{
		String code = "";
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestDisease(false);
			Disease foundDisease = (Disease)jpa.find(Disease.class, code);
			jpa.flush();
			result = diseaseIoOperation.deleteDisease(foundDisease);
			jpa.open();
 			assertEquals(true, result);
 			assertEquals(false, foundDisease.getIpdInInclude());
 			assertEquals(false, foundDisease.getIpdOutInclude());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoDeleteDisease() 
	{
		String code = "";
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestDisease(false);
			Disease foundDisease = (Disease)jpa.find(Disease.class, code);
			jpa.flush();
			result = diseaseIoOperation.deleteDisease(foundDisease);
			jpa.open();
			assertEquals(true, result);
			assertEquals(false, foundDisease.getIpdInInclude());
			assertEquals(false, foundDisease.getIpdOutInclude());
			assertEquals(false, foundDisease.getOpdInclude());
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
			code = _setupTestDisease(false);
			result = diseaseIoOperation.isCodePresent(code);
			
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
	public void testIoIsDescriptionPresent() 
	{
		String code = "";
		boolean result = false;
		

		try 
		{		
			code = _setupTestDisease(false);
			Disease foundDisease = (Disease)jpa.find(Disease.class, code); 
			result = diseaseIoOperation.isDescriptionPresent(foundDisease.getDescription(), foundDisease.getType().getCode());
			
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
		testDiseaseContext.saveAll(jpa);
		testDiseaseTypeContext.saveAll(jpa);
		testDiseaseContext.addMissingKey(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		testDiseaseContext.deleteNews(jpa);
		testDiseaseTypeContext.deleteNews(jpa);
        
        return;
    }
        
	private String _setupTestDisease(
			boolean usingSet) throws OHException 
	{
		Disease disease;
		DiseaseType diseaseType = testDiseaseType.setup(false);
		

    	jpa.beginTransaction();	
    	disease = testDisease.setup(diseaseType, usingSet);
    	jpa.persist(diseaseType);
		jpa.persist(disease);
    	jpa.commitTransaction();
    	
		return disease.getCode();
	}
		
	private void  _checkDiseaseIntoDb(
			String code) throws OHException 
	{
		Disease foundDisease;
		

		foundDisease = (Disease)jpa.find(Disease.class, code); 
		testDisease.check(foundDisease);
		
		return;
	}	
}