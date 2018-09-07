package org.isf.medicalstockward.service;

import java.util.List;

import org.isf.medicalstockward.model.MedicalWard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface MedicalStockWardIoOperationRepository extends JpaRepository<MedicalWard, String>, MedicalStockWardIoOperationRepositoryCustom {      
	   
    @Query(value = "SELECT * FROM MEDICALDSRWARD WHERE MDSRWRD_WRD_ID_A = :ward AND MDSRWRD_MDSR_ID = :medical", nativeQuery= true)
    public MedicalWard findOneWhereCodeAndMedical(@Param("ward") String ward, @Param("medical") int medical);    
    
    @Query(value = "SELECT SUM(MMV_QTY) MAIN FROM MEDICALDSRSTOCKMOV M WHERE MMV_MMVT_ID_A = 'testDisc' AND MMV_MDSR_ID = :medical", nativeQuery= true)
    public Double findMainQuantityWhereMedical(@Param("medical") int medical);
    @Query(value = "SELECT SUM(MMV_QTY) MAIN FROM MEDICALDSRSTOCKMOV M WHERE MMV_MMVT_ID_A = 'testDisc' AND MMV_MDSR_ID = :medical AND MMV_WRD_ID_A = :ward", nativeQuery= true)
    public Double findMainQuantityWhereMedicalAndWard(@Param("medical") int medical, @Param("ward") String ward);
	
    @Query(value = "SELECT SUM(MMVN_MDSR_QTY) DISCHARGE FROM MEDICALDSRSTOCKMOVWARD WHERE MMVN_MDSR_ID = :medical", nativeQuery= true)
    public Double findDischargeQuantityWhereMedical(@Param("medical") int medical);
    @Query(value = "SELECT SUM(MMVN_MDSR_QTY) DISCHARGE FROM MEDICALDSRSTOCKMOVWARD WHERE MMVN_MDSR_ID = :medical AND MMVN_WRD_ID_A = :ward", nativeQuery= true)
    public Double findDischargeQuantityWhereMedicalAndWard(@Param("medical") int medical, @Param("ward") String ward);
        
    @Modifying 
    @Transactional
    @Query(value = "UPDATE MEDICALDSRWARD SET MDSRWRD_IN_QTI = MDSRWRD_IN_QTI + :quantity WHERE MDSRWRD_WRD_ID_A = :ward AND MDSRWRD_MDSR_ID = :medical", nativeQuery= true)
    public void updateInQuantity(@Param("quantity") Double quantity, @Param("ward") String ward, @Param("medical") int medical);      
    @Modifying 
    @Transactional
    @Query(value = "UPDATE MEDICALDSRWARD SET MDSRWRD_OUT_QTI = MDSRWRD_OUT_QTI + :quantity WHERE MDSRWRD_WRD_ID_A = :ward AND MDSRWRD_MDSR_ID = :medical", nativeQuery= true)
    public void updateOutQuantity(@Param("quantity") Double quantity, @Param("ward") String ward, @Param("medical") int medical);   
    @Modifying 
    @Transactional
    @Query(value = "INSERT INTO MEDICALDSRWARD (MDSRWRD_WRD_ID_A, MDSRWRD_MDSR_ID, MDSRWRD_IN_QTI, MDSRWRD_OUT_QTI) " +
    		"VALUES (?, ?, ?, '0')", nativeQuery= true)
    public void insertMedicalWard(@Param("ward") String ward, @Param("medical") int medical, @Param("quantity") Double quantity); 
        
    @Query(value = "SELECT mw FROM MedicalWard mw WHERE mw.id.ward_id=:ward")
    public List<MedicalWard> findAllWhereWard(@Param("ward") char wardId); 

}
