package org.isf.vactype.manager;

/*------------------------------------------
 * VaccineTypeBrowserManager - 
 * -----------------------------------------
 * modification history
 * 19/10/2011 - Cla - version is now 1.0
 *------------------------------------------*/

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.therapy.manager.TherapyManager;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.isf.vactype.model.VaccineType;
import org.isf.vactype.service.VacTypeIoOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VaccineTypeBrowserManager {
	
			private final Logger logger = LoggerFactory.getLogger(TherapyManager.class);
	
		private VacTypeIoOperation ioOperations = Menu.getApplicationContext().getBean(VacTypeIoOperation.class);
		
		/**
		 * This method returns all {@link VaccineType}s from DB	
		 * 	
		 * @return the list of {@link VaccineType}s
		 * @throws OHServiceException 
		 */
		public ArrayList<VaccineType> getVaccineType() throws OHServiceException {
			try {
				return ioOperations.getVaccineType();
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
		 * inserts a new {@link VaccineType} into DB
		 * 
		 * @param vaccineType - the {@link VaccineType} to insert 
		 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise
		 * @throws OHServiceException 
		 */
		public boolean newVaccineType(VaccineType vaccineType) throws OHServiceException {
			try {
				return ioOperations.newVaccineType(vaccineType);
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
		 * update a {@link VaccineType} in the DB
		 *
		 * @param vaccineType - the item to update
		 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise
		 * @throws OHServiceException 
		 */
		public boolean updateVaccineType(VaccineType vaccineType) throws OHServiceException {
				try {
					return ioOperations.updateVaccineType(vaccineType);
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
		 * deletes a {@link VaccineType} in the DB
		 *
		 * @param vaccineType - the item to delete
		 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise
		 * @throws OHServiceException 
		 */
		public boolean deleteVaccineType(VaccineType vaccineType) throws OHServiceException {
			try {
				return ioOperations.deleteVaccineType(vaccineType);
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
		 * @param code - the {@link VaccineType} code
		 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
		 * @throws OHServiceException 
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
						MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
			}
		}
}
