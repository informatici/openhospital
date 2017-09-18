package org.isf.accounting.service;

import java.util.List;

import org.isf.accounting.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountingBillIoOperationRepository extends JpaRepository<Bill, Integer> {
	
	List<Bill> findByStatusOrderByDateDesc(String status);
	
	List<Bill> findByStatusAndPatient_codeOrderByDateDesc(String status, int patientId);
	
	List<Bill> findAllByOrderByDateDesc();
	
}
