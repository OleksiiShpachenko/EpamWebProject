package com.shpach.tutor.servise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.shpach.tutor.persistance.entities.Category;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.category.ICategoryDao;
import com.shpach.tutor.persistance.jdbc.dao.community.ICommunityDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

public class CategoryService {
	public static List<Category> getCategoryByTest(Test test) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		return categoryDao.findCategoryByTestId(test.getTestId());
	}

	public static int getCategoriesCountByUser(User user) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		List<Category> categories=categoryDao.findCategoryByUserId(user.getUserId());
		/*
		Set<Category> categoriesSet=new HashSet<>();
		if(user!=null){
			List<Test> tests=TestService.getTestsByUsers(user);
			for(Test item:tests){
				List<Category> categories=categoryDao.findCategoryByTestId(item.getTestId());
				categoriesSet.addAll(categories);
			}
		}
		*/
		return categories.size();//categoriesSet.size();
	}

	public static List<Category> getCategoriesByUserWithTestsList(User user) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		List<Category> categories=categoryDao.findCategoryByUserId(user.getUserId());
		insertTestsToCategory(categories);
		/*
		List<Category> categories=new ArrayList<>();
		if(user!=null){
			List<Test> tests=TestService.getTestsByUsers(user);
			TestService.insertCategoriesToTests(tests);
			categories=getCategoryListFromTestsList(tests);
			
		}
		*/
		return categories;
		
	}
	private static void insertTestsToCategory(List<Category> categories){
		for(Category item:categories){
			List<Test> tests=TestService.getTestsByCategory(item);
			item.setTests(tests);
		}
	}

	private static List<Category> getCategoryListFromTestsList(List<Test> tests) {
		Map<Integer,Category> categories=new HashMap<>(); 
		for(Test test:tests){
			List<Category> categoriesList=test.getCategories();
			for(Category category:categoriesList){
				if(categories.containsKey(category.getCategoryId())){
					category=categories.get(category.getCategoryId());
				}
				List<Test> categTests=category.getTests();
				if (categTests==null)
					categTests=new ArrayList<>();
				if(!categTests.contains(test)){
					categTests.add(test);
				}
				category.setTests(categTests);
				categories.put(category.getCategoryId(), category);
			}
		}
		return new ArrayList<Category>(categories.values());
	}
	public static boolean addNewCategory(Category category){
		boolean res=false;
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		Category cat=categoryDao.addOrUpdate(category);
		if(cat!=null)
		res=true;
		return res;
	}

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

	private static boolean assignTestToCategory(int testId, int categoryId) {
		if(testId<1 && categoryId<1)
				return false;
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		return categoryDao.assignTestToCategory(testId, categoryId);
	}
}
