package org.isf.medicalstockward.service;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.isf.medicals.model.Medical;
import org.isf.medicalstock.model.Movement;
import org.isf.medicalstockward.model.MedicalWard;
import org.isf.medicalstockward.model.MovementWard;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.db.TranslateOHException;
import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mwithi
 */
@Component
@Transactional(rollbackFor=OHException.class)
@TranslateOHException
public class MedicalStockWardIoOperations 
{

	@Autowired
	private MedicalStockWardIoOperationRepository repository;
	@Autowired
	private MovementWardIoOperationRepository movementRepository;
	
	/**
	 * Get all {@link MovementWard}s with the specified criteria.
	 * @param wardId the ward id.
	 * @param dateFrom the lower bound for the movement date range.
	 * @param dateTo the upper bound for the movement date range.
	 * @return the retrieved movements.
	 * @throws OHException if an error occurs retrieving the movements.
	 */
	public ArrayList<MovementWard> getWardMovements(
			String wardId, 
			GregorianCalendar dateFrom, 
			GregorianCalendar dateTo) throws OHException 
	{
		ArrayList<Integer> pMovementWardCode = null;
		ArrayList<MovementWard> pMovementWard = new ArrayList<MovementWard>(); 
		
		
		pMovementWardCode = new ArrayList<Integer>(repository.findAllWardMovement(wardId, dateFrom, dateTo));			
		for (int i=0; i<pMovementWardCode.size(); i++)
		{
			Integer code = pMovementWardCode.get(i);
			MovementWard movementWard = movementRepository.findOne(code);
			
			
			pMovementWard.add(movementWard);
		}
		
		return pMovementWard;
	}

	/**
	 * Gets the current quantity for the specified {@link Medical}.
	 * @param ward if specified medical are filtered by the {@link Ward}.
	 * @param medical the medical to check.
	 * @return the total quantity.
	 * @throws OHException if an error occurs retrieving the quantity.
	 */
	public int getCurrentQuantity(
			Ward ward, 
			Medical medical) throws OHException 
	{
		Double mainQuantity = 0.0;
		Double dischargeQuantity = 0.0;
		int currentQuantity = 0;
		
		
		mainQuantity = _getMainQuantity(ward, medical);
		dischargeQuantity = _getDischargeQuantity(ward, medical);
		currentQuantity = (int)(mainQuantity - dischargeQuantity);
		
		return currentQuantity;	
	}
	
	private Double _getMainQuantity(
			Ward ward, 
			Medical medical) throws OHException 
	{		
		Double mainQuantity = 0.0;
				

		if (ward!=null) 
		{
			mainQuantity = repository.findMainQuantityWhereMedicalAndWard(medical.getCode(), ward.getCode());
		}
		else
		{
			mainQuantity = repository.findMainQuantityWhereMedical(medical.getCode());
		}	

		return mainQuantity != null ? mainQuantity : 0.0;	
	}

	private Double _getDischargeQuantity(
			Ward ward, 
			Medical medical) throws OHException 
	{
		Double dischargeQuantity = 0.0;
				
		
		if (ward!=null) 
		{
			dischargeQuantity = repository.findDischargeQuantityWhereMedicalAndWard(medical.getCode(), ward.getCode());
		}
		else
		{
			dischargeQuantity = repository.findDischargeQuantityWhereMedical(medical.getCode());
		}		

		return dischargeQuantity != null ? dischargeQuantity : 0.0;	
	}

	/**
	 * Stores the specified {@link Movement}.
	 * @param movement the movement to store.
	 * @return <code>true</code> if has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs.
	 */	
	public boolean newMovementWard(
			MovementWard movement) throws OHException 
	{
		boolean result = true;
	

		MovementWard savedMovement = movementRepository.save(movement);
		result = (savedMovement != null);
		
		return result;
	}

	/**
	 * Stores the specified {@link Movement} list.
	 * @param movements the movement to store.
	 * @return <code>true</code> if the movements have been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs.
	 */
	public boolean newMovementWard(
			ArrayList<MovementWard> movements) throws OHException 
	{
		for (MovementWard movement:movements) {

			boolean inserted = newMovementWard(movement);
			if (!inserted) 
			{
				return false;
			}
		}
		
		return true;		
	}

	/**
	 * Updates the specified {@link MovementWard}.
	 * @param movement the movement ward to update.
	 * @return <code>true</code> if has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateMovementWard(
			MovementWard movement) throws OHException 
	{
		boolean result = true;
	

		MovementWard savedMovement = movementRepository.save(movement);
		result = (savedMovement != null);
		
		return result;
	}

	/**
	 * Deletes the specified {@link MovementWard}.
	 * @param movement the movement ward to delete.
	 * @return <code>true</code> if the movement has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the delete.
	 */
	public boolean deleteMovementWard(
			MovementWard movement) throws OHException 
	{
		boolean result = true;
	
		
		movementRepository.delete(movement);
		
		return result;
	}

	/**
	 * Updates the quantity for the specified movement ward.
	 * @param dbQuery the {@link DbQueryLogger} to use.
	 * @param movement the movement ward to update.
	 * @return <code>true</code> if has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	protected boolean updateStockWardQuantity(
			DbJpaUtil jpa,
			MovementWard movement) throws OHException 
	{
		Double qty = movement.getQuantity();
		String ward = movement.getWard().getCode();
		Integer medical = movement.getMedical().getCode();		
		boolean result = true;
		
		
		MedicalWard medicalWard = repository.findOneWhereCodeAndMedical(ward, medical);
		if (medicalWard == null)
		{
			repository.insertMedicalWard(ward, medical, -qty);
		}
		else
		{
			if (qty.doubleValue() < 0)
			{
				repository.updateInQuantity(-qty, ward, medical);
			}
			else
			{
				repository.updateOutQuantity(qty, ward, medical);				
			}
		}
		
		return result;
	}

	/**
	 * Gets all the {@link Medical}s associated to specified {@link Ward}.
	 * @param wardId the ward id.
	 * @return the retrieved medicals.
	 * @throws OHException if an error occurs during the medical retrieving.
	 */
	public ArrayList<MedicalWard> getMedicalsWard(
			char wardId) throws OHException
	{
		ArrayList<MedicalWard> medicalWards = new ArrayList<MedicalWard>(repository.findAllWhereWard(wardId));
		
		
		for (int i=0; i<medicalWards.size(); i++)
		{
			medicalWards.get(i).setQty(Double.valueOf(medicalWards.get(i).getInQuantity()-medicalWards.get(i).getOutQuantity()));		
		}
		
		return medicalWards;
	}
}
