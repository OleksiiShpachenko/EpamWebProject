package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;

import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import com.shpach.tutor.persistance.entities.Category;
import com.shpach.tutor.persistance.jdbc.connection.ConnectionPool;
import com.shpach.tutor.persistance.jdbc.dao.category.ICategoryDao;
import com.shpach.tutor.persistance.jdbc.dao.category.MySqlCategoryDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(ConnectionPool.class)
public class CategoryDaoTest extends DaoTest {
	private ICategoryDao categoryDao;
	private MySqlCategoryDao spyCategoryDao;

	@Before
	public void init() throws SQLException {
		super.init();
		IDaoFactory daoFactory = new MySqlDaoFactory();
		categoryDao = daoFactory.getCategoryDao();
		spyCategoryDao = (MySqlCategoryDao) spy(categoryDao);
	}

	@Test
	public void populateDtoTest() throws SQLException {
		int categoryId = 1;
		int categoryUserId = 2;
		String categoryDescription = "Category descr";
		String categoryName = "Category name";
		byte categoryActive = 1;

		Category expected = new Category();
		expected.setCategoryId(categoryId);
		expected.setCategory_user_id(categoryUserId);
		expected.setCategoryName(categoryName);
		expected.setCategoryDescription(categoryDescription);
		expected.setCategoryActive(categoryActive);

		when(mockResultSet.getInt(anyInt())).thenReturn(categoryId, categoryUserId);
		when(mockResultSet.getString(anyInt())).thenReturn(categoryName, categoryDescription);
		when(mockResultSet.getByte(anyInt())).thenReturn(categoryActive);

		Category actual = spyCategoryDao.populateDto(mockResultSet);

		verify(mockResultSet, times(2)).getInt(anyInt());
		verify(mockResultSet, times(2)).getString(anyInt());
		verify(mockResultSet, times(1)).getByte(anyInt());

		assertEquals(expected, actual);
	}

	@SuppressWarnings("unused")
	@Test(expected = SQLException.class)
	public void populateDtoTestException() throws SQLException {

		when(mockResultSet.getInt(anyInt())).thenThrow(new SQLException());

		Category actual = spyCategoryDao.populateDto(mockResultSet);

		verify(mockResultSet, times(1)).getInt(anyInt());

	}

