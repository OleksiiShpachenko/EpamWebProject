package com.shpach.tutor.persistance.jdbc.dao.test;

import java.util.List;

import com.shpach.tutor.persistance.entities.Test;

public interface ITestDao {
	public List<Test> findAll();

	public Test addOrUpdate(Test test);

	public Test findTestById(int id);

	//public List<Test> findTestByCommunityId(int id);

	public List<Test> findTestByCategoryId(int id);
	
	public List<Test> findTestByUserId(int id);

}
