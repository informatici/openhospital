package org.isf.utils.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

import org.isf.utils.exception.OHException;
import org.isf.utils.time.TimeTools;

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
	public static void main(String[] args) {
		File fileIn = new File("E:\\prova.csv");
		File fileOut = new File("E:\\prova.sql");
		
		try {
			CSV2SQL converter = new CSV2SQL();  
			converter.pharmacyStartCVS(fileIn, fileOut);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OHException e) {
			e.printStackTrace();
		}
	}
	
	public CSV2SQL() {
	}

	/**
	 * It uses a CSV to start the OpenHospital pharmacy, taking care about LOT management.
	 * (If LOT name is not provided a random number will be generated)
	 * 
	 * Tables involved: 
	 * MEDICALDSR, MEDICALDSRTYPE, MEDICALDSRLOT, MEDICALDSRSTOCKMOV 
	 * Fields needed:
	 * MDSR_ID,MDSR_CODE,MDSR_DESC,MDSR_PCS_X_PCK,MDSR_MDSRT_ID_A,MDSRT_DESC,LT_ID_A,QTY,LT_DUE_DATE,LT_COST
	 * @throws IOException 
	 * @throws OHException 
	 * 
	 */
	public void pharmacyStartCVS(File fileIn, File fileOut) throws IOException, OHException {
		NumberFormat numFormat = NumberFormat.getInstance(Locale.getDefault());
		
		CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
		encoder.onMalformedInput(CodingErrorAction.REPORT);
		encoder.onUnmappableCharacter(CodingErrorAction.REPORT);

		/*
		 * WINDOWS Encodings: 
		 * windows-1250 (Central Europe) 
		 * windows-1251 (Cyrillic) 
		 * windows-1252 (Latin I) 
		 * windows-1253 (Greek) 
		 * windows-1254 (Turkish) 
		 * windows-1255 (Hebrew) 
		 * windows-1256 (Arabic) 
		 * windows-1257 (Baltic) 
		 * windows-1258 (Vietnam) 
		 * windows-874 (Thai) 
		 * windows-932 (Japanese Shift-JIS) 
		 * windows-936 (Simplified Chinese GBK) 
		 * windows-949 (Korean) 
		 * windows-950 (Traditional Chinese Big5)
		 */
		BufferedReader input = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileIn), "windows-1250"));
		BufferedWriter output = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fileOut), encoder));
		
		try {
			String line = null;
			String[] st = null;

			output.write("UPDATE MEDICALDSR SET MDSR_INI_STOCK_QTI = 0, MDSR_IN_QTI = 0, MDSR_OUT_QTI = 0 WHERE 1;\n");
			output.write("DELETE FROM MEDICALDSRSTOCKMOVWARD;\n");
			output.write("DELETE FROM MEDICALDSRSTOCKMOV;\n");
			output.write("DELETE FROM MEDICALDSRWARD;\n");
			output.write("DELETE FROM MEDICALDSRLOT;\n");
			output.write("DELETE FROM MEDICALDSR;\n");
			output.write("DELETE FROM MEDICALDSRTYPE;\n");

			ArrayList<String> medTypeList = new ArrayList<String>();
			ArrayList<String> medList = new ArrayList<String>();
			ArrayList<String> lotList = new ArrayList<String>();

			/*
			 * Check COLUMN HEADERS
			 */
			line = input.readLine();
			
			if (line != null) {
				// System.out.println(line);
				st = line.replace("\"", "").split(";");
				int i = -1;
				String MDSR_ID = st[++i];
				String MDSR_CODE = st[++i];
				String MDSR_DESC = st[++i];
				String MDSR_PCS_X_PCK = st[++i];
				String MDSR_MDSRT_ID_A = st[++i]; // FK to MEDICALDSRTYPE (MDSRT_ID_A)
				String MDSRT_DESC = st[++i]; // FK to MEDICALDSRTYPE (MDSRT_ID_A)
				String LT_ID_A = st[++i]; // could be empty
				String QTY = st[++i]; // could be empty
				String LT_DUE_DATE = st[++i]; // could be wrongly formatted
				String LT_COST = st[++i]; // could be empty

				if (!MDSR_ID.equals("MDSR_ID")
						|| !MDSR_CODE.equals("MDSR_CODE")
						|| !MDSR_DESC.equals("MDSR_DESC")
						|| !MDSR_PCS_X_PCK.equals("MDSR_PCS_X_PCK")
						|| !MDSR_MDSRT_ID_A.equals("MDSR_MDSRT_ID_A")
						|| !MDSRT_DESC.equals("MDSRT_DESC")
						|| !LT_ID_A.equals("LT_ID_A") || !QTY.equals("QTY")
						|| !LT_DUE_DATE.equals("LT_DUE_DATE")
						|| !LT_COST.equals("LT_COST")) {

					throw new OHException(
							"The first line must contain the following COLUMN HEADERS:\nMDSR_ID,MDSR_CODE,MDSR_DESC,MDSR_PCS_X_PCK,MDSR_MDSRT_ID_A,MDSRT_DESC,LT_ID_A,QTY,LT_DUE_DATE,LT_COST");
				}
			}

			/*
			 * Check DATA
			 */
			int lineNumber = 2;
			while ((line = input.readLine()) != null) {
				//System.out.println(line);
				st = line.replace("\"", "").split(";");
				int i = -1;
				String MDSR_ID = st[++i];
				String MDSR_CODE = st[++i];
				String MDSR_DESC = st[++i];
				String MDSR_PCS_X_PCK = st[++i];
				String MDSR_MDSRT_ID_A = st[++i]; // FK to MEDICALDSRTYPE (MDSRT_ID_A)
				String MDSRT_DESC = st[++i]; // FK to MEDICALDSRTYPE (MDSRT_ID_A)
				String LT_ID_A = st[++i]; // could be empty
				String QTY = st[++i]; // could be empty
				String LT_DUE_DATE = st[++i]; // could be wrongly formatted
				String LT_COST = st[++i]; // could be empty

				try {
					new Integer(MDSR_ID);
				} catch (NumberFormatException e) {
					throw new OHException("ERROR MDSR_ID on line "+lineNumber+": " + MDSR_ID);
				}
				int lotQty;
				try {
					lotQty = Integer.parseInt(QTY);
					//System.out.print("QTY: " + lotQty);
				} catch (NumberFormatException e) {
					throw new OHException("ERROR QTY on line "+lineNumber+": " + QTY);
				}
				double lotCost;
				try {
					lotCost = numFormat.parse(LT_COST).doubleValue();
					//System.out.print(" COST: " + lotCost);
				} catch (ParseException e) {
					throw new OHException("ERROR LT_COST on line "+lineNumber+": " + LT_COST);
				}
				//System.out.print("\n");
				
				GregorianCalendar dueDate;
				try {
					dueDate = TimeTools.parseDate(LT_DUE_DATE, "dd/MM/yyyy", true);
				} catch (ParseException e) {
					throw new OHException("ERROR LT_DUE_DATE on line "+lineNumber+": " + LT_DUE_DATE);
				}

				// MEDICALDSRTYPE
				if (!medTypeList.contains(MDSR_MDSRT_ID_A)) {
					medTypeList.add(MDSR_MDSRT_ID_A);
					output.write("INSERT INTO MEDICALDSRTYPE (MDSRT_ID_A, MDSRT_DESC) VALUES ('"
							+ MDSR_MDSRT_ID_A + "', '" + MDSRT_DESC + "');\n");
				}

				// MEDICALDSR
				if (!medList.contains(MDSR_ID)) {
					medList.add(MDSR_ID);
					int pcsXpck;
					try {
						pcsXpck = MDSR_PCS_X_PCK.equals("") ? 0 : Integer.parseInt(MDSR_PCS_X_PCK);
						output.write("INSERT INTO MEDICALDSR (MDSR_ID, MDSR_MDSRT_ID_A, MDSR_DESC, MDSR_CODE, MDSR_PCS_X_PCK, MDSR_IN_QTI) VALUES ('"
								+ MDSR_ID
								+ "', '"
								+ MDSR_MDSRT_ID_A
								+ "', '"
								+ MDSR_DESC
								+ "', '"
								+ MDSR_CODE
								+ "', "
								+ pcsXpck
								+ ", "
								+ QTY + ");\n");
					} catch (NumberFormatException e) {
						System.out.println("Wrong MDSR_PCS_X_PCK: "	+ MDSR_PCS_X_PCK);
						System.exit(-2);
					}
				}

				// MEDICALDSRLOT
				if (LT_ID_A.equals("")) {
					Random random = new Random();
					LT_ID_A = String.valueOf(Math.abs(random.nextLong()));
				}
				if (!lotList.contains(LT_ID_A)) {
					lotList.add(LT_ID_A);
					String prepDate = TimeTools.formatDateTime(new GregorianCalendar(), null);
					try {
						output.write("INSERT INTO MEDICALDSRLOT (LT_ID_A, LT_PREP_DATE, LT_DUE_DATE, LT_COST) VALUES ('"
								+ LT_ID_A
								+ "', '"
								+ prepDate
								+ "', '"
								+ TimeTools.formatDateTime(dueDate, null) 
								+ "', " 
								+ lotCost 
								+ ");\n");
					} catch (NumberFormatException e) {
						System.out.println("Wrong LT_COST: " + LT_COST);
						System.exit(-3);

					} 
				}

				// MEDICALDSRSTOCKMOV
				try {
					String date = TimeTools.formatDateTime(new GregorianCalendar(), null);
					String reference = String.format("INV%5s", lineNumber).replace(' ', '0');
					output.write("INSERT INTO MEDICALDSRSTOCKMOV (MMV_MDSR_ID, MMV_MMVT_ID_A, MMV_LT_ID_A, MMV_DATE, MMV_QTY, MMV_FROM, MMV_REFNO) VALUES ("
							+ MDSR_ID
							+ ", 'charge', '"
							+ LT_ID_A
							+ "', '"
							+ date
							+ "', "
							+ lotQty
							+ ", 0, '"
							+ reference + "');\n");
				} catch (NumberFormatException e) {
					System.out.println("Wrong MMV_QTY: " + QTY);
					System.exit(-3);

				}
				lineNumber++;
			}
		} finally {
			input.close();
			output.close();
		}

	}
}
