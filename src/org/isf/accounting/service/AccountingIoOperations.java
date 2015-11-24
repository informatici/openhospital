package org.isf.accounting.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.accounting.model.Bill;
import org.isf.accounting.model.BillItems;
import org.isf.accounting.model.BillPayments;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

/**
 * Persistence class for Accounting module.
 */
@Component
public class AccountingIoOperations {

	/**
	 * Returns all the pending {@link Bill}s for the specified patient.
	 * @param patID the patient id.
	 * @return the list of pending bills.
	 * @throws OHException if an error occurs retrieving the pending bills.
	 */
	public ArrayList<Bill> getPendingBills(int patID) throws OHException {
		ArrayList<Bill> pendingBills = null;

		List<Object> parameters = new ArrayList<Object>(1);
		StringBuilder query = new StringBuilder("SELECT * FROM BILLS");
		query.append(" WHERE BLL_STATUS = 'O'");
		if (patID != 0) {
			query.append(" AND BLL_ID_PAT = ?");
			parameters.add(patID);
		}
		query.append(" ORDER BY BLL_DATE DESC");

		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);
			pendingBills = new ArrayList<Bill>(resultSet.getFetchSize());
			while (resultSet.next()) {
				Bill bill = toBill(resultSet);
				pendingBills.add(bill);
			}
		}catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return pendingBills;
	}
	
	/**
	 * Get all the {@link Bill}s.
	 * @return a list of bills.
	 * @throws OHException if an error occurs retrieving the bills.
	 */
	public ArrayList<Bill> getBills() throws OHException {
		ArrayList<Bill> bills = null;
		String query = "SELECT * FROM BILLS ORDER BY BLL_DATE DESC";
		
		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			ResultSet resultSet = dbQuery.getData(query,true);
			bills = new ArrayList<Bill>(resultSet.getFetchSize());
			while (resultSet.next()) {
				Bill bill = toBill(resultSet);
				bills.add(bill);
			}
		}catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return bills;
	}
	
	/**
	 * Get the {@link Bill} with specified billID.
	 * @param billID
	 * @return the {@link Bill}.
	 * @throws OHException if an error occurs retrieving the bill.
	 */
	public Bill getBill(int billID) throws OHException {
		Bill bill = null;
		String query = "SELECT * FROM BILLS WHERE BLL_ID = ?";
		List<Object> parameters = Collections.<Object>singletonList(billID);
		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			ResultSet resultSet = dbQuery.getDataWithParams(query, parameters, true);
			while (resultSet.next()) {
				bill = toBill(resultSet);
			}
		}catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return bill;
	}

	/**
	 * Returns all user ids related to a {@link BillPayments}.
	 * @return a list of user id.
	 * @throws OHException if an error occurs retrieving the users list.
	 */
	public ArrayList<String> getUsers() throws OHException {
		ArrayList<String> userIds = null;
		String query = "SELECT DISTINCT(BLP_USR_ID_A) FROM BILLPAYMENTS ORDER BY BLP_USR_ID_A ASC";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			ResultSet resultSet = dbQuery.getData(query,true);
			userIds = new ArrayList<String>(resultSet.getFetchSize());
			while (resultSet.next()) {
				userIds.add(resultSet.getString("BLP_USR_ID_A"));
			}			
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return userIds;
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

		List<Object> parameters = new ArrayList<Object>(1);
		StringBuilder query = new StringBuilder("SELECT * FROM BILLITEMS"); 
		if (billID != 0) {
			query.append(" WHERE BLI_ID_BILL = ?");
			parameters.add(billID);
		}
		query.append(" ORDER BY BLI_ID ASC");

		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);
			billItems = new ArrayList<BillItems>(resultSet.getFetchSize());
			while (resultSet.next()) {
				billItems.add(new BillItems(resultSet.getInt("BLI_ID"),
						resultSet.getInt("BLI_ID_BILL"),
						resultSet.getBoolean("BLI_IS_PRICE"),
						resultSet.getString("BLI_ID_PRICE"),
						resultSet.getString("BLI_ITEM_DESC"),
						resultSet.getDouble("BLI_ITEM_AMOUNT"),
						resultSet.getInt("BLI_QTY")));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
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
	public ArrayList<BillPayments> getPayments(GregorianCalendar dateFrom, GregorianCalendar dateTo) throws OHException {
		ArrayList<BillPayments> payments = null;
		StringBuilder query = new StringBuilder("SELECT * FROM BILLPAYMENTS");
		query.append(" WHERE DATE(BLP_DATE) BETWEEN ? AND ?");
		query.append(" ORDER BY BLP_ID_BILL, BLP_DATE ASC");
		
		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			List<Object> parameters = new ArrayList<Object>(2);
			parameters.add(new Timestamp(dateFrom.getTime().getTime()));
			parameters.add(new Timestamp(dateTo.getTime().getTime()));
			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);

			payments = new ArrayList<BillPayments>(resultSet.getFetchSize());
			while (resultSet.next()) {
				payments.add(new BillPayments(resultSet.getInt("BLP_ID"),
						resultSet.getInt("BLP_ID_BILL"),
						convertToGregorianCalendar(resultSet.getTimestamp("BLP_DATE")),
						resultSet.getDouble("BLP_AMOUNT"),
						resultSet.getString("BLP_USR_ID_A")));
			}
		}catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return payments;
	}

	/**
	 * Retrieves all the {@link BillPayments} for the specified {@link Bill} id, or all 
	 * the stored {@link BillPayments} if no id is indicated.
	 * @param billID the bill id or <code>0</code>.
	 * @return the list of bill payments.
	 * @throws OHException if an error occurs retrieving the bill payments.
	 */
	public ArrayList<BillPayments> getPayments(int billID) throws OHException {
		ArrayList<BillPayments> payments = null;

		List<Object> parameters = new ArrayList<Object>(1);
		StringBuilder query = new StringBuilder("SELECT * FROM BILLPAYMENTS");
		if (billID != 0) {
			query.append(" WHERE BLP_ID_BILL = ?");
			parameters.add(billID);
		}
		query.append(" ORDER BY BLP_ID_BILL, BLP_DATE ASC");

		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);
			payments = new ArrayList<BillPayments>(resultSet.getFetchSize());
			while (resultSet.next()) {
				payments.add(new BillPayments(resultSet.getInt("BLP_ID"),
						resultSet.getInt("BLP_ID_BILL"),
						convertToGregorianCalendar(resultSet.getTimestamp("BLP_DATE")),
						resultSet.getDouble("BLP_AMOUNT"),
						resultSet.getString("BLP_USR_ID_A")));
			}
		}catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return payments;
	}

	/**
	 * Converts the specified {@link Timestamp} to a {@link GregorianCalendar} instance.
	 * @param aDate the date to convert.
	 * @return the corresponding GregorianCalendar value or <code>null</code> if the input value is <code>null</code>.
	 */
	public GregorianCalendar convertToGregorianCalendar(Timestamp aDate) {
		if (aDate == null)
			return null;
		GregorianCalendar time = new GregorianCalendar();
		time.setTime(aDate);
		return time;
	}

	/**
	 * Stores a new {@link Bill}.
	 * @param newBill the bill to store.
	 * @return the generated {@link Bill} id.
	 * @throws OHException if an error occurs storing the bill.
	 */
	public int newBill(Bill newBill) throws OHException {
		int billID;
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {

			String query = "INSERT INTO BILLS (" +
					"BLL_DATE, BLL_UPDATE, BLL_IS_LST, BLL_ID_LST, BLL_LST_NAME, BLL_IS_PAT, BLL_ID_PAT, BLL_PAT_NAME, BLL_STATUS, BLL_AMOUNT, BLL_BALANCE, BLL_USR_ID_A) "+
					"VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

			List<Object> parameters = new ArrayList<Object>(11);
			parameters.add(new java.sql.Timestamp(newBill.getDate().getTime().getTime()));
			parameters.add(new java.sql.Timestamp(newBill.getUpdate().getTime().getTime()));
			parameters.add(newBill.isList());
			parameters.add(newBill.getListID());
			parameters.add(newBill.getListName());
			parameters.add(newBill.isPatient());
			parameters.add(newBill.getPatID());
			parameters.add(newBill.getPatName());
			parameters.add(newBill.getStatus());
			parameters.add(newBill.getAmount());
			parameters.add(newBill.getBalance());
			parameters.add(newBill.getUser());
			ResultSet result = dbQuery.setDataReturnGeneratedKeyWithParams(query, parameters, true);

			if (result.next())
				billID = result.getInt(1);
			else return 0;

			return billID;

		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
	}

	/**
	 * Stores a list of {@link BillItems} associated to a {@link Bill}.
	 * @param billID the bill id.
	 * @param billItems the bill items to store.
	 * @return <code>true</code> if the {@link BillItems} have been store, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the store operation.
	 */
	public boolean newBillItems(int billID, ArrayList<BillItems> billItems) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = true;
		try{
			//With this INSERT and UPDATE processes are equals
			String query = "DELETE FROM BILLITEMS WHERE BLI_ID_BILL = ?";
			List<Object> parameters = Collections.<Object>singletonList(billID);
			dbQuery.setDataWithParams(query, parameters, false);

			query = "INSERT INTO BILLITEMS (" +
					"BLI_ID_BILL, BLI_IS_PRICE, BLI_ID_PRICE, BLI_ITEM_DESC, BLI_ITEM_AMOUNT, BLI_QTY) "+
					"VALUES (?,?,?,?,?,?)";

			for (BillItems item : billItems) {

				parameters = new ArrayList<Object>(6);
				parameters.add(billID);
				parameters.add(item.isPrice());
				parameters.add(item.getPriceID());
				parameters.add(item.getItemDescription());
				parameters.add(item.getItemAmount());
				parameters.add(item.getItemQuantity());
				//System.out.println(pstmt.toString());
				result = result && dbQuery.setDataWithParams(query, parameters, false);
			}
			
			if (result) {
				dbQuery.commit();
			}
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Stores a list of {@link BillPayments} associated to a {@link Bill}.
	 * @param billID the bill id.
	 * @param payItems the bill payments.
	 * @return <code>true</code> if the payment have stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the store procedure.
	 */
	public boolean newBillPayments(int billID, ArrayList<BillPayments> payItems) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = true;
		try{

			//With this INSERT and UPDATE processes are equals
			String query = "DELETE FROM BILLPAYMENTS WHERE BLP_ID_BILL = ?";
			List<Object> parameters = Collections.<Object>singletonList(billID);
			dbQuery.setDataWithParams(query, parameters, false);

			query = "INSERT INTO BILLPAYMENTS (" +
					"BLP_ID_BILL, BLP_DATE, BLP_AMOUNT, BLP_USR_ID_A) " +
					"VALUES (?,?,?,?)";

			for (BillPayments item : payItems) {
				parameters = new ArrayList<Object>(4);
				parameters.add(billID);
				parameters.add(new java.sql.Timestamp(item.getDate().getTime().getTime()));
				parameters.add(item.getAmount());
				parameters.add(item.getUser());
				//System.out.println(pstmt.toString());
				result = result && dbQuery.setDataWithParams(query, parameters, false);
			}

			if (result) {
				dbQuery.commit();
			}
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Updates the specified {@link Bill}.
	 * @param updateBill the bill to update.
	 * @return <code>true</code> if the bill has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateBill(Bill updateBill) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		try{

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

			List<Object> parameters = new ArrayList<Object>(12);

			parameters.add(new java.sql.Timestamp(updateBill.getDate().getTimeInMillis()));
			parameters.add(new java.sql.Timestamp(updateBill.getUpdate().getTimeInMillis()));
			parameters.add(updateBill.isList());
			parameters.add(updateBill.getListID());
			parameters.add(updateBill.getListName());
			parameters.add(updateBill.isPatient());
			parameters.add(updateBill.getPatID());
			parameters.add(updateBill.getPatName());
			parameters.add(updateBill.getStatus());
			parameters.add(updateBill.getAmount());
			parameters.add(updateBill.getBalance());
			parameters.add(updateBill.getUser());
			parameters.add(updateBill.getId());

			//System.out.println(pstmt.toString());
			dbQuery.setDataWithParams(query, parameters, true);

			return true;

		} finally {
			dbQuery.releaseConnection();
		}
	}

	/**
	 * Deletes the specified {@link Bill}.
	 * @param deleteBill the bill to delete.
	 * @return <code>true</code> if the bill has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs deleting the bill.
	 */
	public boolean deleteBill(Bill deleteBill) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = true;
		try{
			List<Object> parameters = Collections.<Object>singletonList(deleteBill.getId());
			
//			String query = "DELETE FROM BILLS WHERE BLL_ID = ?";
//			dbQuery.setDataWithParams(query, parameters, false);
//
//			query = "DELETE FROM BILLPAYMENTS WHERE BLP_ID_BILL = ?";
//			dbQuery.setDataWithParams(query, parameters, false);
//
//			query = "DELETE FROM BILLITEMS WHERE BLI_ID_BILL = ?";
//			dbQuery.setDataWithParams(query, parameters, false);
			
			String query = "UPDATE BILLS SET BLL_STATUS = 'D' WHERE BLL_ID = ?";
			dbQuery.setDataWithParams(query, parameters, false);

		} finally {
			dbQuery.commit();
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Retrieves all the {@link Bill}s for the specified date range.
	 * @param dateFrom the low date range endpoint, inclusive. 
	 * @param dateTo the high date range endpoint, inclusive.
	 * @return a list of retrieved {@link Bill}s.
	 * @throws OHException if an error occurs retrieving the bill list.
	 */
	public ArrayList<Bill> getBills(GregorianCalendar dateFrom, GregorianCalendar dateTo) throws OHException {
		ArrayList<Bill> bills = null;
		String query = "SELECT * FROM BILLS WHERE DATE(BLL_DATE) BETWEEN ? AND ?";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {

			List<Object> parameters = new ArrayList<Object>(2);
			parameters.add(new Timestamp(dateFrom.getTime().getTime()));
			parameters.add(new Timestamp(dateTo.getTime().getTime()));

			ResultSet resultSet = dbQuery.getDataWithParams(query, parameters, true);

			bills = new ArrayList<Bill>(resultSet.getFetchSize());
			while (resultSet.next()) {
				Bill bill = toBill(resultSet);
				bills.add(bill);
			}
		}catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return bills;
	}

	/**
	 * Gets all the {@link Bill}s associated to the passed {@link BillPayments}.
	 * @param payments the {@link BillPayments} associated to the bill to retrieve.
	 * @return a list of {@link Bill} associated to the passed {@link BillPayments}.
	 * @throws OHException if an error occurs retrieving the bill list.
	 */
	public ArrayList<Bill> getBills(ArrayList<BillPayments> payments) throws OHException {

		ArrayList<Bill> bills = null;

		List<Object> parameters = new ArrayList<Object>();

		StringBuilder query = new StringBuilder("SELECT * FROM BILLS WHERE BLL_ID IN ( ");
		for (int i = 0; i < payments.size(); i++) {
			BillPayments payment = payments.get(i);
			if (i == payments.size() - 1) {
				query.append("?");
				parameters.add(payment.getBillID());
			} else {
				query.append("?, ");
				parameters.add(payment.getBillID());
			}
		}
		query.append(")");

		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);
			bills = new ArrayList<Bill>(resultSet.getFetchSize());
			while (resultSet.next()) {
				Bill bill = toBill(resultSet);
				bills.add(bill);
			}
		}catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return bills;
	}

	/**
	 * Retrieves all the {@link BillPayments} associated to the passed {@link Bill} list.
	 * @param bills the bill list.
	 * @return a list of {@link BillPayments} associated to the passed bill list.
	 * @throws OHException if an error occurs retrieving the payments.
	 */
	public ArrayList<BillPayments> getPayments(ArrayList<Bill> bills) throws OHException {
		ArrayList<BillPayments> payments = null;

		List<Object> parameters = new ArrayList<Object>();
		StringBuilder query = new StringBuilder("SELECT * FROM BILLPAYMENTS WHERE BLP_ID_BILL IN (''");
		if (bills!=null) {
			for (Bill bill:bills) {
				query.append(", ?");
				parameters.add(bill.getId());
			}
		}
		query.append(")");

		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);
			payments = new ArrayList<BillPayments>(resultSet.getFetchSize());
			while (resultSet.next()) {
				payments.add(new BillPayments(resultSet.getInt("BLP_ID"),
						resultSet.getInt("BLP_ID_BILL"),
						convertToGregorianCalendar(resultSet.getTimestamp("BLP_DATE")),
						resultSet.getDouble("BLP_AMOUNT"),
						resultSet.getString("BLP_USR_ID_A")));
			}
		}catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return payments;
	}
	
	public Bill toBill(ResultSet resultSet) throws SQLException {
		Bill bill = new Bill(resultSet.getInt("BLL_ID"),
				convertToGregorianCalendar(resultSet.getTimestamp("BLL_DATE")),
				convertToGregorianCalendar(resultSet.getTimestamp("BLL_UPDATE")),
				resultSet.getBoolean("BLL_IS_LST"),
				resultSet.getInt("BLL_ID_LST"),
				resultSet.getString("BLL_LST_NAME"),
				resultSet.getBoolean("BLL_IS_PAT"),
				resultSet.getInt("BLL_ID_PAT"),
				resultSet.getString("BLL_PAT_NAME"),
				resultSet.getString("BLL_STATUS"),
				resultSet.getDouble("BLL_AMOUNT"),
				resultSet.getDouble("BLL_BALANCE"),
				resultSet.getString("BLL_USR_ID_A"));
		return bill;
	}
}
