package org.isf.disctype.test;


import java.util.List;

import org.isf.disctype.model.DischargeType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestDischargeTypeContext 
{		
	private static List<DischargeType> savedDischargeType;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM DISCHARGETYPE", DischargeType.class, false);
		savedDischargeType = (List<DischargeType>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<DischargeType> getAllSaved() throws OHException 
    {	        		
        return savedDischargeType;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM DISCHARGETYPE", DischargeType.class, false);
		List<DischargeType> DischargeTypes = (List<DischargeType>)jpa.getList();
		for (DischargeType dischargeType: DischargeTypes) 
		{    		
			int index = savedDischargeType.indexOf(dischargeType);
			
			
			if (index == -1)
			{				
				jpa.remove(dischargeType);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
