package com.shpach.tutor.persistance.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionPoolFactory {

	Connection getConnection() throws SQLException;

}