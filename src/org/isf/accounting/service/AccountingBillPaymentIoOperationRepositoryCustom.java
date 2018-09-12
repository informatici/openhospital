package org.isf.accounting.service;


import java.util.ArrayList;
import java.util.List;

import org.isf.accounting.model.Bill;


public interface AccountingBillPaymentIoOperationRepositoryCustom {

	List<Integer> findAllByBills(ArrayList<Bill> bills);
	
}
