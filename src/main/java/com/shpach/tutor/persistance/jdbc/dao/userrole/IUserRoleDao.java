package com.shpach.tutor.persistance.jdbc.dao.userrole;

import java.util.List;

import com.shpach.tutor.persistance.entities.UsersRole;

public interface IUserRoleDao {
	public List<UsersRole> findAll();

	public UsersRole addOrUpdate(UsersRole usersRole);

	public UsersRole findUsersRoleById(int id);
}
