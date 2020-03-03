package org.isf.serviceprinting.print;

import java.util.GregorianCalendar;

import org.isf.medicalstock.model.Movement;

public class Movement4Print {
	private GregorianCalendar date;
	private String pharmaceuticalName;
	private String pharmaceuticalType;
	private String movementType;
	private String ward;
	private int quantity;
	private String lot;
	
	public Movement4Print(Movement movement){
		date=movement.getDate();
		pharmaceuticalName=movement.getMedical().getDescription();
		pharmaceuticalType=movement.getMedical().getType().getDescription();
		movementType=movement.getType().getType();
		ward=movement.getWard().getDescription();
		quantity=movement.getQuantity();
		lot=movement.getLot().getCode();
	}

	public String getDate() {
		return getConvertedString(date);
	}

	public void setDate(GregorianCalendar date) {
		this.date = date;
	}

	public String getLot() {
		if(lot!=null)return lot;
		else return "No Lot";
	}

	public void setLot(String lot) {
		this.lot = lot;
	}

	public String getMovementType() {
		return movementType;
	}

	public void setMovementType(String movementType) {
		this.movementType = movementType;
	}

	public String getPharmaceuticalName() {
		return pharmaceuticalName;
	}

	public void setPharmaceuticalName(String pharmaceuticalName) {
		this.pharmaceuticalName = pharmaceuticalName;
	}

	public String getPharmaceuticalType() {
		return pharmaceuticalType;
	}

	public void setPharmaceuticalType(String pharmaceuticalType) {
		this.pharmaceuticalType = pharmaceuticalType;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getWard() {
		if(ward==null)return "No Ward";
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}
	private String getConvertedString(GregorianCalendar time) {
		if (time == null)
			return "No Date";
		String string = String
				.valueOf(time.get(GregorianCalendar.DAY_OF_MONTH));
		string += "/" + (time.get(GregorianCalendar.MONTH) + 1);
		String year = String.valueOf(time.get(GregorianCalendar.YEAR));
		year = year.substring(2, year.length());
		string += "/" + year;
		return string;
	}
}
