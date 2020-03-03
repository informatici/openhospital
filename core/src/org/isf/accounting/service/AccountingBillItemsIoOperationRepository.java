package org.isf.accounting.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.accounting.model.BillItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AccountingBillItemsIoOperationRepository extends JpaRepository<BillItems, Integer> {
	
	List<BillItems> findByBill_idOrderByIdAsc(int billId);
		
	List<BillItems> findAllByOrderByIdAsc();
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM BILLITEMS WHERE BLI_ID_BILL = :billId", nativeQuery= true)
	void deleteWhereId(@Param("billId") Integer billId);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO BILLITEMS (" +
			"BLI_ID_BILL, BLI_IS_PRICE, BLI_ID_PRICE, BLI_ITEM_DESC, BLI_ITEM_AMOUNT, BLI_QTY) "+
			"VALUES (:id,:isPrice,:price,:description,:amount,:qty)", nativeQuery= true)
	void insertBillItem(
			@Param("id") Integer id, @Param("isPrice") Boolean isPrice, @Param("price") String price,
			@Param("description") String description, @Param("amount") Double amount, @Param("qty") Integer qty);
	
	@Query(value = "SELECT * FROM BILLITEMS GROUP BY BLI_ITEM_DESC", nativeQuery = true)
	ArrayList<BillItems> findAllGroupByDesc();
	
}