package org.isf.patient.test;


import org.isf.patient.model.Patient;
import org.isf.patient.service.IoOperations;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List; 

import static org.junit.Assert.assertEquals;

public class Tests 
{	
	private static DbJpaUtil jpa;	
	private static TestPatient testPatient;
	private static TestPatientContext testPatientContext;
	

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
    	jpa.destroy();

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
			System.out.println("==> Test Exception: " + e);		
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
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testIoGetPatients() 
	{
		IoOperations ioOperations = new IoOperations();

		
		try 
		{		
			ArrayList<Patient> patients = ioOperations.getPatients();
			
			List<Patient> savePatients = testPatientContext.getAllSaved();
			assertEquals(savePatients.get(savePatients.size()-1).getName(), patients.get(patients.size()-1).getName());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetPatientsWithHeightAndWeight() 
	{
		IoOperations ioOperations = new IoOperations();
		
		
		try 
		{		
			ArrayList<Patient> patients = ioOperations.getPatientsWithHeightAndWeight(null);
			
			
			// Pay attention that query return with PAT_ID descendant
			List<Patient> savePatients = testPatientContext.getAllSaved();
			assertEquals(savePatients.get(savePatients.size()-1).getName(), patients.get(patients.size()-1).getName());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetPatientsWithHeightAndWeightRegEx() 
	{
		IoOperations ioOperations = new IoOperations();
		
		
		try 
		{		
			ArrayList<Patient> patients = ioOperations.getPatientsWithHeightAndWeight(testPatientContext.getAllSaved().get(1).getFirstName());
			
			
			List<Patient> savePatients = testPatientContext.getAllSaved();
			assertEquals(savePatients.get(savePatients.size()-1).getName(), patients.get(patients.size()-1).getName());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetPatientFromName() 
	{
		IoOperations ioOperations = new IoOperations();
		
		
		try 
		{		
			Patient patient = ioOperations.getPatient(testPatientContext.getAllSaved().get(1).getName());
			
			List<Patient> savePatients = testPatientContext.getAllSaved();
			assertEquals(savePatients.get(savePatients.size()-1).getName(), patient.getName());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetPatientFromCode() 
	{
		IoOperations ioOperations = new IoOperations();
		
		
		try 
		{		
			Patient patient = ioOperations.getPatient(testPatientContext.getAllSaved().get(1).getCode());
			

			List<Patient> savePatients = testPatientContext.getAllSaved();
			assertEquals(savePatients.get(savePatients.size()-1).getName(), patient.getName());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetPatientAll() 
	{
		IoOperations ioOperations = new IoOperations();
		
		
		try 
		{		
			Patient patient = ioOperations.getPatientAll(testPatientContext.getAllSaved().get(0).getCode());
			
			
			assertEquals(testPatientContext.getAllSaved().get(0).getName(), patient.getName());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testNewPatient() 
	{
		IoOperations ioOperations = new IoOperations();

		
		try 
		{		
			Patient patient = testPatient.setup(true);;
			boolean result = ioOperations.newPatient(patient);
			
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
	public void testUpdatePatientFalse() 
	{
		IoOperations ioOperations = new IoOperations();
		int lock = 0;
		
		
		try 
		{		
			Integer code = _setupTestPatient(false);
			Patient patient = (Patient)jpa.find(Patient.class, code); 
			lock = patient.getLock();
			boolean result = ioOperations.updatePatient(patient, false);
			
			assertEquals(true, result);
			assertEquals(lock, patient.getLock());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
		
	@Test
	public void testUpdatePatientTrue() 
	{
		IoOperations ioOperations = new IoOperations();
		int lock = 0;
		
		
		try 
		{		
			Integer code = _setupTestPatient(false);
			Patient patient = (Patient)jpa.find(Patient.class, code); 
			lock = patient.getLock();
			boolean result = ioOperations.updatePatient(patient, true);
			
			assertEquals(true, result);
			assertEquals((lock + 1), patient.getLock());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testDeletePatient() 
	{
		IoOperations ioOperations = new IoOperations();
		
		
		try 
		{		
			Integer code = _setupTestPatient(false);
			Patient patient = (Patient)jpa.find(Patient.class, code); 
			boolean result = ioOperations.deletePatient(patient);
			Patient deletedPatient = _getDeletedPatient(code);
			
			assertEquals(true, result);
			assertEquals(code, deletedPatient.getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}	
	
	@Test
	public void testIsPatientPresent() 
	{
		IoOperations ioOperations = new IoOperations();
		
		
		try 
		{		
			boolean result = ioOperations.isPatientPresent(testPatientContext.getAllSaved().get(1).getName());
			
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
	public void testGetNextPatientCode() 
	{
		IoOperations ioOperations = new IoOperations();
		Integer code = 0;
		Integer max = 0;
		
		
		try 
		{		
			code = ioOperations.getNextPatientCode();
			max = testPatientContext.getMaxCode();
			assertEquals(max, code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testMergePatientHistory()
	{		
		//TODO: function not yet ported to JPA. The test has to fail
		assertEquals(1, 2);
		
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

