package org.isf.medicalstockward.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import org.isf.medicals.model.Medical;
import org.isf.medicalstock.model.Movement;
import org.isf.medicalstockward.model.MedicalWard;
import org.isf.medicalstockward.model.MovementWard;
import org.isf.medicalstockward.service.IoOperations;
import org.isf.serviceprinting.print.MedicalWardForPrint;
import org.isf.serviceprinting.print.MovementForPrint;
import org.isf.serviceprinting.print.MovementWardForPrint;
import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;

public class MovWardBrowserManager {

	IoOperations ioOperations;

	public MovWardBrowserManager(){
		ioOperations=new IoOperations();
	}

	/**
	 * Gets all the {@link MovementWard}s.
	 * If an error occurs a message error is shown and the <code>null</code> value is returned.
	 * @return all the retrieved movements ward.
	 */
	public ArrayList<MovementWard> getMovementWard() {
		try {
			return ioOperations.getWardMovements(null, null, null);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Gets all the {@link MedicalWard}s associated to the specified ward.
	 * If an error occurs a message error is shown and the <code>null</code> value is returned.
	 * @param wardId the ward id.
	 * @return the retrieved medicals.
	 */
	public ArrayList<MedicalWard> getMedicalsWard(String wardId) {
		try {
			return ioOperations.getMedicalsWard(wardId);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Gets all the movement ward with the specified criteria.
	 * If an error occurs a message error is shown and the <code>null</code> value is returned.
	 * @param wardId the ward id.
	 * @param dateFrom the lower bound for the movement date range.
	 * @param dateTo the upper bound for the movement date range.
	 * @return all the retrieved movements.
	 */
	public ArrayList<MovementWard> getMovementWard(String wardId, GregorianCalendar dateFrom, GregorianCalendar dateTo) {
		try {
			return ioOperations.getWardMovements(wardId, dateFrom, dateTo);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Persists the specified movement.
	 * If an error occurs a message error is shown and the <code>false</code> value is returned.
	 * @param newMovement the movement to persist.
	 * @return <code>true</code> if the movement has been persisted, <code>false</code> otherwise.
	 */
	public boolean newMovementWard(MovementWard newMovement) {
		try {
			return ioOperations.newMovementWard(newMovement);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Persists the specified movements.
	 * If an error occurs a message error is shown and the <code>false</code> value is returned.
	 * @param newMovements the movements to persist.
	 * @return <code>true</code> if the movements have been persisted, <code>false</code> otherwise.
	 */
	public boolean newMovementWard(ArrayList<MovementWard> newMovements) {
		try {
			return ioOperations.newMovementWard(newMovements);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Updates the specified {@link MovementWard}.
	 * If an error occurs a message error is shown and the <code>false</code> value is returned.
	 * @param updateMovement the movement ward to update.
	 * @return <code>true</code> if the movement has been updated, <code>false</code> otherwise.
	 */
	public boolean updateMovementWard(MovementWard updateMovement) {
		try {
			return ioOperations.updateMovementWard(updateMovement);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Deletes the specified {@link MovementWard}.
	 * If an error occurs a message error is shown and the <code>false</code> value is returned.
	 * @param movement the movement to delete.
	 * @return <code>true</code> if the movement has been deleted, <code>false</code> otherwise.
	 */
	public boolean deleteMovementWard(MovementWard movement) {
		try {
			return ioOperations.deleteMovementWard(movement);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Gets the current quantity for the specified {@link Medical}.
	 * If an error occurs a message error is shown and the <code>0</code> value is returned.
	 * @param ward the medical ward, or <code>null</code>.
	 * @param medical the medical.
	 * @return the total quantity.
	 */
	public int getCurrentQuantity(Ward ward, Medical medical) {
		try {
			return ioOperations.getCurrentQuantity(ward, medical);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return 0;
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
