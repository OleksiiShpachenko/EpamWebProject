package Service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.shpach.tutor.persistance.entities.Category;
import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.test.ITestDao;
import com.shpach.tutor.service.CategoryService;
import com.shpach.tutor.service.CommunityService;
import com.shpach.tutor.service.TestQuestionsBankService;
import com.shpach.tutor.service.TestService;

import TestUtils.TestUtils;

public class TestServiceTest {
	private TestService testService;
	private ITestDao mockTestDao;
	private CommunityService mockCommunityService;
	private CategoryService mockCategoryService;
	private TestQuestionsBankService mockTestQuestionsBankService;

	@Before
	public void init() {
		testService = TestService.getInstance();
		mockTestDao = Mockito.mock(ITestDao.class);
		mockCommunityService = Mockito.mock(CommunityService.class);
		mockCategoryService = Mockito.mock(CategoryService.class);
		mockTestQuestionsBankService = Mockito.mock(TestQuestionsBankService.class);
		TestUtils.getInstance().mockPrivateField(testService, "testDao", mockTestDao);
		TestUtils.getInstance().mockPrivateField(testService, "communityService", mockCommunityService);
		TestUtils.getInstance().mockPrivateField(testService, "categoryService", mockCategoryService);
		TestUtils.getInstance().mockPrivateField(testService, "testQuestionsBankService", mockTestQuestionsBankService);
	}

	@Test
	public void getTestsCountByUserTestTestsExist() {
		final int expectedCount = 3;

		when(mockTestDao.findTestByUserId(anyInt()))
				.thenReturn(new ArrayList<com.shpach.tutor.persistance.entities.Test>(
						Arrays.asList(new com.shpach.tutor.persistance.entities.Test(),
								new com.shpach.tutor.persistance.entities.Test(),
								new com.shpach.tutor.persistance.entities.Test())));

		int count = testService.getTestsCountByUser(new User());

		verify(mockTestDao, times(1)).findTestByUserId(anyInt());

		assertEquals(expectedCount, count);
	}

	@Test
	public void getTestsCountByUserTestNullTests() {
		final int expectedCount = 0;

		when(mockTestDao.findTestByUserId(anyInt())).thenReturn(null);

		int count = testService.getTestsCountByUser(new User());

		verify(mockTestDao, times(1)).findTestByUserId(anyInt());

		assertEquals(expectedCount, count);
	}

	@Test
	public void getTestsCountByUserTestNullUser() {
		final int expectedCount = 0;

		int count = testService.getTestsCountByUser(null);

		verify(mockTestDao, times(0)).findTestByUserId(anyInt());

		assertEquals(expectedCount, count);
	}

	@Test
	public void getTestsByUsersTestExistUserExistsTests() {
		List<com.shpach.tutor.persistance.entities.Test> expectedTests = TestUtils.getInstance().initTestsList();

		when(mockTestDao.findTestByUserId(anyInt())).thenReturn(expectedTests);

		List<com.shpach.tutor.persistance.entities.Test> tests = testService.getTestsByUsers(new User());

		verify(mockTestDao, times(1)).findTestByUserId(anyInt());

		assertArrayEquals(expectedTests.toArray(), tests.toArray());
	}

	

	@Test
	public void getTestsByUsersTestExistUserNullTests() {

		when(mockTestDao.findTestByUserId(anyInt())).thenReturn(null);

		List<com.shpach.tutor.persistance.entities.Test> tests = testService.getTestsByUsers(new User());

		verify(mockTestDao, times(1)).findTestByUserId(anyInt());

		assertNull(tests);
	}

