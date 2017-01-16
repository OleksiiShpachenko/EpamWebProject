package com.shpach.tutor.persistance.jdbc.dao.testquestionbank;

import java.util.List;

import com.shpach.tutor.persistance.entities.TestQuestionsBank;

public interface ITestQuestionsBankDao {
	public List<TestQuestionsBank> findAll();
	public TestQuestionsBank addOrUpdate(TestQuestionsBank testQuestionsBank);
	public TestQuestionsBank findTestQuestionsBankById(int id);
	public List<TestQuestionsBank> findTestQuestionsBankByTestId(int id);
	public List<TestQuestionsBank> findTestQuestionsBankByQuestionId(int id);
}
