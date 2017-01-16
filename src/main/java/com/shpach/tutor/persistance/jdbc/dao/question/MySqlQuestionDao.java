package com.shpach.tutor.persistance.jdbc.dao.question;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.shpach.tutor.persistance.entities.Question;
import com.shpach.tutor.persistance.jdbc.connection.Database;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

public class MySqlQuestionDao extends AbstractDao<Question> implements IQuestionDao {
	protected enum Columns {

		question_id(1),user_id(2), question_text(3), question_active(4), community_active(5);
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
	protected static final String TABLE_NAME = "question";
	protected static final String TABLE_USER_TO_COMMUNITY_RELATIONSHIP = "user_to_community_relationship";
	protected final String SQL_SELECT = "SELECT " + Columns.question_id.name() + ", " + Columns.user_id.name()
			+ ", " + Columns.question_text.name() + ", " + Columns.question_active.name() + " FROM "
			+ TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.user_id.name() + ", "
			+ Columns.question_text.name() + ", " + Columns.question_active.name() + ") VALUES (?, ?, ?)";// SELECT
																													// last_insert_id();";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.user_id.name() + "=?, "
			+ Columns.question_text.name() + "=?, " + Columns.question_active.name() + "=? WHERE "
			+ Columns.question_active.name() + "=?";// (?, ?, ?)";// SELECT
	/*												// last_insert_id();";
	protected final String SQL_SELECT_BY_USER_ID = "SELECT " + TABLE_NAME + "." + Columns.community_id.name() + ", "
			+ TABLE_NAME + "." + Columns.community_name.name() + ", " + TABLE_NAME + "."
			+ Columns.community_description.name() + ", " + TABLE_NAME + "." + Columns.community_active.name()
			+ " FROM " + TABLE_NAME + ", " + TABLE_USER_TO_COMMUNITY_RELATIONSHIP + " WHERE "
			+ TABLE_USER_TO_COMMUNITY_RELATIONSHIP + ".user_id=? AND " + TABLE_USER_TO_COMMUNITY_RELATIONSHIP + "."
			+ Columns.community_id.name() + "=" + TABLE_NAME + "." + Columns.community_id.name();
*/
	@Override
	protected Question populateDto(ResultSet rs) throws SQLException {
		Question dto= new Question();
		dto.setQuestionId(rs.getInt(Columns.question_id.getId()));
		dto.setUserId(rs.getInt(Columns.user_id.getId()));
		dto.setQuestionText(rs.getString(Columns.question_text.getId()));
		dto.setQuestionActive(rs.getByte(Columns.question_active.getId()));
		return dto;
	}

	
	public List<Question> findAll() {
		List<Question> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	
	public Question addOrUpdate(Question question) {
		if (question.getQuestionId() == 0) {
			add(question);
		} else {
			update(question);
		}
		return question;
	}

	private void update(Question question) {
		PreparedStatement stmt = null;
		Database conn = null;
		try {

			String queryString = SQL_UPDATE;
			conn = MySqlDaoFactory.createConnection();
			stmt = conn.prepareStatement(queryString);
			stmt.setInt(1, question.getUserId());
			stmt.setString(2, question.getQuestionText());
			stmt.setByte(3, question.getQuestionActive());
			stmt.setInt(4, question.getQuestionId());
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

	private void add(Question question) {
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
			stmt.setInt(1, question.getUserId());
			stmt.setString(2, question.getQuestionText());
			stmt.setByte(3, question.getQuestionActive());
			stmt.executeUpdate();
			stmt.close();
			stmt = conn.prepareStatement("SELECT last_insert_id()");
			rs = stmt.executeQuery();
			while (rs.next()) {
				question.setQuestionId(rs.getInt(1));
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

	

	
	public Question findQuestionById(int id) {
		List<Question> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.question_id.name(), Integer.toString(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	
	public List<Question> findQuestionByUserId(int id) {
		List<Question> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.user_id.name(), Integer.toString(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

}
