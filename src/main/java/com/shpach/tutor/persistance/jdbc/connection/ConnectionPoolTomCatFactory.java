package com.shpach.tutor.persistance.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolTomCatFactory implements IConnectionPoolFactory {
	/* (non-Javadoc)
	 * @see com.shpach.tutor.persistance.jdbc.connection.IConnectionPoolFactory#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return ConnectionPoolTomCat.getInstance().getConnection();
	}
}
