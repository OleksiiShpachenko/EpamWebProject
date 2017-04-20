package com.shpach.tutor.persistance.jdbc.dao.questionlog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.tutor.persistance.entities.QuestionLog;
import com.shpach.tutor.persistance.jdbc.connection.ConnectionPoolTomCatFactory;
import com.shpach.tutor.persistance.jdbc.connection.IConnectionPoolFactory;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;


public class MySqlQuestionLogDao extends AbstractDao<QuestionLog> implements IQuestionLogDao {
	private static final Logger logger = Logger.getLogger(MySqlQuestionLogDao.class);

	protected enum Columns {

		question_log_id(1), task_id(2), question_id(3), question_log_sorting_order(4);
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

	protected static final String TABLE_NAME = "question_log";
	protected final String SQL_SELECT = "SELECT " + Columns.question_log_id.name() + ", " + Columns.task_id.name()
			+ ", " + Columns.question_id.name() + ", " + Columns.question_log_sorting_order.name() + " FROM "
			+ TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.task_id.name() + ", "
			+ Columns.question_id.name() + ", " + Columns.question_log_sorting_order.name() + ") VALUES (?, ?,?)";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.task_id.name() + "=?, "
			+ Columns.question_id.name() + "=?, " + Columns.question_log_sorting_order.name() + "=? WHERE "
			+ Columns.question_log_id.name() + "=?";
	
	private static MySqlQuestionLogDao instance = null;

	private MySqlQuestionLogDao() {

	}

	public static synchronized MySqlQuestionLogDao getInstance() {
		if (instance == null)
			return instance = new MySqlQuestionLogDao();
		else
			return instance;

	}
	public List<QuestionLog> findAll() {
		List<QuestionLog> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, null, null);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	@Override
	public QuestionLog populateDto(ResultSet rs) throws SQLException {
		QuestionLog dto = new QuestionLog();
		dto.setQuestionLogId(rs.getInt(Columns.question_log_id.getId()));
		dto.setQuestionId((rs.getInt(Columns.question_id.getId())));
		return dto;
	}
	
	

	@Override
	public QuestionLog addOrUpdate(QuestionLog questionLog) {
		Connection connection = null;
		QuestionLog questionLogNew = null;

		try {
			//connection = ConnectionPool.getConnection();
			IConnectionPoolFactory connectionFactory = (IConnectionPoolFactory) new ConnectionPoolTomCatFactory();
			connection= connectionFactory.getConnection();
			connection.setAutoCommit(false);
			questionLogNew = addOrUpdate(questionLog, connection);
			connection.setAutoCommit(true);
			connection.close();
		} catch (SQLException e) {
			logger.error(e, e);
		}

		return questionLogNew;
	}

	@Override
	public QuestionLog addOrUpdate(QuestionLog questionLog, Connection connection) {
		boolean res = false;
		if (questionLog.getQuestionLogId() == 0) {
			res = add(questionLog, connection);
		} else {
			res = update(questionLog, connection);
		}
		if (res == false)
			return null;
		return questionLog;
	}

	private boolean update(QuestionLog questionLog, Connection connection) {
		try {
			PreparedStatement st = null;
			try {
				String SQL = SQL_UPDATE;
				st = connection.prepareStatement(SQL);
				st.setInt(1, questionLog.getTask().getTaskId());
				st.setInt(2, questionLog.getQuestion().getQuestionId());
				st.setInt(3, questionLog.getQuestionLogSortingOrder());
				st.setInt(4, questionLog.getQuestionLogId());
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

	private boolean add(QuestionLog questionLog, Connection connection) {
		try {
			PreparedStatement st = null;
			try {
				String SQL = SQL_INSERT;
				st = connection.prepareStatement(SQL);
				st.setInt(1, questionLog.getTask().getTaskId());
				st.setInt(2, questionLog.getQuestion().getQuestionId());
				st.setInt(3, questionLog.getQuestionLogSortingOrder());
				st.executeUpdate();
				st.close();
				st = connection.prepareStatement("SELECT last_insert_id()");
				ResultSet rs = null;
				try {
					rs = st.executeQuery();
					while (rs.next()) {
						questionLog.setQuestionLogId(rs.getInt(1));
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
}
