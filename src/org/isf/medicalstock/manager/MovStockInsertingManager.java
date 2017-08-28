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
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovStockInsertingManager {

	private final Logger logger = LoggerFactory.getLogger(MovStockInsertingManager.class);
	
	private MedicalStockIoOperations ioOperations;
	private MedicalsIoOperations ioOperationsMedicals;

	public MovStockInsertingManager() {
		ioOperations = Menu.getApplicationContext().getBean(MedicalStockIoOperations.class);
		ioOperationsMedicals = Menu.getApplicationContext().getBean(MedicalsIoOperations.class);
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
		try {
			return ioOperations.getLotsByMedical(medical);
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.medicalstock.problemsoccurredwithsqlistruction"), OHSeverityLevel.ERROR));
		}
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
		try {
			Medical medical = ioOperationsMedicals.getMedical(medicalSelected.getCode());
			double totalQuantity = medical.getTotalQuantity();
			double residual = totalQuantity - specifiedQuantity;
			return residual < medical.getMinqty();
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.medicalstock.problemsoccurredwithsqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * returns the date of the last movement
	 * 
	 * @return
	 * @throws OHServiceException 
	 */
	public GregorianCalendar getLastMovementDate() throws OHServiceException {
		try {
			return ioOperations.getLastMovementDate();
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.medicalstock.problemsoccurredwithsqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * check if the reference number is already used
	 * 
	 * @return <code>true</code> if is already used, <code>false</code>
	 *         otherwise.
	 * @throws OHServiceException 
	 */
	public boolean refNoExists(String refNo) throws OHServiceException {
		try {
			return ioOperations.refNoExists(refNo);
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.medicalstock.problemsoccurredwithsqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * insert a list of {@link Movement}s and related {@link MedicalCost}s
	 * 
	 * @param movements
	 *            - the list of {@link Movement}s
	 * @param costs
	 *            - the related list of {@link MedicalCost}s
	 * @return the list <code>size</code> if all {@link Movement}s have been
	 *         inserted, otherwise the <code>index</code> of the problematic
	 *         {@link Movement} in the list
	 * @throws OHServiceException 
	 */
	public int newMultipleChargingMovements(ArrayList<Movement> movements) throws OHServiceException {

		int i = 0;
		boolean ok = true;
		int size = movements.size();
		for (i = 0; i < size; i++) {
			Movement mov = movements.get(i);
			ok = prepareChargingMovement(mov);
		}
		if (ok) {
			i++;
		}
		return i;
	}

	/**
	 * Prepare the insert of the specified {@link Movement} (no commit)
	 * 
	 * @param movement
	 *            - the movement to store.
	 * @return <code>true</code> if the movement has been stored,
	 *         <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	private boolean prepareChargingMovement(Movement movement) throws OHServiceException {
		try {
			check(movement);
			return ioOperations.prepareChargingMovement(movement);
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.medicalstock.problemsoccurredwithsqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	private void check(Movement movement) throws OHException {
		if (movement.getQuantity() == 0) {
			throw new OHException(MessageBundle.getMessage("angal.medicalstock.thequantitymustnotbe"));
		}

		if (movement.getMedical() == null) {
			throw new OHException(MessageBundle.getMessage("angal.medicalstock.chooseamedical"));
		}

		if (movement.getType() == null) {
			throw new OHException(MessageBundle.getMessage("angal.medicalstock.chooseatype"));
		}

		if (movement.getLot() != null) {
			
			/*if (movement.getLot().getCode().equalsIgnoreCase("")) {
				JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.insertavalidlotidentifier"));
				return false;
			}*/

			if (movement.getLot().getCode().length() >= 50) {
				throw new OHException(MessageBundle.getMessage("angal.medicalstock.changethelotidbecauseitstoolong"));
			}

			if (movement.getLot().getDueDate() == null) {
				throw new OHException(MessageBundle.getMessage("angal.medicalstock.insertavalidduedate"));
			}

			if (movement.getLot().getPreparationDate() == null) {
				throw new OHException(MessageBundle.getMessage("angal.medicalstock.insertavalidpreparationdate"));
			}

			if (movement.getLot().getPreparationDate().compareTo(movement.getLot().getDueDate()) > 0) {
				throw new OHException(MessageBundle.getMessage("angal.medicalstock.preparationdatecannotbelaterthanduedate"));
			}

			if ((movement.getType().getType().equalsIgnoreCase("-")) && (movement.getQuantity() > movement.getLot().getQuantity())) {
				throw new OHException(MessageBundle.getMessage("angal.medicalstock.movementquantityisgreaterthanthequantityof"));
			}

			// checks if the lot is already used by other medicals
			List<Integer> medicalIds = ioOperations.getMedicalsFromLot(movement.getLot().getCode());
			if (!(medicalIds.size() == 0 || (medicalIds.size() == 1 && medicalIds.get(0).intValue() == movement.getMedical().getCode().intValue()))) {
				throw new OHException(MessageBundle.getMessage("angal.medicalstock.thislotreferstoanothermedical"));
			}
		}
	}

	public int newMultipleDischargingMovements(ArrayList<Movement> movements) throws OHServiceException {
		int i = 0;
		boolean ok = true;
		int size = movements.size();
		for (i = 0; i < size; i++) {
			Movement mov = movements.get(i);
			ok = prepareDishargingMovement(mov);
		}
		if (ok) {
			i++;
		}
		return i;
	}

	private boolean prepareDishargingMovement(Movement movement) throws OHServiceException {
		try {
			boolean result;
			if (isAutomaticLot()) {
				result = ioOperations.newAutomaticDischargingMovement(movement);
			} else
				result = ioOperations.prepareDischargingwMovement(movement);
			return result;
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.medicalstock.problemsoccurredwithsqlistruction"), OHSeverityLevel.ERROR));
		}
	}
	
// not used	
//	/**
//	 * Stores the specified movement.
//	 * 
//	 * @param movement - the movement to store.
//	 * @return <code>true</code> if the movement has been stored,
//	 *         <code>false</code> otherwise.
//	 */
//	public boolean newMovement(Movement movement) {
//
//		try {
//
//			if (movement.getQuantity() == 0) {
//				JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.thequantitymustnotbe"));
//				return false;
//			}
//
//			if (movement.getMedical() == null) {
//				JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.chooseamedical"));
//				return false;
//			}
//
//			if (movement.getType() == null) {
//				JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.chooseatype"));
//				return false;
//			}
//			
//			if (movement.getOrigin() == null) {
//				JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.chooseasupplier"));
//				return false;
//			}
//
//			if (movement.getLot() != null) {
//
//				if (movement.getLot().getCode().equalsIgnoreCase("")) {
//					JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.insertavalidlotidentifier"));
//					return false;
//				}
//
//				if (movement.getLot().getCode().length() >= 50) {
//					JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.changethelotidbecauseitstoolong"));
//					return false;
//				}
//
//				if (movement.getLot().getDueDate() == null) {
//					JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.insertavalidduedate"));
//					return false;
//				}
//
//				if (movement.getLot().getPreparationDate() == null) {
//					JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.insertavalidpreparationdate"));
//					return false;
//				}
//
//				if (movement.getLot().getPreparationDate().compareTo(movement.getLot().getDueDate()) > 0) {
//					JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.preparationdatecannotbelaterthanduedate"));
//					return false;
//				}
//
//				if ((movement.getType().getType().equalsIgnoreCase("-")) && (movement.getQuantity() > movement.getLot().getQuantity())) {
//					JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.movementquantityisgreaterthanthequantityof"));
//					return false;
//				}
//
//				// checks if the lot is already used by other medicals
//				List<Integer> medicalIds = ioOperations.getMedicalsFromLot(movement.getLot().getCode());
//				if (!(medicalIds.size() == 0 || (medicalIds.size() == 1 && medicalIds.get(0).intValue() == movement.getMedical().getCode().intValue()))) {
//					JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.thislotreferstoanothermedical"));
//					return false;
//
//				}
//			}
//
//			// we check movement quantity in outgoing stock case
//			if (!movement.getType().getType().contains("+")) {
//
//				Medical medical = ioOperationsMedicals.getMedical(movement.getMedical().getCode());
//				double totalQuantity = medical.getTotalQuantity() - movement.getQuantity();
//
//				// we check if the outgoing movement has a quantity greater than
//				// the stocked one
//				if (totalQuantity < 0) {
//					JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.thetotalquantitycannotbelessthan"));
//					return false;
//				}
//
//				// we check if we are near to critical quantity
//				if (totalQuantity < medical.getMinqty()) {
//					int reply = JOptionPane.showConfirmDialog(null, MessageBundle.getMessage("angal.medicalstock.youaregoingtorununderthecritical"), MessageBundle
//							.getMessage("angal.medicalstock.select"), JOptionPane.YES_NO_OPTION);
//					boolean abort = reply == JOptionPane.NO_OPTION;
//
//					if (abort)
//						return false;
//				}
//			}
//			boolean result;
//			if (isAutomaticLot() && movement.getType().getType().equals("-")) {
//				result = ioOperations.newAutomaticDischargingMovement(movement);
//			} else
//				result = ioOperations.newMovement(movement);
//			return result;
//		} catch (OHException e) {
//			JOptionPane.showMessageDialog(null, e.getMessage());
//			return false;
//		}
//
//	}
}