package org.isf.accounting.test;


import static org.junit.Assert.assertEquals;

import org.isf.accounting.model.Bill;
import org.isf.accounting.model.BillItems;
import org.isf.utils.exception.OHException;

public class TestBillItems 
{	
	private static boolean isPrice = false;
	private static String priceID = "TestPId";
	private static String itemDescription = "TestItemDescription";
	private static double itemAmount = 10.10;
	private static int itemQuantity = 20;

			
	public BillItems setup(
			Bill  bill,
			boolean usingSet) throws OHException 
	{
		BillItems billItem;
	
				
		if (usingSet)
		{
			billItem = new BillItems();
			_setParameters(billItem, bill);
		}
		else
		{
			// Create BillItem with all parameters 
			billItem = new BillItems(0, bill, isPrice, priceID, itemDescription, itemAmount, itemQuantity);
		}
				    	
		return billItem;
	}
	
	public void _setParameters(
			BillItems billItem,
			Bill bill) 
	{		
		billItem.setBill(bill);
		billItem.setItemAmount(itemAmount);
		billItem.setItemDescription(itemDescription);
		billItem.setItemQuantity(itemQuantity);
		billItem.setPrice(isPrice);
		billItem.setPriceID(priceID);
		
		return;
	}
	
	public void check(
			BillItems billItem) 
	{		
		assertEquals(itemAmount, billItem.getItemAmount(), 0.1);
		assertEquals(itemDescription, billItem.getItemDescription());
		assertEquals(itemQuantity, billItem.getItemQuantity());
		assertEquals(isPrice, billItem.isPrice());
		assertEquals(priceID, billItem.getPriceID());
				
		return;
	}
}
