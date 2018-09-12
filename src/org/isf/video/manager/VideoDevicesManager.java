package org.isf.video.manager;

import java.util.ArrayList;
import java.util.Hashtable;

import org.isf.video.model.VideoDevice;
import org.isf.video.manager.VideoDeviceFactory;

public abstract class VideoDevicesManager {
	
	// It contains the ID of the next device what will be connected
	// It is used to guarantee the devices uniqueness and it's incremented every time
	// a new device is plugged in
	public static int progressiveDevicesIdCount = 0;
	
	public static int idCurrentVideoDevice = -1;
	
	public static VideoDevice currentVideoDevice = null;
	
	protected static Hashtable<Integer, VideoDevice> videoDevicesList = new Hashtable<Integer, VideoDevice>();
	
	public static String resolutionsFilePath = "rsc/resolutions.xml";
	
	private static VideoDeviceFactory videoDeviceFactory = VideoDeviceFactory.getVideoDeviceFactory();
	
	// Abstract Methods	
	public abstract int updateDeviceList();
	
	public abstract void setParameters(Hashtable<String,String> params);
	
	public abstract void checkForRemovedVideoDevices();
	
	public VideoDevice getVideoDevice(int id)	{
		 return videoDevicesList.get(id);
	}
	
	
	public int getVideoDevicesCount()	{
		 return videoDevicesList.size();
	}
	
	
	public VideoDevice getFirstVideoDevice() {
		ArrayList<Integer> id_set = new ArrayList<Integer>(videoDevicesList.keySet());
		
		return videoDevicesList.get(id_set.get(0));
	}
	
	public VideoDevice getNextVideoDevice(int id){
		// Returns the next device in the list
		ArrayList<Integer> id_set = new ArrayList<Integer>(videoDevicesList.keySet());
		System.out.println(videoDevicesList.toString());
		int index = id_set.indexOf(id);
		
		System.out.println("trovato in pos " + index);
		
		VideoDevice device = null;
		// If the ID exists in the list
		if (index > -1)
		{
			// If the ID is not last in the list, returns the next one
			if (index < videoDevicesList.size() - 1) {
				device = videoDevicesList.get(id_set.get(index + 1));
			}
			// Otherwise return the first one
			else {
				device = videoDevicesList.get(id_set.get(0));
			}
		}
			
		return device;
	}
	
	
	public void addDevice(Hashtable<String,String> deviceParams) {
		int newId = progressiveDevicesIdCount;
		
		// creates new videoDevice
		VideoDevice videoDevice = videoDeviceFactory.createVideoDevice(newId, deviceParams);
		
		// add the video device to the video devices manager	
		videoDevicesList.put(newId, videoDevice);
		
		progressiveDevicesIdCount++;
		
		System.out.println("Added new VideoDevice object, id: " + newId);
		
		VideoManager.addDeviceToGui(videoDevice);
	}
	
	
	public void removeDevice(VideoDevice d)	{
		videoDevicesList.remove(d);
	}
	
	
	public void reset(boolean clearDeviceList)	{
		System.out.println("reset abstract videoDevicesManager");
		
		if (clearDeviceList)
		{
			videoDevicesList.clear();
			
			progressiveDevicesIdCount = 0;
		}
	}
}
