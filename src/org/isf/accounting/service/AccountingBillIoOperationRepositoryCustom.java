package org.isf.accounting.service;


import java.util.ArrayList;
import java.util.List;

import org.isf.accounting.model.BillPayments;


public interface AccountingBillIoOperationRepositoryCustom {

	List<Integer> findAllByPayments(ArrayList<BillPayments> payments);
	
}
