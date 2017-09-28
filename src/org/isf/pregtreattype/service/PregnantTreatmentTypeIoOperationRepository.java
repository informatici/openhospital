package org.isf.pregtreattype.service;

import java.util.List;

import org.isf.pregtreattype.model.PregnantTreatmentType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PregnantTreatmentTypeIoOperationRepository extends JpaRepository<PregnantTreatmentType, String> {
    public List<PregnantTreatmentType> findAllOrderByDescriptionAsc();
}