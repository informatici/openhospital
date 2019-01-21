package org.isf.admtype.test;


import java.util.List;

import org.isf.admtype.model.AdmissionType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestAdmissionTypeContext 
{		
	private static List<AdmissionType> savedAdmissionType;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM ADMISSIONTYPE", AdmissionType.class, false);
		savedAdmissionType = (List<AdmissionType>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<AdmissionType> getAllSaved() throws OHException 
    {	        		
        return savedAdmissionType;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM ADMISSIONTYPE", AdmissionType.class, false);
		List<AdmissionType> AdmissionTypes = (List<AdmissionType>)jpa.getList();
		for (AdmissionType admissionType: AdmissionTypes) 
		{    		
			int index = savedAdmissionType.indexOf(admissionType);
			
			
			if (index == -1)
			{				
				jpa.remove(admissionType);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
