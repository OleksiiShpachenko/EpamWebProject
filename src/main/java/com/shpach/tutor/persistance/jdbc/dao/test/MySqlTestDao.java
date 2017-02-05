package com.shpach.tutor.persistance.jdbc.dao.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;
import com.shpach.tutor.persistance.jdbc.dao.category.MySqlCategoryDao;

public class MySqlTestDao extends AbstractDao<Test> implements ITestDao {
	private static final Logger logger = Logger.getLogger(MySqlTestDao.class);

	protected enum Columns {

		test_id(1), user_id(2), test_name(3), test_description(4), test_rnd_question(5), test_rnd_answer(6), test_type(
				7), test_active(8);
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

	protected static final String TABLE_NAME = "test";
	protected static final String TABLE_TEST_TO_COMMUNITY_RELATIONSHIP = "test_to_community_relationship";
	protected static final String TABLE_TEST_TO_CATEGORY_RELATIONSHIP = "test_to_category_relationship";
	protected final String SQL_SELECT = "SELECT " + Columns.test_id.name() + ", " + Columns.user_id.name() + ", "
			+ Columns.test_name.name() + ", " + Columns.test_description.name() + ", "
			+ Columns.test_rnd_question.name() + ", " + Columns.test_rnd_answer.name() + ", " + Columns.test_type.name()
			+ ", " + Columns.test_active.name() + " FROM " + TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.test_name.name() + ", "
			+ Columns.user_id.name() + ", " + Columns.test_description.name() + ", " + Columns.test_rnd_question.name()
			+ ", " + Columns.test_rnd_answer.name() + ", " + Columns.test_type.name() + ", "
			+ Columns.test_active.name() + ") VALUES (?, ?,?, ? ,? ,? ,?)";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.test_name.name() + "=?, "
			+ Columns.user_id.name() + "=?, " + Columns.test_description.name() + "=?, "
			+ Columns.test_rnd_question.name() + "=?, " + Columns.test_rnd_answer.name() + "=?, "
			+ Columns.test_type.name() + "=?, " + Columns.test_active.name() + "=? WHERE " + Columns.test_id.name()
			+ "=?";
	protected final String SQL_SELECT_BY_COMMUNITY_ID = "SELECT " + TABLE_NAME + "." + Columns.test_id.name() + ", "
			+ TABLE_NAME + "." + Columns.user_id.name() + ", " + TABLE_NAME + "." + Columns.test_name.name() + ", "
			+ TABLE_NAME + "." + Columns.test_description.name() + ", " + TABLE_NAME + "."
			+ Columns.test_rnd_question.name() + ", " + TABLE_NAME + "." + Columns.test_rnd_answer.name() + ", "
			+ TABLE_NAME + "." + Columns.test_type.name() + ", " + TABLE_NAME + "." + Columns.test_active.name()
			+ " FROM " + TABLE_NAME + ", " + TABLE_TEST_TO_COMMUNITY_RELATIONSHIP + " WHERE "
			+ TABLE_TEST_TO_COMMUNITY_RELATIONSHIP + ".community_id=? AND " + TABLE_TEST_TO_COMMUNITY_RELATIONSHIP + "."
			+ Columns.test_id.name() + "=" + TABLE_NAME + "." + Columns.test_id.name();
	protected final String SQL_SELECT_BY_CATEGORY_ID = "SELECT " + TABLE_NAME + "." + Columns.test_id.name() + ", "
			+ TABLE_NAME + "." + Columns.user_id.name() + ", " + TABLE_NAME + "." + Columns.test_name.name() + ", "
			+ TABLE_NAME + "." + Columns.test_description.name() + ", " + TABLE_NAME + "."
			+ Columns.test_rnd_question.name() + ", " + TABLE_NAME + "." + Columns.test_rnd_answer.name() + ", "
			+ TABLE_NAME + "." + Columns.test_type.name() + ", " + TABLE_NAME + "." + Columns.test_active.name()
			+ " FROM " + TABLE_NAME + ", " + TABLE_TEST_TO_CATEGORY_RELATIONSHIP + " WHERE "
			+ TABLE_TEST_TO_CATEGORY_RELATIONSHIP + ".category_id=? AND " + TABLE_TEST_TO_CATEGORY_RELATIONSHIP + "."
			+ Columns.test_id.name() + "=" + TABLE_NAME + "." + Columns.test_id.name();
	
	private static MySqlTestDao instance = null;

	private MySqlTestDao() {

	}

	public static synchronized MySqlTestDao getInstance() {
		if (instance == null)
			return instance = new MySqlTestDao();
		else
			return instance;

	}
	
	public List<Test> findtAll() {
		List<Test> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, null, null);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	public Test addOrUpdate(Test test) {
		boolean res = false;
		if (test.getTestId() == 0) {
			res = add(test);
		} else {
			res = update(test);
		}
		if (res == false)
			return null;
		return test;

	}

	public Test findTestById(int id) {
		List<Test> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.test_id.name(), id);
		} catch (Exception e) {
			logger.error(e, e);
		}
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	public List<Test> findTestByCommunityId(int id) {
		String sql = SQL_SELECT_BY_COMMUNITY_ID;
		return getTestByRelationshiId(sql, id);
	}

	public List<Test> findTestByCategoryId(int id) {
		String sql = SQL_SELECT_BY_CATEGORY_ID;
		return getTestByRelationshiId(sql, id);
	}

	protected List<Test> getTestByRelationshiId(String sql, int id) {

		List<Test> res = null;
		try {
			res = findByDynamicSelect(sql, new Integer[] { id });
		} catch (Exception e) {
			logger.error(e, e);
		}
		if (res != null && res.size() > 0)
			return res;
		return null;
	}

	private boolean update(Test test) {

		Object[] sqlParams = new Object[] { test.getTestName(), test.getUserId(), test.getTestDescription(),
				test.getTestRndQuestion(), test.getTestRndAnswer(), test.getTestType(), test.getTestActive(),
				test.getTestId() };
		return dynamicUpdate(SQL_UPDATE, sqlParams);
	}

	private boolean add(Test test) {
		Object[] sqlParams = new Object[] { test.getTestName(), test.getUserId(), test.getTestDescription(),
				test.getTestRndQuestion(), test.getTestRndAnswer(), test.getTestType(), test.getTestActive() };
		int id = dynamicAdd(SQL_INSERT, sqlParams);
		if (id > 0) {
			test.setTestId(id);
			return true;
		}
		return false;
	}

	@Override
	protected Test populateDto(ResultSet rs) throws SQLException {
		Test dto = new Test();
		dto.setTestId(rs.getInt(Columns.test_id.getId()));
		dto.setUserId(rs.getInt(Columns.user_id.getId()));
		dto.setTestName(rs.getString(Columns.test_name.getId()));
		dto.setTestDescription(rs.getString(Columns.test_description.getId()));
		dto.setTestRndQuestion(rs.getByte(Columns.test_rnd_question.getId()));
		dto.setTestRndAnswer(rs.getByte(Columns.test_rnd_answer.getId()));
		dto.setTestType(rs.getInt(Columns.test_type.getId()));
		dto.setTestActive(rs.getByte(Columns.test_active.getId()));
		return dto;
	}

	public List<Test> findTestByUserId(int id) {
		List<Test> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.user_id.name(), id);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

}
