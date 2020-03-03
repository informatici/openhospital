package org.isf.operation.test;


import java.util.List;

import org.isf.operation.model.Operation;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestOperationContext 
{		
	private static List<Operation> savedOperation;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM OPERATION", Operation.class, false);
		savedOperation = (List<Operation>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Operation> getAllSaved() throws OHException 
    {	        		
        return savedOperation;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM OPERATION", Operation.class, false);
		List<Operation> Operations = (List<Operation>)jpa.getList();
		for (Operation operation: Operations) 
		{    		
			int index = savedOperation.indexOf(operation);
			
			
			if (index == -1)
			{				
				jpa.remove(operation);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
