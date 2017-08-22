package org.isf.agetype.manager;

import java.util.ArrayList;

import org.isf.agetype.model.AgeType;
import org.isf.agetype.service.AgeTypeIoOperations;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgeTypeBrowserManager {

	private final Logger logger = LoggerFactory.getLogger(AgeTypeBrowserManager.class);
	
	private AgeTypeIoOperations ioOperations = Menu.getApplicationContext().getBean(AgeTypeIoOperations.class);

	/**
	 * Returns all available age types.
	 * @return a list of {@link AgeType} or <code>null</code> if the operation fails.
	 * @throws OHServiceException 
	 */
	public ArrayList<AgeType> getAgeType() throws OHServiceException {
		try{
			return ioOperations.getAgeType();
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Updates the list of {@link AgeType}s.
	 * @param ageType the {@link AgeType} to update.
	 * @return <code>true</code> if the list has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateAgeType(ArrayList<AgeType> ageTypes) throws OHServiceException {
		try{
			return ioOperations.updateAgeType(ageTypes);
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}

	/**
	 * Retrieves the {@link AgeType} code using the age value.
	 * @param age the age value.
	 * @return the retrieved code, <code>null</code> if age value is out of any range.
	 * @throws OHServiceException 
	 */
	public String getTypeByAge(int age) throws OHServiceException {
		try{
			ArrayList<AgeType> ageTable = ioOperations.getAgeType();

			for (AgeType ageType : ageTable) {

				if (age >= ageType.getFrom() && age <= ageType.getTo()) {
					return ageType.getCode();
				}
			}
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
		return null;
	}

	/**
	 * Gets the {@link AgeType} from the code index.
	 * @param index the code index.
	 * @return the retrieved element, <code>null</code> otherwise.
	 * @throws OHServiceException 
	 */
	public AgeType getTypeByCode(int index) throws OHServiceException {
		try{
			return ioOperations.getAgeTypeByCode(index);
		}  catch(OHException e){
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(null, 
					MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), OHSeverityLevel.ERROR));
		}
	}
}
