package org.isf.visits.model;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import org.isf.patient.model.Patient;

 /*------------------------------------------
 * Visits : ?
 * -----------------------------------------
 * modification history
 * ? - ? - first version 
 * 1/08/2016 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="VISITS")
public class Visit
{
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VST_ID")	
	private int visitID;

	@NotNull
	@ManyToOne
	@JoinColumn(name="VST_PAT_ID")
	Patient patient;

	@NotNull
	@Column(name="VST_DATE")
	private GregorianCalendar date;
	
	@Column(name="VST_NOTE")	
	private String note;
	
	@Column(name="VST_SMS")	
	private boolean sms;

	@Transient
	private volatile int hashCode = 0;
	

	public Visit() {
		super();
	}

	public Visit(int visitID, GregorianCalendar date, Patient patient, String note, boolean sms) {
		super();
		this.visitID = visitID;
		this.date = date;
		this.patient = patient;
		this.note = note;
		this.sms = sms;		
	}
	
	public GregorianCalendar getDate() {
		return date;
	}

	public void setDate(GregorianCalendar date) {
		this.date = date;
	}
	
	public void setDate(Date date) {
		GregorianCalendar gregorian = new GregorianCalendar();
		gregorian.setTime(date);
		setDate(gregorian);
	}

	public int getVisitID() {
		return visitID;
	}

	public void setVisitID(int visitID) {
		this.visitID = visitID;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public boolean isSms() {
		return sms;
	}

	public void setSms(boolean sms) {
		this.sms = sms;
	}
	
	public String toString() {
		
		return formatDateTime(this.date);
	}

	public String formatDateTime(GregorianCalendar time) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy - HH:mm:ss"); //$NON-NLS-1$
		return format.format(time.getTime());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof Visit)) {
			return false;
		}
		
		Visit visit = (Visit)obj;
		return (visitID == visit.getVisitID());
	}
	
	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + visitID;
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}
}
