package org.isf.accounting.model;

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

import org.isf.patient.model.Patient;
import org.isf.priceslist.model.PriceList;

/**
 * Pure Model Bill : represents a Bill 
 * @author Mwithi
 *
 */
/*------------------------------------------
 * Bill - model for the bill entity
 * -----------------------------------------
 * modification history
 * ? - Mwithi - first version 
 * 25/08/2015 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="BILLS")
public class Bill implements Comparable<Bill> 
{	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="BLL_ID")
	private int id;
	
	@Column(name="BLL_DATE")
	private GregorianCalendar date;

	@Column(name="BLL_UPDATE")
	private GregorianCalendar update;
	
	@Column(name="BLL_IS_LST")
	private boolean isList;
	
	@ManyToOne
	@JoinColumn(name="BLL_ID_LST")
	private PriceList list;
	
	@Column(name="BLL_LST_NAME")
	private String listName;
	
	@Column(name="BLL_IS_PAT")
	private boolean isPatient;
	
	@ManyToOne
	@JoinColumn(name="BLL_ID_PAT")
	private Patient patient;
		
	@Column(name="BLL_PAT_NAME")
	private String patName;
	
	@Column(name="BLL_STATUS")
	private String status;
	
	@Column(name="BLL_AMOUNT")
	private Double amount;
	
	@Column(name="BLL_BALANCE")
	private Double balance;
	
	@Column(name="BLL_USR_ID_A")
	private String user;

	@Transient
	private volatile int hashCode = 0;
	
	
	public Bill() {
		super();
		this.id = 0;
		this.date = new GregorianCalendar();
		this.update = new GregorianCalendar();
		this.isList = true;
		this.listName = "";
		this.isPatient = false;
		this.patName = "";
		this.status = "";
		this.amount = 0.;
		this.balance = 0.;
		this.user = "admin";
	}

	public Bill(int id, GregorianCalendar date, GregorianCalendar update,
			boolean isList, PriceList list, String listName, boolean isPatient,
			Patient patient, String patName, String status, Double amount, Double balance, String user) {
		super();
		this.id = id;
		this.date = date;
		this.update = update;
		this.isList = isList;
		this.list = list;
		this.listName = listName;
		this.isPatient = isPatient;
		this.patient = patient;
		this.patName = patName;
		this.status = status;
		this.amount = amount;
		this.balance = balance;
		this.user = user;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public GregorianCalendar getDate() {
		return date;
	}
	public void setDate(GregorianCalendar date) {
		this.date = date;
	}
	public GregorianCalendar getUpdate() {
		return update;
	}
	public void setUpdate(GregorianCalendar update) {
		this.update = update;
	}
	public boolean isList() {
		return isList;
	}
	public void setList(boolean isList) {
		this.isList = isList;
	}
	public PriceList getList() {
		return list;
	}
	public void setList(PriceList list) {
		this.list = list;
	}
	public String getListName() {
		return listName;
	}
	public void setListName(String listName) {
		this.listName = listName;
	}
	public boolean isPatient() {
		return isPatient;
	}
	public void setPatient(boolean isPatient) {
		this.isPatient = isPatient;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public String getPatName() {
		return patName;
	}
	public void setPatName(String patName) {
		this.patName = patName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int compareTo(Bill obj) {
		return this.id - obj.getId();
	}
		
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof Bill)) {
			return false;
		}
		
		Bill bill = (Bill)obj;
		return (id == bill.getId());
	}
	
	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + id;
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}	
}
