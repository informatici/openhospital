package org.isf.video.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LinuxShellInterface {

	private static String execute(String command)
	{
		String outputLine = "";
		
		try	{
			Process p;
			BufferedReader in;
			
			String[] cmd = {"","",""};
			cmd[0]= "/bin/sh";
			cmd[1]= "-c";
			cmd[2] = command;
			
			p = Runtime.getRuntime().exec(cmd);
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			String line = in.readLine();
			
			while (line != null) {
				outputLine += line;
				line = in.readLine();
				
				if (line != null)
					outputLine += "\n";
			}
			
			//System.out.println("cmd[2] = " + cmd[2]);
			//System.out.println("outputLine = " + outputLine);
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		return outputLine;
	}
	
	
	public static String[] getDeviceIdAndBusId(String vendorHexId, String productHexId)	{
		String[] ids = {"",""};
		
		//Stringcommand =	"lsusb | grep " + vendorHexId + ":" + productHexId;
		String command =	"lsusb | grep " + vendorHexId + ":" + productHexId + "| "
			+	" awk '{printf (\"%s:%s\",substr($2,0,3),substr($4,0,3))}'";
		
		String idBusDeviceLine = execute(command);
		
		/*
		String[] lsusbCurrentDeviceHeader = in.readLine().split(" ");
		String idBus = lsusbCurrentDeviceHeader[1].substring(0, 3);
		String idDevice = lsusbCurrentDeviceHeader[3].substring(0, 3);
		*/
		
		//System.out.println(command);
		
		String[] lsusbCurrentDeviceHeader = idBusDeviceLine.split(":");
		String idBus = lsusbCurrentDeviceHeader[0];
		String idDevice = lsusbCurrentDeviceHeader[1];
		
		//System.out.println("idBus = " + idBus + ", idDevice = " + idDevice);
		
		ids[0] = idBus;
		ids[1] = idDevice;
		
		return ids;
	}
	
	//public static String getVendorName(String idBus, String idDevice, String idVendorCode)
	public static String getVendorName(String vendorHexId, String productHexId)	{
		String vendorName = "";
		
		//String command = "lsusb -vs " + idBus + ":" + idDevice + "| grep idVendor";
		String command = "lsusb -vd " + vendorHexId + ":" + productHexId + "| grep idVendor";
		String vendorLine = execute(command);
		
		String[] vendorLineArray = vendorLine.split(vendorHexId);
		vendorName = vendorLineArray[1].trim();
			
		//System.out.println("vendorName = " + vendorName);
		
		return vendorName;
	}
	

	//public static String getProductName(String idBus, String idDevice, String idProductCode)
	public static String getProductName(String vendorHexId, String productHexId)	{
		String productName = "";
		
		//String command = "lsusb -vs " + idBus + ":" + idDevice + "| grep idProduct";
		String command = "lsusb -vd " + vendorHexId + ":" + productHexId + "| grep idProduct";
		String productLine = execute(command);
		
		String[] idProductLineArray = productLine.split(productHexId);
		productName = idProductLineArray[1].trim();
		
		//System.out.println("productName = " + productName);
		
		return productName;
	}

	
	//public static String getManufacturerName(String idBus, String idDevice)
	public static String getManufacturerName(String vendorHexId, String productHexId)	{
		String manufacturerName = "";
		
		//String command = "lsusb -vs " + idBus + ":" + idDevice + "| grep iManufacturer";
		// FS = stringa separatrice
		String command = "lsusb -vd " + vendorHexId + ":" + productHexId + "| grep iManufacturer | awk 'BEGIN { FS = \" 1 \"}; {print $2}'";
		String manufacturerLine = execute(command);
		
		manufacturerName = manufacturerLine;
		
		/*String[] iManifacturerLineArray = manifacturerLine.split(" 1 ");
		if (iManifacturerLineArray.length == 2)
			manifacturerName = iManifacturerLineArray[1].trim();
		*/
		//System.out.println("manifacturerName = " + manufacturerName);
		
		return manufacturerName;
	}
	
	
	//public static String getProductDescription(String idBus, String idDevice)
	public static String getProductDescription(String vendorHexId, String productHexId)	{
		String productDescription = "";
		
		//String command = "lsusb -vs " + idBus + ":" + idDevice + "| grep iProduct";
		// FS = string separator
		String command = "lsusb -vd " + vendorHexId + ":" + productHexId + "| grep iProduct | awk 'BEGIN { FS = \" 1 \"}; {print $2}'";
		String productLine = execute(command);
		
		productDescription = productLine;
		
		/*String[] iProductLineArray = productLine.split(" 1 ");
		if (iProductLineArray.length == 2)
			productDescription = iProductLineArray[1].trim();
		*/
		//System.out.println("productDescription = " + productDescription);
		
		return productDescription;
	}
	
	
	public static ArrayList<String> getResolutions(String vendorHexId, String productHexId)	{
		String widthCommand = "lsusb -vd " + vendorHexId + ":" + productHexId + " | grep 'wWidth(' | awk '{print $3}'";
		//System.out.println(widthCommand);
		String widthLines = execute(widthCommand);
		
		String heightCommand = "lsusb -vd " + vendorHexId + ":" + productHexId + " | grep 'wHeight(' | awk '{print $3}'";
		String heightLines = execute(heightCommand);
		
		List<String> widthArray = Arrays.asList(widthLines.split("\n"));
		List<String> heightArray = Arrays.asList(heightLines.split("\n"));
		
		int nres = widthArray.size();
		
		ArrayList<String> resolutions = new ArrayList<String>();
		
		int k=0;
		
		for (int i=0; i<nres; i++)	{
			String w = widthArray.get(i);
			
			String h = heightArray.get(i);
			
			if (w != "" && h != "")	{
				// Padding with 0 (zeros) useful for sorting
				for (int j = w.length(); j < 4; j++)
					w = "0" + w;
				
				String res = w + "x" + h;
				
				resolutions.add(res);
				
				k++;
				
				//System.out.println(res);
			}
		}
		
		if (k > 0)	{
			Collections.sort(resolutions);
			
			// Removes starting 0 (zero)
			for (int i=0; i<k-1; i++)	{
				resolutions.set(i, resolutions.get(i).replaceAll("^0*", ""));
			}
		}
		
		return resolutions;
	}
}
