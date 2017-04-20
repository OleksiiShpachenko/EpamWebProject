package Service;

import org.junit.*;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.shpach.tutor.persistance.entities.Answer;
import com.shpach.tutor.persistance.entities.Category;
import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.community.ICommunityDao;
import com.shpach.tutor.service.CategoryService;
import com.shpach.tutor.service.CommunityService;
import com.shpach.tutor.service.UserService;

import TestUtils.TestUtils;

public class CommunityServiceTest {
	private CommunityService communityService;
	private ICommunityDao mockCommunityDao;
	private CategoryService mockCategoryService;
	private UserService mockUserService;

	@Before
	public void init() {
		communityService = CommunityService.getInstance();

		mockCommunityDao = Mockito.mock(ICommunityDao.class);
		mockCategoryService = Mockito.mock(CategoryService.class);
		mockUserService = Mockito.mock(UserService.class);

		TestUtils.getInstance().mockPrivateField(communityService, "communityDao", mockCommunityDao);
		TestUtils.getInstance().mockPrivateField(communityService, "categoryService", mockCategoryService);
		TestUtils.getInstance().mockPrivateField(communityService, "userService", mockUserService);
	}

	@Test
	public void getCommunityCountByUser() {
		User user = new User();
		user.setUserId(10);

		when(mockCommunityDao.findCommunityByUserId(user.getUserId()))
				.thenReturn(new ArrayList<Community>(Arrays.asList(new Community(), new Community(), new Community())));

		int actual = communityService.getCommunityCountByUser(user);

		verify(mockCommunityDao, times(1)).findCommunityByUserId(user.getUserId());

		assertEquals(3, actual);
	}

	@Test
	public void getCommunityCountByUserNullCommunities() {
		User user = new User();
		user.setUserId(10);

		when(mockCommunityDao.findCommunityByUserId(user.getUserId())).thenReturn(null);

		int actual = communityService.getCommunityCountByUser(user);

		verify(mockCommunityDao, times(1)).findCommunityByUserId(user.getUserId());

		assertEquals(0, actual);
	}

	@Test
	public void getCommunityCountByUserNullUser() {
		int actual = communityService.getCommunityCountByUser(null);

		verify(mockCommunityDao, times(0)).findCommunityByUserId(anyInt());

		assertEquals(0, actual);
	}

	@Test
	public void getCommunityByCategory() {
		Category category1 = new Category();
		category1.setCategoryId(10);

		Community community = new Community();
		community.setCommunityId(11);

		List<Community> communityList = new ArrayList<>(Arrays.asList(community));

		when(mockCommunityDao.findCommunityByCategoryId(category1.getCategoryId())).thenReturn(communityList);

		List<Community> actuals = communityService.getCommunityByCategory(category1);

		verify(mockCommunityDao, times(1)).findCommunityByCategoryId(category1.getCategoryId());

		assertArrayEquals(communityList.toArray(), actuals.toArray());
	}

	@Test
	public void getCommunityByCategoryNullCommList() {
		com.shpach.tutor.persistance.entities.Test test = new com.shpach.tutor.persistance.entities.Test();
		test.setTestId(10);
		
		Category category1 = new Category();
		category1.setCategoryId(10);

		when(mockCommunityDao.findCommunityByCategoryId(category1.getCategoryId())).thenReturn(null);

		List<Community> actuals = communityService.getCommunityByCategory(category1);

		verify(mockCommunityDao, times(1)).findCommunityByCategoryId(category1.getCategoryId());

		assertNotNull(actuals);
		assertEquals(0, actuals.size());
	}

	@Test
	public void getCommunityByCategoryNullCategory() {

		List<Community> actuals = communityService.getCommunityByCategory(null);

		verify(mockCommunityDao, times(0)).findCommunityByCategoryId(anyInt());

		assertNotNull(actuals);
		assertEquals(0, actuals.size());
	}

