package org.isf.medstockmovtype.service;

import java.util.List;

import org.isf.medstockmovtype.model.MovementType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MedicalStockMovementTypeIoOperationRepository extends JpaRepository<MovementType, String> {
    public List<MovementType> findAllByOrderByDescriptionAsc();    
    public List<MovementType> findAllByCode(String code);
}