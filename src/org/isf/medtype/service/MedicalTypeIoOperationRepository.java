package org.isf.medtype.service;

import java.util.List;

import org.isf.medtype.model.MedicalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalTypeIoOperationRepository extends JpaRepository<MedicalType, String> {
    List<MedicalType> findAllByOrderByDescriptionAsc();
}