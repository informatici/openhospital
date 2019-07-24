package org.isf.stat.gui.report;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.stat.dto.JasperReportResultDto;
import org.isf.stat.manager.JasperReportsManager;
import org.isf.utils.excel.ExcelExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.view.JasperViewer;

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

	public GenericReportMY(Integer month, Integer year, String jasperFileName, String defaultName, boolean toExcel) {
		try{
            JasperReportsManager jasperReportsManager = new JasperReportsManager();
            File defaultFilename = new File(jasperReportsManager.compileDefaultFilename(defaultName));
			
			if (toExcel) {
				JFileChooser fcExcel = ExcelExporter.getJFileChooserExcel(defaultFilename);

                int iRetVal = fcExcel.showSaveDialog(null);
                if(iRetVal == JFileChooser.APPROVE_OPTION)
                {
                    File exportFile = fcExcel.getSelectedFile();
                    FileNameExtensionFilter selectedFilter = (FileNameExtensionFilter) fcExcel.getFileFilter();
					String extension = selectedFilter.getExtensions()[0];
					if (!exportFile.getName().endsWith(extension)) exportFile = new File(exportFile.getAbsoluteFile() + "." + extension);
                    jasperReportsManager.getGenericReportMYExcel(month,year,jasperFileName,exportFile.getAbsolutePath());
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
