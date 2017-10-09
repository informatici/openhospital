/**
 * 
 */
package org.isf.supplier.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.generaldata.ExaminationParameters;
import org.isf.supplier.model.Supplier;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mwithi
 * 
 */
@Component
@Transactional
public class SupplierOperations {

	@Autowired
	private SupplierIoOperationRepository repository;
	
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
		boolean result = true;
	

		Supplier savedSupplier = repository.save(supplier);
		result = (savedSupplier != null);
		
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
		Supplier foundSupplier = repository.findOne(ID);
    	
		return foundSupplier;
	}
	
	/**
	 * Returns the list of all {@link Supplier}s, active and inactive
	 * @return supList - the list of {@link Supplier}s
	 * @throws OHException 
	 */
	public List<Supplier> getAll() throws OHException 
	{
		ArrayList<Supplier> suppliers = (ArrayList<Supplier>)repository.findAll();
		
		return suppliers;
	}

	/**
	 * Returns the list of active {@link Supplier}s
	 * @return supList - the list of {@link Supplier}s
	 * @throws OHException 
	 */
	public List<Supplier> getList() throws OHException 
	{
		ArrayList<Supplier> suppliers = (ArrayList<Supplier>)repository.findAllWhereDeleted();
		
		return suppliers;
	}
}
