package org.isf.lab.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.exa.model.Exam;
import org.isf.exa.test.TestExam;
import org.isf.exa.test.TestExamContext;
import org.isf.exatype.model.ExamType;
import org.isf.exatype.test.TestExamType;
import org.isf.exatype.test.TestExamTypeContext;
import org.isf.lab.manager.LabManager;
import org.isf.lab.model.Laboratory;
import org.isf.lab.model.LaboratoryForPrint;
import org.isf.lab.model.LaboratoryRow;
import org.isf.lab.service.LabIoOperations;
import org.isf.patient.model.Patient;
import org.isf.patient.test.TestPatient;
import org.isf.patient.test.TestPatientContext;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class Tests  
{
	private final static Logger logger = LoggerFactory.getLogger(Tests.class);
	
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
    private LabIoOperations labIoOperation;
    
    @Autowired
    private LabManager labManager; 
	
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
		Integer id = 0;
		
		try 
		{	
			id = _setupTestLaboratoryRow(false);
			LaboratoryRow foundLaboratoryRow = (LaboratoryRow)jpa.find(LaboratoryRow.class, id);
			ArrayList<LaboratoryRow> laboratoryRows = labIoOperation.getLabRow(foundLaboratoryRow.getLabId().getCode());
			
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
			ArrayList<Laboratory> laboratories = labIoOperation.getLaboratory(foundLaboratoryRow.getLabId().getPatient());
			
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
		Integer id = 0;
		
		
		try 
		{		
                    id = _setupTestLaboratory(false);
                    Laboratory foundLaboratory = (Laboratory)jpa.find(Laboratory.class, id); 
                    ArrayList<LaboratoryForPrint> laboratories = labIoOperation.getLaboratoryForPrint(foundLaboratory.getExam().getDescription(), foundLaboratory.getExamDate(), foundLaboratory.getExamDate());
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
			Exam exam = testExam.setup(examType, 1, false);
			Patient patient = testPatient.setup(false);			
			jpa.persist(examType);
			jpa.persist(exam);
			jpa.persist(patient);
			jpa.commitTransaction();
			
			Laboratory laboratory = testLaboratory.setup(exam, patient, false);
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
			Exam exam = testExam.setup(examType, 2, false);
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
	public void testIoNewLabSecondProcedureTransaction() 
	{
		boolean result = false;
		ArrayList<String> labRow = new ArrayList<String>();
		Laboratory laboratory = null;
		
		try 
		{				
			jpa.beginTransaction();	
			ExamType examType = testExamType.setup(false);		
			Exam exam = testExam.setup(examType, 2, false);
			Patient patient = testPatient.setup(false);			
			jpa.persist(examType);
			jpa.persist(exam);
			jpa.persist(patient);
			jpa.commitTransaction();
			
			laboratory = testLaboratory.setup(exam, patient, false);
			labRow.add("TestLabRow");
			labRow.add("TestLabRowTestLabRowTestLabRowTestLabRowTestLabRowTestLabRow"); // Causing rollback
			result = labIoOperation.newLabSecondProcedure(laboratory, labRow);
			
			assertEquals(true, result);
			_checkLaboratoryIntoDb(laboratory.getCode());
		}
		catch (OHServiceException e) 
		{
			logger.debug("==> Voluntary Exception: " + e);
			try {
				Laboratory foundlaboratory = (Laboratory)jpa.find(Laboratory.class, laboratory.getCode());
				assertEquals(null, foundlaboratory);
			}
			catch (Exception e1)
			{
				logger.debug("==> Test Exception: " + e);		
				assertEquals(true, false);
			}
		}
		catch (Exception e)
		{
			logger.debug("==> Test Exception: " + e);		
			assertEquals(true, false);
		}

		return;
	}
	
	@Test
	public void testManagerNewLaboratoryTransaction()
	{
		boolean result = false;
		Laboratory laboratory = null;
		ArrayList<Laboratory> laboratories = new ArrayList<Laboratory>();
		ArrayList<ArrayList<String>> labRowList = new ArrayList<ArrayList<String>>();
		
		try 
		{				
			jpa.beginTransaction();	
			ExamType examType = testExamType.setup(false);		
			Exam exam = testExam.setup(examType, 1, false);
			Exam exam2 = testExam.setup(examType, 2, false);
			exam2.setCode("ZZZ");
			Patient patient = testPatient.setup(false);			
			jpa.persist(examType);
			jpa.persist(exam);
			jpa.persist(exam2);
			jpa.persist(patient);
			jpa.commitTransaction();
			
			// laboratory 1, Procedure One
			ArrayList<String> labRow = new ArrayList<String>();
			laboratory = testLaboratory.setup(exam, patient, false);
			laboratories.add(laboratory);
			labRowList.add(labRow);
			
			// laboratory 2, Procedure Two
			Laboratory laboratory2 = testLaboratory.setup(exam2, patient, false);
			laboratories.add(laboratory2);
			labRow.add("TestLabRow");
			labRow.add("TestLabRowTestLabRowTestLabRowTestLabRowTestLabRowTestLabRow"); // Causing rollback
			labRowList.add(labRow);
			
			labManager.setIoOperations(labIoOperation); 
			result = labManager.newLaboratory(laboratories, labRowList);
			
			assertEquals(true, result);
			_checkLaboratoryIntoDb(laboratory.getCode());
		}
		catch (OHServiceException e) 
		{
			logger.debug("==> Voluntary Exception: ");
			for (OHExceptionMessage error : e.getMessages()) logger.debug("    " + error.getMessage());
		}
		catch (Exception e)
		{
			logger.debug("==> Test Exception: " + e);		
			assertEquals(true, false);
		} 
		return;
	}
	
	@Test
	public void testIoUpdateLaboratory() 
	{
		Integer code = 0;
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestLaboratory(false);
			Laboratory foundlaboratory = (Laboratory)jpa.find(Laboratory.class, code);
			jpa.flush();
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
		Integer code = 0;
		ArrayList<String> labRow = new ArrayList<String>();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestLaboratoryRow(false);
			LaboratoryRow foundLaboratoryRow = (LaboratoryRow)jpa.find(LaboratoryRow.class, code); 
			labRow.add("Update");
			result = labIoOperation.updateLabSecondProcedure(foundLaboratoryRow.getLabId(), labRow);
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
		Integer code = 0;
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
		catch (OHServiceException e) 
		{
			logger.debug("==> Test Exception: " + e);		
			e.printStackTrace();
			assertEquals(true, false);
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
    
	private Integer _setupTestLaboratory(
			boolean usingSet) throws OHException 
	{
		Laboratory laboratory;
		ExamType examType = testExamType.setup(false);		
		Exam exam = testExam.setup(examType, 1, false);
		Patient patient = testPatient.setup(false);
		
	
		jpa.beginTransaction();	
		laboratory = testLaboratory.setup(exam, patient, usingSet);
		jpa.persist(examType);
		jpa.persist(exam);
		jpa.persist(patient);
		jpa.persist(laboratory);
		jpa.commitTransaction();
		
		return laboratory.getCode();
	}
		
	private void  _checkLaboratoryIntoDb(
			Integer code) throws OHException 
	{
		Laboratory foundLaboratory;
		
	
		foundLaboratory = (Laboratory)jpa.find(Laboratory.class, code); 
		testLaboratory.check(foundLaboratory);
		
		return;
	}	
	
	private Integer _setupTestLaboratoryRow(
			boolean usingSet) throws OHException 
	{			
		LaboratoryRow laboratoryRow;
		ExamType examType = testExamType.setup(false);		
		Exam exam = testExam.setup(examType, 2, false);
		Patient patient = testPatient.setup(false);
		Laboratory laboratory = testLaboratory.setup(exam, patient, false);
		

    	jpa.beginTransaction();	
		jpa.persist(examType);
		jpa.persist(exam);
		jpa.persist(patient);
		jpa.persist(laboratory);
		laboratoryRow = testLaboratoryRow.setup(laboratory, usingSet);
		jpa.persist(laboratoryRow);
    	jpa.commitTransaction();
    	
		return laboratoryRow.getCode();
	}
		
	private void  _checkLaboratoryRowIntoDb(
			Integer code) throws OHException 
	{
		LaboratoryRow foundLaboratoryRow;
		

		foundLaboratoryRow = (LaboratoryRow)jpa.find(LaboratoryRow.class, code); 
		testLaboratoryRow.check(foundLaboratoryRow);
		
		return;
	}	
}