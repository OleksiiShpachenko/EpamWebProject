package com.shpach.tutor.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.shpach.tutor.persistance.entities.Category;
import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.TestQuestionsBank;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.entities.builders.TestBuilder;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.test.ITestDao;

/**
 * Collection of services for {@link Test} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class TestService {
	/**
	 * Gets count of assigned {@link Test} to {@link User} (creator)
	 * 
	 * @param user
	 *            - {@link User}
	 * @return count of of assigned {@link Test} to {@link User} (creator)
	 */
	public static int getTestsCountByUser(User user) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ITestDao testDao = daoFactory.getTestDao();
		List<Test> tests = null;
		if (user != null)
			tests = testDao.findTestByUserId(user.getUserId());
		return (tests == null) ? 0 : tests.size();
	}

	/**
	 * Get collection of {@link Test} by {@link User}
	 * 
	 * @param user
	 *            - {@link User}
	 * @return collection of {@link Test}
	 */
	public static List<Test> getTestsByUsers(User user) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ITestDao testDao = daoFactory.getTestDao();
		return testDao.findTestByUserId(user.getUserId());
	}

	/**
	 * Insert to collection of {@link Test} assigned {@link Community}
	 * 
	 * @param tests
	 *            - collection of {@link Test}
	 */
	public static void insertCommunitiesToTests(List<Test> tests) {
		if (tests == null)
			return;
		for (Test item : tests) {
			item.setCommunities(CommunityService.getCommunityByTest(item));
		}
	}

	/**
	 * Insert to collection of {@link Test} assigned {@link Category}
	 * 
	 * @param tests
	 *            - collection of {@link Test}
	 */
	public static void insertCategoriesToTests(List<Test> tests) {
		if (tests == null)
			return;
		for (Test item : tests) {
			item.setCategories(CategoryService.getCategoryByTest(item));
		}
	}

	/**
	 * Get collection of {@link Test} assigned to {@link Category}
	 * 
	 * @param category
	 *            - {@link Category}
	 * @return collection of {@link Test}
	 */
	public static List<Test> getTestsByCategory(Category category) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ITestDao testDao = daoFactory.getTestDao();
		return testDao.findTestByCategoryId(category.getCategoryId());
	}

	/**
	 * Get collection of {@link Test} assigned to {@link Community}
	 * 
	 * @param community
	 *            - {@link Community}
	 * @return collection of {@link Test}
	 */
	public static List<Test> getTestsByCommunity(Community community) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ITestDao testDao = daoFactory.getTestDao();
		return testDao.findTestByCommunityId(community.getCommunityId());
	}

	/**
	 * Get {@link Test} by id
	 * 
	 * @param testId
	 *            - id of {@link Test}
	 * @return {@link Test}
	 */
	public static Test getTestById(int testId) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ITestDao testDao = daoFactory.getTestDao();
		return testDao.findTestById(testId);
	}

	/**
	 * Add new {@link Test} to database and assign a list of {@link Question} to
	 * it
	 * 
	 * @param test
	 *            - {@link Test}
	 * @param questionsId
	 *            - list of id of {@link Question}
	 * @return true if operation success
	 */
	public static boolean addNewTestAndAssignQuestions(Test test, String[] questionsId) {
		boolean res = false;
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ITestDao testDao = daoFactory.getTestDao();
		Test testApdated = testDao.addOrUpdate(test);
		if (testApdated != null) {
			res = true;
			assignQuestionsIdToTest(testApdated.getTestId(), questionsId);
		}
		return res;
	}

	/**
	 * Assign a list of {@link Question} to {@link Test}
	 * 
	 * @param testId
	 *            - id of {@link Test}
	 * @param questionsIdStr
	 *            - array of id of {@link Question}
	 */
	private static void assignQuestionsIdToTest(int testId, String[] questionsIdStr) {
		List<Integer> questionsId = parceStringArrayToInt(questionsIdStr);
		for (Integer qId : questionsId) {
			TestQuestionsBankService.assignTestToQuestion(testId, qId);
		}
	}

	/**
	 * Convert String array to int array
	 * 
	 * @param questionsIdStr
	 *            - String array
	 * @return int array
	 */
	private static List<Integer> parceStringArrayToInt(String[] questionsIdStr) {
		List<Integer> res = new ArrayList<>();
		for (String string : questionsIdStr) {
			try {
				int id = Integer.parseInt(string);
				res.add(id);
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * Gets count of {@link Test} assigned to {@link User} via {@link Community}
	 * connection
	 * 
	 * @param user
	 *            {@link User}
	 * @return count of {@link Test}
	 */
	public static int getTestsCountByStudentUser(User user) {
		List<Community> communities = CommunityService.getCommunitiesByUserWithTestsAndUsers(user);
		return getUniqueTestCountInCommunitiesList(communities);
	}

	/**
	 * Gets count of unique {@link Test} assigned to collection of
	 * {@link Community}
	 * 
	 * @param communities
	 *            - collection of {@link Community}
	 * @return count of {@link Test}
	 */
	private static int getUniqueTestCountInCommunitiesList(List<Community> communities) {
		Set<Test> tests = new HashSet<>();
		for (Community community : communities) {
			tests.addAll(community.getTests());
		}
		return tests.size();
	}

	/**
	 * Gets collection of {@link Test} assigned to {@link User} via
	 * {@link Community} connection
	 * 
	 * @param user
	 *            - {@link User}
	 * @return collection of {@link Test}
	 */
	public static List<Test> getTestsByStudentUser(User user) {
		List<Community> communities = CommunityService.getCommunitiesByUserWithTestsAndUsers(user);
		Set<Test> tests = new HashSet<>();
		for (Community community : communities) {
			tests.addAll(community.getTests());
		}
		return new ArrayList<Test>(tests);
	}

	

	/**
	 * Get {@link Test} by id from database with collection of {@link Question}
	 * and {@link Answers}
	 * 
	 * @param testId
	 *            - id of {@link Test}
	 * @return
	 */
	public static Test getTestByIdWhithQuestionsAndAnswers(int testId) {
		List<TestQuestionsBank> questions = TestQuestionsBankService.getTestQuestionsBankWithQuestionsByTestId(testId);
		return new TestBuilder().setTestFromTemplate(getTestById(testId)).setQuestions(questions).buildForTestTake();
	}
	
	public static void insertStudentsCommunitiesToTests(List<Test> tests) {
		

	}

}
