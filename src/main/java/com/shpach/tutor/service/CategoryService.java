package com.shpach.tutor.service;

import java.util.ArrayList;
import java.util.List;

import com.shpach.tutor.persistance.entities.Category;
import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.category.ICategoryDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

/**
 * Collection of services for {@link Category} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class CategoryService {
	private static CategoryService instance;
	private ICategoryDao categoryDao;
	private TestService testService;

	private CategoryService() {

	}

	public static synchronized CategoryService getInstance() {
		if (instance == null) {
			instance = new CategoryService();
		}
		return instance;
	}

	private ICategoryDao getCategoryDao() {
		if (categoryDao == null) {
			IDaoFactory daoFactory = new MySqlDaoFactory();
			categoryDao = daoFactory.getCategoryDao();
		}
		return categoryDao;
	}

	private TestService getTestService() {
		if (testService == null)
			testService = TestService.getInstance();
		return testService;
	}

	/**
	 * Gets list of {@link Category} from database by {@link Test}
	 * 
	 * @param test
	 *            - {@link Test} entity class
	 * @return list of {@link Category}
	 */
	public List<Category> getCategoryByTest(Test test) {
		if (test == null)
			return new ArrayList<Category>();
		List<Category> res = getCategoryDao().findCategoryByTestId(test.getTestId());
		if (res == null)
			res = new ArrayList<Category>();
		return res;
	}

	/**
	 * Gets count of {@link Category} assigned to {@link User}
	 * 
	 * @param user
	 *            - {@link User} entity class
	 * @return count of assigned {@link Category} to {@link User}
	 */
	public int getCategoriesCountByUser(User user) {
		if (user == null)
			return 0;
		List<Category> categories = getCategoryDao().findCategoryByUserId(user.getUserId());
		if (categories == null)
			return 0;
		return categories.size();
	}

	/**
	 * Gets list of {@link Category} from database by {@link User} with inserted
	 * assigned list of {@link Test} to {@link Category}
	 * 
	 * @param user
	 *            {@link User} entity class
	 * @return list of {@link Category}
	 */
	public List<Category> getCategoriesByUserWithTestsList(User user) {
		if (user == null)
			return new ArrayList<Category>();
		List<Category> categories = getCategoryDao().findCategoryByUserId(user.getUserId());
		if (categories == null)
			categories = new ArrayList<Category>();
		insertTestsToCategory(categories);
		return categories;

	}

	/**
	 * Insert list of assigned {@link Test} to list of {@link Category}
	 * 
	 * @param categories
	 *            - list of {@link Category}
	 */
	private void insertTestsToCategory(List<Category> categories) {
		for (Category item : categories) {
			List<Test> tests = getTestService().getTestsByCategory(item);
			item.setTests(tests);
		}
	}

	/**
	 * add new {@link Category} to database
	 * 
	 * @param category
	 *            {@link Category} entity class
	 * @return boolean values is operation success
	 */
	public boolean addNewCategory(Category category) {
		boolean res = false;
		if (category == null)
			return res;
		Category insertedCategory = getCategoryDao().addOrUpdate(category);
		if (insertedCategory != null)
			res = true;
		return res;
	}

	/**
	 * Assign {@link Category} to {@link Test}
	 * 
	 * @param testId
	 *            - id of {@link Test} in {@link String} format
	 * @param categoryId
	 *            -id of {@link Category} in {@link String} format
	 * @return boolean values is operation success
	 */
	public boolean assignTestToCategory(String testId, String categoryId) {
		boolean res = false;

		try {
			int testIdInt = Integer.parseInt(testId);
			int categoryIdInt = Integer.parseInt(categoryId);
			res = assignTestToCategory(testIdInt, categoryIdInt);
		} catch (NumberFormatException e) {
			return res;
		}
		return res;
	}

	/**
	 * Assign {@link Category} to {@link Test}
	 * 
	 * @param testId
	 *            - id of {@link Test} in int format
	 * @param categoryId
	 *            -id of {@link Category} in int format
	 * @return boolean values is operation success
	 */
	private boolean assignTestToCategory(int testId, int categoryId) {
		if (testId < 1 || categoryId < 1)
			return false;
		return getCategoryDao().assignTestToCategory(testId, categoryId);
	}

	public List<Category> getCategoriesByCommunity(Community item) {
		// TODO Auto-generated method stub
		return null;
	}

}
