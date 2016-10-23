package org.isf.opd.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
    	jpa.destroy();

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
			System.out.println("==> Test Exception: " + e);		
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
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetOpd()
	{
		int code = 0;
		OpdIoOperations ioOperations = new OpdIoOperations();
		
		
		try 
		{		
			code = _setupTestOpd(false);
			Opd foundOpd = (Opd)jpa.find(Opd.class, code); 
			ArrayList<Opd> opds = ioOperations.getOpdList(
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
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewOpd() throws OHException
	{
		OpdIoOperations ioOperations = new OpdIoOperations();
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
			result = ioOperations.newOpd(opd);
			
			assertEquals(true, result);
			_checkOpdIntoDb(opd.getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testOpdIoHasOpdModified() 
	{
		OpdIoOperations ioOperations = new OpdIoOperations();
		int code = 0;
		boolean result = false;
			

		try 
		{		
			code = _setupTestOpd(true);
			Opd foundOpd = (Opd)jpa.find(Opd.class, code); 
			result = ioOperations.hasOpdModified(foundOpd);			
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
	public void testIoUpdateOpd() 
	{
		int code = 0;
		OpdIoOperations ioOperations = new OpdIoOperations();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestOpd(false);
			Opd foundOpd = (Opd)jpa.find(Opd.class, code); 
			foundOpd.setNote("Update");
			result = ioOperations.updateOpd(foundOpd);
			Opd updateOpd = (Opd)jpa.find(Opd.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateOpd.getNote());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeleteOpd() 
	{
		int code = 0;
		OpdIoOperations ioOperations = new OpdIoOperations();
		boolean result = false;
		

		try 
		{		
			code = _setupTestOpd(false);
			Opd foundOpd = (Opd)jpa.find(Opd.class, code); 
			result = ioOperations.deleteOpd(foundOpd);
			
			assertEquals(true, result);
			Opd deletedOpd = (Opd)jpa.find(Opd.class, code); 
			assertEquals(null, deletedOpd);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetProgYear()  
	{
		int code = 0;
		OpdIoOperations ioOperations = new OpdIoOperations();
		int progYear = 0;
		

		try 
		{		
			code = _setupTestOpd(false);
			progYear = ioOperations.getProgYear(0);

			Opd foundOpd = (Opd)jpa.find(Opd.class, code); 
			assertEquals(foundOpd.getYear(), progYear);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetLastOpd()  
	{
		int code = 0;
		OpdIoOperations ioOperations = new OpdIoOperations();
		

		try 
		{		
			code = _setupTestOpd(false);
			Opd foundOpd = (Opd)jpa.find(Opd.class, code); 
			Opd lastOpd = ioOperations.getLastOpd(foundOpd.getpatientCode().getCode());

			assertEquals(foundOpd.getCode(), lastOpd.getCode());
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
		testOpdContext.saveAll(jpa);
		testPatientContext.saveAll(jpa);
		testDiseaseContext.saveAll(jpa);
		testDiseaseTypeContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		System.out.println(testOpdContext.getAllSaved());
		System.out.println(testPatientContext.getAllSaved());
		System.out.println(testDiseaseContext.getAllSaved());
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