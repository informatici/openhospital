package org.isf.pricesothers.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.pricesothers.model.PricesOthers;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

@Component
public class PriceOthersIoOperations {

	/**
	 * return the list of {@link PriceOthers}s in the DB
	 * 
	 * @return the list of {@link PriceOthers}s
	 * @throws OHException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PricesOthers> getOthers() throws OHException 
	{

		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<PricesOthers> pricesOthers = null;
				
		try{
			jpa.beginTransaction();

			String query = "SELECT * FROM PRICESOTHERS ORDER BY OTH_DESC";
			jpa.createQuery(query, PricesOthers.class, false);
			List<PricesOthers> pricesOtherstList = (List<PricesOthers>)jpa.getList();
			pricesOthers = new ArrayList<PricesOthers>(pricesOtherstList);			

			jpa.commitTransaction();
		}  catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		} 
		return pricesOthers;
	}

	/**
	 * insert a new {@link PriceOthers} in the DB
	 * 
	 * @param other - the {@link PriceOthers} to insert
	 * @return <code>true</code> if the list has been inserted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean newOthers(
			PricesOthers other) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		try{
			jpa.beginTransaction();	
			jpa.persist(other);
			jpa.commitTransaction();
		}  catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		} 
		return result;
	}

	/**
	 * delete a {@link PriceOthers} in the DB
	 * 
	 * @param other - the {@link PriceOthers} to delete
	 * @return <code>true</code> if the list has been deleted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean deleteOthers(
			PricesOthers other) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		try{
			jpa.beginTransaction();
			PricesOthers objToRemove = (PricesOthers) jpa.find(PricesOthers.class, other.getId());
			jpa.remove(objToRemove);
			jpa.commitTransaction();
		}  catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		} 
		return result;
	}

	/**
	 * update a {@link PriceOthers} in the DB
	 * 
	 * @param other - the {@link PriceOthers} to update
	 * @return <code>true</code> if the list has been updated, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean updateOther(
			PricesOthers other) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		try{
			jpa.beginTransaction();	
			jpa.merge(other);
			jpa.commitTransaction();
		}  catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		} 
		return result;	
	}
}