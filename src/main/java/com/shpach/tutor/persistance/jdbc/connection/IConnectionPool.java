package com.shpach.tutor.persistance.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionPool {
	Connection getConnection() throws SQLException;
	void closeConnection(Connection connection);
}
