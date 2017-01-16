package com.shpach.tutor.persistance.jdbc.dao.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.connection.Database;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

public class MySqlUserDao extends AbstractDao<User> implements IUserDao {
	protected enum Columns {

		user_id(1), role_id(2), user_email(3), user_password(4);
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

	protected static final String TABLE_NAME = "user";
	protected static final String TABLE_USER_TO_COMMUNITY_RELATIONSHIP = "user_to_community_relationship";
	protected final String SQL_SELECT = "SELECT " + Columns.user_id.name() + ", " + Columns.role_id.name() + ", "
			+ Columns.user_email.name() + ", " + Columns.user_password.name() + " FROM " + TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.role_id.name() + ", "
			+ Columns.user_email.name() + ", " + Columns.user_password.name() + ") VALUES (?, ?, ?)";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.role_id.name() + "=?, "
			+ Columns.user_email.name() + "=?, " + Columns.user_password.name() + "=? WHERE " + Columns.user_id.name()
			+ "=?";
	protected final String SQL_SELECT_BY_COMMUNITY_ID = "SELECT " + TABLE_NAME + "." + Columns.user_id.name() + ", "
			+ TABLE_NAME + "." + Columns.role_id.name() + ", " + TABLE_NAME + "."
			+ Columns.user_email.name() + ", " + TABLE_NAME + "." + Columns.user_password.name()
			+ " FROM " + TABLE_NAME + ", " + TABLE_USER_TO_COMMUNITY_RELATIONSHIP + " WHERE "
			+ TABLE_USER_TO_COMMUNITY_RELATIONSHIP + ".community_id=? AND " + TABLE_USER_TO_COMMUNITY_RELATIONSHIP + "."
			+ Columns.user_id.name() + "=" + TABLE_NAME + "." + Columns.user_id.name();
	
	public List<User> findAll() {
		List<User> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	
	public User addOrUpdate(User user) {
		if (user.getRoleId() == 0) {
			add(user);
		} else {
			update(user);
		}
		return user;
	}

	private void update(User user) {
		PreparedStatement stmt = null;
		Database conn = null;
		try {

			String queryString = SQL_UPDATE;
			conn = MySqlDaoFactory.createConnection();
			stmt = conn.prepareStatement(queryString);
			stmt.setInt(1, user.getRoleId());
			stmt.setString(2, user.getUserEmail());
			stmt.setString(3, user.getUserPassword());
			stmt.setInt(4, user.getUserId());
			stmt.executeUpdate();
			System.out.println("Data Updated Successfully");
		} catch (SQLException e) {
			e.printStackTrace();
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

	private void add(User user) {
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
			stmt.setInt(1, user.getRoleId());
			stmt.setString(2, user.getUserEmail());
			stmt.setString(3, user.getUserPassword());
			stmt.executeUpdate();
			stmt.close();
			stmt = conn.prepareStatement("SELECT last_insert_id()");
			rs = stmt.executeQuery();
			while (rs.next()) {
				user.setUserId(rs.getInt(1));
			}
			// conn.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.getConnection().setAutoCommit(true);

			System.out.println("Data Added Successfully");
		} catch (SQLException e) {
			e.printStackTrace();
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

	
	public User findUserById(int id) {
		List<User> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.user_id.name(), id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	
	public User findUserByEmail(String login) {
		List<User> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.user_email.name(), login);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	@Override
	protected User populateDto(ResultSet rs) throws SQLException {
		User dto = new User();
		dto.setUserId(rs.getInt(Columns.user_id.getId()));
		dto.setUserEmail(rs.getString(Columns.user_email.getId()));
		dto.setUserPassword(rs.getString(Columns.user_password.getId()));
		dto.setRoleId(rs.getInt(Columns.role_id.getId()));
		return dto;
	}


	@Override
	public List<User> findUsersByCommunityId(int id) {

		
		// final boolean isConnSupplied = (userConn != null);
		Database conn = MySqlDaoFactory.createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String SQL = SQL_SELECT_BY_COMMUNITY_ID;
			// System.out.println("Executing " + SQL);
			// prepare statement
			stmt = conn.prepareStatement(SQL);
			stmt.setObject(1, id);
			rs = stmt.executeQuery();
			//String executedQuery = rs.getStatement().toString();

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

}
