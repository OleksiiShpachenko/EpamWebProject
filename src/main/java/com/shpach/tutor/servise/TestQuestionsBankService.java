package com.shpach.tutor.servise;

import java.util.List;

import com.shpach.tutor.persistance.entities.Question;
import com.shpach.tutor.persistance.entities.TestQuestionsBank;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.testquestionbank.ITestQuestionsBankDao;

public class TestQuestionsBankService {

	public static List<TestQuestionsBank> getTestQuestionsBankByQuestion(Question question) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ITestQuestionsBankDao testQuestionsBankDao = daoFactory.getTestQuestionsBankDao();
		List<TestQuestionsBank> testQuestionsBanks=testQuestionsBankDao.findTestQuestionsBankByQuestionId(question.getQuestionId());
		return testQuestionsBanks;
	}

}
