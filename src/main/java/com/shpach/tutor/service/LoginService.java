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
	public static boolean checkLogin(String enterLogin, String enterPass) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserDao userDao = daoFactory.getUserDao();
		User userEntitie = userDao.findUserByEmail(enterLogin);
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
	public static String getStartCommandAccordingToUserRole(User user) {
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
