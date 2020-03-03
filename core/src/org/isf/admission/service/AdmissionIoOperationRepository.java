package org.isf.admission.service;

import java.util.GregorianCalendar;
import java.util.List;

import org.isf.admission.model.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionIoOperationRepository extends JpaRepository<Admission, Integer>, AdmissionIoOperationRepositoryCustom {    
    @Query(value = "SELECT * FROM ADMISSION WHERE ADM_IN = 1 AND ADM_WRD_ID_A = :ward", nativeQuery= true)
    List<Admission> findAllWhereWard(@Param("ward") String ward);
    
    @Query(value = "SELECT * FROM ADMISSION WHERE ADM_PAT_ID=:patient AND ADM_DELETED='N' AND ADM_IN = 1", nativeQuery= true)
    Admission findOneWherePatientIn(@Param("patient") Integer patient);

    @Query(value = "SELECT * FROM ADMISSION WHERE ADM_PAT_ID=:patient and ADM_DELETED='N' ORDER BY ADM_DATE_ADM ASC", nativeQuery= true)
    List<Admission> findAllWherePatientByOrderByDate(@Param("patient") Integer patient);
        
    @Query(value = "SELECT * FROM ADMISSION " +
    		"WHERE ADM_WRD_ID_A=:ward AND ADM_DATE_ADM >= :dateFrom AND ADM_DATE_ADM <= :dateTo AND ADM_DELETED='N' " +
    		"ORDER BY ADM_YPROG DESC", nativeQuery= true)
    List<Admission> findAllWhereWardAndDates(
            @Param("ward") String ward, @Param("dateFrom") GregorianCalendar dateFrom,
            @Param("dateTo") GregorianCalendar dateTo);
    
    @Query(value = "SELECT * FROM ADMISSION WHERE ADM_IN = 1 AND ADM_WRD_ID_A = :ward AND ADM_DELETED = 'N'", nativeQuery= true)
    List<Admission> findAllWhereWardIn(@Param("ward") String ward);
    
}