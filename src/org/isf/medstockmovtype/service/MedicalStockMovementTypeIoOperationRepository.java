package org.isf.medstockmovtype.service;

import java.sql.Timestamp;
import java.util.List;

import org.isf.medstockmovtype.model.MovementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MedicalStockMovementTypeIoOperationRepository extends JpaRepository<MovementType, String> {
    public List<MovementType> findAllByOrderByDescriptionAsc();    

    @Query(value = "SELECT MAX(MMV_DATE) AS DATE FROM MEDICALDSRSTOCKMOV", nativeQuery= true)
    public Timestamp findMaxDate();    

    @Query(value = "SELECT MMV_REFNO FROM MEDICALDSRSTOCKMOV WHERE MMV_REFNO LIKE :refNo", nativeQuery= true)
    public List<String> findAllWhereRefNo(@Param("refNo") String refNo);
}