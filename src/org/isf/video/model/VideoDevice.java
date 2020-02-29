package org.isf.video.model;

import java.util.ArrayList;
import java.util.Hashtable;

import org.isf.video.manager.VideoManager;

public abstract class VideoDevice {
	public int id;
	
	public String vendorName;
	public String productName;
	public String manifacturerName;
	public String productDescription;
	
	public String connectionString;
	public String deviceIdentificationString;
	
	DeviceResolutions resolutions = new DeviceResolutions();
	
	public VideoDevice (int id, Hashtable<String,String> params)	{
		init(id, params);
	}
	
	protected abstract void init(int id, Hashtable<String, String> params);
	
	public abstract void checkResolutions(boolean ignoreFile);
	
	protected String getDefaultResolution() {
		String res = VideoManager.getDefaultResolutionFromFile(deviceIdentificationString);
		//String c[] = res.split("x");
		//return new Resolution(c[0], c[1]);
		return res;
	}
	
	public void addResolution (String resolutionString)	{
		//System.out.println(resolutionString);
		
		String [] res = resolutionString.split("x");
		
		resolutions.add(new Resolution(res[0], res[1]));
	}
	
	
	public void addResolutions (ArrayList<String> resolutionsStringArray,
			String defaultResolution) {
		
		if (resolutionsStringArray != null && resolutions != null)
		{
			int nres = resolutionsStringArray.size();

            for (String s : resolutionsStringArray) {
                String[] res = s.split("x");

                resolutions.add(new Resolution(res[0], res[1]));

                //System.out.println(res[0] + "x" + res[1]);
            }
			
			if (defaultResolution != null)
			{
				resolutions.setDefaultResolution(defaultResolution);
			}
		}
	}
	
	
	public ArrayList<String> getResolutionsFromFile()	{
		return VideoManager.loadResolutionFromFile(deviceIdentificationString);
	}
	
	
	public void removeResolutionsFromFile()	{
		VideoManager.removeResolutionFromFile(deviceIdentificationString);
	}
	
	
	public int getResolutionWidth(int i)	{
		if (i < resolutions.size())
			return resolutions.get(i).width;
		else
			return 0;
	}
	
	
	public int getResolutionHeight(int i)	{
		if (i < resolutions.size())
			return resolutions.get(i).height;
		else
			return 0;
	}
	
	
	public Resolution getPreviousResolution(int width, int height)	{
		
		if (resolutions.get(0).equals(new Resolution(width,height)))
		{
			return null;
		}
		
		int size = resolutions.size();
		
		for (int i = 1; i<size; i++)
		{
			if (resolutions.get(i).equals(new Resolution(width,height)))
			{
				return resolutions.get(i-1);
			}
		}
		
		return null;
	}
	
	
	public Resolution getNextResolution(int width, int height)	{
		int size = resolutions.size();
		
		for (int i = 0; i<size; i++)
		{
			if (resolutions.get(i).equals(new Resolution(width,height)))
			{
				// If not reached the end
				if (i < size - 1)
					return resolutions.get(i+1);
			}
		}
		
		return null;
	}
	
	
	public int getAvailableResolutionsCount()	{
		int n = 0;
		
		if (resolutions != null)
			n = resolutions.size();
		
		return n;
	}
	
	public int getDefaultResolutionIndex()
	{
		if (resolutions != null)
			return resolutions.getDefaultResolutionIndex();
		else
			return -1;
	}
}