	@Test
	public void getTestsByUsersTestNullUser() {

		List<com.shpach.tutor.persistance.entities.Test> tests = testService.getTestsByUsers(null);

		verify(mockTestDao, times(0)).findTestByUserId(anyInt());

		assertNull(tests);
	}

//	@Test
//	public void insertCommunitiesToTests() {
//		List<com.shpach.tutor.persistance.entities.Test> expectedTests = initTestsList();
//		List<Community> expectedCommunitues = new ArrayList<>();
//		Community comm_1 = new Community();
//		comm_1.setCommunityId(1);
//		Community comm_2 = new Community();
//		comm_2.setCommunityId(2);
//		expectedCommunitues.add(comm_1);
//		expectedCommunitues.add(comm_2);
//
//		when(mockCommunityService.getCommunityByTest(expectedTests.get(0))).thenReturn(expectedCommunitues);
//		when(mockCommunityService.getCommunityByTest(expectedTests.get(1))).thenReturn(expectedCommunitues);
//		testService.insertCommunitiesToTests(expectedTests);
//
//		verify(mockCommunityService, times(2)).getCommunityByTest(anyObject());
//
//		for (com.shpach.tutor.persistance.entities.Test test : expectedTests) {
//			assertArrayEquals(expectedCommunitues.toArray(), test.getCommunities().toArray());
//		}
//
//	}
//
//	@Test
//	public void insertCommunitiesToTestsNullTests() {
//		testService.insertCommunitiesToTests(null);
//
//		verify(mockCommunityService, times(0)).getCommunityByTest(anyObject());
//	}

	@Test
	public void insertCategoriesToTests() {
		List<com.shpach.tutor.persistance.entities.Test> expectedTests = TestUtils.getInstance().initTestsList();
		List<Category> expectedCategories = new ArrayList<>();
		Category cat_1 = new Category();
		cat_1.setCategoryId(1);
		Category cat_2 = new Category();
		cat_2.setCategoryId(2);
		expectedCategories.add(cat_1);
		expectedCategories.add(cat_2);

		when(mockCategoryService.getCategoryByTest(expectedTests.get(0))).thenReturn(expectedCategories);
		when(mockCategoryService.getCategoryByTest(expectedTests.get(1))).thenReturn(expectedCategories);
		testService.insertCategoriesToTests(expectedTests);

		verify(mockCategoryService, times(2)).getCategoryByTest(anyObject());

		for (com.shpach.tutor.persistance.entities.Test test : expectedTests) {
			assertArrayEquals(expectedCategories.toArray(), test.getCategories().toArray());
		}

	}

	@Test
	public void insertCategoriesToTestsNullTests() {
		testService.insertCategoriesToTests(null);

		verify(mockCategoryService, times(0)).getCategoryByTest(anyObject());
	}

	@Test
	public void getTestsByCategoryTest() {
		List<com.shpach.tutor.persistance.entities.Test> expectedTests = TestUtils.getInstance().initTestsList();
		when(mockTestDao.findTestByCategoryId(anyInt())).thenReturn(expectedTests);

		List<com.shpach.tutor.persistance.entities.Test> tests = testService.getTestsByCategory(new Category());

		verify(mockTestDao, times(1)).findTestByCategoryId(anyInt());

		assertArrayEquals(expectedTests.toArray(), tests.toArray());
	}

	@Test
	public void getTestsByCategoryTestNullTests() {
		when(mockTestDao.findTestByCategoryId(anyInt())).thenReturn(null);

		List<com.shpach.tutor.persistance.entities.Test> tests = testService.getTestsByCategory(new Category());

		verify(mockTestDao, times(1)).findTestByCategoryId(anyInt());

		assertNull(tests);
	}

