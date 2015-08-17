package org.isf.dlvrtype.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;


import org.isf.dlvrtype.model.DeliveryType;
import org.isf.dlvrtype.service.IoOperation;
import org.isf.utils.exception.OHException;

/**
 * The manager class for the DeliveryType module.
 */
public class DeliveryTypeBrowserManager {

	private IoOperation ioOperations = new IoOperation();

	/**
	 * Returns all stored {@link DeliveryType}s.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return all stored delivery types, <code>null</code> if an error occurred.
	 */
	public ArrayList<DeliveryType> getDeliveryType() {
		try {
			return ioOperations.getDeliveryType();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Stores the specified {@link DeliveryType}.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param deliveryType the delivery type to store.
	 * @return <code>true</code> if the delivery type has been stored, <code>false</code> otherwise.
	 */
	public boolean newDeliveryType(DeliveryType deliveryType) {
		try {
			return ioOperations.newDeliveryType(deliveryType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Updates the specified {@link DeliveryType}.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param deliveryType the delivery type to update.
	 * @return <code>true</code> if the delivery type has been update.
	 */
	public boolean updateDeliveryType(DeliveryType deliveryType) {
		try {
			return ioOperations.updateDeliveryType(deliveryType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Checks if the specified code is already used by others {@link DeliveryType}s.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, <code>false</code> otherwise.
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
	 * Delete the specified {@link DeliveryType}.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param deliveryType the delivery type to delete.
	 * @return <code>true</code> if the delivery type has been deleted, <code>false</code> otherwise.
	 */
	public boolean deleteDeliveryType(DeliveryType deliveryType) {
		try {
			return ioOperations.deleteDeliveryType(deliveryType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
}
