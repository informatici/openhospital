package org.isf.utils.exception.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;

public class OHServiceExceptionUtil {

	private OHServiceExceptionUtil() {}
	
	/**
	 * Iterate exception messages and show them in a JOptionPane with parentComponent = null
	 * @param e
	 */
	public static void showMessages(OHServiceException e) {
		showMessages(e, null);
	}
	
	/**
	 * Iterate exception messages and show them in a JOptionPane with parentComponent specified
	 * @param e
	 * @param parentComponent
	 */
	public static void showMessages(OHServiceException e, Component parentComponent) {
		if (null != e.getMessages()) {
			for(OHExceptionMessage msg : e.getMessages()){
				String title = msg.getTitle();
				String message = msg.getMessage();
				
				if (null == title) {
					title = "";
				} else {
					title = msg.getTitle();
				}
				int messageType = msg.getLevel().getSwingSeverity();
				JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
			}
		}
	}
}
