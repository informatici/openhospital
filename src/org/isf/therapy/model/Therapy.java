package org.isf.therapy.model;

import java.util.GregorianCalendar;

import org.isf.medicals.model.Medical;

/**
 * 
 * @author Mwithi
 *
 * Bean to host Therapies informations extract
 * from TherapyRow beans.
 */
public class Therapy {
	
	private int therapyID;
	private int patID;
	private GregorianCalendar[] dates;
	private Medical medical;
	private Double qty;
	private String units;
	private int freqInDay;
	private String note;
	private boolean notify;
	private boolean sms;

	/**
	 * 
	 */
	public Therapy() {
		super();
	}

	/**
	 * 
	 * @param therapyID
	 * @param patID
	 * @param dates
	 * @param medical
	 * @param qty
	 * @param units
	 * @param freqInDay
	 * @param note
	 * @param notify
	 * @param sms
	 */
	public Therapy(int therapyID, int patID, GregorianCalendar[] dates,
			Medical medical, Double qty, String units, int freqInDay,
			String note, boolean notify, boolean sms) {
		super();
		this.therapyID = therapyID;
		this.patID = patID;
		this.dates = dates;
		this.medical = medical;
		this.qty = qty;
		this.units = units;
		this.freqInDay = freqInDay;
		this.note = note;
		this.notify = notify;
		this.sms = sms;
	}
	
	public int getTherapyID() {
		return therapyID;
	}

	public void setTherapyID(int therapyID) {
		this.therapyID = therapyID;
	}

	public GregorianCalendar[] getDates() {
		return dates;
	}

	public void setDates(GregorianCalendar[] dates) {
		this.dates = dates;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public int getFreqInDay() {
		return freqInDay;
	}

	public void setFreqInDay(int freq) {
		this.freqInDay = freq;
	}

	public Medical getMedical() {
		return medical;
	}

	public void setMedical(Medical med) {
		this.medical = med;
	}

	public int getPatID() {
		return patID;
	}

	public void setPatID(int patID) {
		this.patID = patID;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public boolean isNotify() {
		return notify;
	}

	public void setNotify(boolean notify) {
		this.notify = notify;
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
		String desc = "" + qty + this.units + " of " + medical.toString() + " - " + this.freqInDay + " per day";
		return desc;
	}
}