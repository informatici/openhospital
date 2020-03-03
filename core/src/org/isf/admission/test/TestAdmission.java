package org.isf.admission.test;


import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.isf.admission.model.Admission;
import org.isf.admtype.model.AdmissionType;
import org.isf.disctype.model.DischargeType;
import org.isf.disease.model.Disease;
import org.isf.dlvrrestype.model.DeliveryResultType;
import org.isf.dlvrtype.model.DeliveryType;
import org.isf.operation.model.Operation;
import org.isf.patient.model.Patient;
import org.isf.pregtreattype.model.PregnantTreatmentType;
import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;

public class TestAdmission 
{	
	private int id = 0;	
	private int admitted = 1;	
	private String type = "T"; 
	private int yProg = 0;		
	private GregorianCalendar now = new GregorianCalendar();
	private GregorianCalendar admDate = new GregorianCalendar(now.get(Calendar.YEAR), 9, 8);	
	private String FHU = "TestFHU";		
	private String opResult = "Result";
	private GregorianCalendar opDate = new GregorianCalendar(7, 6, 5); 	
	private GregorianCalendar disDate = new GregorianCalendar(4, 3, 2); 	
	private String note = "TestNote";
	private Float transUnit = (float)10.10;	
	private GregorianCalendar visitDate = new GregorianCalendar(1, 0, 1);
	private GregorianCalendar deliveryDate = new GregorianCalendar(2, 3, 4);	
	private Float weight = (float)20.20;		
	private GregorianCalendar ctrlDate1 = new GregorianCalendar(5, 6, 7);	
	private GregorianCalendar ctrlDate2 = new GregorianCalendar(8, 9, 10);
	private GregorianCalendar abortDate = new GregorianCalendar(9, 8, 7);
	private String userID = "TestUserId";	
	private String deleted = "N";	
	
			
	public Admission setup(
			Ward ward,
			Patient patient,
			AdmissionType admissionType,
			Disease diseaseIn,
			Disease diseaseOut1,
			Disease diseaseOut2, 	
			Disease diseaseOut3, 
			Operation operation,
			DischargeType dischargeType,
			PregnantTreatmentType pregTreatmentType,
			DeliveryType deliveryType,
			DeliveryResultType deliveryResult,
			boolean usingSet) throws OHException 
	{
		Admission admission;
	
				
		if (usingSet)
		{
			admission = new Admission();
			_setParameters(admission, ward, patient, admissionType, diseaseIn, diseaseOut1, diseaseOut2, 	
					diseaseOut3, operation, dischargeType, pregTreatmentType, deliveryType,	deliveryResult);
		}
		else
		{
			// Create Admission with all parameters 
			admission = new Admission(id, admitted, type, ward, yProg, patient, admDate, admissionType, FHU, diseaseIn, 
					diseaseOut1, diseaseOut2, diseaseOut3, operation, opResult, opDate, disDate, dischargeType, note, 
					transUnit, visitDate, pregTreatmentType, deliveryDate, deliveryType, deliveryResult, weight, 
					ctrlDate1, ctrlDate2, abortDate, userID, deleted);
		}
				    	
		return admission;
	}
	
	public void _setParameters(
			Admission admission,
			Ward ward,
			Patient patient,
			AdmissionType admissionType,
			Disease diseaseIn,
			Disease diseaseOut1,
			Disease diseaseOut2, 	
			Disease diseaseOut3, 
			Operation operation,
			DischargeType dischargeType,
			PregnantTreatmentType pregTreatmentType,
			DeliveryType deliveryType,
			DeliveryResultType deliveryResult) 
	{	
		admission.setAbortDate(abortDate);
		admission.setAdmDate(admDate);
		admission.setAdmitted(admitted);
		admission.setAdmType(admissionType);
		admission.setCtrlDate1(ctrlDate1);
		admission.setCtrlDate2(ctrlDate2);
		admission.setDeleted(deleted);
		admission.setDeliveryDate(deliveryDate);
		admission.setDeliveryResult(deliveryResult);
		admission.setDeliveryType(deliveryType);
		admission.setDisDate(disDate);
		admission.setDiseaseIn(diseaseIn);
		admission.setDiseaseOut1(diseaseOut1);
		admission.setDiseaseOut2(diseaseOut2);
		admission.setDiseaseOut3(diseaseOut3);
		admission.setDisType(dischargeType);
		admission.setFHU(FHU);
		admission.setNote(note);
		admission.setOpDate(opDate);
		admission.setOperation(operation);
		admission.setOpResult(opResult);
		admission.setPatient(patient);
		admission.setPregTreatmentType(pregTreatmentType);
		admission.setTransUnit(transUnit);
		admission.setType(type);
		admission.setUserID(userID);
		admission.setVisitDate(visitDate);
		admission.setWard(ward);
		admission.setWeight(weight);
		admission.setYProg(yProg);
		
		return;
	}
	
	public void check(
			Admission admission) 
	{		
    	assertEquals(abortDate, admission.getAbortDate());
    	assertEquals(admDate, admission.getAdmDate());
    	assertEquals(admitted, admission.getAdmitted());
    	assertEquals(ctrlDate1, admission.getCtrlDate1());
    	assertEquals(ctrlDate2, admission.getCtrlDate2());
    	assertEquals(deleted, admission.getDeleted());
    	assertEquals(deliveryDate, admission.getDeliveryDate());
    	assertEquals(disDate, admission.getDisDate());
    	assertEquals(FHU, admission.getFHU());
    	assertEquals(note, admission.getNote());
    	assertEquals(opDate, admission.getOpDate());
    	assertEquals(opResult, admission.getOpResult());
    	assertEquals(transUnit, admission.getTransUnit());
    	assertEquals(type, admission.getType());
    	assertEquals(userID, admission.getUserID());
    	assertEquals(visitDate, admission.getVisitDate());
    	assertEquals(weight, admission.getWeight());
    	assertEquals(yProg, admission.getYProg());
		
		return;
	}
}
