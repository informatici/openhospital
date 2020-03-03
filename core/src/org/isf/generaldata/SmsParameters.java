package org.isf.generaldata;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsParameters {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final String FILE_PROPERTIES = "sms.properties";

	public static String MODE;
	private static String DEFAULT_MODE = "GSM";
	
	public static String GATEWAY;
	private static String DEFAULT_GATEWAY = "";
	
	public static int TIMEOUT;
	private static int DEFAULT_TIMEOUT = 3000;
	
    public static int LOOP;
    private static int DEFAULT_LOOP = 300;
    
    public static String ICC;
    private static String DEFAULT_ICC = "";
    
    private static SmsParameters mySingleData;
	private Properties p;

    private SmsParameters() {
    	try	{
			p = new Properties();
			p.load(new FileInputStream("rsc" + File.separator + FILE_PROPERTIES));
			//logger.info("File " + FILE_PROPERTIES + " loaded. ");
			MODE = myGetProperty("MODE", DEFAULT_MODE);
			GATEWAY = myGetProperty("GATEWAY", DEFAULT_GATEWAY);
			TIMEOUT = myGetProperty("TIMEOUT", DEFAULT_TIMEOUT);
			LOOP = myGetProperty("LOOP", DEFAULT_LOOP);
			ICC = myGetProperty("ICC", DEFAULT_ICC);
			
    	} catch (Exception e) {//no file
    		logger.error(">> " + FILE_PROPERTIES + " file not found.");
    		System.exit(1);
		}
    }
    
    public static SmsParameters getSmsParameters() {
        if (mySingleData == null){ 
        	mySingleData = new SmsParameters();        	
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
	
	/**
     * Method to retrieve an integer property
     * 
     * @param property
     * @param defaultValue
     * @return
     */
    private int myGetProperty(String property, int defaultValue) {
    	int value;
		try {
			value = Integer.parseInt(p.getProperty(property));
		} catch (Exception e) {
			logger.warn(">> " + property + " property not found: default is " + defaultValue);
			return defaultValue;
		}
		return value;
	}
}

