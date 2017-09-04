package org.isf.lab.manager;

import java.util.ArrayList;

import org.isf.generaldata.MessageBundle;
import org.isf.lab.model.Laboratory;
import org.isf.lab.model.LaboratoryRow;
import org.isf.lab.service.LabIoOperations;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LabRowManager {

	private final Logger logger = LoggerFactory.getLogger(LabRowManager.class);
	
	private LabIoOperations ioOperations = Menu.getApplicationContext().getBean(LabIoOperations.class);
	
	/**
	 * Return a list of results ({@link LaboratoryRow}s) for passed lab entry.
	 * @param code - the {@link Laboratory} record ID.
	 * @return the list of {@link LaboratoryRow}s. It could be <code>empty</code>
	 * @throws OHServiceException 
	 */
	public ArrayList<LaboratoryRow> getLabRowByLabId(Integer code) throws OHServiceException{
		try {
			return ioOperations.getLabRowByLabId(code);
		} catch (OHException e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, e.getMessage(), OHSeverityLevel.ERROR));
		} catch (Exception e) {
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null,
					MessageBundle.getMessage("angal.lab.problemsoccuredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}
}
