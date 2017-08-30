package org.isf.disctype.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.disctype.model.DischargeType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

@Component
public class DischargeTypeIoOperation {

	/**
	 * method that returns all DischargeTypes in a list
	 * 
	 * @return the list of all DischargeTypes
	 * @throws OHException
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<DischargeType> getDischargeType() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<DischargeType> dischargeTypes = null;
				
		try {
			jpa.beginTransaction();
			
			String query = "SELECT * FROM DISCHARGETYPE ORDER BY DIST_DESC";
			jpa.createQuery(query, DischargeType.class, false);
			List<DischargeType> dischargeList = (List<DischargeType>)jpa.getList();
			dischargeTypes = new ArrayList<DischargeType>(dischargeList);			
			
			jpa.commitTransaction();
		} catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return dischargeTypes;
	}

	/**
	 * method that updates an already existing DischargeType
	 * 
	 * @param dischargeType
	 * @return true - if the existing DischargeType has been updated
	 * @throws OHException
	 */
	public boolean updateDischargeType(
			DischargeType dischargeType) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		try {		
			jpa.beginTransaction();	
			jpa.merge(dischargeType);
	    	jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return true;	
	}

	/**
	 * method that create a new DischargeType
	 * 
	 * @param dischargeType
	 * @return true - if the new DischargeType has been inserted
	 * @throws OHException
	 */
	public boolean newDischargeType(
			DischargeType dischargeType) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		try {
			jpa.beginTransaction();	
			jpa.persist(dischargeType);
	    	jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return true;
	}

	/**
	 * method that delete a DischargeType
	 * 
	 * @param dischargeType
	 * @return true - if the DischargeType has been deleted
	 * @throws OHException
	 */
	public boolean deleteDischargeType(
			DischargeType dischargeType) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		try {
			jpa.beginTransaction();
			DischargeType objToRemove = (DischargeType) jpa.find(DischargeType.class, dischargeType.getCode());
			jpa.remove(objToRemove);
	    	jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return true;	
	}

	/**
	 * method that check if a DischargeType already exists
	 * 
	 * @param code
	 * @return true - if the DischargeType already exists; false otherwise
	 * @throws OHException 
	 */
	public boolean isCodePresent(
			String code) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		DischargeType dischargeType;
		boolean result = false;
		
		try {
			jpa.beginTransaction();	
			dischargeType = (DischargeType)jpa.find(DischargeType.class, code);
			if (dischargeType != null)
			{
				result = true;
			}
	    	jpa.commitTransaction();
		}catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return result;	
	}
}
