package org.isf.medtype.repository;

import java.util.List;

import org.isf.medtype.model.MedicalType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MedicalTypeIoOperationRepository extends JpaRepository<MedicalType, String> {
    public List<MedicalType> findAllByOrderByDescriptionAsc();
}