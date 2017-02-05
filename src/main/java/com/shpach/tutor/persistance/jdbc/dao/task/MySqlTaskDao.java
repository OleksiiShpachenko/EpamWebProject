package com.shpach.tutor.persistance.jdbc.dao.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.tutor.persistance.entities.Task;
import com.shpach.tutor.persistance.jdbc.connection.ConnectionPool;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;


public class MySqlTaskDao extends AbstractDao<Task> implements ITaskDao {
	private static final Logger logger = Logger.getLogger(MySqlTaskDao.class);

	protected enum Columns {

		task_id(1), user_id(2), test_id(3), task_datetime_start(4), task_datetime_stop(5), task_score(
				6), task_category_id(7);
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

	protected static final String TABLE_NAME = "task";
	protected final String SQL_SELECT = "SELECT " + Columns.task_id.name() + ", " + Columns.user_id.name() + ", "
			+ Columns.test_id.name() + ", " + Columns.task_datetime_start.name() + ", "
			+ Columns.task_datetime_stop.name() + ", " + Columns.task_score.name() + ", "
			+ Columns.task_category_id.name() + " FROM " + TABLE_NAME + "";
	protected final String SQL_SELECT_BY_TEST_ID_AND_USER_ID = "SELECT " + Columns.task_id.name() + ", "
			+ Columns.user_id.name() + ", " + Columns.test_id.name() + ", " + Columns.task_datetime_start.name() + ", "
			+ Columns.task_datetime_stop.name() + ", " + Columns.task_score.name() + ", "
			+ Columns.task_category_id.name() + " FROM " + TABLE_NAME + " WHERE " + Columns.test_id.name() + "=? AND "
			+ Columns.user_id.name() + "=?";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.user_id.name() + ", "
			+ Columns.test_id.name() + ", " + Columns.task_datetime_start.name() + ", "
			+ Columns.task_datetime_stop.name() + ", " + Columns.task_score.name() + ", "
			+ Columns.task_category_id.name() + ") VALUES (?, ?,?, ? ,? ,?)";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.user_id.name() + "=?, "
			+ Columns.test_id.name() + "=?, " + Columns.task_datetime_start.name() + "=?, "
			+ Columns.task_datetime_stop.name() + "=?, " + Columns.task_score.name() + "=?, "
			+ Columns.task_category_id.name() + "=? WHERE " + Columns.task_id.name() + "=?";

	private static MySqlTaskDao instance = null;

	private MySqlTaskDao() {

	}

	public static synchronized MySqlTaskDao getInstance() {
		if (instance == null)
			return instance = new MySqlTaskDao();
		else
			return instance;

	}
	
	@Override
	protected Task populateDto(ResultSet rs) throws SQLException {
		Task dto = new Task();
		dto.setTaskId(rs.getInt(Columns.task_id.getId()));
		dto.setUserId(rs.getInt(Columns.user_id.getId()));
		// dto.setTestId(rs.getString(Columns.test_id.getId()));
		dto.setTaskDatetimeStart(new Date(rs.getTimestamp(Columns.task_datetime_start.getId()).getTime()));
		dto.setTaskDatetimeStop(new Date(rs.getTimestamp(Columns.task_datetime_stop.getId()).getTime()));
		dto.setTaskScore(rs.getByte(Columns.task_score.getId()));
		dto.setTaskCategoryId(rs.getInt(Columns.task_category_id.getId()));
		return dto;
	}

	@Override
	public Task addOrUpdate(Task task) {
		//ConnectionPool pool = ConnectionPool.getInstance();
		Connection cn = null;
		Task taskNew = null;
		try {
			cn =ConnectionPool.getConnection();// pool.getConnection();
			cn.setAutoCommit(false);
			taskNew = addOrUpdate(task, cn);
			cn.setAutoCommit(true);
			cn.close();
		} catch (SQLException e) {
			logger.error(e, e);
		}
		return taskNew;
	}

	@Override
	public Task addOrUpdate(Task task, Connection connection) {
		boolean res = false;
		if (task.getTaskId() == 0) {
			res = add(task, connection);
		} else {
			res = update(task, connection);
		}
		if (res == false)
			return null;
		return task;
	}

	private boolean update(Task task, Connection connection) {
		try {
			PreparedStatement st = null;
			try {
				String SQL = SQL_UPDATE;
				st = connection.prepareStatement(SQL);
				st.setInt(1, task.getUser().getUserId());
				st.setInt(2, task.getTest().getTestId());
				st.setTimestamp(3, new java.sql.Timestamp(task.getTaskDatetimeStart().getTime()));
				st.setTimestamp(4, new java.sql.Timestamp(task.getTaskDatetimeStop().getTime()));
				st.setByte(5, task.getTaskScore());
				st.setInt(6, task.getTaskCategoryId());
				st.setInt(7, task.getTaskId());
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

	private boolean add(Task task, Connection connection) {
		try {
			PreparedStatement st = null;
			try {
				String SQL = SQL_INSERT;
				st = connection.prepareStatement(SQL);
				st.setInt(1, task.getUser().getUserId());
				st.setInt(2, task.getTest().getTestId());
				st.setTimestamp(3, new java.sql.Timestamp(task.getTaskDatetimeStart().getTime()));
				st.setTimestamp(4, new java.sql.Timestamp(task.getTaskDatetimeStop().getTime()));
				st.setByte(5, task.getTaskScore());
				st.setInt(6, task.getTaskCategoryId());
				st.executeUpdate();
				st.close();
				st = connection.prepareStatement("SELECT last_insert_id()");
				ResultSet rs = null;
				try {
					rs = st.executeQuery();
					while (rs.next()) {
						task.setTaskId(rs.getInt(1));
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
	public List<Task> findTaskByTestIdAndUserId(int testId, int userId) {
		try {
			return findByDynamicSelect(SQL_SELECT_BY_TEST_ID_AND_USER_ID, new Integer[] { testId, userId });
		} catch (Exception e) {
			logger.error(e, e);
			return null;
		}

	}

	@Override
	public List<Task> findTaskByTestId(int testId) {
		try {
			return findByDynamicSelect(SQL_SELECT, Columns.test_id.name(), testId);
		} catch (Exception e) {
			logger.error(e, e);
			return null;
		}
	}
}
