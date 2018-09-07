package org.isf.accounting.service;

import java.sql.Timestamp;
import java.util.List;

import org.isf.accounting.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AccountingBillIoOperationRepository
		extends JpaRepository<Bill, Integer>, AccountingBillIoOperationRepositoryCustom {

	List<Bill> findByStatusOrderByDateDesc(String status);

	List<Bill> findByStatusAndPatient_codeOrderByDateDesc(String status, int patientId);

	List<Bill> findAllByOrderByDateDesc();

	@Modifying
	@Query(value = "UPDATE BILLS SET BLL_STATUS = 'D' WHERE BLL_ID = :billId", nativeQuery = true)
	void updateDeleteWhereId(@Param("billId") Integer billId);

	@Query(value = "SELECT * FROM BILLS WHERE DATE(BLL_DATE) BETWEEN :dateFrom AND :dateTo", nativeQuery = true)
	List<Bill> findAllWhereDates(@Param("dateFrom") Timestamp dateFrom, @Param("dateTo") Timestamp dateTo);
}