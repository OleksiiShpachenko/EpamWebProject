package com.shpach.tutor.persistance.jdbc.dao.answerlog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.tutor.persistance.entities.AnswersLog;
import com.shpach.tutor.persistance.entities.Category;
import com.shpach.tutor.persistance.jdbc.connection.ConnectionPool;
import com.shpach.tutor.persistance.jdbc.connection.ConnectionPoolTomCatFactory;
import com.shpach.tutor.persistance.jdbc.connection.IConnectionPoolFactory;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;


public class MySqlAnswerLogDao extends AbstractDao<AnswersLog> implements IAnswerLogDao {
	private static final Logger logger = Logger.getLogger(MySqlAnswerLogDao.class);

	protected enum Columns {

		answer_log_id(1), question_log_id(2), answer_id(3), answer_checked(4), answer_log_sorting_order(5);
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

	protected static final String TABLE_NAME = "answers_log";
	protected final String SQL_SELECT = "SELECT " + Columns.answer_log_id.name() + ", " + Columns.question_log_id.name()
			+ ", " + Columns.answer_id.name() + ", " + Columns.answer_checked.name() + ", "
			+ Columns.answer_log_sorting_order.name() + " FROM " + TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.question_log_id.name() + ", "
			+ Columns.answer_id.name() + ", " + Columns.answer_checked.name() + ", "
			+ Columns.answer_log_sorting_order.name() + ") VALUES (?, ?,?,?)";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.question_log_id.name() + "=?, "
			+ Columns.answer_id.name() + "=?, " + Columns.answer_checked.name() + "=?, "
			+ Columns.answer_log_sorting_order.name() + "=? WHERE " + Columns.answer_log_id.name() + "=?";

	private static MySqlAnswerLogDao instance = null;

	private MySqlAnswerLogDao() {

	}

	public static synchronized MySqlAnswerLogDao getInstance() {
		if (instance == null)
			return instance = new MySqlAnswerLogDao();
		else
			return instance;

	}
	@Override
	public AnswersLog populateDto(ResultSet rs) throws SQLException {
		AnswersLog dto = new AnswersLog();
		dto.setAnswerLogId(rs.getInt(Columns.answer_log_id.getId()));
		dto.setAnswerChecked(rs.getByte(Columns.answer_checked.getId()));
		dto.setAnswerId(rs.getInt(Columns.answer_id.getId()));
		dto.setAnswerLogSortingOrder(rs.getInt(Columns.answer_log_sorting_order.getId()));
		return dto;
	}

	@Override
	public AnswersLog addOrUpdate(AnswersLog answersLog) {
		Connection connection = null;
		AnswersLog answersLogNew = null;
		try {
			//connection =ConnectionPool.getConnection();
			IConnectionPoolFactory connectionFactory = (IConnectionPoolFactory) new ConnectionPoolTomCatFactory();
			connection= connectionFactory.getConnection();
			connection.setAutoCommit(false);
			answersLogNew = addOrUpdate(answersLog, connection);
			connection.setAutoCommit(true);
			connection.close();
		} catch (SQLException e) {
			logger.error(e, e);
		}

		return answersLogNew;
	}

	@Override
	public AnswersLog addOrUpdate(AnswersLog answersLog, Connection connection) {
		boolean res = false;
		if (answersLog.getAnswerLogId() == 0) {
			res = add(answersLog, connection);
		} else {
			res = update(answersLog, connection);
		}
		if (res == false)
			return null;
		return answersLog;
	}

	private boolean update(AnswersLog answersLog, Connection connection) {
		try {
			PreparedStatement st = null;
			try {
				String SQL = SQL_UPDATE;
				st = connection.prepareStatement(SQL);
				st.setInt(1, answersLog.getQuestionLog().getQuestionLogId());
				st.setInt(2, answersLog.getAnswer().getAnswerId());
				st.setByte(3, answersLog.getAnswerChecked());
				st.setInt(4, answersLog.getAnswerLogSortingOrder());
				st.setInt(5, answersLog.getAnswerLogId());
				st.executeUpdate();
				return true;
			} finally {
				if (st != null) {
					st.close();
				}
			}
		} catch (SQLException e) {
			logger.error(e, e);
			return false;
		}
	}

	private boolean add(AnswersLog answersLog, Connection connection) {
		try {
			PreparedStatement st = null;
			try {
				String SQL = SQL_INSERT;
				st = connection.prepareStatement(SQL);
				st.setInt(1, answersLog.getQuestionLog().getQuestionLogId());
				st.setInt(2, answersLog.getAnswer().getAnswerId());
				st.setByte(3, answersLog.getAnswerChecked());
				st.setInt(4, answersLog.getAnswerLogSortingOrder());
				st.executeUpdate();
				st.close();
				st = connection.prepareStatement("SELECT last_insert_id()");
				ResultSet rs = null;
				try {
					rs = st.executeQuery();
					while (rs.next()) {
						answersLog.setAnswerLogId(rs.getInt(1));
					}
				} finally {
					if (rs != null) {
						rs.close();
					}
				}
				return true;
			} finally {
				if (st != null) {
					st.close();
				}
			}

		} catch (SQLException e) {
			logger.error(e, e);
			return false;
		}
	}

	@Override
	public List<AnswersLog> findAnswersLogByQuestionLogId(int questionLogId) {
		List<AnswersLog> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.question_log_id.name(), Integer.toString(questionLogId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;

	}
}
