package org.isf.utils.db;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * 
 */

/**
 * @author Mwithi
 *
 */
public class CSV2SQL {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		BufferedReader input = new BufferedReader(new FileReader(new File("Path\\to\\dump\\medicals_dump_09.02.2012.csv")));     
		Writer output = new BufferedWriter(new FileWriter(new File("Path\\to\\dump\\medicals_dump_09.02.2012.sql")));

		try {
		  String line = null;
		  String[] st = null;
		  
		  output.write("UPDATE MEDICALDSR SET MDSR_INI_STOCK_QTI = 0, MDSR_IN_QTI = 0, MDSR_OUT_QTI = 0 WHERE 1;\n");
		  output.write("DELETE FROM MEDICALDSRSTOCKMOV;\n");
		  output.write("DELETE FROM MEDICALDSRWARD;\n");
		  output.write("DELETE FROM MEDICALDSRLOT;\n");

		  while ((line = input.readLine()) != null) {
			System.out.println(line);
		    st = line.replace("\"","").split(";");

		    output.write("UPDATE MEDICALDSR SET MDSR_INI_STOCK_QTI = " + st[1] + " WHERE MDSR_CODE = '" + st[0] + "';\n");

		  }
		} finally {
		  input.close();
		  output.close();
		}

	}

}
