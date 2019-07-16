/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.isf.operation.manager;

import java.util.ArrayList;
import java.util.List;
import org.isf.admission.model.Admission;
import org.isf.menu.manager.Context;
import org.isf.opd.model.Opd;
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
    
    public ArrayList<OperationRow> getOperationRowByOpd(Opd opd) {
        ArrayList<OperationRow> results = new ArrayList<OperationRow>();
        try {
            results = ioOperations.getOperationRowByOpd(opd);
        } catch (OHException ex) {
            //ignore
        }
        return results;
    }
    
    public boolean deleteOperationRow(OperationRow operationRow) {
        try {
            boolean res = ioOperations.deleteOperationRow(operationRow);
            return res;
        } catch (OHException ex) {
            return false;
        }
    }

    public boolean updateOperationRow(OperationRow opRow) {
        try {
            ioOperations.updateOperationRow(opRow);
            return true;
        } catch (OHException ex) {
            return false;
        }
    }

    public boolean newOperationRow(OperationRow opRow) {
        try {
            ioOperations.newOperationRow(opRow);
            return true;
        } catch (OHException ex) {
            return false;
        }
    }
}
