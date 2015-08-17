package org.isf.video.manager;

import java.util.ArrayList;
import java.util.Hashtable;

import org.isf.video.model.VideoDevice;
import org.isf.video.manager.VideoDeviceFactory;

public abstract class VideoDevicesManager {
	
	// contiene l'id del prossimo device che verrà inserito (serve per garantire l'unicità degli stessi),
	// viene incrementato ad ogni inserimento di un device
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
		// ritorna il device successivo nella lista
		ArrayList<Integer> id_set = new ArrayList<Integer>(videoDevicesList.keySet());
		System.out.println(videoDevicesList.toString());
		int index = id_set.indexOf(id);
		
		System.out.println("trovato in pos " + index);
		
		VideoDevice device = null;
		// se l'id esiste nella lista
		if (index > -1)
		{
			// se l'id non è dell'ultimo device della lista
			// ritorna il successivo
			if (index < videoDevicesList.size() - 1) {
				device = videoDevicesList.get(id_set.get(index + 1));
			}
			// altrimenti ritorna il primo
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
