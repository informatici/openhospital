package org.isf.stat.manager;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.isf.generaldata.GeneralData;
import org.isf.hospital.manager.HospitalBrowsingManager;
import org.isf.hospital.model.Hospital;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.db.DbSingleConn;
import org.isf.utils.excel.ExcelExporter;

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

	public GenericReportMY(Integer month, Integer year, String jasperFileName, boolean toCSV) {
		try{
			HashMap<String, String> parameters = new HashMap<String, String>();
			HospitalBrowsingManager hospManager = new HospitalBrowsingManager();
			Hospital hosp = hospManager.getHospital();
			
			parameters.put("Hospital", hosp.getDescription());
			parameters.put("Address", hosp.getAddress());
			parameters.put("City", hosp.getCity());
			parameters.put("Email", hosp.getEmail());
			parameters.put("Telephone", hosp.getTelephone());
			parameters.put("year", String.valueOf(year)); // real param
			parameters.put("month", String.valueOf(month)); // real param
		
			StringBuilder sbFilename = new StringBuilder();
			sbFilename.append("rpt");
			sbFilename.append(File.separator);
			sbFilename.append(jasperFileName);
			sbFilename.append(".jasper");

			File jasperFile = new File(sbFilename.toString());

			Connection conn = DbSingleConn.getConnection();
			
			JasperReport jasperReport = (JasperReport)JRLoader.loadObject(jasperFile);
			if (toCSV) {
				JRQuery query = jasperReport.getMainDataset().getQuery();

				String queryString = query.getText();

				queryString = queryString.replace("$P{year}", "'" + String.valueOf(year) + "'");
				queryString = queryString.replace("$P{month}", "'" + String.valueOf(month) + "'");

				DbQueryLogger dbQuery = new DbQueryLogger();
				ResultSet resultSet = dbQuery.getData(queryString, true);
				JFileChooser fcExcel = new JFileChooser();
				FileNameExtensionFilter excelFilter = new FileNameExtensionFilter("CSV (*.csv)","csv");
				fcExcel.setFileFilter(excelFilter);
				fcExcel.setFileSelectionMode(JFileChooser.FILES_ONLY);  
				
				int iRetVal = fcExcel.showSaveDialog(null);
				if(iRetVal == JFileChooser.APPROVE_OPTION)
				{
					File exportFile = fcExcel.getSelectedFile();
					if (!exportFile.getName().endsWith("csv")) exportFile = new File(exportFile.getAbsoluteFile() + ".csv");
					
					ExcelExporter xlsExport = new ExcelExporter();
					xlsExport.exportResultsetToCSV(resultSet, exportFile);
				}
				dbQuery.releaseConnection();
				
			} else {
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
				String PDFfile = "rpt/PDF/"+jasperFileName+"_"+year+"_"+month+".pdf";
				JasperExportManager.exportReportToPdfFile(jasperPrint, PDFfile);
				if (GeneralData.INTERNALVIEWER)
					JasperViewer.viewReport(jasperPrint,false);
				else { 
					try{
						Runtime rt = Runtime.getRuntime();
						rt.exec(GeneralData.VIEWER +" "+ PDFfile);
						
					} catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
