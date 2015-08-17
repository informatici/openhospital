package org.isf.video.manager;

import org.isf.video.manager.VideoDevicesManager;

public abstract class VideoDevicesManagerFactory {

	public static VideoDevicesManagerFactory getVideoDevicesManagerFactory()
	{
		if (System.getProperty("os.name").toLowerCase().contains("linux"))	{
			return new LinuxVideoDevicesManagerFactory();
        }
		else if (System.getProperty("os.name").toLowerCase().contains("windows"))	{
			return new MSWindowsVideoDevicesManagerFactory();
		}
		else
			return null;
	}
	
	public abstract VideoDevicesManager createVideoDevicesManager();
	
}

class LinuxVideoDevicesManagerFactory extends VideoDevicesManagerFactory {
	
	public LinuxVideoDevicesManager createVideoDevicesManager() {
		return new LinuxVideoDevicesManager();
	}
}

class MSWindowsVideoDevicesManagerFactory extends VideoDevicesManagerFactory {
	
	public MSWindowsVideoDevicesManager createVideoDevicesManager() {
		return new MSWindowsVideoDevicesManager();
	}
}
