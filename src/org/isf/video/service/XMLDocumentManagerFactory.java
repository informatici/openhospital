package org.isf.video.service;

public abstract class XMLDocumentManagerFactory {
	public static XMLDocumentManagerFactory getXMLDocumentManagerFactory()
	{
		if (System.getProperty("os.name").toLowerCase().contains("linux"))	{
			return new LinuxXMLDocumentManagerFactory();
        }
		else if (System.getProperty("os.name").toLowerCase().contains("windows"))	{
			return new MSWindowsXMLDocumentManagerFactory();
		}
		else
			return null;
	}
	
	public abstract XMLDocumentManager createXMLDocumentManager();
}

class LinuxXMLDocumentManagerFactory extends XMLDocumentManagerFactory {
	
	public XMLDocumentManager createXMLDocumentManager() {
		return new LinuxXMLDocumentManager();
	}
}

class MSWindowsXMLDocumentManagerFactory extends XMLDocumentManagerFactory {
	
	public XMLDocumentManager createXMLDocumentManager() {
		return new MSWindowsXMLDocumentManager();
	}
}
