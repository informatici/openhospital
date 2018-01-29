package org.isf.patvac.service;

import org.isf.patvac.model.PatientVaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PatVacIoOperationRepository extends JpaRepository<PatientVaccine, Integer>, PatVacIoOperationRepositoryCustom {

    @Query(value = "SELECT MAX(PAV_YPROG) FROM PATIENTVACCINE", nativeQuery= true)
    public Integer findMaxCode();
    
    @Query(value = "SELECT MAX(PAV_YPROG) FROM PATIENTVACCINE WHERE YEAR(PAV_DATE) = :year", nativeQuery= true)
    public Integer findMaxCodeWhereVaccineDate(@Param("year") Integer year);   
}