package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.shpach.tutor.persistance.entities.Category;
import com.shpach.tutor.persistance.jdbc.connection.ConnectionPool;
import com.shpach.tutor.persistance.jdbc.dao.category.ICategoryDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(ConnectionPool.class)
public class CategoryDaoTest {
	private Connection mockConnection;
	private PreparedStatement mockPreparedStmnt;
	private ResultSet mockResultSet;

	@Before
	public void init() {
		mockConnection = Mockito.mock(Connection.class);
		mockPreparedStmnt = Mockito.mock(PreparedStatement.class);
		mockResultSet = Mockito.mock(ResultSet.class);
	}

	@Test
	public void findCategoryByIdTestExistId() throws SQLException, NamingException {
		int categoryId = 1;
		int categoryUserId = 2;
		String categoryDescription = "Category descr";
		String categoryName = "Category name";
		byte categoryActive = 1;
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(categoryId, categoryUserId);
		when(mockResultSet.getByte(anyInt())).thenReturn(categoryActive);
		when(mockResultSet.getString(anyInt())).thenReturn(categoryName, categoryDescription);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		Category category = new Category();
		category.setCategoryId(categoryId);
		category.setCategory_user_id(categoryUserId);
		category.setCategoryName(categoryName);
		category.setCategoryDescription(categoryDescription);
		category.setCategoryActive(categoryActive);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		Category categoryExpected = categoryDao.findCategoryById(categoryId);

		assertEquals(categoryExpected, category);
	}

	@Test
	public void findCategoryByIdTestNotExistId() throws SQLException, NamingException {

		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(Boolean.FALSE);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		Category categoryExpected = categoryDao.findCategoryById(2);

		assertNull(categoryExpected);
	}

	@Test
	public void findCategoryByTestIdTestExistId() throws SQLException, NamingException {
		int categoryId = 1;
		int categoryUserId = 2;
		String categoryDescription = "Category descr";
		String categoryName = "Category name";
		byte categoryActive = 1;
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(categoryId, categoryUserId);
		when(mockResultSet.getByte(anyInt())).thenReturn(categoryActive);
		when(mockResultSet.getString(anyInt())).thenReturn(categoryName, categoryDescription);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		Category category = new Category();
		category.setCategoryId(categoryId);
		category.setCategory_user_id(categoryUserId);
		category.setCategoryName(categoryName);
		category.setCategoryDescription(categoryDescription);
		category.setCategoryActive(categoryActive);
		List<Category> categories = new ArrayList<>();
		categories.add(category);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		List<Category> categoriesExpected = categoryDao.findCategoryByTestId(2);

		assertArrayEquals(categoriesExpected.toArray(), categories.toArray());
	}

	@Test
	public void findCategoryByTestIdTestNotExistId() throws SQLException, NamingException {

		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(Boolean.FALSE);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		List<Category> categoriesExpected = categoryDao.findCategoryByTestId(2);

		assertNull(categoriesExpected);
	}

	@Test
	public void findCategoryByUserIdTestExistId() throws SQLException, NamingException {
		int categoryId = 1;
		int categoryUserId = 2;
		String categoryDescription = "Category descr";
		String categoryName = "Category name";
		byte categoryActive = 1;
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(categoryId, categoryUserId);
		when(mockResultSet.getByte(anyInt())).thenReturn(categoryActive);
		when(mockResultSet.getString(anyInt())).thenReturn(categoryName, categoryDescription);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		Category category = new Category();
		category.setCategoryId(categoryId);
		category.setCategory_user_id(categoryUserId);
		category.setCategoryName(categoryName);
		category.setCategoryDescription(categoryDescription);
		category.setCategoryActive(categoryActive);
		List<Category> categories = new ArrayList<>();
		categories.add(category);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		List<Category> categoriesExpected = categoryDao.findCategoryByUserId(2);

		assertArrayEquals(categoriesExpected.toArray(), categories.toArray());
	}

	@Test
	public void findCategoryByUserIdTestNotExistId() throws SQLException, NamingException {

		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(Boolean.FALSE);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		List<Category> categoriesExpected = categoryDao.findCategoryByUserId(2);

		assertEquals(categoriesExpected.size(), 0);
	}

	@Test
	public void addOrApdateTestUpdateCategory() throws SQLException, NamingException {
		int categoryId = 1;
		int categoryUserId = 2;
		String categoryDescription = "Category descr";
		String categoryName = "Category name";
		byte categoryActive = 1;
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);

		Category category = new Category();
		category.setCategoryId(categoryId);
		category.setCategory_user_id(categoryUserId);
		category.setCategoryName(categoryName);
		category.setCategoryDescription(categoryDescription);
		category.setCategoryActive(categoryActive);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		Category categoryExpected = categoryDao.addOrUpdate(category);

		assertNotNull(categoryExpected);
	}

	@Test
	public void addOrApdateTestNewCategory() throws SQLException, NamingException {
		int categoryId = 1;
		int categoryUserId = 2;
		String categoryDescription = "Category descr";
		String categoryName = "Category name";
		byte categoryActive = 1;

		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(categoryId);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		Category category = new Category();
		category.setCategory_user_id(categoryUserId);
		category.setCategoryName(categoryName);
		category.setCategoryDescription(categoryDescription);
		category.setCategoryActive(categoryActive);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		Category categoryExpected = categoryDao.addOrUpdate(category);

		assertEquals(categoryId, categoryExpected.getCategoryId());
	}

	@Test
	public void assignTestToCategoryTestGoodParam() throws SQLException, NamingException {
		int categoryId = 1;
		int testId = 2;

		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		boolean res = categoryDao.assignTestToCategory(testId, categoryId);

		assertTrue(res);
	}

	@Test
	public void assignTestToCategoryTestWrongParam() throws SQLException, NamingException {
		int categoryId = 1;
		int testId = 2;

		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeUpdate()).thenThrow(new SQLException());

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICategoryDao categoryDao = daoFactory.getCategoryDao();
		boolean res = categoryDao.assignTestToCategory(testId, categoryId);

		assertFalse(res);
	}

}
