package org.isf.medicalstock.model;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.isf.generaldata.MessageBundle;

/*------------------------------------------
 * Medical Lot - model for the medical entity
 * -----------------------------------------
 * modification history
 * ? - ?
 * 17/01/2015 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="MEDICALDSRLOT")
public class Lot 
{
	@Id 
	@Column(name="LT_ID_A")
	private String code;

	@NotNull
	@Column(name="LT_PREP_DATE")
	private GregorianCalendar preparationDate;

	@NotNull
	@Column(name="LT_DUE_DATE")
	private GregorianCalendar dueDate;

	@Transient
	private int quantity;
	
	@Column(name="LT_COST")
	private BigDecimal cost;
	
	@Transient
	private volatile int hashCode = 0;
	

	public Lot() { 
	}
	
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
	public Lot(String aCode,GregorianCalendar aPreparationDate,GregorianCalendar aDueDate,BigDecimal aCost){
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
	public BigDecimal getCost() {
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
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	public String toString(){
		if(code==null)return MessageBundle.getMessage("angal.medicalstock.nolot");
		return getCode();
	}

	public boolean isValidLot(){
		return getCode().length()<=50;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lot other = (Lot) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (cost != null) {
			if (cost.compareTo(other.cost) != 0)
				return false;
		}
		if (dueDate == null) {
			if (other.dueDate != null)
				return false;
		} else if (!dueDate.equals(other.dueDate))
			return false;
		if (preparationDate == null) {
			if (other.preparationDate != null)
				return false;
		} else if (!preparationDate.equals(other.preparationDate))
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + code.hashCode();
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}	
}
