package org.isf.utils.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JTable;

public class ExcelImporter {
	public ExcelImporter(){}
	
	public void OpenInTable(JTable jtebale,File file) throws FileNotFoundException,IOException
	{
		FileReader inFile = new FileReader(file);
		BufferedReader bis = new BufferedReader(inFile);
		String strLine = null;
		
		while((strLine = bis.readLine()) != null)
		{
			//String strToken[] = strLine.split("\t");
			strLine.split("\t");
		}
		
		inFile.close();
	}
}
