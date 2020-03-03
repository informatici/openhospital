package org.isf.serviceprinting.manager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.hospital.manager.HospitalBrowsingManager;
import org.isf.hospital.model.Hospital;
import org.isf.utils.exception.OHServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

@Component
public class PrintManager {
	public static final int toDisplay = 0;

	public static final int toPdf = 1;

	public static final int toPrint = 2;
	
	@Autowired
	private HospitalBrowsingManager hospitalManager;
	
	public PrintManager() {}
	
	public void print(String filename, List<?> toPrint, int action) throws OHServiceException {
		
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		Hospital hospital = hospitalManager.getHospital();
		parameters.put("ospedaleNome", hospital.getDescription());
		parameters.put("ospedaleIndirizzo", hospital.getAddress());
		parameters.put("ospedaleCitta", hospital.getCity());
		parameters.put("ospedaleTel", hospital.getTelephone());
		parameters.put("ospedaleFax", hospital.getFax());
		parameters.put("ospedaleMail", hospital.getEmail());

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(toPrint);
		File jasperFile = new File("rpt/" + filename + ".jasper");
		try {
			if (jasperFile.isFile()) {
				JasperReport jasperReport = (JasperReport) JRLoader
				.loadObject(jasperFile);
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperReport, parameters, dataSource);
				switch (action) {
				case 0:
					if (GeneralData.INTERNALVIEWER)
						JasperViewer.viewReport(jasperPrint,false, new Locale(GeneralData.LANGUAGE));
					else { 
						String PDFfile = "rpt/PDF/" + filename + ".pdf";
						JasperExportManager.exportReportToPdfFile(jasperPrint, PDFfile);
						try{
							Runtime rt = Runtime.getRuntime();
							rt.exec(GeneralData.VIEWER +" "+ PDFfile);
						} catch(Exception e){
							e.printStackTrace();
						}
					}
					break;
				case 1:
					JasperExportManager.exportReportToPdfFile(jasperPrint,"rpt/PDF/"+JOptionPane.showInputDialog(null,MessageBundle.getMessage("angal.serviceprinting.selectapathforthepdffile"),filename)+".pdf");
					break;
				case 2:JasperPrintManager.printReport(jasperPrint, true);
					break;
				default:JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.serviceprinting.selectacorrectaction"));
					break;
				}
			}else JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.serviceprinting.notavalidfile"));
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
