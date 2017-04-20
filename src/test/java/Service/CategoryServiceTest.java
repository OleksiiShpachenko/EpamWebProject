package Service;

import org.junit.*;
import org.mockito.Mockito;

import com.shpach.tutor.persistance.entities.Category;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.category.ICategoryDao;
import com.shpach.tutor.service.CategoryService;
import com.shpach.tutor.service.TestService;

import TestUtils.TestUtils;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryServiceTest {
	private CategoryService categoryService;

	private ICategoryDao mockCategoryDao;
	private TestService mockTestService;

	@Before
	public void init() {
		categoryService = CategoryService.getInstance();
		mockCategoryDao = Mockito.mock(ICategoryDao.class);
		mockTestService = Mockito.mock(TestService.class);

		TestUtils.getInstance().mockPrivateField(categoryService, "categoryDao", mockCategoryDao);
		TestUtils.getInstance().mockPrivateField(categoryService, "testService", mockTestService);
	}

	@Test
	public void getCategoryByTest() {
		com.shpach.tutor.persistance.entities.Test test = new com.shpach.tutor.persistance.entities.Test();
		test.setTestId(1);

		List<Category> expexteds = getExpectedCategoryList();

		when(mockCategoryDao.findCategoryByTestId(test.getTestId())).thenReturn(expexteds);

		List<Category> actuals = categoryService.getCategoryByTest(test);

		verify(mockCategoryDao, times(1)).findCategoryByTestId(test.getTestId());

		assertArrayEquals(expexteds.toArray(), actuals.toArray());
	}

	@Test
	public void getCategoryByTestNullCategoryList() {
		com.shpach.tutor.persistance.entities.Test test = new com.shpach.tutor.persistance.entities.Test();
		test.setTestId(1);

		when(mockCategoryDao.findCategoryByTestId(test.getTestId())).thenReturn(null);

		List<Category> actuals = categoryService.getCategoryByTest(test);

		verify(mockCategoryDao, times(1)).findCategoryByTestId(test.getTestId());

		assertNotNull(actuals);
		assertEquals(0, actuals.size());
	}

	@Test
	public void getCategoryByTestNullTest() {
		com.shpach.tutor.persistance.entities.Test test = new com.shpach.tutor.persistance.entities.Test();
		test.setTestId(1);

		List<Category> actuals = categoryService.getCategoryByTest(null);

		verify(mockCategoryDao, times(0)).findCategoryByTestId(anyInt());

		assertNotNull(actuals);
		assertEquals(0, actuals.size());
	}

	@Test
	public void getCategoriesCountByUser() {
		User user = new User();
		user.setUserId(11);
		when(mockCategoryDao.findCategoryByUserId(user.getUserId()))
				.thenReturn(new ArrayList<Category>(Arrays.asList(new Category(), new Category(), new Category())));

		int actual = categoryService.getCategoriesCountByUser(user);

		verify(mockCategoryDao, times(1)).findCategoryByUserId(user.getUserId());

		assertEquals(3, actual);
	}

	@Test
	public void getCategoriesCountByUserNullCategories() {
		User user = new User();
		user.setUserId(11);
		when(mockCategoryDao.findCategoryByUserId(user.getUserId())).thenReturn(null);

		int actual = categoryService.getCategoriesCountByUser(user);

		verify(mockCategoryDao, times(1)).findCategoryByUserId(user.getUserId());

		assertEquals(0, actual);
	}

	@Test
	public void getCategoriesCountByUserNullUser() {
		int actual = categoryService.getCategoriesCountByUser(null);

		verify(mockCategoryDao, times(0)).findCategoryByUserId(anyInt());

		assertEquals(0, actual);
	}

	@Test
	public void getCategoriesByUserWithTestsList() {
		User user = new User();
		user.setUserId(11);

		List<Category> expexteds = getExpectedCategoryList();

		List<com.shpach.tutor.persistance.entities.Test> tests = initTestList();

		when(mockCategoryDao.findCategoryByUserId(user.getUserId())).thenReturn(expexteds);
		when(mockTestService.getTestsByCategory(anyObject())).thenReturn(tests);

		List<Category> actuals = categoryService.getCategoriesByUserWithTestsList(user);

		verify(mockCategoryDao, times(1)).findCategoryByUserId(user.getUserId());
		verify(mockTestService, times(2)).getTestsByCategory(anyObject());

		assertArrayEquals(tests.toArray(), actuals.get(0).getTests().toArray());
		assertArrayEquals(tests.toArray(), actuals.get(1).getTests().toArray());
		assertArrayEquals(expexteds.toArray(), actuals.toArray());
	}

	@Test
	public void getCategoriesByUserWithTestsListNullCategories() {
		User user = new User();
		user.setUserId(11);

		when(mockCategoryDao.findCategoryByUserId(user.getUserId())).thenReturn(null);

		List<Category> actuals = categoryService.getCategoriesByUserWithTestsList(user);

		verify(mockCategoryDao, times(1)).findCategoryByUserId(user.getUserId());
		verify(mockTestService, times(0)).getTestsByCategory(anyObject());

		assertNotNull(actuals);
		assertEquals(0, actuals.size());
	}

	@Test
	public void getCategoriesByUserWithTestsListNullUser() {

		List<Category> actuals = categoryService.getCategoriesByUserWithTestsList(null);

		verify(mockCategoryDao, times(0)).findCategoryByUserId(anyInt());
		verify(mockTestService, times(0)).getTestsByCategory(anyObject());

		assertNotNull(actuals);
		assertEquals(0, actuals.size());
	}
	
	@Test
	public void addNewCategory(){
		List<Category> expexteds = getExpectedCategoryList();
		
		when(mockCategoryDao.addOrUpdate(expexteds.get(0))).thenReturn(expexteds.get(0));
		
		 boolean actual=categoryService.addNewCategory(expexteds.get(0));
		 
		 verify(mockCategoryDao,times(1)).addOrUpdate(expexteds.get(0));
		 
		 assertTrue(actual);
	}
	@Test
	public void addNewCategoryNullReturn(){
		List<Category> expexteds = getExpectedCategoryList();
		
		when(mockCategoryDao.addOrUpdate(expexteds.get(0))).thenReturn(null);
		
		 boolean actual=categoryService.addNewCategory(expexteds.get(0));
		 
		 verify(mockCategoryDao,times(1)).addOrUpdate(expexteds.get(0));
		 
		 assertFalse(actual);
	}
	@Test
	public void addNewCategoryNullCategory(){
		 boolean actual=categoryService.addNewCategory(null);
		 		 
		 verify(mockCategoryDao,times(0)).addOrUpdate(anyObject());
		 
		 assertFalse(actual);
	}
	
	@Test
	public void  assignTestToCategory(){
		
		when(mockCategoryDao.assignTestToCategory(1, 2)).thenReturn(true);
		
		boolean actual=categoryService.assignTestToCategory("1","2");
		
		verify(mockCategoryDao,times(1)).assignTestToCategory(1, 2);
		
		assertTrue(actual);
	}
	@Test
	public void  assignTestToCategoryFalseAssign(){
		
		when(mockCategoryDao.assignTestToCategory(1, 2)).thenReturn(false);
		
		boolean actual=categoryService.assignTestToCategory("1","2");
		
		verify(mockCategoryDao,times(1)).assignTestToCategory(1, 2);
		
		assertFalse(actual);
	}
	@Test
	public void  assignTestToCategoryExeptionParseTestId(){
		
		boolean actual=categoryService.assignTestToCategory("1a","2");
		
		verify(mockCategoryDao,times(0)).assignTestToCategory(anyInt(), anyInt());
		
		assertFalse(actual);
	}
	@Test
	public void  assignTestToCategoryExeptionParseCategoryId(){
		
		boolean actual=categoryService.assignTestToCategory("1","2a");
		
		verify(mockCategoryDao,times(0)).assignTestToCategory(anyInt(), anyInt());
		
		assertFalse(actual);
	}
	@Test
	public void  assignTestToCategoryExeptionWrongTestIdIndex(){
		
		boolean actual=categoryService.assignTestToCategory("0","2");
		
		verify(mockCategoryDao,times(0)).assignTestToCategory(anyInt(), anyInt());
		
		assertFalse(actual);
	}
	@Test
	public void  assignTestToCategoryExeptionWrongCategoryIdIndex(){
		
		boolean actual=categoryService.assignTestToCategory("10","0");
		
		verify(mockCategoryDao,times(0)).assignTestToCategory(anyInt(), anyInt());
		
		assertFalse(actual);
	}
	private List<com.shpach.tutor.persistance.entities.Test> initTestList() {
		List<com.shpach.tutor.persistance.entities.Test> tests = new ArrayList<>();
		com.shpach.tutor.persistance.entities.Test test1 = new com.shpach.tutor.persistance.entities.Test();
		test1.setTestId(10);
		com.shpach.tutor.persistance.entities.Test test2 = new com.shpach.tutor.persistance.entities.Test();
		test2.setTestId(11);
		com.shpach.tutor.persistance.entities.Test test3 = new com.shpach.tutor.persistance.entities.Test();
		test3.setTestId(12);
		tests.add(test1);
		tests.add(test2);
		tests.add(test3);
		return tests;
	}

	private List<Category> getExpectedCategoryList() {
		List<Category> expexteds = new ArrayList<>();
		Category category1 = new Category();
		category1.setCategoryId(1);
		Category category2 = new Category();
		category2.setCategoryId(2);
		expexteds.add(category1);
		expexteds.add(category2);
		return expexteds;
	}
}
