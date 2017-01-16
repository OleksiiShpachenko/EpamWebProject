package com.shpach.tutor.persistance.jdbc.dao.community;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.jdbc.connection.Database;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

public class MySqlCommunityDao extends AbstractDao<Community> implements ICommunityDao {
	protected enum Columns {

		community_id(1), community_name(2), community_description(3), community_active(4);
		Columns(int id) {
			this.id = id;
		}

		private int id;

		public int getId() {
			return id;
		}

		public static String getClassName() {
			return Columns.class.getName();
		}

	}

	protected static final String TABLE_NAME = "community";
	protected static final String TABLE_USER_TO_COMMUNITY_RELATIONSHIP = "user_to_community_relationship";
	protected static final String TABLE_TEST_TO_COMMUNITY_RELATIONSHIP = "test_to_community_relationship";
	protected final String SQL_SELECT = "SELECT " + Columns.community_id.name() + ", " + Columns.community_name.name()
			+ ", " + Columns.community_description.name() + ", " + Columns.community_active.name() + " FROM "
			+ TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.community_name.name() + ", "
			+ Columns.community_description.name() + ", " + Columns.community_active.name() + ") VALUES (?, ?, ?)";// SELECT
																													// last_insert_id();";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.community_name.name() + "=?, "
			+ Columns.community_description.name() + "=?, " + Columns.community_active.name() + "=? WHERE "
			+ Columns.community_id.name() + "=?";// (?, ?, ?)";// SELECT
													// last_insert_id();";
	protected final String SQL_SELECT_BY_USER_ID = "SELECT " + TABLE_NAME + "." + Columns.community_id.name() + ", "
			+ TABLE_NAME + "." + Columns.community_name.name() + ", " + TABLE_NAME + "."
			+ Columns.community_description.name() + ", " + TABLE_NAME + "." + Columns.community_active.name()
			+ " FROM " + TABLE_NAME + ", " + TABLE_USER_TO_COMMUNITY_RELATIONSHIP + " WHERE "
			+ TABLE_USER_TO_COMMUNITY_RELATIONSHIP + ".user_id=? AND " + TABLE_USER_TO_COMMUNITY_RELATIONSHIP + "."
			+ Columns.community_id.name() + "=" + TABLE_NAME + "." + Columns.community_id.name();

