package org.isf.medicalstockward.test;


import java.util.List;

import org.isf.medicalstockward.model.MovementWard;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestMovementWardContext 
{		
	private static List<MovementWard> savedMovementWard;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM MEDICALDSRSTOCKMOVWARD", MovementWard.class, false);
		savedMovementWard = (List<MovementWard>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<MovementWard> getAllSaved() throws OHException 
    {	        		
        return savedMovementWard;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM MEDICALDSRSTOCKMOVWARD", MovementWard.class, false);
		List<MovementWard> MovementWards = (List<MovementWard>)jpa.getList();
		for (MovementWard movementWard: MovementWards) 
		{    		
			int index = savedMovementWard.indexOf(movementWard);
			
			
			if (index == -1)
			{				
				jpa.remove(movementWard);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
