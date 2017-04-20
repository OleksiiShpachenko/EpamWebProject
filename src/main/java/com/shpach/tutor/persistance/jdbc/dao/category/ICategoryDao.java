package com.shpach.tutor.persistance.jdbc.dao.category;

import java.util.List;

import com.shpach.tutor.persistance.entities.Category;

public interface ICategoryDao {
	public List<Category> findAll();
	public Category addOrUpdate(Category category);
	public Category findCategoryById(int id);
	public List<Category> findCategoryByTestId(int id);
	public List<Category> findCategoryByUserId(int id);
	public List<Category> findCategoryByCommunityId(int id);
	public boolean assignTestToCategory(int testId, int categoryId);
	

}
