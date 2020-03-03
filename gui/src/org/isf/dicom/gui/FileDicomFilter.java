package org.isf.dicom.gui;

import java.io.File;

import javax.swing.filechooser.FileView;
  
/**
 * Filter for file DICOM
 * @author Pietro Castellucci
 * @version 1.0.0
 */
public class FileDicomFilter
        extends javax.swing.filechooser.FileFilter
{
     /** 
     * Whether the given file is accepted by this filter.
     */
    public boolean accept(File f)
    {

        return  (f.getName().toLowerCase().endsWith(".dcm") ||
                 f.isDirectory()||
                (f.getName().indexOf(".")<0));
    }

    /**
     * The description of this filter. For example: "JPG and GIF Images"
     * @see FileView#getName
     */
    public String getDescription()
    {
        return "DICOM Images";
    }
}
