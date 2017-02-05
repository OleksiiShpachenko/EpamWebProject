package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.shpach.tutor.persistance.entities.UsersRole;
import com.shpach.tutor.persistance.jdbc.connection.ConnectionPool;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.userrole.IUserRoleDao;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(ConnectionPool.class)
public class UserRoleDaoTest {
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
	public void findUserRoleByIdTestExistingId() throws SQLException, NamingException {
		int roleId = 1;
		String roleDescription = "Description";
		String roleName = "Tutor";

		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(roleId);
		when(mockResultSet.getString(anyInt())).thenReturn(roleName, roleDescription);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		UsersRole userRole = new UsersRole();
		userRole.setRoleId(roleId);
		userRole.setRoleName(roleName);
		userRole.setRoleDescription(roleDescription);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserRoleDao userRoleDao = daoFactory.getUserRoleDao();
		UsersRole usersRoleExpected = userRoleDao.findUsersRoleById(roleId);

		assertEquals(usersRoleExpected, userRole);
	}

	@Test
	public void findUserRoleByIdTestNoExistingId() throws SQLException, NamingException {

		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(Boolean.FALSE);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserRoleDao userRoleDao = daoFactory.getUserRoleDao();
		UsersRole usersRoleExpected = userRoleDao.findUsersRoleById(1);

		assertNull(usersRoleExpected);
	}

	@Test
	public void addOrApdateTestNewUserRole() throws SQLException, NamingException {

		int roleId = 1;
		String roleDescription = "Description";
		String roleName = "Tutor";

		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(roleId);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		UsersRole userRole = new UsersRole();
		userRole.setRoleName(roleName);
		userRole.setRoleDescription(roleDescription);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserRoleDao userRoleDao = daoFactory.getUserRoleDao();
		UsersRole usersRoleExpected = userRoleDao.findUsersRoleById(roleId);

		assertEquals(usersRoleExpected.getRoleId(), roleId);
	}

	@Test
	public void addOrApdateTestUpdateUserRole() throws SQLException, NamingException {

		int roleId = 1;
		String roleDescription = "Description";
		String roleName = "Tutor";

		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(roleId);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		UsersRole userRole = new UsersRole();
		userRole.setRoleId(roleId);
		userRole.setRoleName(roleName);
		userRole.setRoleDescription(roleDescription);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		IUserRoleDao userRoleDao = daoFactory.getUserRoleDao();
		UsersRole usersRoleExpected = userRoleDao.findUsersRoleById(roleId);

		assertNotNull(usersRoleExpected);
	}
}
