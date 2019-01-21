package org.isf.medicalstock.test;


import java.util.List;

import org.isf.medicalstock.model.Movement;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestMovementContext 
{		
	private static List<Movement> savedMovement;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM MEDICALDSRSTOCKMOV", Movement.class, false);
		savedMovement = (List<Movement>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Movement> getAllSaved() throws OHException 
    {	        		
        return savedMovement;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM MEDICALDSRSTOCKMOV", Movement.class, false);
		List<Movement> Movements = (List<Movement>)jpa.getList();
		for (Movement movement: Movements) 
		{    		
			int index = savedMovement.indexOf(movement);
			
			
			if (index == -1)
			{				
				jpa.remove(movement);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
