package org.isf.utils.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OHException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Logger logger = LoggerFactory.getLogger(OHException.class);
	
	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public OHException(String message, Throwable cause) {
		super(message, cause);
		if (logger.isErrorEnabled()) {
			logger.error(">> EXCEPTION: " + sanitize(message));
			logger.error(">> " + cause);
		}
	}
	
	/**
	 * 
	 * @param message
	 */
	public OHException(String message) {
		super(message);
		if (logger.isErrorEnabled()) {
			logger.error(">> EXCEPTION: " + sanitize(message));
		}
	}
	
	/**
	 * Sanitize the given {@link String} value. 
	 * 
	 * @param value the value to sanitize.
	 * @return the sanitized value or <code>null</code> if the passed value is <code>null</code>.
	 */
	protected String sanitize(String value)
	{
		if (value == null) return null;
		return value.trim().replaceAll("'", "''");
	}
}
