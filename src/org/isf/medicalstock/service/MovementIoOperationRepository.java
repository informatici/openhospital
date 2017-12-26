package org.isf.medicalstock.service;

import java.util.List;

import org.isf.medicalstock.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MovementIoOperationRepository extends JpaRepository<Movement, Integer>, MovementIoOperationRepositoryCustom {    
    @Query(value = "select * from MEDICALDSRSTOCKMOV where MMV_MDSR_ID = :code", nativeQuery= true)
    public Movement findOneStockWhereCode(@Param("code") Integer code);
    
    @Query(value = "select distinct MDSR_ID from " +
		"((MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) " +
		"join MEDICALDSR  on MMV_MDSR_ID=MDSR_ID ) " +
		"join MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A where LT_ID_A=:lot", nativeQuery= true)
    public List<Integer> findAllByLot(@Param("lot") String lot);    
    
    @Query(value = "SELECT * FROM (" +
			"(MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) " +
			"JOIN (MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A=MDSRT_ID_A) on MMV_MDSR_ID=MDSR_ID ) " +
			"LEFT JOIN MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A " +
			"LEFT JOIN WARD ON MMV_WRD_ID_A = WRD_ID_A " +
			"WHERE MMV_REFNO = :refNo " +
			"ORDER BY MMV_DATE DESC, MMV_REFNO DESC", nativeQuery= true)
    public List<Movement> findAllByRefNo(@Param("refNo") String refNo);    
}
