package org.isf.patvac.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.isf.patient.model.Patient;
import org.isf.patient.test.TestPatient;
import org.isf.patient.test.TestPatientContext;
import org.isf.patvac.model.PatientVaccine;
import org.isf.patvac.service.PatVacIoOperations;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.vaccine.model.Vaccine;
import org.isf.vaccine.test.TestVaccine;
import org.isf.vaccine.test.TestVaccineContext;
import org.isf.vactype.model.VaccineType;
import org.isf.vactype.test.TestVaccineType;
import org.isf.vactype.test.TestVaccineTypeContext;
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
	private static TestPatientVaccine testPatientVaccine;
	private static TestPatientVaccineContext testPatientVaccineContext;
	private static TestVaccine testVaccine;
	private static TestVaccineContext testVaccineContext;
	private static TestVaccineType testVaccineType;
	private static TestVaccineTypeContext testVaccineTypeContext;
	private static TestPatient testPatient;
	private static TestPatientContext testPatientContext;

    @Autowired
    PatVacIoOperations patvacIoOperation;
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testPatientVaccine = new TestPatientVaccine();
    	testPatientVaccineContext = new TestPatientVaccineContext();
    	testPatient = new TestPatient();
    	testPatientContext = new TestPatientContext();
    	testVaccine = new TestVaccine();
    	testVaccineContext = new TestVaccineContext();
    	testVaccineType = new TestVaccineType();
    	testVaccineTypeContext = new TestVaccineTypeContext();
    	
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
    	testPatientVaccine = null;
    	testPatientVaccineContext = null;
    	testPatient = null;
    	testPatientContext = null;
    	testVaccine = null;
    	testVaccineContext = null;
    	testVaccineType = null;
    	testVaccineTypeContext = null;

    	return;
    }
	
		
	@Test
	public void testPatientVaccineGets() throws OHException 
	{
		int code = 0;
			

		try 
		{		
			code = _setupTestPatientVaccine(false);
			_checkPatientVaccineIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}

	@Test
	public void testPatientVaccineSets() 
	{
		int code = 0;
			

		try 
		{		
			code = _setupTestPatientVaccine(true);
			_checkPatientVaccineIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetPatientVaccine() 
	{
		int code = 0;
		
		
		try 
		{		
			code = _setupTestPatientVaccine(false);
			PatientVaccine foundPatientVaccine = (PatientVaccine)jpa.find(PatientVaccine.class, code); 
			ArrayList<PatientVaccine> patientVaccines = patvacIoOperation.getPatientVaccine(
					foundPatientVaccine.getVaccine().getVaccineType().getCode(),
					foundPatientVaccine.getVaccine().getCode(),
					foundPatientVaccine.getVaccineDate(),
					foundPatientVaccine.getVaccineDate(),
					foundPatientVaccine.getPatient().getSex(),
					foundPatientVaccine.getPatient().getAge(),
					foundPatientVaccine.getPatient().getAge());
			
			assertEquals(foundPatientVaccine.getPatName(), patientVaccines.get(patientVaccines.size()-1).getPatName());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdatePatientVaccine() 
	{
		int code = 0;
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestPatientVaccine(false);
			PatientVaccine foundPatientVaccine = (PatientVaccine)jpa.find(PatientVaccine.class, code); 
			foundPatientVaccine.setPatName("Update");
			result = patvacIoOperation.updatePatientVaccine(foundPatientVaccine);
			PatientVaccine updatePatientVaccine = (PatientVaccine)jpa.find(PatientVaccine.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updatePatientVaccine.getPatName());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewPatientVaccine() 
	{
		boolean result = false;
		
		
		try 
		{		
	    	jpa.beginTransaction();	
	    	Patient patient = testPatient.setup(false);
	    	VaccineType vaccineType = testVaccineType.setup(false);
	    	Vaccine vaccine = testVaccine.setup(vaccineType, false);
	    	jpa.persist(patient);
	    	jpa.persist(vaccineType);
	    	jpa.persist(vaccine);
			jpa.commitTransaction();
	    	
			PatientVaccine patientVaccine = testPatientVaccine.setup(patient, vaccine, true);
			result = patvacIoOperation.newPatientVaccine(patientVaccine);
			
			assertEquals(true, result);
			_checkPatientVaccineIntoDb(patientVaccine.getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeletePatientVaccine() 
	{
		int code = 0;
		boolean result = false;
		

		try 
		{		
			code = _setupTestPatientVaccine(false);
			PatientVaccine foundPatientVaccine = (PatientVaccine)jpa.find(PatientVaccine.class, code); 
			result = patvacIoOperation.deletePatientVaccine(foundPatientVaccine);

			assertEquals(true, result);
			result = patvacIoOperation.isCodePresent(code);			
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
		int prog_year = 0;
		int found_prog_year = 0;

		try 
		{		
			_setupTestPatientVaccine(false);
			List<PatientVaccine> patientVaccineList = patvacIoOperation.getPatientVaccine(null, null, null, null, 'A', 0, 0);
			for (PatientVaccine patVac : patientVaccineList) {
				if (patVac.getProgr() > found_prog_year) found_prog_year = patVac.getProgr();
			}
			prog_year = patvacIoOperation.getProgYear(0);
			assertEquals(found_prog_year, prog_year);
			
			prog_year = patvacIoOperation.getProgYear(1984); //TestPatientVaccine's year
			assertEquals(10, prog_year);
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
		testPatientVaccineContext.saveAll(jpa);
		testVaccineTypeContext.saveAll(jpa);
		testVaccineContext.saveAll(jpa);
		testPatientContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		testPatientVaccineContext.deleteNews(jpa);
		testPatientContext.deleteNews(jpa);
		testVaccineContext.deleteNews(jpa);
		testVaccineTypeContext.deleteNews(jpa);
        
        return;
    }
        
	private int _setupTestPatientVaccine(
			boolean usingSet) throws OHException 
	{
		PatientVaccine patientVaccine;
		VaccineType vaccineType = testVaccineType.setup(false);
		Vaccine vaccine = testVaccine.setup(vaccineType, false);
		Patient patient = testPatient.setup(false);
		

    	jpa.beginTransaction();	
    	patientVaccine = testPatientVaccine.setup(patient, vaccine, usingSet);
		jpa.persist(vaccineType);
		jpa.persist(vaccine);
		jpa.persist(patient);
		jpa.persist(patientVaccine);
    	jpa.commitTransaction();
    	
		return patientVaccine.getCode();
	}
		
	private void  _checkPatientVaccineIntoDb(
			int code) throws OHException 
	{
		PatientVaccine foundPatientVaccine;
		

		foundPatientVaccine = (PatientVaccine)jpa.find(PatientVaccine.class, code); 
		testPatientVaccine.check(foundPatientVaccine);
		
		return;
	}	
}