package org.isf.medicalstock.test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.isf.utils.exception.OHException;
import org.isf.medicalstock.model.Lot;

import static org.junit.Assert.assertEquals;

public class TestLot 
{	 
	private String code = "123456";
	private GregorianCalendar now = new GregorianCalendar();
	private GregorianCalendar preparationDate = new GregorianCalendar(now.get(Calendar.YEAR), 1, 1);
	private GregorianCalendar dueDate = new GregorianCalendar(now.get(Calendar.YEAR), 1, 1);
	private double cost = 10.10;
	    
			
	public Lot setup(
			boolean usingSet) throws OHException 
	{
		Lot lot;
	
				
		if (usingSet == true)
		{
			lot = new Lot();
			_setParameters(lot);
		}
		else
		{
			// Create Lot with all parameters 
			lot = new Lot(code, preparationDate, dueDate, cost);
		}
				    	
		return lot;
	}
	
	public void _setParameters(
			Lot lot) 
	{	
		lot.setCode(code);
		lot.setCost(cost);
		lot.setDueDate(dueDate);
		lot.setPreparationDate(preparationDate);
		
		return;
	}
	
	public void check(
			Lot lot) 
	{		
    	System.out.println("Check Lot: " + lot.getCode());	
    	assertEquals(cost, lot.getCost());
    	assertEquals(dueDate, lot.getDueDate());
    	assertEquals(preparationDate, lot.getPreparationDate());
		
		return;
	}
}
