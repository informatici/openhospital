package org.isf.accounting.model;

import java.util.GregorianCalendar;

/**
 * Pure Model BillPayments : represents a patient Payment for a Bill
 * @author Mwithi
 *
 */
public class BillPayments implements Comparable<Object>{

	private int id;
	private int billID;
	private GregorianCalendar date;
	private double amount;
	private String user;
	
	public BillPayments(int id, int billID, GregorianCalendar date,
			double amount, String user) {
		super();
		this.id = id;
		this.billID = billID;
		this.date = date;
		this.amount = amount;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBillID() {
		return billID;
	}

	public void setBillID(int billID) {
		this.billID = billID;
	}

	public GregorianCalendar getDate() {
		return date;
	}

	public void setDate(GregorianCalendar date) {
		this.date = date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public int compareTo(Object anObject) {
		if (anObject instanceof BillPayments)
			if (this.date.after(((BillPayments)anObject).getDate()))
				return 1;
			else
				return 0;
		return 0;
	}
}
