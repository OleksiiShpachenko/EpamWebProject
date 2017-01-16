package com.shpach.tutor.servise;

import java.util.List;

import com.shpach.tutor.persistance.entities.Question;
import com.shpach.tutor.persistance.entities.TestQuestionsBank;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.question.IQuestionDao;

public class QuestionService {
	public static int getQuestionsCountByUser(User user) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IQuestionDao questionDao = daoFactory.getQuestionDao();
		List<Question> questions=questionDao.findQuestionByUserId(user.getUserId());
		return questions.size();
	}

	public static List<Question> getQuestionsByUserWithTestsList(User user) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IQuestionDao questionDao = daoFactory.getQuestionDao();
		List<Question> questions=questionDao.findQuestionByUserId(user.getUserId());
		insertTestsToquestionsList(questions);
		return questions;
	}

	private static void insertTestsToquestionsList(List<Question> questions) {
		for(Question item:questions){
			List<TestQuestionsBank> testQuestionsBank=TestQuestionsBankService.getTestQuestionsBankByQuestion(item);
			if (testQuestionsBank!=null)
			item.setTestQuestionsBanks(testQuestionsBank);
		}
	}
}
