package org.isf.disctype.service;

import java.util.ArrayList;

import org.isf.disctype.model.DischargeType;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
public class DischargeTypeIoOperation {

	@Autowired
	private DischargeTypeIoOperationRepository repository;
	
	/**
	 * method that returns all DischargeTypes in a list
	 * 
	 * @return the list of all DischargeTypes
	 * @throws OHServiceException
	 */
	public ArrayList<DischargeType> getDischargeType() throws OHServiceException 
	{
		return new ArrayList<DischargeType>(repository.findAllByOrderByDescriptionAsc());
	}

	/**
	 * method that updates an already existing DischargeType
	 * 
	 * @param dischargeType
	 * @return true - if the existing DischargeType has been updated
	 * @throws OHServiceException
	 */
	public boolean UpdateDischargeType(
			DischargeType dischargeType) throws OHServiceException 
	{
		boolean result = true;
	
		
		DischargeType savedDischargeType = repository.save(dischargeType);
		result = (savedDischargeType != null);
		
		return result;
	}

	/**
	 * method that create a new DischargeType
	 * 
	 * @param dischargeType
	 * @return true - if the new DischargeType has been inserted
	 * @throws OHServiceException
	 */
	public boolean newDischargeType(
			DischargeType dischargeType) throws OHServiceException 
	{
		boolean result = true;
	
		
		DischargeType savedDischargeType = repository.save(dischargeType);
		result = (savedDischargeType != null);
		
		return result;
	}

	/**
	 * method that delete a DischargeType
	 * 
	 * @param dischargeType
	 * @return true - if the DischargeType has been deleted
	 * @throws OHServiceException
	 */
	public boolean deleteDischargeType(
			DischargeType dischargeType) throws OHServiceException
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
	 * @throws OHServiceException 
	 */
	public boolean isCodePresent(
			String code) throws OHServiceException 
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;
	}
}
