package org.isf.stat.gui.report;

import net.sf.jasperreports.view.JasperViewer;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.stat.dto.JasperReportResultDto;
import org.isf.stat.manager.JasperReportsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/*--------------------------------------------------------
 * GenericReportLauncerMY - lancia tutti i report che come parametri hanno
 * 							anno e mese
 * 							la classe prevede l'inizializzazione attraverso 
 *                          anno, mese, nome del report (senza .jasper)
 *---------------------------------------------------------
 * modification history
 * 11/11/2006 - prima versione
 *
 *-----------------------------------------------------------------*/

public class GenericReportMY {

    private final Logger logger = LoggerFactory.getLogger(GenericReportMY.class);

	public GenericReportMY(Integer month, Integer year, String jasperFileName, boolean toCSV) {
		try{
            JasperReportsManager jasperReportsManager = new JasperReportsManager();
			if (toCSV) {
                JFileChooser fcExcel = new JFileChooser();
                FileNameExtensionFilter excelFilter = new FileNameExtensionFilter("CSV (*.csv)","csv");
                fcExcel.setFileFilter(excelFilter);
                fcExcel.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int iRetVal = fcExcel.showSaveDialog(null);
                if(iRetVal == JFileChooser.APPROVE_OPTION){
                    File exportFile = fcExcel.getSelectedFile();
                    jasperReportsManager.getGenericReportMYCsv(month,year,jasperFileName,exportFile.getAbsolutePath());
                }
			} else {
                JasperReportResultDto jasperReportResultDto = jasperReportsManager.getGenericReportMYPdf(month,year,jasperFileName);
                if (GeneralData.INTERNALVIEWER)
                    JasperViewer.viewReport(jasperReportResultDto.getJasperPrint(),false);
                else {
                    Runtime rt = Runtime.getRuntime();
                    rt.exec(GeneralData.VIEWER +" "+ jasperReportResultDto.getFilename());
                }
			}
		} catch (Exception e) {
            logger.error("", e);
            JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.stat.reporterror"), MessageBundle.getMessage("angal.hospital"), JOptionPane.ERROR_MESSAGE);
		}
	}

}
