package com.shpach.tutor.service;

import java.sql.Connection;

import com.shpach.tutor.persistance.entities.QuestionLog;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.questionlog.IQuestionLogDao;

public class QuestionLogService {

	public static QuestionLog addQuestionLog(QuestionLog questionLog, Connection connection) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IQuestionLogDao questionLogDao = daoFactory.getQuestionLogDao();
		if (questionLogDao.addOrUpdate(questionLog, connection) == null)
			return null;
		return questionLog;
	}

}
