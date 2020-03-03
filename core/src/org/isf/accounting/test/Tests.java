package org.isf.accounting.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.isf.accounting.model.Bill;
import org.isf.accounting.model.BillItems;
import org.isf.accounting.model.BillPayments;
import org.isf.accounting.service.AccountingIoOperations;
import org.isf.patient.model.Patient;
import org.isf.patient.test.TestPatient;
import org.isf.patient.test.TestPatientContext;
import org.isf.priceslist.model.PriceList;
import org.isf.priceslist.test.TestPriceList;
import org.isf.priceslist.test.TestPriceListContext;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestBill testBill;
	private static TestBillItems testBillItems;
	private static TestBillPayments testBillPayments;
	private static TestPatient testPatient;
	private static TestPriceList testPriceList;
	private static TestBillContext testBillContext;
	private static TestBillItemsContext testBillItemsContext;
	private static TestBillPaymentsContext testBillPaymentsContext;
	private static TestPatientContext testPatientContext;
	private static TestPriceListContext testPriceListContext;

    @Autowired
    AccountingIoOperations accountingIoOperation;
	
	@BeforeClass
    public static void setUpClass()  
    {
		jpa = new DbJpaUtil();
    	testBill = new TestBill();
    	testBillItems = new TestBillItems();
    	testBillPayments = new TestBillPayments();
    	testPatient = new TestPatient();
    	testPriceList = new TestPriceList();
    	testBillContext = new TestBillContext();
    	testBillItemsContext = new TestBillItemsContext();
    	testBillPaymentsContext = new TestBillPaymentsContext();
    	testPatientContext = new TestPatientContext();
    	testPriceListContext = new TestPriceListContext();
    	
        return;
    }

    @Before
    public void setUp() throws OHException
    {
        jpa.open();
        
        _saveContext();
		
		return;
    }
        
    @After
    public void tearDown() throws Exception 
    {
        _restoreContext();   
        
        jpa.flush();
        jpa.close();
                
        return;
    }
    
    @AfterClass
    public static void tearDownClass() throws OHException 
    {
    	testBill = null;
    	testBillItems = null;
    	testBillPayments = null;
    	testPatient = null;
    	testPriceList = null;
    	testBillContext = null;
    	testBillItemsContext = null;
    	testBillPaymentsContext = null;
    	testPatientContext = null;
    	testPriceListContext = null;

    	return;
    }
	
		
	@Test
	public void testBillGets()
	{
		int id = 0;
			

		try 
		{
			id = _setupTestBill(false);
			_checkBillIntoDb(id);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();	
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testBillSets() 
	{
		int id = 0;
			

		try 
		{
			id = _setupTestBill(true);
			_checkBillIntoDb(id);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testBillItemsGets()
	{
		int id = 0;
			

		try 
		{
			id = _setupTestBillItems(false);
			_checkBillItemsIntoDb(id);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testBillItemsSets()
	{
		int id = 0;
			

		try 
		{
			id = _setupTestBillItems(true);
			_checkBillItemsIntoDb(id);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testBillPaymentsGets()
	{
		int id = 0;
			

		try 
		{
			id = _setupTestBillPayments(false);
			_checkBillPaymentsIntoDb(id);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testBillPaymentsSets() 
	{
		int id = 0;
			

		try 
		{
			id = _setupTestBillPayments(true);
			_checkBillPaymentsIntoDb(id);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetPendingBills()
	{
		int id = 0;
	
		
		try 
		{
			id = _setupTestBill(false);
			Bill foundBill = (Bill)jpa.find(Bill.class, id); 
			ArrayList<Bill> bills = accountingIoOperation.getPendingBills(0);
			
			assertEquals(true, bills.contains(foundBill));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}	
				
		return;
	}

	@Test
	public void testIoGetPendingBillsPatId()
	{
		int id = 0;
		
		
		try 
		{
			id = _setupTestBill(false);
			Bill foundBill = (Bill)jpa.find(Bill.class, id); 
			ArrayList<Bill> bills = accountingIoOperation.getPendingBills(foundBill.getPatient().getCode());
			
			assertEquals(foundBill.getAmount(), bills.get(0).getAmount(), 0.1);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}	
	
	@Test
	public void testIoGetBills() 
	{
		int id = 0;
		
		
		try 
		{
			id = _setupTestBill(false);
			Bill foundBill = (Bill)jpa.find(Bill.class, id); 
			ArrayList<Bill> bills = accountingIoOperation.getBills();
			
			assertEquals(true, bills.contains(foundBill));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}	
	
	@Test
	public void testIoGetBill() 
	{
		int id = 0;
				

		try 
		{
			id = _setupTestBill(false);
			Bill foundBill = (Bill)jpa.find(Bill.class, id); 
			Bill bill = accountingIoOperation.getBill(id);
			
			assertEquals(foundBill.getAmount(), bill.getAmount(), 0.1);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}	
	
	@Test
	public void testIoGetUsers()
	{
		int id = 0;
		ArrayList<String> userIds = null;
		
		
		try 
		{
			id = _setupTestBillPayments(false);
			BillPayments foundBillPayment = (BillPayments)jpa.find(BillPayments.class, id); 
			userIds = accountingIoOperation.getUsers();
			
			assertEquals(true, userIds.contains(foundBillPayment.getUser()));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}	

	@Test
	public void testIoGetItems() 
	{		
		try 
		{
			int billItemID = _setupTestBillItems(false);
			
			BillItems foundBillItem = (BillItems)jpa.find(BillItems.class, billItemID); 
			ArrayList<BillItems> billItems = accountingIoOperation.getItems(foundBillItem.getBill().getId());
			
			assertEquals(true, billItems.contains(foundBillItem));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}

	@Test
	public void testIoGetItemsBillId() 
	{
		int id = 0;
		
		
		try 
		{
			id = _setupTestBillItems(false);
			BillItems foundBillItem = (BillItems)jpa.find(BillItems.class, id); 
			ArrayList<BillItems> billItems = accountingIoOperation.getItems(foundBillItem.getBill().getId());
			
			assertEquals(foundBillItem.getItemAmount(), billItems.get(0).getItemAmount(), 0.1);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testIoGetPayments() 
	{
		int id = 0;
		
		
		try 
		{
			id = _setupTestBillPayments(false);
			BillPayments foundBillPayment = (BillPayments)jpa.find(BillPayments.class, id); 
			GregorianCalendar dateFrom = new GregorianCalendar(4, 3, 2);
			GregorianCalendar dateTo = new GregorianCalendar();
			ArrayList<BillPayments> billPayments = accountingIoOperation.getPayments(dateFrom, dateTo);
			
			assertEquals(true, billPayments.contains(foundBillPayment));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}

	@Test
	public void testIoGetPaymentsBillId()
	{
		int id = 0;
		
		
		try 
		{
			id = _setupTestBillPayments(false);
			BillPayments foundBillPayment = (BillPayments)jpa.find(BillPayments.class, id); 
			ArrayList<BillPayments> billItems = accountingIoOperation.getPayments(foundBillPayment.getBill().getId());
			
			assertEquals(foundBillPayment.getAmount(), billItems.get(0).getAmount(), 0.1);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}

	@Test
	public void testIoNewBill() 
	{
		int id = 0;
		Bill bill = null;
		Patient	patient = null; 
		PriceList priceList = null;
		

		try 
		{
			patient = testPatient.setup(false); 
			priceList = testPriceList.setup(false);
			bill = testBill.setup(priceList, patient, false);
			jpa.beginTransaction();	
			jpa.persist(priceList);
			jpa.persist(patient);
			jpa.commitTransaction();
			id = accountingIoOperation.newBill(bill);
			_checkBillIntoDb(id);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
						
		return;
	}
	
	@Test
	public void testIoNewBillItems()
	{
		ArrayList<BillItems> billItems = new ArrayList<BillItems>(); 
		int deleteId = 0, insertId = 0;
		boolean result = false;
			

		try 
		{
			deleteId = _setupTestBillItems(false);
			BillItems deleteBillItem = (BillItems)jpa.find(BillItems.class, deleteId); 
			
			Bill bill = deleteBillItem.getBill();
			BillItems insertBillItem = testBillItems.setup(null, false);
			insertId = deleteId + 1;
			billItems.add(insertBillItem);	
			result = accountingIoOperation.newBillItems(bill, billItems);
			
			BillItems foundBillItems = (BillItems)jpa.find(BillItems.class, insertId); 		
			assertEquals(true, result);				
			assertEquals(bill.getId(), foundBillItems.getBill().getId());		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
		
		return;
	}	

	@Test
	public void testIoNewBillPayments() 
	{
		ArrayList<BillPayments> billPayments = new ArrayList<BillPayments>(); 
		int deleteId = 0, insertId = 0;
		boolean result = false;
			

		try 
		{		
			deleteId = _setupTestBillPayments(false);
			BillPayments deleteBillPayment = (BillPayments)jpa.find(BillPayments.class, deleteId); 
			
			Bill bill = deleteBillPayment.getBill();
			BillPayments insertBillPayment = testBillPayments.setup(null, false);
			insertId = deleteId + 1;
			billPayments.add(insertBillPayment);	
			result = accountingIoOperation.newBillPayments(bill, billPayments);
			
			BillPayments foundBillPayments = (BillPayments)jpa.find(BillPayments.class, insertId); 		
			assertEquals(true, result);				
			assertEquals(bill.getId(), foundBillPayments.getBill().getId());		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}	
		
		return;
	}	
	
	@Test
	public void testIoUpdateBill() 
	{
		int id = 0;
		
		
		try 
		{		
			id = _setupTestBill(true);
			Bill bill = (Bill)jpa.find(Bill.class, id); 
			bill.setAmount(12.34);
			
			accountingIoOperation.updateBill(bill);
			
			assertEquals(12.34, bill.getAmount(), 0.1);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
						
		return;
	}

	@Test
	public void testIoDeleteBill()
	{
		int id = 0;
		
		
		try 
		{		
			id = _setupTestBill(true);
			Bill bill = (Bill)jpa.find(Bill.class, id); 
			
			boolean result = accountingIoOperation.deleteBill(bill);
			 		
			assertEquals(true, result);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
						
		return;
	}

	@Test
	public void testIoGetBillsTimeRange() 
	{
		int id = 0;
		
		
		try 
		{		
			id = _setupTestBill(false);
			Bill foundBill = (Bill)jpa.find(Bill.class, id); 
			GregorianCalendar dateFrom = new GregorianCalendar(4, 3, 2);
			GregorianCalendar dateTo = new GregorianCalendar();
			ArrayList<Bill> bills = accountingIoOperation.getBills(dateFrom, dateTo);
			
			assertEquals(true, bills.contains(foundBill));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testIoGetBillsTimeRangeAndItem() 
	{
		int id = 0;
		//billDate = new GregorianCalendar(10, 9, 8); //from src/org/isf/accounting/test/TestBill.java
		GregorianCalendar dateFrom = new GregorianCalendar(10, 9, 7);
		GregorianCalendar dateTo = new GregorianCalendar(10, 9, 9);
		
		try 
		{
			id = _setupTestBill(false);
			Bill foundBill = (Bill)jpa.find(Bill.class, id); 
			
			ArrayList<Bill> bills = accountingIoOperation.getBills(dateFrom, dateTo);
			assertEquals(true, bills.contains(foundBill));
			
			bills = accountingIoOperation.getBills(new GregorianCalendar(10, 0, 1), dateFrom);
			assertEquals(false, bills.contains(foundBill));
			
			bills = accountingIoOperation.getBills(dateTo, new GregorianCalendar(11, 0, 1));
			assertEquals(false, bills.contains(foundBill));
			
			id = _setupTestBillItems(false);
			BillItems foundBillItem = (BillItems)jpa.find(BillItems.class, id);
			foundBill = (Bill)jpa.find(Bill.class, foundBillItem.getBill().getId());
			
			bills = accountingIoOperation.getBills(dateFrom, dateTo, foundBillItem);
			assertEquals(true, bills.contains(foundBill));
			
			id = _setupTestBillItems(true);
			foundBillItem = (BillItems)jpa.find(BillItems.class, id);
			
			bills = accountingIoOperation.getBills(dateFrom, dateTo, foundBillItem);
			assertEquals(true, bills.contains(foundBill));
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}

	@Test
	public void testIoGetBillsPayment() 
	{
		int id = 0;
		ArrayList<BillPayments> payments = new ArrayList<BillPayments>();
		
		
		try 
		{		
			id = _setupTestBillPayments(false);
			BillPayments foundBillPayment = (BillPayments)jpa.find(BillPayments.class, id); 
			
			payments.add(foundBillPayment);	
			ArrayList<Bill> bills = accountingIoOperation.getBills(payments);
			
			assertEquals(foundBillPayment.getBill().getAmount(), bills.get(0).getAmount(), 0.1);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}

	@Test
	public void testIoGetPaymentsBill()
	{
		int id = 0;
		ArrayList<Bill> bills = new ArrayList<Bill>();
		
		
		try 
		{		
			id = _setupTestBillPayments(false);
			BillPayments foundBillPayment = (BillPayments)jpa.find(BillPayments.class, id); 
			Bill foundBill = foundBillPayment.getBill(); 
			
			bills.add(foundBill);	
			ArrayList<BillPayments> payments = accountingIoOperation.getPayments(bills);
			
			assertEquals(foundBill.getAmount(), payments.get(0).getBill().getAmount(), 0.1);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testIoGetDistictsBillItems() 
	{
		int id = 0;
		
		
		try 
		{
			id = _setupTestBillItems(false);
			BillItems foundBillItem = (BillItems)jpa.find(BillItems.class, id); 
			ArrayList<BillItems> billItems = accountingIoOperation.getDistictsBillItems();
			
			assertEquals(true, billItems.contains(foundBillItem));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();		
			assertEquals(true, false);
		}
				
		return;
	}
	
	
	private void _saveContext() throws OHException 
    {	
		testBillContext.saveAll(jpa);
		testBillItemsContext.saveAll(jpa);
		testBillPaymentsContext.saveAll(jpa);
		testPatientContext.saveAll(jpa);
		testPriceListContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		testBillPaymentsContext.deleteNews(jpa);
		testBillItemsContext.deleteNews(jpa);
		testBillContext.deleteNews(jpa);
		testPatientContext.deleteNews(jpa);
		testPriceListContext.deleteNews(jpa);
        
        return;
    }
        
	private int _setupTestBill(
			boolean usingSet) throws OHException 
	{
		Bill bill;
		Patient	patient = testPatient.setup(false); 
		PriceList priceList = testPriceList.setup(false);
		

    	jpa.beginTransaction();	
    	bill = testBill.setup(priceList, patient, usingSet);
    	jpa.persist(priceList);
    	jpa.persist(patient);
		jpa.persist(bill);
    	jpa.commitTransaction();
    	
		return bill.getId();
	}
		
	private void  _checkBillIntoDb(
			int id) throws OHException 
	{
		Bill foundBill;
		

		foundBill = (Bill)jpa.find(Bill.class, id); 
		testBill.check(foundBill);
		testPriceList.check(foundBill.getList());
		testPatient.check(foundBill.getPatient());
		
		return;
	}
	
	private int _setupTestBillItems(
			boolean usingSet) throws OHException 
	{
		BillItems billItem;
		Patient	patient = testPatient.setup(false); 
		PriceList priceList = testPriceList.setup(false);
		Bill bill = testBill.setup(priceList, patient, false);
		

    	jpa.beginTransaction();	
    	billItem = testBillItems.setup(bill, usingSet);
    	jpa.persist(priceList);
    	jpa.persist(patient);
		jpa.persist(bill);
		jpa.persist(billItem);
    	jpa.commitTransaction();
    	
		return billItem.getId();		
	}
	
	private void  _checkBillItemsIntoDb(
			int id) throws OHException 
	{
		BillItems foundBillItem;
		

		foundBillItem = (BillItems)jpa.find(BillItems.class, id); 
		testBillItems.check(foundBillItem);
		testBill.check(foundBillItem.getBill());
		testPriceList.check(foundBillItem.getBill().getList());
		testPatient.check(foundBillItem.getBill().getPatient());
		
		return;
	}
	
	private int _setupTestBillPayments(
			boolean usingSet) throws OHException 
	{
		BillPayments billPayment;
		Patient	patient = testPatient.setup(false); 
		PriceList priceList = testPriceList.setup(false);
		Bill bill = testBill.setup(priceList, patient, false);
		

    	jpa.beginTransaction();	
    	billPayment = testBillPayments.setup(bill, usingSet);
    	jpa.persist(priceList);
    	jpa.persist(patient);
		jpa.persist(bill);
		jpa.persist(billPayment);
    	jpa.commitTransaction();
    			
		return billPayment.getId();
	}
		
	private void  _checkBillPaymentsIntoDb(
			int id) throws OHException 
	{
		BillPayments foundBillPayment;
		

		foundBillPayment = (BillPayments)jpa.find(BillPayments.class, id); 
		testBillPayments.check(foundBillPayment);
		testBill.check(foundBillPayment.getBill());
		testPriceList.check(foundBillPayment.getBill().getList());
		testPatient.check(foundBillPayment.getBill().getPatient());
		
		return;
	}
	
}