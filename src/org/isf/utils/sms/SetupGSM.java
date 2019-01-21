/**
 * Test.java - 30/gen/2014
 */
package org.isf.utils.sms;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * @author Mwithi
 *
 */
public class SetupGSM extends JFrame implements SerialPortEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Properties props = new Properties();
	private CommPortIdentifier portId = null;
	private Enumeration<?> portList = null;
	private SerialPort serialPort = null;
	private InputStream inputStream;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SetupGSM setup = new SetupGSM();
		System.exit(0);
	}
	
	public SetupGSM() {	
		
		FileInputStream in;
		try {
			in = new FileInputStream("rsc" + File.separator + "SmsGateway" + File.separator + "GSM.properties");
			props.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		String model = props.getProperty("GMM");
		
		portList = CommPortIdentifier.getPortIdentifiers();
		
		while (portList.hasMoreElements()) {
			
			portId = (CommPortIdentifier) portList.nextElement();

			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
			//if (portId.getName().equals("COM25")) {
				
				System.out.println("Port found: " + portId.getName() + " " + (portId.getPortType() == CommPortIdentifier.PORT_SERIAL ? "SERIAL" : "PARALLEL"));
			
				try {
					serialPort = (SerialPort) portId.open("SmsSender", 10);
					serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					serialPort.addEventListener(this);
			        serialPort.notifyOnDataAvailable(true);
					
					OutputStream outputStream = serialPort.getOutputStream();
						if (outputStream != null) System.out.println("Output stream OK");
							else System.out.println("Output stream not found");
					
					inputStream = serialPort.getInputStream(); 
					byte[] command = model.getBytes();
			        outputStream.write(command);
			        
			        Thread.sleep(5000);
			        
				} catch (PortInUseException e) {
					System.out.println("Port in use.");
					continue;
				} catch (Exception e) {
					System.out.println("Failed to open port " + portId.getName());
					e.printStackTrace();
					continue;
				} finally {
					serialPort.close();
				}
			}
		}
		System.out.println("End.");
	}

	@Override
    public void serialEvent(SerialPortEvent event) {
		SerialPort serialPort = (SerialPort) event.getSource();
		String port = serialPort.getName();
		StringBuffer sb = new StringBuffer();
        byte[] buffer = new byte[1];
        try {
			while(inputStream.available() > 0){
			  int len = inputStream.read(buffer);
			  sb.append(new String(buffer));
			}
			String answer = sb.toString();
			if (confirm(port, answer) == JOptionPane.YES_OPTION) {
				save(port);
				System.exit(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	/**
	 * @param port
	 * @param answer
	 * @return
	 * @throws HeadlessException
	 */
	private int confirm(String port, String answer) throws HeadlessException {
		try {
			int ok = answer.indexOf("OK");
			if (ok > 0) answer = answer.substring(2, ok - 3);
				else return JOptionPane.NO_OPTION;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("outofbound: '" + answer + "'");
		}
		System.out.println(answer.trim());
		
		int option = JOptionPane.showConfirmDialog(this, "Found modem: "+answer+" on port " + port + "\nConfirm?");
		return option;
	}

	/**
	 * @param port
	 */
	private void save(String port) {
		FileOutputStream out;
		StringBuilder comment = new StringBuilder(" Configuration file for SMS Sender GSM\n");
		comment.append(" PORT = COMx (Windows) or /dev/ttyUSBx (Linux)");
		try {
			out = new FileOutputStream("rsc" + File.separator + "SmsGateway" + File.separator + "GSM.properties");
			props.setProperty("PORT", port);
			props.store(out, comment.toString());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
