package org.isf.priceslist.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.priceslist.model.Price;
import org.isf.priceslist.model.PriceList;
import org.isf.priceslist.service.PricesListIoOperations;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class Tests 
{
	@Autowired
	private static DbJpaUtil jpa;
	private static TestPriceList testPriceList;
	private static TestPriceListContext testPriceListContext;
	private static TestPrice testPrice;
	private static TestPriceContext testPriceContext;
	
		
	@BeforeClass
    public static void setUpClass()  
    {
    	
    	testPriceList = new TestPriceList();
    	testPriceListContext = new TestPriceListContext();
    	testPrice = new TestPrice();
    	testPriceContext = new TestPriceContext();

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
    	jpa.destroy();
    	testPriceList = null;
    	testPriceListContext = null;
    	testPrice = null;
    	testPriceContext = null;

    	return;
    }
	
		
	@Test
	public void testPriceListGets()
	{
		int id = 0;
			

		try 
		{		
			id = _setupTestPriceList(false);
			_checkPriceListIntoDb(id);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testPriceListSets()
	{
		int id = 0;
			

		try 
		{		
			id = _setupTestPriceList(true);
			_checkPriceListIntoDb(id);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetLists() 
	{
		PricesListIoOperations ioOperations = new PricesListIoOperations();
		
		
		try 
		{		
			ArrayList<PriceList> priceLists = ioOperations.getLists();
			
			
			assertEquals(testPriceListContext.getAllSaved().get(0).getName(), priceLists.get(0).getName());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testPriceGets()
	{
		int id = 0;
			

		try 
		{		
			id = _setupTestPrice(false);
			_checkPriceIntoDb(id);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testPriceSets()
	{
		int id = 0;
			

		try 
		{		
			id = _setupTestPrice(false);
			_checkPriceIntoDb(id);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetPrices()
	{
		PricesListIoOperations ioOperations = new PricesListIoOperations();
		
		
		try 
		{		
			ArrayList<Price> prices = ioOperations.getPrices();
			
			
			assertEquals(testPriceContext.getAllSaved().get(0).getPrice(), prices.get(0).getPrice());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdatePrices() 
	{
		PricesListIoOperations ioOperations = new PricesListIoOperations();
		ArrayList<Price> prices = new ArrayList<Price>(); 
		int deleteId = 0, insertId = 0;
		boolean result = false;
			

		try 
		{		
			deleteId = _setupTestPrice(false);
			Price deletePrice = (Price)jpa.find(Price.class, deleteId); 
			
			PriceList priceList = deletePrice.getList();
			Price insertPrice = testPrice.setup(null, false);
			insertId = deleteId + 1;
			prices.add(insertPrice);	
			result = ioOperations.updatePrices(priceList, prices);
			
			Price foundPrice = (Price)jpa.find(Price.class, insertId); 		
			assertEquals(true, result);				
			assertEquals(priceList.getId(), foundPrice.getList().getId());		
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}	
		
	@Test
	public void testIoNewList()
	{
		PricesListIoOperations ioOperations = new PricesListIoOperations();
		int id = 0;
		
		
		try 
		{		
			PriceList pricelist = testPriceList.setup(false);
			ioOperations.newList(pricelist);	
			
			id = _getListMax();	
			PriceList foundPriceList = (PriceList)jpa.find(PriceList.class, id); 	
			_checkPriceListIntoDb(foundPriceList.getId());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testIoUpdateList()
	{
		PricesListIoOperations ioOperations = new PricesListIoOperations();
		int id = 0;
		
		
		try 
		{		
			id = _setupTestPriceList(true);
			PriceList priceList = (PriceList)jpa.find(PriceList.class, id); 
			priceList.setName("NewListName");
			
			ioOperations.updateList(priceList);
			
			assertEquals("NewListName", priceList.getName());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
						
		return;
	}
	
	@Test
	public void testIoDeleteList()
	{
		PricesListIoOperations ioOperations = new PricesListIoOperations();
		int id = 0, maxId = 0;
		
		
		try 
		{		
			maxId = _getListMax();
			id = _setupTestPriceList(true);
			PriceList priceList = (PriceList)jpa.find(PriceList.class, id); 
			
			ioOperations.deleteList(priceList);
			
			id = _getListMax();
			assertEquals(maxId, id);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
						
		return;
	}
	
	@Test
	public void testIoCopyList()
	{
		PricesListIoOperations ioOperations = new PricesListIoOperations();
		int id = 0, maxId = 0;
		
		
		try 
		{		
			id = _setupTestPrice(true);
			Price price = (Price)jpa.find(Price.class, id); 
			PriceList priceList = price.getList(); 
			
			ioOperations.copyList(priceList, 2, 0);
			
			maxId = _getPriceMax();
			Price copyPrice = (Price)jpa.find(Price.class, maxId);
			assertEquals(id+1, maxId);
			assertEquals(2 * price.getPrice().doubleValue(), copyPrice.getPrice().doubleValue(), 0.10);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
						
		return;
	}
	
	@Test
	public void testIoCopyListSteps()
	{
		PricesListIoOperations ioOperations = new PricesListIoOperations();
		int id = 0, maxId = 0;
		
		
		try 
		{		
			id = _setupTestPrice(true);
			Price price = (Price)jpa.find(Price.class, id); 
			PriceList priceList = price.getList(); 
			
			ioOperations.copyList(priceList, 2, 3);
			
			maxId = _getPriceMax();
			Price copyPrice = (Price)jpa.find(Price.class, maxId);
			assertEquals(id+1, maxId);
			assertEquals(Math.round(2 * price.getPrice().doubleValue() / 3) *3, copyPrice.getPrice().doubleValue(), 0.10);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
						
		return;
	}
	
	private void _saveContext() throws OHException 
    {	
		testPriceListContext.saveAll(jpa);
		testPriceContext.saveAll(jpa);
        		
        return;
    }
		
    private void _restoreContext() throws OHException 
    {
		System.out.println(testPriceContext.getAllSaved());
		System.out.println(testPriceListContext.getAllSaved());
    	testPriceContext.deleteNews(jpa);
    	testPriceListContext.deleteNews(jpa);
        
        return;
    }
              
	private int _setupTestPriceList(
			boolean usingSet) throws OHException 
	{
		PriceList priceList;
	

    	jpa.beginTransaction();	
    	priceList = testPriceList.setup(usingSet);
		jpa.persist(priceList);
    	jpa.commitTransaction();
		
		return priceList.getId();
	}
	
	private void _checkPriceListIntoDb(
			int id) throws OHException 
	{
		PriceList foundPriceList;
		

		foundPriceList = (PriceList)jpa.find(PriceList.class, id); 
		testPriceList.check(foundPriceList);
		
		return;
	}
	
    private int _setupTestPrice(
		boolean usingSet) throws OHException 
	{
		Price price;
		PriceList priceList = testPriceList.setup(true);
		
		
		jpa.beginTransaction();	
		price = testPrice.setup(priceList, usingSet);
		jpa.persist(priceList);
		jpa.persist(price);
		jpa.commitTransaction();
		
		return price.getId();
	}
	
	private void _checkPriceIntoDb(
		int id) throws OHException 
	{
		Price foundPrice;
		
		
		foundPrice = (Price)jpa.find(Price.class, id); 
		testPrice.check(foundPrice);
		testPriceList.check(foundPrice.getList());
		
		return;
	}
	
	private int _getListMax() throws OHException 
	{
		String query = null;
		Integer id = 0;
				

		jpa.beginTransaction();		
		
		try {
			query = "SELECT MAX(LST_ID) FROM PRICELISTS";
			jpa.createQuery(query, null, false);
			id = (Integer)jpa.getResult();
		}  catch (OHException e) {
			System.out.println("==> Test Exception: " + e);
		} 				
	
		jpa.commitTransaction();

		return id;
	}	

	
	private int _getPriceMax() throws OHException 
	{
		String query = null;
		Integer id = 0;
				

		jpa.beginTransaction();		
		
		try {
			query = "SELECT MAX(PRC_ID) FROM PRICES";
			jpa.createQuery(query, null, false);
			id = (Integer)jpa.getResult();
		}  catch (OHException e) {
			System.out.println("==> Test Exception: " + e);
		} 				
	
		jpa.commitTransaction();

		return id;
	}
}