	@Test
	public void findAllTest() throws Exception {
		List<Category> expecteds = new ArrayList<>(Arrays.asList(new Category(), new Category()));

		doReturn(expecteds).when(spyCategoryDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Category> actuals = spyCategoryDao.findAll();

		verify(spyCategoryDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void addOrApdateTestUpdateCategory() throws SQLException, NamingException {
		int categoryId = 1;
		int categoryUserId = 2;
		String categoryDescription = "Category descr";
		String categoryName = "Category name";
		byte categoryActive = 1;

		Category expected = new Category();
		expected.setCategoryId(categoryId);
		expected.setCategory_user_id(categoryUserId);
		expected.setCategoryName(categoryName);
		expected.setCategoryDescription(categoryDescription);
		expected.setCategoryActive(categoryActive);

		doReturn(true).when(spyCategoryDao).dynamicUpdate(anyString(), anyObject());

		Category actual = spyCategoryDao.addOrUpdate(expected);

		verify(spyCategoryDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateTestUpdateCategoryFail() throws SQLException, NamingException {
		int categoryId = 1;
		int categoryUserId = 2;
		String categoryDescription = "Category descr";
		String categoryName = "Category name";
		byte categoryActive = 1;

		Category expected = new Category();
		expected.setCategoryId(categoryId);
		expected.setCategory_user_id(categoryUserId);
		expected.setCategoryName(categoryName);
		expected.setCategoryDescription(categoryDescription);
		expected.setCategoryActive(categoryActive);

		doReturn(false).when(spyCategoryDao).dynamicUpdate(anyString(), anyObject());

		Category actual = spyCategoryDao.addOrUpdate(expected);

		verify(spyCategoryDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateTestNewCategory() throws SQLException, NamingException {
		int categoryId = 1;
		int categoryUserId = 2;
		String categoryDescription = "Category descr";
		String categoryName = "Category name";
		byte categoryActive = 1;

		Category inserted = new Category();
		inserted.setCategory_user_id(categoryUserId);
		inserted.setCategoryName(categoryName);
		inserted.setCategoryDescription(categoryDescription);
		inserted.setCategoryActive(categoryActive);

		Category expected = new Category();
		expected.setCategoryId(categoryId);
		expected.setCategory_user_id(categoryUserId);
		expected.setCategoryName(categoryName);
		expected.setCategoryDescription(categoryDescription);
		expected.setCategoryActive(categoryActive);

		doReturn(categoryId).when(spyCategoryDao).dynamicAdd(anyString(), anyObject());

		Category actual = spyCategoryDao.addOrUpdate(inserted);

		verify(spyCategoryDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateTestNewCategoryFail() throws SQLException, NamingException {
		int categoryUserId = 2;
		String categoryDescription = "Category descr";
		String categoryName = "Category name";
		byte categoryActive = 1;

		Category inserted = new Category();
		inserted.setCategory_user_id(categoryUserId);
		inserted.setCategoryName(categoryName);
		inserted.setCategoryDescription(categoryDescription);
		inserted.setCategoryActive(categoryActive);

		doReturn(0).when(spyCategoryDao).dynamicAdd(anyString(), anyObject());

		Category actual = spyCategoryDao.addOrUpdate(inserted);

		verify(spyCategoryDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void findCategoryByIdTestExistId() throws SQLException, NamingException {
		int categoryId = 1;
		int categoryUserId = 2;
		String categoryDescription = "Category descr";
		String categoryName = "Category name";
		byte categoryActive = 1;

		Category expected = new Category();
		expected.setCategoryId(categoryId);
		expected.setCategory_user_id(categoryUserId);
		expected.setCategoryName(categoryName);
		expected.setCategoryDescription(categoryDescription);
		expected.setCategoryActive(categoryActive);

		doReturn(new ArrayList<Category>(Arrays.asList(expected))).when(spyCategoryDao).findByDynamicSelect(anyString(),
				anyString(), anyObject());

		Category actuals = spyCategoryDao.findCategoryById(categoryId);

		verify(spyCategoryDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(expected, actuals);
	}

	@Test
	public void findCategoryByIdTestNotExistId() throws SQLException, NamingException {

		doReturn(new ArrayList<Category>()).when(spyCategoryDao).findByDynamicSelect(anyString(), anyString(),
				anyObject());

		Category actuals = spyCategoryDao.findCategoryById(2);

		verify(spyCategoryDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actuals);
	}

	@Test
	public void findCategoryByTestIdTestExistId() throws SQLException, NamingException {
		int categoryId = 1;
		int categoryUserId = 2;
		String categoryDescription = "Category descr";
		String categoryName = "Category name";
		byte categoryActive = 1;

		Category category = new Category();
		category.setCategoryId(categoryId);
		category.setCategory_user_id(categoryUserId);
		category.setCategoryName(categoryName);
		category.setCategoryDescription(categoryDescription);
		category.setCategoryActive(categoryActive);
		List<Category> expecteds = new ArrayList<>();
		expecteds.add(category);

		doReturn(expecteds).when(spyCategoryDao).findByDynamicSelect(anyString(), anyObject());

		List<Category> actuals = spyCategoryDao.findCategoryByTestId(2);

		verify(spyCategoryDao, times(1)).findByDynamicSelect(anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findCategoryByTestIdTestNotExistId() throws SQLException, NamingException {

		doReturn(new ArrayList<Category>()).when(spyCategoryDao).findByDynamicSelect(anyString(), anyObject());

		List<Category> actuals = spyCategoryDao.findCategoryByTestId(2);

		verify(spyCategoryDao, times(1)).findByDynamicSelect(anyString(), anyObject());

		assertEquals(0, actuals.size());
	}

	@Test
	public void findCategoryByUserIdTestExistId() throws SQLException, NamingException {
		int categoryId = 1;
		int categoryUserId = 2;
		String categoryDescription = "Category descr";
		String categoryName = "Category name";
		byte categoryActive = 1;

		Category category = new Category();
		category.setCategoryId(categoryId);
		category.setCategory_user_id(categoryUserId);
		category.setCategoryName(categoryName);
		category.setCategoryDescription(categoryDescription);
		category.setCategoryActive(categoryActive);
		List<Category> expecteds = new ArrayList<>();
		expecteds.add(category);

		doReturn(expecteds).when(spyCategoryDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Category> actuals = spyCategoryDao.findCategoryByUserId(2);

		verify(spyCategoryDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findCategoryByUserIdTestNotExistId() throws SQLException, NamingException {

		doReturn(new ArrayList<Category>()).when(spyCategoryDao).findByDynamicSelect(anyString(), anyString(),
				anyObject());

		List<Category> actuals = spyCategoryDao.findCategoryByUserId(2);

		verify(spyCategoryDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(0, actuals.size());
	}

	@Test
	public void assignTestToCategoryTestGoodParam() throws SQLException, NamingException {
		int categoryId = 1;
		int testId = 2;

		doReturn(true).when(spyCategoryDao).dynamicUpdate(anyString(), anyObject());

		boolean res = spyCategoryDao.assignTestToCategory(testId, categoryId);

		verify(spyCategoryDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertTrue(res);
	}

	@Test
	public void assignTestToCategoryTestWrongParam() throws SQLException, NamingException {
		int categoryId = 1;
		int testId = 2;

		doReturn(false).when(spyCategoryDao).dynamicUpdate(anyString(), anyObject());

		boolean res = spyCategoryDao.assignTestToCategory(testId, categoryId);

		verify(spyCategoryDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertFalse(res);
	}

	@Test
	public void findCategoryByCommunityIdTestExistId() throws SQLException, NamingException {
		int categoryId = 1;
		int categoryUserId = 2;
		String categoryDescription = "Category descr";
		String categoryName = "Category name";
		byte categoryActive = 1;

		Category category = new Category();
		category.setCategoryId(categoryId);
		category.setCategory_user_id(categoryUserId);
		category.setCategoryName(categoryName);
		category.setCategoryDescription(categoryDescription);
		category.setCategoryActive(categoryActive);
		List<Category> expecteds = new ArrayList<>();
		expecteds.add(category);

		doReturn(expecteds).when(spyCategoryDao).findByDynamicSelect(anyString(), anyObject());

		List<Category> actuals = spyCategoryDao.findCategoryByCommunityId(2);

		verify(spyCategoryDao, times(1)).findByDynamicSelect(anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findCategoryByCommunityIdTestNotExistId() throws SQLException, NamingException {

		doReturn(new ArrayList<Category>()).when(spyCategoryDao).findByDynamicSelect(anyString(), anyObject());

		List<Category> actuals = spyCategoryDao.findCategoryByCommunityId(2);

		verify(spyCategoryDao, times(1)).findByDynamicSelect(anyString(), anyObject());

		assertEquals(0, actuals.size());
	}

}