	@Test
	public void getTestsByCategoryTestNullCategory() {

		List<com.shpach.tutor.persistance.entities.Test> tests = testService.getTestsByCategory(null);

		verify(mockTestDao, times(0)).findTestByCategoryId(anyInt());

		assertNull(tests);
	}

//	@Test
//	public void getTestsByCommunityTest() {
//		List<com.shpach.tutor.persistance.entities.Test> expectedTests = TestUtils.getInstance().initTestsList();
//		when(mockTestDao.findTestByCommunityId(anyInt())).thenReturn(expectedTests);
//
//		List<com.shpach.tutor.persistance.entities.Test> tests = testService.getTestsByCommunity(new Community());
//
//		verify(mockTestDao, times(1)).findTestByCommunityId(anyInt());
//
//		assertArrayEquals(expectedTests.toArray(), tests.toArray());
//	}
//
//	@Test
//	public void getTestsByCommunityTestNullTests() {
//		when(mockTestDao.findTestByCommunityId(anyInt())).thenReturn(null);
//
//		List<com.shpach.tutor.persistance.entities.Test> tests = testService.getTestsByCommunity(new Community());
//
//		verify(mockTestDao, times(1)).findTestByCommunityId(anyInt());
//
//		assertNull(tests);
//	}
//
//	@Test
//	public void getTestsByCommunityTestNullCommunity() {
//
//		List<com.shpach.tutor.persistance.entities.Test> tests = testService.getTestsByCommunity(null);
//
//		verify(mockTestDao, times(0)).findTestByCommunityId(anyInt());
//
//		assertNull(tests);
//	}

	@Test
	public void getTestByIdTest() {
		com.shpach.tutor.persistance.entities.Test expectedTest = new com.shpach.tutor.persistance.entities.Test();
		expectedTest.setTestId(1);

		when(mockTestDao.findTestById(anyInt())).thenReturn(expectedTest);

		com.shpach.tutor.persistance.entities.Test test = testService.getTestById(1);

		verify(mockTestDao, times(1)).findTestById(anyInt());

		assertEquals(expectedTest, test);

	}

	@Test
	public void getTestByIdTestNullResponce() {

		when(mockTestDao.findTestById(anyInt())).thenReturn(null);

		com.shpach.tutor.persistance.entities.Test test = testService.getTestById(1);

		verify(mockTestDao, times(1)).findTestById(anyInt());

		assertNull(test);

	}

	@Test
	public void addNewTestAndAssignQuestionsTest() {
		when(mockTestDao.addOrUpdate(anyObject())).thenReturn(new com.shpach.tutor.persistance.entities.Test());

		boolean res = testService.addNewTestAndAssignQuestions(new com.shpach.tutor.persistance.entities.Test(),
				new String[] { "1", "2", "3" });

		verify(mockTestDao, times(1)).addOrUpdate(anyObject());
		verify(mockTestQuestionsBankService, times(3)).assignTestToQuestion(anyInt(), anyInt());
		assertTrue(res);
	}

	@Test
	public void addNewTestAndAssignQuestionsTestNullTest() {
		boolean res = testService.addNewTestAndAssignQuestions(null, new String[] { "1", "2", "3" });

		verify(mockTestDao, times(0)).addOrUpdate(anyObject());
		verify(mockTestQuestionsBankService, times(0)).assignTestToQuestion(anyInt(), anyInt());
		assertFalse(res);
	}

	@Test
	public void addNewTestAndAssignQuestionsTestNullQuestionsIds() {
		boolean res = testService.addNewTestAndAssignQuestions(new com.shpach.tutor.persistance.entities.Test(), null);

		verify(mockTestDao, times(0)).addOrUpdate(anyObject());
		verify(mockTestQuestionsBankService, times(0)).assignTestToQuestion(anyInt(), anyInt());
		assertFalse(res);
	}

	@Test
	public void addNewTestAndAssignQuestionsTestWrongQuestionsIdsParam() {
		when(mockTestDao.addOrUpdate(anyObject())).thenReturn(new com.shpach.tutor.persistance.entities.Test());

		boolean res = testService.addNewTestAndAssignQuestions(new com.shpach.tutor.persistance.entities.Test(),
				new String[] { "1", "2A", "3" });

		verify(mockTestDao, times(1)).addOrUpdate(anyObject());
		verify(mockTestQuestionsBankService, times(0)).assignTestToQuestion(anyInt(), anyInt());
		assertFalse(res);
	}

