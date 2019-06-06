/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.isf.operation.service;

import java.util.ArrayList;
import org.isf.admission.model.Admission;
import org.isf.opd.model.Opd;
import org.isf.operation.model.OperationRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author hp
 */
public interface OperationRowIoOperationRepository extends JpaRepository<OperationRow, String> {
    	@Query(value = "SELECT * FROM OPERATIONROW ORDER BY OPER_OPDATE DESC", nativeQuery= true)
        public ArrayList<OperationRow> getOperationRow();
        
        public ArrayList<OperationRow> findByAdmission(Admission adm);
        
        public OperationRow findById(int id);
        
        public ArrayList<OperationRow> findByOpd(Opd opd);
}
