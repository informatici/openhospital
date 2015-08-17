/**
 * 19-dec-2005
 * 14-jan-2006
 */
package org.isf.medicals.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.medicals.model.Medical;
import org.isf.medicals.service.IoOperations;
import org.isf.utils.exception.OHException;
import org.isf.generaldata.MessageBundle;

/**
 * Class that provides gui separation from database operations and gives some
 * useful logic manipulations of the dinamic data (memory)
 * 
 * @author bob
 * 
 */
public class MedicalBrowsingManager {

	private IoOperations ioOperations = new IoOperations();
	
	/**
	 * Returns the requested medical.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param code the medical code.
	 * @return the retrieved medical.
	 */
	public Medical getMedical(int code) {
		try {
			return ioOperations.getMedical(code);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Returns all the medicals.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return all the medicals.
	 */
	public ArrayList<Medical> getMedicals() {
		try {
			return ioOperations.getMedicals();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Returns all the medicals with the specified description.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param description the medical description.
	 * @return all the medicals with the specified description.
	 */
	public ArrayList<Medical> getMedicals(String description) {
		try {
			return ioOperations.getMedicals(description);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Return all the medicals with the specified criteria.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param description the medical description or <code>null</code>
	 * @param type the medical type or <code>null</code>.
	 * @param expiring <code>true</code> to include only expiring medicals.
	 * @return the retrieved medicals.
	 */
	public ArrayList<Medical> getMedicals(String description, String type, boolean expiring) {
		try {
			return ioOperations.getMedicals(description, type, expiring);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Saves the specified {@link Medical}. The medical is updated with the generated id.
	 * In case of wrong parameters values a message error is shown and a <code>false</code> value is returned.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param medical the medical to store.
	 * @return <code>true</code> if the medical has been stored, <code>false</code> otherwise.
	 */
	public boolean newMedical(Medical medical) {

		if(medical.getMinqty()<0){
			JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicals.minquantitycannotbelessthan0"));
			return false;
		}

		if(medical.getDescription().equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicals.inseravaliddescription"));
			return false;
		}

		try {
			boolean medicalExists = ioOperations.medicalExists(medical);
			if (medicalExists) {
				JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicals.thetypemedicalyouinsertedwasalreadyinuse"));
				return false;
			} else return ioOperations.newMedical(medical);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Updates the specified medical.
	 * In case of wrong parameters values a message error is shown and a <code>false</code> value is returned.
	 * In case of concurrent modification an overwrite request is made.
	 * In case of already deleted medical an error message is shown.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param medical the medical to update.
	 * @return <code>true</code> if updated, <code>false</code> otherwise.
	 */
	public boolean updateMedical(Medical medical) {

		if(medical.getMinqty()<0){
			JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicals.minquantitycannotbelessthan0"));
			return false;
		}

		try {
			int lock = ioOperations.getMedicalLock(medical.getCode());
			if (lock>=0) {
				//ok the record is present, it was not deleted
				if (lock!=medical.getLock()) {
					//it was updated by someone else
					String msg = MessageBundle.getMessage("angal.medicals.thedatahasbeenupdatedbysomeoneelse") +
							MessageBundle.getMessage("angal.medicals.doyouwanttooverwritethedata");
					int response = JOptionPane.showConfirmDialog(null, msg, MessageBundle.getMessage("angal.medicals.select"), JOptionPane.YES_NO_OPTION);
					if (response== JOptionPane.OK_OPTION) {
						return ioOperations.updateMedical(medical);
					} else return false;
				} else {
					//ok it was not updated
					return ioOperations.updateMedical(medical);
				}

			} else {
				//the record was deleted since the last read
				JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicals.couldntfindthedataithasprobablybeendeleted"));
				return false;
			}
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}

	}

	/**
	 * Deletes the specified medical.
	 * If the medical is involved in stock movement an error message is shown.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param medical the medical to delete.
	 * @return <code>true</code> if the medical has been deleted.
	 */
	public boolean deleteMedical(Medical medical) {
		try {
			boolean inStockMovement = ioOperations.isMedicalReferencedInStockMovement(medical.getCode());

			if(inStockMovement){
				JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicals.therearestockmovementsreferredtothismedical"));
				return false;
			}
			return ioOperations.deleteMedical(medical);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
}
