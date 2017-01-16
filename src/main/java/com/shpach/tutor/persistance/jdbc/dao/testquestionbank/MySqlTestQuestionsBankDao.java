package com.shpach.tutor.persistance.jdbc.dao.testquestionbank;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.shpach.tutor.persistance.entities.TestQuestionsBank;
import com.shpach.tutor.persistance.jdbc.connection.Database;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

public class MySqlTestQuestionsBankDao extends AbstractDao<TestQuestionsBank> implements ITestQuestionsBankDao {
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
	// protected static final String TABLE_USER_TO_COMMUNITY_RELATIONSHIP =
	// "user_to_community_relationship";
	protected final String SQL_SELECT = "SELECT " + Columns.test_questions_bank_id.name() + ", "
			+ Columns.test_id.name() + ", " + Columns.question_id.name() + ", "
			+ Columns.question_default_sorting_order.name() + " FROM " + TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.test_id.name() + ", "
			+ Columns.question_id.name() + ", " + Columns.question_default_sorting_order.name() + ") VALUES (?, ?, ?)";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.test_id.name() + "=?, "
			+ Columns.question_id.name() + "=?, " + Columns.question_default_sorting_order.name()  + "=? WHERE "
			+ Columns.test_questions_bank_id.name() + "=?";

	/*
	 * // last_insert_id();"; protected final String SQL_SELECT_BY_USER_ID =
	 * "SELECT " + TABLE_NAME + "." + Columns.community_id.name() + ", " +
	 * TABLE_NAME + "." + Columns.community_name.name() + ", " + TABLE_NAME +
	 * "." + Columns.community_description.name() + ", " + TABLE_NAME + "." +
	 * Columns.community_active.name() + " FROM " + TABLE_NAME + ", " +
	 * TABLE_USER_TO_COMMUNITY_RELATIONSHIP + " WHERE " +
	 * TABLE_USER_TO_COMMUNITY_RELATIONSHIP + ".user_id=? AND " +
	 * TABLE_USER_TO_COMMUNITY_RELATIONSHIP + "." + Columns.community_id.name()
	 * + "=" + TABLE_NAME + "." + Columns.community_id.name();
	 */
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
			e.printStackTrace();
		}
		return res;
	}

	
	public TestQuestionsBank addOrUpdate(TestQuestionsBank testQuestionsBank) {
		if (testQuestionsBank.getTestQuestionsBankId() == 0) {
			add(testQuestionsBank);
		} else {
			update(testQuestionsBank);
		}
		return testQuestionsBank;
	}

	private void update(TestQuestionsBank testQuestionsBank) {
		PreparedStatement stmt = null;
		Database conn = null;
		try {

			String queryString = SQL_UPDATE;
			conn = MySqlDaoFactory.createConnection();
			stmt = conn.prepareStatement(queryString);
			stmt.setInt(1, testQuestionsBank.getTestId());
			stmt.setInt(2, testQuestionsBank.getQuestionId());
			stmt.setInt(3, testQuestionsBank.getQuestionDefaultSortingOrder());
			stmt.setInt(4, testQuestionsBank.getTestQuestionsBankId());
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

	private void add(TestQuestionsBank testQuestionsBank) {
		PreparedStatement stmt = null;
		Database conn = null;
		ResultSet rs = null;
		try {

			String queryString = SQL_INSERT;
			conn = MySqlDaoFactory.createConnection();
			//int i = conn.getConnection().getTransactionIsolation();
			conn.getConnection().setAutoCommit(false);
			// conn.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			stmt = conn.prepareStatement(queryString);
			stmt.setInt(1, testQuestionsBank.getTestId());
			stmt.setInt(2, testQuestionsBank.getQuestionId());
			stmt.setInt(3, testQuestionsBank.getQuestionDefaultSortingOrder());
			stmt.executeUpdate();
			stmt.close();
			stmt = conn.prepareStatement("SELECT last_insert_id()");
			rs = stmt.executeQuery();
			while (rs.next()) {
				testQuestionsBank.setTestQuestionsBankId(rs.getInt(1));
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
	
	public TestQuestionsBank findTestQuestionsBankById(int id) {
		List<TestQuestionsBank> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.test_questions_bank_id.name(), Integer.toString(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}


	@Override
	public List<TestQuestionsBank> findTestQuestionsBankByQuestionId(int id) {
		List<TestQuestionsBank> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.question_id.name(), Integer.toString(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

}
