package org.isf.sms.providers;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mwithi
 *
 */
public class SkebbyParameters {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final String FILE_PROPERTIES = "Skebby.properties";

	public static String URL;
    private static String DEFAULT_URL = "";
    
    public static String USR;
    private static String DEFAULT_USR = "";
    
    public static String PWD;
    private static String DEFAULT_PWD = "";
    
    public static String TYPE;
    private static String DEFAULT_TYPE = "send_sms_basic";
    
    public static String SENDER_NUMBER;
    private static String DEFAULT_SENDER_NUMBER = "";
    
    public static String SENDER_STRING;
    private static String DEFAULT_SENDER_STRING = "";
    
    private static SkebbyParameters mySingleData;
	private Properties p;

    private SkebbyParameters() {
    	try	{
			p = new Properties();
			p.load(new FileInputStream("rsc" + File.separator + "SmsGateway" + File.separator + FILE_PROPERTIES));
			//logger.info("File " + FILE_PROPERTIES + " loaded. ");
			URL = myGetProperty("URL", DEFAULT_URL);
			USR = myGetProperty("USR", DEFAULT_USR);
			PWD = myGetProperty("PWD", DEFAULT_PWD);
			TYPE = myGetProperty("TYPE", DEFAULT_TYPE);
			SENDER_NUMBER = myGetProperty("SENDER_NUMBER", DEFAULT_SENDER_NUMBER);
			SENDER_STRING = myGetProperty("SENDER_STRING", DEFAULT_SENDER_STRING);
			
    	} catch (Exception e) {//no file
    		logger.error(">> " + FILE_PROPERTIES + " file not found.");
    		System.exit(1);
		}
    }
    
    public static SkebbyParameters getSkebbyParameters() {
        if (mySingleData == null){ 
        	mySingleData = new SkebbyParameters();        	
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

