package org.isf.disctype.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.disctype.model.DischargeType;
import org.isf.disctype.service.IoOperation;
import org.isf.utils.exception.OHException;

public class DischargeTypeBrowserManager {

	private IoOperation ioOperations = new IoOperation();

	/**
	 * method that returns all DischargeTypes in a list
	 * 
	 * @return the list of all DischargeTypes (could be null)
	 */
	public ArrayList<DischargeType> getDischargeType() {
		try {
			return ioOperations.getDischargeType();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * method that create a new DischargeType
	 * 
	 * @param dischargeType
	 * @return true - if the new DischargeType has been inserted
	 */
	public boolean newDischargeType(DischargeType dischargeType) {
		try {
			return ioOperations.newDischargeType(dischargeType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * method that updates an already existing DischargeType
	 * 
	 * @param dischargeType
	 * @return true - if the existing DischargeType has been updated
	 */
	public boolean updateDischargeType(DischargeType dischargeType) {
		try {
			return ioOperations.UpdateDischargeType(dischargeType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * method that check if a DischargeType already exists
	 * 
	 * @param code
	 * @return true - if the DischargeType already exists
	 */
	public boolean codeControl(String code) {
		try {
			return ioOperations.isCodePresent(code);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return true;
		}
	}

	/**
	 * method that delete a DischargeType
	 * 
	 * @param dischargeType
	 * @return true - if the DischargeType has been deleted
	 */
	public boolean deleteDischargeType(DischargeType dischargeType) {
		try {
			return ioOperations.deleteDischargeType(dischargeType);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

}
