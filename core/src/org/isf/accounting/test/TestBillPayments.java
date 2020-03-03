package org.isf.accounting.test;


import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.isf.accounting.model.Bill;
import org.isf.accounting.model.BillPayments;
import org.isf.utils.exception.OHException;

public class TestBillPayments 
{	
	private GregorianCalendar paymentDate = new GregorianCalendar(4, 3, 2);
	private static double paymentAmount = 10.10;
	private static String paymentUser = "TestUser";

			
	public BillPayments setup(
			Bill  bill,
			boolean usingSet) throws OHException 
	{
		BillPayments billPayment;
	
				
		if (usingSet)
		{
			billPayment = new BillPayments();
			_setParameters(billPayment, bill);
		}
		else
		{
			// Create bill payment with all parameters 
			billPayment = new BillPayments(0, bill, paymentDate, paymentAmount, paymentUser);
		}
				    	
		return billPayment;
	}
	
	public void _setParameters(
			BillPayments billPayment,
			Bill bill) 
	{		
		billPayment.setBill(bill);
		billPayment.setDate(paymentDate);
		billPayment.setAmount(paymentAmount);
		billPayment.setUser(paymentUser);
		
		return;
	}
	
	public void check(
			BillPayments billPayment) 
	{		
		assertEquals(paymentAmount, billPayment.getAmount(), 0.1);
		assertEquals(paymentDate, billPayment.getDate());
		assertEquals(paymentUser, billPayment.getUser());
				
		return;
	}
}
