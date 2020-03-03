package org.isf.distype.service;

import java.util.List;

import org.isf.distype.model.DiseaseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseTypeIoOperationRepository extends JpaRepository<DiseaseType, String> {
    List<DiseaseType> findAllByOrderByDescriptionAsc();
}