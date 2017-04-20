package Service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.user.IUserDao;
import com.shpach.tutor.service.TaskService;
import com.shpach.tutor.service.UserService;

import TestUtils.TestUtils;

public class UserServiceTest {
	private IUserDao mockUserDao;
	private UserService userService;

	@Before
	public void init() {
		mockUserDao = Mockito.mock(IUserDao.class);
		userService = UserService.getInstance();
		TestUtils.getInstance().mockPrivateField(userService, "userDao", mockUserDao);
	}

	@Test
	public void getUserByLoginTestExistUser() {
		when(mockUserDao.findUserByEmail(anyString())).thenReturn(new User());
		User user = userService.getUserByLogin("userLogin");
		verify(mockUserDao, times(1)).findUserByEmail(anyString());
		assertNotNull(user);
	}

	@Test
	public void getUserByLoginTestNoExistUser() {
		when(mockUserDao.findUserByEmail(anyString())).thenReturn(null);
		User user = userService.getUserByLogin("userLogin");
		verify(mockUserDao, times(1)).findUserByEmail(anyString());
		assertNull(user);
	}
	@Test
	public void getUserByLoginTestNullLogin() {
		when(mockUserDao.findUserByEmail(anyString())).thenReturn(null);
		User user = userService.getUserByLogin(null);
		verify(mockUserDao, times(0)).findUserByEmail(anyString());
		assertNull(user);
	}

	@Test
	public void getUsersByCommunityTestExistCommunityExistUsers() {
		when(mockUserDao.findUsersByCommunityId(anyInt())).thenReturn(new ArrayList<User>());
		List<User> users = userService.getUsersByCommunity(new Community());
		verify(mockUserDao, times(1)).findUsersByCommunityId(anyInt());
		assertNotNull(users);
	}

	@Test
	public void getUsersByCommunityTestExistCommunityNoExistUsers() {
		when(mockUserDao.findUsersByCommunityId(anyInt())).thenReturn(null);
		List<User> user = userService.getUsersByCommunity(new Community());
		verify(mockUserDao, times(1)).findUsersByCommunityId(anyInt());
		assertNull(user);
	}

	@Test
	public void getUsersByCommunityTestNullCommunity() {
		List<User> user = userService.getUsersByCommunity(null);
		verify(mockUserDao, times(0)).findUsersByCommunityId(anyInt());
		assertNull(user);
	}
	@Test
	public void addNewUserSuccess() {
		when(mockUserDao.addOrUpdate(anyObject())).thenReturn(new User());
		boolean res = userService.addNewUser(new User());
		verify(mockUserDao, times(1)).addOrUpdate(anyObject());
		assertTrue(res);
	}
	@Test
	public void addNewUserFail() {
		when(mockUserDao.addOrUpdate(anyObject())).thenReturn(null);
		boolean res = userService.addNewUser(new User());
		verify(mockUserDao, times(1)).addOrUpdate(anyObject());
		assertFalse(res);
	}
	@Test
	public void addNewUserNull() {
		boolean res = userService.addNewUser(null);
		verify(mockUserDao, times(0)).addOrUpdate(anyObject());
		assertFalse(res);
	}
	@Test
	public void findUserWithGreatWorstStatisticTest(){
		User user_1=new User();
		user_1.setUserId(1);
		User user_2=new User();
		user_2.setUserId(2);
		User user_3=new User();
		user_3.setUserId(3);
		User user_4=new User();
		user_4.setUserId(4);
				
		when(mockUserDao.findAll()).thenReturn(new ArrayList<User>(Arrays.asList(user_1, user_2, user_3, user_4)));
		TaskService taskService=Mockito.mock(TaskService.class);
		TestUtils.getInstance().mockPrivateField(userService,"taskService", taskService);
		when(taskService.getTasksByUser(anyObject())).thenReturn(null);
		when(taskService.getMinScore(null)).thenReturn(5,10,45,40);
		User user=userService.findUserWithGreatWorstStatistic();
		
		verify(mockUserDao, times(1)).findAll();
		verify(taskService, times(4)).getTasksByUser(anyObject());
		verify(taskService, times(4)).getMinScore(anyObject());
		assertEquals(user, user_3);
	}
	@Test
	public void findUserWithGreatWorstStatisticTestNullUser(){
			
		when(mockUserDao.findAll()).thenReturn(null);
		TaskService taskService=Mockito.mock(TaskService.class);
		TestUtils.getInstance().mockPrivateField(userService,"taskService", taskService);
		
		User user=userService.findUserWithGreatWorstStatistic();
		
		verify(mockUserDao, times(1)).findAll();
		verify(taskService, times(0)).getTasksByUser(anyObject());
		verify(taskService, times(0)).getMinScore(anyObject());
		assertNull(user);
	}
	@Test
	public void findUserWithGreatWorstStatisticTestNullTests(){
		User user_1=new User();
		user_1.setUserId(1);
		User user_2=new User();
		user_2.setUserId(2);
		User user_3=new User();
		user_3.setUserId(3);
		User user_4=new User();
		user_4.setUserId(4);
				
		when(mockUserDao.findAll()).thenReturn(new ArrayList<User>(Arrays.asList(user_1, user_2, user_3, user_4)));
		TaskService taskService=Mockito.mock(TaskService.class);
		TestUtils.getInstance().mockPrivateField(userService,"taskService", taskService);
		when(taskService.getTasksByUser(anyObject())).thenReturn(null);
		when(taskService.getMinScore(null)).thenReturn(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);
		User user=userService.findUserWithGreatWorstStatistic();
		
		verify(mockUserDao, times(1)).findAll();
		verify(taskService, times(4)).getTasksByUser(anyObject());
		verify(taskService, times(4)).getMinScore(anyObject());
		assertNull(user);
	}

	
}
