package com.shpach.tutor.persistance.jdbc.dao.testquestionbank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.tutor.persistance.entities.TestQuestionsBank;
import com.shpach.tutor.persistance.jdbc.connection.ConnectionPool;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;
import com.shpach.tutor.persistance.jdbc.dao.test.MySqlTestDao;

public class MySqlTestQuestionsBankDao extends AbstractDao<TestQuestionsBank> implements ITestQuestionsBankDao {
	private static final Logger logger = Logger.getLogger(MySqlTestQuestionsBankDao.class);

	protected enum Columns {

		test_questions_bank_id(1), test_id(2), question_id(3), question_default_sorting_order(4);
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

	protected static final String TABLE_NAME = "test_questions_bank";
	protected final String SQL_SELECT = "SELECT " + Columns.test_questions_bank_id.name() + ", "
			+ Columns.test_id.name() + ", " + Columns.question_id.name() + ", "
			+ Columns.question_default_sorting_order.name() + " FROM " + TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.test_id.name() + ", "
			+ Columns.question_id.name() + ", " + Columns.question_default_sorting_order.name() + ") VALUES (?, ?, ?)";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.test_id.name() + "=?, "
			+ Columns.question_id.name() + "=?, " + Columns.question_default_sorting_order.name() + "=? WHERE "
			+ Columns.test_questions_bank_id.name() + "=?";
	protected final String SQL_SELECT_MAX_ORDER = "SELECT MAX(" + Columns.question_default_sorting_order.name()
			+ ") FROM " + TABLE_NAME + " WHERE " + Columns.test_id.name() + "=?";

	private static MySqlTestQuestionsBankDao instance = null;

	private MySqlTestQuestionsBankDao() {

	}

	public static synchronized MySqlTestQuestionsBankDao getInstance() {
		if (instance == null)
			return instance = new MySqlTestQuestionsBankDao();
		else
			return instance;

	}
	@Override
	protected TestQuestionsBank populateDto(ResultSet rs) throws SQLException {
		TestQuestionsBank dto = new TestQuestionsBank();
		dto.setTestQuestionsBankId(rs.getInt(Columns.test_questions_bank_id.getId()));
		dto.setTestId(rs.getInt(Columns.test_id.getId()));
		dto.setQuestionId(rs.getInt(Columns.question_id.getId()));
		dto.setQuestionDefaultSortingOrder(rs.getInt(Columns.question_default_sorting_order.getId()));
		return dto;
	}

	public List<TestQuestionsBank> findAll() {
		List<TestQuestionsBank> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, null, null);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	public TestQuestionsBank addOrUpdate(TestQuestionsBank testQuestionsBank) {
		boolean res = false;
		if (testQuestionsBank.getTestQuestionsBankId() == 0) {
			res = add(testQuestionsBank);
		} else {
			res = update(testQuestionsBank);
		}
		if (res == false)
			return null;
		return testQuestionsBank;
	}

	private boolean update(TestQuestionsBank testQuestionsBank) {
		Object[] sqlParams = new Object[] { testQuestionsBank.getTestId(), testQuestionsBank.getQuestionId(),
				testQuestionsBank.getQuestionDefaultSortingOrder(), testQuestionsBank.getTestQuestionsBankId() };
		return dynamicUpdate(SQL_UPDATE, sqlParams);
	}

	private boolean add(TestQuestionsBank testQuestionsBank) {
		Object[] sqlParams = new Object[] { testQuestionsBank.getTestId(), testQuestionsBank.getQuestionId(),
				testQuestionsBank.getQuestionDefaultSortingOrder() };
		int id = dynamicAdd(SQL_INSERT, sqlParams);
		if (id > 0) {
			testQuestionsBank.setTestQuestionsBankId(id);
			return true;
		}
		return false;
	}

	public TestQuestionsBank findTestQuestionsBankById(int id) {
		List<TestQuestionsBank> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.test_questions_bank_id.name(), Integer.toString(id));
		} catch (Exception e) {
			logger.error(e, e);
		}
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	public List<TestQuestionsBank> findTestQuestionsBankByTestId(int id) {
		List<TestQuestionsBank> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.test_id.name(), Integer.toString(id));
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	@Override
	public List<TestQuestionsBank> findTestQuestionsBankByQuestionId(int id) {
		List<TestQuestionsBank> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.question_id.name(), Integer.toString(id));
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	@Override
	public int findMaxquestionDefaultSortingOrderByTestId(int testId) {

		//ConnectionPool pool = ConnectionPool.getInstance();
		try {

			Connection cn = null;
			try {
				cn =ConnectionPool.getConnection();// pool.getConnection();
				PreparedStatement st = null;
				String SQL = SQL_SELECT_MAX_ORDER;
				try {
					ResultSet rs = null;
					st = cn.prepareStatement(SQL);
					st.setInt(1, testId);
					try {
						rs = st.executeQuery();
						int res = 0;
						while (rs.next()) {
							res = rs.getInt(1);
						}
						return res;
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
			logger.error(e, e);
			return 0;
		}
	}

}
