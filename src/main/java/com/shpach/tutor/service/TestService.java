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
 * Service layer for {@link Test} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class TestService {

	private static TestService instance;
	private CategoryService categoryService;
	private TestQuestionsBankService testQuestionsBankService;
	private ITestDao testDao;
	private CommunityService communityService;

	private TestService() {
		//IDaoFactory daoFactory = new MySqlDaoFactory();
		//testDao = daoFactory.getTestDao();
		//communityService = CommunityService.getInstance();
		//categoryService = CategoryService.getInstance();
		// testQuestionsBankService = TestQuestionsBankService.getInstance();
	}

	public static synchronized TestService getInstance() {
		if (instance == null) {
			instance = new TestService();
		}
		return instance;
	}

	/**
	 * Gets count of assigned {@link Test} to {@link User} (creator)
	 * 
	 * @param user
	 *            - {@link User}
	 * @return count of of assigned {@link Test} to {@link User} (creator)
	 */
	public int getTestsCountByUser(User user) {
		List<Test> tests = null;
		if (user != null)
			tests = getTestDao().findTestByUserId(user.getUserId());
		return (tests == null) ? 0 : tests.size();
	}

	private ITestDao getTestDao() {
		if (testDao == null) {
			IDaoFactory daoFactory = new MySqlDaoFactory();
			testDao = daoFactory.getTestDao();
		}
		return testDao;
	}

	/**
	 * Get collection of {@link Test} by {@link User}
	 * 
	 * @param user
	 *            - {@link User}
	 * @return collection of {@link Test}
	 */
	public List<Test> getTestsByUsers(User user) {
		if (user == null)
			return null;
		return getTestDao().findTestByUserId(user.getUserId());
	}

	// /**
	// * Insert to collection of {@link Test} assigned {@link Community}
	// *
	// * @param tests
	// * - collection of {@link Test}
	// */
	// public void insertCommunitiesToTests(List<Test> tests) {
	// if (tests == null)
	// return;
	// for (Test item : tests) {
	// item.setCommunities(communityService.getCommunityByTest(item));
	// }
	// }

	/**
	 * Insert to collection of {@link Test} assigned {@link Category}
	 * 
	 * @param tests
	 *            - collection of {@link Test}
	 */
	public void insertCategoriesToTests(List<Test> tests) {
		if (tests == null)
			return;
		for (Test item : tests) {
			item.setCategories(getCategoryService().getCategoryByTest(item));
		}
	}

	private CategoryService getCategoryService() {
		if(categoryService==null)
			categoryService = CategoryService.getInstance();
		return categoryService;
	}

	/**
	 * Get collection of {@link Test} assigned to {@link Category}
	 * 
	 * @param category
	 *            - {@link Category}
	 * @return collection of {@link Test}
	 */
	public List<Test> getTestsByCategory(Category category) {
		if (category == null)
			return null;
		return getTestDao().findTestByCategoryId(category.getCategoryId());
	}

//	/**
//	 * Get collection of {@link Test} assigned to {@link Community}
//	 * 
//	 * @param community
//	 *            - {@link Community}
//	 * @return collection of {@link Test}
//	 */
//	public List<Test> getTestsByCommunity(Community community) {
//		if (community == null)
//			return null;
//		return getTestDao().findTestByCommunityId(community.getCommunityId());
//	}

	/**
	 * Get {@link Test} by id
	 * 
	 * @param testId
	 *            - id of {@link Test}
	 * @return {@link Test}
	 */
	public Test getTestById(int testId) {
		return getTestDao().findTestById(testId);
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
	public boolean addNewTestAndAssignQuestions(Test test, String[] questionsId) {
		boolean res = false;
		if (test == null || questionsId == null)
			return res;
		Test testApdated = getTestDao().addOrUpdate(test);
		if (testApdated != null) {
			res = assignQuestionsIdToTest(testApdated.getTestId(), questionsId);
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
	private boolean assignQuestionsIdToTest(int testId, String[] questionsIdStr) {
		List<Integer> questionsId = parceStringArrayToInt(questionsIdStr);
		if (questionsId == null)
			return false;
		for (Integer qId : questionsId) {
			if (getTestQuestionsBankService().assignTestToQuestion(testId, qId))
				return false;
		}
		return true;
	}

	private TestQuestionsBankService getTestQuestionsBankService() {
		if (testQuestionsBankService == null)
			testQuestionsBankService = TestQuestionsBankService.getInstance();
		return testQuestionsBankService;
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
				return null;
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
	public int getTestsCountByStudentUser(User user) {
		if (user == null)
			return 0;
		List<Community> communities = getCommunityService().getCommunitiesByUserWithCategoriesAndUsers(user);
		return getUniqueTestCountInCommunitiesList(communities);
	}

	private CommunityService getCommunityService() {
		if(communityService ==null)
			communityService = CommunityService.getInstance();
		return communityService;
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
		if (communities == null)
			return 0;
		Set<Test> tests = new HashSet<>();
		for (Community community : communities) {
			if (community.getCategories() != null) {
				for (Category category : community.getCategories()) {
					if (category.getTests() != null)
						tests.addAll(category.getTests());
				}

			}
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
	public List<Test> getTestsByStudentUser(User user) {
		if (user == null)
			return null;
		List<Community> communities = getCommunityService().getCommunitiesByUserWithCategoriesAndUsers(user);
		if (communities == null)
			return null;
		Set<Test> tests = new HashSet<>();
		for (Community community : communities) {
			if (community.getCategories() != null) {
				for (Category category : community.getCategories()) {
					if (category.getTests() != null)
						tests.addAll(category.getTests());
				}
			}
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
	public Test getTestByIdWhithQuestionsAndAnswers(int testId) {
		List<TestQuestionsBank> questions = getTestQuestionsBankService().getTestQuestionsBankWithQuestionsByTestId(testId);
		return new TestBuilder().setTestFromTemplate(getTestById(testId)).setQuestions(questions).buildForTestTake();
	}

	public static void insertStudentsCommunitiesToTests(List<Test> tests) {

	}

}
