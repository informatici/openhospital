package org.isf.accounting.service;

import java.util.List;

import org.isf.accounting.model.BillItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountingBillItemsIoOperationRepository extends JpaRepository<BillItems, Integer> {
	
	List<BillItems> findByBill_idOrderByIdAsc(int billId);
		
	List<BillItems> findAllOrderByIdAsc();
}