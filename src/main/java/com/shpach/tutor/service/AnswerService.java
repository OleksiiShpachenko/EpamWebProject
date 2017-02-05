package com.shpach.tutor.service;

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

	/**
	 * Add new list of Answer entities to database
	 * 
	 * @param answers
	 *            - list of Answer entities
	 * @return boolean values is operation success
	 */
	public static boolean addNewAnswersList(List<Answer> answers) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IAnswerDao answerDao = daoFactory.getAnswerDao();
		for (Answer item : answers) {
			Answer answer = answerDao.addOrUpdate(item);
			if (answer == null)
				return false;
		}
		return true;
	}

	/**
	 * Gets the list of Answer entities from database by Question
	 * 
	 * @param item
	 *            - Question entity
	 * @return List of Answer entities
	 */
	public static List<Answer> getAnswersByQuestion(Question item) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IAnswerDao answerDao = daoFactory.getAnswerDao();
		return answerDao.findAnswerByQuestionId(item.getQuestionId());

	}

}