	@Test
	public void getCommunitiesByUserWithCategoriesAndUsers() {
		User user = new User();
		user.setUserId(11);
		Community community = new Community();
		community.setCommunityId(11);

		Answer answer = new Answer();
		answer.setAnswerId(1);
		List<Community> communityList = new ArrayList<>(Arrays.asList(community));

		Category category = new Category();
		category.setCategoryId(1);
		List<Category> categories = new ArrayList<>(Arrays.asList(category));

		List<User> users = new ArrayList<User>(Arrays.asList(user));

		when(mockCommunityDao.findCommunityByUserId(user.getUserId())).thenReturn(communityList);
		when(mockCategoryService.getCategoriesByCommunity(community)).thenReturn(categories);
		when(mockUserService.getUsersByCommunity(community)).thenReturn(users);

		List<Community> actuals = communityService.getCommunitiesByUserWithCategoriesAndUsers(user);

		verify(mockCommunityDao, times(1)).findCommunityByUserId(user.getUserId());
		verify(mockCategoryService, times(1)).getCategoriesByCommunity(community);
		verify(mockUserService, times(1)).getUsersByCommunity(community);

		assertArrayEquals(communityList.toArray(), actuals.toArray());
		assertArrayEquals(categories.toArray(), actuals.get(0).getCategories().toArray());
		assertArrayEquals(users.toArray(), actuals.get(0).getUsers().toArray());

	}

	@Test
	public void getCommunitiesByUserWithCategoriesAndUsersNullCommunities() {
		User user = new User();
		user.setUserId(11);
		when(mockCommunityDao.findCommunityByUserId(user.getUserId())).thenReturn(null);

		List<Community> actuals = communityService.getCommunitiesByUserWithCategoriesAndUsers(user);

		verify(mockCommunityDao, times(1)).findCommunityByUserId(user.getUserId());
		verify(mockCategoryService, times(0)).getCategoriesByCommunity(anyObject());
		verify(mockUserService, times(0)).getUsersByCommunity(anyObject());

		assertNotNull(actuals);
		assertEquals(0, actuals.size());
	}

	@Test
	public void getCommunitiesByUserWithCategoriesAndUsersNullUser() {
		User user = new User();
		user.setUserId(11);

		List<Community> actuals = communityService.getCommunitiesByUserWithCategoriesAndUsers(null);

		verify(mockCommunityDao, times(0)).findCommunityByUserId(anyInt());
		verify(mockCategoryService, times(0)).getCategoriesByCommunity(anyObject());
		verify(mockUserService, times(0)).getUsersByCommunity(anyObject());

		assertNotNull(actuals);
		assertEquals(0, actuals.size());
	}

	@Test
	public void addNewCommunity() {
		User user = new User();
		user.setUserId(11);
		Community community = new Community();
		community.setCommunityId(11);

		when(mockCommunityDao.addOrUpdate(community)).thenReturn(community);
		when(mockCommunityDao.assignUserToCommunity(user.getUserId(), community.getCommunityId())).thenReturn(true);

		boolean actual = communityService.addNewCommunity(community, user);

		verify(mockCommunityDao, times(1)).addOrUpdate(community);
		verify(mockCommunityDao, times(1)).assignUserToCommunity(user.getUserId(), community.getCommunityId());

		assertTrue(actual);
	}

	@Test
	public void addNewCommunityNullAddOrUpdate() {
		User user = new User();
		user.setUserId(11);
		Community community = new Community();
		community.setCommunityId(11);

		when(mockCommunityDao.addOrUpdate(community)).thenReturn(null);

		boolean actual = communityService.addNewCommunity(community, user);

		verify(mockCommunityDao, times(1)).addOrUpdate(community);
		verify(mockCommunityDao, times(0)).assignUserToCommunity(anyInt(), anyInt());

		assertFalse(actual);
	}

	@Test
	public void addNewCommunityfailAssign() {
		User user = new User();
		user.setUserId(11);
		Community community = new Community();
		community.setCommunityId(11);

		when(mockCommunityDao.addOrUpdate(community)).thenReturn(null);
		when(mockCommunityDao.assignUserToCommunity(user.getUserId(), community.getCommunityId())).thenReturn(false);

		boolean actual = communityService.addNewCommunity(community, user);

		verify(mockCommunityDao, times(1)).addOrUpdate(community);
		verify(mockCommunityDao, times(0)).assignUserToCommunity(user.getUserId(), community.getCommunityId());

		assertFalse(actual);
	}

