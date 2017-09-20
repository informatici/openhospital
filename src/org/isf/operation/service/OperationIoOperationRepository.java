package org.isf.operation.service;

import java.util.ArrayList;

import org.isf.operation.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface OperationIoOperationRepository extends JpaRepository<Operation, String> {
    @Query(value = "SELECT * FROM OPERATION JOIN OPERATIONTYPE ON OPE_OCL_ID_A = OCL_ID_A ORDER BY OPE_DESC", nativeQuery= true)
    public ArrayList<Operation> findAllWithoutDescription();
    @Query(value = "SELECT * FROM OPERATION JOIN OPERATIONTYPE ON OPE_OCL_ID_A = OCL_ID_A WHERE OCL_DESC LIKE CONCAT('%', :type , '%') ORDER BY OPE_DESC", nativeQuery= true)
    public ArrayList<Operation> findAllByDescription(@Param("type") String type);    
    @Query(value = "SELECT * FROM OPERATION WHERE OPE_DESC = :description AND OPE_OCL_ID_A = :type", nativeQuery= true)
    public Operation findOneByDescriptionAndType(@Param("description") String description, @Param("type") String type);
}