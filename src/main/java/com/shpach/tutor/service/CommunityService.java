package com.shpach.tutor.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.shpach.tutor.persistance.entities.Category;
import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.community.ICommunityDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

/**
 * Service layer for {@link Community} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommunityService {
	private static CommunityService instance;
	private ICommunityDao communityDao;
	private CategoryService categoryService;
	private UserService userService;

	private CommunityService() {

	}

	public static synchronized CommunityService getInstance() {
		if (instance == null) {
			instance = new CommunityService();
		}
		return instance;
	}

	private ICommunityDao getCommunityDao() {
		if (communityDao == null) {
			IDaoFactory daoFactory = new MySqlDaoFactory();
			communityDao = daoFactory.getCommunityDao();
		}
		return communityDao;
	}

	private CategoryService getCategoryService() {
		if (categoryService == null)
			categoryService = CategoryService.getInstance();
		return categoryService;
	}

	private UserService getUserService() {
		if (userService == null)
			userService = UserService.getInstance();
		return userService;
	}

	/**
	 * Gets list of {@link Community} from database by {@link User}
	 * 
	 * @param user
	 *            - {@link User} entity class
	 * @return list of {@link Community}
	 */
	public int getCommunityCountByUser(User user) {
		if (user == null)
			return 0;
		List<Community> communities = getCommunityDao().findCommunityByUserId(user.getUserId());
		return communities != null ? communities.size() : 0;
	}

	/**
	 * Gets list of {@link Community} from database by {@link Test}
	 * 
	 * @param test
	 *            - {@link Test} entity class
	 * @return list of {@link Community}
	 */
	public List<Community> getCommunityByCategory(Category category) {
		if (category == null)
			return new ArrayList<Community>();
		List<Community> res = getCommunityDao().findCommunityByCategoryId(category.getCategoryId());
		if (res == null)
			res = new ArrayList<Community>();
		return res;
	}

	/**
	 * Gets list of {@link Community} from database by {@link User} with
	 * assigned list of {@link Category} and {@link User}
	 * 
	 * @param user
	 *            - {@link User} entity class
	 * @return list of {@link Community}
	 */
	public List<Community> getCommunitiesByUserWithCategoriesAndUsers(User user) {
		if (user == null)
			return new ArrayList<Community>();
		List<Community> communities = getCommunityDao().findCommunityByUserId(user.getUserId());
		if (communities != null) {
			insertCategoriesToCommunities(communities);
			insertUsersToCommunities(communities);
		} else {
			return new ArrayList<Community>();
		}
		return communities;
	}

	/**
	 * Insert list of assigned {@link User} to list of {@link Community}
	 * 
	 * @param communities
	 *            - list of {@link Community}
	 */
	private void insertUsersToCommunities(List<Community> communities) {
		for (Community item : communities) {
			item.setUsers(getUserService().getUsersByCommunity(item));
		}

	}

	/**
	 * Insert list of assigned {@link Test} to list of {@link Community}
	 * 
	 * @param communities
	 *            - list of {@link Community}
	 */
	private void insertCategoriesToCommunities(List<Community> communities) {
		for (Community item : communities) {
			item.setCategories(getCategoryService().getCategoriesByCommunity(item));
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
	public  boolean addNewCommunity(Community community, User userAdmin) {
		boolean res = false;
		if (community==null || userAdmin==null)
			return res;
		Community comm = getCommunityDao().addOrUpdate(community);
		if (comm != null) {
			res = assignUserToCommunity(userAdmin.getUserId(), community.getCommunityId());
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
	public  boolean assignUserToCommunity(int userId, int communityId) {
			return getCommunityDao().assignUserToCommunity(userId, communityId);
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
	public boolean assignUserToCommunity(String userName, String communityId) {
		boolean res = false;
		if(userName==null)
			return res;
		User user = getUserService().getUserByLogin(userName);
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
	public  boolean assignCategoryToCommunity(String testId, String communityId) {
		boolean res = false;

		try {
			int testIdInt = Integer.parseInt(testId);
			int communityIdInt = Integer.parseInt(communityId);
			res = assignCategoryToCommunity(testIdInt, communityIdInt);
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
	private  boolean assignCategoryToCommunity(int testId, int communityId) {
		if (testId < 1 || communityId < 1)
			return false;
		return getCommunityDao().assignCategoryToCommunity(testId, communityId);
	}

	/**
	 * Gets collection of unique {@link Test} from collection of
	 * {@link Community}
	 * 
	 * @param communities
	 *            - collection of {@link Community}
	 * @return collection of unique {@link Test}
	 */
	public List<Test> getUniqueTestsFromCommunityList(List<Community> communities) {
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

}