	@Test
	public void addNewCommunityNullCommunity() {
		User user = new User();
		user.setUserId(11);

		boolean actual = communityService.addNewCommunity(null, user);

		verify(mockCommunityDao, times(0)).addOrUpdate(anyObject());
		verify(mockCommunityDao, times(0)).assignUserToCommunity(anyInt(), anyInt());

		assertFalse(actual);
	}

	@Test
	public void addNewCommunityNullUser() {
		Community community = new Community();
		community.setCommunityId(11);

		boolean actual = communityService.addNewCommunity(community, null);

		verify(mockCommunityDao, times(0)).addOrUpdate(anyObject());
		verify(mockCommunityDao, times(0)).assignUserToCommunity(anyInt(), anyInt());

		assertFalse(actual);
	}

	@Test
	public void assignUserToCommunity() {
		int userId = 1;
		int communityId = 2;

		when(mockCommunityDao.assignUserToCommunity(userId, communityId)).thenReturn(true);

		boolean actual = communityService.assignUserToCommunity(userId, communityId);

		verify(mockCommunityDao, times(1)).assignUserToCommunity(userId, communityId);

		assertTrue(actual);
	}

	@Test
	public void assignUserToCommunityFail() {
		int userId = 1;
		int communityId = 2;

		when(mockCommunityDao.assignUserToCommunity(userId, communityId)).thenReturn(false);

		boolean actual = communityService.assignUserToCommunity(userId, communityId);

		verify(mockCommunityDao, times(1)).assignUserToCommunity(userId, communityId);

		assertFalse(actual);
	}

	@Test
	public void assignUserToCommunityString() {
		User user = new User();
		user.setUserId(1);
		String username = "userName";
		int communityId = 2;

		CommunityService my = spy(communityService);
		when(my.assignUserToCommunity(user.getUserId(), communityId)).thenReturn(true);
		when(mockUserService.getUserByLogin(username)).thenReturn(user);

		boolean actual = communityService.assignUserToCommunity(username, "2");

		verify(mockUserService, times(1)).getUserByLogin("userName");

		assertTrue(actual);
	}

	@Test
	public void assignUserToCommunityStringNullUser() {
		String username = "userName";

		when(mockUserService.getUserByLogin(username)).thenReturn(null);

		boolean actual = communityService.assignUserToCommunity(username, "2");

		verify(mockUserService, times(1)).getUserByLogin("userName");

		assertFalse(actual);
	}

	@Test
	public void assignUserToCommunityStringNullUserName() {

		boolean actual = communityService.assignUserToCommunity(null, "2");

		verify(mockUserService, times(0)).getUserByLogin(anyString());

		assertFalse(actual);
	}

	@Test
	public void assignUserToCommunityStringNullCommunityId() {
		User user = new User();
		user.setUserId(1);
		String username = "userName";

		when(mockUserService.getUserByLogin(username)).thenReturn(user);

		boolean actual = communityService.assignUserToCommunity(username, null);

		verify(mockUserService, times(1)).getUserByLogin(username);

		assertFalse(actual);
	}

	@Test
	public void assignUserToCommunityStringWrongCommunityId() {
		User user = new User();
		user.setUserId(1);
		String username = "userName";

		when(mockUserService.getUserByLogin(username)).thenReturn(user);

		boolean actual = communityService.assignUserToCommunity(username, "2w");

		verify(mockUserService, times(1)).getUserByLogin(username);

		assertFalse(actual);
	}

	@Test
	public void assignCategoryToCommunity() {

		String testId = "1";
		String communityId = "2";

		when(mockCommunityDao.assignCategoryToCommunity(1, 2)).thenReturn(true);

		boolean actual = communityService.assignCategoryToCommunity(testId, communityId);

		verify(mockCommunityDao, times(1)).assignCategoryToCommunity(1, 2);

		assertTrue(actual);
	}

