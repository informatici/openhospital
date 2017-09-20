package org.isf.vactype.repository;

import java.util.List;

import org.isf.vactype.model.VaccineType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VaccineTypeIoOperationRepository extends JpaRepository<VaccineType, String> {
    public List<VaccineType> findAllByOrderByDescriptionAsc();
}