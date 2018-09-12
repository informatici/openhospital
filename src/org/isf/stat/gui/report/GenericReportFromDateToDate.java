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
 * GenericReportLauncer2Dates
 *  - lancia tutti i report che come parametri hanno "da data" "a data"
 * 	- la classe prevede l'inizializzazione attraverso 
 *    dadata, adata, nome del report (senza .jasper)
 *---------------------------------------------------------
 * modification history
 * 09/06/2007 - prima versione
 *
 *-----------------------------------------------------------------*/
	public class GenericReportFromDateToDate {

    private final Logger logger = LoggerFactory.getLogger(GenericReportFromDateToDate.class);

		public  GenericReportFromDateToDate(String fromDate, String toDate, String jasperFileName, String defaultName, boolean toExcel) {
			try{
				
				StringBuilder sbFilename = new StringBuilder();
				sbFilename.append("rpt");
				sbFilename.append(File.separator);
				sbFilename.append(jasperFileName);
				sbFilename.append(".jasper");
				File jasperFile = new File(sbFilename.toString());
				
				sbFilename = new StringBuilder();
				sbFilename.append("PDF");
				sbFilename.append(File.separator);
				sbFilename.append(defaultName);
				File defaultFilename = new File(sbFilename.toString());
				
                JasperReportsManager jasperReportsManager = new JasperReportsManager();
				if (toExcel) {
					JFileChooser fcExcel = new JFileChooser();
					FileNameExtensionFilter excelFilter = new FileNameExtensionFilter("Excel (*.xlsx)","xlsx");
					FileNameExtensionFilter excelFilter2003 = new FileNameExtensionFilter("Excel 97-2003 (*.xls)","xls");
					fcExcel.addChoosableFileFilter(excelFilter);
					fcExcel.addChoosableFileFilter(excelFilter2003);
					fcExcel.setFileFilter(excelFilter);
					fcExcel.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fcExcel.setSelectedFile(defaultFilename);

                    int iRetVal = fcExcel.showSaveDialog(null);
                    if(iRetVal == JFileChooser.APPROVE_OPTION)
                    {
                        File exportFile = fcExcel.getSelectedFile();
                        if (!exportFile.getName().endsWith("xls")) exportFile = new File(exportFile.getAbsoluteFile() + ".xls");
                        jasperReportsManager.getGenericReportFromDateToDateExcel(fromDate,toDate, jasperFileName, exportFile.getAbsolutePath());
                    }
                } else {
                    JasperReportResultDto jasperReportResultDto = jasperReportsManager.getGenericReportFromDateToDatePdf(fromDate, toDate, jasperFileName);
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
