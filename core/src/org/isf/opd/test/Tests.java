package org.isf.opd.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;

import org.isf.disease.model.Disease;
import org.isf.disease.test.TestDisease;
import org.isf.disease.test.TestDiseaseContext;
import org.isf.distype.model.DiseaseType;
import org.isf.distype.test.TestDiseaseType;
import org.isf.distype.test.TestDiseaseTypeContext;
import org.isf.opd.model.Opd;
import org.isf.opd.service.OpdIoOperations;
import org.isf.patient.model.Patient;
import org.isf.patient.test.TestPatient;
import org.isf.patient.test.TestPatientContext;
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
	private static TestOpd testOpd;
	private static TestOpdContext testOpdContext;
	private static TestPatient testPatient;
	private static TestPatientContext testPatientContext;
	private static TestDiseaseType testDiseaseType;
	private static TestDiseaseTypeContext testDiseaseTypeContext;
	private static TestDisease testDisease;
	private static TestDiseaseContext testDiseaseContext;

    @Autowired
    OpdIoOperations opdIoOperation;	
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testOpd = new TestOpd();
    	testOpdContext = new TestOpdContext();
    	testPatient = new TestPatient();
    	testPatientContext = new TestPatientContext();
    	testDisease = new TestDisease();
    	testDiseaseContext = new TestDiseaseContext();
    	testDiseaseType = new TestDiseaseType();
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
    	//jpa.destroy();
    	testOpd = null;
    	testOpdContext = null;
    	testPatient = null;
    	testPatientContext = null;
    	testDisease = null;
    	testDiseaseContext = null;
    	testDiseaseType = null;
    	testDiseaseTypeContext = null;

    	return;
    }
	
		
	@Test
	public void testOpdGets() 
	{
		int code = 0;
			

		try 
		{		
			code = _setupTestOpd(false);
			_checkOpdIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testOpdSets() 
	{
		int code = 0;
			

		try 
		{		
			code = _setupTestOpd(true);
			_checkOpdIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetOpd()
	{
		int code = 0;
		
		
		try 
		{		
			code = _setupTestOpd(false);
			Opd foundOpd = (Opd)jpa.find(Opd.class, code); 
			ArrayList<Opd> opds = opdIoOperation.getOpdList(
					foundOpd.getDisease().getType().getCode(), 
					foundOpd.getDisease().getCode(),
					foundOpd.getVisitDate(),
					foundOpd.getVisitDate(),
					foundOpd.getAge()-1,
					foundOpd.getAge()+1,
					foundOpd.getSex(),
					foundOpd.getNewPatient());			
						
			assertEquals(foundOpd.getCode(), opds.get(opds.size()-1).getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewOpd() throws OHException
	{
		Patient	patient = testPatient.setup(false); 
		DiseaseType diseaseType = testDiseaseType.setup(false);
		Disease disease = testDisease.setup(diseaseType, false);
		boolean result = false;

    	
		try 
		{		
			jpa.beginTransaction();	
			jpa.persist(patient);
			jpa.persist(diseaseType);
			jpa.persist(disease);
			jpa.commitTransaction();

			Opd opd = testOpd.setup(patient, disease, false);
	    	opd.setDate(new Date());
			result = opdIoOperation.newOpd(opd);
			
			assertEquals(true, result);
			_checkOpdIntoDb(opd.getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateOpd() 
	{
		int code = 0;
		Opd result = null;
		
		
		try 
		{		
			code = _setupTestOpd(false);
			Opd foundOpd = (Opd)jpa.find(Opd.class, code); 
			jpa.flush();
			foundOpd.setNote("Update");
			result = opdIoOperation.updateOpd(foundOpd);
			jpa.open();
			Opd updateOpd = (Opd)jpa.find(Opd.class, code); 
			
			assertEquals(true, (result != null));
			assertEquals("Update", updateOpd.getNote());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeleteOpd() 
	{
		int code = 0;
		boolean result = false;
		

		try 
		{		
			code = _setupTestOpd(false);
			Opd foundOpd = (Opd)jpa.find(Opd.class, code); 
			result = opdIoOperation.deleteOpd(foundOpd);

			assertEquals(true, result);
			result = opdIoOperation.isCodePresent(code);			
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
	public void testIoGetProgYear()  
	{
		int code = 0;
		int progYear = 0;
		

		try 
		{		
			code = _setupTestOpd(false);
			progYear = opdIoOperation.getProgYear(0);

			Opd foundOpd = (Opd)jpa.find(Opd.class, code); 
			assertEquals(foundOpd.getProgYear(), progYear);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetLastOpd()  
	{
		int code = 0;
		

		try 
		{		
			code = _setupTestOpd(false);
			Opd foundOpd = (Opd)jpa.find(Opd.class, code); 
			Opd lastOpd = opdIoOperation.getLastOpd(foundOpd.getPatient().getCode());

			assertEquals(foundOpd.getCode(), lastOpd.getCode());
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
		testOpdContext.saveAll(jpa);
		testPatientContext.saveAll(jpa);
		testDiseaseContext.saveAll(jpa);
		testDiseaseTypeContext.saveAll(jpa);
		testDiseaseContext.addMissingKey(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		testOpdContext.deleteNews(jpa);
		testPatientContext.deleteNews(jpa);
		testDiseaseContext.deleteNews(jpa);
		testDiseaseTypeContext.deleteNews(jpa);
        
        return;
    }
        
	private int _setupTestOpd(
			boolean usingSet) throws OHException 
	{
		Opd opd;
		Patient	patient = testPatient.setup(false); 
		DiseaseType diseaseType = testDiseaseType.setup(false);
		Disease disease = testDisease.setup(diseaseType, false);
		

    	jpa.beginTransaction();	
    	opd = testOpd.setup(patient, disease, usingSet);
    	opd.setDate(new Date());
    	jpa.persist(patient);
    	jpa.persist(diseaseType);
    	jpa.persist(disease);
		jpa.persist(opd);
    	jpa.commitTransaction();
    	
		return opd.getCode();
	}
		
	private void  _checkOpdIntoDb(
			int code) throws OHException 
	{
		Opd foundOpd;
		

		foundOpd = (Opd)jpa.find(Opd.class, code); 
		testOpd.check(foundOpd);
		
		return;
	}	
}