package com.shpach.tutor.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.community.ICommunityDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

/**
 * Collection of services for {@link Community} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommunityService {
	/**
	 * Gets list of {@link Community} from database by {@link User}
	 * 
	 * @param user
	 *            - {@link User} entity class
	 * @return list of {@link Community}
	 */
	public static int getCommunityCountByUser(User user) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		List<Community> communities = communityDao.findCommunityByUserId(user.getUserId());
		return communities != null ? communities.size() : 0;
	}

	/**
	 * Gets list of {@link Community} from database by {@link Test}
	 * 
	 * @param test
	 *            - {@link Test} entity class
	 * @return list of {@link Community}
	 */
	public static List<Community> getCommunityByTest(Test test) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		return communityDao.findCommunityByTestId(test.getTestId());
	}

	/**
	 * Gets list of {@link Community} from database by {@link User} with
	 * assigned list of {@link Test} and {@link User}
	 * 
	 * @param user
	 *            - {@link User} entity class
	 * @return list of {@link Community}
	 */
	public static List<Community> getCommunitiesByUserWithTestsAndUsers(User user) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		List<Community> communities = communityDao.findCommunityByUserId(user.getUserId());
		if (communities != null) {
			insertTestsToCommunities(communities);
			insertUsersToCommunities(communities);
		}
		return communities;
	}

	/**
	 * Insert list of assigned {@link User} to list of {@link Community}
	 * 
	 * @param communities
	 *            - list of {@link Community}
	 */
	private static void insertUsersToCommunities(List<Community> communities) {
		for (Community item : communities) {
			item.setUsers(UserService.getUsersByCommunity(item));
		}

	}

	/**
	 * Insert list of assigned {@link Test} to list of {@link Community}
	 * 
	 * @param communities
	 *            - list of {@link Community}
	 */
	private static void insertTestsToCommunities(List<Community> communities) {
		for (Community item : communities) {
			item.setTests(TestService.getTestsByCommunity(item));
		}
	}

	/**
	 * Add new {@link Community} to database
	 * 
	 * @param community
	 *            - {@link Community} entity class
	 * @param userAdmin
	 *            - {@link User} witch create a {@link Community}
	 * @return boolean values is operation success
	 */
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

	/**
	 * Assign {@link User} to {@link Community}
	 * 
	 * @param userId
	 *            - id of {@link User} in int format
	 * @param communityId
	 *            - id of {@link Community} in int format
	 * @return boolean values is operation success
	 */
	public static boolean assignUserToCommunity(int userId, int communityId) {
		boolean res = false;
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		res = communityDao.assignUserToCommunity(userId, communityId);
		return res;
	}

	/**
	 * Assign {@link User} to {@link Community}
	 * 
	 * @param userId
	 *            - id of {@link User} in {@link String} format
	 * @param communityId
	 *            - id of {@link Community} in {@link String} format
	 * @return boolean values is operation success
	 */
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

	/**
	 * Assign {@link Test} to {@link Community}
	 * 
	 * @param testId
	 *            - id of {@link Test} in {@link String} format
	 * @param communityId
	 *            - id of {@link Community} in {@link String} format
	 * @return boolean values is operation success
	 */
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

	/**
	 * Assign {@link Test} to {@link Community}
	 * 
	 * @param testId
	 *            - id of {@link Test} in int format
	 * @param communityId
	 *            - id of {@link Community} in int format
	 * @return boolean values is operation success
	 */
	private static boolean assignTestToCommunity(int testId, int communityId) {
		if (testId < 1 && communityId < 1)
			return false;
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		return communityDao.assignTestToCommunity(testId, communityId);
	}

	/**
	 * Gets collection of unique {@link Test} from collection of
	 * {@link Community}
	 * 
	 * @param communities
	 *            - collection of {@link Community}
	 * @return collection of unique {@link Test}
	 */
	public static List<Test> getUniqueTestsFromCommunityList(List<Community> communities) {
		Set<Test> tests = new HashSet<>();
		for (Community community : communities) {
			if (community.getTests() != null)
				tests.addAll(community.getTests());
		}
		return new ArrayList<Test>(tests);
	}
}
