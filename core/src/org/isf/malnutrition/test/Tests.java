package org.isf.malnutrition.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.admission.model.Admission;
import org.isf.admission.test.TestAdmission;
import org.isf.admission.test.TestAdmissionContext;
import org.isf.admtype.model.AdmissionType;
import org.isf.admtype.test.TestAdmissionType;
import org.isf.admtype.test.TestAdmissionTypeContext;
import org.isf.disctype.model.DischargeType;
import org.isf.disctype.test.TestDischargeType;
import org.isf.disctype.test.TestDischargeTypeContext;
import org.isf.disease.model.Disease;
import org.isf.disease.test.TestDisease;
import org.isf.disease.test.TestDiseaseContext;
import org.isf.distype.model.DiseaseType;
import org.isf.distype.test.TestDiseaseType;
import org.isf.distype.test.TestDiseaseTypeContext;
import org.isf.dlvrrestype.model.DeliveryResultType;
import org.isf.dlvrrestype.test.TestDeliveryResultType;
import org.isf.dlvrrestype.test.TestDeliveryResultTypeContext;
import org.isf.dlvrtype.model.DeliveryType;
import org.isf.dlvrtype.test.TestDeliveryType;
import org.isf.dlvrtype.test.TestDeliveryTypeContext;
import org.isf.malnutrition.model.Malnutrition;
import org.isf.malnutrition.service.MalnutritionIoOperation;
import org.isf.operation.model.Operation;
import org.isf.operation.test.TestOperation;
import org.isf.operation.test.TestOperationContext;
import org.isf.opetype.model.OperationType;
import org.isf.opetype.test.TestOperationType;
import org.isf.opetype.test.TestOperationTypeContext;
import org.isf.patient.model.Patient;
import org.isf.patient.test.TestPatient;
import org.isf.patient.test.TestPatientContext;
import org.isf.pregtreattype.model.PregnantTreatmentType;
import org.isf.pregtreattype.test.TestPregnantTreatmentType;
import org.isf.pregtreattype.test.TestPregnantTreatmentTypeContext;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;
import org.isf.ward.test.TestWard;
import org.isf.ward.test.TestWardContext;
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
	private static TestAdmission testAdmission;
	private static TestAdmissionContext testAdmissionContext;
	private static TestWard testWard;
	private static TestWardContext testWardContext;
	private static TestPatient testPatient;
	private static TestPatientContext testPatientContext;
	private static TestAdmissionType testAdmissionType;
	private static TestAdmissionTypeContext testAdmissionTypeContext;
	private static TestDiseaseType testDiseaseType;
	private static TestDiseaseTypeContext testDiseaseTypeContext;
	private static TestDisease testDisease;
	private static TestDiseaseContext testDiseaseContext;
	private static TestOperationType testOperationType;
	private static TestOperationTypeContext testOperationTypeContext;
	private static TestOperation testOperation;
	private static TestOperationContext testOperationContext;
	private static TestDischargeType testDischargeType;
	private static TestDischargeTypeContext testDischargeTypeContext;
	private static TestPregnantTreatmentType testPregnantTreatmentType;
	private static TestPregnantTreatmentTypeContext testPregnantTreatmentTypeContext;
	private static TestDeliveryType testDeliveryType;
	private static TestDeliveryTypeContext testDeliveryTypeContext;
	private static TestDeliveryResultType testDeliveryResultType;
	private static TestDeliveryResultTypeContext testDeliveryResultTypeContext;
	private static TestMalnutrition testMalnutrition;
	private static TestMalnutritionContext testMalnutritionContext;

    @Autowired
    MalnutritionIoOperation malnutritionIoOperation;
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testAdmission = new TestAdmission();
    	testAdmissionContext = new TestAdmissionContext();
    	testWard = new TestWard();
    	testWardContext = new TestWardContext();
    	testPatient = new TestPatient();
    	testPatientContext = new TestPatientContext();
    	testAdmissionType = new TestAdmissionType();
    	testAdmissionTypeContext = new TestAdmissionTypeContext();
    	testDiseaseType = new TestDiseaseType();
    	testDiseaseTypeContext = new TestDiseaseTypeContext();
    	testDisease = new TestDisease();
    	testDiseaseContext = new TestDiseaseContext();
    	testOperationType = new TestOperationType();
    	testOperationTypeContext = new TestOperationTypeContext();
    	testOperation = new TestOperation();
    	testOperationContext = new TestOperationContext();
    	testDischargeType = new TestDischargeType();
    	testDischargeTypeContext = new TestDischargeTypeContext();
    	testPregnantTreatmentType = new TestPregnantTreatmentType();
    	testPregnantTreatmentTypeContext = new TestPregnantTreatmentTypeContext();
    	testDeliveryType = new TestDeliveryType();
    	testDeliveryTypeContext = new TestDeliveryTypeContext();
    	testDeliveryResultType = new TestDeliveryResultType();
    	testDeliveryResultTypeContext = new TestDeliveryResultTypeContext();
    	testMalnutrition = new TestMalnutrition();
    	testMalnutritionContext = new TestMalnutritionContext();
    	
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
    	testAdmission = null;
    	testAdmissionContext = null;
    	testWard = null;
    	testWardContext = null;
    	testPatient = null;
    	testPatientContext = null;
    	testAdmissionType = null;
    	testAdmissionTypeContext = null;
    	testDiseaseType = null;
    	testDiseaseTypeContext = null;
    	testDisease = null;
    	testDiseaseContext = null;
    	testOperationType = null;
    	testOperationTypeContext = null;
    	testOperation = null;
    	testOperationContext = null;
    	testDischargeType = null;
    	testDischargeTypeContext = null;
    	testPregnantTreatmentType = null;
    	testPregnantTreatmentTypeContext = null;
    	testDeliveryType = null;
    	testDeliveryTypeContext = null;
    	testDeliveryResultType = null;
    	testDeliveryResultTypeContext = null;
    	testMalnutrition = null;
    	testMalnutritionContext = null;

    	return;
    }
	
	
	@Test
	public void testMalnutritionGets() 
	{
		int code = 0;
			
		
		try 
		{		
			code = _setupTestMalnutrition(false);
			_checkMalnutritionIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testMalnutritionSets() 
	{
		int code = 0;
			

		try 
		{		
			code = _setupTestMalnutrition(true);
			_checkMalnutritionIntoDb(code);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetMalnutrition() 
	{
		int code = 0;
		
		
		try 
		{		
			code = _setupTestMalnutrition(false);
			Malnutrition foundMalnutrition = (Malnutrition)jpa.find(Malnutrition.class, code); 
			ArrayList<Malnutrition> malnutritions = malnutritionIoOperation.getMalnutritions(String.valueOf(foundMalnutrition.getAdmission().getId()));
			
			assertEquals(code, malnutritions.get(malnutritions.size()-1).getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetLastMalnutrition() 
	{
		int code = 0;
		
		
		try 
		{		
			code = _setupTestMalnutrition(false);
			Malnutrition foundMalnutrition = (Malnutrition)jpa.find(Malnutrition.class, code); 
			Malnutrition malnutrition = malnutritionIoOperation.getLastMalnutrition(foundMalnutrition.getAdmission().getId());
			
			assertEquals(code, malnutrition.getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateMalnutrition() 
	{
		int code = 0;
		Malnutrition result = null;
		
		
		try 
		{		
			code = _setupTestMalnutrition(false);
			Malnutrition foundMalnutrition = (Malnutrition)jpa.find(Malnutrition.class, code);
			jpa.flush();
			foundMalnutrition.setHeight(200);
			result = malnutritionIoOperation.updateMalnutrition(foundMalnutrition);
			Malnutrition updateMalnutrition = (Malnutrition)jpa.find(Malnutrition.class, code);
			
			
			assertEquals(true, (result != null));
			assertEquals(200.0, updateMalnutrition.getHeight(), 0.000001d);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewMalnutrition() 
	{
		boolean result = false;
		
		
		try 
		{	
			Ward ward = testWard.setup(false);
			Patient patient = testPatient.setup(true);
			AdmissionType admissionType = testAdmissionType.setup(false);
			DiseaseType diseaseType = testDiseaseType.setup(false);
			Disease diseaseIn = testDisease.setup(diseaseType, false);
			Disease diseaseOut1 = testDisease.setup(diseaseType, false);
			diseaseOut1.setCode("888");
			Disease diseaseOut2 = testDisease.setup(diseaseType, false);
			diseaseOut2.setCode("777");
			Disease diseaseOut3 = testDisease.setup(diseaseType, false); 
			diseaseOut3.setCode("666");
			OperationType operationType = testOperationType.setup(false);
			Operation operation = testOperation.setup(operationType, false);
			DischargeType dischargeType = testDischargeType.setup(false);
			PregnantTreatmentType pregTreatmentType = testPregnantTreatmentType.setup(false);
			DeliveryType deliveryType = testDeliveryType.setup(false);
			DeliveryResultType deliveryResult = testDeliveryResultType.setup(false);
			Admission admission = testAdmission.setup(ward, patient, admissionType, diseaseIn, diseaseOut1, 
					diseaseOut2, diseaseOut3, operation, dischargeType, pregTreatmentType, 
					deliveryType, deliveryResult, true);
			
			jpa.beginTransaction();	
			jpa.persist(ward);
			jpa.persist(patient);	
			jpa.persist(admissionType);
			jpa.persist(diseaseType);
			jpa.persist(diseaseIn);
			jpa.persist(diseaseOut1);
			jpa.persist(diseaseOut2);
			jpa.persist(diseaseOut3);
			jpa.persist(operationType);
			jpa.persist(operation);
			jpa.persist(dischargeType);
			jpa.persist(pregTreatmentType);
			jpa.persist(deliveryType);
			jpa.persist(deliveryResult);
			jpa.persist(admission);
			jpa.commitTransaction();
						
			Malnutrition malnutrition = testMalnutrition.setup(admission, true);
			result = malnutritionIoOperation.newMalnutrition(malnutrition);
			
			assertEquals(true, result);
			_checkMalnutritionIntoDb(malnutrition.getCode());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeleteMalnutrition() 
	{
		int code = 0;
		boolean result = false;
		

		try 
		{		
			code = _setupTestMalnutrition(false);
			Malnutrition foundMalnutrition = (Malnutrition)jpa.find(Malnutrition.class, code);
			jpa.flush();
			
			result = malnutritionIoOperation.deleteMalnutrition(foundMalnutrition);
			
			assertEquals(true, result);
			result = malnutritionIoOperation.isCodePresent(code);			
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
		testMalnutritionContext.saveAll(jpa);
		testAdmissionContext.saveAll(jpa);
    	testWardContext.saveAll(jpa);
    	testPatientContext.saveAll(jpa);
    	testAdmissionTypeContext.saveAll(jpa);
    	testDiseaseTypeContext.saveAll(jpa);
    	testDiseaseContext.saveAll(jpa);
		testDiseaseContext.addMissingKey(jpa);
    	testOperationTypeContext.saveAll(jpa);
    	testOperationContext.saveAll(jpa);
    	testDischargeTypeContext.saveAll(jpa);
    	testPregnantTreatmentTypeContext.saveAll(jpa);
    	testDeliveryTypeContext.saveAll(jpa);
    	testDeliveryResultTypeContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		testMalnutritionContext.deleteNews(jpa);
		testAdmissionContext.deleteNews(jpa);
    	testWardContext.deleteNews(jpa);
    	testPatientContext.deleteNews(jpa);
    	testAdmissionTypeContext.deleteNews(jpa);
    	testDiseaseContext.deleteNews(jpa);
    	testDiseaseTypeContext.deleteNews(jpa);
    	testOperationContext.deleteNews(jpa);
    	testOperationTypeContext.deleteNews(jpa);
    	testDischargeTypeContext.deleteNews(jpa);
    	testPregnantTreatmentTypeContext.deleteNews(jpa);
    	testDeliveryTypeContext.deleteNews(jpa);
    	testDeliveryResultTypeContext.deleteNews(jpa);
        
        return;
    }
    
	private int _setupTestMalnutrition(
			boolean usingSet) throws OHException 
	{
		Malnutrition malnutrition;
		Ward ward = testWard.setup(false);
		Patient patient = testPatient.setup(true);
		AdmissionType admissionType = testAdmissionType.setup(false);
		DiseaseType diseaseType = testDiseaseType.setup(false);
		Disease diseaseIn = testDisease.setup(diseaseType, false);
		Disease diseaseOut1 = testDisease.setup(diseaseType, false);
		diseaseOut1.setCode("888");
		Disease diseaseOut2 = testDisease.setup(diseaseType, false);
		diseaseOut2.setCode("777");
		Disease diseaseOut3 = testDisease.setup(diseaseType, false); 
		diseaseOut3.setCode("666");
		OperationType operationType = testOperationType.setup(false);
		Operation operation = testOperation.setup(operationType, false);
		DischargeType dischargeType = testDischargeType.setup(false);
		PregnantTreatmentType pregTreatmentType = testPregnantTreatmentType.setup(false);
		DeliveryType deliveryType = testDeliveryType.setup(false);
		DeliveryResultType deliveryResult = testDeliveryResultType.setup(false);
		Admission admission = testAdmission.setup(ward, patient, admissionType, diseaseIn, diseaseOut1, diseaseOut2, diseaseOut3, operation, dischargeType, pregTreatmentType, deliveryType, deliveryResult, false);
		
		jpa.beginTransaction();	
		jpa.persist(ward);
		jpa.persist(patient);	
		jpa.persist(admissionType);
		jpa.persist(diseaseType);
		jpa.persist(diseaseIn);
		jpa.persist(diseaseOut1);
		jpa.persist(diseaseOut2);
		jpa.persist(diseaseOut3);
		jpa.persist(operationType);
		jpa.persist(operation);
		jpa.persist(dischargeType);
		jpa.persist(pregTreatmentType);
		jpa.persist(deliveryType);
		jpa.persist(deliveryResult);
		jpa.persist(admission);
		
		malnutrition = testMalnutrition.setup(admission, usingSet);
		jpa.persist(malnutrition);
		jpa.commitTransaction();
		
		return malnutrition.getCode();
	}
		
	private void  _checkMalnutritionIntoDb(
			int code) throws OHException 
	{
		Malnutrition foundMalnutrition;
		
	
		foundMalnutrition = (Malnutrition)jpa.find(Malnutrition.class, code); 
		testMalnutrition.check(foundMalnutrition);
		
		return;
	}	
}