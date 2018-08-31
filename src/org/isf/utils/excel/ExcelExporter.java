package org.isf.utils.excel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHException;

public class ExcelExporter {
	
	private CharsetEncoder encoder;
	private  Locale currentLocale;
	
	public ExcelExporter() {
		encoder = Charset.forName("UTF-8").newEncoder();
		encoder.onMalformedInput(CodingErrorAction.REPORT);
		encoder.onUnmappableCharacter(CodingErrorAction.REPORT);
		
		currentLocale = Locale.getDefault();
	}
	
	/**
	 * Writes BOM for Excel UTF-8 automatic handling
	 * @param fileStream - the filestream to use 
	 * @throws IOException 
	 */
	private void writeBOM(FileOutputStream fileStream) throws IOException {
		byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF }; 
		fileStream.write(bom); 
	}
	
	/**
	 * Export a jTable to CSV file with a semi-column (;) as list separator
	 * 
	 * @param jtable
	 * @param file
	 * @throws IOException
	 * @deprecated use exportTableToExcel method
	 */
	public void exportTableToCSV(JTable jtable, File file) throws IOException {
		exportTableToCSV(jtable, file, ";");
	}
	
	/**
	 * Export a jTable to CSV file format
	 * 
	 * @param jtable
	 * @param file
	 * @param separator - the character to use as separator (usually ',' or ';')
	 * @throws IOException
	 */
	private void exportTableToCSV(JTable jtable, File file, String separator) throws IOException {
		TableModel model = jtable.getModel();
		FileOutputStream fileStream = new FileOutputStream(file);
		writeBOM(fileStream); 
		
		BufferedWriter outFile = new BufferedWriter(new OutputStreamWriter(fileStream, encoder));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		int colCount =  model.getColumnCount();
		for (int i = 0; i < colCount; i++) {
			if (i == colCount -1)
				outFile.write(model.getColumnName(i));
			else
				outFile.write(model.getColumnName(i) + separator);
		}
		outFile.write("\n");

		int rowCount =  model.getColumnCount();
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				String strVal;
				Object objVal = model.getValueAt(i, j);
				if (objVal != null) {
					if (objVal instanceof Integer) {
						Integer val = (Integer) objVal;
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						strVal = format.format(val);
						
					} else if (objVal instanceof Double) {
						
						Double val = (Double) objVal;
						NumberFormat format = NumberFormat.getInstance(currentLocale);
						strVal = format.format(val);
					} else if (objVal instanceof Timestamp) {
						
						Timestamp val = (Timestamp) objVal;
						strVal = sdf.format(val);
					} else {
						
						strVal = objVal.toString();
					}
				} else {
					strVal = " ";
				}
				if (j == colCount -1)
					outFile.write(strVal);
				else
					outFile.write(strVal + separator);

			}
			outFile.write("\n");
		}

		outFile.close();
	}
	
	/**
	 * Export a {@link ResultSet} to CSV file with a semi-column (;) as list separator
	 * 
	 * @param resultSet
	 * @param exportFile
	 * @throws IOException
	 * @throws OHException
	 * @deprecated use exportTableToExcel method
	 */
	public void exportResultsetToCSV(ResultSet resultSet, File exportFile) throws IOException, OHException {
		exportResultsetToCSV(resultSet, exportFile, ";");
	}
	
	/**
	 * Export a {@link ResultSet} to CSV file
	 * 
	 * @param resultSet
	 * @param exportFile
	 * @param separator - the character to use as separator (usually ',' or ';')
	 * @throws IOException
	 * @throws OHException
	 */
	private void exportResultsetToCSV(ResultSet resultSet, File exportFile, String separator) throws IOException, OHException {
		
		/*
		 * write BOM for Excel UTF-8 automatic handling
		 */
		FileOutputStream fileStream = new FileOutputStream(exportFile);
		writeBOM(fileStream); 
		
		BufferedWriter output = new BufferedWriter(new OutputStreamWriter(fileStream, encoder));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		NumberFormat numFormat = NumberFormat.getInstance(Locale.getDefault());
		
		try {
			ResultSetMetaData rsmd = resultSet.getMetaData();

			int colCount =  rsmd.getColumnCount();
			for (int i = 1; i <= colCount; i++) {
				if (i == colCount - 1)
					output.write(rsmd.getColumnName(i));
				else
					output.write(rsmd.getColumnName(i) + separator);
			}
			output.write("\n");
			
			while(resultSet.next()) {
				
				String strVal;
				for (int i = 1; i <= colCount; i++) {
					Object objVal = resultSet.getObject(i);
					if (objVal != null) {
						if (objVal instanceof Double) {
							
							Double val = (Double) objVal;
							strVal = numFormat.format(val);
						} else if (objVal instanceof Timestamp) {
							
							Timestamp val = (Timestamp) objVal;
							strVal = sdf.format(val);
						} else {
							
							strVal = objVal.toString();
						}
					} else {
						strVal = " ";
					}
					if (i == colCount - 1)
						output.write(strVal);
					else
						output.write(strVal + separator);
						
				}
				output.write("\n");
				
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
		output.close();
	}
	
	/**
	 * Export a {@link JTable} to Excel using Apache POI library
	 * 
	 * @param jtable
	 * @param file
	 * @throws IOException
	 */
	public void exportTableToExcel(JTable jtable, File file) throws IOException {
		TableModel model = jtable.getModel();
		FileOutputStream fileStream = new FileOutputStream(file);
		//BufferedWriter outFile = new BufferedWriter(new OutputStreamWriter(fileStream, encoder));
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet worksheet = workbook.createSheet();

		HSSFRow headers = worksheet.createRow((short) 0);
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short)10);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		
		int colCount =  model.getColumnCount();
		for (int i = 0; i < colCount; i++) {
			HSSFCell cell = headers.createCell((short) i);
			HSSFRichTextString value = new HSSFRichTextString(model.getColumnName(i));
			cell.setCellStyle(style);
			cell.setCellValue(value);
		}
		
		int rowCount =  model.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			int index = i + 1;
			HSSFRow row = worksheet.createRow((short) index);
			
			for (int j = 0; j < colCount; j++) {
				HSSFCell cell = row.createCell((short) j);
				Object objVal = model.getValueAt(i, j);
				if (objVal != null) {
					if (objVal instanceof Integer) {
						Integer val = (Integer) objVal;
						cell.setCellValue(val);
					} else if (objVal instanceof Double) {
						Double val = (Double) objVal;
						cell.setCellValue(val);
					} else if (objVal instanceof Timestamp) {
						Timestamp val = (Timestamp) objVal;
						cell.setCellValue(val);
					} else {
						HSSFRichTextString val = new HSSFRichTextString(objVal.toString());
						cell.setCellValue(val);
					}
				}
			}
		}
		workbook.write(fileStream);
		fileStream.flush();
		fileStream.close();
	}
	
	/**
	 * Export a {@link ResultSet} to Excel using Apache POI library
	 * 
	 * @param resultSet
	 * @param exportFile
	 * @throws IOException
	 * @throws OHException
	 */
	public void exportResultsetToExcel(ResultSet resultSet, File exportFile) throws IOException, OHException {
		FileOutputStream fileStream = new FileOutputStream(exportFile);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet worksheet = workbook.createSheet();

		HSSFRow headers = worksheet.createRow((short) 0);
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short)10);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		
		try {
			ResultSetMetaData rsmd = resultSet.getMetaData();
		
			int colCount =  rsmd.getColumnCount();
			for (int i = 0; i < colCount; i++) {
				HSSFCell cell = headers.createCell((short) i);
				HSSFRichTextString value = new HSSFRichTextString(rsmd.getColumnName(i+1));
				cell.setCellStyle(style);
				cell.setCellValue(value);
			}
			
			int index = 1;
			while(resultSet.next()) {
				HSSFRow row = worksheet.createRow((short) index);
				
				for (int j = 0; j < colCount; j++) {
					HSSFCell cell = row.createCell((short) j);
					Object objVal = resultSet.getObject(j+1);
					if (objVal != null) {
						if (objVal instanceof Integer) {
							Integer val = (Integer) objVal;
							cell.setCellValue(val);
						} else if (objVal instanceof Double) {
							Double val = (Double) objVal;
							cell.setCellValue(val);
						} else if (objVal instanceof Timestamp) {
							Timestamp val = (Timestamp) objVal;
							cell.setCellValue(val);
						} else {
							HSSFRichTextString val = new HSSFRichTextString(objVal.toString());
							cell.setCellValue(val);
						}
					} 
				}
				index++;
			}
			workbook.write(fileStream);
			fileStream.flush();
			fileStream.close();
		
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
		
	}
}
