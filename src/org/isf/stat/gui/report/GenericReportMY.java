package org.isf.stat.gui.report;

import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.stat.dto.JasperReportResultDto;
import org.isf.stat.manager.JasperReportsManager;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.excel.ExcelExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.sql.ResultSet;

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

	public GenericReportMY(Integer month, Integer year, String jasperFileName, String defaultFileName, boolean toExcel) {
		try{
            JasperReportsManager jasperReportsManager = new JasperReportsManager();
            
            StringBuilder sbFilename = new StringBuilder();
			sbFilename.append("rpt");
			sbFilename.append(File.separator);
			sbFilename.append(jasperFileName);
			sbFilename.append(".jasper");
			File jasperFile = new File(sbFilename.toString());
			
			sbFilename = new StringBuilder();
			sbFilename.append("rpt");
			sbFilename.append(File.separator);
			sbFilename.append(defaultFileName);
			File defaultFile = new File(sbFilename.toString());
			
			if (toExcel) {
                JFileChooser fcExcel = new JFileChooser();
                FileNameExtensionFilter excelFilter = new FileNameExtensionFilter("Excel (*.xlsx)","xlsx");
                FileNameExtensionFilter excelFilter2003 = new FileNameExtensionFilter("Excel 97-2003 (*.xls)","xls");
				fcExcel.addChoosableFileFilter(excelFilter);
				fcExcel.addChoosableFileFilter(excelFilter2003);
                fcExcel.setFileFilter(excelFilter);
                fcExcel.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fcExcel.setSelectedFile(defaultFile);

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
