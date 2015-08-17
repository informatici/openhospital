package org.isf.video.manager;

import java.util.Hashtable;

import org.isf.video.model.LinuxVideoDevice;
import org.isf.video.model.MSWindowsVideoDevice;
import org.isf.video.model.VideoDevice;

public abstract class VideoDeviceFactory {

	public static VideoDeviceFactory getVideoDeviceFactory()
	{
		if (System.getProperty("os.name").toLowerCase().contains("linux"))	{
			return new LinuxVideoDeviceFactory();
        }
		else if (System.getProperty("os.name").toLowerCase().contains("windows"))	{
			return new MSWindowsVideoDeviceFactory();
		}
		else
			return null;
	}
	
	public abstract VideoDevice createVideoDevice(int id, Hashtable<String,String> deviceParams);
	
}


class LinuxVideoDeviceFactory extends VideoDeviceFactory {
		
	public LinuxVideoDevice createVideoDevice(int id, Hashtable<String,String> deviceParams) {
		return new LinuxVideoDevice(id, deviceParams);
	}
}


class MSWindowsVideoDeviceFactory extends VideoDeviceFactory {
	
	public MSWindowsVideoDevice createVideoDevice(int id, Hashtable<String,String> deviceParams) {
		return new MSWindowsVideoDevice(id, deviceParams);
	}
}
