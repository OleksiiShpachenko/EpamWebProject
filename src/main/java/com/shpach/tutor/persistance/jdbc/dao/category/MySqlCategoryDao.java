package com.shpach.tutor.persistance.jdbc.dao.category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.shpach.tutor.persistance.entities.Category;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;

public class MySqlCategoryDao extends AbstractDao<Category> implements ICategoryDao {
	private static final Logger logger = Logger.getLogger(MySqlCategoryDao.class);

	protected enum Columns {

		category_id(1), category_name(2), category_description(3), category_active(4), category_user_id(5);
		Columns(int id) {
			this.id = id;
		}

		private int id;

		public int getId() {
			return id;
		}

		public static String getClassName() {
			return Columns.class.getName();
		}

	}

	protected static final String TABLE_NAME = "category";
	protected static final String TABLE_TEST_TO_CATEGORY_RELATIONSHIP = "test_to_category_relationship";
	protected final String SQL_SELECT = "SELECT " + Columns.category_id.name() + ", " + Columns.category_name.name()
			+ ", " + Columns.category_description.name() + ", " + Columns.category_active.name() + ", "
			+ Columns.category_user_id.name() + " FROM " + TABLE_NAME + "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.category_name.name() + ", "
			+ Columns.category_description.name() + ", " + Columns.category_active.name() + ", "
			+ Columns.category_user_id.name() + ") VALUES (?, ?, ?,?)";

	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.category_name.name() + "=?, "
			+ Columns.category_description.name() + "=?, " + Columns.category_active.name() + "=?, "
			+ Columns.category_user_id.name() + "=? WHERE " + Columns.category_id.name() + "=?";
	protected final String SQL_SELECT_BY_TEST_ID = "SELECT " + TABLE_NAME + "." + Columns.category_id.name() + ", "
			+ TABLE_NAME + "." + Columns.category_name.name() + ", " + TABLE_NAME + "."
			+ Columns.category_description.name() + ", " + TABLE_NAME + "." + Columns.category_active.name() + ", "
			+ TABLE_NAME + "." + Columns.category_user_id.name() + " FROM " + TABLE_NAME + ", "
			+ TABLE_TEST_TO_CATEGORY_RELATIONSHIP + " WHERE " + TABLE_TEST_TO_CATEGORY_RELATIONSHIP + ".test_id=? AND "
			+ TABLE_TEST_TO_CATEGORY_RELATIONSHIP + "." + Columns.category_id.name() + "=" + TABLE_NAME + "."
			+ Columns.category_id.name();
	protected final String SQL_INSERT_TEST_TO_CATEGORY = "INSERT INTO " + TABLE_TEST_TO_CATEGORY_RELATIONSHIP + " ("
			+ Columns.category_id.name() + ", " + "test_id" + ") VALUES (?, ?)";

	private static MySqlCategoryDao instance = null;

	private MySqlCategoryDao() {

	}

	public static synchronized MySqlCategoryDao getInstance() {
		if (instance == null)
			return instance = new MySqlCategoryDao();
		else
			return instance;

	}
	
	@Override
	protected Category populateDto(ResultSet rs) throws SQLException {
		Category dto = new Category();
		dto.setCategoryId(rs.getInt(Columns.category_id.getId()));
		dto.setCategoryName(rs.getString(Columns.category_name.getId()));
		dto.setCategoryDescription(rs.getString(Columns.category_description.getId()));
		dto.setCategoryActive(rs.getByte(Columns.category_active.getId()));
		dto.setCategory_user_id(rs.getInt(Columns.category_user_id.getId()));
		return dto;
	}

	public List<Category> findAll() {
		List<Category> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, null, null);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	public Category addOrUpdate(Category category) {
		boolean res = false;
		if (category.getCategoryId() == 0) {
			res = add(category);
		} else {
			res = update(category);
		}
		if (res == true)
			return category;
		return null;
	}

	private boolean update(Category category) {
		Object[] sqlParams = new Object[] { category.getCategoryName(), category.getCategoryDescription(),
				category.getCategoryActive(), category.getCategory_user_id(), category.getCategoryId() };
		return dynamicUpdate(SQL_UPDATE, sqlParams);
	}

	private boolean add(Category category) {
		Object[] sqlParams = new Object[] { category.getCategoryName(), category.getCategoryDescription(),
				category.getCategoryActive(), category.getCategory_user_id() };
		int id = dynamicAdd(SQL_INSERT, sqlParams);
		if (id > 0) {
			category.setCategoryId(id);
			return true;
		}
		return false;
	}

	public Category findCategoryById(int id) {
		List<Category> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.category_id.name(), Integer.toString(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	public List<Category> findCategoryByTestId(int id) {
		List<Category> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT_BY_TEST_ID, new Integer[] { id });
		} catch (Exception e) {
			logger.error(e, e);
		}
		if (res != null && res.size() > 0)
			return res;
		return null;
	}

	@Override
	public List<Category> findCategoryByUserId(int id) {
		List<Category> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.category_user_id.name(), Integer.toString(id));
		} catch (Exception e) {
			logger.error(e, e);
		}

		return res;
	}

	@Override
	public boolean assignTestToCategory(int testId, int categoryId) {
		Object[] sqlParams = new Object[] { categoryId, testId };
		return dynamicUpdate(SQL_INSERT_TEST_TO_CATEGORY, sqlParams);
	}

}
