package org.isf.dlvrtype.test;


import org.isf.dlvrtype.model.DeliveryType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

import java.util.List;

public class TestDeliveryTypeContext 
{		
	private static List<DeliveryType> savedDeliveryType;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM DELIVERYTYPE", DeliveryType.class, false);
		savedDeliveryType = (List<DeliveryType>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<DeliveryType> getAllSaved() throws OHException 
    {	        		
        return savedDeliveryType;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM DELIVERYTYPE", DeliveryType.class, false);
		List<DeliveryType> DeliveryTypes = (List<DeliveryType>)jpa.getList();
		for (DeliveryType deliveryType: DeliveryTypes) 
		{    		
			int index = savedDeliveryType.indexOf(deliveryType);
			
			
			if (index == -1)
			{				
				jpa.remove(deliveryType);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
