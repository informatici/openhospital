package org.isf.video.model;

import java.util.ArrayList;
import java.util.Hashtable;

import org.isf.video.gui.VideoDeviceStreamAppletManager;
import org.isf.video.manager.VideoManager;

public class MSWindowsVideoDevice extends VideoDevice {	
	
	public MSWindowsVideoDevice(int id, Hashtable<String,String> params)	{
		super(id, params);
	}
	
	
	protected void init(int id, Hashtable<String, String> params) {
		this.id = id;
		
		productName = params.get("productName");
		
		connectionString = productName;
		
		deviceIdentificationString = productName;
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
		if ((resolutions == null)	||	(resolutions.size() == 0))	{			
			resolutions = VideoDeviceStreamAppletManager.checkResolutions(this);
			
			VideoManager.saveResolutionInFile(deviceIdentificationString, resolutions);
		}
		
		if ((resolutions != null)	&&	(resolutions.size() > 0)) {
			addResolutions(resolutions, defaultResolution);
		}
	}
}
