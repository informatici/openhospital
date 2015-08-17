package org.isf.admission.model;

import java.util.*;

/*
 * pure model
 */
public class Admission implements Comparable<Admission> {
	private int id;							// admission key
	private int admitted;					// values are 0 or 1, default 0 (not admitted)
	private String type;	   				// values are 'N'(normal)  or 'M' (malnutrition)  default 'N' 
	private String wardId; 					// ward key
	private int yProg;						// a progr. in year for each ward
	private int patId;						// patient key
	private GregorianCalendar admDate;		// admission date
	private String	admType;				// admissionType key
	private String FHU;						// FromHealthUnit (null)
	private String diseaseInId;				// disease in key  (null)
	private String diseaseOutId1;			// disease out key  (null)
	private String diseaseOutId2; 			// disease out key (null)
	private String diseaseOutId3; 			// disease out key (null)
	private String operationId;				// operation key (null)
	private String opResult;				// value is 'P' or 'N' (null)
	private GregorianCalendar opDate; 		// operation date (null)
	private GregorianCalendar disDate; 		// discharge date (null)
	private String disType;					// disChargeType key (null)
	private String note;					// free notes (null)
	private Float transUnit;				// transfusional unit
	private GregorianCalendar visitDate;	// ADM_PRG_DATE_VIS	
	private String pregTreatmentType;		// ADM_PRG_PTT_ID_A treatmentType key
	private GregorianCalendar deliveryDate;	// ADM_PRG_DATE_DEL delivery date		
	private String deliveryTypeId;			// ADM_PRG_DLT_ID_A delivery type key
	private String deliveryResultId;		// ADM_PRG_DRT_ID_A	delivery res. key
	private Float weight;					// ADM_PRG_WEIGHT	weight
	private GregorianCalendar ctrlDate1;	// ADM_PRG_DATE_CTRL1
	private GregorianCalendar ctrlDate2;	// ADM_PRG_DATE_CTRL2
	private GregorianCalendar abortDate;	// ADM_PRG_DATE_ABORT
	private String userID;					// the user ID
	private int lock;						// default 0
	private String deleted;					// flag record deleted ; values are 'Y' OR 'N' default is 'N'
	
	public Admission() {
		super();
	}

	/**
	 * 
	 * @param id
	 * @param admitted
	 * @param type
	 * @param wardId
	 * @param prog
	 * @param patId
	 * @param admDate
	 * @param admType
	 * @param fhu
	 * @param diseaseInId
	 * @param diseaseOutId1
	 * @param diseaseOutId2
	 * @param diseaseOutId3
	 * @param operationId
	 * @param opResult
	 * @param opDate
	 * @param disDate
	 * @param disType
	 * @param note
	 * @param transUnit
	 * @param visitDate
	 * @param pregTreatmentType
	 * @param deliveryDate
	 * @param deliveryTypeId
	 * @param deliveryResultId
	 * @param weight
	 * @param ctrlDate1
	 * @param ctrlDate2
	 * @param abortDate
	 * @param userID
	 * @param lock
	 * @param deleted
	 */
	public Admission(int id, int admitted, String type, String wardId, int prog, int patId, GregorianCalendar admDate, String admType, String fhu, String diseaseInId, String diseaseOutId1, String diseaseOutId2, String diseaseOutId3,
			String operationId, String opResult, GregorianCalendar opDate, GregorianCalendar disDate, String disType, String note, Float transUnit, GregorianCalendar visitDate,
			String pregTreatmentType, GregorianCalendar deliveryDate, String deliveryTypeId, String deliveryResultId, Float weight, GregorianCalendar ctrlDate1, GregorianCalendar ctrlDate2,
			GregorianCalendar abortDate, String userID, int lock, String deleted) {
		super();
		this.id = id;
		this.admitted = admitted;
		this.type = type;
		this.wardId = wardId;
		this.yProg = prog;
		this.patId = patId;
		this.admDate = admDate;
		this.admType = admType;
		this.FHU = fhu;
		this.diseaseInId = diseaseInId;
		this.diseaseOutId1 = diseaseOutId1;
		this.diseaseOutId2 = diseaseOutId2;
		this.diseaseOutId3 = diseaseOutId3;
		this.operationId = operationId;
		this.opResult = opResult;
		this.opDate = opDate;
		this.disDate = disDate;
		this.disType = disType;
		this.note = note;
		this.transUnit = transUnit;
		this.visitDate = visitDate;
		this.pregTreatmentType = pregTreatmentType;
		this.deliveryDate = deliveryDate;
		this.deliveryTypeId = deliveryTypeId;
		this.deliveryResultId = deliveryResultId;
		this.weight = weight;
		this.ctrlDate1 = ctrlDate1;
		this.ctrlDate2 = ctrlDate2;
		this.abortDate = abortDate;
		this.userID = userID;
		this.lock = lock;
		this.deleted = deleted;
	}

