package org.isf.video.service;

public class MSWindowsXMLDocumentManager extends XMLDocumentManager {

	protected String identificationAttribute = "description";
	
	protected String os = "win";
	
	protected String getIdentificationAttributeName()	{
		return identificationAttribute;
	}
	
	protected String getOsAttributeValue()	{
		return os;
	}
}
