package org.isf.disctype.service;

import java.util.ArrayList;

import org.isf.disctype.model.DischargeType;
import org.isf.disctype.repository.DischargeTypeIoOperationRepository;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DischargeTypeIoOperation {

	@Autowired
	private DischargeTypeIoOperationRepository repository;
	
	/**
	 * method that returns all DischargeTypes in a list
	 * 
	 * @return the list of all DischargeTypes
	 * @throws OHException
	 */
	public ArrayList<DischargeType> getDischargeType() throws OHException 
	{
		return new ArrayList<DischargeType>(repository.findAllByOrderByDescriptionAsc());
	}

	/**
	 * method that updates an already existing DischargeType
	 * 
	 * @param dischargeType
	 * @return true - if the existing DischargeType has been updated
	 * @throws OHException
	 */
	public boolean UpdateDischargeType(
			DischargeType dischargeType) throws OHException 
	{
		boolean result = true;
	
		
		repository.save(dischargeType);
		
		return result;
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
		boolean result = true;
	
		
		repository.save(dischargeType);
		
		return result;
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
		boolean result = true;
	
		
		repository.delete(dischargeType);
		
		return result;
	}

	/**
	 * method that check if a DischargeType already exists
	 * 
	 * @param code
	 * @return true - if the DischargeType already exists
	 * @throws OHException 
	 */
	public boolean isCodePresent(
			String code) throws OHException 
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;
	}
}
