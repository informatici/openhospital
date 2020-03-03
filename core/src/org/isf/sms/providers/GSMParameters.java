package org.isf.sms.providers;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GSMParameters {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final String FILE_PROPERTIES = "GSM.properties";

    public static String PORT;
    private static String DEFAULT_PORT = "";
    
    public static String DRIVERNAME;
    private static String DEFAULT_DRIVERNAME = "com.sun.comm.Win32Driver";
    
    public static String CMGF;
    private static String DEFAULT_CMGF = "AT+CMGF=1\r";
    
    public static String CSMP;
    private static String DEFAULT_CSMP = "AT+CSMP=17,167,0,0\r";
    
    public static String CMGS;
    private static String DEFAULT_CMGS = "AT+CMGS=\"";
    
    private static GSMParameters mySingleData;
	private Properties p;

    private GSMParameters() {
    	try	{
			p = new Properties();
			p.load(new FileInputStream("rsc" + File.separator + "SmsGateway" + File.separator + FILE_PROPERTIES));
			//logger.info("File " + FILE_PROPERTIES + " loaded. ");
			PORT = myGetProperty("PORT", DEFAULT_PORT);
			DRIVERNAME = myGetProperty("DRIVERNAME", DEFAULT_DRIVERNAME);
			CMGF = myGetProperty("CMGF", DEFAULT_CMGF);
			CSMP = myGetProperty("CSMP", DEFAULT_CSMP);
			CMGS = myGetProperty("CMGS", DEFAULT_CMGS);
			
    	} catch (Exception e) {//no file
    		logger.error(">> " + FILE_PROPERTIES + " file not found.");
    		System.exit(1);
		}
    }
    
    public static GSMParameters getGSMParameters() {
        if (mySingleData == null){ 
        	mySingleData = new GSMParameters();        	
        }
        return mySingleData;
    }
    
    /**
	 * 
	 * Method to retrieve a string property
	 * 
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	private String myGetProperty(String property, String defaultValue) {
		String value;
		value = p.getProperty(property);
		if (value == null) {
			logger.warn(">> " + property + " property not found: default is " + defaultValue);
			return defaultValue;
		}
		return value;
	}
}

