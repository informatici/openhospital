package org.isf.dlvrrestype.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;


import org.isf.dlvrrestype.model.DeliveryResultType;
import org.isf.dlvrrestype.service.IoOperation;
import org.isf.utils.exception.OHException;

/**
 * Manager class for DeliveryResultTypeModule.
 */
public class DeliveryResultTypeBrowserManager {

	private IoOperation ioOperations = new IoOperation();

	/**
	 * Returns all stored {@link DeliveryResultType}s.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the stored {@link DeliveryResultType}s, <code>null</code> if an error occurred.
	 */
	public ArrayList<DeliveryResultType> getDeliveryResultType() {
		try {
			return ioOperations.getDeliveryResultType();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Stores the specified {@link DeliveryResultType}.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param deliveryresultType the delivery result type to store.
	 * @return <code>true</code> if the delivery result type has been stored.
	 */
	public boolean newDeliveryResultType(DeliveryResultType deliveryresultType) {
		try {
			return ioOperations.newDeliveryResultType(deliveryresultType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Updates the specified {@link DeliveryResultType}.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param deliveryresultType the delivery result type to update.
	 * @return <code>true</code> if the delivery result type has been updated, <code>false</code> otherwise.
	 */
	public boolean updateDeliveryResultType(DeliveryResultType deliveryresultType) {
		try {
			return ioOperations.updateDeliveryResultType(deliveryresultType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Checks if the specified code is already used by others {@link DeliveryResultType}s.
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
	 * Deletes the specified {@link DeliveryResultType}.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param deliveryresultType the delivery result type to delete.
	 * @return <code>true</code> if the delivery result type has been deleted, <code>false</code> otherwise.
	 */
	public boolean deleteDeliveryResultType(DeliveryResultType deliveryresultType) {
		try {
			return ioOperations.deleteDeliveryResultType(deliveryresultType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

}
