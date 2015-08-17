package org.isf.medicalstock.manager;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import org.isf.medicalstock.model.Movement;
import org.isf.medicalstock.service.IoOperations;
import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;
import org.isf.generaldata.MessageBundle;


public class MovBrowserManager {
	
	IoOperations ioOperations;
	
	public MovBrowserManager(){
		ioOperations = new IoOperations();
	}

	/**
	 * Retrieves all the {@link Movement}s.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the retrieved movements.
	 */
	public ArrayList<Movement> getMovements(){
		try {
			return ioOperations.getMovements();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Retrieves all the movement associated to the specified {@link Ward}.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param wardId the ward id.
	 * @param dateTo 
	 * @param dateFrom 
	 * @return the retrieved movements.
	 */
	public ArrayList<Movement> getMovements(String wardId, GregorianCalendar dateFrom, GregorianCalendar dateTo){
		try {
			return ioOperations.getMovements(wardId, dateFrom, dateTo);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * Retrieves all the movement associated to the specified reference number.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param refNo the reference number.
	 * @return the retrieved movements.
	 */
	public ArrayList<Movement> getMovementsByReference(String refNo){
		try {
			return ioOperations.getMovementsByReference(refNo);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Retrieves all the {@link Movement}s with the specified criteria.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
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
	 */
	public ArrayList<Movement> getMovements(Integer medicalCode,String medicalType,
			String wardId,String movType,GregorianCalendar movFrom,GregorianCalendar movTo,
			GregorianCalendar lotPrepFrom,GregorianCalendar lotPrepTo,
			GregorianCalendar lotDueFrom,GregorianCalendar lotDueTo) {

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

		if (movFrom == null || movTo == null) {
			if (!(movFrom == null && movTo == null)) {
				JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.medicalstock.chooseavalidpreparationdate"));
				return null;
			}
		}

		if (lotPrepFrom == null || lotPrepTo == null) {
			if (!(lotPrepFrom == null && lotPrepTo == null)) {
				JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.medicalstock.chooseavalidpreparationdate"));
				return null;
			}
		}

		if (lotDueFrom == null || lotDueTo == null) {
			if (!(lotDueFrom == null && lotDueTo == null)) {
				JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.medicalstock.chooseavalidduedate"));
				return null;
			}
		}

		try {
			return ioOperations.getMovements(medicalCode,medicalType,wardId,movType,movFrom,movTo,lotPrepFrom,lotPrepTo,lotDueFrom,lotDueTo);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
}
