package org.isf.dicom.test;

import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.isf.dicom.gui.DicomGui;
import org.isf.generaldata.GeneralData;
import org.isf.patient.model.Patient;




/**
 * Util Class for DICOM interface test
 *
 * @author Pietro Castellucci
 *
 */

public class DicomMain
{

    /**
     * a test main method
     * @param args
     */
    public static void main(String[] args)
    {	
    	PropertyConfigurator.configure(new File("./rsc/log4j.properties").getAbsolutePath());

//       Session s1 =  HybernateSessions.getSession("dicom.h8");
//       System.out.println(s1);
//       Session s2 =  HybernateSessions.getSession("dicom.h8.properties");
//       System.out.println(s2);
    	
        GeneralData.getGeneralData();
        Patient p = new Patient();
        p.setCode(new Integer(1));
        p.setFirstName("Nome");
        p.setSecondName("Cognome");
        p.setSex('M');
        p.setPhoto(new BufferedImage(200,200,BufferedImage.TYPE_INT_ARGB));
        disegnaPaz(p.getPhoto().getGraphics());
        new DicomGui(p, null);

    }

    private static void disegnaPaz(java.awt.Graphics g)
    {
        g.setColor(java.awt.Color.YELLOW);
        g.drawString("FOTO PAZIENTE", 5, 100);
    }

}
