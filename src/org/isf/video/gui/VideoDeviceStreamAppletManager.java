package org.isf.video.gui;

import java.util.ArrayList;

import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.isf.video.manager.VideoDevicesManager;
import org.isf.video.model.VideoDevice;

public class VideoDeviceStreamAppletManager {

	public static int selectedWidth = 0;
	public static int selectedHeight = 0;
	
	public static VideoDeviceStreamApplet currentStreamApplet = null;
	
	public static VideoDeviceStreamApplet checkApplet(VideoDevice videoDevice, 
			int resolutionWidth, int resolutionHeight, 
			int appletWidth, int appletHeight)
	{	
		VideoDeviceStreamApplet newVideoDeviceApplet = new VideoDeviceStreamApplet(videoDevice, resolutionWidth, resolutionHeight, appletWidth, appletHeight);
		
		newVideoDeviceApplet.check();
		
		return newVideoDeviceApplet;
	}
	
	
	public static boolean saveCurrentFrame(VideoDeviceStreamApplet videoDeviceStreamApplet, String path)
	{
		boolean saved = false;
		
		int resW = videoDeviceStreamApplet.resolutionWidth;
		int resH = videoDeviceStreamApplet.resolutionHeight;
		
		if (videoDeviceStreamApplet != null)	{
			//System.out.println("Saving frame in path: " + path);
			videoDeviceStreamApplet.save (path);
			
			try {
				int photoMaxWidth = 200;
				int photoMaxHeight = 160;
				
				Image img = ImageIO.read(new File(path));
				
				int photoW = photoMaxWidth;
				int photoH = photoMaxWidth * resH / resW;
				
				if (photoH > photoMaxHeight)
				{
					photoH = photoMaxHeight;
					photoW = photoMaxHeight * resW / resH;
				}
				
				// utilizziamo il metodo che scala soltanto
				Image patientPhoto = PatientPhotoCreator.createPatientPhoto(img, photoW, photoH);
				
				//Image patientPhoto = PatientPhotoCreator.createPatientPhoto(img, photoW, photoH, photoMaxWidth, photoMaxHeight);
				
			    int type = BufferedImage.TYPE_INT_RGB;
			    BufferedImage bufimg = new BufferedImage(photoW, photoH, type);
			    Graphics2D g2 = bufimg.createGraphics();
			    g2.drawImage(patientPhoto, 0, 0, null);
			    g2.dispose();
				
			    File outputfile = new File(path);
			    ImageIO.write(bufimg, "jpg", outputfile);
			    
			    saved = true;
			}
			catch (IOException ioe)	{
				
			}
		}
		
		return saved;
	}
	

	public static boolean switchApplets(VideoDevice selectedDevice, int resolutionWidth, int resolutionHeight, 
			int appletWidth, int appletHeight)
	{	
		boolean switchOk = false;
		
		String messagePrefix = "";
		
		VideoDeviceStreamApplet prevStreamApplet = null;
		
		if (currentStreamApplet != null)	{
			prevStreamApplet = currentStreamApplet;
		}
		
		VideoDevicesManager.currentVideoDevice = selectedDevice;
		VideoDevicesManager.idCurrentVideoDevice = selectedDevice.id;
		
		messagePrefix = "[Stream Check] ";
		
		if (prevStreamApplet != null) {
			prevStreamApplet.freeze();
			
			if (prevStreamApplet.appletStatus == "init")	{
				prevStreamApplet.stop();
			}
		}
		
		System.out.print (messagePrefix);
		
		currentStreamApplet = checkApplet (VideoDevicesManager.currentVideoDevice, 
				resolutionWidth, resolutionHeight, appletWidth, appletHeight);
		
		if (currentStreamApplet != null)	{
			if(currentStreamApplet.isNowWorking()) {
				
				currentStreamApplet.activate();
				
				switchOk = true;
			}
			else
				System.out.println(messagePrefix + "It wasn't possible to show the selected video device stream at the selected resolution.");
		}
		else
			System.out.println(messagePrefix + "It wasn't possible to show the selected video device stream.");
				
		return switchOk;
	}	
	
	
	
	public static ArrayList<String> checkResolutions(VideoDevice videoDevice)	{
		ArrayList<String> availableResolutions = new ArrayList<String>();
		
		if (currentStreamApplet != null)	{
			currentStreamApplet.freeze();
		}
		
		int[][] resolutions = {{160,120}, {176,144}, {320,240}, {352,288}, {640,480}, {1280,1024}};
		
		for (int[] res : resolutions)	{
			int w = res[0];
			int h = res[1];
			
			System.out.print("[Resolution Check] ");
			
			VideoDeviceStreamApplet applet = checkApplet(videoDevice, w, h, w, h);
			
			System.out.println("done.");
			
			if (applet != null)	{
				if(applet.isNowWorking()) {
					applet.freeze();
					
					System.out.println("[Resolution Check] The video device works well at " + w + "x" + h);
					
					availableResolutions.add(w + "x" + h);					
				}
				else	{
					System.out.println("[Resolution Check] It wasn't possible to show the selected video device stream at the selected resolution.");
				}
			}
			else	{
				System.out.println("[Resolution Check] It wasn't possible to show the selected video device stream.");
			}
		}
		
		// controllo non necessario, ma non si sa mai...
		if (currentStreamApplet != null)	{
			currentStreamApplet.activate();
		}
		
		return availableResolutions;
	}
}
