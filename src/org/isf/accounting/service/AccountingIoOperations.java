package org.isf.accounting.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.accounting.model.Bill;
import org.isf.accounting.model.BillItems;
import org.isf.accounting.model.BillPayments;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Persistence class for Accounting module.
 */
@Component
@Transactional
public class AccountingIoOperations {	
	@Autowired
	private AccountingBillIoOperationRepository billRepository;
	@Autowired
	private AccountingBillPaymentIoOperationRepository billPaymentRepository;
	@Autowired
	private AccountingBillItemsIoOperationRepository billItemsRepository;

	
	/**
	 * Returns all the pending {@link Bill}s for the specified patient.
	 * @param patID the patient id.
	 * @return the list of pending bills.
	 * @throws OHException if an error occurs retrieving the pending bills.
	 */
	public ArrayList<Bill> getPendingBills(int patID) throws OHException {
		if (patID != 0)
			return new ArrayList<Bill>(billRepository.findByStatusAndPatient_codeOrderByDateDesc("O", patID));

		return new ArrayList<Bill>(billRepository.findByStatusOrderByDateDesc("O"));
	}
	
	/**
	 * Get all the {@link Bill}s.
	 * @return a list of bills.
	 * @throws OHException if an error occurs retrieving the bills.
	 */
	public ArrayList<Bill> getBills() throws OHException {
		return new ArrayList<Bill>(billRepository.findAllByOrderByDateDesc());
	}
	
	/**
	 * Get the {@link Bill} with specified billID.
	 * @param billID
	 * @return the {@link Bill}.
	 * @throws OHException if an error occurs retrieving the bill.
	 */
	public Bill getBill(int billID) throws OHException {
		return billRepository.findOne(billID);
	}

	/**
	 * Returns all user ids from {@link BillPayments}.
	 * @return a list of user id.
	 * @throws OHException if an error occurs retrieving the users list.
	 */
    @SuppressWarnings("unchecked")
    public ArrayList<String> getUsers() throws OHException {

		return new ArrayList<String>(billPaymentRepository.findUserDistinctByOrderByUserAsc());
	}

	/**
	 * Returns the {@link BillItems} associated to the specified {@link Bill} id or all 
	 * the stored {@link BillItems} if no id is provided. 
	 * @param billID the bill id or <code>0</code>.
	 * @return a list of {@link BillItems} associated to the bill id or all the stored bill items.
	 * @throws OHException if an error occurs retrieving the bill items.
	 */
	public ArrayList<BillItems> getItems(int billID) throws OHException {
		ArrayList<BillItems> billItems = null;
		
		
		if (billID != 0)
		{
			billItems = new ArrayList<BillItems>(billItemsRepository.findByBill_idOrderByIdAsc(billID));			
		}
		else
		{
			billItems = new ArrayList<BillItems>(billItemsRepository.findAllByOrderByIdAsc()); 
		}

		return billItems;
	}

	/**
	 * Retrieves all the {@link BillPayments} for the specified date range.
	 * @param dateFrom low endpoint, inclusive, for the date range. 
	 * @param dateTo high endpoint, inclusive, for the date range.
	 * @return a list of {@link BillPayments} for the specified date range.
	 * @throws OHException if an error occurs retrieving the bill payments.
	 */
	public ArrayList<BillPayments> getPayments(
		GregorianCalendar dateFrom, 
		GregorianCalendar dateTo) throws OHException {

		return new ArrayList<BillPayments>(
			billPaymentRepository.findByDateBetweenOrderByIdAscDateAsc(dateFrom.getTime(), dateTo.getTime()));
	}

