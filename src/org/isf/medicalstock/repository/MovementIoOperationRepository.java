package org.isf.medicalstock.repository;

import java.util.List;

import org.isf.medicalstock.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MovementIoOperationRepository extends JpaRepository<Movement, Integer> {    
    @Query(value = "select * from MEDICALDSRSTOCKMOV where MMV_MDSR_ID = :code", nativeQuery= true)
    public Movement findOneStockWhereCode(@Param("code") Integer code);
    
    @Query(value = "select distinct MDSR_ID from " +
		"((MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) " +
		"join MEDICALDSR  on MMV_MDSR_ID=MDSR_ID ) " +
		"join MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A where LT_ID_A=:lot", nativeQuery= true)
    public List<Integer> findAllByLot(@Param("lot") String lot);
    
}
