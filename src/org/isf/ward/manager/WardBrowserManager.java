package org.isf.ward.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;
import org.isf.ward.service.IoOperations;

/**
 * Class that provides gui separation from database operations and gives some
 * useful logic manipulations of the dinamic data (memory)
 * 
 * @author Rick
 * 
 */
public class WardBrowserManager {

	private IoOperations ioOperations = Menu.getApplicationContext().getBean(IoOperations.class);

	/**
	 * Returns all stored wards.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the stored wards.
	 */
	public ArrayList<Ward> getWards() {
		try {
			return ioOperations.getWards(null);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return new ArrayList<Ward>();
		}
	}
	
	/**
	 * Returns all the stored {@link Ward} with maternity flag <code>false</code>.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the stored diseases with maternity flag false.
	 * @return
	 */
	public ArrayList<Ward> getWardsNoMaternity() {
		try {
			return ioOperations.getWardsNoMaternity();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return new ArrayList<Ward>();
		}
	}

	/**
	 * Stores the specified {@link Ward}. 
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param ward the ward to store.
	 * @return <code>true</code> if the ward has been stored, <code>false</code> otherwise.
	 */
	public boolean newWard(Ward ward) {
		try {
			return ioOperations.newWard(ward);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Updates the specified {@link Ward}.
	 * If the ward has been updated concurrently a overwrite confirmation message is shown.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param ward the ward to update.
	 * @return <code>true</code> if the ward has been updated, <code>false</code> otherwise.
	 */
	public boolean updateWard(Ward ward) {
		boolean isConfirmedOverwriteRecord = false;
		try {
			int admitted_patients_in_ward = getCurrentOccupation(ward);
			if (ward.getBeds() < admitted_patients_in_ward) {
				String message = MessageBundle.getMessagePattern("angal.ward.pattern.patientsarestilladmittedinward", admitted_patients_in_ward);
				throw new OHException(message);
			}
			if (!(ioOperations.isLockWard(ward))) {
				isConfirmedOverwriteRecord = (JOptionPane.showConfirmDialog(null, 
						MessageBundle.getMessage("angal.ward.thedatahasbeenupdatedbysomeoneelse") + ". " +
						MessageBundle.getMessage("angal.ward.doyouwanttooverwritethedata") + "?", 
						MessageBundle.getMessage("angal.ward.select"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION);
			}
			return ioOperations.updateWard(ward,isConfirmedOverwriteRecord);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Mark as deleted the specified {@link Ward}.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param ward the ward to make delete.
	 * @return <code>true</code> if the ward has been marked, <code>false</code> otherwise.
	 */
	public boolean deleteWard(Ward ward) {
		try {
			return ioOperations.deleteWard(ward);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * Check if the specified code is used by other {@link Ward}s.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param code the code to check.
	 * @return <code>true</code> if it is already used, <code>false</code> otherwise.
	 */
	public boolean codeControl(String code) {
		try {
			return ioOperations.isCodePresent(code);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * Check if the specified maternity is used by other {@link Ward}s.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param code the code to check.
	 * @return <code>true</code> if it is already used, <code>false</code> otherwise.
	 */
	public boolean maternityControl() {
		try {
			return ioOperations.isMaternityPresent();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * Retrieves the number of patients currently admitted in the {@link Ward}
	 * @param ward - the ward
	 * @return the number of patients currently admitted, <code>-1</code> if an error occurs
	 */
	public int getCurrentOccupation(Ward ward) throws OHException {
		try {
			return ioOperations.getCurrentOccupation(ward);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return -1;
		}
	}
	
}
