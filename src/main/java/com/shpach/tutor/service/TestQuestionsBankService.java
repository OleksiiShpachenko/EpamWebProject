package com.shpach.tutor.service;

import java.util.List;

import com.shpach.tutor.persistance.entities.Question;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.TestQuestionsBank;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.testquestionbank.ITestQuestionsBankDao;

/**
 * Collection of services for {@link TestQuestionsBank} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class TestQuestionsBankService {

	/**
	 * Gets collection of {@link TestQuestionsBank} with {@link Test} from
	 * database by {@link Question}
	 * 
	 * @param question
	 *            - {@link Question}
	 * @return collection of {@link TestQuestionsBank}
	 */
	public static List<TestQuestionsBank> getTestQuestionsBankWithTestInfoByQuestion(Question question) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ITestQuestionsBankDao testQuestionsBankDao = daoFactory.getTestQuestionsBankDao();
		List<TestQuestionsBank> testQuestionsBanks = testQuestionsBankDao
				.findTestQuestionsBankByQuestionId(question.getQuestionId());
		if (testQuestionsBanks != null)
			insertTestInfoToTestQuestionsBanks(testQuestionsBanks);
		return testQuestionsBanks;
	}

	/**
	 * Insert to collection of {@link TestQuestionsBank} the {@link Test}
	 * 
	 * @param testQuestionsBanks
	 *            - collection of {@link TestQuestionsBank}
	 */
	private static void insertTestInfoToTestQuestionsBanks(List<TestQuestionsBank> testQuestionsBanks) {
		if (testQuestionsBanks == null)
			return;
		for (TestQuestionsBank testQuestionsBank : testQuestionsBanks) {
			Test test = TestService.getTestById(testQuestionsBank.getTestId());
			testQuestionsBank.setTest(test);
		}

	}

	/**
	 * Assign {@link Question} to {@link Test}
	 * 
	 * @param testId
	 *            - id of {@link Test} in {@link String} format
	 * @param questionId
	 *            - id of {@link Question} in {@link String} format
	 * @return
	 */
	public static boolean assignTestToQuestion(String testId, String questionId) {
		boolean res = false;

		try {
			int testIdInt = Integer.parseInt(testId);
			int questionIdInt = Integer.parseInt(questionId);
			res = assignTestToQuestion(testIdInt, questionIdInt);
		} catch (NumberFormatException e) {
			return res;
		}
		return res;
	}

	/**
	 * Assign {@link Question} to {@link Test}
	 * 
	 * @param testId
	 *            - id of {@link Test} in int format
	 * @param questionId
	 *            - id of {@link Question} in int format
	 * @return
	 */
	public static boolean assignTestToQuestion(int testId, int questionId) {
		if (testId < 1 && questionId < 1)
			return false;
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ITestQuestionsBankDao testQuestionsBankDao = daoFactory.getTestQuestionsBankDao();
		TestQuestionsBank testQuestionsBank = new TestQuestionsBank();
		testQuestionsBank.setTestId(testId);
		testQuestionsBank.setQuestionId(questionId);
		int questionDefaultSortingOrder = testQuestionsBankDao.findMaxquestionDefaultSortingOrderByTestId(testId) + 100;
		testQuestionsBank.setQuestionDefaultSortingOrder(questionDefaultSortingOrder);
		TestQuestionsBank testQuestionsBankReturned = testQuestionsBankDao.addOrUpdate(testQuestionsBank);
		if (testQuestionsBankReturned == null)
			return false;
		return true;
	}

	/**
	 * Gets collection of {@link TestQuestionsBank} with collection
	 * {@link Question} from database by {@link Test} id
	 * 
	 * @param testId
	 *            id of {@link Test}
	 * @return
	 */
	public static List<TestQuestionsBank> getTestQuestionsBankWithQuestionsByTestId(int testId) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ITestQuestionsBankDao testQuestionsBankDao = daoFactory.getTestQuestionsBankDao();
		List<TestQuestionsBank> testQuestionsBanks = testQuestionsBankDao.findTestQuestionsBankByTestId(testId);
		insertQuestionsWithAnswerToTestQuestionsBankList(testQuestionsBanks);
		return testQuestionsBanks;
	}

	/**
	 * Insert to collection of {@link TestQuestionsBank} collection of
	 * {@link Question} with collection of {@link Answer}
	 * 
	 * @param testQuestionsBanks
	 *            - collection of {@link TestQuestionsBank}
	 */
	private static void insertQuestionsWithAnswerToTestQuestionsBankList(List<TestQuestionsBank> testQuestionsBanks) {
		for (TestQuestionsBank testQuestionsBank : testQuestionsBanks) {
			Question question = QuestionService.getQuestionByIdWithAnswers(testQuestionsBank.getQuestionId());
			testQuestionsBank.setQuestion(question);
		}

	}
}
