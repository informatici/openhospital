package org.isf.video.manager;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;

public class PatientPhotoCreator {
	
	/**
	 * Resizes the image to the specified dimesions
	 * @param img
	 * @param photoW
	 * @param photoH
	 * @return
	 */
	public static Image createPatientPhoto(Image img, int photoW, int photoH)
	{	
		Image patientPhoto = img.getScaledInstance(photoW, photoH, Image.SCALE_SMOOTH);
		
		return patientPhoto;
	}
	
	/**
	 * Resizes the image to the specified dimesions starting from position
	 * @param img
	 * @param photoW
	 * @param photoH
	 * @param photoFinalW
	 * @param photoFinalH
	 * @return
	 */
	public static Image createPatientPhoto(Image img, int photoW, int photoH, int photoFinalW, int photoFinalH)
	{	
		Image patientPhoto = null;
		
		int startX = 0;
		int startY = 0;
		
		if (photoH > photoFinalH)	{
			startY = (photoH - photoFinalH) / 2;
			System.out.println("photoW=photoFinalW (" + photoW + "), " +
					"photoH>photoFinalH ("+photoH+">"+photoFinalH+"), startY=" + startY);
		}
		else if (photoH < photoFinalH)	{
			
			if (photoW > photoFinalW)	{
				startX = (photoW - photoFinalW) / 2;
				System.out.println("photoW>photoFinalW ("+photoW+">"+photoFinalW+"); " +
						"photoH=photoFinalH ("+photoH+"); startX=" + startX);
			}
			else {
				System.out.println("photoW<=photoFinalW ("+photoW+"<"+photoFinalW+"); " +
						"photoH=photoFinalH ("+photoH+"); strano!");
			}
		}
		
		Image scaledImg = img.getScaledInstance(photoW, photoH, Image.SCALE_SMOOTH);
		
		ImageFilter filt = new CropImageFilter(startX, startY, photoFinalW, photoFinalH);
		ImageProducer pro = new FilteredImageSource(scaledImg.getSource(),filt);
		patientPhoto = Toolkit.getDefaultToolkit().createImage(pro);
		
		return patientPhoto;
	}
}
