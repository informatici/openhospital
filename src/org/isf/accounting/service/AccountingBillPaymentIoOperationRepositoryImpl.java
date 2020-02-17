package org.isf.accounting.service;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.isf.accounting.model.Bill;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class AccountingBillPaymentIoOperationRepositoryImpl implements AccountingBillPaymentIoOperationRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;

	
	@SuppressWarnings("unchecked")	
	@Override
	public List<Integer> findAllByBills(ArrayList<Bill> bills) {
		return this.entityManager.
				createNativeQuery(_getQueryByPayments(bills)).
					getResultList();
	}	

	
	private String _getQueryByPayments(
			ArrayList<Bill> bills) 
	{
		String query = "SELECT BLP_ID FROM BILLPAYMENTS WHERE BLP_ID_BILL IN (''";	
		
		
		if (bills!=null) {
			for (Bill bill:bills) 
			{
				query = query + ", \"" + bill.getId() + "\"";
			}
		}
		query = query + ")";

		return query;
	}
}