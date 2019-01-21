package org.isf.priceslist.test;


import java.util.List;

import org.isf.priceslist.model.PriceList;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestPriceListContext 
{		
	private static List<PriceList> savedPriceLists;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM PRICELISTS", PriceList.class, false);
		savedPriceLists = (List<PriceList>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<PriceList> getAllSaved() throws OHException 
    {	        		
        return savedPriceLists;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM PRICELISTS", PriceList.class, false);
		List<PriceList> PriceLists = (List<PriceList>)jpa.getList();
		for (PriceList priceList: PriceLists) 
		{    		
			int index = savedPriceLists.indexOf(priceList);

			
			if (index == -1)
			{				
				jpa.remove(priceList);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