	protected final String SQL_SELECT_BY_TEST_ID = "SELECT " + TABLE_NAME + "." + Columns.community_id.name() + ", "
			+ TABLE_NAME + "." + Columns.community_name.name() + ", " + TABLE_NAME + "."
			+ Columns.community_description.name() + ", " + TABLE_NAME + "." + Columns.community_active.name()
			+ " FROM " + TABLE_NAME + ", " + TABLE_TEST_TO_COMMUNITY_RELATIONSHIP + " WHERE "
			+ TABLE_TEST_TO_COMMUNITY_RELATIONSHIP + ".test_id=? AND " + TABLE_TEST_TO_COMMUNITY_RELATIONSHIP + "."
			+ Columns.community_id.name() + "=" + TABLE_NAME + "." + Columns.community_id.name();
	protected final String SQL_INSERT_USER_TOCOMMUNITY = "INSERT INTO " + TABLE_USER_TO_COMMUNITY_RELATIONSHIP + " (" + Columns.community_id.name() + ", "
			+ "user_id" + ") VALUES (?, ?)";
	protected final String SQL_INSERT_TEST_TOCOMMUNITY = "INSERT INTO " + TABLE_TEST_TO_COMMUNITY_RELATIONSHIP + " (" + Columns.community_id.name() + ", "
			+ "test_id" + ") VALUES (?, ?)";
	public List<Community> findAll() {
		List<Community> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	/*
	 * public List<Community> findByDynamicSelect(String sql, String
	 * paramColumn, Object paramValue) throws Exception {
	 * 
	 * // final boolean isConnSupplied = (userConn != null); Database conn =
	 * MySqlDaoFactory.createConnection(); PreparedStatement stmt = null;
	 * ResultSet rs = null;
	 * 
	 * try { String SQL = sql; // System.out.println("Executing " + SQL); //
	 * prepare statement if (paramColumn != null && paramValue != null) { SQL =
	 * SQL + " WHERE " + paramColumn + "=?";
	 * 
	 * stmt = conn.prepareStatement(SQL); // stmt.setMaxRows(maxRows); // bind
	 * parameters stmt.setObject(1, paramValue); //
	 * stmt.setInt(2,5);//setObject(2, paramValue);
	 * 
	 * } else { stmt = conn.prepareStatement(SQL); } rs = stmt.executeQuery();
	 * //String executedQuery = rs.getStatement().toString();
	 * 
	 * // fetch the results return fetchMultiResults(rs); } catch (Exception ex)
	 * { // logger.error(ex, ex); throw new Exception("Exception: " +
	 * ex.getMessage(), ex); } finally { rs.close(); stmt.close(); conn.close();
	 * // ! if (!isConnSupplied) { // ! connPool.returnConnection(conn); // ! }
	 * 
	 * }
	 * 
	 * } /* protected Community fetchSingleResult(ResultSet rs) throws
	 * SQLException { if (rs.next()) { Community dto = new Community();
	 * populateDto(dto, rs); return dto; } else { return null; }
	 * 
	 * }
	 * 
	 * protected List<Community> fetchMultiResults(ResultSet rs) throws
	 * SQLException { List<Community> resultList = new ArrayList<Community>();
	 * while (rs.next()) { Community dto = new Community(); populateDto(dto,
	 * rs); resultList.add(dto); } return resultList; }
	 * 
	 */

	public Community addOrUpdate(Community community) {
		boolean res = false;
		if (community.getCommunityId() == 0) {
			res = add(community);
		} else {
			res = update(community);
		}
		if (res == true)
			return community;
		return null;

	}

	private boolean update(Community community) {
		PreparedStatement stmt = null;
		Database conn = null;
		try {

			String queryString = SQL_UPDATE;
			conn = MySqlDaoFactory.createConnection();
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, community.getCommunityName());
			stmt.setString(2, community.getCommunityDescription());
			stmt.setByte(3, community.getCommunityActive());
			stmt.setInt(4, community.getCommunityId());
			stmt.executeUpdate();
			System.out.println("Data Updated Successfully");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private boolean add(Community community) {
		PreparedStatement stmt = null;
		Database conn = null;
		ResultSet rs = null;
		try {

			String queryString = SQL_INSERT;
			conn = MySqlDaoFactory.createConnection();
			// int i = conn.getConnection().getTransactionIsolation();
			conn.getConnection().setAutoCommit(false);
			// conn.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, community.getCommunityName());
			stmt.setString(2, community.getCommunityDescription());
			stmt.setByte(3, community.getCommunityActive());
			stmt.executeUpdate();
			stmt.close();
			stmt = conn.prepareStatement("SELECT last_insert_id()");
			rs = stmt.executeQuery();
			while (rs.next()) {
				community.setCommunityId(rs.getInt(1));
			}
			// conn.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.getConnection().setAutoCommit(true);

			System.out.println("Data Added Successfully");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				rs.close();
				if (stmt != null) {
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public Community findCommunityById(int id) {
		List<Community> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.community_id.name(), Integer.toString(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	public List<Community> findCommunityByUserId(int id) {

		// final boolean isConnSupplied = (userConn != null);
		Database conn = MySqlDaoFactory.createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String SQL = SQL_SELECT_BY_USER_ID;
			// System.out.println("Executing " + SQL);
			// prepare statement
			stmt = conn.prepareStatement(SQL);
			stmt.setObject(1, id);
			rs = stmt.executeQuery();
			// String executedQuery = rs.getStatement().toString();

			// fetch the results
			return fetchMultiResults(rs);
		} catch (Exception ex) {
			// logger.error(ex, ex);
			// throw new Exception("Exception: " + ex.getMessage(), ex);
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// ! if (!isConnSupplied) {
			// ! connPool.returnConnection(conn);
			// ! }

		}
		return null;

	}

	public List<Community> findCommunityByTestId(int id) {

		// final boolean isConnSupplied = (userConn != null);
		Database conn = MySqlDaoFactory.createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String SQL = SQL_SELECT_BY_TEST_ID;
			// System.out.println("Executing " + SQL);
			// prepare statement
			stmt = conn.prepareStatement(SQL);
			stmt.setObject(1, id);
			rs = stmt.executeQuery();
			// String executedQuery = rs.getStatement().toString();

			// fetch the results
			return fetchMultiResults(rs);
		} catch (Exception ex) {
			// logger.error(ex, ex);
			// throw new Exception("Exception: " + ex.getMessage(), ex);
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// ! if (!isConnSupplied) {
			// ! connPool.returnConnection(conn);
			// ! }

		}
		return null;

	}

	@Override
	protected Community populateDto(ResultSet rs) throws SQLException {
		Community dto = new Community();
		dto.setCommunityId(rs.getInt(Columns.community_id.getId()));
		dto.setCommunityName(rs.getString(Columns.community_name.getId()));
		dto.setCommunityDescription(rs.getString(Columns.community_description.getId()));
		dto.setCommunityActive(rs.getByte(Columns.community_active.getId()));
		return dto;
	}

	@Override
	public boolean assignUserToCommunity(int userId, int communityId) {
		PreparedStatement stmt = null;
		Database conn = null;
		
		try {

			String queryString = SQL_INSERT_USER_TOCOMMUNITY;
			conn = MySqlDaoFactory.createConnection();
 		stmt = conn.prepareStatement(queryString);
			stmt.setInt(1, communityId);
			stmt.setInt(2, userId);
			
			stmt.executeUpdate();
			stmt.close();
		System.out.println("Data Added Successfully");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				
				if (stmt != null) {
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public boolean assignTestToCommunity(int testId, int communityId) {
		PreparedStatement stmt = null;
		Database conn = null;
		
		try {

			String queryString = SQL_INSERT_TEST_TOCOMMUNITY;
			conn = MySqlDaoFactory.createConnection();
 		stmt = conn.prepareStatement(queryString);
			stmt.setInt(1, communityId);
			stmt.setInt(2, testId);
			
			stmt.executeUpdate();
			stmt.close();
		System.out.println("Data Added Successfully");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				
				if (stmt != null) {
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
