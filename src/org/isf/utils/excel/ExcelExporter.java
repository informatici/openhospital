package org.isf.utils.excel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHException;

public class ExcelExporter {
	
	public ExcelExporter() {
	}
	
	public void exportTableToCSV(JTable jtable, File file) throws IOException {
		exportTable(jtable, file, ";");
	}
	
	public void exportTableToExcel(JTable jtable, File file) throws IOException {
		exportTable(jtable, file, "\t");
	}

	private void exportTable(JTable jtable, File file, String separator) throws IOException {
		TableModel model = jtable.getModel();
		FileWriter outFile = new FileWriter(file);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		for (int i = 0; i < model.getColumnCount(); i++) {
			outFile.write(model.getColumnName(i) + separator);
		}
		outFile.write("\n");

		for (int i = 0; i < model.getRowCount(); i++) {
			for (int j = 0; j < model.getColumnCount(); j++) {
				String strVal;
				Object objVal = model.getValueAt(i, j);
				if (objVal != null) {
					if (objVal instanceof Double) {
						
						Double val = (Double) objVal;
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
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
				outFile.write(strVal + separator);

			}
			outFile.write("\n");
		}

		outFile.close();
	}

	public void exportResultsetToCSV(ResultSet resultSet, File exportFile) throws IOException, OHException {
		
		FileWriter outFile = new FileWriter(exportFile);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			ResultSetMetaData rsmd = resultSet.getMetaData();

			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				outFile.write(rsmd.getColumnName(i) + ";");
			}
			outFile.write("\n");
			
			while(resultSet.next()) {
				
				String strVal;
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					Object objVal = resultSet.getObject(i);
					if (objVal != null) {
						if (objVal instanceof Double) {
							
							Double val = (Double) objVal;
							NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
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
					outFile.write(strVal + ";");
				}
				outFile.write("\n");
				
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
		outFile.close();
	}
}
