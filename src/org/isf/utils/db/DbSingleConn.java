package org.isf.utils.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.isf.generaldata.MessageBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

/*
 * @version 0.1 2005-11-06
 * @author bob
 * 
 */

/**
 * classe singleton che provvede alla connessione con database i parametri: dati
 * connessione,database, user, passwd ecc sono letti da file properties
 */
public class DbSingleConn {
	
	protected static Logger logger = LoggerFactory.getLogger(DbSingleConn.class);

	private final static int MYSQL_DEFAULT_PORT = 3306;
	
	private static Connection pConn;

	private DbSingleConn() { }

	public static Connection getConnection() throws SQLException, IOException {
		if (pConn == null) {
			try {
				pConn = createConnection();
			} catch (CommunicationsException ce){
				String message = MessageBundle.getMessage("angal.utils.dbserverconnectionfailure");
				logger.error(">> " + message);
				JOptionPane.showMessageDialog(null, message);
				System.exit(1);
			}
				
		}
		return pConn;
	}

	public static void closeConnection() throws SQLException, IOException {
		pConn.close();
	}

	public static void releaseConnection() throws SQLException, IOException {
		pConn.close();
		pConn = null;
	}

	public static void commitConnection() throws SQLException , IOException{
		pConn.commit();
	}
	
	public static void rollbackConnection() throws SQLException , IOException{
		pConn.rollback();
	}
	
	private static Connection createConnection() throws SQLException, IOException {
		
		Properties props = new Properties();
		InputStream is = DbSingleConn.class.getClassLoader().getResourceAsStream("database.properties");
		if (is == null) {
			FileInputStream in = new FileInputStream("rsc/database.properties");
			props.load(in);
			in.close();
		} else {
			props.load(is);
			is.close();
		}
		
		String drivers = props.getProperty("jdbc.drivers");
		if (drivers != null)
			System.setProperty("jdbc.drivers", drivers);
		String url = props.getProperty("jdbc.url");
		String server = props.getProperty("jdbc.server");
		String db = props.getProperty("jdbc.db");
		String username = props.getProperty("jdbc.username");
		String password = props.getProperty("jdbc.password");
		String port = props.getProperty("jdbc.port");
		if (port == null) {
			port = String.valueOf(MYSQL_DEFAULT_PORT);
		}
		
		StringBuilder sbURL = new StringBuilder();
		sbURL.append(url);
		sbURL.append("//");
		sbURL.append(server);
		sbURL.append(":");
		sbURL.append(port);
		sbURL.append("/");
		sbURL.append(db);
		sbURL.append("?useUnicode=true&characterEncoding=UTF-8");
		sbURL.append("&user=");
		sbURL.append(username);
		sbURL.append("&password=");
		sbURL.append(password);
		

		return DriverManager.getConnection(sbURL.toString());
	}

}
