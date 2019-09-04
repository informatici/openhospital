package org.isf.accounting.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.accounting.model.Bill;
import org.isf.accounting.model.BillItems;
import org.isf.accounting.model.BillPayments;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Persistence class for Accounting module.
 */
@Component
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
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
	 * @throws OHServiceException if an error occurs retrieving the pending bills.
	 */
	public ArrayList<Bill> getPendingBills(int patID) throws OHServiceException {
		if (patID != 0)
			return new ArrayList<Bill>(billRepository.findByStatusAndPatient_codeOrderByDateDesc("O", patID));

		return new ArrayList<Bill>(billRepository.findByStatusOrderByDateDesc("O"));
	}
	
	/**
	 * Get all the {@link Bill}s.
	 * @return a list of bills.
	 * @throws OHServiceException if an error occurs retrieving the bills.
	 */
	public ArrayList<Bill> getBills() throws OHServiceException {
		return new ArrayList<Bill>(billRepository.findAllByOrderByDateDesc());
	}
	
	/**
	 * Get the {@link Bill} with specified billID.
	 * @param billID
	 * @return the {@link Bill}.
	 * @throws OHServiceException if an error occurs retrieving the bill.
	 */
	public Bill getBill(int billID) throws OHServiceException {
		return billRepository.findOne(billID);
	}

	/**
	 * Returns all user ids from {@link BillPayments}.
	 * @return a list of user id.
	 * @throws OHServiceException if an error occurs retrieving the users list.
	 */
    public ArrayList<String> getUsers() throws OHServiceException {

		return new ArrayList<String>(billPaymentRepository.findUserDistinctByOrderByUserAsc());
	}

	/**
	 * Returns the {@link BillItems} associated to the specified {@link Bill} id or all 
	 * the stored {@link BillItems} if no id is provided. 
	 * @param billID the bill id or <code>0</code>.
	 * @return a list of {@link BillItems} associated to the bill id or all the stored bill items.
	 * @throws OHServiceException if an error occurs retrieving the bill items.
	 */
	public ArrayList<BillItems> getItems(int billID) throws OHServiceException {
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
	 * @throws OHServiceException if an error occurs retrieving the bill payments.
	 */
	public ArrayList<BillPayments> getPayments(
		GregorianCalendar dateFrom, 
		GregorianCalendar dateTo) throws OHServiceException {

		return new ArrayList<BillPayments>(
			billPaymentRepository.findByDateBetweenOrderByIdAscDateAsc(dateFrom.getTime(), dateTo.getTime()));
	}

	/**
	 * Retrieves all the {@link BillPayments} for the specified {@link Bill} id, or all 
	 * the stored {@link BillPayments} if no id is indicated.
	 * @param billID the bill id or <code>0</code>.
	 * @return the list of bill payments.
	 * @throws OHServiceException if an error occurs retrieving the bill payments.
	 */
	public ArrayList<BillPayments> getPayments(
			int billID) throws OHServiceException 
	{ 
		ArrayList<BillPayments> payments = null;
				
		if (billID != 0) 
		{
			payments = (ArrayList<BillPayments>) billPaymentRepository.findAllWherBillIdByOrderByBillAndDate(billID);
		}
		else
		{
			payments = (ArrayList<BillPayments>) billPaymentRepository.findAllByOrderByBillAndDate();
		}
		
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
	 * @throws OHServiceException if an error occurs storing the bill.
	 */
	public int newBill(Bill newBill) throws OHServiceException {

		return billRepository.save(newBill).getId();
	}

	/**
	 * Stores a list of {@link BillItems} associated to a {@link Bill}.
	 * @param billID the bill id.
	 * @param billItems the bill items to store.
	 * @return <code>true</code> if the {@link BillItems} have been store, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs during the store operation.
	 */
	public boolean newBillItems(
			Bill bill,
			ArrayList<BillItems> billItems) throws OHServiceException 
	{
		boolean result = true;
		
		
		result = _deleteBillsInsideBillItems(bill.getId());
		
		result &= _insertNewBillInsideBillItems(bill, billItems);

		return result;
	}
	
	private boolean _deleteBillsInsideBillItems(
			int id) throws OHServiceException 
    {	
		boolean result = true;
        		
		
		billItemsRepository.deleteWhereId(id);
		
        return result;
    }
	
	private boolean _insertNewBillInsideBillItems(
			Bill bill,
			ArrayList<BillItems> billItems) throws OHServiceException 
    {	
		boolean result = true;
        		
		
		for (BillItems item : billItems) 
		{
			billItemsRepository.insertBillItem(
				bill.getId(),
				item.isPrice(),
				item.getPriceID(),
				item.getItemDescription(),
				item.getItemAmount(),
				item.getItemQuantity());
		}
		
		return result;
    }
	
	/**
	 * Stores a list of {@link BillPayments} associated to a {@link Bill}.
	 * @param billID the bill id.
	 * @param payItems the bill payments.
	 * @return <code>true</code> if the payment have stored, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs during the store procedure.
	 */
	public boolean newBillPayments(
			Bill bill, 
			ArrayList<BillPayments> payItems) throws OHServiceException 
	{
		boolean result = true;
		
		
		result = _deleteBillsInsideBillPayments(bill.getId());
		
		result &= _insertNewBillInsideBillPayments(bill, payItems);

		return result;
	}
	
	private boolean _deleteBillsInsideBillPayments(
			int id) throws OHServiceException 
    {	
		boolean result = true;
        		
		
		billPaymentRepository.deleteWhereId(id);
		
        return result;
    }
	
	private boolean _insertNewBillInsideBillPayments(
			Bill bill,
			ArrayList<BillPayments> billPayments) throws OHServiceException 
    {	

		boolean result = true;
        		
		
		for (BillPayments payment : billPayments) 
		{
			billPaymentRepository.insertBillPayment(
				bill.getId(),
				payment.getDate(),
				payment.getAmount(),
				payment.getUser());
		}
		
		return result;
    }
	
	/**
	 * Updates the specified {@link Bill}.
	 * @param updateBill the bill to update.
	 * @return <code>true</code> if the bill has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs during the update.
	 */
	public boolean updateBill(
			Bill updateBill) throws OHServiceException 
	{
		boolean result = true;
	

		Bill savedBill = billRepository.save(updateBill);
		result = (savedBill != null);
				
		return result;
	}

	/**
	 * Deletes the specified {@link Bill}.
	 * @param deleteBill the bill to delete.
	 * @return <code>true</code> if the bill has been deleted, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs deleting the bill.
	 */
	public boolean deleteBill(
			Bill deleteBill) throws OHServiceException 
	{
		boolean result = true;
        		
		
		billRepository.updateDeleteWhereId(deleteBill.getId());
		
		return result;
	}

	/**
	 * Retrieves all the {@link Bill}s for the specified date range.
	 * @param dateFrom the low date range endpoint, inclusive. 
	 * @param dateTo the high date range endpoint, inclusive.
	 * @return a list of retrieved {@link Bill}s.
	 * @throws OHServiceException if an error occurs retrieving the bill list.
	 */
	public ArrayList<Bill> getBills(
			GregorianCalendar dateFrom, 
			GregorianCalendar dateTo) throws OHServiceException 
	{
		ArrayList<Bill> bills = (ArrayList<Bill>) billRepository.findAllWhereDates(
				new Timestamp(dateFrom.getTime().getTime()),
				new Timestamp(dateTo.getTime().getTime()));
		

		return bills;
	}

	/**
	 * Gets all the {@link Bill}s associated to the passed {@link BillPayments}.
	 * @param payments the {@link BillPayments} associated to the bill to retrieve.
	 * @return a list of {@link Bill} associated to the passed {@link BillPayments}.
	 * @throws OHServiceException if an error occurs retrieving the bill list.
	 */
	public ArrayList<Bill> getBills(
			ArrayList<BillPayments> payments) throws OHServiceException 
	{
		ArrayList<Integer> pBillCode = null;
		ArrayList<Bill> pBill = new ArrayList<Bill>();
		
		
		pBillCode = new ArrayList<Integer>(billRepository.findAllByPayments(payments));			
		for (int i=0; i<pBillCode.size(); i++)
		{
			Integer code = pBillCode.get(i);
			Bill bill = billRepository.findOne(code);
			
			
			pBill.add(i, bill);
		}
		
		return pBill;
	}

	/**
	 * Retrieves all the {@link BillPayments} associated to the passed {@link Bill} list.
	 * @param bills the bill list.
	 * @return a list of {@link BillPayments} associated to the passed bill list.
	 * @throws OHServiceException if an error occurs retrieving the payments.
	 */
	public ArrayList<BillPayments> getPayments(
			ArrayList<Bill> bills) throws OHServiceException 
	{

		ArrayList<Integer> pPaymentCode = null;
		ArrayList<BillPayments> pPayment = new ArrayList<BillPayments>();
		
		
		pPaymentCode = new ArrayList<Integer>(billPaymentRepository.findAllByBills(bills));			
		for (int i=0; i<pPaymentCode.size(); i++)
		{
			Integer code = pPaymentCode.get(i);
			BillPayments payment = billPaymentRepository.findOne(code);
			
			
			pPayment.add(i, payment);
		}
		
		return pPayment;
	}
	/**
	 * Return Distincts BillItems
	 * added by u2g
	 * @return BillItems list 
	 * @throws OHException
	 */
	public ArrayList<BillItems> getDistictsBillItems() throws OHException {
		ArrayList<BillItems> billItems =  billItemsRepository.findAllGroupByDesc();
		return billItems;
	}
	/**
	 * return the bill list which date between dateFrom and dateTo and containing given billItem
	 * added by u2g
	 * @param dateFrom
	 * @param dateTo
	 * @param billItem
	 * @return the bill list
	 * @throws OHException
	 */
	public ArrayList<Bill> getBills(GregorianCalendar dateFrom, GregorianCalendar dateTo, BillItems billItem) throws OHException {
		ArrayList<Bill> bills = null;
		Timestamp timestamp1 = new Timestamp(dateFrom.getTimeInMillis());
		Timestamp timestamp2 = new Timestamp(dateTo.getTimeInMillis());
		if(billItem == null) {
			bills = (ArrayList<Bill>) billRepository.findAllWhereDates(timestamp1, timestamp2);
		}
		else {
			bills = (ArrayList<Bill>)billRepository.findAllWhereDatesAndBillItem(timestamp1, timestamp2, billItem.getItemDescription());
			//for(Bill bill: bills)System.out.println("***************bill****************"+bill.toString());
		}
		return bills;
	}
}
