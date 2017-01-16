package com.shpach.tutor.servise;

import java.util.List;

import com.shpach.tutor.persistance.entities.Category;
import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.test.ITestDao;

public class TestService {
	public static int getTestsCountByUser(User user){
		IDaoFactory daoFactory= new MySqlDaoFactory();
		ITestDao testDao=daoFactory.getTestDao();
		List<Test> tests=null;
		if(user!=null)
			tests=testDao.findTestByUserId(user.getUserId());
		return (tests==null)? 0: tests.size();
	}
	public static List<Test> getTestsByUsers(User user){
		IDaoFactory daoFactory= new MySqlDaoFactory();
		ITestDao testDao=daoFactory.getTestDao();
		return testDao.findTestByUserId(user.getUserId());
	}
	public static void insertCommunitiesToTests(List<Test> tests){
		for(Test item:tests){
			item.setCommunities(CommunityService.getCommunityByTest(item));
		}
	}
	public static void insertCategoriesToTests(List<Test> tests){
		for(Test item:tests){
			item.setCategories(CategoryService.getCategoryByTest(item));
		}
	}
	public static List<Test> getTestsByCategory(Category category){
		IDaoFactory daoFactory= new MySqlDaoFactory();
		ITestDao testDao=daoFactory.getTestDao();
		return testDao.findTestByCategoryId(category.getCategoryId());
	}
	public static List<Test> getTestsByCommunity(Community community) {
		IDaoFactory daoFactory= new MySqlDaoFactory();
		ITestDao testDao=daoFactory.getTestDao();
		return testDao.findTestByCommunityId(community.getCommunityId());
	}

}
