/*
 * Created on 15/giu/08
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.isf.stat.gui.report;

import javax.swing.JOptionPane;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.generaldata.TxtPrinter;
import org.isf.serviceprinting.manager.PrintReceipt;
import org.isf.stat.dto.JasperReportResultDto;
import org.isf.stat.manager.JasperReportsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.view.JasperViewer;

public class GenericReportBill {

    private final Logger logger = LoggerFactory.getLogger(GenericReportBill.class);

	public GenericReportBill(Integer billID, String jasperFileName) {
		new GenericReportBill(billID, jasperFileName, true, true);
	}

	public GenericReportBill(Integer billID, String jasperFileName, boolean show, boolean askForPrint) {
		
		TxtPrinter.getTxtPrinter();
		
		try {
            JasperReportsManager jasperReportsManager = new JasperReportsManager();
            JasperReportResultDto jasperReportPDFResultDto = jasperReportsManager.getGenericReportBillPdf(billID, jasperFileName, show, askForPrint);

			if (show) {
                if (GeneralData.INTERNALVIEWER) {
                    JasperViewer.viewReport(jasperReportPDFResultDto.getJasperPrint(), false);
                } else {
                    Runtime rt = Runtime.getRuntime();
                    rt.exec(GeneralData.VIEWER + " " + jasperReportPDFResultDto.getFilename());
                }
			}
			
			if (GeneralData.RECEIPTPRINTER) {
				JasperReportResultDto jasperReportTxtResultDto = jasperReportsManager.getGenericReportBillTxt(billID, jasperFileName, show, askForPrint);
				int print = JOptionPane.OK_OPTION;
				if (askForPrint) {
					print = JOptionPane.showConfirmDialog(null, MessageBundle.getMessage("angal.genericreportbill.doyouwanttoprintreceipt"));
				}
				if (print != JOptionPane.OK_OPTION) return; //STOP
				
				if (TxtPrinter.MODE.equals("PDF")) new PrintReceipt(jasperReportPDFResultDto.getJasperPrint(), jasperReportPDFResultDto.getFilename());
				else if (TxtPrinter.MODE.equals("TXT") ||
						TxtPrinter.MODE.equals("ZPL"))
					new PrintReceipt(jasperReportTxtResultDto.getJasperPrint(), jasperReportTxtResultDto.getFilename());
			}
		} catch (Exception e) {
            logger.error("", e);
            JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.stat.reporterror"), MessageBundle.getMessage("angal.hospital"), JOptionPane.ERROR_MESSAGE);
        }
	}
}
