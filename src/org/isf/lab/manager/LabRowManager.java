package org.isf.lab.manager;

import java.util.ArrayList;

import org.isf.lab.model.Laboratory;
import org.isf.lab.model.LaboratoryRow;
import org.isf.lab.service.LabIoOperations;
import org.isf.utils.exception.OHServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LabRowManager {

	private final Logger logger = LoggerFactory.getLogger(LabRowManager.class);
	
	@Autowired
	private LabIoOperations ioOperations;
	
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
