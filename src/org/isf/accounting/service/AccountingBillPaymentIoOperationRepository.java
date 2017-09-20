package org.isf.accounting.service;

import java.util.Date;
import java.util.List;

import org.isf.accounting.model.BillPayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountingBillPaymentIoOperationRepository extends JpaRepository<BillPayments, Integer> {

	List<String> findUserDistinctByOrderByUserAsc();

	@Query("SELECT BP FROM BillPayments BP WHERE DATE(BLP_DATE) BETWEEN ?1 AND ?2"
				+ " ORDER BY BLP_ID_BILL, BLP_DATE ASC")
	List<BillPayments> findByDateBetweenOrderByIdAscDateAsc(Date start, Date end);
}