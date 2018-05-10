package org.isf.medstockmovtype.service;

import java.sql.Timestamp;
import java.util.List;

import org.isf.medstockmovtype.model.MovementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MedicalStockMovementTypeIoOperationRepository extends JpaRepository<MovementType, String> {
    public List<MovementType> findAllByOrderByDescriptionAsc();    

}