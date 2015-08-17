package org.isf.medicalstock.model;

import java.util.GregorianCalendar;

import org.isf.generaldata.MessageBundle;

public class Lot {
	private String code;
	private GregorianCalendar preparationDate;
	private GregorianCalendar dueDate;
	private int quantity;
	private double cost;
	
	public Lot(String aCode,GregorianCalendar aPreparationDate,GregorianCalendar aDueDate){
		code=aCode;
		preparationDate=aPreparationDate;
		dueDate=aDueDate;
	}
	public Lot(String aCode,GregorianCalendar aPreparationDate,GregorianCalendar aDueDate,int aQuantity){
		code=aCode;
		preparationDate=aPreparationDate;
		dueDate=aDueDate;
		quantity=aQuantity;
	}
	public Lot(String aCode,GregorianCalendar aPreparationDate,GregorianCalendar aDueDate,double aCost){
		code=aCode;
		preparationDate=aPreparationDate;
		dueDate=aDueDate;
		cost=aCost;
	}
	public String getCode(){
		return code;
	}
	public int getQuantity(){
		return quantity;
	}
	public GregorianCalendar getPreparationDate(){
		return preparationDate;
	}
	public GregorianCalendar getDueDate(){
		return dueDate;
	}
	public double getCost() {
		return cost;
	}
	public void setCode(String aCode){
		code=aCode;
	}
	public void setPreparationDate(GregorianCalendar aPreparationDate){
		preparationDate=aPreparationDate;
	}
	public void setQuantity(int aQuantity){
		quantity=aQuantity;
	}
	public void setDueDate(GregorianCalendar aDueDate){
		dueDate=aDueDate;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public String toString(){
		if(code==null)return MessageBundle.getMessage("angal.medicalstock.nolot");
		return getCode();
	}

	public boolean isValidLot(){
		return getCode().length()<=50;
	}
	
	public boolean equals(Lot lot) {
		if (this.code == lot.getCode() &&
				this.dueDate == lot.getDueDate() &&
				this.preparationDate == lot.getPreparationDate())
			return true;
		return false;
	}
}
