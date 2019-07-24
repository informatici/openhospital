package org.isf.medicalstockward.service;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import org.isf.medicalstockward.model.MovementWard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MovementWardIoOperationRepository extends JpaRepository<MovementWard, Integer>{      
    @Query(value = "SELECT * FROM MEDICALDSRSTOCKMOVWARD WHERE MMVN_WRD_ID_A_TO = :idwardto AND (MMVN_DATE BETWEEN :datefrom AND :dateto)", nativeQuery= true)
    public ArrayList<MovementWard> findWardMovements(@Param("idwardto") String idWardTo, 
            @Param("datefrom") GregorianCalendar dateFrom, 
            @Param("dateto") GregorianCalendar dateTo);
}
