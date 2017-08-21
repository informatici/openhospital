package org.isf.utils.exception;

import java.util.ArrayList;
import java.util.List;

import org.isf.utils.exception.model.OHExceptionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * OH service layer exception. Can encapsulate messages to show in UI. 
 * @author akashytsa
 *
 */
public class OHServiceException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Logger logger = LoggerFactory.getLogger(OHServiceException.class);
	private List<OHExceptionMessage> messages = new ArrayList<OHExceptionMessage>();
	
	public OHServiceException(Throwable cause, List<OHExceptionMessage> messages) {
		super(cause);
		this.messages = messages;
	}
	
	public OHServiceException(OHExceptionMessage message) {
		this.messages.add(message);
	}
	
	public OHServiceException(List<OHExceptionMessage> messages) {
		this.messages = messages;
	}
	
	public OHServiceException(Throwable cause, OHExceptionMessage message) {
		super(cause);
		this.messages.add(message);
	}

	public List<OHExceptionMessage> getMessages() {
		return messages;
	}
}
