package org.isf.disease.manager;

/*------------------------------------------
 * DiseaseBrowserManager - Class that provides gui separation from database operations and gives some
 * 						   useful logic manipulations of the dinamic data (memory)
 * -----------------------------------------
 * modification history
 * 25/01/2006 - Rick, Vero, Pupo  - first beta version 
 * 08/11/2006 - ross - added getDiseaseOpd members, and getDiseaseIpd  
 * 					   to get only opd/ipd related diseases
 *------------------------------------------*/

import java.util.ArrayList;
import java.util.List;

import org.isf.disease.model.Disease;
import org.isf.disease.service.DiseaseIoOperations;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The manage class for the disease module.
 */
@Component
public class DiseaseBrowserManager {

	@Autowired
	private DiseaseIoOperations ioOperations;

	/**
	 * Returns all the stored {@link Disease} with ODP flag <code>true</code>.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the stored diseases with ODP flag true.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDiseaseOpd() throws OHServiceException {
        return ioOperations.getDiseases(null,true,false,false);
	}

	/**
	 * Returns all diseases, deleted ones also
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the stored diseases.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDiseaseAll() throws OHServiceException {
        return ioOperations.getDiseases(null,false,false,false);
	}

	/**
	 * Returns all the stored {@link Disease} with the specified typecode and flag ODP true.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param typecode the filter typecode.
	 * @return the retrieved diseases.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDiseaseOpd(String typecode) throws OHServiceException {
        return ioOperations.getDiseases(typecode,true,false,false);
	}

	/**
	 * Returns all the stored {@link Disease} with IPD_OUT flag <code>true</code>.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the stored disease with IPD flag <code>true</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDiseaseIpdOut() throws OHServiceException {
        return ioOperations.getDiseases(null,false,false,true);
	}

	/**
	 * Returns all the stored {@link Disease} with the specified typecode and the flag IPD_OUT <code>true</code>.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param typecode the filter typecode.
	 * @return the retrieved diseases.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDiseaseIpdOut(String typecode) throws OHServiceException {
        return ioOperations.getDiseases(typecode,false,false,true);
	}
	
	/**
	 * Returns all the stored {@link Disease} with IPD_IN flag <code>true</code>.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the stored disease with IPD flag <code>true</code>.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDiseaseIpdIn() throws OHServiceException {
        return ioOperations.getDiseases(null,false,true,false);
	}

	/**
	 * Returns all the stored {@link Disease} with the specified typecode and the flag IPD_IN <code>true</code>.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param typecode the filter typecode.
	 * @return the retrieved diseases.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDiseaseIpdIn(String typecode) throws OHServiceException {
        return ioOperations.getDiseases(typecode,false,true,false);
	}
	
	/**
	 * Returns both OPD and IPDs diseases.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @return the stored diseases.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDisease() throws OHServiceException {
        return ioOperations.getDiseases(null,true,true,true);
	}

	/**
	 * Retrieves all OPD and IPDs {@link Disease} with the specified typecode.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param typecode the filter typecode.
	 * @return all the diseases with the specified typecode.
	 * @throws OHServiceException 
	 */
	public ArrayList<Disease> getDisease(String typecode) throws OHServiceException {
        return ioOperations.getDiseases(typecode,false,false,false);
	}

	/**
	 * Gets a {@link Disease} with the specified code.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param code the disease code.
	 * @return the found disease, <code>null</code> if no disease has found.
	 * @throws OHServiceException 
	 */
	public Disease getDiseaseByCode(int code) throws OHServiceException {
        return ioOperations.getDiseaseByCode(code);
	}

	/**
	 * Stores the specified {@link Disease}. 
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param disease the disease to store.
	 * @return <code>true</code> if the disease has been stored, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newDisease(Disease disease) throws OHServiceException {
        List<OHExceptionMessage> errors = validateDisease(disease, true);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        return ioOperations.newDisease(disease);
	}
	
	/**
	 * Updates the specified {@link Disease}.
	 * If the disease has been updated concurrently a overwrite confirmation message is shown.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param disease the disease to update.
	 * @return <code>true</code> if the disease has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateDisease(Disease disease) throws OHServiceException {
        List<OHExceptionMessage> errors = validateDisease(disease, true);
        if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
        return ioOperations.updateDisease(disease);
	}

	/**
	 * Mark as deleted the specified {@link Disease}.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param disease the disease to make delete.
	 * @return <code>true</code> if the disease has been marked, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteDisease(Disease disease) throws OHServiceException {
        return ioOperations.deleteDisease(disease);
	}

	/**
	 * Check if the specified code is used by other {@link Disease}s.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param code the code to check.
	 * @return <code>true</code> if it is already used, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean codeControl(String code) throws OHServiceException {
        return ioOperations.isCodePresent(code);
	}

	/**
	 * Checks if the specified description is used by a disease with the specified type code.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param description the description to check.
	 * @param typeCode the disease type code.
	 * @return <code>true</code> if is used, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean descriptionControl(String description,String typeCode) throws OHServiceException {
        return ioOperations.isDescriptionPresent(description,typeCode);
	}

    private List<OHExceptionMessage> validateDisease(Disease disease, boolean insert) throws OHServiceException {
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();

        if (insert){
            String key = disease.getCode();
            if (key.equals("")){
                errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.disease.pleaseinsertacode"),
                        OHSeverityLevel.ERROR));
            }
            if (key.length()>10){
                errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.disease.codetoolongmaxchars"),
                        OHSeverityLevel.ERROR));
            }
            if (codeControl(key)){
                errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.common.codealreadyinuse"),
                        OHSeverityLevel.ERROR));
            }
        }

        if (disease.getDescription().equals("")){
            errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.disease.pleaseinsertavaliddescription"),
                    OHSeverityLevel.ERROR));
        }

        Disease oldDisease = null;
        if(!insert){
            oldDisease = getDiseaseByCode(Integer.valueOf(disease.getCode()));
        }
        String lastDescription = oldDisease == null ? null : oldDisease.getDescription();
        //if inserting or description has changed on updating
        //avoid two disease with the same description for the same type
        if (lastDescription == null || !lastDescription.equals(disease.getDescription())) {
            if (descriptionControl(disease.getDescription(),
                    disease.getType().getCode())){
                errors.add(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), MessageBundle.getMessage("angal.disease.diseasealreadypresent"),
                        OHSeverityLevel.ERROR));
            }
        }

	    return errors;
    }
}
