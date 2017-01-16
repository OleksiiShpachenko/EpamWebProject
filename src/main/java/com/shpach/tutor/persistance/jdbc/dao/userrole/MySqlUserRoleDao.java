package com.shpach.tutor.persistance.jdbc.dao.userrole;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.shpach.tutor.persistance.entities.UsersRole;
import com.shpach.tutor.persistance.jdbc.connection.Database;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

public class MySqlUserRoleDao extends AbstractDao<UsersRole> implements IUserRoleDao {
	protected enum Columns {

		role_id(1), role_name(2), role_description(3);
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
	protected final String SQL_SELECT = "SELECT " + Columns.role_id.name() + ", " + Columns.role_name.name() + ", "
			+ Columns.role_description.name() + " FROM " + TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.role_name.name() + ", "
			+ Columns.role_description.name() + ") VALUES (?, ?)";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.role_name.name() + "=?, "
			+ Columns.role_description.name() + "=? WHERE " + Columns.role_id.name() + "=?";

	
	public List<UsersRole> findAll() {
		List<UsersRole> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	
	public UsersRole addOrUpdate(UsersRole usersRole) {
		if (usersRole.getRoleId() == 0) {
			add(usersRole);
		} else {
			update(usersRole);
		}
		return usersRole;
	}

	private void update(UsersRole usersRole) {
		PreparedStatement stmt = null;
		Database conn = null;
		try {

			String queryString = SQL_UPDATE;
			conn = MySqlDaoFactory.createConnection();
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, usersRole.getRoleName());
			stmt.setString(2, usersRole.getRoleDescription());
			stmt.setInt(3, usersRole.getRoleId());
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

	private void add(UsersRole usersRole) {
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
			stmt.setString(1, usersRole.getRoleName());
			stmt.setString(2, usersRole.getRoleDescription());
			stmt.executeUpdate();
			stmt.close();
			stmt = conn.prepareStatement("SELECT last_insert_id()");
			rs = stmt.executeQuery();
			while (rs.next()) {
				usersRole.setRoleId(rs.getInt(1));
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

	
	public UsersRole findUsersRoleById(int id) {
		List<UsersRole> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.role_id.name(), id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	
	protected UsersRole populateDto(ResultSet rs) throws SQLException {
		UsersRole dto = new UsersRole();
		dto.setRoleId(rs.getInt(Columns.role_id.getId()));
		dto.setRoleName(rs.getString(Columns.role_name.getId()));
		dto.setRoleDescription(rs.getString(Columns.role_description.getId()));
		return dto;
	}

}
