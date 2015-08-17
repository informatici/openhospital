package org.isf.video.service;

public class LinuxXMLDocumentManager extends XMLDocumentManager{
	
	protected String identificationAttribute = "id";
	
	protected String os = "linux";
	
	protected String getIdentificationAttributeName()	{
		return identificationAttribute;
	}
	
	protected String getOsAttributeValue()	{
		return os;
	}
}
