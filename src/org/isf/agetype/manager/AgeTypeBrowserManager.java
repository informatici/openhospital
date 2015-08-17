package org.isf.agetype.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.agetype.service.IoOperations;
import org.isf.agetype.model.AgeType;
import org.isf.utils.exception.OHException;

public class AgeTypeBrowserManager {

	private IoOperations ioOperations = new IoOperations();

	/**
	 * Returns all available age types.
	 * @return a list of {@link AgeType} or <code>null</code> if the operation fails.
	 */
	public ArrayList<AgeType> getAgeType() {
		try{
			return ioOperations.getAgeType();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Updates the list of {@link AgeType}s.
	 * @param ageType the {@link AgeType} to update.
	 * @return <code>true</code> if the list has been updated, <code>false</code> otherwise.
	 */
	public boolean updateAgeType(ArrayList<AgeType> ageTypes) {
		try{
			return ioOperations.updateAgeType(ageTypes);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Retrieves the {@link AgeType} code using the age value.
	 * @param age the age value.
	 * @return the retrieved code, <code>null</code> if age value is out of any range.
	 */
	public String getTypeByAge(int age) {
		try{
			ArrayList<AgeType> ageTable = ioOperations.getAgeType();

			for (AgeType ageType : ageTable) {

				if (age >= ageType.getFrom() && age <= ageType.getTo()) {
					return ageType.getCode();
				}
			}
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
		return null;
	}

	/**
	 * Gets the {@link AgeType} from the code index.
	 * @param index the code index.
	 * @return the retrieved element, <code>null</code> otherwise.
	 */
	public AgeType getTypeByCode(int index) {
		try{
			return ioOperations.getAgeTypeByCode(index);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
}
