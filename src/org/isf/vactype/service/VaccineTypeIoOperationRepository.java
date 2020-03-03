package org.isf.vactype.service;

import java.util.List;

import org.isf.vactype.model.VaccineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineTypeIoOperationRepository extends JpaRepository<VaccineType, String> {
    List<VaccineType> findAllByOrderByDescriptionAsc();
}