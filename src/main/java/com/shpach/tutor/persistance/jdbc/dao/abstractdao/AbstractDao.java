package com.shpach.tutor.persistance.jdbc.dao.abstractdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.shpach.tutor.persistance.jdbc.connection.ConnectionPool;

/**Abstract generic class which perform base realization of CRUD operations using JDBC and connection pool
 * @author Shpachenko_A_K
 *
 * @param <T> - 
 */
public abstract class AbstractDao<T> {
	 

	public List<T> findByDynamicSelect(String sql, String paramColumn, Object paramValue) throws Exception {
		try {

			Connection cn = null;
			try {
				cn = ConnectionPool.getConnection();
				PreparedStatement st = null;
				String SQL = sql;
				try {
					if (paramColumn != null && paramValue != null) {
						SQL = SQL + " WHERE " + paramColumn + "=?";

						st = cn.prepareStatement(SQL);
						st.setObject(1, paramValue);

					} else {
						st = cn.prepareStatement(SQL);
					}
					ResultSet rs = null;
					try {
						rs = st.executeQuery();
						return fetchMultiResults(rs);
					} finally {
						if (rs != null) {
							rs.close();
						}
					}
				} finally {
					if (st != null) {
						st.close();
					}
				}
			} finally {
				if (cn != null) {
					cn.close();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<T> findByDynamicSelect(String sql, Object[] sqlParams) throws Exception {

		//ConnectionPool pool = ConnectionPool.getInstance();
		try {

			Connection cn = null;
			try {
				cn = ConnectionPool.getConnection();//pool.getConnection();
				PreparedStatement st = null;
				String SQL = sql;
				try {
					ResultSet rs = null;
					st = cn.prepareStatement(SQL);
					for (int i = 0; sqlParams != null && i < sqlParams.length; i++) {
						st.setObject(i + 1, sqlParams[i]);
					}
					try {
						rs = st.executeQuery();
						return fetchMultiResults(rs);
					} finally {
						if (rs != null) {
							rs.close();
						}
					}
				} finally {
					if (st != null) {
						st.close();
					}
				}
			} finally {
				if (cn != null) {
					cn.close();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected int dynamicAdd(String sql, Object[] sqlParams) {
		int res = 0;
		try {

			Connection cn = null;
			try {
				cn =ConnectionPool.getConnection();
				cn.setAutoCommit(false);
				PreparedStatement st = null;
				try {
					String SQL = sql;
					st = cn.prepareStatement(SQL);
					for (int i = 0; sqlParams != null && i < sqlParams.length; i++) {
						st.setObject(i + 1, sqlParams[i]);
					}
					st.executeUpdate();
					st.close();
					st = cn.prepareStatement("SELECT last_insert_id()");
					ResultSet rs = null;

					try {
						rs = st.executeQuery();
						while (rs.next()) {
							res = rs.getInt(1);
						}
					} finally {
						if (rs != null) {
							rs.close();
						}
					}
					cn.setAutoCommit(true);
					return res;
				} finally {
					if (st != null) {
						st.close();
					}
				}
			} finally {
				if (cn != null) {
					cn.close();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return res;
		}

	}

	protected boolean dynamicUpdate(String sql, Object[] sqlParams) {
		try {

			Connection cn = null;
			try {
				cn = ConnectionPool.getConnection();
				PreparedStatement st = null;
				try {
					String SQL = sql;
					st = cn.prepareStatement(SQL);
					for (int i = 0; sqlParams != null && i < sqlParams.length; i++) {
						st.setObject(i + 1, sqlParams[i]);
					}
					st.executeUpdate();
					return true;
				} finally {
					if (st != null) {
						st.close();
					}
				}
			} finally {
				if (cn != null) {
					cn.close();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	protected T fetchSingleResult(ResultSet rs) throws SQLException {
		if (rs.next()) {
			T dto = populateDto(rs);
			return dto;
		} else {
			return null;
		}

	}

	protected List<T> fetchMultiResults(ResultSet rs) throws SQLException {
		List<T> resultList = new ArrayList<T>();
		while (rs.next()) {
			T dto = populateDto(rs);
			resultList.add(dto);
		}
		return resultList;
	}

	/**Parse  ResultSet and return Entity class according generic type<T>
	 * @param rs ResultSet
	 * @return
	 * @throws SQLException
	 */
	abstract protected T populateDto(ResultSet rs) throws SQLException;

}
