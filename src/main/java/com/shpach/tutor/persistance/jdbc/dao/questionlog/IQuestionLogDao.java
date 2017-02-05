package com.shpach.tutor.persistance.jdbc.dao.questionlog;

import java.sql.Connection;

import com.shpach.tutor.persistance.entities.QuestionLog;

public interface IQuestionLogDao {
	public QuestionLog addOrUpdate(QuestionLog questionLog);
	public QuestionLog addOrUpdate(QuestionLog questionLog, Connection connection);
}
