package org.isf.visits.model;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class Visit extends GregorianCalendar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int visitID;
	private int patID;
	private String note;
	private boolean sms;

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

	public int getVisitID() {
		return visitID;
	}

	public void setVisitID(int visitID) {
		this.visitID = visitID;
	}

	public int getPatID() {
		return patID;
	}

	public void setPatID(int patID) {
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
}
