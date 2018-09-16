package org.isf.agetype.manager;

import java.util.ArrayList;
import java.util.List;

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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
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
            List<OHExceptionMessage> errors = validateAgeTypes(ageTypes);
            if(!errors.isEmpty()){
                throw new OHServiceException(errors);
            }
			return ioOperations.updateAgeType(ageTypes);
        } catch (OHServiceException e) {
            throw e;
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
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
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

    private List<OHExceptionMessage> validateAgeTypes(ArrayList<AgeType> ageTypes) {
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        for (int i = 1; i < ageTypes.size(); i++) {
            if (ageTypes.get(i).getFrom() <= ageTypes.get(i-1).getTo()) {
                errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),MessageBundle.getMessage("angal.agetype.rangesoverlappleasecheck"),
                        OHSeverityLevel.ERROR));
            }
            if (ageTypes.get(i).getFrom() - ageTypes.get(i-1).getTo() > 1) {
                errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),MessageBundle.getMessage("angal.agetype.rangesnotdefinedpleasecheck"),
                        OHSeverityLevel.ERROR));
            }
        }
        return errors;
    }
}
