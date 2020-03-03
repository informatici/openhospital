package org.isf.supplier.manager;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.supplier.model.Supplier;
import org.isf.supplier.service.SupplierOperations;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class SupplierBrowserManager {

    private final Logger logger = LoggerFactory.getLogger(SupplierBrowserManager.class);
    @Autowired
    private SupplierOperations ioOperations;

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
    
    /**
	 * returns the {@link HashMap} of all {@link Supplier}s 
	 * @param all - if <code>true</code> it will returns deleted ones also
	 * @return the {@link HashMap} of all {@link Supplier}s  
     * @throws OHServiceException 
	 */
	public HashMap<Integer, String> getHashMap(boolean all) throws OHServiceException {
		List<Supplier> supList = getAll();
		HashMap<Integer, String> supMap = new HashMap<Integer, String>();
		for (Supplier sup : supList) {
			supMap.put(sup.getSupId(), sup.getSupName());
		}
		return supMap;
	}
}

