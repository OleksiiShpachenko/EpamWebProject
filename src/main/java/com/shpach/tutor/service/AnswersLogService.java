package com.shpach.tutor.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.shpach.tutor.persistance.entities.AnswersLog;
import com.shpach.tutor.persistance.entities.QuestionLog;
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
	private static AnswersLogService instance;
	private IAnswerLogDao answersLogDao;
	private AnswerService answerService;

	private AnswersLogService() {
	}

	public static AnswersLogService getInstance() {
		if (instance == null) {
			instance = new AnswersLogService();
		}
		return instance;
	}

	private IAnswerLogDao getAnswersLogDao() {
		if (answersLogDao == null) {
			IDaoFactory daoFactory = new MySqlDaoFactory();
			answersLogDao = daoFactory.getAnswersLogDao();
		}
		return answersLogDao;
	}

	private AnswerService getAnswerService() {
		if (answerService == null)
			answerService = AnswerService.getInstance();
		return answerService;
	}

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
	public AnswersLog addAnswersLog(AnswersLog answersLog, Connection connection) {
		return getAnswersLogDao().addOrUpdate(answersLog, connection);

	}

	public List<AnswersLog> getAnsversLogsByQuestionLog(QuestionLog questionLog) {
		if (questionLog == null)
			return new ArrayList<AnswersLog>();
		List<AnswersLog> answersLogs = getAnswersLogDao().findAnswersLogByQuestionLogId(questionLog.getQuestionLogId());
		if (answersLogs == null)
			return new ArrayList<AnswersLog>();
		insertAnswersToAnswersLog(answersLogs);
		return answersLogs;
	}

	private void insertAnswersToAnswersLog(List<AnswersLog> answersLogs) {
		for (AnswersLog answersLog : answersLogs) {
			answersLog.setAnswer(getAnswerService().getAnswerById(answersLog.getAnswerId()));
		}

	}
}
