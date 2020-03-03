package org.isf.accounting.test;


import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.isf.accounting.model.Bill;
import org.isf.patient.model.Patient;
import org.isf.priceslist.model.PriceList;
import org.isf.utils.exception.OHException;

public class TestBill 
{	
	private static GregorianCalendar date = new GregorianCalendar(10, 9, 8);
	private static GregorianCalendar update = new GregorianCalendar(7, 6, 5);
	private static boolean isList = false;
	private static String listName = "TestListName";
	private static boolean isPatient = true;
	private static String patName = "TestPatName";
	private static String status = "O";
	private static Double amount = 10.10;
	private static Double balance = 20.20;
	private static String user = "TestUser";
	
			
	public Bill setup(
			PriceList priceList,
			Patient patient,
			boolean usingSet) throws OHException 
	{
		Bill bill;
	
				
		if (usingSet)
		{
			bill = new Bill();
			_setParameters(bill, priceList, patient);
		}
		else
		{
			// Create Bill with all parameters 
			bill = new Bill(0, date, update, isList, priceList, listName, isPatient, patient, patName, 
					status, amount, balance, user);
		}
				    	
		return bill;
	}
	
	public void _setParameters(
			Bill bill,
			PriceList priceList,
			Patient patient) 
	{		
		bill.setDate(date);
		bill.setUpdate(update);
		bill.setList(isList);
		bill.setList(priceList);
		bill.setListName(listName);
		bill.setPatient(isPatient);
		bill.setPatient(patient);
		bill.setStatus(status);
		bill.setAmount(amount);
		bill.setBalance(balance);
		bill.setUser(user);
		
		return;
	}
	
	public void check(
			Bill bill) 
	{		
    	assertEquals(date, bill.getDate());
    	assertEquals(update, bill.getUpdate());
    	assertEquals(isList, bill.isList());
    	assertEquals(listName, bill.getListName());
    	assertEquals(isPatient,bill.isPatient());
    	assertEquals(status,bill.getStatus());
    	assertEquals(amount, bill.getAmount());
    	assertEquals(balance, bill.getBalance());
    	assertEquals(user, bill.getUser());
		
		return;
	}
}
