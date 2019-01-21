package org.isf.visits.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.patient.model.Patient;
import org.isf.patient.test.TestPatient;
import org.isf.patient.test.TestPatientContext;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.visits.model.Visit;
import org.isf.visits.service.VisitsIoOperations;
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
	private static TestVisit testVisit;
	private static TestVisitContext testVisitContext;
	private static TestPatient testPatient;
	private static TestPatientContext testPatientContext;

    @Autowired
    VisitsIoOperations visitsIoOperation;
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testVisit = new TestVisit();
    	testVisitContext = new TestVisitContext();
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
    	//jpa.destroy();
    	testVisit = null;
    	testVisitContext = null;
    	testPatient = null;
    	testPatientContext = null;
    	
    	return;
    }
	
		
	@Test
	public void testVisitGets() 
	{
		int id = 0;
			

		try 
		{		
			id = _setupTestVisit(false);
			_checkVisitIntoDb(id);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testVisitSets() 
	{
		int id = 0;
			

		try 
		{		
			id = _setupTestVisit(true);
			_checkVisitIntoDb(id);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetVisit() 
	{
		int id = 0;
		
		
		try 
		{		
			id = _setupTestVisit(false);
			Visit foundVisit = (Visit)jpa.find(Visit.class, id); 
			ArrayList<Visit> visits = visitsIoOperation.getVisits(foundVisit.getPatient().getCode());
			
			assertEquals(foundVisit.getDate(), visits.get(visits.size()-1).getDate());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewVisit() 
	{
		int id = 0;		
							
		
		try 
		{		
			Patient patient = testPatient.setup(false);
			jpa.beginTransaction();	
			jpa.persist(patient);
			jpa.commitTransaction();
			Visit visit = testVisit.setup(patient, true);
			id = visitsIoOperation.newVisit(visit);
			
			_checkVisitIntoDb(id);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeleteVisit() 
	{
		int id = 0;
		boolean result = false;
		

		try 
		{		
			id = _setupTestVisit(false);
			Visit foundVisit = (Visit)jpa.find(Visit.class, id); 
			result = visitsIoOperation.deleteAllVisits(foundVisit.getPatient().getCode());

			assertEquals(true, result);
			result = visitsIoOperation.isCodePresent(id);			
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
		testPatientContext.saveAll(jpa);
		testVisitContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		testVisitContext.deleteNews(jpa);
		testPatientContext.deleteNews(jpa);
        
        return;
    }
        
	private int _setupTestVisit(
			boolean usingSet) throws OHException 
	{
		Visit visit;
		Patient patient = testPatient.setup(false);
					

    	jpa.beginTransaction();	
    	jpa.persist(patient);
    	visit = testVisit.setup(patient, usingSet);
		jpa.persist(visit);
    	jpa.commitTransaction();
    	
		return visit.getVisitID();
	}
		
	private void  _checkVisitIntoDb(
			int id) throws OHException 
	{
		Visit foundVisit;
		

		foundVisit = (Visit)jpa.find(Visit.class, id); 
		testVisit.check(foundVisit);
		
		return;
	}	
}