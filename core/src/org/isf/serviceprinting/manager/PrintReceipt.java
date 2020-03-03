package org.isf.serviceprinting.manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.isf.generaldata.TxtPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;

/**
 * 
 * @author Mwithi
 * 
 *         This class will read generic/text printer parameters and compile and
 *         print given jasper report. A copy will be at given file path
 * 
 */
public class PrintReceipt {
	
	private PrintService defaultPrintService;
	private final Logger logger = LoggerFactory.getLogger(PrintReceipt.class);

	/**
	 * @param jasperFileName
	 * @param parameters
	 * @param conn
	 */
	public PrintReceipt(JasperPrint jasperPrint, String fileName) {
				
		TxtPrinter.getTxtPrinter();
		
		try {
			defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
			if (defaultPrintService != null) {
				if (TxtPrinter.MODE.equalsIgnoreCase("ZPL")) {
					
					JRTextExporter exporter = new JRTextExporter();
					exporter.setParameter(JRTextExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRTextExporterParameter.OUTPUT_FILE_NAME, fileName);
					exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, TxtPrinter.TXT_CHAR_WIDTH);
					exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, TxtPrinter.TXT_CHAR_HEIGHT);
					exporter.exportReport();
					
					printFileZPL(fileName, !TxtPrinter.USE_DEFAULT_PRINTER);
					
				} else if (TxtPrinter.MODE.equalsIgnoreCase("TXT")) {
						
					if (jasperPrint.getPages().size() > 1) {
						printReversPages(jasperPrint);
					} else {
						JasperPrintManager.printReport(jasperPrint, !TxtPrinter.USE_DEFAULT_PRINTER);
					}
					
				} else if (TxtPrinter.MODE.equalsIgnoreCase("PDF")) {
					
					if (jasperPrint.getPages().size() > 1) {
						printReversPages(jasperPrint);
					} else {
						JasperPrintManager.printReport(jasperPrint, !TxtPrinter.USE_DEFAULT_PRINTER);
					}

				} else {
					logger.debug("invalid MODE");
					logger.debug("MODE: " + TxtPrinter.MODE);
				}
			} else {
				logger.debug("printer was not found.");
				logger.debug(defaultPrintService.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param file
	 * @param showDialog
	 */
	private void printFileZPL(String file, boolean showDialog) {
		try {
			PrintService printService;
			if (showDialog) {
				PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
				DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
			    PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, pras);
			    PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
			    printService = ServiceUI.printDialog(null, 200, 200, printServices, defaultService, flavor, pras);
			} else {
				printService = defaultPrintService;
			}
			if (printService == null) return;
			getPrinterDetails(printService);
			DocPrintJob job = printService.createPrintJob();
			DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
			PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
			DocAttributeSet das = new HashDocAttributeSet();
			
			FileReader frStream = new FileReader(file);
			BufferedReader brStream = new BufferedReader(frStream);
			
			int charH = TxtPrinter.ZPL_ROW_HEIGHT;
			String font = "^A" + TxtPrinter.ZPL_FONT_TYPE;
			String aLine = brStream.readLine();
			String header = "^XA^LH0,30" + aLine;//starting point
			
			StringBuilder zpl = new StringBuilder();
			int i = 0;
			while (!aLine.equals("")) {
				//System.out.println(aLine);
				zpl.append("^FO0," + (i * charH));//line position
				zpl.append(font + "," + charH);//font size
				zpl.append("^FD" + aLine + "^FS");//line field
				aLine = brStream.readLine();
				i++;
			}
			zpl.append("^XZ");//end
			String labelLenght = "^LL" + charH * i;
			header+=labelLenght;
			String label = header+zpl;
			
			//System.out.println(label);
			byte[] by = label.getBytes();
			Doc doc = new SimpleDoc(by, flavor, das);
			job.print(doc, pras);
			brStream.close();
			frStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PrintException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param file
	 */
	private void printReversPages(JasperPrint jasperPrint) {
		try {
			
			List pages = jasperPrint.getPages();
			JasperPrintManager.printPages(jasperPrint, 0, pages.size()-1, !TxtPrinter.USE_DEFAULT_PRINTER);
			
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param printService
	 */
	private void getPrinterDetails(PrintService printService) {
		logger.debug("Printer: " + printService.getName());
		logger.debug("Supported flavors:");
		DocFlavor[] flavors = printService.getSupportedDocFlavors();
		if (flavors != null) {
			for (DocFlavor flavor : flavors) {
				logger.debug(flavor.toString());
			}
		}
		//System.out.println("Attributes:");
		Attribute[] attributes = printService.getAttributes().toArray();
		if (attributes != null) {
			for (Attribute attr : attributes) {
				logger.debug(attr.getName() + ": " + (attr.getClass()).toString());
			}
		}
	}
}