	/**
	 * Retrieves all the {@link BillPayments} for the specified {@link Bill} id, or all 
	 * the stored {@link BillPayments} if no id is indicated.
	 * @param billID the bill id or <code>0</code>.
	 * @return the list of bill payments.
	 * @throws OHException if an error occurs retrieving the bill payments.
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<BillPayments> getPayments(
			int billID) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<BillPayments> payments = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM BILLPAYMENTS";
		if (billID != 0) 
		{
			query = query + " WHERE BLP_ID_BILL = ?";
		}
		query = query + " ORDER BY BLP_ID_BILL, BLP_DATE ASC";		
		jpa.createQuery(query, BillPayments.class, false);
		if (billID != 0) 
		{
			params.add(billID);
			jpa.setParameters(params, false);
		}
		List<BillPayments> billPaymentList = (List<BillPayments>)jpa.getList();
		payments = new ArrayList<BillPayments>(billPaymentList);			
		
		jpa.commitTransaction();

		return payments;
	}

	/**
	 * Converts the specified {@link Timestamp} to a {@link GregorianCalendar} instance.
	 * @param aDate the date to convert.
	 * @return the corresponding GregorianCalendar value or <code>null</code> if the input value is <code>null</code>.
	 */
	public GregorianCalendar convertToGregorianCalendar(
			Timestamp aDate) 
	{
		GregorianCalendar time = null;
		
		
		if (aDate != null)
		{
			time = new GregorianCalendar();
			time.setTime(aDate);
		}
		
		return time;
	}

	/**
	 * Stores a new {@link Bill}.
	 * @param newBill the bill to store.
	 * @return the generated {@link Bill} id.
	 * @throws OHException if an error occurs storing the bill.
	 */
	public int newBill(Bill newBill) throws OHException {

		return billRepository.save(newBill).getId();
	}

	/**
	 * Stores a list of {@link BillItems} associated to a {@link Bill}.
	 * @param billID the bill id.
	 * @param billItems the bill items to store.
	 * @return <code>true</code> if the {@link BillItems} have been store, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the store operation.
	 */
	public boolean newBillItems(
			Bill bill,
			ArrayList<BillItems> billItems) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		boolean result = true;
		
		
		result = _deleteBillsInsideBillItems(jpa, bill.getId());
		
		result &= _insertNewBillInsideBillItems(jpa, bill, billItems);

