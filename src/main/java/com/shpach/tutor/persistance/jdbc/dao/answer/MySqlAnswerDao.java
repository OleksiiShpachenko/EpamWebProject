package com.shpach.tutor.persistance.jdbc.dao.answer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.shpach.tutor.persistance.entities.Answer;
import com.shpach.tutor.persistance.jdbc.connection.Database;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

public class MySqlAnswerDao extends AbstractDao<Answer> implements IAnswerDao {
	protected enum Columns {

		answer_id(1), question_id(2), answer_text(3), answer_correct(4), answer_active(5), answer_default_sorting_order(
				6);
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

	protected static final String TABLE_NAME = "answer";
	//protected static final String TABLE_USER_TO_COMMUNITY_RELATIONSHIP = "user_to_community_relationship";
	protected final String SQL_SELECT = "SELECT " + Columns.answer_id.name() + ", " + Columns.question_id.name() + ", "
			+ Columns.answer_text.name() + ", " + Columns.answer_correct.name() + ", " + Columns.answer_active.name()
			+ ", " + Columns.answer_default_sorting_order.name() + " FROM " + TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.question_id.name() + ", "
			+ Columns.answer_text.name() + ", " + Columns.answer_correct.name() + ", " + Columns.answer_active.name()
			+ ", " + Columns.answer_default_sorting_order.name() + ") VALUES (?, ?, ?,?,?)";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.question_id.name() + "=?, "
			+ Columns.answer_text.name() + "=?, " + Columns.answer_correct.name() + "=?, "
			+ Columns.answer_active.name() + "=?, " + Columns.answer_default_sorting_order.name() + "=? WHERE "
			+ Columns.answer_id.name() + "=?";
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
	protected Answer populateDto(ResultSet rs) throws SQLException {
		Answer dto= new Answer();
		dto.setAnswerId(rs.getInt(Columns.answer_id.getId()));
		dto.setQuestionId(rs.getInt(Columns.question_id.getId()));
		dto.setAnswerText(rs.getString(Columns.answer_text.getId()));
		dto.setAnswerCorrect(rs.getByte(Columns.answer_correct.getId()));
		dto.setAnswerActive(rs.getByte(Columns.answer_active.getId()));
		dto.setAnswerDefaultSortingOrder(rs.getInt(Columns.answer_default_sorting_order.getId()));
		return dto;
	}

	
	public List<Answer> findAll() {
		List<Answer> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	
	public Answer addOrUpdate(Answer answer) {
		if (answer.getQuestionId() == 0) {
			add(answer);
		} else {
			update(answer);
		}
		return answer;
	}

	private void update(Answer answer) {
		PreparedStatement stmt = null;
		Database conn = null;
		try {

			String queryString = SQL_UPDATE;
			conn = MySqlDaoFactory.createConnection();
			stmt = conn.prepareStatement(queryString);
			stmt.setInt(1, answer.getQuestionId());
			stmt.setString(2, answer.getAnswerText());
			stmt.setByte(3, answer.getAnswerCorrect());
			stmt.setByte(4, answer.getAnswerActive());
			stmt.setInt(5, answer.getAnswerDefaultSortingOrder());
			stmt.setInt(6, answer.getAnswerId());
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

	private void add(Answer answer) {
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
			stmt.setInt(1, answer.getQuestionId());
			stmt.setString(2, answer.getAnswerText());
			stmt.setByte(3, answer.getAnswerCorrect());
			stmt.setByte(4, answer.getAnswerActive());
			stmt.setInt(5, answer.getAnswerDefaultSortingOrder());
			stmt.executeUpdate();
			stmt.close();
			stmt = conn.prepareStatement("SELECT last_insert_id()");
			rs = stmt.executeQuery();
			while (rs.next()) {
				answer.setAnswerId(rs.getInt(1));
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


	
	public Answer findAnswerById(int id) {
		List<Answer> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.answer_id.name(), Integer.toString(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	
	public List<Answer> findAnswerByQuestionId(int id) {
		List<Answer> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.question_id.name(), Integer.toString(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	

}
