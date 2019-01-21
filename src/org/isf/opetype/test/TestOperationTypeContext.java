package org.isf.opetype.test;


import java.util.List;

import org.isf.opetype.model.OperationType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestOperationTypeContext 
{		
	private static List<OperationType> savedOperationType;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM OPERATIONTYPE", OperationType.class, false);
		savedOperationType = (List<OperationType>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<OperationType> getAllSaved() throws OHException 
    {	        		
        return savedOperationType;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM OPERATIONTYPE", OperationType.class, false);
		List<OperationType> OperationTypes = (List<OperationType>)jpa.getList();
		for (OperationType operationType: OperationTypes) 
		{    		
			int index = savedOperationType.indexOf(operationType);
			
			
			if (index == -1)
			{				
				jpa.remove(operationType);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
