/*
 * Created on 15/giu/08
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.isf.stat.manager;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.hospital.manager.HospitalBrowsingManager;
import org.isf.hospital.model.Hospital;
import org.isf.serviceprinting.manager.PrintReceipt;
import org.isf.utils.db.DbSingleConn;

public class GenericReportBill {
	
	public GenericReportBill(Integer billID, String jasperFileName) {
		new GenericReportBill(billID, jasperFileName, true, true);
	}

	public GenericReportBill(Integer billID, String jasperFileName, boolean show, boolean askForPrint) {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			HospitalBrowsingManager hospManager = new HospitalBrowsingManager();
			Hospital hosp = hospManager.getHospital();

			parameters.put("Hospital", hosp.getDescription());
			parameters.put("Address", hosp.getAddress());
			parameters.put("City", hosp.getCity());
			parameters.put("Email", hosp.getEmail());
			parameters.put("Telephone", hosp.getTelephone());
			parameters.put("billID", String.valueOf(billID)); // real param

			StringBuilder sbFilename = new StringBuilder();
			sbFilename.append("rpt");
			sbFilename.append(File.separator);
			sbFilename.append(jasperFileName);
			sbFilename.append(".jasper");
			// System.out.println("Jasper Report Name:"+sbFilename.toString());

			File jasperFile = new File(sbFilename.toString());

			Connection conn = DbSingleConn.getConnection();

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			String PDFfile = "rpt/PDF/" + jasperFileName + "_" + String.valueOf(billID) + ".pdf";
			JasperExportManager.exportReportToPdfFile(jasperPrint, PDFfile);

			if (show) {
				if (GeneralData.INTERNALVIEWER) {
	
					JasperViewer.viewReport(jasperPrint, false);
				} else {
					try {
						Runtime rt = Runtime.getRuntime();
						rt.exec(GeneralData.VIEWER + " " + PDFfile);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			if (GeneralData.RECEIPTPRINTER) {
				
				sbFilename = new StringBuilder();
				sbFilename.append("rpt");
				sbFilename.append(File.separator);
				sbFilename.append(jasperFileName);
				sbFilename.append("Txt");
				sbFilename.append(".jasper");
				//System.out.println("Jasper Report Name:"+sbFilename.toString());

				jasperFile = new File(sbFilename.toString());
				
				jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
				jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
				
				String TXTfile = "rpt/PDF/" + jasperFileName + "_" + String.valueOf(billID) + ".txt";
				
				int print = JOptionPane.OK_OPTION;
				if (askForPrint) {
					print = JOptionPane.showConfirmDialog(null, MessageBundle.getMessage("angal.genericreportbill.doyouwanttoprintreceipt"));
				}
				if (print == JOptionPane.OK_OPTION) {
					new PrintReceipt(jasperPrint, TXTfile);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
