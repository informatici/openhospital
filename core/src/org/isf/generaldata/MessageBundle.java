/*
 * Created on 27/ott/07
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.isf.generaldata;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.isf.utils.db.UTF8Control;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageBundle {
	
	private final static Logger logger = LoggerFactory.getLogger(MessageBundle.class);

	private static ResourceBundle resourceBundle = null;
	
	private static ResourceBundle defaultResourceBundle = null;
	
	public static void initialize() throws RuntimeException {
		try {
			defaultResourceBundle = ResourceBundle.getBundle("language", new Locale("en"));
			resourceBundle = ResourceBundle.getBundle("language", new Locale(GeneralData.LANGUAGE), new UTF8Control());
			JOptionPane.setDefaultLocale(new Locale(GeneralData.LANGUAGE));
		} catch (MissingResourceException e) {
			logger.error(">> no resource bundle found.");
			System.exit(1);
			//throw new RuntimeException("no resource bundle found.");
		}
	}
	
	public static String getMessage(String key) {
		String message = "";
		
		try {
			if (resourceBundle != null) {
				//message = new String(resourceBundle.getString(key).getBytes("ISO-8859-1"), "UTF-8");
				message = resourceBundle.getString(key);
			} else return key;
		} catch (MissingResourceException e) {
			if (GeneralData.DEBUG) {
				message = key;
			} else {
				try {
					message = defaultResourceBundle.getString(key);
				} catch (MissingResourceException e1) {
					message = key;
				}
			}
			logger.error(">> key not found: " + key);
		} 
		return message;
	}
	
	public static String getMessagePattern(String key, Object input) {
		String message = "";
		
		try {
			if (resourceBundle != null) {
				//message = new String(resourceBundle.getString(key).getBytes("ISO-8859-1"), "UTF-8");
				message = resourceBundle.getString(key);
			}
		} catch (MissingResourceException e) {
			if (GeneralData.DEBUG) {
				message = key;
			} else {
				message = defaultResourceBundle.getString(key);
			}
			logger.error(">> key not found: " + key);
		} 
		message = message.replace("#",	input.toString());
		return message;
	}
	
	public static String getMessagePattern(String key, Object[] inputs) {
		String message = "";
		
		try {
			if (resourceBundle != null) {
				//message = new String(resourceBundle.getString(key).getBytes("ISO-8859-1"), "UTF-8");
				message = resourceBundle.getString(key);
			}
		} catch (MissingResourceException e) {
			if (GeneralData.DEBUG) {
				message = key;
			} else {
				message = defaultResourceBundle.getString(key);
			}
			logger.error(">> key not found: " + key);
		} 
		
		for (Object input : inputs) {
			message = message.replaceFirst("#",	input.toString());
		}
		return message;
	}
	
	public static ResourceBundle getBundle(){
		return resourceBundle;
	}
}
