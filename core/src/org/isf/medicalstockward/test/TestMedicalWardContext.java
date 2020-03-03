package org.isf.medicalstockward.test;


import java.util.List;

import org.isf.medicalstockward.model.MedicalWard;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestMedicalWardContext 
{		
	private static List<MedicalWard> savedMedicalWard;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM MEDICALDSRWARD", MedicalWard.class, false);
		savedMedicalWard = (List<MedicalWard>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<MedicalWard> getAllSaved() throws OHException 
    {	        		
        return savedMedicalWard;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM MEDICALDSRWARD", MedicalWard.class, false);
		List<MedicalWard> MedicalWards = (List<MedicalWard>)jpa.getList();
		for (MedicalWard medicalward: MedicalWards) 
		{    		
			int index = savedMedicalWard.indexOf(medicalward);
			
			
			if (index == -1)
			{				
				jpa.remove(medicalward);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
