package org.isf.video.service;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult; 

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.isf.video.manager.VideoDevicesManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import org.xml.sax.SAXException;

public abstract class XMLDocumentManager {
	
	private DocumentBuilder docBuilder;
	
	protected Document doc;
	protected Element root;
	
	public XMLDocumentManager () {
		try {
			File file = new File(VideoDevicesManager.resolutionsFilePath);
			
			//Create instance of DocumentBuilderFactory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
			//Get the DocumentBuilder
			docBuilder = factory.newDocumentBuilder();
			
			// se il file esiste
			if (file.exists())	{
				doc = docBuilder.parse(file);
				doc.getDocumentElement().normalize();
				
				root = getRoot();
				
				// se la radice non esiste
				if (root == null)	{
					root = createRoot();
					saveFile();
				}
			}
			else {
				//Create blank DOM Document
				doc = docBuilder.newDocument();
				
				root = createRoot();
				
				saveFile();
			}
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
		catch(ParserConfigurationException e) {
			System.out.println(e.getMessage());
		}
		catch(SAXException e) {
			//Create blank DOM Document
			doc = docBuilder.newDocument();
			
			root = createRoot();
		}
	}
	
	protected abstract String getIdentificationAttributeName();
	
	protected abstract String getOsAttributeValue();

	
	private Element getRoot()	{
		// c'è un solo nodo radice che verrà memorizzato in roots.item(0)
		NodeList roots = doc.getElementsByTagName("root");
		Element root = null;
		
		if ((roots != null)	&&	(roots.getLength() > 0))	{	
			root = (Element)roots.item(0);
		}
		
		return root;
	}
	
	
	private Element createRoot()	{
		//create the root element
		root = doc.createElement("root");
		
		//all it to the xml tree
		doc.appendChild(root);
		
		return root;
	}


	public void writeResolutions(String deviceId, ArrayList<String> resolutions)	{
		Element deviceElement = writeDeviceElement(deviceId);
		
		String os = getOsAttributeValue();
		
		for (String res : resolutions)	{
			Element resolutionElement = doc.createElement("Resolution");
			resolutionElement.setAttribute("os", os);
			
			Text text = doc.createTextNode(res);
			resolutionElement.appendChild(text);
			
			deviceElement.appendChild(resolutionElement);
			System.out.println("[XMLdebug] Adding: <resolution," + os + "," + res + ">");
		}
		
		saveFile();
	}
	
	
	private Element writeDeviceElement(String deviceId)	{
		Element deviceElement = getDeviceElement(deviceId);
		
		if (deviceElement == null)	{
			deviceElement = doc.createElement("Device");
			System.out.println("[XMLdebug] Adding: <device, " + deviceId + ">");
		}
		// altrimenti se esiste già modifica o aggiungi il valore dell'attributo id
		
		deviceElement.setAttribute(getIdentificationAttributeName(), deviceId);
		
		root.appendChild(deviceElement);
		
		return deviceElement;
	}
	
	
	protected Element getDeviceElement(String deviceId) {
		NodeList deviceNodeList = doc.getElementsByTagName("Device");
		Element foundDeviceElement = null;
		
		System.out.println("[XMLdebug] Device da cercare: " + deviceId);
		
		for (int s = 0; s < deviceNodeList.getLength(); s++)	{
			Element deviceElement = (Element) deviceNodeList.item(s);
			
			String identificationAttributeName = getIdentificationAttributeName();	// id for linux, description for win
			
			String currentId = deviceElement.getAttribute(identificationAttributeName);
			
			//System.out.println("[XMLdebug] Ricerca device, id corrente: " + currentId);
			
			if (currentId.equals(deviceId))	{
				System.out.println("[XMLdebug] ...trovato!");
				foundDeviceElement = deviceElement;
			}
		}
		
		return foundDeviceElement;
	}
	
	
	public ArrayList<String> readResolutionsForCurrentOs(String deviceId)	{
		ArrayList<String> resolutions = null;
		
		Element deviceElement = getDeviceElement(deviceId);
		if (deviceElement != null)	{
			NodeList resolutionNodeList = deviceElement.getElementsByTagName("Resolution");
			
			resolutions = getOsResolutions(resolutionNodeList);
		}
		
		return resolutions;
	}
	
	
	private ArrayList<String> getOsResolutions (NodeList resolutionNodeList)	{
		ArrayList<String> resolutions = new ArrayList<String>();
		
		String os = getOsAttributeValue();
		
		for (int i = 0; i < resolutionNodeList.getLength(); i++)	{
			// get current node <Resolution os="..">WIDTHxHEIGHT</Resolution>
			Node resTagElement = resolutionNodeList.item(i);

			// get the first (and only) attribute
			Node attribute = resTagElement.getAttributes().getNamedItem("os");
			String attrValue = attribute.getNodeValue();
			
			// verify if the attribute is "os" and his value 
			if (attrValue.equals(os)	||	attrValue.equals("all"))	{
				//System.out.println("Resolution : "	+ attrValue);
			
				// get the resolution and add it to the ArrayList
				Node resTextElement = resTagElement.getChildNodes().item(0);
				resolutions.add(resTextElement.getTextContent());
			}
			//else
			//	System.out.println(attributes.item(0).getNodeValue());
		}
		
		return resolutions;
	}

	
	private Element getDefaultResolution  (NodeList resolutionNodeList)	{
		
		String os = getOsAttributeValue();
		
		for (int i = 0; i < resolutionNodeList.getLength(); i++)	{
			// get current node <Resolution os="..">WIDTHxHEIGHT</Resolution>
			Node resTagElement = resolutionNodeList.item(i);

			Node osAttribute = resTagElement.getAttributes().getNamedItem("os");
			String osAttrValue = osAttribute.getNodeValue();
			
			// verify if the attribute is "os" and his value 
			if (osAttrValue.equals(os)	||	osAttrValue.equals("all"))
			{
				Node defaultAttribute = resTagElement.getAttributes().getNamedItem("default");
				if (defaultAttribute != null)
				{
					String defaultAttrValue = defaultAttribute.getNodeValue();
					if (defaultAttrValue.equals("true"))
					{
						return (Element)resTagElement;
					}
				}
			}
			//else
			//	System.out.println(attributes.item(0).getNodeValue());
		}
		
		return null;
	}
	
	
	
	private Element getOsResolutionElement (int width, int height, NodeList resolutionNodeList)	{
		String os = getOsAttributeValue();
		
		String res = width + "x" + height;
		
		for (int i = 0; i < resolutionNodeList.getLength(); i++)	{
			// get current node <Resolution os="..">WIDTHxHEIGHT</Resolution>
			Node resTagElement = resolutionNodeList.item(i);
			
			Node attribute = resTagElement.getAttributes().getNamedItem("os");
			String attrValue = attribute.getNodeValue();
			
			if (attrValue.equals(os)	||	attrValue.equals("all"))
			{
				Node resTextElement = resTagElement.getChildNodes().item(0);
				if (resTextElement.getTextContent().equals(res))
				{
					System.out.println("Found resolution: " + res);  
					return (Element)resTagElement;
				}
			}
			//else
			//	System.out.println(attributes.item(0).getNodeValue());
		}
		
		System.out.println("NOT Found resolution: " + res);  
		return null;
	}
	
	
	public void removeResolutionsForCurrentOs(String deviceId)	{
		Element deviceElement = getDeviceElement(deviceId);
		NodeList resolutionNodeList = deviceElement.getElementsByTagName("Resolution");
		
		String os = getOsAttributeValue();
							
		int nres = resolutionNodeList.getLength();
		//System.out.println("Nodi:" + nres);
		
		int deletedNodes = 0;
		
		for (int i = 0; i < nres; i++)	{
			// get current node <Resolution os="..">WIDTHxHEIGHT</Resolution>
			Node resTagElement = resolutionNodeList.item(i - deletedNodes);

			Node attribute = resTagElement.getAttributes().getNamedItem("os");
			String attrValue = attribute.getNodeValue();
			
			if (attrValue.equals(os))	{
				deviceElement.removeChild(resTagElement);
				System.out.println("[XMLdebug] Removing: <os, " + attrValue + "," + resTagElement.getTextContent() + ">");		
				deletedNodes++;
			}
			else if (attrValue.equals("all"))	{
				String otherOs = (os == "win")	?	"linux"	:	"win";
				((Element)resTagElement).setAttribute("os", otherOs);
				System.out.println("[XMLdebug] Changing: <os, " + attrValue + "," + resTagElement.getTextContent() + "> to " + otherOs);
			}
		}
		
		saveFile();
	}
	
	
	public String getDefaultResolutionForDevice(String deviceId)
	{
		String defRes = null;
		
		Element deviceElement = getDeviceElement(deviceId);
		if (deviceElement != null)
		{
			NodeList resolutionNodeList = deviceElement.getElementsByTagName("Resolution");
			
			Element defaultResolutionElement = getDefaultResolution(resolutionNodeList);
			
			if (defaultResolutionElement != null)
				defRes = defaultResolutionElement.getTextContent();
		}
		
		return defRes;
	}
	
	public boolean setDefaultResolutionForDevice(int width, int height, String deviceId)
	{
		Element deviceElement = getDeviceElement(deviceId);
		NodeList resolutionNodeList = deviceElement.getElementsByTagName("Resolution");
		
		Element previousDefaultResolution = getDefaultResolution(resolutionNodeList);
		
		if (previousDefaultResolution != null)
			previousDefaultResolution.removeAttribute("default");
		
		Element resolutionElement = getOsResolutionElement(width, height, resolutionNodeList);
		if (resolutionElement != null)
		{
			resolutionElement.setAttribute("default", "true");
			saveFile();
			System.out.println("Set default resolution: " + width + "x" + height);
			return true;
		}
		
		System.out.println("Trying to set default resolution: " + width + "x" + height + " (not found)");
		return false;
	}
	
	
	protected void saveFile()	{
		File file = new File(VideoDevicesManager.resolutionsFilePath);
		
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer(); 
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			Source src = new DOMSource(doc);
			Result dest = new StreamResult(file);
			
			transformer.transform(src, dest);
		}
		catch(TransformerException e) {
			System.out.println(e.getMessage());
		}
	}
}
