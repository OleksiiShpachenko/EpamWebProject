package com.shpach.tutor.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.Task;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.user.IUserDao;

/**
 * Service layer for {@link User} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class UserService {
	private static UserService instance = null;
	private IUserDao userDao;
	private TaskService taskService;

	private UserService() {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		userDao = daoFactory.getUserDao();
		taskService = TaskService.getInstance();
	}

	public static synchronized UserService getInstance() {
		if (instance == null) {
			instance = new UserService();
		}
		return instance;
	}

	/**
	 * Get {@link User} by login (email)
	 * 
	 * @param login
	 *            - users login (email)
	 * @return {@link User}
	 */
	public User getUserByLogin(String login) {
		if (login==null)
			return null;
		User userEntitie = userDao.findUserByEmail(login);
		return userEntitie;
	}

	/**
	 * Gets collections of {@link User} by {@link Community}
	 * 
	 * @param community
	 *            - {@link Community}
	 * @return collections of {@link User}
	 */
	public List<User> getUsersByCommunity(Community community) {
		if (community == null)
			return null;
		return userDao.findUsersByCommunityId(community.getCommunityId());
	}

	/**
	 * add {@link User} to database
	 * 
	 * @param user
	 * @return
	 */
	public boolean addNewUser(User user) {
		if (user==null)
			return false;
		User userAppdated = userDao.addOrUpdate(user);
		if (userAppdated == null)
			return false;
		return true;
	}

	public  User findUserWithGreatWorstStatistic() {
		List<User> users = userDao.findAll();
		if (users==null)
			return null;
		User res = null;
		int max = -1;
		for (User user : users) {
			List<Task> tasks =taskService.getTasksByUser(user);
			int min = taskService.getMinScore(tasks);
			if (max < min && min!=Integer.MAX_VALUE) {
				res = user;
				max = min;
			}
		}
		return res;
	}

	public static boolean test(String testString) {
		Pattern p = Pattern.compile("[A-z,À-ÿ]{1,25}");
		Matcher m = p.matcher(testString);
		return m.matches();
	}

}
