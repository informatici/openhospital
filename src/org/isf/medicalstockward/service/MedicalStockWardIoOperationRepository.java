package org.isf.medicalstockward.service;

import java.util.List;

import org.isf.medicalstockward.model.MedicalWard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MedicalStockWardIoOperationRepository extends JpaRepository<MedicalWard, String>, MedicalStockWardIoOperationRepositoryCustom {      
	   
    @Query(value = "SELECT * FROM MEDICALDSRWARD WHERE MDSRWRD_WRD_ID_A = :ward AND MDSRWRD_MDSR_ID = :medical", nativeQuery= true)
    MedicalWard findOneWhereCodeAndMedical(@Param("ward") String ward, @Param("medical") int medical);
    
    @Query(value = "SELECT SUM(MDSRWRD_IN_QTI-MDSRWRD_OUT_QTI) QTY FROM MEDICALDSRWARD WHERE MDSRWRD_MDSR_ID = :medical", nativeQuery= true)
    Double findQuantityInWardWhereMedical(@Param("medical") int medical);
    @Query(value = "SELECT SUM(MDSRWRD_IN_QTI-MDSRWRD_OUT_QTI) QTY FROM MEDICALDSRWARD WHERE MDSRWRD_MDSR_ID = :medical AND MDSRWRD_WRD_ID_A = :ward", nativeQuery= true)
    Double findQuantityInWardWhereMedicalAndWard(@Param("medical") int medical, @Param("ward") String ward);
	
    @Modifying 
    @Transactional
    @Query(value = "UPDATE MEDICALDSRWARD SET MDSRWRD_IN_QTI = MDSRWRD_IN_QTI + :quantity WHERE MDSRWRD_WRD_ID_A = :ward AND MDSRWRD_MDSR_ID = :medical", nativeQuery= true)
    void updateInQuantity(@Param("quantity") Double quantity, @Param("ward") String ward, @Param("medical") int medical);
    @Modifying 
    @Transactional
    @Query(value = "UPDATE MEDICALDSRWARD SET MDSRWRD_OUT_QTI = MDSRWRD_OUT_QTI + :quantity WHERE MDSRWRD_WRD_ID_A = :ward AND MDSRWRD_MDSR_ID = :medical", nativeQuery= true)
    void updateOutQuantity(@Param("quantity") Double quantity, @Param("ward") String ward, @Param("medical") int medical);
    @Modifying 
    @Transactional
    @Query(value = "INSERT INTO MEDICALDSRWARD (MDSRWRD_WRD_ID_A, MDSRWRD_MDSR_ID, MDSRWRD_IN_QTI, MDSRWRD_OUT_QTI) " +
    		"VALUES (?, ?, ?, '0')", nativeQuery= true)
    void insertMedicalWard(@Param("ward") String ward, @Param("medical") int medical, @Param("quantity") Double quantity);
        
    @Query(value = "SELECT * FROM MEDICALDSRWARD WHERE MDSRWRD_WRD_ID_A = :ward", nativeQuery= true)
    List<MedicalWard> findAllWhereWard(@Param("ward") char wardId);

}
