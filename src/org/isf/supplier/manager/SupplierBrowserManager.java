package org.isf.supplier.manager;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.Menu;
import org.isf.supplier.model.Supplier;
import org.isf.supplier.service.SupplierOperations;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SupplierBrowserManager {

    private final Logger logger = LoggerFactory.getLogger(SupplierBrowserManager.class);
    private SupplierOperations ioOperations = Menu.getApplicationContext().getBean(SupplierOperations.class);

    public boolean saveOrUpdate(Supplier supplier) throws OHServiceException {
        try {
            return ioOperations.saveOrUpdate(supplier);
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

    public Supplier getByID(int ID) throws OHServiceException {
        try {
            return ioOperations.getByID(ID);
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

    public List<Supplier> getAll() throws OHServiceException {
        try {
            return ioOperations.getAll();
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

    public List<Supplier> getList() throws OHServiceException {
        try {
            return ioOperations.getList();
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

