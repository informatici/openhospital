package org.isf.sms.test;


import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.sms.model.Sms;
import org.isf.sms.service.SmsOperations;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestSms testSms;
	private static TestSmsContext testSmsContext;
		
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testSms = new TestSms();
    	testSmsContext = new TestSmsContext();
    	
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
    	//jpa.destroy();

    	return;
    }
	
	
	@Test
	public void testSmsGets() 
	{
		int code = 0;
			
		
		try 
		{		
			code = _setupTestSms(false);
			_checksmsIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testSmsSets() 
	{
		int code = 0;
			

		try 
		{		
			code = _setupTestSms(true);
			_checksmsIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testSmsSaveOrUpdate() 
	{		
		SmsOperations ioOperations = new SmsOperations();
		boolean result = false;
		
		
		try 
		{		
			Sms sms = testSms.setup(true);
			result = ioOperations.saveOrUpdate(sms);
			
			assertEquals(true, result);
			_checksmsIntoDb(sms.getSmsId());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testSmsGetByID() 
	{	
		SmsOperations ioOperations = new SmsOperations();
		int code = 0;
		
		
		try 
		{		
			code = _setupTestSms(false);
			Sms foundSms = ioOperations.getByID(code);
			
			_checksmsIntoDb(foundSms.getSmsId());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testSmsGetAll() 
	{		
		int code = 0;
		SmsOperations ioOperations = new SmsOperations();
		Date smsDateStart = new GregorianCalendar(2011, 9, 6).getTime();
		Date smsDateEnd = new GregorianCalendar(2011, 9, 9).getTime();
		
		
		try 
		{		
			code = _setupTestSms(false);
			Sms foundSms = (Sms)jpa.find(Sms.class, code); 
			List<Sms> sms = ioOperations.getAll(smsDateStart, smsDateEnd);			
			
			assertEquals(foundSms.getSmsText(), sms.get(0).getSmsText());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testSmsGetList() 
	{	
		int code = 0;
		SmsOperations ioOperations = new SmsOperations();
		 
		
		try 
		{		
			code = _setupTestSms(false);
			Sms foundSms = (Sms)jpa.find(Sms.class, code); 
			List<Sms> sms = ioOperations.getList();			
			
			assertEquals(foundSms.getSmsText(), sms.get(0).getSmsText());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoDeleteSms() 
	{
		int code = 0;
		SmsOperations ioOperations = new SmsOperations();
		

		try 
		{		
			code = _setupTestSms(false);
			Sms foundSms = (Sms)jpa.find(Sms.class, code); 
			ioOperations.delete(foundSms);
			
			Sms deletedSms = (Sms)jpa.find(Sms.class, code); 
			assertEquals(null, deletedSms);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}


	@Test
	public void testIoDeleteByModuleModuleID() 
	{
		int code = 0;
		SmsOperations ioOperations = new SmsOperations();
		

		try 
		{		
			code = _setupTestSms(false);
			Sms foundSms = (Sms)jpa.find(Sms.class, code); 
			ioOperations.deleteByModuleModuleID(
					foundSms.getModule(), 
					foundSms.getModuleID());
			
			Sms deletedSms = (Sms)jpa.find(Sms.class, code); 
			assertEquals(null, deletedSms);
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
		testSmsContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		System.out.println(testSmsContext.getAllSaved());
		testSmsContext.deleteNews(jpa);
        
        return;
    }
    
	private int _setupTestSms(
			boolean usingSet) throws OHException 
	{
		Sms sms;
		
	
		jpa.beginTransaction();	
		sms = testSms.setup(usingSet);
		jpa.persist(sms);
		jpa.commitTransaction();
		
		return sms.getSmsId();
	}
		
	private void  _checksmsIntoDb(
			int code) throws OHException 
	{
		Sms foundSms;
		
	
		foundSms = (Sms)jpa.find(Sms.class, code); 
		testSms.check(foundSms);
		
		return;
	}	
}