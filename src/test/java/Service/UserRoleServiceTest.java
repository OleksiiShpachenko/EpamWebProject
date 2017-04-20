package Service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.shpach.tutor.persistance.entities.UsersRole;
import com.shpach.tutor.persistance.jdbc.dao.userrole.IUserRoleDao;
import com.shpach.tutor.service.UserRoleService;

import TestUtils.TestUtils;

public class UserRoleServiceTest {
	private IUserRoleDao mockUserRoleDao;
	private UserRoleService userRoleService;

	@Before
	public void init() {
		mockUserRoleDao = Mockito.mock(IUserRoleDao.class);
		userRoleService = UserRoleService.getInstance();
		TestUtils.getInstance().mockPrivateField(userRoleService, "userRoleDao", mockUserRoleDao);
	}

	@Test
	public void getUserRoleByIdTestExistUserRole() {
		when(mockUserRoleDao.findUsersRoleById(anyInt())).thenReturn(new UsersRole());
		UsersRole userRole = userRoleService.getUserRoleById(1);
		verify(mockUserRoleDao, times(1)).findUsersRoleById(anyInt());
		assertNotNull(userRole);
	}
	@Test
	public void getUserRoleByIdTestNoExistUserRole() {
		when(mockUserRoleDao.findUsersRoleById(anyInt())).thenReturn(null);
		UsersRole userRole = userRoleService.getUserRoleById(1);
		verify(mockUserRoleDao, times(1)).findUsersRoleById(anyInt());
		assertNull(userRole);
	}

}
