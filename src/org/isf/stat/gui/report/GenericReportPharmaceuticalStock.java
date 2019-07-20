/*
 * Created on 15/giu/08
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.isf.stat.gui.report;

import java.io.File;
import java.util.Date;

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

public class GenericReportPharmaceuticalStock {
	
	private final Logger logger = LoggerFactory.getLogger(GenericReportPharmaceuticalStock.class);

	public GenericReportPharmaceuticalStock(Date date, String jasperFileName, String filter, String groupBy, String sortBy, boolean toExcel) {
		try{
            JasperReportsManager jasperReportsManager = new JasperReportsManager();
            File defaultFilename = new File(jasperReportsManager.compileDefaultFilename(jasperFileName));
            
            if (toExcel) {
				JFileChooser fcExcel = ExcelExporter.getJFileChooserExcel(defaultFilename);

                int iRetVal = fcExcel.showSaveDialog(null);
                if(iRetVal == JFileChooser.APPROVE_OPTION)
                {
                    File exportFile = fcExcel.getSelectedFile();
                    FileNameExtensionFilter selectedFilter = (FileNameExtensionFilter) fcExcel.getFileFilter();
					String extension = selectedFilter.getExtensions()[0];
					if (!exportFile.getName().endsWith(extension)) exportFile = new File(exportFile.getAbsoluteFile() + "." + extension);
                    jasperReportsManager.getGenericReportPharmaceuticalStockExcel(date, jasperFileName, exportFile.getAbsolutePath(), filter, groupBy, sortBy);
                }
            } else {
                JasperReportResultDto jasperReportResultDto = jasperReportsManager.getGenericReportPharmaceuticalStockPdf(date, jasperFileName, filter, groupBy, sortBy);
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
