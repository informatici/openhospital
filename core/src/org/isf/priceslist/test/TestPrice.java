package org.isf.priceslist.test;


import static org.junit.Assert.assertEquals;

import org.isf.priceslist.model.Price;
import org.isf.priceslist.model.PriceList;
import org.isf.utils.exception.OHException;

public class TestPrice 
{	
    private static String group = "TG";
    private static String item = "TestItem";
    private static String desc = "TestDescription";
    private static Double priceValue = 10.10; 
    private static boolean editable = true;
				
		
	public Price setup(
			PriceList list,
			boolean usingSet) throws OHException 
	{
		Price price;
	
				
		if (usingSet)
		{
			price = new Price();
			_setParameters(list, price);
		}
		else
		{
			// Create PriceList with all parameters 
			price = new Price(0, list, group, item, desc, priceValue);
		}
				    	
		return price;
	}
	
	public void _setParameters(
			PriceList list,
			Price price) 
	{		
		price.setDesc(desc);
		price.setEditable(editable);
		price.setGroup(group);
		price.setItem(item);
		price.setList(list);
		price.setPrice(priceValue);
		
		return;
	}
	
	public void check(
			Price price) 
	{		
		assertEquals(desc, price.getDesc());
		assertEquals(group, price.getGroup());
		assertEquals(item, price.getItem());		
		assertEquals(priceValue, price.getPrice());
		
		return;
	}
}
