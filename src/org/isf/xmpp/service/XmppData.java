package org.isf.xmpp.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class XmppData {
	public static String DOMAIN;
	public static int PORT;
	private static XmppData xmppData;

	private XmppData() {
		try {
			Properties p = new Properties();
			p.load(new FileInputStream("rsc" + File.separator + "xmpp.properties"));
			DOMAIN = p.getProperty("DOMAIN");
			PORT = Integer.parseInt(p.getProperty("PORT"));

		} catch (Exception e) {
			DOMAIN = "127.0.0.1";
			PORT = 5222;
		}

	}

	public static XmppData getXmppData() {
		if (xmppData == null) {
			xmppData = new XmppData();
		}
		return xmppData;
	}

}
