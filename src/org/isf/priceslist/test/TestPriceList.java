package org.isf.priceslist.test;


import org.isf.priceslist.model.PriceList;
import org.isf.utils.exception.OHException;

import static org.junit.Assert.assertEquals;

public class TestPriceList 
{	
    private static String listCode = "Code";
    private static String listName = "TestName";
    private static String listDescription = "TestDescription";
    private static String listCurrency = "Curr";
				
		
	public PriceList setup(
			boolean usingSet) throws OHException 
	{
		PriceList priceList;
	
				
		if (usingSet == true)
		{
			priceList = new PriceList();
			_setParameters(priceList);
		}
		else
		{
			// Create PriceList with all parameters 
			priceList = new PriceList(0, listCode, listName, listDescription, listCurrency);
		}
				    	
		return priceList;
	}
	
	public void _setParameters(
			PriceList priceList) 
	{		
		priceList.setCode(listCode);
		priceList.setCurrency(listCurrency);
		priceList.setDescription(listDescription);
		priceList.setName(listName);
		
		return;
	}
	
	public void check(
			PriceList priceList) 
	{		
    	System.out.println("Check prices list: " + priceList.getCode());
		assertEquals(listCode, priceList.getCode());
		assertEquals(listCurrency, priceList.getCurrency());
		assertEquals(listDescription, priceList.getDescription());
		assertEquals(listName, priceList.getName());
		
		return;
	}
}
