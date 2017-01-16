package com.shpach.tutor.servise;

import java.util.List;

import com.shpach.tutor.persistance.entities.Category;
import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.category.ICategoryDao;
import com.shpach.tutor.persistance.jdbc.dao.community.ICommunityDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.test.ITestDao;

public class CommunityService {
	public static int getCommunityCountByUser(User user) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		List<Community> communities = communityDao.findCommunityByUserId(user.getUserId());
		return communities != null ? communities.size() : 0;
	}

	public static List<Community> getCommunityByTest(Test test) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		return communityDao.findCommunityByTestId(test.getTestId());
	}

	public static List<Community> getCommunitiesByUserWithTestsAndUsers(User user) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		List<Community> communities = communityDao.findCommunityByUserId(user.getUserId());
		insertTestsToCommunities(communities);
		insertUsersToCommunities(communities);
		return communities;
	}

	private static void insertUsersToCommunities(List<Community> communities) {
		for (Community item : communities) {
			item.setUsers(UserService.getUsersByCommunity(item));
		}

	}

	private static void insertTestsToCommunities(List<Community> communities) {
		for (Community item : communities) {
			item.setTests(TestService.getTestsByCommunity(item));
		}
	}

	public static boolean addNewCommunity(Community community, User userAdmin) {
		boolean res = false;
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		Community comm = communityDao.addOrUpdate(community);
		if (comm != null) {
			res = communityDao.assignUserToCommunity(userAdmin.getUserId(), community.getCommunityId());
		}
		return res;
	}

	public static boolean assignUserToCommunity(int userId, int communityId) {
		boolean res = false;
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		res = communityDao.assignUserToCommunity(userId, communityId);
		return res;
	}

	public static boolean assignUserToCommunity(String userName, String communityId) {
		boolean res = false;
		User user = UserService.getUserByLogin(userName);
		if (user == null)
			return res;
		try {
			int commId = Integer.parseInt(communityId);
			res = assignUserToCommunity(user.getUserId(), commId);
		} catch (NumberFormatException e) {
			return res;
		}
		return res;
	}

	public static boolean assignTestToCommunity(String testId, String communityId) {
		boolean res = false;
		
		try {
			int testIdInt = Integer.parseInt(testId);
			int communityIdInt = Integer.parseInt(communityId);
			res = assignTestToCommunity(testIdInt, communityIdInt);
		} catch (NumberFormatException e) {
			return res;
		}
		return res;
	}

	private static boolean assignTestToCommunity(int testId, int communityId) {
		if(testId<1 && communityId<1)
				return false;
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		return communityDao.assignTestToCommunity(testId, communityId);
	}
}
