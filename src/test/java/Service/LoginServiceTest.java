package Service;

import org.junit.*;
import org.mockito.Mockito;

import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.user.IUserDao;
import com.shpach.tutor.service.LoginService;
import TestUtils.TestUtils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LoginServiceTest {
	private LoginService loginService;
	private IUserDao mockUserDao;

	@Before
	public void intn() {
		mockUserDao = Mockito.mock(IUserDao.class);
		loginService = LoginService.getInstance();
		TestUtils.getInstance().mockPrivateField(loginService, "userDao", mockUserDao);
	}

	@Test
	public void checkLoginOk() {
		String email = "email";
		String password = "password";
		User user = new User();
		user.setUserEmail(email);
		user.setUserPassword(password);
		when(mockUserDao.findUserByEmail(email)).thenReturn(user);

		boolean actual = loginService.checkLogin(email, password);

		verify(mockUserDao, times(1)).findUserByEmail(email);

		assertTrue(actual);
	}

	@Test
	public void checkLoginWrongEmail() {
		String email = "email";
		String password = "password";
		User user = new User();
		user.setUserEmail(email);
		user.setUserPassword(password);
		when(mockUserDao.findUserByEmail(email)).thenReturn(null);

		boolean actual = loginService.checkLogin(email, password);

		verify(mockUserDao, times(1)).findUserByEmail(email);

		assertFalse(actual);
	}

	@Test
	public void checkLoginWrongPass() {
		String email = "email";
		String password = "password";
		User user = new User();
		user.setUserEmail(email);
		user.setUserPassword(password);
		when(mockUserDao.findUserByEmail(email)).thenReturn(user);

		boolean actual = loginService.checkLogin(email, "wrongPassword");

		verify(mockUserDao, times(1)).findUserByEmail(email);

		assertFalse(actual);
	}

	@Test
	public void getStartCommandAccordingToUserRoleTutor() {
		User user = new User();
		user.setRoleId(1);
		String expected = "tutorTests";
		String actual = loginService.getStartCommandAccordingToUserRole(user);

		assertEquals(expected, actual);
	}

	@Test
	public void getStartCommandAccordingToUserRoleStudent() {
		User user = new User();
		user.setRoleId(2);
		String expected = "studentTests";
		String actual = loginService.getStartCommandAccordingToUserRole(user);

		assertEquals(expected, actual);
	}

	@Test
	public void getStartCommandAccordingToUserRoleDefault() {
		User user = new User();
		user.setRoleId(3);
		String expected = "studentTests";
		String actual = loginService.getStartCommandAccordingToUserRole(user);

		assertEquals(expected, actual);
	}
}
