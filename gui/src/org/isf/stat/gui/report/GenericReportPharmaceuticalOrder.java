/*
 * Created on 15/giu/08
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.isf.stat.gui.report;

import java.util.Locale;

import javax.swing.JOptionPane;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.stat.dto.JasperReportResultDto;
import org.isf.stat.manager.JasperReportsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.view.JasperViewer;

public class GenericReportPharmaceuticalOrder {

    private final Logger logger = LoggerFactory.getLogger(GenericReportPharmaceuticalOrder.class);
    private JasperReportsManager jasperReportsManager = Context.getApplicationContext().getBean(JasperReportsManager.class);

	public GenericReportPharmaceuticalOrder(String jasperFileName) {
        try{
            JasperReportResultDto jasperReportResultDto = jasperReportsManager.getGenericReportPharmaceuticalOrderPdf(jasperFileName);
            if (GeneralData.INTERNALVIEWER)
                JasperViewer.viewReport(jasperReportResultDto.getJasperPrint(),false, new Locale(GeneralData.LANGUAGE));
            else {
                Runtime rt = Runtime.getRuntime();
                rt.exec(GeneralData.VIEWER +" "+ jasperReportResultDto.getFilename());
            }
        } catch (Exception e) {
            logger.error("", e);
            JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.stat.reporterror"), MessageBundle.getMessage("angal.hospital"), JOptionPane.ERROR_MESSAGE);
        }
	}
	
}
