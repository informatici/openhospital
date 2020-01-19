package org.isf.agetype.manager;

import java.util.ArrayList;
import java.util.List;

import org.isf.agetype.model.AgeType;
import org.isf.agetype.service.AgeTypeIoOperations;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AgeTypeBrowserManager {

	@Autowired
	private AgeTypeIoOperations ioOperations;

	/**
	 * Returns all available age types.
	 * @return a list of {@link AgeType} or <code>null</code> if the operation fails.
	 * @throws OHServiceException 
	 */
	public ArrayList<AgeType> getAgeType() throws OHServiceException {
        return ioOperations.getAgeType();
	}

	/**
	 * Updates the list of {@link AgeType}s.
	 * @param ageType the {@link AgeType} to update.
	 * @return <code>true</code> if the list has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateAgeType(ArrayList<AgeType> ageTypes) throws OHServiceException {
        List<OHExceptionMessage> errors = validateAgeTypes(ageTypes);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        return ioOperations.updateAgeType(ageTypes);
	}


    /**
	 * Retrieves the {@link AgeType} code using the age value.
	 * @param age the age value.
	 * @return the retrieved code, <code>null</code> if age value is out of any range.
	 * @throws OHServiceException 
	 */
	public String getTypeByAge(int age) throws OHServiceException {
        ArrayList<AgeType> ageTable = ioOperations.getAgeType();

        for (AgeType ageType : ageTable) {

            if (age >= ageType.getFrom() && age <= ageType.getTo()) {
                return ageType.getCode();
            }
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
        return ioOperations.getAgeTypeByCode(index);
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
