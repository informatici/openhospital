package org.isf.medype.test;


import org.isf.medtype.model.MedicalType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

import java.util.List;

public class TestMedicalTypeContext 
{		
	private static List<MedicalType> savedMedicalType;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM MEDICALDSRTYPE", MedicalType.class, false);
		savedMedicalType = (List<MedicalType>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<MedicalType> getAllSaved() throws OHException 
    {	        		
        return savedMedicalType;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM MEDICALDSRTYPE", MedicalType.class, false);
		List<MedicalType> MedicalTypes = (List<MedicalType>)jpa.getList();
		for (MedicalType medicalType: MedicalTypes) 
		{    		
			int index = savedMedicalType.indexOf(medicalType);
			
			
			if (index == -1)
			{				
				jpa.remove(medicalType);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
