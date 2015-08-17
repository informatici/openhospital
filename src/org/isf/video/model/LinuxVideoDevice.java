package org.isf.video.model;

import java.util.ArrayList;
import java.util.Hashtable;

import org.isf.video.gui.VideoDeviceStreamAppletManager;
import org.isf.video.manager.VideoManager;
import org.isf.video.service.LinuxShellInterface;

public class LinuxVideoDevice extends VideoDevice {
	
	public String idBus;
	public String idDevice;
	
	public String deviceFile;
	public String devicePath;
	
	public String vendorHexId;
	public String productHexId;
		
	
	public LinuxVideoDevice(int id, Hashtable<String,String> params)	{
		super(id, params);
	}
	
	
	protected void init(int id, Hashtable<String, String> params) {
		this.id = id;
		
		idBus = params.get("idBus");
		idDevice = params.get("idDevice");
		
		deviceFile = params.get("deviceFile");
		devicePath = params.get("devicePath");
		
		vendorHexId = params.get("vendorHexId");
		productHexId = params.get("productHexId");
		
		vendorName = params.get("vendorName");
		productName = params.get("productName");
		manifacturerName = params.get("manifacturerName");
		productDescription = params.get("productDescription");
		
		connectionString = params.get("devicePath");
		deviceIdentificationString = vendorHexId + ":" + productHexId;
	}

	
	public void checkResolutions(boolean ignoreFile)	{
		ArrayList<String> resolutions = null;
		String defaultResolution = null;
		
		if (ignoreFile == false)	{
			resolutions = getResolutionsFromFile();
			defaultResolution = getDefaultResolution();
		}
		else	{
			removeResolutionsFromFile();
		}
		
		// se nel file non erano memorizzate risoluzioni per questo device
		if ((resolutions == null) || (resolutions.size() == 0))	{
			resolutions = LinuxShellInterface.getResolutions(vendorHexId, productHexId);
			
			// se non è stato possibile recuperare le risoluzioni dalle informazioni del sistema operativo
			if ((resolutions == null) || (resolutions.size() == 0))
				resolutions = VideoDeviceStreamAppletManager.checkResolutions(this);
			
			VideoManager.saveResolutionInFile(deviceIdentificationString, resolutions);
		}
		
		if ((resolutions != null)	&&	(resolutions.size() > 0)) {
			addResolutions(resolutions, defaultResolution);
		}
	}
}
