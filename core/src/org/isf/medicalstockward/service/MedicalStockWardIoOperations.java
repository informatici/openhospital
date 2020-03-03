package org.isf.medicalstockward.service;

import org.isf.medicals.model.Medical;
import org.isf.medicalstock.model.Movement;
import org.isf.medicalstockward.model.MedicalWard;
import org.isf.medicalstockward.model.MovementWard;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHServiceException;
import org.isf.ward.model.Ward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * @author mwithi
 */
@Service
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
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
	 * @throws OHServiceException if an error occurs retrieving the movements.
	 */
	public ArrayList<MovementWard> getWardMovements(
			String wardId, 
			GregorianCalendar dateFrom, 
			GregorianCalendar dateTo) throws OHServiceException 
	{
		ArrayList<Integer> pMovementWardCode = null;
		ArrayList<MovementWard> pMovementWard = new ArrayList<MovementWard>(); 
		
		
		pMovementWardCode = new ArrayList<Integer>(repository.findAllWardMovement(wardId, dateFrom, dateTo));
        for (Integer code : pMovementWardCode) {
            MovementWard movementWard = movementRepository.findOne(code);


            pMovementWard.add(movementWard);
        }
		
		return pMovementWard;
	}
        
    /**
	 * Get all {@link MovementWard}s with the specified criteria.
	 * @param idwardTo the target ward id.
	 * @param dateFrom the lower bound for the movement date range.
	 * @param dateTo the upper bound for the movement date range.
	 * @return the retrieved movements.
	 * @throws OHServiceException if an error occurs retrieving the movements.
	 */
	public ArrayList<MovementWard> getWardMovementsToWard(
			String idwardTo, 
			GregorianCalendar dateFrom, 
			GregorianCalendar dateTo) throws OHServiceException 
	{
            return movementRepository.findWardMovements(idwardTo, dateFrom, dateTo);
	}

	/**
	 * Gets the current quantity for the specified {@link Medical} and specified {@link Ward}.
	 * @param ward - if {@code null} the quantity is counted for the whole hospital
	 * @param medical - the {@link Medical} to check.
	 * @return the total quantity.
	 * @throws OHServiceException if an error occurs retrieving the quantity.
	 */
	public int getCurrentQuantityInWard(
			Ward ward, 
			Medical medical) throws OHServiceException 
	{
		Double mainQuantity = 0.0;
		

		if (ward!=null) 
		{
			mainQuantity = repository.findQuantityInWardWhereMedicalAndWard(medical.getCode(), ward.getCode());
		}
		else
		{
			mainQuantity = repository.findQuantityInWardWhereMedical(medical.getCode());
		}	

		return (int) (mainQuantity != null ? mainQuantity : 0.0);	
	}
	
	/**
	 * Stores the specified {@link Movement}.
	 * @param movement the movement to store.
	 * @return <code>true</code> if has been stored, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs.
	 */	
	public boolean newMovementWard(
			MovementWard movement) throws OHServiceException 
	{
		boolean result = true;
	

		MovementWard savedMovement = movementRepository.save(movement);
		if (savedMovement.getWardTo() != null) {
			// We have to register also the income movement for the destination Ward
			MovementWard destinationWardIncomeMovement = new MovementWard();
			destinationWardIncomeMovement.setDate(savedMovement.getDate());
			destinationWardIncomeMovement.setDescription(savedMovement.getWard().getDescription());
			destinationWardIncomeMovement.setMedical(savedMovement.getMedical());
			destinationWardIncomeMovement.setQuantity(-savedMovement.getQuantity());
			destinationWardIncomeMovement.setUnits(savedMovement.getUnits());
			destinationWardIncomeMovement.setWard(savedMovement.getWardTo());
			destinationWardIncomeMovement.setWardFrom(savedMovement.getWard());
			movementRepository.save(destinationWardIncomeMovement);
		}
		
		if (savedMovement != null) {
			updateStockWardQuantity(movement);
		}
		result = (savedMovement != null);
		
		return result;
	}

	/**
	 * Stores the specified {@link Movement} list.
	 * @param movements the movement to store.
	 * @return <code>true</code> if the movements have been stored, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs.
	 */
	public boolean newMovementWard(
			ArrayList<MovementWard> movements) throws OHServiceException 
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
	 * @throws OHServiceException if an error occurs during the update.
	 */
	public boolean updateMovementWard(
			MovementWard movement) throws OHServiceException 
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
	 * @throws OHServiceException if an error occurs during the delete.
	 */
	public boolean deleteMovementWard(
			MovementWard movement) throws OHServiceException 
	{
		boolean result = true;
	
		
		movementRepository.delete(movement);
		
		return result;
	}

	/**
	 * Updates the quantity for the specified movement ward.
	 * @param movement the movement ward to update.
	 * @return <code>true</code> if has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs during the update.
	 */
	protected boolean updateStockWardQuantity(
			MovementWard movement) throws OHServiceException 
	{
		Double qty = movement.getQuantity();
		String ward = movement.getWard().getCode();
                String wardTo = null;
                if (movement.getWardTo() != null) { 
                	// in case of a mvnt from the ward movement.getWard() to the ward movement.getWardTO()
                    wardTo = movement.getWardTo().getCode();
                }
		Integer medical = movement.getMedical().getCode();		
		boolean result = true;
		
		
        if(wardTo != null) {
            MedicalWard medicalWardTo = repository.findOneWhereCodeAndMedical(wardTo, medical);
            if(medicalWardTo != null) {
                repository.updateInQuantity(Math.abs(qty), wardTo, medical);
            } else {
                repository.insertMedicalWard(wardTo, medical, Math.abs(qty));
            }
            repository.updateOutQuantity(Math.abs(qty), ward, medical);
            return result;
        }
                
		MedicalWard medicalWard = repository.findOneWhereCodeAndMedical(ward, medical);
        if (medicalWard == null)
		{
            repository.insertMedicalWard(ward, medical, -qty);
        }
		else
		{
			if (qty < 0)
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
	 * @throws OHServiceException if an error occurs during the medical retrieving.
	 */
	public ArrayList<MedicalWard> getMedicalsWard(
			char wardId) throws OHServiceException
	{
		ArrayList<MedicalWard> medicalWards = new ArrayList<MedicalWard>(repository.findAllWhereWard(wardId));
		for (int i=0; i<medicalWards.size(); i++)
		{
			double qty = (double) (medicalWards.get(i).getInQuantity() - medicalWards.get(i).getOutQuantity());
			if (qty != 0) {
				medicalWards.get(i).setQty(qty);
			} else {
				medicalWards.remove(i);
			}
		}
		
		return medicalWards;
	}
}
