package org.isf.therapy.model;

import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.isf.medicals.model.Medical;
import org.isf.patient.model.Patient;

/**
 * 
 * @author Mwithi
 * 
 * Bean to collect data from DB table THERAPIES
 *
 */ /*------------------------------------------
 * Therapies : informations extract
 * from TherapyRow beans
 * -----------------------------------------
 * modification history
 * ? - Mwithi - first version 
 * 1/08/2016 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="THERAPIES")
public class TherapyRow 
{	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="THR_ID")	
	private int therapyID;

	@NotNull
	@ManyToOne
	@JoinColumn(name="THR_PAT_ID")
	Patient patID;

	@NotNull
	@Column(name="THR_STARTDATE")	
	private GregorianCalendar startDate;

	@NotNull
	@Column(name="THR_ENDDATE")	
	private GregorianCalendar endDate;

	@NotNull
	@Column(name="THR_MDSR_ID")	
	private Integer medicalId;

	@NotNull
	@Column(name="THR_QTY")	
	private Double qty;

	@NotNull
	@Column(name="THR_UNT_ID")	
	private int unitID;

	@NotNull
	@Column(name="THR_FREQINDAY")	
	private int freqInDay;

	@NotNull
	@Column(name="THR_FREQINPRD")	
	private int freqInPeriod;
	
	@Column(name="THR_NOTE")	
	private String note;

	@NotNull
	@Column(name="THR_NOTIFY")	
	private int notifyInt;

	@NotNull
	@Column(name="THR_SMS")	
	private int smsInt;

	@Transient
	private volatile int hashCode = 0;
	
	
	public TherapyRow() {
		super();
	}
	
	/**
	 * @param therapyID
	 * @param patID
	 * @param startDate
	 * @param endDate
	 * @param medical
	 * @param qty
	 * @param unitID
	 * @param freqInDay
	 * @param freqInPeriod
	 * @param note
	 * @param notify
	 * @param sms
	 */
	public TherapyRow(int therapyID, Patient patID, 
			GregorianCalendar startDate, GregorianCalendar endDate,
			Medical medical, Double qty, int unitID, int freqInDay,
			int freqInPeriod, String note, boolean notify, boolean sms) {
		super();
		this.therapyID = therapyID;
		this.patID = patID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.medicalId = medical.getCode();
		this.qty = qty;
		this.unitID = unitID;
		this.freqInDay = freqInDay;
		this.freqInPeriod = freqInPeriod;
		this.note = note;
		this.notifyInt = notify ? 1 : 0;
		this.smsInt = sms ? 1 : 0;
	}
	
	public int getTherapyID() {
		return therapyID;
	}

	public void setTherapyID(int therapyID) {
		this.therapyID = therapyID;
	}

	public Patient getPatID() {
		return patID;
	}

	public void setPatID(Patient patID) {
		this.patID = patID;
	}

	public GregorianCalendar getStartDate() {
		return startDate;
	}

	public void setStartDate(GregorianCalendar startDate) {
		this.startDate = startDate;
	}

	public GregorianCalendar getEndDate() {
		return endDate;
	}

	public void setEndDate(GregorianCalendar endDate) {
		this.endDate = endDate;
	}

	public Integer getMedical() {
		return medicalId;
	}

	public void setMedical(Medical medical) {
		this.medicalId = medical.getCode();
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public int getUnitID() {
		return unitID;
	}

	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}

	public int getFreqInDay() {
		return freqInDay;
	}

	public void setFreqInDay(int freqInDay) {
		this.freqInDay = freqInDay;
	}

	public int getFreqInPeriod() {
		return freqInPeriod;
	}

	public void setFreqInPeriod(int freqInPeriod) {
		this.freqInPeriod = freqInPeriod;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isNotify() {
		return (this.notifyInt == 1);
	}

	public void setNotify(boolean notify) {
		this.notifyInt = notify ? 1 : 0;
	}

	public boolean isSms() {
		return (this.smsInt == 1);
	}

	public void setSms(boolean sms) {
		this.smsInt = sms ? 1 : 0;
	}
	
	public String toString() {
		String string = medicalId.toString() + " - " + this.unitID + " " + this.qty + "/" + this.freqInDay + "/" + this.freqInPeriod;
		return string;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof TherapyRow)) {
			return false;
		}
		
		TherapyRow therapy = (TherapyRow)obj;
		return (therapyID == therapy.getTherapyID());
	}
	
	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + therapyID;
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}
}
