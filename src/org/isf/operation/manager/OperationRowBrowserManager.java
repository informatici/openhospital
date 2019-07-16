/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.isf.operation.manager;

import java.util.List;
import org.isf.admission.model.Admission;
import org.isf.menu.manager.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.isf.operation.model.OperationRow;
import org.isf.operation.service.OperationRowIoOperations;
import org.isf.utils.exception.OHException;
/**
 *
 * @author xavier
 */
public class OperationRowBrowserManager {
    private final Logger logger = LoggerFactory.getLogger(OperationRowBrowserManager.class);
    private OperationRowIoOperations ioOperations = Context.getApplicationContext().getBean(OperationRowIoOperations.class);
    
    public List<OperationRow> getOperationRowByAdmission(Admission adm) throws OHException{
	return ioOperations.getOperationRowByAdmission(adm);
    }
    
    public boolean deleteOperationRow(OperationRow operationRow) {
        try {
            boolean res = ioOperations.deleteOperationRow(operationRow);
            return res;
        } catch (OHException ex) {
            return false;
        }
    }

    public void updateOperationRow(OperationRow opRow) {
        ioOperations.updateOperationRow(opRow);
    }

    public void newOperationRow(OperationRow opRow) {
        ioOperations.newOperationRow(opRow);
    }
}
