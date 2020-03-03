package org.isf.utils.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Singleton, provide db connection used on persistence unit
 */
public class DbSingleJpaConn {

	protected static Logger logger = LoggerFactory.getLogger(DbSingleJpaConn.class);
	private static Connection connection;

	private DbSingleJpaConn() { }

	public static Connection getConnection() throws OHException {
		if (connection == null) {
			try {
				connection = createConnection();
			} catch (Exception e){
				String message = MessageBundle.getMessage("angal.utils.dbserverconnectionfailure");
				logger.error(">> " + message);
                throw new OHException(message, e);
			}
		}
		return connection;
	}

	private static Connection createConnection() throws SQLException, IOException, OHException {
        DbJpaUtil jpa = new DbJpaUtil();
        if(jpa.getEntityManager() == null){
            jpa.open();
        }
        final Connection[] jpaConnection = {null};
        Session session =  jpa.getEntityManager().unwrap(Session.class);
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                jpaConnection[0] = connection;
            }
        });
        return jpaConnection[0];
	}

}
