package com.shpach.tutor.persistance.jdbc.dao.test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.jdbc.connection.Database;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

public class MySqlTestDao extends AbstractDao<Test> implements ITestDao {
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

	public List<Test> findtAll() {
		List<Test> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public Test addOrUpdate(Test test) {
		if (test.getTestId() == 0) {
			add(test);
		} else {
			update(test);
		}
		return test;
	}

	public Test findTestById(int id) {
		List<Test> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.test_id.name(), id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	public List<Test> findTestByCommunityId(int id) {
		String sql = SQL_SELECT_BY_COMMUNITY_ID;
		return getTestByRelationshiId(sql, id);
	}
	/*
	 * public List<Test> findTestByUserId(int id) { List<Test> res = null; try {
	 * res = findByDynamicSelect(SQL_SELECT, Columns.user_id.name(), id); }
	 * catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return res; }
	 */

	public List<Test> findTestByCategoryId(int id) {
		String sql = SQL_SELECT_BY_CATEGORY_ID;
		return getTestByRelationshiId(sql, id);
	}

	protected List<Test> getTestByRelationshiId(String sql, int id) {

		// final boolean isConnSupplied = (userConn != null);
		Database conn = MySqlDaoFactory.createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			// System.out.println("Executing " + SQL);
			// prepare statement
			stmt = conn.prepareStatement(sql);
			stmt.setObject(1, id);
			rs = stmt.executeQuery();
			// String executedQuery = rs.getStatement().toString();

			// fetch the results
			return fetchMultiResults(rs);
		} catch (Exception ex) {
			int a = 1;
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

	private void update(Test test) {
		PreparedStatement stmt = null;
		Database conn = null;
		try {

			String queryString = SQL_UPDATE;
			conn = MySqlDaoFactory.createConnection();
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, test.getTestName());
			stmt.setInt(2, test.getUserId());
			stmt.setString(3, test.getTestDescription());
			stmt.setByte(4, test.getTestRndQuestion());
			stmt.setByte(5, test.getTestRndAnswer());
			stmt.setInt(6, test.getTestType());
			stmt.setByte(7, test.getTestActive());
			stmt.setInt(8, test.getTestId());
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

	private void add(Test test) {
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
			stmt.setString(1, test.getTestName());
			stmt.setInt(2, test.getUserId());
			stmt.setString(3, test.getTestDescription());
			stmt.setByte(4, test.getTestRndQuestion());
			stmt.setByte(5, test.getTestRndAnswer());
			stmt.setInt(6, test.getTestType());
			stmt.setByte(7, test.getTestActive());
			stmt.executeUpdate();
			stmt.close();
			stmt = conn.prepareStatement("SELECT last_insert_id()");
			rs = stmt.executeQuery();
			while (rs.next()) {
				test.setTestId(rs.getInt(1));
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

}
