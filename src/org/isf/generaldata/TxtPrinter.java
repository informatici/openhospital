package org.isf.generaldata;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtPrinter {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final String FILE_PROPERTIES = "txtPrinter.properties";

	public static boolean PRINT_AS_PAID;
    public static boolean PRINT_WITHOUT_ASK;
    public static int PAGE_HEIGHT;
    public static int PAGE_WIDTH;
    public static boolean ZPL;
    public static String ZPL_FONT_TYPE;
    public static int ZPL_ROW_HEIGHT;
    
    public static boolean DEFAULT_PRINT_AS_PAID = false;
    public static boolean DEFAULT_PRINT_WITHOUT_ASK = false;
    private static int DEFAULT_PAGE_HEIGHT = 50;
    private static int DEFAULT_PAGE_WIDTH = 47;
    public static boolean DEFAULT_ZPL = false;
    public static String DEFAULT_ZPL_FONT_TYPE = "A";
    public static int DEFAULT_ZPL_ROW_HEIGHT = 9;
    
    private static TxtPrinter mySingleData;
	private Properties p;

    private TxtPrinter() {
    	try	{
			p = new Properties();
			p.load(new FileInputStream("rsc" + File.separator + FILE_PROPERTIES));
			logger.info("File txtPrinter.properties loaded. ");
			PRINT_AS_PAID = myGetProperty("PRINT_AS_PAID", DEFAULT_PRINT_AS_PAID);
			PRINT_WITHOUT_ASK = myGetProperty("PRINT_WITHOUT_ASK", DEFAULT_PRINT_WITHOUT_ASK);
			PAGE_HEIGHT = myGetProperty("PAGE_HEIGHT", DEFAULT_PAGE_HEIGHT);
			PAGE_WIDTH = myGetProperty("PAGE_WIDTH", DEFAULT_PAGE_WIDTH);
			ZPL = myGetProperty("ZPL", DEFAULT_ZPL);
			ZPL_FONT_TYPE = myGetProperty("ZPL_FONT_TYPE", DEFAULT_ZPL_FONT_TYPE);
			ZPL_ROW_HEIGHT = myGetProperty("ZPL_ROW_HEIGHT", DEFAULT_ZPL_ROW_HEIGHT);
			
    	} catch (Exception e) {//no file
    		logger.error(">> " + FILE_PROPERTIES + " file not found.");
			System.exit(1);
		}
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

	/**
	 * 
	 * Method to retrieve a boolean property
	 * 
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	private boolean myGetProperty(String property, boolean defaultValue) {
		boolean value;
		try {
			value = p.getProperty(property).equalsIgnoreCase("YES");
		} catch (Exception e) {
			logger.warn(">> " + property + " property not found: default is " + defaultValue);
			return defaultValue;
		}
		return value;
	}

    public static TxtPrinter getTxtPrinter() {
        if (mySingleData == null){ 
        	mySingleData = new TxtPrinter();        	
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

