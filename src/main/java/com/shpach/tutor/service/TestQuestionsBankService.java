package com.shpach.tutor.service;

import java.util.ArrayList;
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

	private static TestQuestionsBankService instance;
	private ITestQuestionsBankDao testQuestionsBankDao;
	private TestService testService;
	private QuestionService questionService;

	private TestQuestionsBankService() {
	}

	public static TestQuestionsBankService getInstance() {
		if (instance == null) {
			instance = new TestQuestionsBankService();
		}
		return instance;
	}

	/**
	 * Gets collection of {@link TestQuestionsBank} with {@link Test} from
	 * database by {@link Question}
	 * 
	 * @param question
	 *            - {@link Question}
	 * @return collection of {@link TestQuestionsBank}
	 */
	public List<TestQuestionsBank> getTestQuestionsBankWithTestInfoByQuestion(Question question) {
		List<TestQuestionsBank> testQuestionsBanks = new ArrayList<>();
		if (question == null)
			return testQuestionsBanks;
		testQuestionsBanks = getTestQuestionsBankDao().findTestQuestionsBankByQuestionId(question.getQuestionId());
		if (testQuestionsBanks == null)
			return new ArrayList<TestQuestionsBank>();
		insertTestInfoToTestQuestionsBanks(testQuestionsBanks);
		return testQuestionsBanks;
	}

	private ITestQuestionsBankDao getTestQuestionsBankDao() {
		if (testQuestionsBankDao == null) {
			IDaoFactory daoFactory = new MySqlDaoFactory();
			testQuestionsBankDao = daoFactory.getTestQuestionsBankDao();
		}
		return testQuestionsBankDao;
	}

	/**
	 * Insert to collection of {@link TestQuestionsBank} the {@link Test}
	 * 
	 * @param testQuestionsBanks
	 *            - collection of {@link TestQuestionsBank}
	 */
	private void insertTestInfoToTestQuestionsBanks(List<TestQuestionsBank> testQuestionsBanks) {
		for (TestQuestionsBank testQuestionsBank : testQuestionsBanks) {
			Test test = getTestService().getTestById(testQuestionsBank.getTestId());
			testQuestionsBank.setTest(test);
		}

	}

	private TestService getTestService() {
		if (testService == null)
			testService = TestService.getInstance();
		return testService;
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
	public boolean assignTestToQuestion(String testId, String questionId) {
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
	public boolean assignTestToQuestion(int testId, int questionId) {
		if (testId < 1 || questionId < 1)
			return false;
		TestQuestionsBank testQuestionsBank = new TestQuestionsBank();
		testQuestionsBank.setTestId(testId);
		testQuestionsBank.setQuestionId(questionId);
		int questionDefaultSortingOrder = getTestQuestionsBankDao()
				.findMaxquestionDefaultSortingOrderByTestId(testQuestionsBank.getTestId()) + 100;
		testQuestionsBank.setQuestionDefaultSortingOrder(questionDefaultSortingOrder);
		TestQuestionsBank testQuestionsBankReturned = getTestQuestionsBankDao().addOrUpdate(testQuestionsBank);
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
	public List<TestQuestionsBank> getTestQuestionsBankWithQuestionsByTestId(int testId) {

		List<TestQuestionsBank> testQuestionsBanks = getTestQuestionsBankDao().findTestQuestionsBankByTestId(testId);
		if (testQuestionsBanks == null)
			return new ArrayList<TestQuestionsBank>();
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
	private void insertQuestionsWithAnswerToTestQuestionsBankList(List<TestQuestionsBank> testQuestionsBanks) {
		for (TestQuestionsBank testQuestionsBank : testQuestionsBanks) {
			Question question = getQuestionService().getQuestionByIdWithAnswers(testQuestionsBank.getQuestionId());
			testQuestionsBank.setQuestion(question);
		}

	}

	private QuestionService getQuestionService() {
		if (questionService == null)
			questionService = QuestionService.getInstance();
		return questionService;
	}

}
