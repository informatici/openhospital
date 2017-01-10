package org.isf.therapy.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.medicals.model.Medical;
import org.isf.medicals.test.TestMedical;
import org.isf.medicals.test.TestMedicalContext;
import org.isf.medtype.model.MedicalType;
import org.isf.medtype.test.TestMedicalType;
import org.isf.medtype.test.TestMedicalTypeContext;
import org.isf.patient.model.Patient;
import org.isf.patient.test.TestPatient;
import org.isf.patient.test.TestPatientContext;
import org.isf.therapy.model.TherapyRow;
import org.isf.therapy.service.TherapyIoOperations;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestTherapy testTherapyRow;
	private static TestTherapyContext testTherapyRowContext;
	private static TestMedical testMedical;
	private static TestMedicalContext testMedicalContext;
	private static TestMedicalType testMedicalType;
	private static TestMedicalTypeContext testMedicalTypeContext;
	private static TestPatient testPatient;
	private static TestPatientContext testPatientContext;
		
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testTherapyRow = new TestTherapy();
    	testTherapyRowContext = new TestTherapyContext();
    	testPatient = new TestPatient();
    	testPatientContext = new TestPatientContext();
    	testMedical = new TestMedical();
    	testMedicalContext = new TestMedicalContext();
    	testMedicalType = new TestMedicalType();
    	testMedicalTypeContext = new TestMedicalTypeContext();
    	
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
    	testTherapyRow = null;
    	testTherapyRowContext = null;
    	testPatient = null;
    	testPatientContext = null;
    	testMedical = null;
    	testMedicalContext = null;
    	testMedicalType = null;
    	testMedicalTypeContext = null;

    	return;
    }
	
		
	@Test
	public void testTherapyRowGets() 
	{
		int id = 0;
			

		try 
		{		
			id = _setupTestTherapyRow(false);
			_checkTherapyRowIntoDb(id);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testTherapyRowSets() 
	{
		int id = 0;
			

		try 
		{		
			id = _setupTestTherapyRow(true);
			_checkTherapyRowIntoDb(id);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetTherapyRow() 
	{
		int id = 0;
		TherapyIoOperations ioOperations = new TherapyIoOperations();
		
		
		try 
		{		
			id = _setupTestTherapyRow(false);
			TherapyRow foundTherapyRow = (TherapyRow)jpa.find(TherapyRow.class, id); 
			ArrayList<TherapyRow> therapyRows = ioOperations.getTherapyRows(foundTherapyRow.getPatID().getCode());
			
			assertEquals(foundTherapyRow.getNote(), therapyRows.get(therapyRows.size()-1).getNote());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewTherapyRow() 
	{
		TherapyIoOperations ioOperations = new TherapyIoOperations();
		int id = 0;		
							
		
		try 
		{		
			MedicalType medicalType = testMedicalType.setup(false);
			Medical medical = testMedical.setup(medicalType, false);
			Patient patient = testPatient.setup(false);
			jpa.beginTransaction();	
			jpa.persist(medicalType);
			jpa.persist(medical);
			jpa.persist(patient);
			jpa.commitTransaction();
			TherapyRow therapyRow = testTherapyRow.setup(patient, medical, true);
			id = ioOperations.newTherapy(therapyRow);
			
			_checkTherapyRowIntoDb(id);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeleteTherapyRow() 
	{
		int id = 0;
		TherapyIoOperations ioOperations = new TherapyIoOperations();
		boolean result = false;
		

		try 
		{		
			id = _setupTestTherapyRow(false);
			TherapyRow foundTherapyRow = (TherapyRow)jpa.find(TherapyRow.class, id); 
			result = ioOperations.deleteAllTherapies(foundTherapyRow.getPatID().getCode());
			
			assertEquals(true, result);
			TherapyRow deletedTherapyRow = (TherapyRow)jpa.find(TherapyRow.class, id); 
			assertEquals(null, deletedTherapyRow);
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
		testMedicalContext.saveAll(jpa);
		testMedicalTypeContext.saveAll(jpa);
		testPatientContext.saveAll(jpa);
		testTherapyRowContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		System.out.println(testTherapyRowContext.getAllSaved());
		testTherapyRowContext.deleteNews(jpa);
		testPatientContext.deleteNews(jpa);
		testMedicalContext.deleteNews(jpa);
		testMedicalTypeContext.deleteNews(jpa);
        
        return;
    }
        
	private int _setupTestTherapyRow(
			boolean usingSet) throws OHException 
	{
		TherapyRow therapyRow;
		MedicalType medicalType = testMedicalType.setup(false);
		Medical medical = testMedical.setup(medicalType, false);
		Patient patient = testPatient.setup(false);
					

    	jpa.beginTransaction();	
    	jpa.persist(medicalType);
    	jpa.persist(medical);
    	jpa.persist(patient);
    	therapyRow = testTherapyRow.setup(patient, medical, usingSet);
		jpa.persist(therapyRow);
    	jpa.commitTransaction();
    	
		return therapyRow.getTherapyID();
	}
		
	private void  _checkTherapyRowIntoDb(
			int id) throws OHException 
	{
		TherapyRow foundTherapyRow;
		

		foundTherapyRow = (TherapyRow)jpa.find(TherapyRow.class, id); 
		testTherapyRow.check(foundTherapyRow);
		
		return;
	}	
}