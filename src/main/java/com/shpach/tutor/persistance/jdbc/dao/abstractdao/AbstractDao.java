package com.shpach.tutor.persistance.jdbc.dao.abstractdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shpach.tutor.persistance.jdbc.connection.Database;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

public abstract class AbstractDao<T> {
	
	public List<T> findByDynamicSelect(String sql, String paramColumn, Object paramValue) throws Exception {

		// final boolean isConnSupplied = (userConn != null);
		Database conn = MySqlDaoFactory.createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String SQL = sql;
			// System.out.println("Executing " + SQL);
			// prepare statement
			if (paramColumn != null && paramValue != null) {
				SQL = SQL + " WHERE " + paramColumn + "=?";

				stmt = conn.prepareStatement(SQL);
				// stmt.setMaxRows(maxRows);
				// bind parameters
				stmt.setObject(1, paramValue);
				// stmt.setInt(2,5);//setObject(2, paramValue);

			} else {
				stmt = conn.prepareStatement(SQL);
			}
			rs = stmt.executeQuery();
			//String executedQuery = rs.getStatement().toString();

			// fetch the results
			return fetchMultiResults(rs);
		} catch (Exception ex) {
			// logger.error(ex, ex);
			throw new Exception("Exception: " + ex.getMessage(), ex);
		} finally {
			rs.close();
			stmt.close();
			conn.close();
			// ! if (!isConnSupplied) {
			// ! connPool.returnConnection(conn);
			// ! }

		}

	}
	protected T fetchSingleResult(ResultSet rs) throws SQLException {
		if (rs.next()) {
			//Test dto = new Test();
			T dto=populateDto(rs);
			return dto;
		} else {
			return null;
		}

	}

	protected List<T> fetchMultiResults(ResultSet rs) throws SQLException {
		List<T> resultList = new ArrayList<T>();
		while (rs.next()) {
			//T dto = new T();
			T dto =populateDto(rs);
			resultList.add(dto);
		}
		return resultList;
	}

	abstract protected  T populateDto(ResultSet rs) throws SQLException;


}
