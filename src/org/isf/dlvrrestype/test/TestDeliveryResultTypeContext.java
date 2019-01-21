package org.isf.dlvrrestype.test;


import java.util.List;

import org.isf.dlvrrestype.model.DeliveryResultType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestDeliveryResultTypeContext 
{		
	private static List<DeliveryResultType> savedDeliveryResultType;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM DELIVERYRESULTTYPE", DeliveryResultType.class, false);
		savedDeliveryResultType = (List<DeliveryResultType>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<DeliveryResultType> getAllSaved() throws OHException 
    {	        		
        return savedDeliveryResultType;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM DELIVERYRESULTTYPE", DeliveryResultType.class, false);
		List<DeliveryResultType> DeliveryResultTypes = (List<DeliveryResultType>)jpa.getList();
		for (DeliveryResultType deliveryResultType: DeliveryResultTypes) 
		{    		
			int index = savedDeliveryResultType.indexOf(deliveryResultType);
			
			
			if (index == -1)
			{				
				jpa.remove(deliveryResultType);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
