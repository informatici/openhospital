package org.isf.supplier.test;

import static org.junit.Assert.assertEquals;

import org.isf.supplier.model.Supplier;
import org.isf.utils.exception.OHException;

public class TestSupplier 
{	 
	private Integer supId = null;
	private String supName = "TestName";
	private String supAddress = "TestAddress";
	private String supTaxcode = "TestTax";
	private String supPhone = "TestPhone";
	private String supFax = "TestFax";
	private String supEmail = "TestEmail";
	private String supNote = "TestNote";
	private Character supDeleted = 'N';    
			
	public Supplier setup(
			boolean usingSet) throws OHException 
	{
		Supplier supplier;
	
				
		if (usingSet)
		{
			supplier = new Supplier();
			_setParameters(supplier);
		}
		else
		{
			// Create Supplier with all parameters 
			supplier = new Supplier(supId, supName, supAddress, supTaxcode, supPhone, supFax, supEmail, supNote, supDeleted);
		}
				    	
		return supplier;
	}
	
	public void _setParameters(
			Supplier supplier) 
	{	
		supplier.setSupAddress(supAddress);
		supplier.setSupDeleted(supDeleted);
		supplier.setSupEmail(supEmail);
		supplier.setSupFax(supFax);
		supplier.setSupName(supName);
		supplier.setSupNote(supNote);
		supplier.setSupPhone(supPhone);
		supplier.setSupTaxcode(supTaxcode);
		
		return;
	}
	
	public void check(
			Supplier supplier) 
	{		
    	assertEquals(supAddress, supplier.getSupAddress());
    	assertEquals(supDeleted, supplier.getSupDeleted());
    	assertEquals(supEmail, supplier.getSupEmail());
    	assertEquals(supFax, supplier.getSupFax());
    	assertEquals(supName, supplier.getSupName());
    	assertEquals(supNote, supplier.getSupNote());
    	assertEquals(supPhone, supplier.getSupPhone());
    	assertEquals(supTaxcode, supplier.getSupTaxcode());
		
		return;
	}
}
