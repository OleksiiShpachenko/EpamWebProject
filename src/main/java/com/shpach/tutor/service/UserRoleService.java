package com.shpach.tutor.service;

import com.shpach.tutor.persistance.entities.UsersRole;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.userrole.IUserRoleDao;

/**
 * Service layer for {@link UserRole} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class UserRoleService {

	private static UserRoleService instance = null;
	private IUserRoleDao userRoleDao;

	private UserRoleService() {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		userRoleDao = daoFactory.getUserRoleDao();
	}

	public static synchronized UserRoleService getInstance() {
		if (instance == null) {
			instance = new UserRoleService();
		}
		return instance;
	}

	/**
	 * Gets {@link UserRole} by id
	 * 
	 * @param roleId
	 *            - id of {@link UserRole}
	 * @return {@link UserRole}
	 */
	public UsersRole getUserRoleById(int roleId) {
		return userRoleDao.findUsersRoleById(roleId);
	}

}
