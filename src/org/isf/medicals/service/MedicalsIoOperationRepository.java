package org.isf.medicals.service;

import java.util.List;

import org.isf.medicals.model.Medical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MedicalsIoOperationRepository extends JpaRepository<Medical, Integer> {
    @Query(value = "SELECT * FROM MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A order BY MDSR_DESC", nativeQuery= true)
    public List<Medical> findAllOrderByDescription();    
    @Query(value = "SELECT * FROM MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A where MDSRT_DESC like :description order BY MDSR_DESC", nativeQuery= true)
    public List<Medical> findAllWhereDescriptionOrderByDescription(@Param("description") String description );    
    
    @Query(value = "SELECT * FROM MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A where (MDSR_DESC like %:description% OR MDSR_CODE like %:description%) and (MDSRT_ID_A=:type) and ((MDSR_INI_STOCK_QTI+MDSR_IN_QTI-MDSR_OUT_QTI)<MDSR_MIN_STOCK_QTI) order BY MDSR_MDSRT_ID_A, MDSR_DESC", nativeQuery= true)
    public List<Medical> findAllWhereDescriptionAndTypeAndExpiringOrderByTypeAndDescritpion(@Param("description") String description, @Param("type") String type);  
    @Query(value = "SELECT * FROM MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A where (MDSR_DESC like %:description% OR MDSR_CODE like %:description%) and (MDSRT_ID_A=:type) order BY MDSR_MDSRT_ID_A, MDSR_DESC", nativeQuery= true)   
    public List<Medical> findAllWhereDescriptionAndTypeOrderByTypeAndDescritpion(@Param("description")String description, @Param("type") String type);  
    @Query(value = "SELECT * FROM MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A where (MDSR_DESC like %:description% OR MDSR_CODE like %:description%) and ((MDSR_INI_STOCK_QTI+MDSR_IN_QTI-MDSR_OUT_QTI)<MDSR_MIN_STOCK_QTI) order BY MDSR_MDSRT_ID_A, MDSR_DESC", nativeQuery= true)
    public List<Medical> findAllWhereDescriptionAndExpiringOrderByTypeAndDescritpion(@Param("description")String description);  
    @Query(value = "SELECT * FROM MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A where (MDSR_DESC like %:description% OR MDSR_CODE like %:description%) order BY MDSR_MDSRT_ID_A, MDSR_DESC", nativeQuery= true)
    public List<Medical> findAllWhereDescriptionOrderByTypeAndDescritpion(@Param("description")String description);      
    @Query(value = "SELECT * FROM MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A where (MDSRT_ID_A=:type) and ((MDSR_INI_STOCK_QTI+MDSR_IN_QTI-MDSR_OUT_QTI)<MDSR_MIN_STOCK_QTI) order BY MDSR_MDSRT_ID_A, MDSR_DESC", nativeQuery= true)
    public List<Medical> findAllWhereTypeAndExpiringOrderByTypeAndDescritpion(@Param("type") String type);  
    @Query(value = "SELECT * FROM MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A where (MDSRT_ID_A=:type) order BY MDSR_MDSRT_ID_A, MDSR_DESC", nativeQuery= true)   
    public List<Medical> findAllWhereTypeOrderByTypeAndDescritpion(@Param("type") String type);  
    @Query(value = "SELECT * FROM MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A where ((MDSR_INI_STOCK_QTI+MDSR_IN_QTI-MDSR_OUT_QTI)<MDSR_MIN_STOCK_QTI) order BY MDSR_MDSRT_ID_A, MDSR_DESC", nativeQuery= true)
    public List<Medical> findAllWhereExpiringOrderByTypeAndDescritpion();  
    @Query(value = "SELECT * FROM MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A order BY MDSR_MDSRT_ID_A, MDSR_DESC", nativeQuery= true)
    public List<Medical> findAllOrderByTypeAndDescritpion();  
    
    @Query(value = "SELECT * FROM MEDICALDSR WHERE MDSR_MDSRT_ID_A = :type AND MDSR_DESC = :description", nativeQuery= true)
    public Medical findOneWhereDescriptionAndType(@Param("description") String description, @Param("type") String type);   
}
