package org.isf.accounting.test;


import java.util.List;

import org.isf.accounting.model.BillItems;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestBillItemsContext 
{		
	private static List<BillItems> savedBillItems;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM BILLITEMS", BillItems.class, false);
		savedBillItems = (List<BillItems>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<BillItems> getAllSaved() throws OHException 
    {	        		
        return savedBillItems;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM BILLITEMS", BillItems.class, false);
		List<BillItems> billItems = (List<BillItems>)jpa.getList();
		for (BillItems billItem: billItems) 
		{    		
			int index = savedBillItems.indexOf(billItem);
			
			
			if (index == -1)
			{				
				jpa.remove(billItem);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
