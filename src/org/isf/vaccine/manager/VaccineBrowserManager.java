package org.isf.vaccine.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.isf.vaccine.model.Vaccine;
import org.isf.vaccine.service.VaccineIoOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that provides gui separation from database operations and gives some
 * useful logic manipulations of the dinamic data (memory)
 *
 * @author Eva
 *
 *
 * modification history
 *  20/10/2011 - Cla - insert vaccinetype managment
 *
 */
public class VaccineBrowserManager {

    private final Logger logger = LoggerFactory.getLogger(VaccineBrowserManager.class);
	private VaccineIoOperations ioOperations = Menu.getApplicationContext().getBean(VaccineIoOperations.class);

	/**
	 * returns the list of {@link Vaccine}s in the DB
	 *
	 * @return the list of {@link Vaccine}s
	 */
	public ArrayList<Vaccine> getVaccine() throws OHServiceException {
		return getVaccine(null);
	}

	/**
	 * returns the list of {@link Vaccine}s based on vaccine type code
	 *
	 * @param vaccineTypeCode - the type code.
	 * @return the list of {@link Vaccine}s
	 */
	public ArrayList<Vaccine> getVaccine(String vaccineTypeCode) throws OHServiceException {
		try {
			return ioOperations.getVaccine(vaccineTypeCode);
        } catch (OHException e) {
				/*Already cached exception with OH specific error message -
				 * create ready to return OHServiceException and keep existing error message
				 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"), OHSeverityLevel.ERROR));
        }
	}
	
	/**
	 * inserts a new {@link Vaccine} in the DB
	 *
	 * @param vaccine - the item to insert
	 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise
	 */
	public boolean newVaccine(Vaccine vaccine) throws OHServiceException {
		try {
			return ioOperations.newVaccine(vaccine);
        } catch (OHException e) {
				/*Already cached exception with OH specific error message -
				 * create ready to return OHServiceException and keep existing error message
				 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
        }
	}

    /**
     * updates the specified {@link Vaccine} object.
     * @param vaccine - the {@link Vaccine} object to update.
     * @return <code>true</code> if has been updated, <code>false</code> otherwise.
     */
    public boolean hasVaccineModified(Vaccine vaccine) throws OHServiceException {
        try {
            return ioOperations.hasVaccineModified(vaccine);
        } catch (OHException e) {
				/*Already cached exception with OH specific error message -
				 * create ready to return OHServiceException and keep existing error message
				 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"), OHSeverityLevel.ERROR));
        }
    }

	/**
	 * updates the specified {@link Vaccine} object.
	 * @param vaccine - the {@link Vaccine} object to update.
	 * @return <code>true</code> if has been updated, <code>false</code> otherwise.
	 */
	public boolean updateVaccine(Vaccine vaccine) throws OHServiceException {
        try {
            return ioOperations.updateVaccine(vaccine);
        } catch (OHException e) {
				/*Already cached exception with OH specific error message -
				 * create ready to return OHServiceException and keep existing error message
				 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
        }
    }
	
	/**
	 * deletes a {@link Vaccine} in the DB
	 *
	 * @param vaccine - the item to delete
	 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise
	 */
	public boolean deleteVaccine(Vaccine vaccine) throws OHServiceException {
		try {
			return ioOperations.deleteVaccine(vaccine);
        } catch (OHException e) {
				/*Already cached exception with OH specific error message -
				 * create ready to return OHServiceException and keep existing error message
				 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
        }
	}
	
	/**
	 * checks if the code is already in use
	 *
	 * @param code - the vaccine code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 */
	public boolean codeControl(String code) throws OHServiceException {
		try {
			return ioOperations.isCodePresent(code);
        } catch (OHException e) {
				/*Already cached exception with OH specific error message -
				 * create ready to return OHServiceException and keep existing error message
				 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"), OHSeverityLevel.ERROR));
        }
	}
}
