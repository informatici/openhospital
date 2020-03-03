package org.isf.pregtreattype.test;


import java.util.List;

import org.isf.pregtreattype.model.PregnantTreatmentType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestPregnantTreatmentTypeContext 
{		
	private static List<PregnantTreatmentType> savedPregnantTreatmentType;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM PREGNANTTREATMENTTYPE", PregnantTreatmentType.class, false);
		savedPregnantTreatmentType = (List<PregnantTreatmentType>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<PregnantTreatmentType> getAllSaved() throws OHException 
    {	        		
        return savedPregnantTreatmentType;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM PREGNANTTREATMENTTYPE", PregnantTreatmentType.class, false);
		List<PregnantTreatmentType> PregnantTreatmentTypes = (List<PregnantTreatmentType>)jpa.getList();
		for (PregnantTreatmentType pregnantTreatmentType: PregnantTreatmentTypes) 
		{    		
			int index = savedPregnantTreatmentType.indexOf(pregnantTreatmentType);
			
			
			if (index == -1)
			{				
				jpa.remove(pregnantTreatmentType);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