	@Test
	public void assignCategoryToCommunityNullTestId() {

		String communityId = "2";

		when(mockCommunityDao.assignCategoryToCommunity(1, 2)).thenReturn(true);

		boolean actual = communityService.assignCategoryToCommunity(null, communityId);

		verify(mockCommunityDao, times(0)).assignCategoryToCommunity(anyInt(), anyInt());

		assertFalse(actual);
	}

	@Test
	public void assignCategoryToCommunityNullCommunityId() {

		String testId = "1";
	
		when(mockCommunityDao.assignCategoryToCommunity(1, 2)).thenReturn(true);

		boolean actual = communityService.assignCategoryToCommunity(testId, null);

		verify(mockCommunityDao, times(0)).assignCategoryToCommunity(anyInt(), anyInt());

		assertFalse(actual);
	}

	@Test
	public void assignCategoryToCommunityParseError() {

		String testId = "1a";
		String communityId = "2b";

		when(mockCommunityDao.assignCategoryToCommunity(1, 2)).thenReturn(true);

		boolean actual = communityService.assignCategoryToCommunity(testId, communityId);

		verify(mockCommunityDao, times(0)).assignCategoryToCommunity(anyInt(), anyInt());

		assertFalse(actual);
	}

	@Test
	public void assignCategoryToCommunityDaoFalse() {

		String testId = "1";
		String communityId = "2";

		when(mockCommunityDao.assignCategoryToCommunity(1, 2)).thenReturn(false);

		boolean actual = communityService.assignCategoryToCommunity(testId, communityId);

		verify(mockCommunityDao, times(1)).assignCategoryToCommunity(1, 2);

		assertFalse(actual);
	}

	@Test
	public void assignCategoryToCommunityWrongTestId() {

		String testId = "0";
		String communityId = "2";

		boolean actual = communityService.assignCategoryToCommunity(testId, communityId);

		verify(mockCommunityDao, times(0)).assignCategoryToCommunity(anyInt(), anyInt());

		assertFalse(actual);
	}

	@Test
	public void assignCategoryToCommunityWrongCommunityId() {

		String testId = "1";
		String communityId = "0";

		boolean actual = communityService.assignCategoryToCommunity(testId, communityId);

		verify(mockCommunityDao, times(0)).assignCategoryToCommunity(anyInt(), anyInt());

		assertFalse(actual);
	}
	@Test
	public void getUniqueTestsFromCommunityList(){
		com.shpach.tutor.persistance.entities.Test test1= new com.shpach.tutor.persistance.entities.Test();
		test1.setTestId(1);
		com.shpach.tutor.persistance.entities.Test test2= new com.shpach.tutor.persistance.entities.Test();
		test2.setTestId(2);
		com.shpach.tutor.persistance.entities.Test test3= new com.shpach.tutor.persistance.entities.Test();
		test3.setTestId(3);
		
		Category category1= new Category();
		category1.setCategoryId(1);
		category1.setTests(new ArrayList<com.shpach.tutor.persistance.entities.Test>(Arrays.asList(test1,test2)));
		Category category2= new Category();
		category2.setCategoryId(2);
		category2.setTests(new ArrayList<com.shpach.tutor.persistance.entities.Test>(Arrays.asList(test3)));
		
		Community community1= new Community();
		community1.setCommunityId(1);
		community1.setCategories(new ArrayList<Category>(Arrays.asList(category1)) );
		Community community2= new Community();
		community2.setCommunityId(2);
		community2.setCategories(new ArrayList<Category>(Arrays.asList(category1, category2)) );
		
		List<com.shpach.tutor.persistance.entities.Test> actuals=communityService.getUniqueTestsFromCommunityList(Arrays.asList(community1,community2));
		Collections.sort(actuals, new Comparator<com.shpach.tutor.persistance.entities.Test>() {

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
		assertArrayEquals(Arrays.asList(test1,test2,test3).toArray(), actuals.toArray());
		
	}
}
