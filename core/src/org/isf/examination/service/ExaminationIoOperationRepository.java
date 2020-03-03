package org.isf.examination.service;

import java.util.List;

import org.isf.examination.model.PatientExamination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExaminationIoOperationRepository extends JpaRepository<PatientExamination, Integer> {
    @Query(value = "SELECT * FROM PATIENTEXAMINATION WHERE PEX_PAT_ID = :id ORDER BY PEX_DATE DESC", nativeQuery= true)
    List<PatientExamination> findAllByIdOrderDesc(@Param("id") int id);
    @Query(value = "SELECT * FROM PATIENTEXAMINATION WHERE PEX_PAT_ID = :id ORDER BY PEX_DATE DESC LIMIT :limit", nativeQuery= true)
    List<PatientExamination> findAllByIdOrderDescLimited(@Param("id") int id, @Param("limit") int limit);
}