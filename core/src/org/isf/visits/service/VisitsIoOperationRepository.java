package org.isf.visits.service;

import java.util.List;

import org.isf.visits.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitsIoOperationRepository extends JpaRepository<Visit, Integer> {

    @Query(value = "SELECT * FROM VISITS ORDER BY VST_PAT_ID, VST_DATE", nativeQuery= true)
    List<Visit> findAllByOrderPatientAndDateAsc();
    
    @Query(value = "SELECT * FROM VISITS WHERE VST_PAT_ID = :patient ORDER BY VST_PAT_ID, VST_DATE", nativeQuery= true)
    List<Visit> findAllWherePatientByOrderPatientAndDateAsc(@Param("patient") Integer patient);
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM VISITS WHERE VST_PAT_ID = :patient", nativeQuery= true)
    void deleteWherePatient(@Param("patient") Integer patient);
}