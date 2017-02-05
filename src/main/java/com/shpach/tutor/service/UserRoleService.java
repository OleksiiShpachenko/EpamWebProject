package com.shpach.tutor.service;

import com.shpach.tutor.persistance.entities.UsersRole;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.userrole.IUserRoleDao;

/**
 * Collection of services for {@link UserRole} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class UserRoleService {

	/**
	 * Gets {@link UserRole} by id
	 * 
	 * @param roleId
	 *            - id of {@link UserRole}
	 * @return {@link UserRole}
	 */
	public static UsersRole getUserRoleById(int roleId) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserRoleDao userRoleDao = daoFactory.getUserRoleDao();
		return userRoleDao.findUsersRoleById(roleId);
	}

}