	@Test
	public void getTestsCountByStudentUserTest() {
		List<com.shpach.tutor.persistance.entities.Test> expectedTests = TestUtils.getInstance().initTestsList();
		Category category=new Category();
		category.setTests(expectedTests);
		Community community_01 = new Community();
		community_01.setCommunityId(1);
		community_01.setCategories(new ArrayList<Category>(Arrays.asList(category)));
		Community community_02 = new Community();
		community_02.setCommunityId(2);
		community_02.setCategories(new ArrayList<Category>(Arrays.asList(category)));

		when(mockCommunityService.getCommunitiesByUserWithCategoriesAndUsers(new User()))
				.thenReturn(new ArrayList<Community>(Arrays.asList(community_01, community_02)));
		int count = testService.getTestsCountByStudentUser(new User());

		verify(mockCommunityService, times(1)).getCommunitiesByUserWithCategoriesAndUsers(anyObject());
		assertEquals(count, 2);
	}

	@Test
	public void getTestsCountByStudentUserTestNullUser() {
		int count = testService.getTestsCountByStudentUser(null);

		verify(mockCommunityService, times(0)).getCommunitiesByUserWithCategoriesAndUsers(anyObject());
		assertEquals(count, 0);

	}

	@Test
	public void getTestsCountByStudentUserTestNullCommunity() {

		when(mockCommunityService.getCommunitiesByUserWithCategoriesAndUsers(new User())).thenReturn(null);
		int count = testService.getTestsCountByStudentUser(new User());

		verify(mockCommunityService, times(1)).getCommunitiesByUserWithCategoriesAndUsers(anyObject());
		assertEquals(count, 0);

	}

	@Test
	public void getTestsByStudentUser() {
		List<com.shpach.tutor.persistance.entities.Test> expectedTests = TestUtils.getInstance().initTestsList();
		Category category=new Category();
		category.setTests(expectedTests);
		Community community_01 = new Community();
		community_01.setCommunityId(1);
		community_01.setCategories(new ArrayList<Category>(Arrays.asList(category)));
		Community community_02 = new Community();
		community_02.setCommunityId(2);
		community_02.setCategories(new ArrayList<Category>(Arrays.asList(category)));

		when(mockCommunityService.getCommunitiesByUserWithCategoriesAndUsers(anyObject()))
				.thenReturn(Arrays.asList(community_01, community_02));

		List<com.shpach.tutor.persistance.entities.Test> tests = testService.getTestsByStudentUser(new User());
		Collections.sort(tests, new Comparator<com.shpach.tutor.persistance.entities.Test>() {

			@Override
			public int compare(com.shpach.tutor.persistance.entities.Test o1,
					com.shpach.tutor.persistance.entities.Test o2) {
				if (o1.getTestId() == o2.getTestId())
					return 0;
				else if (o1.getTestId() < o2.getTestId())
					return -1;
				return 1;
			}
		});
		verify(mockCommunityService, times(1)).getCommunitiesByUserWithCategoriesAndUsers(anyObject());

		assertArrayEquals(expectedTests.toArray(), tests.toArray());

	}
	@Test
	public void getTestsByStudentUserNullUser() {

		List<com.shpach.tutor.persistance.entities.Test> tests = testService.getTestsByStudentUser(null);
	
		verify(mockCommunityService, times(0)).getCommunitiesByUserWithCategoriesAndUsers(anyObject());

		assertNull(tests);

	}
	@Test
	public void getTestsByStudentUserNullCommunity() {

		when(mockCommunityService.getCommunitiesByUserWithCategoriesAndUsers(anyObject()))
				.thenReturn(null);

		List<com.shpach.tutor.persistance.entities.Test> tests = testService.getTestsByStudentUser(new User());

		verify(mockCommunityService, times(1)).getCommunitiesByUserWithCategoriesAndUsers(anyObject());

		assertNull(tests);

	}
}
