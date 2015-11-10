package org.isf.lab.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.lab.model.Laboratory;
import org.isf.lab.model.LaboratoryRow;
import org.isf.lab.service.IoOperations;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;

public class LabRowManager {

	private IoOperations ioOperations = Menu.getApplicationContext().getBean(IoOperations.class);
	
	/**
	 * Return a list of results ({@link LaboratoryRow}s) for passed lab entry.
	 * @param code - the {@link Laboratory} record ID.
	 * @return the list of {@link LaboratoryRow}s. It could be <code>empty</code>
	 */
	public ArrayList<LaboratoryRow> getLabRow(Integer code){
		try {
			return ioOperations.getLabRow(code);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return new ArrayList<LaboratoryRow>();
		}
	}
}
