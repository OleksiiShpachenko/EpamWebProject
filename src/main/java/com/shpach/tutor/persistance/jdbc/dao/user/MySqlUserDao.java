package com.shpach.tutor.persistance.jdbc.dao.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;

public class MySqlUserDao extends AbstractDao<User> implements IUserDao {
	private static final Logger logger = Logger.getLogger(MySqlUserDao.class);

	protected enum Columns {

		user_id(1), role_id(2), user_email(3), user_password(4), user_name(5);
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
			+ Columns.user_email.name() + ", " + Columns.user_password.name() + ", " + Columns.user_name.name()
			+ " FROM " + TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.role_id.name() + ", "
			+ Columns.user_email.name() + ", " + Columns.user_password.name() + ", " + Columns.user_name.name()
			+ ") VALUES (?, ?, ?,?)";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.role_id.name() + "=?, "
			+ Columns.user_email.name() + "=?, " + Columns.user_password.name() + "=?, " + Columns.user_name.name()
			+ "=? WHERE " + Columns.user_id.name() + "=?";
	protected final String SQL_SELECT_BY_COMMUNITY_ID = "SELECT " + TABLE_NAME + "." + Columns.user_id.name() + ", "
			+ TABLE_NAME + "." + Columns.role_id.name() + ", " + TABLE_NAME + "." + Columns.user_email.name() + ", "
			+ TABLE_NAME + "." + Columns.user_password.name() + ", " + TABLE_NAME + "." + Columns.user_name.name()
			+ " FROM " + TABLE_NAME + ", " + TABLE_USER_TO_COMMUNITY_RELATIONSHIP + " WHERE "
			+ TABLE_USER_TO_COMMUNITY_RELATIONSHIP + ".community_id=? AND " + TABLE_USER_TO_COMMUNITY_RELATIONSHIP + "."
			+ Columns.user_id.name() + "=" + TABLE_NAME + "." + Columns.user_id.name();

	private static MySqlUserDao instance = null;

	private MySqlUserDao() {

	}

	public static synchronized MySqlUserDao getInstance() {
		if (instance == null)
			return instance = new MySqlUserDao();
		else
			return instance;

	}

	public List<User> findAll() {
		List<User> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, null, null);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	public User addOrUpdate(User user) {
		boolean res = false;
		if (user.getUserId() == 0) {
			res = add(user);
		} else {
			res = update(user);
		}
		if (res == false)
			return null;
		return user;
	}

	private boolean update(User user) {

		Object[] sqlParams = new Object[] { user.getRoleId(), user.getUserEmail(), user.getUserPassword(),
				user.getUserName(), user.getUserId() };
		return dynamicUpdate(SQL_UPDATE, sqlParams);
	}

	private boolean add(User user) {
		Object[] sqlParams = new Object[] { user.getRoleId(), user.getUserEmail(), user.getUserPassword(),
				user.getUserName() };
		int id = dynamicAdd(SQL_INSERT, sqlParams);
		if (id > 0) {
			user.setUserId(id);
			return true;
		}
		return false;
	}

	public User findUserById(int id) {
		List<User> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.user_id.name(), id);
		} catch (Exception e) {
			logger.error(e, e);
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
			logger.error(e, e);
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
		dto.setUserName(rs.getString(Columns.user_name.getId()));
		return dto;
	}

	@Override
	public List<User> findUsersByCommunityId(int id) {

		List<User> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT_BY_COMMUNITY_ID, new Integer[] { id });
		} catch (Exception e) {
			logger.error(e, e);
		}
		if (res != null && res.size() > 0)
			return res;
		return null;
	}

}
