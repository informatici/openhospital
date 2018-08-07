package org.isf.utils.exception.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.isf.utils.exception.OHException;

public class OHExceptionUtil {

	private OHExceptionUtil() {}
	
	/**
	 * Show exception messages in a JOptionPane with parentComponent = null
	 * @param e
	 */
	public static void showMessage(OHException e) {
		showMessage(e, null);
	}
	
	/**
	 * Show exception message in a JOptionPane with parentComponent specified
	 * @param e
	 * @param parentComponent
	 */
	public static void showMessage(OHException e, Component parentComponent) {
		if (null != e.getMessage()) {
			String message = e.getMessage();				
			String title = null;
			if (null == title) {
				title = "";
			}
			
			int messageType = JOptionPane.INFORMATION_MESSAGE;
			JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
		}
	}
}
