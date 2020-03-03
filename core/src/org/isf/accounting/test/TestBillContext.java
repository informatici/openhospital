package org.isf.accounting.test;


import java.util.List;

import org.isf.accounting.model.Bill;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestBillContext 
{		
	private static List<Bill> savedBills;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM BILLS", Bill.class, false);
		savedBills = (List<Bill>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Bill> getAllSaved() throws OHException 
    {	        		
        return savedBills;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM BILLS", Bill.class, false);
		List<Bill> Bills = (List<Bill>)jpa.getList();
		for (Bill bill: Bills) 
		{    		
			int index = savedBills.indexOf(bill);
			
			
			if (index == -1)
			{				
				jpa.remove(bill);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
