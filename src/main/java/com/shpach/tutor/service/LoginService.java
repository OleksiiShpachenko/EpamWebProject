package com.shpach.tutor.service;

import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.user.IUserDao;
import com.shpach.tutor.view.service.MenuStrategy;

/**
 * Collection of services for users authentication
 * 
 * @author Shpachenko_A_K
 *
 */
public class LoginService {
	private static LoginService instance;
	private IUserDao userDao;

	private LoginService() {
	}

	public static LoginService getInstance() {
		if (instance == null) {
			instance = new LoginService();
		}
		return instance;
	}

	public IUserDao getUserDao() {
		if (userDao == null) {
			IDaoFactory daoFactory = new MySqlDaoFactory();
			userDao = daoFactory.getUserDao();
		}
		return userDao;
	}

	/**
	 * Check if users login (email) and password is correct
	 * 
	 * @param enterLogin
	 *            - users email
	 * @param enterPass
	 *            - users password
	 * @return {@link User} entity class if users login (email) and password is
	 *         correct, else return null
	 */
	public boolean checkLogin(String enterLogin, String enterPass) {

		User userEntitie = getUserDao().findUserByEmail(enterLogin);
		if (userEntitie == null)
			return false;
		return userEntitie.getUserPassword().equals(enterPass);
	}

	/**
	 * Return Command name for redirection after login page according to
	 * {@link User#getRoleId()}
	 * 
	 * @param user
	 *            - {@link User} entity class
	 * @return String of Command name
	 */
	public  String getStartCommandAccordingToUserRole(User user) {
		String res = "";
		switch (user.getRoleId()) {
		case MenuStrategy.USER_ROLE_TUTOR: {
			res = "tutorTests";
			break;
		}
		case MenuStrategy.USER_ROLE_STUDENT: {
			res = "studentTests";
			break;
		}
		default: {
			res = "studentTests";
			break;
		}
		}
		return res;
	}

}
