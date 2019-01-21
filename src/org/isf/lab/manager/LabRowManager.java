package org.isf.lab.manager;

import java.util.ArrayList;

import org.isf.lab.model.Laboratory;
import org.isf.lab.model.LaboratoryRow;
import org.isf.lab.service.LabIoOperations;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LabRowManager {

	private final Logger logger = LoggerFactory.getLogger(LabRowManager.class);
	
	private LabIoOperations ioOperations = Context.getApplicationContext().getBean(LabIoOperations.class);
	
	/**
	 * Return a list of results ({@link LaboratoryRow}s) for passed lab entry.
	 * @param code - the {@link Laboratory} record ID.
	 * @return the list of {@link LaboratoryRow}s. It could be <code>empty</code>
	 * @throws OHServiceException 
	 */
	public ArrayList<LaboratoryRow> getLabRowByLabId(Integer code) throws OHServiceException{
		return ioOperations.getLabRow(code);
	}
}
