package com.shpach.tutor.persistance.jdbc.dao.community;

import java.util.List;

import com.shpach.tutor.persistance.entities.Community;

public interface ICommunityDao {
	public List<Community> findAll();
	public Community addOrUpdate(Community community);
	public Community findCommunityById(int id);
	public List<Community> findCommunityByUserId(int id);
	public List<Community> findCommunityByCategoryId(int id);
	public boolean assignUserToCommunity(int userId, int communityId);
	public boolean assignCategoryToCommunity(int testId, int communityId);
	

}
