package com.shpach.tutor.persistance.jdbc.dao.answer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.tutor.persistance.entities.Answer;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;

public class MySqlAnswerDao extends AbstractDao<Answer> implements IAnswerDao {
	private static final Logger logger = Logger.getLogger(MySqlAnswerDao.class);

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

	private static MySqlAnswerDao instance = null;

	private MySqlAnswerDao() {

	}

	public static synchronized MySqlAnswerDao getInstance() {
		if (instance == null)
			return instance = new MySqlAnswerDao();
		else
			return instance;

	}

	@Override
	public Answer populateDto(ResultSet rs) throws SQLException {
		Answer dto = new Answer();
		dto.setAnswerId(rs.getInt(Columns.answer_id.getId()));
		dto.setQuestionId(rs.getInt(Columns.question_id.getId()));
		dto.setAnswerText(rs.getString(Columns.answer_text.getId()));
		dto.setAnswerCorrect(rs.getByte(Columns.answer_correct.getId()));
		dto.setAnswerActive(rs.getByte(Columns.answer_active.getId()));
		dto.setAnswerDefaultSortingOrder(rs.getInt(Columns.answer_default_sorting_order.getId()));
		return dto;
	}

	public List<Answer> findAll() {
		return findByDynamicSelect(SQL_SELECT, null, null);
	}

	public Answer addOrUpdate(Answer answer) {
		boolean res = false;
		if (answer.getAnswerId() == 0) {
			res = add(answer);
		} else {
			res = update(answer);
		}
		if (res == true)
			return answer;
		return null;
	}

	private boolean update(Answer answer) {
		Object[] sqlParams = new Object[] { answer.getQuestionId(), answer.getAnswerText(), answer.getAnswerCorrect(),
				answer.getAnswerActive(), answer.getAnswerDefaultSortingOrder(), answer.getAnswerId() };
		return dynamicUpdate(SQL_UPDATE, sqlParams);
	}

	private boolean add(Answer answer) {
		Object[] sqlParams = new Object[] { answer.getQuestionId(), answer.getAnswerText(), answer.getAnswerCorrect(),
				answer.getAnswerActive(), answer.getAnswerDefaultSortingOrder() };
		int id = dynamicAdd(SQL_INSERT, sqlParams);
		if (id > 0) {
			answer.setAnswerId(id);
			return true;
		}
		return false;
	}

	public Answer findAnswerById(int id) {
		List<Answer> res = null;
		res = findByDynamicSelect(SQL_SELECT, Columns.answer_id.name(), Integer.toString(id));

		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	public List<Answer> findAnswerByQuestionId(int id) {
		return findByDynamicSelect(SQL_SELECT, Columns.question_id.name(), Integer.toString(id));
	}
}
