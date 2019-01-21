package org.isf.medicalstock.manager;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.medicals.model.Medical;
import org.isf.medicals.service.MedicalsIoOperations;
import org.isf.medicalstock.model.Lot;
import org.isf.medicalstock.model.Movement;
import org.isf.medicalstock.service.MedicalStockIoOperations;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MovStockInsertingManager {

	private final Logger logger = LoggerFactory.getLogger(MovStockInsertingManager.class);
	
	@Autowired
	private MedicalStockIoOperations ioOperations;
	@Autowired
	private MedicalsIoOperations ioOperationsMedicals;

	public MovStockInsertingManager() {}
	
	/**
	 * Verify if the object is valid for CRUD and return a list of errors, if any
	 * @param Movement - the movement to validate
	 * @param checkReference - if {@code true} it will use {@link #checkReferenceNumber(String) checkReferenceNumber}
	 * @return list of {@link OHExceptionMessage}
	 * @throws OHServiceException 
	 */
	protected List<OHExceptionMessage> validateMovement(Movement movement, boolean checkReference) throws OHServiceException {
		List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
		
		// Check the Date
		GregorianCalendar today = new GregorianCalendar();
		GregorianCalendar movDate = movement.getDate();
		GregorianCalendar lastDate = getLastMovementDate();
		if (movDate.after(today)) {
			errors.add(new OHExceptionMessage("movementDateInFutureError",
					MessageBundle.getMessage("angal.medicalstock.multiplecharging.futuredatenotallowed"), //$NON-NLS-1$
					OHSeverityLevel.ERROR));
		}
		if (lastDate != null && movDate.compareTo(lastDate) < 0) {
			errors.add(new OHExceptionMessage("movementDateBeforeLastDateError",
					MessageBundle.getMessage("angal.medicalstock.multiplecharging.datebeforelastmovement"), //$NON-NLS-1$
					OHSeverityLevel.ERROR));
		}
		
		// Check the RefNo
		if (checkReference) {
			String refNo = movement.getRefNo();
			errors.addAll(checkReferenceNumber(refNo));
		}
		
		// Check supplier
		if (movement.getType().getType().contains("+")) {
			Object supplier = movement.getSupplier();
			if (supplier == null || supplier instanceof String) {
				errors.add(new OHExceptionMessage("emptyOrNullSupplierError",
						MessageBundle.getMessage("angal.medicalstock.multiplecharging.pleaseselectasupplier"), //$NON-NLS-1$
						OHSeverityLevel.ERROR));
			}
		} else {
			Object ward = movement.getWard();
			if (ward == null || ward instanceof String) {
				errors.add(new OHExceptionMessage("emptyOrNullWardError",
						MessageBundle.getMessage("angal.medicalstock.multipledischarging.pleaseselectaward"), //$NON-NLS-1$
						OHSeverityLevel.ERROR));
			}
		}
		
		// Check quantity
		Lot lot = movement.getLot();
		if (movement.getQuantity() == 0) {
			errors.add(new OHExceptionMessage("zeroQuantityError",
					MessageBundle.getMessage("angal.medicalstock.thequantitymustnotbezero"), //$NON-NLS-1$
					OHSeverityLevel.ERROR));
		}
		if ((movement.getType().getType().contains("-")) && (movement.getQuantity() > lot.getQuantity())) {
			errors.add(new OHExceptionMessage("quantityGreaterThanLotError",
					MessageBundle.getMessage("angal.medicalstock.movementquantityisgreaterthanthequantityof"), //$NON-NLS-1$
					OHSeverityLevel.ERROR));
		}

		// Check Medical
		if (movement.getMedical() == null) {
			errors.add(new OHExceptionMessage("emptyOrNullMedicalError",
					MessageBundle.getMessage("angal.medicalstock.chooseamedical"), //$NON-NLS-1$
					OHSeverityLevel.ERROR));
		}

		// Check Movement Type
		if (movement.getType() == null) {
			errors.add(new OHExceptionMessage("emptyOrNullMovementTypeError",
					MessageBundle.getMessage("angal.medicalstock.chooseatype"), //$NON-NLS-1$
					OHSeverityLevel.ERROR));
		}
		
		// Check Lot
		{
			List<Integer> medicalIds = ioOperations.getMedicalsFromLot(lot.getCode());
			if (!(medicalIds.size() == 0 || (medicalIds.size() == 1 && medicalIds.get(0).intValue() == movement.getMedical().getCode().intValue()))) {
				errors.add(new OHExceptionMessage("sharedLotError",
						MessageBundle.getMessage("angal.medicalstock.thislotreferstoanothermedical"), //$NON-NLS-1$
						OHSeverityLevel.ERROR));
			}
			if (GeneralData.LOTWITHCOST) {
				Double cost = lot.getCost();
				if (cost == null || cost.doubleValue() <= 0.) {
					errors.add(new OHExceptionMessage("zeroLotCostError",
							MessageBundle.getMessage("angal.medicalstock.multiplecharging.zerocostsnotallowed"), //$NON-NLS-1$
							OHSeverityLevel.ERROR));
				}
			}
			errors.addAll(validateLot(lot));
		}
		return errors;
	}
	
	/**
	 * Verify if the object is valid for CRUD and return a list of errors, if any
	 * @param Lot - the lot to validate
	 * @return list of {@link OHExceptionMessage}
	 * @throws OHServiceException 
	 */
	protected List<OHExceptionMessage> validateLot(Lot lot) throws OHServiceException {
		List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
		
		if (lot != null) {

			if (lot.getCode().length() >= 50) {
				errors.add(new OHExceptionMessage("lotIdTooLongError",
						MessageBundle.getMessage("angal.medicalstock.changethelotidbecauseitstoolong"), //$NON-NLS-1$
						OHSeverityLevel.ERROR));
			}

			if (lot.getDueDate() == null) {
				errors.add(new OHExceptionMessage("invalidDueDateError",
						MessageBundle.getMessage("angal.medicalstock.insertavalidduedate"), //$NON-NLS-1$
						OHSeverityLevel.ERROR));
			}
			
			if (lot.getPreparationDate() == null) {
				errors.add(new OHExceptionMessage("invalidPreparationDateError",
						MessageBundle.getMessage("angal.medicalstock.insertavalidpreparationdate"), //$NON-NLS-1$
						OHSeverityLevel.ERROR));
			}

			if (lot.getPreparationDate().compareTo(lot.getDueDate()) > 0) {
				errors.add(new OHExceptionMessage("preparationDateAfterDueDate",
						MessageBundle.getMessage("angal.medicalstock.preparationdatecannotbelaterthanduedate"), //$NON-NLS-1$
						OHSeverityLevel.ERROR));
			}
		}
		return errors;
	}
	
	/**
	 * Verify if the referenceNumber is valid for CRUD and return a list of errors, if any
	 * @param referenceNumber - the lot to validate
	 * @return list of {@link OHExceptionMessage}
	 * @throws OHServiceException 
	 */
	protected List<OHExceptionMessage> checkReferenceNumber(String referenceNumber) throws OHServiceException {
		List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
		if (referenceNumber == null || referenceNumber.isEmpty()) { //$NON-NLS-1$
			errors.add(new OHExceptionMessage("emptyOrNullRefNumberError",
					MessageBundle.getMessage("angal.medicalstock.multiplecharging.pleaseinsertareferencenumber"), //$NON-NLS-1$
					OHSeverityLevel.ERROR));
		} else {
			if (refNoExists(referenceNumber)) {
				errors.add(new OHExceptionMessage("existingRefNumberError",
						MessageBundle.getMessage("angal.medicalstock.multiplecharging.theinsertedreferencenumberalreadyexists"), //$NON-NLS-1$
						OHSeverityLevel.ERROR));
			}
		}
		return errors;
	}

	// Replaced by getMedical in MedicalBrowsingManager
	/*
	 * Gets the current quantity for the specified {@link Medical}. 
	 * 
	 * @param medical the medical to check.
	 * 
	 * @return the current quantity of medical.
	 * 
	 * public int getCurrentQuantity(Medical medical){ try { return
	 * ioOperations.getCurrentQuantity(medical); } catch (OHException e) {
	 * JOptionPane.showMessageDialog(null, e.getMessage()); return 0; } }
	 */

	private boolean isAutomaticLot() {
		return GeneralData.AUTOMATICLOT;
	}

	/**
	 * Retrieves all the {@link Lot} associated to the specified {@link Medical}
	 * 
	 * @param medical
	 *            the medical.
	 * @return the retrieved lots.
	 * @throws OHServiceException 
	 */
	public ArrayList<Lot> getLotByMedical(Medical medical) throws OHServiceException {
		if (medical == null) {
			return new ArrayList<Lot>();
		}
		return ioOperations.getLotsByMedical(medical);
	}

	/**
	 * Checks if the provided quantity is under the medical limits. 
	 * 
	 * @param medicalSelected
	 *            the selected medical.
	 * @param specifiedQuantity
	 *            the quantity provided by the user.
	 * @return <code>true</code> if is under the limit, false otherwise.
	 * @throws OHServiceException 
	 */
	public boolean alertCriticalQuantity(Medical medicalSelected, int specifiedQuantity) throws OHServiceException {
		Medical medical = ioOperationsMedicals.getMedical(medicalSelected.getCode());
		double totalQuantity = medical.getTotalQuantity();
		double residual = totalQuantity - specifiedQuantity;
		return residual < medical.getMinqty();
	}

	/**
	 * returns the date of the last movement
	 * 
	 * @return
	 * @throws OHServiceException 
	 */
	public GregorianCalendar getLastMovementDate() throws OHServiceException {
		return ioOperations.getLastMovementDate();
	}

	/**
	 * check if the reference number is already used
	 * 
	 * @return <code>true</code> if is already used, <code>false</code>
	 *         otherwise.
	 * @throws OHServiceException 
	 */
	public boolean refNoExists(String refNo) throws OHServiceException {
		return ioOperations.refNoExists(refNo);
	}
	
	/**
	 * insert a list of {@link Movement}s and related {@link Lot}s
	 * 
	 * @param movements - the list of {@link Movement}s
	 * @return 
	 * @throws OHServiceException 
	 */
	public boolean newMultipleChargingMovements(ArrayList<Movement> movements) throws OHServiceException {
		return newMultipleChargingMovements(movements, null);
	}

	/**
	 * Insert a list of charging {@link Movement}s and related {@link Lot}s
	 * 
	 * @param movements - the list of {@link Movement}s
	 * @param referenceNumber - the reference number to be set for all movements
	 * 		   if {@link null}, each movements must have a different referenceNumber 
	 * @return 
	 * @throws OHServiceException 
	 */
	@Transactional(rollbackFor=OHServiceException.class)
	public boolean newMultipleChargingMovements(ArrayList<Movement> movements, String referenceNumber) throws OHServiceException {
		
		boolean ok = true;
		boolean checkReference = referenceNumber == null;
		if (!checkReference) { // referenceNumber != null
			List<OHExceptionMessage> errors = checkReferenceNumber(referenceNumber);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
		}
		for (Movement mov : movements) {
			try {
				prepareChargingMovement(mov, checkReference);
			} catch (OHServiceException e) {
				List<OHExceptionMessage> errors = e.getMessages();
				errors.add(new OHExceptionMessage("invalidMovement", 
						mov.getMedical().getDescription(), 
						OHSeverityLevel.INFO));
				throw new OHServiceException(errors);
			}
		}
		return ok;
	}


	/**
	 * Prepare the insert of the specified charging {@link Movement}
	 * 
	 * @param movement - the movement to store.
	 * @param checkReference - if {@code true} every movement must have unique reference number
	 * @return <code>true</code> if the movement has been stored,
	 *         <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	@Transactional(rollbackFor=OHServiceException.class)
	private boolean prepareChargingMovement(Movement movement, boolean checkReference) throws OHServiceException {
		try {
			List<OHExceptionMessage> errors = validateMovement(movement, checkReference);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
			return ioOperations.prepareChargingMovement(movement);
		} catch (OHServiceException e) {
			throw e;
		}
	}
	
	/**
	 * Insert a list of discharging {@link Movement}s
	 * 
	 * @param movements - the list of {@link Movement}s
	 * @return 
	 * @throws OHServiceException 
	 */
	@Transactional(rollbackFor=OHServiceException.class)
	public boolean newMultipleDischargingMovements(ArrayList<Movement> movements) throws OHServiceException {
		return newMultipleDischargingMovements(movements, null);
	}

	/**
	 * Insert a list of discharging {@link Movement}s
	 * 
	 * @param movements - the list of {@link Movement}s
	 * @param referenceNumber - the reference number to be set for all movements
	 * 		   if {@link null}, each movements must have a different referenceNumber 
	 * @return 
	 * @throws OHServiceException 
	 */
	@Transactional(rollbackFor=OHServiceException.class)
	public boolean newMultipleDischargingMovements(ArrayList<Movement> movements, String referenceNumber) throws OHServiceException {
		
		boolean ok = true;
		boolean checkReference = referenceNumber == null;
		if (!checkReference) { // referenceNumber != null
			List<OHExceptionMessage> errors = checkReferenceNumber(referenceNumber);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
		}
		for (Movement mov : movements) {
			try {
				prepareDishargingMovement(mov, checkReference);
			} catch (OHServiceException e) {
				List<OHExceptionMessage> errors = e.getMessages();
				errors.add(new OHExceptionMessage("invalidMovement", 
						mov.getMedical().getDescription(), 
						OHSeverityLevel.INFO));
				throw new OHServiceException(errors);
			}
		}
		return ok;
	}

	/**
	 * Prepare the insert of the specified {@link Movement}
	 * 
	 * @param movement - the movement to store.
	 * @param checkReference - if {@code true} every movement must have unique reference number
	 * @return <code>true</code> if the movement has been stored,
	 *         <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	@Transactional(rollbackFor=OHServiceException.class)
	private boolean prepareDishargingMovement(Movement movement, boolean checkReference) throws OHServiceException {
		try {
			List<OHExceptionMessage> errors = validateMovement(movement, checkReference);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
            if (isAutomaticLot()) {
            	return ioOperations.newAutomaticDischargingMovement(movement);
            } else 
            	return ioOperations.prepareDischargingMovement(movement);
		} catch (OHServiceException e) {
			throw e;
		}
	}
}