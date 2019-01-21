package org.isf.accounting.test;


import java.util.List;

import org.isf.accounting.model.BillPayments;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestBillPaymentsContext 
{		
	private static List<BillPayments> savedBillPayments;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM BILLPAYMENTS", BillPayments.class, false);
		savedBillPayments = (List<BillPayments>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<BillPayments> getAllSaved() throws OHException 
    {	        		
        return savedBillPayments;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM BILLPAYMENTS", BillPayments.class, false);
		List<BillPayments> billPayments = (List<BillPayments>)jpa.getList();
		for (BillPayments billPayment: billPayments) 
		{    		
			int index = savedBillPayments.indexOf(billPayment);
			
			
			if (index == -1)
			{				
				jpa.remove(billPayment);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
