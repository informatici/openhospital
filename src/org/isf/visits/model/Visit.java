package org.isf.visits.model;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

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
public class Visit extends GregorianCalendar 
{
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VST_ID")	
	private int visitID;

	@NotNull
	@ManyToOne
	@JoinColumn(name="VST_PAT_ID")
	Patient patID;

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

	public Visit(int year, int month, int dayOfMonth, int hourOfDay,
			int minute, int second) {
		super(year, month, dayOfMonth, hourOfDay, minute, second);
	}

	public Visit(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
		super(year, month, dayOfMonth, hourOfDay, minute);
	}

	public Visit(int year, int month, int dayOfMonth) {
		super(year, month, dayOfMonth);
	}

	public Visit(Locale locale) {
		super(locale);
	}

	public Visit(TimeZone zone, Locale locale) {
		super(zone, locale);
	}

	public Visit(TimeZone zone) {
		super(zone);
	}

	public Visit(int visitID, GregorianCalendar date, Patient patient, String note, boolean sms) {
		super();
		this.visitID = visitID;
		this.date = date;
		this.patID = patient;
		this.note = note;
		this.sms = sms;		
	}
	
	public GregorianCalendar getDate() {
		return date;
	}

	public void setDate(GregorianCalendar date) {
		this.date = date;
	}

	public int getVisitID() {
		return visitID;
	}

	public void setVisitID(int visitID) {
		this.visitID = visitID;
	}

	public Patient getPatID() {
		return patID;
	}

	public void setPatID(Patient patID) {
		this.patID = patID;
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

		return formatDateTime(this);
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
