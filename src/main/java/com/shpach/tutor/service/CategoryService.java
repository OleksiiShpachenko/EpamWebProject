package com.shpach.tutor.service;

import java.util.List;

import com.shpach.tutor.persistance.entities.Category;
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
	/**
	 * Gets list of {@link Category} from database by {@link Test}
	 * 
	 * @param test
	 *            - {@link Test} entity class
	 * @return list of {@link Category}
	 */
	public static List<Category> getCategoryByTest(Test test) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		return categoryDao.findCategoryByTestId(test.getTestId());
	}

	/**
	 * Gets count of {@link Category} assigned to {@link User}
	 * 
	 * @param user
	 *            - {@link User} entity class
	 * @return count of assigned {@link Category} to {@link User}
	 */
	public static int getCategoriesCountByUser(User user) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		List<Category> categories = categoryDao.findCategoryByUserId(user.getUserId());
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
	public static List<Category> getCategoriesByUserWithTestsList(User user) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		List<Category> categories = categoryDao.findCategoryByUserId(user.getUserId());
		if (categories != null)
			insertTestsToCategory(categories);
		return categories;

	}

	/**
	 * Insert list of assigned {@link Test} to list of {@link Category}
	 * 
	 * @param categories
	 *            - list of {@link Category}
	 */
	private static void insertTestsToCategory(List<Category> categories) {
		for (Category item : categories) {
			List<Test> tests = TestService.getTestsByCategory(item);
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
	public static boolean addNewCategory(Category category) {
		boolean res = false;
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		Category cat = categoryDao.addOrUpdate(category);
		if (cat != null)
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
	public static boolean assignTestToCategory(String testId, String categoryId) {
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
	private static boolean assignTestToCategory(int testId, int categoryId) {
		if (testId < 1 && categoryId < 1)
			return false;
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		return categoryDao.assignTestToCategory(testId, categoryId);
	}
}
