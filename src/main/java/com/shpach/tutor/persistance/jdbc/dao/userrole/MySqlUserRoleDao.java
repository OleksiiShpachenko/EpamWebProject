package com.shpach.tutor.persistance.jdbc.dao.userrole;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.tutor.persistance.entities.UsersRole;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;
import com.shpach.tutor.persistance.jdbc.dao.user.MySqlUserDao;

public class MySqlUserRoleDao extends AbstractDao<UsersRole> implements IUserRoleDao {
	private static final Logger logger = Logger.getLogger(MySqlUserRoleDao.class);

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

	protected static final String TABLE_NAME = "users_role";
	protected final String SQL_SELECT = "SELECT " + Columns.role_id.name() + ", " + Columns.role_name.name() + ", "
			+ Columns.role_description.name() + " FROM " + TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.role_name.name() + ", "
			+ Columns.role_description.name() + ") VALUES (?, ?)";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.role_name.name() + "=?, "
			+ Columns.role_description.name() + "=? WHERE " + Columns.role_id.name() + "=?";

	private static MySqlUserRoleDao instance = null;

	private MySqlUserRoleDao() {

	}

	public static synchronized MySqlUserRoleDao getInstance() {
		if (instance == null)
			return instance = new MySqlUserRoleDao();
		else
			return instance;

	}

	public List<UsersRole> findAll() {
		List<UsersRole> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, null, null);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	public UsersRole addOrUpdate(UsersRole usersRole) {
		boolean res = false;
		if (usersRole.getRoleId() == 0) {
			res = add(usersRole);
		} else {
			res = update(usersRole);
		}
		if (res == false)
			return null;
		return usersRole;
	}

	private boolean update(UsersRole usersRole) {
		Object[] sqlParams = new Object[] { usersRole.getRoleName(), usersRole.getRoleDescription(),
				usersRole.getRoleId() };
		return dynamicUpdate(SQL_UPDATE, sqlParams);
	}

	private boolean add(UsersRole usersRole) {
		Object[] sqlParams = new Object[] { usersRole.getRoleName(), usersRole.getRoleDescription() };
		int id = dynamicAdd(SQL_INSERT, sqlParams);
		if (id > 0) {
			usersRole.setRoleId(id);
			return true;
		}
		return false;
	}

	public UsersRole findUsersRoleById(int id) {
		List<UsersRole> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.role_id.name(), id);
		} catch (Exception e) {
			logger.error(e, e);
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
