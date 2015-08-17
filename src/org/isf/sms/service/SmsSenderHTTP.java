/**
 * SmsSenderGSM.java - 03/feb/2014
 */
package org.isf.sms.service;

import org.isf.generaldata.SmsParameters;
import org.isf.sms.model.Sms;
import org.isf.sms.providers.SkebbyGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mwithi
 *
 */
public class SmsSenderHTTP implements SmsSenderInterface {

	private static Logger logger = LoggerFactory.getLogger(SmsSenderHTTP.class);
	private SmsSenderInterface smsSender;
	
	/**
	 * 
	 */
	public SmsSenderHTTP() {
		logger.info("SMS Sender HTTP started...");
		SmsParameters.getSmsParameters();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SmsSenderHTTP sender = new SmsSenderHTTP();
		sender.sendSMS(new Sms(), true);
		
	}

	@Override
	public boolean sendSMS(Sms sms, boolean test) {
		return smsSender.sendSMS(sms, test);
	}

	public boolean initialize() {
		String gateway = SmsParameters.GATEWAY;
		if (gateway.equals("")) {
			logger.error("No HTTP Gateway has been set. Please check sms.properties file");
			return false;
		}
		if (gateway.equalsIgnoreCase("Skebby")) {
			smsSender = new SkebbyGateway();
		} else {
			logger.error("HTTP Gateway not found. Please check sms.properties file");
			return false;
		}
		return smsSender.initialize(); 
	}
}
