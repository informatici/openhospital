/**
 * 
 */
package org.isf.supplier.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.generaldata.ExaminationParameters;
import org.isf.supplier.model.Supplier;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

/**
 * @author Mwithi
 * 
 */
@Component
public class SupplierOperations {

	/**
	 * 
	 */
	public SupplierOperations() {
		ExaminationParameters.getExaminationParameters();
	}

	/**
	 * Save or Update a {@link Supplier}
	 * @param supplier - the {@link Supplier} to save or update
	 * return <code>true</code> if data has been saved, <code>false</code> otherwise. 
	 * @throws OHException 
	 */
	public boolean saveOrUpdate(
			Supplier supplier) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();
		if (supplier.getSupId() != null)
		{
			jpa.merge(supplier);			
		}
		else
		{			
			jpa.persist(supplier);
		}
    	jpa.commitTransaction();
    	
		return result;	
	}

	/**
	 * Returns a {@link Supplier} with specified ID
	 * @param ID - supplier ID
	 * @return supplier - the supplier with specified ID
	 * @throws OHException 
	 */
	public Supplier getByID(
			int ID) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		
		
		jpa.beginTransaction();	
		Supplier foundSupplier = (Supplier)jpa.find(Supplier.class, ID);
    	jpa.commitTransaction();
    	
		return foundSupplier;
	}
	
	/**
	 * Returns the list of all {@link Supplier}s, active and inactive
	 * @return supList - the list of {@link Supplier}s
	 * @throws OHException 
	 */
	@SuppressWarnings("unchecked")
	public List<Supplier> getAll() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Supplier> suppliers = null;
			
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM SUPPLIER";
		jpa.createQuery(query, Supplier.class, false);
		List<Supplier> suppliersList = (List<Supplier>)jpa.getList();
		suppliers = new ArrayList<Supplier>(suppliersList);			
		
		jpa.commitTransaction();
		
		return suppliers;
	}

	/**
	 * Returns the list of active {@link Supplier}s
	 * @return supList - the list of {@link Supplier}s
	 * @throws OHException 
	 */
	@SuppressWarnings("unchecked")
	public List<Supplier> getList() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Supplier> suppliers = null;
			
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM SUPPLIER WHERE SUP_DELETED = 'N'";
		jpa.createQuery(query, Supplier.class, false);
		List<Supplier> suppliersList = (List<Supplier>)jpa.getList();
		suppliers = new ArrayList<Supplier>(suppliersList);			
		
		jpa.commitTransaction();
		
		return suppliers;
	}
}
