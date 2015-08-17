package org.isf.video.manager;

import java.util.Hashtable;

import codeanticode.gsvideo.GSCapture;

public class MSWindowsVideoDevicesManager extends VideoDevicesManager {
	
	public MSWindowsVideoDevicesManager()	{
	}
	
	
	public int updateDeviceList() {
		String [] devices = GSCapture.list();
	
		for (String d : devices)
		{
			Hashtable<String,String> params = new Hashtable<String,String>(); 
			
			//System.out.println(d);
			params.put("productName", d);
			
			setParameters(params);
		}
		
		return getVideoDevicesCount();
	}
	
	
	public void reset(boolean clearDeviceList)	{
		super.reset(clearDeviceList);
	}
	
	
	public void setParameters(Hashtable<String,String> params)	{
		Hashtable<String,String> deviceParams = new Hashtable<String,String>();
		
		String productName = params.get("productName");
		
		deviceParams.put("productName", productName);
		
		deviceParams.put("connectionString", productName);
		
		System.out.println("Found device #" + getVideoDevicesCount() + ": " + productName);
		
		addDevice(deviceParams);
	}
	
	
	public void checkForRemovedVideoDevices()	{
		
	}
}
