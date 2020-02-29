package org.isf.disease.test;


import java.util.ArrayList;
import java.util.List;

import org.isf.disease.model.Disease;
import org.isf.distype.model.DiseaseType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestDiseaseContext 
{		
	private static List<Disease> savedDisease;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM DISEASE", Disease.class, false);
		savedDisease = (List<Disease>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Disease> getAllSaved() throws OHException 
    {	        		
        return savedDisease;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM DISEASE", Disease.class, false);
		List<Disease> Diseases = (List<Disease>)jpa.getList();
		for (Disease disease: Diseases) 
		{    		
			int index = savedDisease.indexOf(disease);
			
			
			if (index == -1)
			{				
				jpa.remove(disease);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
    
	@SuppressWarnings("unchecked")
	public void addMissingKey(
    		DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT DISTINCT DCL_ID_A FROM DISEASETYPE", null, false);
		List<String> diseaseTypeList = (List<String>)jpa.getList();
		ArrayList<String> diseaseTypeArray = new ArrayList<String>(diseaseTypeList);
		jpa.createQuery("SELECT DISTINCT DIS_DCL_ID_A FROM DISEASE", null, false);
		List<String> missingDiseaseTypeList = (List<String>)jpa.getList();
		ArrayList<String> missingDiseaseType = new ArrayList<String>(missingDiseaseTypeList);	
		missingDiseaseType.removeAll(diseaseTypeArray);
        for (String s : missingDiseaseType) {
            DiseaseType diseaseType = new DiseaseType(s, "Add because missing...");
            jpa.persist(diseaseType);
        }
		jpa.commitTransaction();
        		
        return;
    }
}
