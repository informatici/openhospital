package org.isf.supplier.service;

import java.util.List;

import org.isf.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierIoOperationRepository extends JpaRepository<Supplier, Integer> {
    
    @Query(value = "SELECT * FROM SUPPLIER WHERE SUP_DELETED = 'N'", nativeQuery= true)
    List<Supplier> findAllWhereNotDeleted();
}