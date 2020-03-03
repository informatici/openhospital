package org.isf.utils.excel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHException;

public class ExcelExporter {
	
	private CharsetEncoder encoder;
	private Locale currentLocale;
	private Workbook workbook;
	private CellStyle doubleStyle;
	private CellStyle dateStyle;
	private CellStyle dateTimeStyle;
	private CellStyle bigDecimalStyle;
	private CellStyle headerStyle;
	private CreationHelper createHelper;
	
	public ExcelExporter() {
		encoder = Charset.forName("UTF-8").newEncoder();
		encoder.onMalformedInput(CodingErrorAction.REPORT);
		encoder.onUnmappableCharacter(CodingErrorAction.REPORT);
		currentLocale = Locale.getDefault();
	}
	
	private void initStyles() {
		
		headerStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short)10);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerStyle.setFont(font);
		
		short doubleFormat = workbook.createDataFormat().getFormat("#,##0.00");
		doubleStyle = workbook.createCellStyle();
		doubleStyle.setDataFormat(doubleFormat);
		
		short dateFormat = workbook.createDataFormat().getFormat("yyyy-mm-dd");
		dateStyle = workbook.createCellStyle();
		dateStyle.setDataFormat(dateFormat);
		
		short dateTimeFormat = workbook.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss");
		dateTimeStyle = workbook.createCellStyle();
		dateTimeStyle.setDataFormat(dateTimeFormat);
		
		short bigDecimalFormat = workbook.createDataFormat().getFormat("#,##0");
		bigDecimalStyle = workbook.createCellStyle();
		bigDecimalStyle.setDataFormat(bigDecimalFormat);
		
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
	 * Export a {@link Collection} to CSV using Apache POI library
	 * 
	 * @param resultSet
	 * @param exportFile
	 * @throws IOException
	 * @throws OHException
	 * @deprecated use exportDataToExcel
	 */
	public void exportDataToCSV(Collection data, File exportFile) throws IOException, OHException {
		
		FileWriter outFile = new FileWriter(exportFile);
		try {
			boolean header = false;
			for (Object map : data) {
				Map thisMap = ((Map) map);
				if (!header) {
					Set columns = thisMap.keySet();
					for (Object column : columns) {
						outFile.write(column.toString() + ";");
					}
					outFile.write("\n");
					header = true;
				}
				
				String strVal;
				Collection values = thisMap.values();
				for (Object value : values) {
					strVal = convertValue(value);
					outFile.write(strVal + ";");
				}
				outFile.write("\n");
			}
		} finally {
			outFile.close();
		}
	}

	private String convertValue(Object value) {
		String strVal = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if (value != null) {
			if (value instanceof BigDecimal) {

				BigDecimal val = (BigDecimal) value;
				NumberFormat format = NumberFormat.getInstance(Locale
						.getDefault());
				strVal = format.format(val);
			} else if (value instanceof Double) {

				Double val = (Double) value;
				NumberFormat format = NumberFormat.getInstance(Locale
						.getDefault());
				strVal = format.format(val);
			} else if (value instanceof Timestamp) {

				Timestamp val = (Timestamp) value;
				strVal = sdf.format(val);
			} else {

				strVal = value.toString();
			}
		} else {
			strVal = " ";
		}
		return strVal;
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
		
		workbook = new XSSFWorkbook();
		createHelper = workbook.getCreationHelper();
		
		Sheet worksheet = workbook.createSheet();
		initStyles();
		
		Row headers = worksheet.createRow((short) 0);
		int colCount =  model.getColumnCount();
		for (int i = 0; i < colCount; i++) {
			Cell cell = headers.createCell((short) i);
			RichTextString value = createHelper.createRichTextString(model.getColumnName(i));
			cell.setCellStyle(headerStyle);
			cell.setCellValue(value);
		}
		
		int rowCount =  model.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			int index = i + 1;
			Row row = worksheet.createRow(index);
			
			for (int j = 0; j < colCount; j++) {
				Cell cell = row.createCell((short) j);
				Object value = model.getValueAt(i, j);
				setValueForExcel(cell, value);
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
		
		workbook = new XSSFWorkbook();
		createHelper = workbook.getCreationHelper();
		
		Sheet worksheet = workbook.createSheet();
		initStyles();

		Row headers = worksheet.createRow((short) 0);
		try {
			ResultSetMetaData rsmd = resultSet.getMetaData();
		
			int colCount =  rsmd.getColumnCount();
			for (int i = 0; i < colCount; i++) {
				Cell cell = headers.createCell((short) i);
				RichTextString value = createHelper.createRichTextString(rsmd.getColumnName(i+1));
				cell.setCellStyle(headerStyle);
				cell.setCellValue(value);
			}
			
			int index = 1;
			while(resultSet.next()) {
				Row row = worksheet.createRow(index);
				
				for (int j = 0; j < colCount; j++) {
					Object value = resultSet.getObject(j+1);
					Cell cell = row.createCell((short) j);
					setValueForExcel(cell, value);
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
	
	/**
	 * Export a {@link ResultSet} to Excel using Apache POI library
	 * 
	 * @param resultSet
	 * @param exportFile
	 * @throws IOException
	 * @throws OHException
	 */
	public void exportDataToExcel(Collection data, File exportFile) throws IOException, OHException {
		FileOutputStream fileStream = new FileOutputStream(exportFile);
		
		workbook = new XSSFWorkbook();
		createHelper = workbook.getCreationHelper();
		Sheet worksheet = workbook.createSheet();
		initStyles();

		Row headers = worksheet.createRow((short) 0);
		boolean header = false;
		int index = 1;
		for (Object map : data) {
			Map thisMap = ((Map) map);
			if (!header) {
				Set columns = thisMap.keySet();
				int h = 0;
				for (Object column : columns) {
					Cell cell = headers.createCell((short) h);
					RichTextString value = createHelper.createRichTextString(column.toString());
					cell.setCellStyle(headerStyle);
					cell.setCellValue(value);
					h++;
				}
				header = true;
				continue;
			}
			
			Row row = worksheet.createRow(index);
			Collection values = thisMap.values();
			int j = 0;
			for (Object value : values) {
				Cell cell = row.createCell((short) j);
				setValueForExcel(cell, value);
				j++;
			}
			index++;
		}
		workbook.write(fileStream);
		fileStream.flush();
		fileStream.close();
	}
	
	private void setValueForExcel(Cell cell, Object value) {
		
		if (value != null) {
			if (value instanceof Integer) {
				Integer val = (Integer) value;
				cell.setCellValue(val);
			} else if (value instanceof Double) {
				Double val = (Double) value;
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(doubleStyle);
				cell.setCellValue(val);
			} else if (value instanceof Timestamp) {
				Timestamp val = (Timestamp) value;
				cell.setCellStyle(dateTimeStyle);
				cell.setCellValue(val);
			} else if (value instanceof Date) {
				Timestamp val = new Timestamp(((Date) value).getTime());
				cell.setCellStyle(dateStyle);
				cell.setCellValue(val);
			} else if (value instanceof BigDecimal) {
				BigDecimal val = (BigDecimal) value;
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(bigDecimalStyle);
				cell.setCellValue(val.doubleValue());
			} else if (value instanceof Long) {
				Long val = (Long) value;
				cell.setCellValue(val);
			} else {
				RichTextString val = createHelper.createRichTextString(value.toString());
				cell.setCellValue(val);
			}
		}
	}
	
	/**
	 * Export a {@link JTable} to Excel 97-2003 using Apache POI library
	 * 
	 * @param jtable
	 * @param file
	 * @throws IOException
	 */
	public void exportTableToExcelOLD(JTable jtable, File file) throws IOException {
		TableModel model = jtable.getModel();
		FileOutputStream fileStream = new FileOutputStream(file);
		//BufferedWriter outFile = new BufferedWriter(new OutputStreamWriter(fileStream, encoder));
		
		workbook = new HSSFWorkbook();
		HSSFSheet worksheet = (HSSFSheet) workbook.createSheet();
		initStyles();
		
		HSSFRow headers = worksheet.createRow((short) 0);
		int colCount =  model.getColumnCount();
		for (int i = 0; i < colCount; i++) {
			HSSFCell cell = headers.createCell((short) i);
			HSSFRichTextString value = new HSSFRichTextString(model.getColumnName(i));
			cell.setCellStyle(headerStyle);
			cell.setCellValue(value);
		}
		
		int rowCount =  model.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			int index = i + 1;
			HSSFRow row = worksheet.createRow((short) index);
			
			for (int j = 0; j < colCount; j++) {
				HSSFCell cell = row.createCell((short) j);
				Object value = model.getValueAt(i, j);
				setValueForExcelOLD(cell, value);
			}
		}
		workbook.write(fileStream);
		fileStream.flush();
		fileStream.close();
	}
	
	/**
	 * Export a {@link ResultSet} to Excel 97-2003 using Apache POI library
	 * 
	 * @param resultSet
	 * @param exportFile
	 * @throws IOException
	 * @throws OHException
	 */
	public void exportResultsetToExcelOLD(ResultSet resultSet, File exportFile) throws IOException, OHException {
		FileOutputStream fileStream = new FileOutputStream(exportFile);
		
		workbook = new HSSFWorkbook();
		HSSFSheet worksheet = (HSSFSheet) workbook.createSheet();
		initStyles();

		HSSFRow headers = worksheet.createRow((short) 0);
		try {
			ResultSetMetaData rsmd = resultSet.getMetaData();
		
			int colCount =  rsmd.getColumnCount();
			for (int i = 0; i < colCount; i++) {
				HSSFCell cell = headers.createCell((short) i);
				HSSFRichTextString value = new HSSFRichTextString(rsmd.getColumnName(i+1));
				cell.setCellStyle(headerStyle);
				cell.setCellValue(value);
			}
			
			int index = 1;
			while(resultSet.next()) {
				HSSFRow row = worksheet.createRow((short) index);
				
				for (int j = 0; j < colCount; j++) {
					Object value = resultSet.getObject(j+1);
					HSSFCell cell = row.createCell((short) j);
					setValueForExcelOLD(cell, value);
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
	
	/**
	 * Export a {@link ResultSet} to Excel 97-2003 using Apache POI library
	 * 
	 * @param resultSet
	 * @param exportFile
	 * @throws IOException
	 * @throws OHException
	 */
	public void exportDataToExcelOLD(Collection data, File exportFile) throws IOException, OHException {
		FileOutputStream fileStream = new FileOutputStream(exportFile);
		
		workbook = new HSSFWorkbook();
		HSSFSheet worksheet = (HSSFSheet) workbook.createSheet();
		initStyles();

		HSSFRow headers = worksheet.createRow((short) 0);
		boolean header = false;
		int index = 1;
		for (Object map : data) {
			Map thisMap = ((Map) map);
			if (!header) {
				Set columns = thisMap.keySet();
				int h = 0;
				for (Object column : columns) {
					HSSFCell cell = headers.createCell((short) h);
					HSSFRichTextString value = new HSSFRichTextString(column.toString());
					cell.setCellStyle(headerStyle);
					cell.setCellValue(value);
					h++;
				}
				header = true;
				continue;
			}
			
			HSSFRow row = worksheet.createRow((short) index);
			Collection values = thisMap.values();
			int j = 0;
			for (Object value : values) {
				HSSFCell cell = row.createCell((short) j);
				setValueForExcelOLD(cell, value);
				j++;
			}
			index++;
		}
		workbook.write(fileStream);
		fileStream.flush();
		fileStream.close();
	}
	
	private void setValueForExcelOLD(HSSFCell cell, Object value) {
		
		if (value != null) {
			if (value instanceof Integer) {
				Integer val = (Integer) value;
				cell.setCellValue(val);
			} else if (value instanceof Double) {
				Double val = (Double) value;
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(doubleStyle);
				cell.setCellValue(val);
			} else if (value instanceof Timestamp) {
				Timestamp val = (Timestamp) value;
				cell.setCellStyle(dateTimeStyle);
				cell.setCellValue(val);
			} else if (value instanceof Date) {
				Timestamp val = new Timestamp(((Date) value).getTime());
				cell.setCellStyle(dateStyle);
				cell.setCellValue(val);
			} else if (value instanceof BigDecimal) {
				BigDecimal val = (BigDecimal) value;
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(bigDecimalStyle);
				cell.setCellValue(val.doubleValue());
			} else if (value instanceof Long) {
				Long val = (Long) value;
				cell.setCellValue(val);
			} else {
				HSSFRichTextString val = new HSSFRichTextString(value.toString());
				cell.setCellValue(val);
			}
		}
	}
	
	public static JFileChooser getJFileChooserExcel(File defaultFilename) {
		JFileChooser fcExcel = new JFileChooser();
		FileNameExtensionFilter excelFilter = new FileNameExtensionFilter("Excel (*.xlsx)","xlsx");
		FileNameExtensionFilter excelFilter2003 = new FileNameExtensionFilter("Excel 97-2003 (*.xls)","xls");
		fcExcel.addChoosableFileFilter(excelFilter);
		fcExcel.addChoosableFileFilter(excelFilter2003);
		fcExcel.setFileFilter(excelFilter);
		fcExcel.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fcExcel.setSelectedFile(defaultFilename);
		return fcExcel;
	}
}
