package org.isf.pricesothers.service;

import java.util.ArrayList;

import org.isf.pricesothers.model.PricesOthers;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
public class PriceOthersIoOperations {

	@Autowired
	private PriceOthersIoOperationRepository repository;
	
	/**
	 * return the list of {@link PriceOthers}s in the DB
	 * 
	 * @return the list of {@link PriceOthers}s
	 * @throws OHServiceException 
	 */
	public ArrayList<PricesOthers> getOthers() throws OHServiceException 
	{		
		ArrayList<PricesOthers> pricesOthers = (ArrayList<PricesOthers>) repository.findAllByOrderByDescriptionAsc();

		return pricesOthers;
	}

	/**
	 * insert a new {@link PriceOthers} in the DB
	 * 
	 * @param other - the {@link PriceOthers} to insert
	 * @return <code>true</code> if the list has been inserted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean newOthers(
			PricesOthers other) throws OHServiceException 
	{
		boolean result = true;
	

		PricesOthers savedOther = repository.save(other);
		result = (savedOther != null);
		    	
		return result;
	}

	/**
	 * delete a {@link PriceOthers} in the DB
	 * 
	 * @param other - the {@link PriceOthers} to delete
	 * @return <code>true</code> if the list has been deleted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean deleteOthers(
			PricesOthers other) throws OHServiceException 
	{
		boolean result = true;
	
		
		repository.delete(other);
		
		return result;
	}

	/**
	 * update a {@link PriceOthers} in the DB
	 * 
	 * @param other - the {@link PriceOthers} to update
	 * @return <code>true</code> if the list has been updated, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean updateOther(
			PricesOthers other) throws OHServiceException 
	{
		boolean result = true;
	

		PricesOthers savedOther = repository.save(other);
		result = (savedOther != null);
		    	
		return result;
	}

	/**
	 * checks if the code is already in use
	 *
	 * @param code - the price other code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean isCodePresent(
			Integer id) throws OHServiceException
	{
		boolean result = true;
	
		
		result = repository.exists(id);
		
		return result;	
	}
}