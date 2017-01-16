package com.shpach.tutor.persistance.jdbc.dao.category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.shpach.tutor.persistance.entities.Category;
import com.shpach.tutor.persistance.jdbc.connection.Database;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

public class MySqlCategoryDao extends AbstractDao<Category> implements ICategoryDao {
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
			+ ", " + Columns.category_description.name() + ", " + Columns.category_active.name()+ ", " +Columns.category_user_id.name() + " FROM " + TABLE_NAME
			+ "";
	protected final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + Columns.category_name.name() + ", "
			+ Columns.category_description.name() + ", " + Columns.category_active.name()+ ", " +Columns.category_user_id.name() + ") VALUES (?, ?, ?,?)";// SELECT
																													// last_insert_id();";
	protected final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + Columns.category_name.name() + "=?, "
			+ Columns.category_description.name() + "=?, " + Columns.category_active.name()+ "=?, " +Columns.category_user_id.name() + "=? WHERE "
			+ Columns.category_id.name() + "=?";
	protected final String SQL_SELECT_BY_TEST_ID = "SELECT " + TABLE_NAME + "." + Columns.category_id.name() + ", "
			+ TABLE_NAME + "." + Columns.category_name.name() + ", " + TABLE_NAME + "."
			+ Columns.category_description.name() + ", " + TABLE_NAME + "." + Columns.category_active.name() + ", " + TABLE_NAME + "." + Columns.category_user_id.name() + " FROM "
			+ TABLE_NAME + ", " + TABLE_TEST_TO_CATEGORY_RELATIONSHIP + " WHERE " + TABLE_TEST_TO_CATEGORY_RELATIONSHIP
			+ ".test_id=? AND " + TABLE_TEST_TO_CATEGORY_RELATIONSHIP + "." + Columns.category_id.name() + "="
			+ TABLE_NAME + "." + Columns.category_id.name();
	protected final String SQL_INSERT_TEST_TO_CATEGORY = "INSERT INTO " + TABLE_TEST_TO_CATEGORY_RELATIONSHIP + " (" + Columns.category_id.name() + ", "
			+ "test_id" + ") VALUES (?, ?)";
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	
	public Category addOrUpdate(Category category) {
		boolean res=false;
		if (category.getCategoryId() == 0) {
			res=add(category);
		} else {
			res=update(category);
		}
		if(res==true)
		return category;
		return null;
	}

	private boolean update(Category category) {
		PreparedStatement stmt = null;
		Database conn = null;
		try {

			String queryString = SQL_UPDATE;
			conn = MySqlDaoFactory.createConnection();
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, category.getCategoryName());
			stmt.setString(2, category.getCategoryDescription());
			stmt.setByte(3, category.getCategoryActive());
			stmt.setInt(4, category.getCategory_user_id());
			stmt.setInt(5, category.getCategoryId());
			
			stmt.executeUpdate();
			System.out.println("Data Updated Successfully");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private boolean add(Category category) {
		PreparedStatement stmt = null;
		Database conn = null;
		ResultSet rs = null;
		try {

			String queryString = SQL_INSERT;
			conn = MySqlDaoFactory.createConnection();
			// int i = conn.getConnection().getTransactionIsolation();
			conn.getConnection().setAutoCommit(false);
			// conn.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, category.getCategoryName());
			stmt.setString(2, category.getCategoryDescription());
			stmt.setByte(3, category.getCategoryActive());
			stmt.setInt(4, category.getCategory_user_id());
			stmt.executeUpdate();
			stmt.close();
			stmt = conn.prepareStatement("SELECT last_insert_id()");
			rs = stmt.executeQuery();
			while (rs.next()) {
				category.setCategoryId(rs.getInt(1));
			}
			// conn.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.getConnection().setAutoCommit(true);

			System.out.println("Data Added Successfully");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				rs.close();
				if (stmt != null) {
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	
	public Category findCategoryById(int id) {
		List<Category> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.category_id.name(), Integer.toString(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (res != null && res.size() > 0)
			return res.get(0);
		return null;
	}

	
	public List<Category> findCategoryByTestId(int id) {

		// final boolean isConnSupplied = (userConn != null);
		Database conn = MySqlDaoFactory.createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String SQL = SQL_SELECT_BY_TEST_ID;
			// System.out.println("Executing " + SQL);
			// prepare statement
			stmt = conn.prepareStatement(SQL);
			stmt.setObject(1, id);
			rs = stmt.executeQuery();
			//String executedQuery = rs.getStatement().toString();

			// fetch the results
			return fetchMultiResults(rs);
		} catch (Exception ex) {
			// logger.error(ex, ex);
			// throw new Exception("Exception: " + ex.getMessage(), ex);
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// ! if (!isConnSupplied) {
			// ! connPool.returnConnection(conn);
			// ! }

		}
		return null;
	}


	@Override
	public List<Category> findCategoryByUserId(int id) {
		List<Category> res = null;
		try {
			res = findByDynamicSelect(SQL_SELECT, Columns.category_user_id.name(), Integer.toString(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}


	@Override
	public boolean assignTestToCategory(int testId, int categoryId) {
		PreparedStatement stmt = null;
		Database conn = null;
		
		try {

			String queryString = SQL_INSERT_TEST_TO_CATEGORY;
			conn = MySqlDaoFactory.createConnection();
 		    stmt = conn.prepareStatement(queryString);
			stmt.setInt(1, categoryId);
			stmt.setInt(2, testId);
			
			stmt.executeUpdate();
			stmt.close();
		System.out.println("Data Added Successfully");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				
				if (stmt != null) {
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
