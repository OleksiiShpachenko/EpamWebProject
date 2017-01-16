package com.shpach.tutor.persistance.jdbc.dao.user;

import java.util.List;

import com.shpach.tutor.persistance.entities.User;


public interface IUserDao {
	public List<User> findAll();
	public User addOrUpdate(User user);
	public User findUserById(int id);
	public User findUserByEmail(String login);
	public List<User> findUsersByCommunityId(int id);
	
}
