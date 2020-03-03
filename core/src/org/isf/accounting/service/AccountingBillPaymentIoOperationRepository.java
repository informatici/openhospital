package org.isf.accounting.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.accounting.model.BillPayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AccountingBillPaymentIoOperationRepository extends JpaRepository<BillPayments, Integer>, AccountingBillPaymentIoOperationRepositoryCustom {

	@Query(value = "SELECT DISTINCT BLP_USR_ID_A FROM BILLPAYMENTS ORDER BY BLP_USR_ID_A ASC", nativeQuery= true)
	List<String> findUserDistinctByOrderByUserAsc();

	@Query("SELECT BP FROM BillPayments BP WHERE DATE(BLP_DATE) BETWEEN ?1 AND ?2"
				+ " ORDER BY BLP_ID_BILL, BLP_DATE ASC")
	List<BillPayments> findByDateBetweenOrderByIdAscDateAsc(Date start, Date end);	

	@Query(value = "SELECT * FROM BILLPAYMENTS ORDER BY BLP_ID_BILL, BLP_DATE ASC", nativeQuery= true)
	List<BillPayments> findAllByOrderByBillAndDate();
	
	@Query(value = "SELECT * FROM BILLPAYMENTS WHERE BLP_ID_BILL = :billId ORDER BY BLP_ID_BILL, BLP_DATE ASC", nativeQuery= true)
	List<BillPayments> findAllWherBillIdByOrderByBillAndDate(@Param("billId") Integer billId);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM BILLPAYMENTS WHERE BLP_ID_BILL =  :billId", nativeQuery= true)
	void deleteWhereId(@Param("billId") Integer billId);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO BILLPAYMENTS (" +
			"BLP_ID_BILL, BLP_DATE, BLP_AMOUNT, BLP_USR_ID_A) " +
			"VALUES (:id,:date,:amount,:user)", nativeQuery= true)
	void insertBillPayment(
			@Param("id") Integer id, @Param("date") GregorianCalendar date,
			@Param("amount") Double amount, @Param("user") String user);
	
	@Query(value = "SELECT * FROM BILLPAYMENTS BLP INNER JOIN BILLS BLL ON BLP.BLP_ID_BILL= BLL.BLL_ID "
			+" WHERE DATE(BLP.BLP_DATE) BETWEEN :dateFrom AND :dateTo "
			+" AND (BLL.BLL_ID_PAT= :patientCode ) "
			+" ORDER BY BLP_ID_BILL, BLP_DATE ASC ", nativeQuery= true)
	ArrayList<BillPayments> findByDateAndPatient(@Param("dateFrom") GregorianCalendar dateFrom , @Param("dateTo") GregorianCalendar dateTo, @Param("patientCode") Integer patientCode);
}