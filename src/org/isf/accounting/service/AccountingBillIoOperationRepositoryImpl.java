package org.isf.accounting.service;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.isf.accounting.model.BillPayments;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class AccountingBillIoOperationRepositoryImpl implements AccountingBillIoOperationRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;

	
	@SuppressWarnings("unchecked")	
	@Override
	public List<Integer> findAllByPayments(ArrayList<BillPayments> payments) {
		return this.entityManager.
				createNativeQuery(_getQueryByPayments(payments)).
					getResultList();
	}	

	
	private String _getQueryByPayments(
			ArrayList<BillPayments> payments) 
	{
		String query = "SELECT BLL_ID FROM BILLS WHERE BLL_ID IN ( ";	
		
		
		for (int i = 0; i < payments.size(); i++) 
		{
			BillPayments payment = payments.get(i);
			if (i == payments.size() - 1) 
			{
				query = query + "\"" + payment.getBill().getId() + "\"";
			} 
			else 
			{
				query = query + "\"" + payment.getBill().getId() + "\", ";
			}
		}
		query = query + ")";

		return query;
	}
}