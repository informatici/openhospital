package org.isf.medicalstockward.service;

import java.util.List;

import org.isf.medicalstock.model.Lot;
import org.isf.medicalstockward.model.MedicalWard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MedicalStockWardIoOperationRepository extends JpaRepository<MedicalWard, String> {      
	   
    @Query(value = "SELECT * FROM MEDICALDSRWARD WHERE MDSRWRD_WRD_ID_A = :ward AND MDSRWRD_MDSR_ID = :medical", nativeQuery= true)
    public MedicalWard findAllWhereIds(@Param("ward") String ward, @Param("medical") int medical);
}
