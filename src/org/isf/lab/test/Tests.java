package org.isf.lab.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.exa.model.Exam;
import org.isf.exa.test.TestExam;
import org.isf.exa.test.TestExamContext;
import org.isf.exatype.model.ExamType;
import org.isf.exatype.test.TestExamType;
import org.isf.exatype.test.TestExamTypeContext;
import org.isf.lab.model.Laboratory;
import org.isf.lab.model.LaboratoryForPrint;
import org.isf.lab.model.LaboratoryRow;
import org.isf.lab.test.TestLaboratoryRow;
import org.isf.lab.test.TestLaboratoryRowContext;
import org.isf.lab.service.LabIoOperations;
import org.isf.lab.test.TestLaboratory;
import org.isf.patient.model.Patient;
import org.isf.patient.test.TestPatient;
import org.isf.patient.test.TestPatientContext;
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
	private static TestLaboratory testLaboratory;
	private static TestLaboratoryRow testLaboratoryRow;
	private static TestExam testExam;
	private static TestExamType testExamType;
	private static TestPatient testPatient;
	private static TestLaboratoryContext testLaboratoryContext;
	private static TestLaboratoryRowContext testLaboratoryRowContext;
	private static TestExamContext testExamContext;
	private static TestExamTypeContext testExamTypeContext;
	private static TestPatientContext testPatientContext;

    @Autowired
    LabIoOperations labIoOperation;
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testLaboratory = new TestLaboratory();
    	testLaboratoryRow = new TestLaboratoryRow();
    	testLaboratoryContext = new TestLaboratoryContext();
    	testLaboratoryRowContext = new TestLaboratoryRowContext();
    	testExam = new TestExam();
    	testExamType = new TestExamType();
    	testExamContext = new TestExamContext();
    	testExamTypeContext = new TestExamTypeContext();
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
    	testLaboratory = null;
    	testLaboratoryRow = null;
    	testLaboratoryContext = null;
    	testLaboratoryRowContext = null;
    	testExam = null;
    	testExamType = null;
    	testExamContext = null;
    	testExamTypeContext = null;
    	testPatient = null;
    	testPatientContext = null;

    	return;
    }
	
	
	@Test
	public void testLaboratoryGets() 
	{
		int code = 0;
			
		
		try 
		{		
			code = _setupTestLaboratory(false);
			_checkLaboratoryIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testLaboratorySets() 
	{
		int code = 0;
			

		try 
		{		
			code = _setupTestLaboratory(true);
			_checkLaboratoryIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testLaboratoryRowGets() 
	{
		int code = 0;
			
		
		try 
		{		
			code = _setupTestLaboratoryRow(false);
			_checkLaboratoryRowIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testLaboratoryRowSets() 
	{
		int code = 0;
			

		try 
		{		
			code = _setupTestLaboratoryRow(true);
			_checkLaboratoryRowIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetLabRowByLabId() 
	{
		int id = 0;
		
		
		try 
		{		
			id = _setupTestLaboratoryRow(false);
			LaboratoryRow foundLaboratoryRow = (LaboratoryRow)jpa.find(LaboratoryRow.class, id); 
			ArrayList<LaboratoryRow> laboratoryRows = labIoOperation.getLabRow(id);
			
			assertEquals(true, laboratoryRows.contains(foundLaboratoryRow));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetLaboratory() 
	{
		int id = 0;
		
		
		try 
		{		
			id = _setupTestLaboratory(false);
			Laboratory foundLaboratory = (Laboratory)jpa.find(Laboratory.class, id); 
			ArrayList<Laboratory> laboratories = labIoOperation.getLaboratory(foundLaboratory.getExam().getDescription(), foundLaboratory.getExamDate(), foundLaboratory.getExamDate());
			
			assertEquals(foundLaboratory.getCode(), laboratories.get(0).getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetLaboratoryFromPatient() 
	{
		int id = 0;
		
		
		try 
		{		
			id = _setupTestLaboratoryRow(false);
			LaboratoryRow foundLaboratoryRow = (LaboratoryRow)jpa.find(LaboratoryRow.class, id); 
			ArrayList<Laboratory> laboratories = labIoOperation.getLaboratory(foundLaboratoryRow.getLabId().getPatId());
			
			assertEquals(foundLaboratoryRow.getLabId().getCode(), laboratories.get(0).getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetLaboratoryForPrint() 
	{
		int id = 0;
		
		
		try 
		{		
			id = _setupTestLaboratory(false);
			Laboratory foundLaboratory = (Laboratory)jpa.find(Laboratory.class, id); 
			ArrayList<LaboratoryForPrint> laboratories = labIoOperation.getLaboratoryForPrint(foundLaboratory.getExam().getDescription(), foundLaboratory.getDate(), foundLaboratory.getDate());
			
			assertEquals(foundLaboratory.getCode(), laboratories.get(0).getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewLabFirstProcedure() 
	{
		boolean result = false;
		
		
		try 
		{				
			jpa.beginTransaction();	
			ExamType examType = testExamType.setup(false);		
			Exam exam = testExam.setup(examType, false);
			Patient patient = testPatient.setup(false);			
			jpa.persist(examType);
			jpa.persist(exam);
			jpa.persist(patient);
			jpa.commitTransaction();
			
			Laboratory laboratory = testLaboratory.setup(exam, patient, false);;
			result = labIoOperation.newLabFirstProcedure(laboratory);
			
			assertEquals(true, result);
			_checkLaboratoryIntoDb(laboratory.getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewLabSecondProcedure() 
	{
		boolean result = false;
		ArrayList<String> labRow = new ArrayList<String>();
		
		
		try 
		{				
			jpa.beginTransaction();	
			ExamType examType = testExamType.setup(false);		
			Exam exam = testExam.setup(examType, false);
			Patient patient = testPatient.setup(false);			
			jpa.persist(examType);
			jpa.persist(exam);
			jpa.persist(patient);
			jpa.commitTransaction();
			
			Laboratory laboratory = testLaboratory.setup(exam, patient, false);
			labRow.add("TestLabRow");
			result = labIoOperation.newLabSecondProcedure(laboratory, labRow);
			
			assertEquals(true, result);
			_checkLaboratoryIntoDb(laboratory.getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateLaboratory() 
	{
		int code = 0;
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestLaboratory(false);
			Laboratory foundlaboratory = (Laboratory)jpa.find(Laboratory.class, code); 
			foundlaboratory.setNote("Update");
			result = labIoOperation.updateLabFirstProcedure(foundlaboratory);
			Laboratory updateLaboratory = (Laboratory)jpa.find(Laboratory.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateLaboratory.getNote());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	
	@Test
	public void testIoEditLabSecondProcedure() 
	{
		int code = 0;
		ArrayList<String> labRow = new ArrayList<String>();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestLaboratoryRow(false);
			LaboratoryRow foundLaboratoryRow = (LaboratoryRow)jpa.find(LaboratoryRow.class, code); 
			labRow.add("Update");
			result = labIoOperation.editLabSecondProcedure(foundLaboratoryRow.getLabId(), labRow);
			LaboratoryRow updateLaboratoryRow = (LaboratoryRow)jpa.find(LaboratoryRow.class, (code + 1)); 
			
			assertEquals(true, result);
			assertEquals("Update", updateLaboratoryRow.getDescription());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeleteLaboratory() 
	{
		int code = 0;
		boolean result = false;
		

		try 
		{		
			code = _setupTestLaboratory(false);
			Laboratory foundLaboratory = (Laboratory)jpa.find(Laboratory.class, code); 
			result = labIoOperation.deleteLaboratory(foundLaboratory);
			
			assertEquals(true, result);
			result = labIoOperation.isCodePresent(code);			
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
		testLaboratoryContext.saveAll(jpa);
		testLaboratoryRowContext.saveAll(jpa);
		testExamContext.saveAll(jpa);
		testExamTypeContext.saveAll(jpa);
		testPatientContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		testLaboratoryRowContext.deleteNews(jpa);
		testLaboratoryContext.deleteNews(jpa);
		testExamContext.deleteNews(jpa);
		testExamTypeContext.deleteNews(jpa);
		testPatientContext.deleteNews(jpa);
        
        return;
    }
    
	private int _setupTestLaboratory(
			boolean usingSet) throws OHException 
	{
		Laboratory laboratory;
		ExamType examType = testExamType.setup(false);		
		Exam exam = testExam.setup(examType, false);
		Patient patient = testPatient.setup(false);
		
	
		jpa.beginTransaction();	
		laboratory = testLaboratory.setup(exam, patient, usingSet);
		jpa.persist(examType);
		exam.setProcedure(2);
		jpa.persist(exam);
		jpa.persist(patient);
		jpa.persist(laboratory);
		jpa.commitTransaction();
		
		return laboratory.getCode();
	}
		
	private void  _checkLaboratoryIntoDb(
			int code) throws OHException 
	{
		Laboratory foundLaboratory;
		
	
		foundLaboratory = (Laboratory)jpa.find(Laboratory.class, code); 
		testLaboratory.check(foundLaboratory);
		
		return;
	}	
	
	private int _setupTestLaboratoryRow(
			boolean usingSet) throws OHException 
	{			
		LaboratoryRow laboratoryRow;
		ExamType examType = testExamType.setup(false);		
		Exam exam = testExam.setup(examType, false);
		Patient patient = testPatient.setup(false);
		Laboratory laboratory = testLaboratory.setup(exam, patient, false);
		

    	jpa.beginTransaction();	
    	laboratoryRow = testLaboratoryRow.setup(laboratory, usingSet);
		jpa.persist(examType);
		jpa.persist(exam);
		jpa.persist(patient);
		jpa.persist(laboratory);
		jpa.persist(laboratoryRow);
    	jpa.commitTransaction();
    	
		return laboratoryRow.getCode();
	}
		
	private void  _checkLaboratoryRowIntoDb(
			int code) throws OHException 
	{
		LaboratoryRow foundLaboratoryRow;
		

		foundLaboratoryRow = (LaboratoryRow)jpa.find(LaboratoryRow.class, code); 
		testLaboratoryRow.check(foundLaboratoryRow);
		
		return;
	}	
}