		return result;
	}
	
	private boolean _deleteBillsInsideBillItems(
			DbJpaUtil jpa,
			int id) throws OHException 
    {	
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true;
        		
		
		jpa.beginTransaction();		

		try {
			jpa.createQuery("DELETE FROM BILLITEMS WHERE BLI_ID_BILL = ?", BillItems.class, false);
			params.add(id);
			jpa.setParameters(params, false);
			jpa.executeUpdate();
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 	

		jpa.commitTransaction();	
		
        return result;
    }
	
	private boolean _insertNewBillInsideBillItems(
			DbJpaUtil jpa,
			Bill bill,
			ArrayList<BillItems> billItems) throws OHException 
    {	
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true;
        		
		
		for (BillItems item : billItems) 
		{
			jpa.beginTransaction();		
	
			try {
				String query = "INSERT INTO BILLITEMS (" +
								"BLI_ID_BILL, BLI_IS_PRICE, BLI_ID_PRICE, BLI_ITEM_DESC, BLI_ITEM_AMOUNT, BLI_QTY) "+
								"VALUES (?,?,?,?,?,?)";
				jpa.createQuery(query, BillItems.class, false);
				params = _addUpdateBillItemParameters(bill, item);
				jpa.setParameters(params, false);
				jpa.executeUpdate();
			}  catch (OHException e) {
				result = false;
				throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
			} 	
	
			jpa.commitTransaction();
		}		
		
        return result;
    }
	
	private ArrayList<Object> _addUpdateBillItemParameters(
			Bill bill, 
			BillItems item) throws OHException 
    {	
		ArrayList<Object> params = new ArrayList<Object>();
		

		params.add(bill.getId());
		params.add(item.isPrice());
		params.add(item.getPriceID());
		params.add(item.getItemDescription());
		params.add(item.getItemAmount());
		params.add(item.getItemQuantity());
        		
        return params;
    }
	
	/**
	 * Stores a list of {@link BillPayments} associated to a {@link Bill}.
	 * @param billID the bill id.
	 * @param payItems the bill payments.
	 * @return <code>true</code> if the payment have stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the store procedure.
	 */
	public boolean newBillPayments(
			Bill bill, 
			ArrayList<BillPayments> payItems) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		boolean result = true;
		
		
		result = _deleteBillsInsideBillPayments(jpa, bill.getId());
		
		result &= _insertNewBillInsideBillPayments(jpa, bill, payItems);

		return result;
	}
	
	private boolean _deleteBillsInsideBillPayments(
			DbJpaUtil jpa,
			int id) throws OHException 
    {	
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true;
        		
		
		jpa.beginTransaction();		

		try {
			jpa.createQuery("DELETE FROM BILLPAYMENTS WHERE BLP_ID_BILL = ?", BillPayments.class, false);
			params.add(id);
			jpa.setParameters(params, false);
			jpa.executeUpdate();
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 	

		jpa.commitTransaction();	
		
        return result;
    }
	
	private boolean _insertNewBillInsideBillPayments(
			DbJpaUtil jpa,
			Bill bill,
			ArrayList<BillPayments> billPayments) throws OHException 
    {	
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true;
        		
		
		for (BillPayments payment : billPayments) 
		{
			jpa.beginTransaction();		
	
			try {
				String query = "INSERT INTO BILLPAYMENTS (" +
						"BLP_ID_BILL, BLP_DATE, BLP_AMOUNT, BLP_USR_ID_A) " +
						"VALUES (?,?,?,?)";
				jpa.createQuery(query, BillPayments.class, false);
				params = _addUpdateBillPaymentParameters(bill, payment);
				jpa.setParameters(params, false);
				jpa.executeUpdate();
			}  catch (OHException e) {
				result = false;
				throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
			} 	
	
			jpa.commitTransaction();
		}		
		
        return result;
    }
	
	private ArrayList<Object> _addUpdateBillPaymentParameters(
			Bill bill, 
			BillPayments payment) throws OHException 
    {	
		ArrayList<Object> params = new ArrayList<Object>();
		

		params.add(bill.getId());
		params.add(payment.getDate());
		params.add(payment.getAmount());
		params.add(payment.getUser());
        		
        return params;
    }
	
	/**
	 * Updates the specified {@link Bill}.
	 * @param updateBill the bill to update.
	 * @return <code>true</code> if the bill has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateBill(
			Bill updateBill) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true;
        		
		
		jpa.beginTransaction();		

		try {
			String query = "UPDATE BILLS SET " +
							"BLL_DATE = ?, " +
							"BLL_UPDATE = ?, " +
							"BLL_IS_LST = ?, " +
							"BLL_ID_LST = ?, " +
							"BLL_LST_NAME = ?, " +
							"BLL_IS_PAT = ?, " +
							"BLL_ID_PAT = ?, " +
							"BLL_PAT_NAME = ?, " +
							"BLL_STATUS = ?, " +
							"BLL_AMOUNT = ?, " +
							"BLL_BALANCE = ?, " +
							"BLL_USR_ID_A = ? " +
							"WHERE BLL_ID = ?";
			jpa.createQuery(query, Bill.class, false);
			params = _addUpdateBillParameters(updateBill);
			jpa.setParameters(params, false);
			jpa.executeUpdate();
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 	

		jpa.commitTransaction();
		
		return result;
	}
	
	private ArrayList<Object> _addUpdateBillParameters(
			Bill bill) throws OHException 
    {	
		ArrayList<Object> params = new ArrayList<Object>();
		
			
		params.add(bill.getDate());
		params.add(bill.getUpdate());
		params.add(bill.isList());
		params.add(bill.getList().getId());
		params.add(bill.getListName());
		params.add(bill.isPatient());			
		params.add(bill.getPatient().getCode());
		params.add(bill.getPatName());
		params.add(bill.getStatus());
		params.add(bill.getAmount());
		params.add(bill.getBalance());
		params.add(bill.getUser());
		params.add(bill.getId());
        		
        return params;
    }

	/**
	 * Deletes the specified {@link Bill}.
	 * @param deleteBill the bill to delete.
	 * @return <code>true</code> if the bill has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs deleting the bill.
	 */
	public boolean deleteBill(
			Bill deleteBill) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true;
        		
		
		jpa.beginTransaction();		

		try {
			String query = "UPDATE BILLS SET BLL_STATUS = 'D' WHERE BLL_ID = ?";
			jpa.createQuery(query, Bill.class, false);
			params.add(deleteBill.getId());
			jpa.setParameters(params, false);
			jpa.executeUpdate();
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 	

		jpa.commitTransaction();
		
		return result;
	}

	/**
	 * Retrieves all the {@link Bill}s for the specified date range.
	 * @param dateFrom the low date range endpoint, inclusive. 
	 * @param dateTo the high date range endpoint, inclusive.
	 * @return a list of retrieved {@link Bill}s.
	 * @throws OHException if an error occurs retrieving the bill list.
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<Bill> getBills(
			GregorianCalendar dateFrom, 
			GregorianCalendar dateTo) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Bill> bills = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM BILLS WHERE DATE(BLL_DATE) BETWEEN ? AND ?";	
		jpa.createQuery(query, Bill.class, false);
		params.add(new Timestamp(dateFrom.getTime().getTime()));
		params.add(new Timestamp(dateTo.getTime().getTime()));
		jpa.setParameters(params, false);
		List<Bill> billList = (List<Bill>)jpa.getList();
		bills = new ArrayList<Bill>(billList);			
		
		jpa.commitTransaction();

		return bills;
	}

	/**
	 * Gets all the {@link Bill}s associated to the passed {@link BillPayments}.
	 * @param payments the {@link BillPayments} associated to the bill to retrieve.
	 * @return a list of {@link Bill} associated to the passed {@link BillPayments}.
	 * @throws OHException if an error occurs retrieving the bill list.
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<Bill> getBills(
			ArrayList<BillPayments> payments) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Bill> bills = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM BILLS WHERE BLL_ID IN ( ";	
		for (int i = 0; i < payments.size(); i++) 
		{
			BillPayments payment = payments.get(i);
			if (i == payments.size() - 1) 
			{
				query = query + "?";
			} 
			else 
			{
				query = query + "?, ";
			}
			params.add(payment.getBill().getId());
		}
		query = query + ")";
		jpa.createQuery(query, Bill.class, false);
		jpa.setParameters(params, false);
		List<Bill> billList = (List<Bill>)jpa.getList();
		bills = new ArrayList<Bill>(billList);			

		jpa.commitTransaction();
		
		return bills;
	}

	/**
	 * Retrieves all the {@link BillPayments} associated to the passed {@link Bill} list.
	 * @param bills the bill list.
	 * @return a list of {@link BillPayments} associated to the passed bill list.
	 * @throws OHException if an error occurs retrieving the payments.
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<BillPayments> getPayments(
			ArrayList<Bill> bills) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<BillPayments> payments = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM BILLPAYMENTS WHERE BLP_ID_BILL IN (''";	
		if (bills!=null) {
			for (Bill bill:bills) 
			{
				query = query + ", ?";
				params.add(bill.getId());
			}
		}
		query = query + ")";
		jpa.createQuery(query, BillPayments.class, false);
		jpa.setParameters(params, false);
		List<BillPayments> paymentList = (List<BillPayments>)jpa.getList();
		payments = new ArrayList<BillPayments>(paymentList);			

		jpa.commitTransaction();
		
		return payments;
	}
}
