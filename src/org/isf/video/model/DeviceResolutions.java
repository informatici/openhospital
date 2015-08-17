package org.isf.video.model;

import java.util.ArrayList;

public class DeviceResolutions {

	private ArrayList<Resolution> resolutions;
	private Resolution defaultResolution;
	
	public DeviceResolutions()
	{
		resolutions = new ArrayList<Resolution>();
	}
	
	public void add(Resolution r)
	{
		resolutions.add(r);
	}

	public int size()
	{
		return resolutions.size();
	}
	
	public Resolution get(int i)
	{
		return resolutions.get(i);
	}

	public Resolution getDefaultResolution()
	{
		return defaultResolution;
	}
	
	public int getDefaultResolutionIndex()
	{
		int i = -1;
		
		if (resolutions != null && resolutions.size() > 0 
			&&  defaultResolution != null && ! defaultResolution.equals(""))
		{
			for (int j=0; j<resolutions.size() && i==-1; j++)
			{
				if (resolutions.get(j).equals(defaultResolution))
				{
					i = j;
					break;
				}
			}
		}
		
		return i;
	}
	
	
	public void setDefaultResolution(Resolution defaultResolution)
	{
		this.defaultResolution = defaultResolution;
	}
	
	
	public void setDefaultResolution(String defaultResolution)
	{
		if (defaultResolution != null)
		{
			String[] c = defaultResolution.split("x");
			
			// se era stata impostata una risoluzione di default
			if (! c[0].equals(""))
			{
				this.defaultResolution = new Resolution(c[0], c[1]);
			}
		}
	}
}
