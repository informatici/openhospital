package org.isf.video.model;

public class Resolution
{
	public int width;
	public int height;
	
	Resolution(int w, int h) {
		width = w;
		height = h;
	}
	
	Resolution(String w, String h) {
		width = Integer.parseInt(w);
		height = Integer.parseInt(h);
	}

	public String toString()
	{
		return width + "x" + height;
	}
	
	
	public boolean equals(Resolution res)
	{
		return (width == res.width && height == res.height);
	}
}