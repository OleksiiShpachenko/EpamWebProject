package com.shpach.tutor.service;

import java.util.ArrayList;
import java.util.List;

import com.shpach.tutor.persistance.entities.Answer;
import com.shpach.tutor.persistance.entities.Question;
import com.shpach.tutor.persistance.jdbc.dao.answer.IAnswerDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

/**
 * Collection of services for {@link Answer} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class AnswerService {

	private static AnswerService instance = null;
	private IAnswerDao answerDao;

	private AnswerService() {

	}

	public static synchronized AnswerService getInstance() {
		if (instance == null) {
			instance = new AnswerService();
		}
		return instance;
	}

	public IAnswerDao getAnswerDao() {
		if (answerDao == null) {
			IDaoFactory daoFactory = new MySqlDaoFactory();
			answerDao = daoFactory.getAnswerDao();
		}
		return answerDao;
	}

	/**
	 * Add new list of {@link Answer} entities to database
	 * 
	 * @param answers
	 *            - list of {@link Answer} entities
	 * @return boolean values is operation success
	 */
	public boolean addNewAnswersList(List<Answer> answers) {
		if (answers == null)
			return false;
		for (Answer item : answers) {
			Answer answer = getAnswerDao().addOrUpdate(item);
			if (answer == null)
				return false;
		}
		return true;
	}

	/**
	 * Gets the list of {@link Answer} entities from database by Question
	 * 
	 * @param item
	 *            - Question entity
	 * @return List of {@link Answer} entities
	 */
	public List<Answer> getAnswersByQuestion(Question item) {
		if (item == null)
			return new ArrayList<Answer>();
		List<Answer> res = getAnswerDao().findAnswerByQuestionId(item.getQuestionId());
		if (res == null)
			res = new ArrayList<Answer>();
		return res;

	}

	/**
	 * Gets {@link Answer} from database by Id
	 * 
	 * @param answerId
	 * @return {@link Answer}
	 */
	public Answer getAnswerById(int answerId) {
		return getAnswerDao().findAnswerById(answerId);
	}

}
