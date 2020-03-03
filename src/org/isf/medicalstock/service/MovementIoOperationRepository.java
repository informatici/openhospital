package org.isf.medicalstock.service;

import java.sql.Timestamp;
import java.util.List;

import org.isf.medicalstock.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementIoOperationRepository extends JpaRepository<Movement, Integer>, MovementIoOperationRepositoryCustom {    
    @Query(value = "select * from MEDICALDSRSTOCKMOV where MMV_MDSR_ID = :code", nativeQuery= true)
    Movement findAllByMedicalCode(@Param("code") Integer code);
    
    @Query(value = "select distinct MDSR_ID from " +
		"((MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) " +
		"join MEDICALDSR  on MMV_MDSR_ID=MDSR_ID ) " +
		"join MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A where LT_ID_A=:lot", nativeQuery= true)
    List<Integer> findAllByLot(@Param("lot") String lot);
    
    @Query(value = "SELECT * FROM (" +
			"(MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) " +
			"JOIN (MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A=MDSRT_ID_A) on MMV_MDSR_ID=MDSR_ID ) " +
			"LEFT JOIN MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A " +
			"LEFT JOIN WARD ON MMV_WRD_ID_A = WRD_ID_A " +
			"WHERE MMV_REFNO = :refNo " +
			"ORDER BY MMV_DATE DESC, MMV_REFNO DESC", nativeQuery= true)
    List<Movement> findAllByRefNo(@Param("refNo") String refNo);
    
    @Query(value = "SELECT MAX(MMV_DATE) AS DATE FROM MEDICALDSRSTOCKMOV", nativeQuery= true)
    Timestamp findMaxDate();

    @Query(value = "SELECT MMV_REFNO FROM MEDICALDSRSTOCKMOV WHERE MMV_REFNO LIKE :refNo", nativeQuery= true)
    List<String> findAllWhereRefNo(@Param("refNo") String refNo);
}
