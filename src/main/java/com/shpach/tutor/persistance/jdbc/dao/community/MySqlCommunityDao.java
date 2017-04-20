package com.shpach.tutor.persistance.jdbc.dao.community;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;

public class MySqlCommunityDao extends AbstractDao<Community> implements ICommunityDao {
	private static final Logger logger = Logger.getLogger(MySqlCommunityDao.class);

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
	protected static final String TABLE_CATEGORY_TO_COMMUNITY_RELATIONSHIP = "category_to_community_relationship";
	protected final String SQL_SELECT = "SELECT " + Columns.community_id.name() + ", " + Columns.community_name.name()
			+ ", " + Columns.community_description.name() + ", " + Columns.community_active.name() + " FROM "
			+ TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.community_name.name() + ", "
			+ Columns.community_description.name() + ", " + Columns.community_active.name() + ") VALUES (?, ?, ?)";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.community_name.name() + "=?, "
			+ Columns.community_description.name() + "=?, " + Columns.community_active.name() + "=? WHERE "
			+ Columns.community_id.name() + "=?";
	protected final String SQL_SELECT_BY_USER_ID = "SELECT " + TABLE_NAME + "." + Columns.community_id.name() + ", "
			+ TABLE_NAME + "." + Columns.community_name.name() + ", " + TABLE_NAME + "."
			+ Columns.community_description.name() + ", " + TABLE_NAME + "." + Columns.community_active.name()
			+ " FROM " + TABLE_NAME + ", " + TABLE_USER_TO_COMMUNITY_RELATIONSHIP + " WHERE "
			+ TABLE_USER_TO_COMMUNITY_RELATIONSHIP + ".user_id=? AND " + TABLE_USER_TO_COMMUNITY_RELATIONSHIP + "."
			+ Columns.community_id.name() + "=" + TABLE_NAME + "." + Columns.community_id.name();

	protected final String SQL_SELECT_BY_CATEGORY_ID = "SELECT " + TABLE_NAME + "." + Columns.community_id.name() + ", "
			+ TABLE_NAME + "." + Columns.community_name.name() + ", " + TABLE_NAME + "."
			+ Columns.community_description.name() + ", " + TABLE_NAME + "." + Columns.community_active.name()
			+ " FROM " + TABLE_NAME + ", " + TABLE_CATEGORY_TO_COMMUNITY_RELATIONSHIP + " WHERE "
			+ TABLE_CATEGORY_TO_COMMUNITY_RELATIONSHIP + ".category_id=? AND " + TABLE_CATEGORY_TO_COMMUNITY_RELATIONSHIP + "."
			+ Columns.community_id.name() + "=" + TABLE_NAME + "." + Columns.community_id.name();
	protected final String SQL_INSERT_USER_TOCOMMUNITY = "INSERT INTO " + TABLE_USER_TO_COMMUNITY_RELATIONSHIP + " ("
			+ Columns.community_id.name() + ", " + "user_id" + ") VALUES (?, ?)";
	protected final String SQL_INSERT_CATEGORY_TOCOMMUNITY = "INSERT INTO " + TABLE_CATEGORY_TO_COMMUNITY_RELATIONSHIP + " ("
			+ Columns.community_id.name() + ", " + "category_id" + ") VALUES (?, ?)";

	private static MySqlCommunityDao instance = null;

	private MySqlCommunityDao() {

	}

	public static synchronized MySqlCommunityDao getInstance() {
		if (instance == null)
			return instance = new MySqlCommunityDao();
		else
			return instance;

	}
	
	public List<Community> findAll() {
		return findByDynamicSelect(SQL_SELECT, null, null);
	}

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
		Object[] sqlParams = new Object[] { community.getCommunityName(), community.getCommunityDescription(),
				community.getCommunityActive(), community.getCommunityId() };
		return dynamicUpdate(SQL_UPDATE, sqlParams);
	}

	private boolean add(Community community) {
		Object[] sqlParams = new Object[] { community.getCommunityName(), community.getCommunityDescription(),
				community.getCommunityActive() };
		int id = dynamicAdd(SQL_INSERT, sqlParams);
		if (id > 0) {
			community.setCommunityId(id);
			return true;
		}
		return false;
	}

	public Community findCommunityById(int id) {
		List<Community> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.community_id.name(), Integer.toString(id));
		} catch (Exception e) {
			logger.error(e, e);
		}
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	public List<Community> findCommunityByUserId(int id) {
		List<Community> res = null;
			res = findByDynamicSelect(SQL_SELECT_BY_USER_ID, new Integer[] { id });
		if (res != null && res.size() > 0)
			return res;
		return new ArrayList<Community>();
	}

	public List<Community> findCommunityByCategoryId(int id) {
		List<Community> res = null;
			res = findByDynamicSelect(SQL_SELECT_BY_CATEGORY_ID, new Integer[] { id });
		if (res != null && res.size() > 0)
			return res;
		return new ArrayList<Community>();
	}

	@Override
	public Community populateDto(ResultSet rs) throws SQLException {
		Community dto = new Community();
		dto.setCommunityId(rs.getInt(Columns.community_id.getId()));
		dto.setCommunityName(rs.getString(Columns.community_name.getId()));
		dto.setCommunityDescription(rs.getString(Columns.community_description.getId()));
		dto.setCommunityActive(rs.getByte(Columns.community_active.getId()));
		return dto;
	}

	@Override
	public boolean assignUserToCommunity(int userId, int communityId) {
		Object[] sqlParams = new Object[] { communityId, userId };
		return dynamicUpdate(SQL_INSERT_USER_TOCOMMUNITY, sqlParams);
	}

	@Override
	public boolean assignCategoryToCommunity(int testId, int communityId) {
		Object[] sqlParams = new Object[] { communityId, testId };
		return dynamicUpdate(SQL_INSERT_CATEGORY_TOCOMMUNITY, sqlParams);
	}
}
