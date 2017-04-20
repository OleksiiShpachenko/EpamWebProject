package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import com.shpach.tutor.persistance.entities.UsersRole;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.userrole.IUserRoleDao;
import com.shpach.tutor.persistance.jdbc.dao.userrole.MySqlUserRoleDao;


public class UserRoleDaoTest extends DaoTest {
	private IUserRoleDao userRoleDao;
	private MySqlUserRoleDao spyUserRoleDao;

	@Before
	public void init() throws SQLException {
		super.init();
		IDaoFactory daoFactory = new MySqlDaoFactory();
		userRoleDao = daoFactory.getUserRoleDao();
		spyUserRoleDao =  (MySqlUserRoleDao)spy(userRoleDao);
	}

	@Test
	public void findAllTest() throws Exception{
		List<UsersRole> expecteds=new ArrayList<>(Arrays.asList(new UsersRole(), new UsersRole())) ;
		
		doReturn(expecteds).when(spyUserRoleDao).findByDynamicSelect(anyString(), anyString(),anyObject());
		
		List<UsersRole> actuals=spyUserRoleDao.findAll();
		
		verify(spyUserRoleDao, times(1)).findByDynamicSelect(anyString(), anyString(),anyObject());
	
		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}
	
	@Test
	public void findUserRoleByIdTestExistingId() throws SQLException, NamingException {
		List<UsersRole> expecteds=new ArrayList<>();
		int roleId = 1;
		String roleDescription = "Description";
		String roleName = "Tutor";

		UsersRole userRoleExpected = new UsersRole();
		userRoleExpected.setRoleId(roleId);
		userRoleExpected.setRoleName(roleName);
		userRoleExpected.setRoleDescription(roleDescription);
		expecteds.add(userRoleExpected);
		
		doReturn(expecteds).when(spyUserRoleDao).findByDynamicSelect(anyString(), anyString(),anyObject());

		UsersRole usersRoleActual = spyUserRoleDao.findUsersRoleById(roleId);
		
		verify(spyUserRoleDao, times(1)).findByDynamicSelect(anyString(), anyString(),anyObject());

		assertEquals(userRoleExpected,usersRoleActual);
	}

	@Test
	public void findUserRoleByIdTestNoExistingId() throws SQLException, NamingException {
		doReturn(new ArrayList<UsersRole>() ).when(spyUserRoleDao).findByDynamicSelect(anyString(), anyString(),anyObject());

		UsersRole usersRoleActual = spyUserRoleDao.findUsersRoleById(1);

		verify(spyUserRoleDao, times(1)).findByDynamicSelect(anyString(), anyString(),anyObject());
		
		assertNull(usersRoleActual);
	}

	@Test
	public void addOrApdateTestNewUserRole() throws SQLException, NamingException {

		int roleId = 1;
		String roleDescription = "Description";
		String roleName = "Tutor";

		UsersRole usersRole = new UsersRole();
		usersRole.setRoleName(roleName);
		usersRole.setRoleDescription(roleDescription);
		
		UsersRole userRoleExpected = new UsersRole();
		userRoleExpected.setRoleId(roleId);
		userRoleExpected.setRoleName(roleName);
		userRoleExpected.setRoleDescription(roleDescription);
		
		doReturn(roleId).when(spyUserRoleDao).dynamicAdd(anyString(), anyObject());

		UsersRole usersRoleActual = spyUserRoleDao.addOrUpdate(usersRole);
		
		verify(spyUserRoleDao, times(1)).dynamicAdd(anyString(), anyObject());
		
		assertEquals(userRoleExpected, usersRoleActual);
	}
	@Test
	public void addOrApdateTestNewUserRoleFail() throws SQLException, NamingException {

		int roleId = 1;
		String roleDescription = "Description";
		String roleName = "Tutor";

		UsersRole usersRole = new UsersRole();
		usersRole.setRoleName(roleName);
		usersRole.setRoleDescription(roleDescription);
		
		UsersRole userRoleExpected = new UsersRole();
		userRoleExpected.setRoleId(roleId);
		userRoleExpected.setRoleName(roleName);
		userRoleExpected.setRoleDescription(roleDescription);
		
		doReturn(0).when(spyUserRoleDao).dynamicAdd(anyString(), anyObject());

		UsersRole usersRoleActual = spyUserRoleDao.addOrUpdate(usersRole);
		
		verify(spyUserRoleDao, times(1)).dynamicAdd(anyString(), anyObject());
		
		assertNull(usersRoleActual);
	}

	@Test
	public void addOrApdateTestUpdateUserRole() throws SQLException, NamingException {

		int roleId = 1;
		String roleDescription = "Description";
		String roleName = "Tutor";

		UsersRole userRoleExpected = new UsersRole();
		userRoleExpected.setRoleId(roleId);
		userRoleExpected.setRoleName(roleName);
		userRoleExpected.setRoleDescription(roleDescription);
		
		doReturn(true).when(spyUserRoleDao).dynamicUpdate(anyString(), anyObject());

		UsersRole usersRoleActual = spyUserRoleDao.addOrUpdate(userRoleExpected);
		
		verify(spyUserRoleDao, times(1)).dynamicUpdate(anyString(), anyObject());
		
		assertEquals(userRoleExpected, usersRoleActual);
	}
	@Test
	public void addOrApdateTestUpdateUserRoleFail() throws SQLException, NamingException {

		int roleId = 1;
		String roleDescription = "Description";
		String roleName = "Tutor";

		UsersRole userRoleExpected = new UsersRole();
		userRoleExpected.setRoleId(roleId);
		userRoleExpected.setRoleName(roleName);
		userRoleExpected.setRoleDescription(roleDescription);
		
		doReturn(false).when(spyUserRoleDao).dynamicUpdate(anyString(), anyObject());

		UsersRole usersRoleActual = spyUserRoleDao.addOrUpdate(userRoleExpected);
		
		verify(spyUserRoleDao, times(1)).dynamicUpdate(anyString(), anyObject());
		
		assertNull(usersRoleActual);
	}
	@Test
	public void addOrApdateTestNull() throws SQLException, NamingException {

		UsersRole usersRoleActual = spyUserRoleDao.addOrUpdate(null);
		
		verify(spyUserRoleDao, times(0)).dynamicUpdate(anyString(), anyObject());
		
		assertNull(usersRoleActual);
	}
	@Test
	public void populateDtoTest() throws SQLException{
		int roleId = 1;
		String roleDescription = "Description";
		String roleName = "Tutor";
		
		UsersRole userRoleExpected = new UsersRole();
		userRoleExpected.setRoleId(roleId);
		userRoleExpected.setRoleName(roleName);
		userRoleExpected.setRoleDescription(roleDescription);
		
		when(mockResultSet.getInt(anyInt())).thenReturn(roleId);
		when(mockResultSet.getString(anyInt())).thenReturn(roleName,roleDescription);
		
		UsersRole actual= spyUserRoleDao.populateDto(mockResultSet);
		
		verify(mockResultSet,times(1)).getInt(anyInt());
		verify(mockResultSet,times(2)).getString(anyInt());
		
		assertEquals(userRoleExpected, actual);
	}
	@SuppressWarnings("unused")
	@Test(expected = SQLException.class)
	public void populateDtoTestException() throws SQLException{
	
		when(mockResultSet.getInt(anyInt())).thenThrow(new SQLException());
		
		UsersRole actual= spyUserRoleDao.populateDto(mockResultSet);
		
		verify(mockResultSet,times(1)).getInt(anyInt());
		
	}
}
