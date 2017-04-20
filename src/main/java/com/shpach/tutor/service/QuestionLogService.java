package com.shpach.tutor.service;

import java.sql.Connection;
import com.shpach.tutor.persistance.entities.QuestionLog;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.questionlog.IQuestionLogDao;

public class QuestionLogService {
	private static QuestionLogService instance;
	private IQuestionLogDao questionLogDao;

	private QuestionLogService() {
	}

	public static QuestionLogService getInstance() {
		if (instance == null) {
			instance = new QuestionLogService();
		}
		return instance;
	}

	public IQuestionLogDao getQuestionLogDao() {
		if(questionLogDao==null){
			IDaoFactory daoFactory = new MySqlDaoFactory();
			questionLogDao = daoFactory.getQuestionLogDao();
		}	
		return questionLogDao;
	}

	public QuestionLog addQuestionLog(QuestionLog questionLog, Connection connection) {
		return questionLogDao.addOrUpdate(questionLog, connection);
	}

//	public  Question getHardQuestion() {
//		IDaoFactory daoFactory = new MySqlDaoFactory();
//		IQuestionLogDao questionLogDao = daoFactory.getQuestionLogDao();
//		List<QuestionLog> questionLogs = questionLogDao.findAll();
//		for (QuestionLog questionLog : questionLogs) {
//			insertQuestionToQuestionLog(questionLog);
//			insertAnswersLogToQuestionLog(questionLog);
//		}
//		return null;
//	}
//
//	private  void insertAnswersLogToQuestionLog(QuestionLog questionLog) {
//		questionLog.setAnswersLogs(AnswersLogService.getAnsversLogsByQuestionLog(questionLog));
//
//	}
//
//	private static void insertQuestionToQuestionLog(QuestionLog questionLog) {
//		QuestionService.getInstance().getQuestionByIdWithAnswers(questionLog.getQuestionId());
//
//	}
}
