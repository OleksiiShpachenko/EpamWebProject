package com.shpach.tutor.servise;

import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.user.IUserDao;
import com.shpach.tutor.persistance.jdbc.dao.user.MySqlUserDao;

public class LoginService {
	private final static String ADMIN_LOGIN = "admin";
	private final static String ADMIN_PASS = "qwer";

	public static boolean checkLogin(String enterLogin, String enterPass) {
		IDaoFactory daoFactory= new MySqlDaoFactory();
		IUserDao userDao=daoFactory.getUserDao();
		User userEntitie=userDao.findUserByEmail(enterLogin);
		if (userEntitie==null)
			return false;
		return userEntitie.getUserPassword().equals(enterPass);
	}

}
