package org.isf.supplier.test;


import java.util.List;

import org.isf.supplier.model.Supplier;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

public class TestSupplierContext 
{		
	private static List<Supplier> savedSupplier;
		
		
	@SuppressWarnings("unchecked")
	public void saveAll(
			DbJpaUtil jpa) throws OHException 
    {	
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM SUPPLIER", Supplier.class, false);
		savedSupplier = (List<Supplier>)jpa.getList();
		jpa.commitTransaction();
        		
        return;
    }
	
	public List<Supplier> getAllSaved() throws OHException 
    {	        		
        return savedSupplier;
    }
	    
    @SuppressWarnings("unchecked")
    public void deleteNews(
    		DbJpaUtil jpa) throws OHException 
    {
		jpa.beginTransaction();			
		jpa.createQuery("SELECT * FROM SUPPLIER", Supplier.class, false);
		List<Supplier> Suppliers = (List<Supplier>)jpa.getList();
		for (Supplier supplier: Suppliers) 
		{    		
			int index = savedSupplier.indexOf(supplier);
			
			
			if (index == -1)
			{				
				jpa.remove(supplier);
			}
	    }        
		jpa.commitTransaction();
		        
        return;
    } 
}
