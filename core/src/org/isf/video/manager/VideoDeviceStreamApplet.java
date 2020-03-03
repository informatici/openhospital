package org.isf.video.manager;

import org.isf.video.model.VideoDevice;

import codeanticode.gsvideo.GSCapture;
import processing.core.PApplet;

public class VideoDeviceStreamApplet extends PApplet	{
	private static final long serialVersionUID = 1L;
	
	private VideoDevice videoDevice;
	public GSCapture videoDeviceStream;
	
	public int resolutionWidth;
	public int resolutionHeight;
	
	public int appletWidth;
	public int appletHeight;
	
	public String appletStatus;
	
	public VideoDeviceStreamApplet(VideoDevice videoDevice, int resWidth, int resHeight, int appletWidth, int appletHeight)	{
		this.resolutionWidth = resWidth;
		this.resolutionHeight = resHeight;
		
		this.appletWidth = appletWidth;
		this.appletHeight = appletHeight;
		
		this.videoDevice = videoDevice;
		this.videoDeviceStream = null;
	}
	
	
	public void setup()	{
		try	{
			size (appletWidth, appletHeight);
			
			videoDeviceStream = new GSCapture(this, resolutionWidth, resolutionHeight, 
					videoDevice.connectionString, 15);
			
			//System.out.println("Setup applet: res "+resolutionWidth+"x"+resolutionHeight
			//		+	", dim "+appletWidth+"x"+appletHeight);
		}
		catch(Exception e)	{
			e.printStackTrace();
		}
	}

	public void draw()	{
		if (videoDeviceStream != null && videoDeviceStream.available())	{
			videoDeviceStream.read();
			
			image(videoDeviceStream, 0, 0, appletWidth, appletHeight);
		}
	}
	
	
	public boolean isNowWorking()	{
		return ((videoDeviceStream != null)	&&	(videoDeviceStream.newFrame()));
	}
	
	
	public void freeze()	{
		if (videoDeviceStream != null)
			videoDeviceStream.setState("null");
	}
	
	
	public void activate()	{
		if (videoDeviceStream != null)
			videoDeviceStream.setState("play");
	}
	

	public void check()	{
		int id = videoDevice.id;
		String productName = videoDevice.productName;
		
		System.out.print("Checking video device #" + id + " (" + productName + ") at " 
					+	resolutionWidth + "x" + resolutionHeight + "...") ;
		
		appletStatus = "init";
		
		try	{
			init();
			Thread.sleep(5000);
		}
		catch(InterruptedException ie)	{
			System.out.println(ie);
		}
		catch(Exception e)	{
			System.out.println(e);
		}
		
		appletStatus = "stop";
		
		System.out.println("done.");
	}
}
