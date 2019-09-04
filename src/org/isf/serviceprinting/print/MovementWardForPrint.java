package org.isf.serviceprinting.print;

import java.util.Date;
import java.util.GregorianCalendar;

import org.isf.medicalstockward.model.MovementWard;

/**
 * 		   @author mwithi
 * 
 */
public class MovementWardForPrint implements Comparable<MovementWardForPrint>{

	private int code;
	private String ward;
	private Date date;
	private String medical;
	private Double quantity;
	private String units;
	private boolean patient;
	
	public MovementWardForPrint(MovementWard mov) {
		super();
		this.ward = mov.getWard().getDescription();
		this.date = removeTime(mov.getDate());
		this.medical = null;
		this.medical = mov.getMedical().getDescription();
		this.quantity = mov.getQuantity();
		this.units = mov.getUnits();
		this.patient = mov.isPatient() || mov.getWardTo() == null || mov.getWardFrom() == null;
	}

	public int getCode(){
		return code;
	}
	
	public String getMedical(){
		return medical;
	}
	
	public Date getDate(){
		return date;
	}
	
	public Double getQuantity(){
		return quantity;
	}
	
	public String getWard() {
		return ward;
	}

	public String getUnits() {
		return units;
	}
	
	public boolean getPatient() {
		return patient;
	}

	public int compareTo(MovementWardForPrint o) {
		return this.date.compareTo(o.getDate());
	}
	
	private Date removeTime(GregorianCalendar date) {
		GregorianCalendar newDate = date;
		date.set(GregorianCalendar.HOUR_OF_DAY, 0);
		date.set(GregorianCalendar.MINUTE, 0);
		date.set(GregorianCalendar.SECOND, 0);
		return newDate.getTime();
	}
}
