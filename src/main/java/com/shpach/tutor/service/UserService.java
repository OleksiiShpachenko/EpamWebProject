package com.shpach.tutor.service;

import java.util.List;

import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.user.IUserDao;

/**
 * Collection of services for {@link User} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class UserService {
	/**
	 * Get {@link User} by login (email)
	 * 
	 * @param login
	 *            - users login (email)
	 * @return {@link User}
	 */
	public static User getUserByLogin(String login) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserDao userDao = daoFactory.getUserDao();
		User userEntitie = userDao.findUserByEmail(login);
		return userEntitie;
	}

	/**
	 * Gets collections of {@link User} by {@link Community}
	 * 
	 * @param item
	 *            - {@link Community}
	 * @return collections of {@link User}
	 */
	public static List<User> getUsersByCommunity(Community item) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserDao userDao = daoFactory.getUserDao();
		return userDao.findUsersByCommunityId(item.getCommunityId());
	}

	/**
	 * add {@link User} to database
	 * 
	 * @param user
	 * @return
	 */
	public static boolean addNewUser(User user) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserDao userDao = daoFactory.getUserDao();
		User userAppdated = userDao.addOrUpdate(user);
		if (userAppdated == null)
			return false;
		return true;
	}

}
