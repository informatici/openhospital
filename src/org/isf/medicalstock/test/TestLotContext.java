package org.isf.medicalstock.test;


import java.util.List;

import org.isf.medicalstock.model.Lot;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestLotContext 
{		
	private static List<Lot> savedLot;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM MEDICALDSRLOT", Lot.class, false);
		savedLot = (List<Lot>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Lot> getAllSaved() throws OHException 
    {	        		
        return savedLot;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM MEDICALDSRLOT", Lot.class, false);
		List<Lot> Lots = (List<Lot>)jpa.getList();
		for (Lot lot: Lots) 
		{    		
			int index = savedLot.indexOf(lot);
			
			
			if (index == -1)
			{				
				jpa.remove(lot);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
