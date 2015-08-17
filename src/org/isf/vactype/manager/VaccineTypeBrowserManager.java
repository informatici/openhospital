package org.isf.vactype.manager;

/*------------------------------------------
 * VaccineTypeBrowserManager - 
 * -----------------------------------------
 * modification history
 * 19/10/2011 - Cla - version is now 1.0
 *------------------------------------------*/

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.utils.exception.OHException;
import org.isf.vactype.model.VaccineType;
import org.isf.vactype.service.IoOperation;

public class VaccineTypeBrowserManager {
	
		private IoOperation ioOperations = new IoOperation();
		
		/**
		 * This method returns all {@link VaccineType}s from DB	
		 * 	
		 * @return the list of {@link VaccineType}s
		 */
		public ArrayList<VaccineType> getVaccineType() {
			try {
				return ioOperations.getVaccineType();
			} catch (OHException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
				return null;
			}
		}
		
		/**
		 * inserts a new {@link VaccineType} into DB
		 * 
		 * @param vaccineType - the {@link VaccineType} to insert 
		 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise
		 */
		public boolean newVaccineType(VaccineType vaccineType) {
			try {
				return ioOperations.newVaccineType(vaccineType);
			} catch (OHException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
				return false;
			}
		}

		/**
		 * update a {@link VaccineType} in the DB
		 *
		 * @param vaccineType - the item to update
		 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise
		 */
		public boolean UpdateVaccineType(VaccineType vaccineType) {
				try {
					return ioOperations.updateVaccineType(vaccineType);
				} catch (OHException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					return false;
				}
		}
		
		/**
		 * deletes a {@link VaccineType} in the DB
		 *
		 * @param vaccineType - the item to delete
		 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise
		 */
		public boolean deleteVaccineType(VaccineType vaccineType) {
			try {
				return ioOperations.deleteVaccineType(vaccineType);
			} catch (OHException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
				return false;
			}
		}
		
		/**
		 * checks if the code is already in use
		 *
		 * @param code - the {@link VaccineType} code
		 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
		 */
		public boolean codeControl(String code) {
			try {
				return ioOperations.isCodePresent(code);
			} catch (OHException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
				return false;
			}
		}
}
