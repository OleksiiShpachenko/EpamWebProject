package com.shpach.tutor.persistance.jdbc.dao.question;

import java.util.List;

import com.shpach.tutor.persistance.entities.Question;

public interface IQuestionDao {
	public List<Question> findAll();
	public Question addOrUpdate(Question question);
	public Question findQuestionById(int id);
	public List<Question> findQuestionByUserId(int id);
}
