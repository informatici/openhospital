package org.isf.distype.service;

import java.util.List;

import org.isf.distype.model.DiseaseType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DiseaseTypeIoOperationRepository extends JpaRepository<DiseaseType, String> {
    public List<DiseaseType> findAllByOrderByDescriptionAsc();
}