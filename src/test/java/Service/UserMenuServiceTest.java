package Service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.service.CategoryService;
import com.shpach.tutor.service.CommunityService;
import com.shpach.tutor.service.QuestionService;
import com.shpach.tutor.service.TestService;
import com.shpach.tutor.service.UserMenuService;
import com.shpach.tutor.view.service.UserMenuItem;

import TestUtils.TestUtils;

public class UserMenuServiceTest {
	private UserMenuService userMenuService;
	private TestService mockTestService;
	private QuestionService mockQuestionService;
	private CommunityService mockCommunityService;
	private CategoryService mockCategoryService;

	@Before
	public void init() {
		userMenuService = UserMenuService.getInstance();
		mockTestService = Mockito.mock(TestService.class);
		mockQuestionService = Mockito.mock(QuestionService.class);
		mockCommunityService = Mockito.mock(CommunityService.class);
		mockCategoryService = Mockito.mock(CategoryService.class);
		TestUtils.getInstance().mockPrivateField(userMenuService, "testService", mockTestService);
		TestUtils.getInstance().mockPrivateField(userMenuService, "questionService", mockQuestionService);
		TestUtils.getInstance().mockPrivateField(userMenuService, "communityService", mockCommunityService);
		TestUtils.getInstance().mockPrivateField(userMenuService, "categoryService", mockCategoryService);
	}

	@Test
	public void getTutorMenuTest() {
		when(mockTestService.getTestsCountByUser(anyObject())).thenReturn(1);
		when(mockQuestionService.getQuestionsCountByUser(anyObject())).thenReturn(2);
		when(mockCommunityService.getCommunityCountByUser(anyObject())).thenReturn(3);
		when(mockCategoryService.getCategoriesCountByUser(anyObject())).thenReturn(4);
		List<UserMenuItem> userMenuItems = initUserTutorMenu();

		List<UserMenuItem> userMenuItemsExpected = userMenuService.getTutorMenu(new User());

		verify(mockTestService, times(1)).getTestsCountByUser(anyObject());
		verify(mockQuestionService, times(1)).getQuestionsCountByUser(anyObject());
		verify(mockCommunityService, times(1)).getCommunityCountByUser(anyObject());
		verify(mockCategoryService, times(1)).getCategoriesCountByUser(anyObject());

		assertArrayEquals(userMenuItems.toArray(), userMenuItemsExpected.toArray());

	}

	@Test
	public void setActiveMenuByCommandTestExistCommand() {
		String command = "tutorCategories";
		List<UserMenuItem> userMenuItems = initUserTutorMenu();
		userMenuItems.get(2).setActive(true);
		userMenuService.setActiveMenuByCommand(userMenuItems, command);
		for (UserMenuItem userMenuItem : userMenuItems) {
			if (userMenuItem.getCommand().equals(command))
				assertTrue(userMenuItem.isActive());
			else
				assertFalse(userMenuItem.isActive());
		}
	}

	@Test
	public void setActiveMenuByCommandTestNotExistCommand() {
		String command = "notExistCommand";
		List<UserMenuItem> userMenuItems = initUserTutorMenu();
		userMenuItems.get(2).setActive(true);
		userMenuService.setActiveMenuByCommand(userMenuItems, command);
		for (UserMenuItem userMenuItem : userMenuItems) {
			assertFalse(userMenuItem.isActive());
		}
	}

	@Test
	public void setActiveMenuByCommandTestNullMenu() {
		String command = "notExistCommand";
		List<UserMenuItem> userMenuItems = initUserTutorMenu();
		userMenuItems.get(2).setActive(true);
		userMenuService.setActiveMenuByCommand(null, command);
		assertTrue(true);
	}

	@Test
	public void setActiveMenuByCommandTestNullCommand() {
		List<UserMenuItem> userMenuItems = initUserTutorMenu();
		userMenuItems.get(2).setActive(true);
		userMenuService.setActiveMenuByCommand(userMenuItems, null);
		for (UserMenuItem userMenuItem : userMenuItems) {
			assertFalse(userMenuItem.isActive());
		}
	}

	@Test
	public void getStudentMenuTest() {
		when(mockTestService.getTestsCountByStudentUser(anyObject())).thenReturn(1);
		List<UserMenuItem> userMenuItems = initUserStudentMenu();

		List<UserMenuItem> userMenuItemsExpected = userMenuService.getStudentMenu(new User());

		verify(mockTestService, times(1)).getTestsCountByStudentUser(anyObject());

		assertArrayEquals(userMenuItems.toArray(), userMenuItemsExpected.toArray());

	}

	private List<UserMenuItem> initUserStudentMenu() {
		List<UserMenuItem> userMenuItems = new ArrayList<UserMenuItem>();
		UserMenuItem tests = new UserMenuItem("student.menu.tests", 1, "studentTests");
		UserMenuItem statistic = new UserMenuItem("student.menu.statistic", -1, "studentStatistic");
		userMenuItems.add(tests);
		userMenuItems.add(statistic);
		return userMenuItems;
	}

	private List<UserMenuItem> initUserTutorMenu() {
		List<UserMenuItem> userMenuItems = new ArrayList<UserMenuItem>();
		UserMenuItem tests = new UserMenuItem("tutor.menu.test", 1, "tutorTests");
		UserMenuItem questions = new UserMenuItem("tutor.menu.questions", 2, "tutorQuestions");
		UserMenuItem communities = new UserMenuItem("tutor.menu.communities", 3, "tutorCommunities");
		UserMenuItem categories = new UserMenuItem("tutor.menu.categories", 4, "tutorCategories");
		UserMenuItem statistic = new UserMenuItem("tutor.menu.statistic", -1, "tutorStatistic");

		userMenuItems.add(tests);
		userMenuItems.add(questions);
		userMenuItems.add(communities);
		userMenuItems.add(categories);
		userMenuItems.add(statistic);
		return userMenuItems;
	}

}
