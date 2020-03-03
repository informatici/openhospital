package org.isf.sms.test;


import java.util.List;

import org.isf.sms.model.Sms;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestSmsContext 
{		
	private static List<Sms> savedSms;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM SMS", Sms.class, false);
		savedSms = (List<Sms>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Sms> getAllSaved() throws OHException 
    {	        		
        return savedSms;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM SMS", Sms.class, false);
		List<Sms> Smss = (List<Sms>)jpa.getList();
		for (Sms sms: Smss) 
		{    		
			int index = savedSms.indexOf(sms);
			
			
			if (index == -1)
			{				
				jpa.remove(sms);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
