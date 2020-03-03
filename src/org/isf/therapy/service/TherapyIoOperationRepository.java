package org.isf.therapy.service;

import java.util.List;

import org.isf.therapy.model.TherapyRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TherapyIoOperationRepository extends JpaRepository<TherapyRow, Integer> {

    @Query(value = "SELECT * FROM THERAPIES JOIN (MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A) ON THR_MDSR_ID = MDSR_ID ORDER BY THR_PAT_ID, THR_ID", nativeQuery= true)
    List<TherapyRow> findAllByOrderPatientAndIdAsc();
    
    @Query(value = "SELECT * FROM THERAPIES JOIN (MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A) ON THR_MDSR_ID = MDSR_ID WHERE THR_PAT_ID = :patient ORDER BY THR_PAT_ID, THR_ID", nativeQuery= true)
    List<TherapyRow> findAllWherePatientByOrderPatientAndIdAsc(@Param("patient") Integer patient);
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM THERAPIES WHERE THR_PAT_ID = :patient", nativeQuery= true)
    void deleteWherePatient(@Param("patient") Integer patient);
}