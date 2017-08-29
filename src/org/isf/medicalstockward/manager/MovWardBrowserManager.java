package org.isf.medicalstockward.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

import org.isf.generaldata.MessageBundle;
import org.isf.medicals.model.Medical;
import org.isf.medicalstock.model.Movement;
import org.isf.medicalstockward.model.MedicalWard;
import org.isf.medicalstockward.model.MovementWard;
import org.isf.medicalstockward.service.MedicalStockWardIoOperations;
import org.isf.menu.gui.Menu;
import org.isf.serviceprinting.print.MedicalWardForPrint;
import org.isf.serviceprinting.print.MovementForPrint;
import org.isf.serviceprinting.print.MovementWardForPrint;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.isf.ward.model.Ward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovWardBrowserManager {

	private final Logger logger = LoggerFactory.getLogger(MovWardBrowserManager.class);
	private MedicalStockWardIoOperations ioOperations=Menu.getApplicationContext().getBean(MedicalStockWardIoOperations.class);


	/**
	 * Gets all the {@link MovementWard}s.
	 * If an error occurs a message error is shown and the <code>null</code> value is returned.
	 * @return all the retrieved movements ward.
	 * @throws OHServiceException 
	 */
	public ArrayList<MovementWard> getMovementWard() throws OHServiceException {
		try {
			return ioOperations.getWardMovements(null, null, null);
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
					MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Gets all the {@link MedicalWard}s associated to the specified ward.
	 * If an error occurs a message error is shown and the <code>null</code> value is returned.
	 * @param wardId the ward id.
	 * @return the retrieved medicals.
	 * @throws OHServiceException 
	 */
	public ArrayList<MedicalWard> getMedicalsWard(char wardId) throws OHServiceException {
		try {
			return ioOperations.getMedicalsWard(wardId);
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
					MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Gets all the movement ward with the specified criteria.
	 * If an error occurs a message error is shown and the <code>null</code> value is returned.
	 * @param wardId the ward id.
	 * @param dateFrom the lower bound for the movement date range.
	 * @param dateTo the upper bound for the movement date range.
	 * @return all the retrieved movements.
	 * @throws OHServiceException 
	 */
	public ArrayList<MovementWard> getMovementWard(String wardId, GregorianCalendar dateFrom, GregorianCalendar dateTo) throws OHServiceException {
		try {
			return ioOperations.getWardMovements(wardId, dateFrom, dateTo);
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
					MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Persists the specified movement.
	 * If an error occurs a message error is shown and the <code>false</code> value is returned.
	 * @param newMovement the movement to persist.
	 * @return <code>true</code> if the movement has been persisted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newMovementWard(MovementWard newMovement) throws OHServiceException {
		try {
			return ioOperations.newMovementWard(newMovement);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Persists the specified movements.
	 * If an error occurs a message error is shown and the <code>false</code> value is returned.
	 * @param newMovements the movements to persist.
	 * @return <code>true</code> if the movements have been persisted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newMovementWard(ArrayList<MovementWard> newMovements) throws OHServiceException {
		try {
			return ioOperations.newMovementWard(newMovements);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Updates the specified {@link MovementWard}.
	 * If an error occurs a message error is shown and the <code>false</code> value is returned.
	 * @param updateMovement the movement ward to update.
	 * @return <code>true</code> if the movement has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateMovementWard(MovementWard updateMovement) throws OHServiceException {
		try {
			return ioOperations.updateMovementWard(updateMovement);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Deletes the specified {@link MovementWard}.
	 * If an error occurs a message error is shown and the <code>false</code> value is returned.
	 * @param movement the movement to delete.
	 * @return <code>true</code> if the movement has been deleted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteMovementWard(MovementWard movement) throws OHServiceException {
		try {
			return ioOperations.deleteMovementWard(movement);
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Gets the current quantity for the specified {@link Medical}.
	 * If an error occurs a message error is shown and the <code>0</code> value is returned.
	 * @param ward the medical ward, or <code>null</code>.
	 * @param medical the medical.
	 * @return the total quantity.
	 * @throws OHServiceException 
	 */
	public int getCurrentQuantity(Ward ward, Medical medical) throws OHServiceException {
		try {
			return ioOperations.getCurrentQuantity(ward, medical);
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
					MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"), OHSeverityLevel.ERROR));
		}
	}

	public ArrayList<MovementWardForPrint> convertMovementWardForPrint(ArrayList<MovementWard> wardOutcomes) {
		ArrayList<MovementWardForPrint> movPrint = new ArrayList<MovementWardForPrint>();
		for (MovementWard mov : wardOutcomes) {
			movPrint.add(new MovementWardForPrint(mov));
		}
		Collections.sort(movPrint, new ComparatorMovementWardForPrint());
		return movPrint;
	}

	public ArrayList<MovementForPrint> convertMovementForPrint(ArrayList<Movement> wardIncomes) {
		ArrayList<MovementForPrint> movPrint = new ArrayList<MovementForPrint>();
		for (Movement mov : wardIncomes) {
			movPrint.add(new MovementForPrint(mov));
		}
		Collections.sort(movPrint, new ComparatorMovementForPrint());
		return movPrint;
	}

	public ArrayList<MedicalWardForPrint> convertWardDrugs(Ward wardSelected, ArrayList<MedicalWard> wardDrugs) {
		ArrayList<MedicalWardForPrint> drugPrint = new ArrayList<MedicalWardForPrint>();
		for (MedicalWard mov : wardDrugs) {
			drugPrint.add(new MedicalWardForPrint(mov, wardSelected));
		}
		Collections.sort(drugPrint);
		return drugPrint;
	}
	
	class ComparatorMovementWardForPrint implements Comparator<MovementWardForPrint>  {
		@Override
		public int compare(MovementWardForPrint o1, MovementWardForPrint o2) {
			int byDate = o2.getDate().compareTo(o1.getDate());
			if (byDate != 0) {
				return byDate;
			} else {
				return o1.getMedical().compareTo(o2.getMedical());
			}
		}
	}
	
	class ComparatorMovementForPrint implements Comparator<MovementForPrint>  {
		@Override
		public int compare(MovementForPrint o1, MovementForPrint o2) {
			int byDate = o2.getDate().compareTo(o1.getDate());
			if (byDate != 0) {
				return byDate;
			} else {
				return o1.getMedical().compareTo(o2.getMedical());
			}
		}
	}
}
