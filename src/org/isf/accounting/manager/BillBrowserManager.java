package org.isf.accounting.manager;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import org.isf.accounting.model.Bill;
import org.isf.accounting.model.BillItems;
import org.isf.accounting.model.BillPayments;
import org.isf.accounting.service.AccountingIoOperations;
import org.isf.menu.gui.Menu;
import org.isf.utils.exception.OHException;

public class BillBrowserManager {

	private AccountingIoOperations ioOperations;

	public BillBrowserManager() {
		ioOperations = Menu.getApplicationContext().getBean(AccountingIoOperations.class);
	}

	/**
	 * Returns all the stored {@link BillItems}.
	 * 
	 * @return a list of {@link BillItems} or null if an error occurs.
	 */
	public ArrayList<BillItems> getItems() {
		try {
			return ioOperations.getItems(0);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Retrieves all the {@link BillItems} associated to the passed {@link Bill}
	 * id.
	 * 
	 * @param billID
	 *            the bill id.
	 * @return a list of {@link BillItems} or <code>null</code> if an error
	 *         occurred.
	 */
	public ArrayList<BillItems> getItems(int billID) {
		if (billID == 0)
			return new ArrayList<BillItems>();
		try {
			return ioOperations.getItems(billID);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Retrieves all the stored {@link BillPayments}.
	 * 
	 * @return a list of bill payments or <code>null</code> if an error
	 *         occurred.
	 */
	public ArrayList<BillPayments> getPayments() {
		try {
			return ioOperations.getPayments(0);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Gets all the {@link BillPayments} for the specified {@link Bill}.
	 * 
	 * @param billID
	 *            the bill id.
	 * @return a list of {@link BillPayments} or <code>null</code> if an error
	 *         occurred.
	 */
	public ArrayList<BillPayments> getPayments(int billID) {
		if (billID == 0)
			return new ArrayList<BillPayments>();
		try {
			return ioOperations.getPayments(billID);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Stores a new {@link Bill}.
	 * 
	 * @param newBill
	 *            the bill to store.
	 * @return the generated id.
	 */
	public int newBill(Bill newBill) {
		try {
			return ioOperations.newBill(newBill);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return 0;
		}

	}

	/**
	 * Stores a list of {@link BillItems} associated to a {@link Bill}.
	 * 
	 * @param billID
	 *            the bill id.
	 * @param billItems
	 *            the bill items to store.
	 * @return <code>true</code> if the {@link BillItems} have been store,
	 *         <code>false</code> otherwise.
	 */
	public boolean newBillItems(int billID, ArrayList<BillItems> billItems) {
		try {
			return ioOperations.newBillItems(ioOperations.getBill(billID), billItems);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Stores a list of {@link BillPayments} associated to a {@link Bill}.
	 * 
	 * @param billID
	 *            the bill id.
	 * @param payItems
	 *            the bill payments.
	 * @return <code>true</code> if the payment have stored, <code>false</code>
	 *         otherwise.
	 */
	public boolean newBillPayments(int billID, ArrayList<BillPayments> payItems) {
		try {
			return ioOperations.newBillPayments(ioOperations.getBill(billID), payItems);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Updates the specified {@link Bill}.
	 * 
	 * @param updateBill
	 *            the bill to update.
	 * @return <code>true</code> if the bill has been updated,
	 *         <code>false</code> otherwise.
	 */
	public boolean updateBill(Bill updateBill) {
		try {
			return ioOperations.updateBill(updateBill);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Returns all the pending {@link Bill}s for the specified patient.
	 * 
	 * @param patID
	 *            the patient id.
	 * @return the list of pending bills or <code>null</code> if an error
	 *         occurred.
	 */
	public ArrayList<Bill> getPendingBills(int patID) {
		try {
			return ioOperations.getPendingBills(patID);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Get all the {@link Bill}s.
	 * 
	 * @return a list of bills or <code>null</code> if an error occurred.
	 */
	public ArrayList<Bill> getBills() {
		try {
			return ioOperations.getBills();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Get the {@link Bill} with specified billID
	 * 
	 * @param billID
	 * @return the {@link Bill} or <code>null</code> if an error occurred.
	 */
	public Bill getBill(int billID) {
		try {
			return ioOperations.getBill(billID);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Returns all user ids related to a {@link BillPayments}.
	 * 
	 * @return a list of user id or <code>null</code> if an error occurred.
	 */
	public ArrayList<String> getUsers() {
		try {
			return ioOperations.getUsers();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Deletes the specified {@link Bill}.
	 * 
	 * @param deleteBill
	 *            the bill to delete.
	 * @return <code>true</code> if the bill has been deleted,
	 *         <code>false</code> otherwise.
	 */
	public boolean deleteBill(Bill deleteBill) {
		try {
			return ioOperations.deleteBill(deleteBill);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * Retrieves all the {@link Bill}s for the specified date range.
	 * 
	 * @param dateFrom
	 *            the low date range endpoint, inclusive.
	 * @param dateTo
	 *            the high date range endpoint, inclusive.
	 * @return a list of retrieved {@link Bill}s or <code>null</code> if an
	 *         error occurred.
	 */
	public ArrayList<Bill> getBills(GregorianCalendar dateFrom, GregorianCalendar dateTo) {
		try {
			return ioOperations.getBills(dateFrom, dateTo);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Gets all the {@link Bill}s associated to the passed {@link BillPayments}.
	 * 
	 * @param payments
	 *            the {@link BillPayments} associated to the bill to retrieve.
	 * @return a list of {@link Bill} associated to the passed
	 *         {@link BillPayments} or <code>null</code> if an error occurred.
	 */
	public ArrayList<Bill> getBills(ArrayList<BillPayments> billPayments) {
		if (billPayments.isEmpty())
			return new ArrayList<Bill>();
		try {
			return ioOperations.getBills(billPayments);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Retrieves all the {@link BillPayments} for the specified date range.
	 * 
	 * @param dateFrom
	 *            low endpoint, inclusive, for the date range.
	 * @param dateTo
	 *            high endpoint, inclusive, for the date range.
	 * @return a list of {@link BillPayments} for the specified date range or
	 *         <code>null</code> if an error occurred.
	 */
	public ArrayList<BillPayments> getPayments(GregorianCalendar dateFrom, GregorianCalendar dateTo) {
		try {
			return ioOperations.getPayments(dateFrom, dateTo);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Retrieves all the {@link BillPayments} associated to the passed
	 * {@link Bill} list.
	 * 
	 * @param bills
	 *            the bill list.
	 * @return a list of {@link BillPayments} associated to the passed bill list
	 *         or <code>null</code> if an error occurred.
	 */
	public ArrayList<BillPayments> getPayments(ArrayList<Bill> billArray) {
		try {
			return ioOperations.getPayments(billArray);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
}
