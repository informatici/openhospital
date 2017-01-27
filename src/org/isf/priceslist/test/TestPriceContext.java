package org.isf.priceslist.test;


import org.isf.priceslist.model.Price;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

import java.util.List;

public class TestPriceContext 
{		
	private static List<Price> savedPrices;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM PRICES", Price.class, false);
		savedPrices = (List<Price>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Price> getAllSaved() throws OHException 
    {	        		
        return savedPrices;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM PRICES", Price.class, false);
		List<Price> Prices = (List<Price>)jpa.getList();
		for (Price price: Prices) 
		{    		
			int index = savedPrices.indexOf(price);
			
			
			if (index == -1)
			{				
				jpa.remove(price);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
