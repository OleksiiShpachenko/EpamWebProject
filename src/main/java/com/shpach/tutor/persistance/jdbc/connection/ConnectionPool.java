package com.shpach.tutor.persistance.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.shpach.tutor.manager.Config;

public class ConnectionPool {
	private static final Logger logger = Logger.getLogger(ConnectionPool.class);
	/// private static ConnectionPool instance = null;
	private static final String TOMCAT_JNDI_NAME = "java:comp/env";
	private static DataSource pool;
	private static final String DATASOURCE;

	static {
		DATASOURCE = Config.getInstance().getProperty(Config.DATASOURCE);
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup(TOMCAT_JNDI_NAME);
			pool = (DataSource) envContext.lookup(DATASOURCE);
			if (pool == null)
				logger.error("Connection pool is not received");
		} catch (NamingException e) {
			logger.error(e, e);
		}
	}

	public static synchronized Connection getConnection() {
		Connection connection = null;

		try {
			connection = pool.getConnection();
			if (connection == null)
				logger.error("Connection is not received");
		} catch (SQLException e) {
			logger.error(e, e);
		}
		return connection;
	}

	public static void closeConnection(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			logger.error(e, e);
		}
	}
}

/*
 * public class ConnectionPool {
 * 
 * private static ConnectionPool instance = null; private static final String
 * TOMCAT_JNDI_NAME="java:comp/env"; private DataSource pool; private final
 * String DATASOURCE;
 * 
 * private ConnectionPool() { DATASOURCE =
 * Config.getInstance().getProperty(Config.DATASOURCE); initialPool(); }
 * 
 * public static synchronized ConnectionPool getInstance() { if (instance ==
 * null) { instance = new ConnectionPool(); } return instance; }
 * 
 * private void initialPool(){ try{ Context initContext = new InitialContext();
 * Context envContext = (Context) initContext.lookup(TOMCAT_JNDI_NAME); pool =
 * (DataSource)envContext.lookup(DATASOURCE); }catch(NamingException e){
 * e.printStackTrace(); } }
 * 
 * public synchronized Connection getConnection() throws SQLException{ return
 * pool.getConnection(); }
 * 
 * public void closeConnection(Connection connection){ try{ if(connection !=
 * null){ connection.close(); } }catch(SQLException e){ e.printStackTrace(); } }
 * }
 */
