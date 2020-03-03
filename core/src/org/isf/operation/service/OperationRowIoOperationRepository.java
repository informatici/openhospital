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
import org.springframework.stereotype.Repository;

/**
 *
 * @author hp
 */
@Repository
public interface OperationRowIoOperationRepository extends JpaRepository<OperationRow, String> {
    	@Query(value = "SELECT * FROM OPERATIONROW ORDER BY OPER_OPDATE DESC", nativeQuery= true)
        ArrayList<OperationRow> getOperationRow();
        
        ArrayList<OperationRow> findByAdmission(Admission adm);
        
        OperationRow findById(int id);
        
        ArrayList<OperationRow> findByOpd(Opd opd);
}
