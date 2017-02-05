package com.shpach.tutor.persistance.jdbc.dao.answerlog;

import java.sql.Connection;

import com.shpach.tutor.persistance.entities.AnswersLog;

public interface IAnswerLogDao {
	public AnswersLog addOrUpdate(AnswersLog answersLog);
	public AnswersLog addOrUpdate(AnswersLog answersLog, Connection connection);
}
