package com.shpach.tutor.service;

import java.sql.Connection;

import com.shpach.tutor.persistance.entities.AnswersLog;
import com.shpach.tutor.persistance.jdbc.dao.answerlog.IAnswerLogDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

/**
 * Collection of services for {@link AnswersLog} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class AnswersLogService {

	/**
	 * Add new AnswersLog entity to database
	 * 
	 * @param answersLog
	 *            - AnswersLog entity
	 * @param connection
	 *            - {@link java.sql.Connection}
	 * @return - if operation success return AnswersLog entity with id, else
	 *         return null
	 */
	public static AnswersLog addAnswersLog(AnswersLog answersLog, Connection connection) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IAnswerLogDao answersLogDao = daoFactory.getAnswersLogDao();
		if (answersLogDao.addOrUpdate(answersLog, connection) == null)
			return null;
		return answersLog;
	}

}
