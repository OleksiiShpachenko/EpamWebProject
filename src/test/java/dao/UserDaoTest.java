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

import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.connection.ConnectionPool;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.user.IUserDao;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(ConnectionPool.class)
public class UserDaoTest {
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
	public void findUserByEmailTestExistEmail() throws SQLException, NamingException {
		int userId = 1;
		int userRoleId = 2;
		String userName = "Lesha";
		String userEmail = "q@gmail.com";
		String userPassword = "password";
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(userId, userRoleId);
		when(mockResultSet.getString(anyInt())).thenReturn(userEmail, userPassword, userName);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		User user = new User();
		user.setUserId(userId);
		user.setUserName(userName);
		user.setUserEmail(userEmail);
		user.setUserPassword(userPassword);
		user.setRoleId(userRoleId);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserDao userDao = daoFactory.getUserDao();
		User userExpected = userDao.findUserByEmail("q@gmail.com");

		assertEquals(user, userExpected);
	}
	@Test
	public void findUserByEmailTestNotExistEmail() throws SQLException, NamingException {
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(Boolean.FALSE);
		

		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserDao userDao = daoFactory.getUserDao();
		User userExpected = userDao.findUserByEmail("q@gmail.com");

		assertNull(userExpected);
	}
	@Test
	public void findUserByIdTestExistEmail() throws SQLException, NamingException {
		int userId = 1;
		int userRoleId = 2;
		String userName = "Lesha";
		String userEmail = "q@gmail.com";
		String userPassword = "password";
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(userId, userRoleId);
		when(mockResultSet.getString(anyInt())).thenReturn(userEmail, userPassword, userName);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		User user = new User();
		user.setUserId(userId);
		user.setUserName(userName);
		user.setUserEmail(userEmail);
		user.setUserPassword(userPassword);
		user.setRoleId(userRoleId);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserDao userDao = daoFactory.getUserDao();
		User userExpected = userDao.findUserById(userId);

		assertEquals(user, userExpected);
	}
	@Test
	public void findUserByIdTestNotExistEmail() throws SQLException, NamingException {
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(Boolean.FALSE);
		

		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserDao userDao = daoFactory.getUserDao();
		User userExpected = userDao.findUserById(1);

		assertNull(userExpected);
	}
	@Test
	public void addOrApdateTestNewUser() throws SQLException, NamingException {
		
		int userId = 1;
		int userRoleId = 2;
		String userName = "Lesha";
		String userEmail = "q@gmail.com";
		String userPassword = "password";
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(userId);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		User user = new User();
		user.setUserName(userName);
		user.setUserEmail(userEmail);
		user.setUserPassword(userPassword);
		user.setRoleId(userRoleId);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserDao userDao = daoFactory.getUserDao();
		User userExpected = userDao.addOrUpdate(user);

		assertEquals(userExpected.getUserId(), userId);
	}
	@Test
	public void addOrApdateTestUpdateUser() throws SQLException, NamingException {
		
		int userId = 1;
		int userRoleId = 2;
		String userName = "Lesha";
		String userEmail = "q@gmail.com";
		String userPassword = "password";
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);

		User user = new User();
		user.setUserId(userId);
		user.setUserName(userName);
		user.setUserEmail(userEmail);
		user.setUserPassword(userPassword);
		user.setRoleId(userRoleId);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserDao userDao = daoFactory.getUserDao();
		User userExpected = userDao.addOrUpdate(user);

		assertNotNull(userExpected);
	}

	@Test
	public void findUsersByCommunityIdTest() throws SQLException, NamingException {
		
		int userId = 1;
		int userRoleId = 2;
		String userName = "Lesha";
		String userEmail = "q@gmail.com";
		String userPassword = "password";
		int userId_2 = 2;
		int userRoleId_2 = 2;
		String userName_2 = "Sasha";
		String userEmail_2 = "qq@gmail.com";
		String userPassword_2 = "password2";
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(userId, userRoleId,userId_2, userRoleId_2);
		when(mockResultSet.getString(anyInt())).thenReturn(userEmail, userPassword, userName,userEmail_2, userPassword_2, userName_2);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE,Boolean.TRUE, Boolean.FALSE);

		User user1 = new User();
		user1.setUserId(userId);
		user1.setUserName(userName);
		user1.setUserEmail(userEmail);
		user1.setUserPassword(userPassword);
		user1.setRoleId(userRoleId);

		User user2 = new User();
		user2.setUserId(userId_2);
		user2.setUserName(userName_2);
		user2.setUserEmail(userEmail_2);
		user2.setUserPassword(userPassword_2);
		user2.setRoleId(userRoleId_2);
		
		List<User> users= new ArrayList<>();
		users.add(user1);
		users.add(user2);
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserDao userDao = daoFactory.getUserDao();
		List<User> usersExpected = userDao.findUsersByCommunityId(1);

		assertArrayEquals(users.toArray(), usersExpected.toArray());
	}
}
