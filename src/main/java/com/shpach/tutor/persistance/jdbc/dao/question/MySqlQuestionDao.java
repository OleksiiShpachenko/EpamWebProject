package com.shpach.tutor.persistance.jdbc.dao.question;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.tutor.persistance.entities.Question;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;

public class MySqlQuestionDao extends AbstractDao<Question> implements IQuestionDao {
	private static final Logger logger = Logger.getLogger(MySqlQuestionDao.class);

	protected enum Columns {

		question_id(1), user_id(2), question_text(3), question_active(4), question_name(5);
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
	protected final String SQL_SELECT = "SELECT " + Columns.question_id.name() + ", " + Columns.user_id.name() + ", "
			+ Columns.question_text.name() + ", " + Columns.question_active.name() + ", " + Columns.question_name.name()
			+ " FROM " + TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.user_id.name() + ", "
			+ Columns.question_text.name() + ", " + Columns.question_active.name() + ", " + Columns.question_name.name()
			+ ") VALUES (?, ?, ?,?)";

	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.user_id.name() + "=?, "
			+ Columns.question_text.name() + "=?, " + Columns.question_active.name() + "=?, "
			+ Columns.question_name.name() + "=? WHERE " + Columns.question_active.name() + "=?";

	private static MySqlQuestionDao instance = null;

	private MySqlQuestionDao() {

	}

	public static synchronized MySqlQuestionDao getInstance() {
		if (instance == null)
			return instance = new MySqlQuestionDao();
		else
			return instance;

	}

	@Override
	public Question populateDto(ResultSet rs) throws SQLException {
		Question dto = new Question();
		dto.setQuestionId(rs.getInt(Columns.question_id.getId()));
		dto.setUserId(rs.getInt(Columns.user_id.getId()));
		dto.setQuestionText(rs.getString(Columns.question_text.getId()));
		dto.setQuestionActive(rs.getByte(Columns.question_active.getId()));
		dto.setQuestionName(rs.getString(Columns.question_name.getId()));
		return dto;
	}

	public List<Question> findAll() {
		return findByDynamicSelect(SQL_SELECT, null, null);
	}

	public Question addOrUpdate(Question question) {
		boolean res = false;
		if (question.getQuestionId() == 0) {
			res = add(question);
		} else {
			res = update(question);
		}
		if (res == true)
			return question;
		return null;
	}

	private boolean update(Question question) {
		Object[] sqlParams = new Object[] { question.getUserId(), question.getQuestionText(),
				question.getQuestionActive(), question.getQuestionName(), question.getQuestionId() };
		return dynamicUpdate(SQL_UPDATE, sqlParams);
	}

	private boolean add(Question question) {
		Object[] sqlParams = new Object[] { question.getUserId(), question.getQuestionText(),
				question.getQuestionActive(), question.getQuestionName() };
		int id = dynamicAdd(SQL_INSERT, sqlParams);
		if (id > 0) {
			question.setQuestionId(id);
			return true;
		}
		return false;
	}

	public Question findQuestionById(int id) {
		List<Question> res = findByDynamicSelect(SQL_SELECT, Columns.question_id.name(), Integer.toString(id));
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	public List<Question> findQuestionByUserId(int id) {
		return findByDynamicSelect(SQL_SELECT, Columns.user_id.name(), Integer.toString(id));
	}

}
