package org.isf.accounting.model;

import java.util.GregorianCalendar;

/**
 * Pure Model Bill : represents a Bill
 * @author Mwithi
 *
 */
public class Bill implements Comparable<Bill> {
	private int id;
	private GregorianCalendar date;
	private GregorianCalendar update;
	private boolean isList;
	private int listID;
	private String listName;
	private boolean isPatient;
	private int patID;
	private String patName;
	private String status;
	private Double amount;
	private Double balance;
	private String user;
	
	public Bill() {
		super();
		this.id = 0;
		this.date = new GregorianCalendar();
		this.update = new GregorianCalendar();
		this.isList = true;
		this.listID = 0;
		this.listName = "";
		this.isPatient = false;
		this.patID = 0;
		this.patName = "";
		this.status = "";
		this.amount = 0.;
		this.balance = 0.;
		this.user = "admin";
	}

	public Bill(int id, GregorianCalendar date, GregorianCalendar update,
			boolean isList, int listID, String listName, boolean isPatient,
			int patID, String patName, String status, Double amount, Double balance, String user) {
		super();
		this.id = id;
		this.date = date;
		this.update = update;
		this.isList = isList;
		this.listID = listID;
		this.listName = listName;
		this.isPatient = isPatient;
		this.patID = patID;
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
	public int getListID() {
		return listID;
	}
	public void setListID(int listID) {
		this.listID = listID;
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
	public int getPatID() {
		return patID;
	}
	public void setPatID(int patID) {
		this.patID = patID;
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
}
