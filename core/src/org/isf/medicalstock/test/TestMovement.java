package org.isf.medicalstock.test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.isf.medicals.model.Medical;
import org.isf.medicalstock.model.Lot;
import org.isf.medicalstock.model.Movement;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.supplier.model.Supplier;
import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;

public class TestMovement 
{	
	private GregorianCalendar now = new GregorianCalendar();
	private GregorianCalendar date = new GregorianCalendar(now.get(Calendar.YEAR), 2, 2);
	private int quantity = 10;
	private String refNo = "TestRef";
    
			
	public Movement setup(
			Medical medical,
			MovementType movementType,
			Ward ward,
			Lot lot,
			Supplier supplier,
			boolean usingSet) throws OHException 
	{
		Movement movement;
	
				
		if (usingSet)
		{
			movement = new Movement();
			_setParameters(movement, medical, movementType, ward, lot, supplier);
		}
		else
		{
			// Create Movement with all parameters 
			movement = new Movement(medical, movementType, ward, lot, date, quantity, supplier, refNo);
		}
				    	
		return movement;
	}
	
	public void _setParameters(
			Movement movement,
			Medical medical,
			MovementType movementType,
			Ward ward,
			Lot lot,
			Supplier supplier) 
	{	
		movement.setDate(date);
		movement.setLot(lot);
		movement.setMedical(medical);
		movement.setQuantity(quantity);
		movement.setRefNo(refNo);
		movement.setSupplier(supplier);
		movement.setType(movementType);
		movement.setWard(ward);
				
		return;
	}
	
	public void check(
			Movement movement) 
	{		
    	assertEquals(date, movement.getDate());
    	assertEquals(quantity, movement.getQuantity());
    	assertEquals(refNo, movement.getRefNo());
		
		return;
	}
}
