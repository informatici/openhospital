package org.isf.examination.test;


import org.isf.examination.model.PatientExamination;
import org.isf.examination.service.ExaminationOperations;
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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class Tests 
{
	private static DbJpaUtil jpa;
	private static TestPatient testPatient;
	private static TestPatientExamination testPatientExamination;
	private static TestPatientContext testPatientContext;
	private static TestPatientExaminationContext testPatientExaminationContext;

    @Autowired
    ExaminationOperations examinationOperations;
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testPatient = new TestPatient();
    	testPatientExamination = new TestPatientExamination();
    	testPatientContext = new TestPatientContext();
    	testPatientExaminationContext = new TestPatientExaminationContext();

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
    	testPatient = null;
    	testPatientExamination = null;
    	testPatientContext = null;
    	testPatientExaminationContext = null;

    	return;
    }
	
		
	@Test
	public void testPatientExaminationGets() 
	{
		int id = 0;
			

		try 
		{		
			id = _setupTestPatientExamination(false);
			_checkPatientExaminationIntoDb(id);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testPatientExaminationSets() 
	{
		int id = 0;
			

		try 
		{		
			id = _setupTestPatientExamination(true);
			_checkPatientExaminationIntoDb(id);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testGetDefaultPatientExamination()
	{
		try 
		{		
			Patient	patient = testPatient.setup(false);

			
			PatientExamination patientExamination = examinationOperations.getDefaultPatientExamination(patient);
			testPatient.check(patientExamination.getPatient());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}	
	
	@Test
	public void testGetFromLastPatientExamination()
	{		
		try 
		{		
			Patient	patient = testPatient.setup(false);		
			PatientExamination lastPatientExamination = testPatientExamination.setup(patient, false);	

			
			PatientExamination patientExamination = examinationOperations.getFromLastPatientExamination(lastPatientExamination);
			testPatientExamination.check(patientExamination);
			testPatient.check(patientExamination.getPatient());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testSaveOrUpdate() 
	{
		int id = 0;
			

		try 
		{		
			id = _setupTestPatientExamination(false);	
			PatientExamination patientExamination = (PatientExamination)jpa.find(PatientExamination.class, id); 
			int pex_fc = patientExamination.getPex_fc();
			patientExamination.setPex_fc(pex_fc + 1);
			examinationOperations.saveOrUpdate(patientExamination);
			assertEquals((pex_fc + 1), patientExamination.getPex_fc());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testGetByID() 
	{
		int id = 0;
			

		try 
		{		
			id = _setupTestPatientExamination(false);	
			PatientExamination patientExamination = examinationOperations.getByID(id);
			testPatientExamination.check(patientExamination);
			testPatient.check(patientExamination.getPatient());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}	

	@Test
	public void testGetLastByPatID()
	{				
		try 
		{		
			jpa.beginTransaction();				
			Patient	patient = testPatient.setup(false);		
			PatientExamination lastPatientExamination = testPatientExamination.setup(patient, false);	
			jpa.persist(patient);
			jpa.persist(lastPatientExamination);
			jpa.commitTransaction();
			PatientExamination foundExamination = examinationOperations.getLastByPatID(patient.getCode());
			
			_checkPatientExaminationIntoDb(foundExamination.getPex_ID());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}	

	@Test
	public void testGetLastNByPatID()
	{				
		try 
		{		
			jpa.beginTransaction();				
			Patient	patient = testPatient.setup(false);		
			PatientExamination lastPatientExamination = testPatientExamination.setup(patient, false);	
			jpa.persist(patient);
			jpa.persist(lastPatientExamination);
			jpa.commitTransaction();
			ArrayList<PatientExamination> foundExamination = examinationOperations.getLastNByPatID(patient.getCode(), 1);
			
			_checkPatientExaminationIntoDb(foundExamination.get(0).getPex_ID());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}	

	@Test
	public void testGetByPatID()
	{	
		try 
		{		
			jpa.beginTransaction();				
			Patient	patient = testPatient.setup(false);		
			PatientExamination lastPatientExamination = testPatientExamination.setup(patient, false);	
			jpa.persist(patient);
			jpa.persist(lastPatientExamination);
			jpa.commitTransaction();
			ArrayList<PatientExamination> foundExamination = examinationOperations.getByPatID(patient.getCode());
			
			_checkPatientExaminationIntoDb(foundExamination.get(0).getPex_ID());
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
		testPatientContext.saveAll(jpa);
		testPatientExaminationContext.saveAll(jpa);
        		
        return;
    }
		
    private void _restoreContext() throws OHException 
    {
    	testPatientExaminationContext.deleteNews(jpa);
		testPatientContext.deleteNews(jpa);
        
        return;
    }
        
	private int _setupTestPatientExamination(
			boolean usingSet) throws OHException 
	{
		PatientExamination patientExamination;
		Patient	patient = testPatient.setup(false);
	

    	jpa.beginTransaction();	
    	patientExamination = testPatientExamination.setup(patient, usingSet);
		jpa.persist(patient);
		jpa.persist(patientExamination);
    	jpa.commitTransaction();
		
		return patientExamination.getPex_ID();
	}
		
	private void _checkPatientExaminationIntoDb(
			int id) throws OHException 
	{
		PatientExamination foundPatientExamination;
		

		foundPatientExamination = (PatientExamination)jpa.find(PatientExamination.class, id); 
		testPatientExamination.check(foundPatientExamination);
		testPatient.check(foundPatientExamination.getPatient());
		
		return;
	}
}