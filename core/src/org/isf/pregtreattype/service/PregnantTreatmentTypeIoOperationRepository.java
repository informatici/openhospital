package org.isf.pregtreattype.service;

import java.util.List;

import org.isf.pregtreattype.model.PregnantTreatmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PregnantTreatmentTypeIoOperationRepository extends JpaRepository<PregnantTreatmentType, String> {
    List<PregnantTreatmentType> findAllByOrderByDescriptionAsc();
}