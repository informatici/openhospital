package org.isf.admission.service;

import java.util.List;

import org.isf.admission.model.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AdmissionIoOperationRepository extends JpaRepository<Admission, Integer> {    
    @Query(value = "SELECT * FROM ADMISSION WHERE ADM_IN = 1 AND ADM_WRD_ID_A = :ward", nativeQuery= true)
    public List<Admission> findAllWhereWard(@Param("ward") String ward);
}