package org.isf.video.manager;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Hashtable;

import org.isf.video.model.LinuxVideoDevice;
import org.isf.video.model.VideoDevice;
import org.isf.video.service.LinuxShellInterface;

import jcu.sal.config.HwProbeService;

public class LinuxVideoDevicesManager extends VideoDevicesManager {
	HwProbeService hp = HwProbeService.getService();
	
	public LinuxVideoDevicesManager()	{
	}
	
	
	public int updateDeviceList() {
		hp.loadAll();
		
		return getVideoDevicesCount();
	}
	
	
	public void reset(boolean clearDeviceList)	{
		System.out.println("reset linuxVideoDevicesManager...stop!");
		hp.stopAll();
		
		super.reset(clearDeviceList);
	}
	
	
	private Hashtable<String,String> parseDeviceHexId(String devicePath, String deviceId) {
		
		Hashtable<String,String> deviceParams = new Hashtable<String,String>();
		
		if (deviceId != null)	{
			
			String [] deviceIdArray = deviceId.split("/");
		
			String [] vendorAndProductId = deviceIdArray[5].split("_");
			
			// if the digits are less than 4, padding with 0 (zero)
			String vendorId = vendorAndProductId[2];
			for (int i = vendorId.length(); i < 4; i++)
				vendorId = "0" + vendorId;
			
			String productId = vendorAndProductId[3];
			for (int i = productId.length(); i < 4; i++)
				productId = "0" + productId;
			
			deviceParams.put("vendorHexId", vendorId);
			deviceParams.put("productHexId", productId);
			//VideoManager.addDevice(devicePath, vendorId, productId);
		}
		
		return deviceParams;
	}
	
	
	public void setParameters(Hashtable<String,String> params)	{
		
		String devicePath = params.get("devicePath");
		String deviceId = params.get("deviceId");
		
		Hashtable<String,String> deviceParams = new Hashtable<String,String>();
		
		deviceParams = parseDeviceHexId(devicePath, deviceId);
		
		deviceParams.put("devicePath", devicePath);
		
		String vendorHexId = deviceParams.get("vendorHexId");
		String productHexId = deviceParams.get("productHexId");
		
		String [] path = devicePath.split("/");
		String deviceFile = path[path.length-1];
		deviceParams.put("deviceFile", deviceFile);
		
		String[] ids = LinuxShellInterface.getDeviceIdAndBusId(vendorHexId, productHexId);
		String idBus = ids[0];
		String idDevice = ids[1];
		
		deviceParams.put("idBus", idBus);
		deviceParams.put("idDevice", idDevice);
		
		String vendorName = LinuxShellInterface.getVendorName(vendorHexId, productHexId);
		deviceParams.put("vendorName", vendorName);
		
		String productName = LinuxShellInterface.getProductName(vendorHexId, productHexId);
		deviceParams.put("productName", productName);
		deviceParams.put("connectionString", productName);
		
		String manifacturerName = LinuxShellInterface.getManufacturerName(vendorHexId, productHexId);
		deviceParams.put("manifacturerName", manifacturerName);
		
		String productDescription = LinuxShellInterface.getProductDescription(vendorHexId, productHexId);
		deviceParams.put("productDescription", productDescription);
		
		System.out.println("Found device #" + getVideoDevicesCount() + ": " + productName + " on " + devicePath);
		
		addDevice(deviceParams);
	}
	

	public void checkForRemovedVideoDevices()	{
		File dir = new File("/dev");
		
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) { 
				return name.startsWith("video"); 
			}
		};
		
		String [] devicesFound = dir.list(filter);
		
		LinuxVideoDevice removedDevice = null;
		
		for (VideoDevice dev : videoDevicesList.values())	{
			LinuxVideoDevice linuxdev = (LinuxVideoDevice) dev;
			
			boolean found = false;

            for (String s : devicesFound) {
                if (linuxdev.deviceFile.equals(s)) {
                    found = true;
                    break;
                }
            }
			
			if (! found) {
				removedDevice = linuxdev;
				break;
			}
		}
		
		if (removedDevice != null)	{
			removeDevice(removedDevice);
			
			VideoManager.removeDeviceFromGui(removedDevice);
		}
	}
}
