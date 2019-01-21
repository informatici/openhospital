package org.isf.opd.test;


import java.util.List;

import org.isf.opd.model.Opd;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestOpdContext 
{		
	private static List<Opd> savedOpd;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM OPD", Opd.class, false);
		savedOpd = (List<Opd>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Opd> getAllSaved() throws OHException 
    {	        		
        return savedOpd;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM OPD", Opd.class, false);
		List<Opd> Opds = (List<Opd>)jpa.getList();
		for (Opd opd: Opds) 
		{    		
			int index = savedOpd.indexOf(opd);
			
			
			if (index == -1)
			{				
				jpa.remove(opd);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
