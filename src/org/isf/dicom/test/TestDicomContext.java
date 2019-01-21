package org.isf.dicom.test;


import java.util.List;

import org.isf.dicom.model.FileDicom;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestDicomContext 
{		
	private static List<FileDicom> savedFileDicom;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM DICOM", FileDicom.class, false);
		savedFileDicom = (List<FileDicom>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<FileDicom> getAllSaved() throws OHException 
    {	        		
        return savedFileDicom;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM DICOM", FileDicom.class, false);
		List<FileDicom> FileDicoms = (List<FileDicom>)jpa.getList();
		for (FileDicom dicom: FileDicoms) 
		{    		
			int index = savedFileDicom.indexOf(dicom);
			
			
			if (index == -1)
			{				
				jpa.remove(dicom);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
