package org.isf.video.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.isf.video.gui.VideoDeviceStreamApplet;
import org.isf.video.gui.VideoDeviceStreamAppletManager;
import org.isf.video.gui.VideoFrame;
import org.isf.video.model.VideoDevice;
import org.isf.video.service.XMLDocumentManager;
import org.isf.video.service.XMLDocumentManagerFactory;

public class VideoManager {
	
	private static VideoDevicesManager videoDevicesManager = VideoDevicesManagerFactory.getVideoDevicesManagerFactory().createVideoDevicesManager();
	
	private static XMLDocumentManager xmlDocManager = XMLDocumentManagerFactory.getXMLDocumentManagerFactory().createXMLDocumentManager();
	
	public static VideoFrame frame = null;
	
	public static String shotPhotosTempDir = "";
	public static String shotPhotosExtension = ".jpg";
	
	public static void init (VideoFrame vf)	{
		frame = vf;
		init();
	}
	
	public static void init()	{
		shotPhotosTempDir = System.getProperty("java.io.tmpdir");
		// aggiunge lo slash finale se manca
		if ( !(shotPhotosTempDir.endsWith("/") || shotPhotosTempDir.endsWith("\\")) )
			shotPhotosTempDir += System.getProperty("file.separator");
	}
	
	public static int getVideoDevicesCount() {
		return videoDevicesManager.getVideoDevicesCount();
	}
	
	
	public static VideoDevice getVideoDevice(int id)	{
		return videoDevicesManager.getVideoDevice(id);
	}
	
	
	public static VideoDevice getFirstVideoDevice()	{
		return videoDevicesManager.getFirstVideoDevice();
	}

	
	public static VideoDevice getNextVideoDevice(int id)	{
		return videoDevicesManager.getNextVideoDevice(id);
	}
	
	
	public static void addDeviceToGui(VideoDevice videoDevice) {
		if (frame != null)	{
			frame.addDevice(videoDevice);
			
			boolean ignoreFile = false;
			frame.checkResolutions(videoDevice, ignoreFile);	
		}
	}
	
	
	public static void removeDeviceFromGui(VideoDevice videoDevice) {
		if (frame != null)	{
			frame.removeDevice(videoDevice);
		}
	}
	
	
	public static int updateDeviceList() {
		int videoDevicesCount = videoDevicesManager.updateDeviceList();
		
		System.out.println("Updated device list, found " + videoDevicesCount + " devices");
		
		return videoDevicesCount;
	}
	
	
	public static void removeAllResolutionsFromGui(){
		if (frame != null)	{
			frame.removeAllResolutions();
		}
	}
	
	public static void saveResolutionInFile(String deviceId, ArrayList<String> resolutions) {
		xmlDocManager.writeResolutions(deviceId, resolutions);
	}
	
	
	public static void removeResolutionFromFile(String deviceId)	{
		xmlDocManager.removeResolutionsForCurrentOs(deviceId);
	}
	
	
	public static ArrayList<String> loadResolutionFromFile(String deviceId) {
		ArrayList<String> resolutions = xmlDocManager.readResolutionsForCurrentOs(deviceId);
		
		return resolutions;
	}
	
	
	public static boolean setCurrentResolutionAsDefault(int width, int height, String deviceId)  {
		return xmlDocManager.setDefaultResolutionForDevice(width, height, deviceId);
	}
	
	
	public static void reset(boolean clearDeviceList)	{
		System.out.println("reset videomanager");
		videoDevicesManager.reset(clearDeviceList);
	}


	public static String getDefaultResolutionFromFile(String deviceId) {
		String defRes = xmlDocManager.getDefaultResolutionForDevice(deviceId);
		
		return defRes;
	}
	
	public static String savePhoto (VideoDeviceStreamApplet applet, String filename)
	{
		String extension = VideoManager.shotPhotosExtension;
		String generatedFileName = "";
		
		String path = "";
		
		try {
			// crea un file temporaneo per la foto scattata
			File tempFile = File.createTempFile(filename, extension);
			tempFile.deleteOnExit();
			
			// il metodo createTempFile aggiunge un suffisso numerico al nome del file,
			// ci interessa conoscere quindi il nome reale del file creato per passarlo
			// a saveCurrentFrame
			generatedFileName = tempFile.getName().replace(extension, "");
			
			String tempDir = VideoManager.shotPhotosTempDir;
			
			path = tempDir + generatedFileName + extension;
			
			VideoDeviceStreamAppletManager.saveCurrentFrame(applet, path);
		}
		catch(IOException ioe) {
			
		}
		
		return generatedFileName;
	}
}