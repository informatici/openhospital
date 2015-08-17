package org.isf.video;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import org.isf.video.gui.VideoDeviceStreamApplet;
import org.isf.video.gui.VideoDeviceStreamAppletManager;
import org.isf.video.gui.VideoFrame;

public class Main {
	
	public static void main(String s[]) {
		VideoFrame vf = new VideoFrame(new JDialog());
		vf.init(true);
		vf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				VideoDeviceStreamApplet applet = VideoDeviceStreamAppletManager.currentStreamApplet;
				
				if (applet != null && applet.isNowWorking())	{
					applet.freeze();
				}
				
				System.exit(0);
			}
		});
	}
}
