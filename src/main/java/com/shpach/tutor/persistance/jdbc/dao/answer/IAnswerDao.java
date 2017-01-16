package com.shpach.tutor.persistance.jdbc.dao.answer;

import java.util.List;

import com.shpach.tutor.persistance.entities.Answer;


public interface IAnswerDao {
	public List<Answer> findAll();
	public Answer addOrUpdate(Answer answer);
	public Answer findAnswerById(int id);
	public List<Answer> findAnswerByQuestionId(int id);
}
