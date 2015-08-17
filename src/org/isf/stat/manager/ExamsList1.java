package org.isf.stat.manager;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.isf.generaldata.GeneralData;
import org.isf.hospital.manager.HospitalBrowsingManager;
import org.isf.hospital.model.Hospital;
import org.isf.utils.db.DbSingleConn;

public class ExamsList1 {

	public ExamsList1() {
		try{
			HashMap<String, String> parameters = new HashMap<String, String>();
			HospitalBrowsingManager hospMan = new HospitalBrowsingManager();
			Hospital hospital = hospMan.getHospital();
		
			parameters.put("hospital", hospital.getDescription());
		
			String jasperFileName = "examslist";
			StringBuilder sbFilename = new StringBuilder();
			sbFilename.append("rpt");
			sbFilename.append(File.separator);
			sbFilename.append(jasperFileName);
			sbFilename.append(".jasper");
			
			StringBuilder pdfFilename = new StringBuilder();
			pdfFilename.append("rpt");
			pdfFilename.append(File.separator);
			pdfFilename.append("PDF");
			pdfFilename.append(File.separator);
			pdfFilename.append(jasperFileName);
			pdfFilename.append(".pdf");
			
			File jasperFile = new File(sbFilename.toString());
			
			Connection conn = DbSingleConn.getConnection();

			JasperReport jasperReport = (JasperReport)JRLoader.loadObject(jasperFile);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFilename.toString());
			
			//mostra a video 
			if (GeneralData.INTERNALVIEWER)
				JasperViewer.viewReport(jasperPrint,false);
			else { 
				try{
					Runtime rt = Runtime.getRuntime();
					rt.exec(GeneralData.VIEWER +" "+ pdfFilename.toString());
					
				} catch(Exception e){
					e.printStackTrace();
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
