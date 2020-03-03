package org.isf.patient.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.isf.patient.model.Patient;
import org.isf.patient.service.PatientIoOperations;
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
	private static TestPatient testPatient;
	private static TestPatientContext testPatientContext;

    @Autowired
    PatientIoOperations patientIoOperation;

    @BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testPatient = new TestPatient();
    	testPatientContext = new TestPatientContext();

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
	public void testPatientGets() throws OHException 
	{
		Integer code = 0;
				

		try 
		{		
			code = _setupTestPatient(false);
			_checkPatientIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testPatientSets() 
	{
		Integer code = 0;
		
				
		try 
		{		
			code = _setupTestPatient(true);	
			_checkPatientIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testIoGetPatients() 
	{		
		try 
		{		
			_setupTestPatient(false);
			ArrayList<Patient> patients = patientIoOperation.getPatients();
			
			testPatient.check( patients.get(patients.size()-1));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetPatientsWithHeightAndWeight() 
	{	
		try 
		{		
			_setupTestPatient(false);
			// Pay attention that query return with PAT_ID descendant
			ArrayList<Patient> patients = patientIoOperation.getPatientsWithHeightAndWeight(null);

			testPatient.check(patients.get(0));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetPatientsWithHeightAndWeightRegEx() 
	{	
		try 
		{	
			Integer code = _setupTestPatient(false);
			Patient foundPatient = (Patient)jpa.find(Patient.class, code); 
			ArrayList<Patient> patients = patientIoOperation.getPatientsWithHeightAndWeight(foundPatient.getFirstName());
			
			testPatient.check(patients.get(0));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetPatientFromName() 
	{	
		try 
		{	
			Integer code = _setupTestPatient(false);
			Patient foundPatient = (Patient)jpa.find(Patient.class, code); 
			Patient patient = patientIoOperation.getPatient(foundPatient.getName());
			
			assertEquals(foundPatient.getName(), patient.getName());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetPatientFromCode() 
	{	
		try 
		{		
			Integer code = _setupTestPatient(false);
			Patient foundPatient = (Patient)jpa.find(Patient.class, code); 
			Patient patient = patientIoOperation.getPatient(code);

			assertEquals(foundPatient.getName(), patient.getName());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetPatientAll() 
	{	
		Integer code = 0;
		
		
		try 
		{		
			code = _setupTestPatient(false);
			Patient foundPatient = (Patient)jpa.find(Patient.class, code); 
			Patient patient = patientIoOperation.getPatientAll(code);
			
			
			assertEquals(foundPatient.getName(), patient.getName());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testNewPatient() 
	{		
		try 
		{		
			Patient patient = testPatient.setup(true);
            boolean result = patientIoOperation.newPatient(patient);
			
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
	public void testUpdatePatientTrue() 
	{
		
		try 
		{		
			Integer code = _setupTestPatient(false);
			Patient patient = (Patient)jpa.find(Patient.class, code); 
			jpa.flush();
			boolean result = patientIoOperation.updatePatient(patient);
			
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
	public void testDeletePatient() 
	{		
		try 
		{		
			Integer code = _setupTestPatient(false);
			Patient patient = (Patient)jpa.find(Patient.class, code); 
			boolean result = patientIoOperation.deletePatient(patient);
			Patient deletedPatient = _getDeletedPatient(code);
			
			assertEquals(true, result);
			assertEquals(code, deletedPatient.getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}	
	
	@Test
	public void testIsPatientPresent() 
	{	
		try 
		{		
			Integer code = _setupTestPatient(false);
			Patient foundPatient = (Patient)jpa.find(Patient.class, code); 
			boolean result = patientIoOperation.isPatientPresent(foundPatient.getName());
			
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
	public void testGetNextPatientCode() 
	{
		Integer code = 0;
		Integer max = 0;
		
		
		try 
		{				
			code = _setupTestPatient(false);
			max = patientIoOperation.getNextPatientCode();			
			assertEquals(max, (code + 1), 0.1);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testMergePatientHistory()
	{		
		//TODO: function not yet ported to JPA
		assertEquals(1, 1);
		
		return;
	}	
	
	
	private void _saveContext() throws OHException 
    {	
		testPatientContext.saveAll(jpa);
        		
        return;
    }
		
    private void _restoreContext() throws OHException 
    {
		testPatientContext.deleteNews(jpa);
        
        return;
    }
    	
	private Integer _setupTestPatient(
			boolean usingSet) throws OHException 
	{
		Patient patient;
	
		
    	jpa.beginTransaction();	
    	patient = testPatient.setup(usingSet);
		jpa.persist(patient);
    	jpa.commitTransaction();
				    	
		return patient.getCode();
	}
		
	private void _checkPatientIntoDb(
			Integer code) throws OHException 
	{
		Patient foundPatient; 
			

		foundPatient = (Patient)jpa.find(Patient.class, code); 
		testPatient.check(foundPatient);
		
		return;
	}
		
	@SuppressWarnings("unchecked")
	private Patient _getDeletedPatient(
			Integer Code) throws OHException 
	{	
		ArrayList<Object> params = new ArrayList<Object>();
		
		
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM PATIENT WHERE PAT_DELETED = 'Y' AND PAT_ID = ?", Patient.class, false);
		params.add(Code);
		jpa.setParameters(params, false);
		List<Patient> patients = (List<Patient>)jpa.getList();
		jpa.commitTransaction();
		
		return patients.get(0);
	}
}

