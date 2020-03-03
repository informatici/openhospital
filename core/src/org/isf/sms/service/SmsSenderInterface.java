/**
 * SmsSenderInterface.java - 03/feb/2014
 */
package org.isf.sms.service;

import org.isf.sms.model.Sms;

/**
 * 
 * @author Mwithi
 */
public interface SmsSenderInterface {
	
	/**
	 * Public method to send one {@link Sms}
	 * @param sms - the {@link Sms} to send
	 * @param debug - if <code>true</code> the method should not really send the sms (for debug)
	 * @return <code>true</code> if the SMS has been sent, <code>false</code> otherwise
	 */
    boolean sendSMS(Sms sms, boolean debug);
	
	/**
	 * Public method to initialize the SmsSender.
	 * For GSM Sender it could check if the device is ready.
	 * For HTTP Sender it could check if the URL is reachable
	 * @return
	 */
    boolean initialize();
	
}