	public GregorianCalendar getOpDate() {
		return opDate;
	}

	public void setOpDate(GregorianCalendar opDate) {
		this.opDate = opDate;
	}

	public Float getTransUnit() {
		return transUnit;
	}

	public void setTransUnit(Float transUnit) {
		this.transUnit = transUnit;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String string) {
		this.userID = string;
	}

	public GregorianCalendar getAbortDate() {
		return abortDate;
	}

	public void setAbortDate(GregorianCalendar abortDate) {
		this.abortDate = abortDate;
	}

	public GregorianCalendar getAdmDate() {
		return admDate;
	}

	public void setAdmDate(GregorianCalendar admDate) {
		this.admDate = admDate;
	}

	public int getAdmitted() {
		return admitted;
	}

	public void setAdmitted(int admitted) {
		this.admitted = admitted;
	}

	public String getAdmType() {
		return admType;
	}

	public void setAdmType(String admType) {
		this.admType = admType;
	}

	public GregorianCalendar getCtrlDate1() {
		return ctrlDate1;
	}

	public void setCtrlDate1(GregorianCalendar ctrlDate1) {
		this.ctrlDate1 = ctrlDate1;
	}

	public GregorianCalendar getCtrlDate2() {
		return ctrlDate2;
	}

	public void setCtrlDate2(GregorianCalendar ctrlDate2) {
		this.ctrlDate2 = ctrlDate2;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public GregorianCalendar getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(GregorianCalendar deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryResultId() {
		return deliveryResultId;
	}

	public void setDeliveryResultId(String deliveryResultId) {
		this.deliveryResultId = deliveryResultId;
	}

	public String getDeliveryTypeId() {
		return deliveryTypeId;
	}

	public void setDeliveryTypeId(String deliveryTypeId) {
		this.deliveryTypeId = deliveryTypeId;
	}

	public GregorianCalendar getDisDate() {
		return disDate;
	}

	public void setDisDate(GregorianCalendar disDate) {
		this.disDate = disDate;
	}

	public String getDiseaseInId() {
		return diseaseInId;
	}

	public void setDiseaseInId(String diseaseInId) {
		this.diseaseInId = diseaseInId;
	}

	public String getDiseaseOutId1() {
		return diseaseOutId1;
	}

	public void setDiseaseOutId1(String diseaseOutId1) {
		this.diseaseOutId1 = diseaseOutId1;
	}

	public String getDiseaseOutId2() {
		return diseaseOutId2;
	}

	public void setDiseaseOutId2(String diseaseOutId2) {
		this.diseaseOutId2 = diseaseOutId2;
	}

	public String getDiseaseOutId3() {
		return diseaseOutId3;
	}

	public void setDiseaseOutId3(String diseaseOutId3) {
		this.diseaseOutId3 = diseaseOutId3;
	}

	public String getDisType() {
		return disType;
	}

	public void setDisType(String disType) {
		this.disType = disType;
	}

	public String getFHU() {
		return FHU;
	}

	public void setFHU(String fhu) {
		this.FHU = fhu;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getOpResult() {
		return opResult;
	}

	public void setOpResult(String opResult) {
		this.opResult = opResult;
	}

	public int getPatId() {
		return patId;
	}

	public void setPatId(int patId) {
		this.patId = patId;
	}

	public String getPregTreatmentType() {
		return pregTreatmentType;
	}

	public void setPregTreatmentType(String pregTreatmentType) {
		this.pregTreatmentType = pregTreatmentType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public GregorianCalendar getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(GregorianCalendar visitDate) {
		this.visitDate = visitDate;
	}

	public String getWardId() {
		return wardId;
	}

	public void setWardId(String wardId) {
		this.wardId = wardId;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public int getYProg() {
		return yProg;
	}

	public void setYProg(int prog) {
		this.yProg = prog;
	}

	@Override
	public int compareTo(Admission anAdmission) {
		return this.id - anAdmission.getId();
	}
}
