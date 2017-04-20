package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.user.IUserDao;
import com.shpach.tutor.persistance.jdbc.dao.user.MySqlUserDao;

public class UserDaoTest extends DaoTest {
	private User user_1, user_2;
	private User userNew = null;
	private IUserDao userDao;
	private MySqlUserDao spyUserDao;

	
	@Before
	public void init() throws SQLException {
		super.init();
		IDaoFactory daoFactory = new MySqlDaoFactory();
		userDao = daoFactory.getUserDao();
		spyUserDao = (MySqlUserDao) spy(userDao);

		user_1 = new User();
		user_1.setUserId(1);
		user_1.setUserName("Lesha");
		user_1.setUserEmail("q@gmail.com");
		user_1.setUserPassword("password");
		user_1.setRoleId(2);

		user_2 = new User();
		user_2.setUserId(2);
		user_2.setUserName("Sasha");
		user_2.setUserEmail("q2@gmail.com");
		user_2.setUserPassword("password");
		user_2.setRoleId(2);

		userNew = new User();
		userNew.setUserName("Lesha");
		userNew.setUserEmail("q@gmail.com");
		userNew.setUserPassword("password");
		userNew.setRoleId(2);

	}

	@Test
	public void findUserByEmailTestExistEmail() throws SQLException, NamingException {
		doReturn(new ArrayList<User>(Arrays.asList(user_1))).when(spyUserDao).findByDynamicSelect(anyString(),
				anyString(), anyObject());

		User actual = spyUserDao.findUserByEmail(user_1.getUserEmail());

		verify(spyUserDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(user_1, actual);
	}

	@Test
	public void findUserByEmailTestNullEmail() throws SQLException, NamingException {
		User actual = spyUserDao.findUserByEmail(null);

		verify(spyUserDao, times(0)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void findUserByEmailTestNotExistEmail() throws SQLException, NamingException {
		doReturn(new ArrayList<User>()).when(spyUserDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		User actual = spyUserDao.findUserByEmail("notExistEmail");

		verify(spyUserDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void findUserByIdTestExistId() throws SQLException, NamingException {
		doReturn(new ArrayList<User>(Arrays.asList(user_1))).when(spyUserDao).findByDynamicSelect(anyString(),
				anyString(), anyObject());

		User actual = spyUserDao.findUserById(user_1.getUserId());

		verify(spyUserDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(user_1, actual);
	}

	@Test
	public void findUserByIdTestNotExistId() throws SQLException, NamingException {
		doReturn(new ArrayList<User>()).when(spyUserDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		User actual = spyUserDao.findUserById(0);

		verify(spyUserDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateTestNewUser() throws SQLException, NamingException {

		doReturn(user_1.getUserId()).when(spyUserDao).dynamicAdd(anyString(), anyObject());

		User actual = spyUserDao.addOrUpdate(userNew);

		verify(spyUserDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertEquals(user_1, actual);
	}

	@Test
	public void addOrApdateTestNewUserFail() throws SQLException, NamingException {

		doReturn(0).when(spyUserDao).dynamicAdd(anyString(), anyObject());

		User actual = spyUserDao.addOrUpdate(userNew);

		verify(spyUserDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateTestUpdateUser() throws SQLException, NamingException {
		doReturn(true).when(spyUserDao).dynamicUpdate(anyString(), anyObject());

		User actual = spyUserDao.addOrUpdate(user_1);

		verify(spyUserDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertEquals(user_1, actual);
	}

	@Test
	public void addOrApdateTestUpdateUserFail() throws SQLException, NamingException {
		doReturn(false).when(spyUserDao).dynamicUpdate(anyString(), anyObject());

		User actual = spyUserDao.addOrUpdate(user_1);

		verify(spyUserDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void findUsersByCommunityIdTest() throws SQLException, NamingException {
		List<User> expecteds = new ArrayList<User>(Arrays.asList(user_1, user_2));

		doReturn(expecteds).when(spyUserDao).findByDynamicSelect(anyString(), anyObject());

		List<User> actuals = spyUserDao.findUsersByCommunityId(1);

		verify(spyUserDao, times(1)).findByDynamicSelect(anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findUsersByCommunityIdTestEmpty() throws SQLException, NamingException {
		doReturn(new ArrayList<User>()).when(spyUserDao).findByDynamicSelect(anyString(), anyObject());

		List<User> actuals = spyUserDao.findUsersByCommunityId(1);

		verify(spyUserDao, times(1)).findByDynamicSelect(anyString(), anyObject());

		assertEquals(0, actuals.size());
	}

	@Test
	public void findAllTest() throws SQLException, NamingException {
		List<User> expecteds = new ArrayList<>(Arrays.asList(new User(), new User()));

		doReturn(expecteds).when(spyUserDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<User> actuals = spyUserDao.findAll();

		verify(spyUserDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}
}
