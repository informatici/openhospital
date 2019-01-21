package org.isf.distype.test;


import java.util.List;

import org.isf.distype.model.DiseaseType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestDiseaseTypeContext 
{		
	private static List<DiseaseType> savedDiseaseType;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM DISEASETYPE", DiseaseType.class, false);
		savedDiseaseType = (List<DiseaseType>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<DiseaseType> getAllSaved() throws OHException 
    {	        		
        return savedDiseaseType;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM DISEASETYPE", DiseaseType.class, false);
		List<DiseaseType> DiseaseTypes = (List<DiseaseType>)jpa.getList();
		for (DiseaseType diseaseType: DiseaseTypes) 
		{    		
			int index = savedDiseaseType.indexOf(diseaseType);
			
			
			if (index == -1)
			{				
				jpa.remove(diseaseType);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
