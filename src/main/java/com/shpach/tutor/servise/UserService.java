package com.shpach.tutor.servise;

import java.util.List;

import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.user.IUserDao;

public class UserService {
	public static User getUserByLogin(String login){
		IDaoFactory daoFactory= new MySqlDaoFactory();
		IUserDao userDao=daoFactory.getUserDao();
		User userEntitie=userDao.findUserByEmail(login);
		return userEntitie;
	}

	public static List<User> getUsersByCommunity(Community item) {
		IDaoFactory daoFactory= new MySqlDaoFactory();
		IUserDao userDao=daoFactory.getUserDao();
		return userDao.findUsersByCommunityId(item.getCommunityId());
	}

}
