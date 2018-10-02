package org.isf.medicalstock.manager;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.isf.generaldata.MessageBundle;
import org.isf.medicalstock.model.Movement;
import org.isf.medicalstock.service.MedicalStockIoOperations;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.isf.ward.model.Ward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MovBrowserManager {
	
	private final Logger logger = LoggerFactory.getLogger(MovBrowserManager.class);
	
	private MedicalStockIoOperations ioOperations;
	
	public MovBrowserManager(){
		ioOperations = Menu.getApplicationContext().getBean(MedicalStockIoOperations.class);
	}
	
	/**
	 * Retrieves all the {@link Movement}s.
	 * @return the retrieved movements.
	 * @throws OHServiceException 
	 */
	public ArrayList<Movement> getMovements() throws OHServiceException{
		return ioOperations.getMovements();
	}

	/**
	 * Retrieves all the movement associated to the specified {@link Ward}.
	 * @param wardId the ward id.
	 * @param dateTo 
	 * @param dateFrom 
	 * @return the retrieved movements.
	 * @throws OHServiceException 
	 */
	public ArrayList<Movement> getMovements(String wardId, GregorianCalendar dateFrom, GregorianCalendar dateTo) throws OHServiceException{
		return ioOperations.getMovements(wardId, dateFrom, dateTo);
	}
	
	/**
	 * Retrieves all the movement associated to the specified reference number.
	 * @param refNo the reference number.
	 * @return the retrieved movements.
	 * @throws OHServiceException 
	 */
	public ArrayList<Movement> getMovementsByReference(String refNo) throws OHServiceException{
		return ioOperations.getMovementsByReference(refNo);
	}

	/**
	 * Retrieves all the {@link Movement}s with the specified criteria.
	 * @param medicalCode the medical code.
	 * @param medicalType the medical type.
	 * @param wardId the ward type.
	 * @param movType the movement type.
	 * @param movFrom the lower bound for the movement date range.
	 * @param movTo the upper bound for the movement date range.
	 * @param lotPrepFrom the lower bound for the lot preparation date range.
	 * @param lotPrepTo the upper bound for the lot preparation date range.
	 * @param lotDueFrom the lower bound for the lot due date range.
	 * @param lotDueTo the lower bound for the lot due date range.
	 * @return the retrieved movements.
	 * @throws OHServiceException 
	 */
	public ArrayList<Movement> getMovements(Integer medicalCode,String medicalType,
			String wardId,String movType,GregorianCalendar movFrom,GregorianCalendar movTo,
			GregorianCalendar lotPrepFrom,GregorianCalendar lotPrepTo,
			GregorianCalendar lotDueFrom,GregorianCalendar lotDueTo) throws OHServiceException {

		if (medicalCode == null && 
				medicalType == null && 
				movType == null && 
				movFrom == null &&
				movTo == null && 
				lotPrepFrom == null && 
				lotPrepTo == null && 
				lotDueFrom == null && 
				lotDueTo == null) {
			return getMovements();
		}
		
		check(movFrom, movTo, "angal.medicalstock.chooseavalidmovementdate");
		check(lotPrepFrom, lotPrepTo, "angal.medicalstock.chooseavalidmovementdate");
		check(lotDueFrom, lotDueTo, "angal.medicalstock.chooseavalidduedate");
		
		return ioOperations.getMovements(medicalCode,medicalType,wardId,movType,movFrom,movTo,lotPrepFrom,lotPrepTo,lotDueFrom,lotDueTo);
	}
	
	private void check(GregorianCalendar from, GregorianCalendar to, String errMsgKey) throws OHServiceException {
		if (from == null || to == null) {
			if (!(from == null && to == null)) {
				throw new OHServiceException(
						new OHExceptionMessage(
							MessageBundle.getMessage("angal.hospital"), 
							MessageBundle.getMessage(errMsgKey), 
							OHSeverityLevel.ERROR
						)
				);
			}
		}
	}
}
