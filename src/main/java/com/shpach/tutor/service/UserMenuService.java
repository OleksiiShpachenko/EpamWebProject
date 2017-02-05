package com.shpach.tutor.service;

import java.util.ArrayList;
import java.util.List;

import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.view.service.UserMenuItem;

/**
 * Collection of services for {@link UserMenuItem} class
 * 
 * @author Shpachenko_A_K
 *
 */
public class UserMenuService {
	private static final int NO_COUNT_IN_MENU = -1;

	/**
	 * Get collection of {@link UserMenuItem} for Tutor by {@link User}
	 * 
	 * @param user
	 *            - {@link User}
	 * @return collection of {@link UserMenuItem}
	 */
	public static List<UserMenuItem> getTutorMenu(User user) {
		List<UserMenuItem> res = new ArrayList<UserMenuItem>();
		UserMenuItem tests = new UserMenuItem("tutor.menu.test", TestService.getTestsCountByUser(user), "tutorTests");
		UserMenuItem questions = new UserMenuItem("tutor.menu.questions", QuestionService.getQuestionsCountByUser(user),
				"tutorQuestions");
		UserMenuItem communities = new UserMenuItem("tutor.menu.communities", CommunityService.getCommunityCountByUser(user),
				"tutorCommunities");
		UserMenuItem categories = new UserMenuItem("tutor.menu.categories", CategoryService.getCategoriesCountByUser(user),
				"tutorCategories");
		UserMenuItem statistic = new UserMenuItem("tutor.menu.statistic", NO_COUNT_IN_MENU, "tutorStatistic");

		res.add(tests);
		res.add(questions);
		res.add(communities);
		res.add(categories);
		res.add(statistic);
		return res;
	}

	/**
	 * Set {@link UserMenuItem} to active mode from collection by Command parameter
	 * 
	 * @param tutorMenu
	 *            collection of {@link UserMenuItem}
	 * @param command
	 *            - command parameter
	 */
	public static void setActiveMenuByCommand(List<UserMenuItem> tutorMenu, String command) {
		for (UserMenuItem item : tutorMenu) {
			if (item.getCommand().equals(command))
				item.setActive(true);
		}
	}

	/**
	 * Get collection of {@link UserMenuItem} for Student by {@link User}
	 * 
	 * @param user
	 *            - {@link User}
	 * @return collection of {@link UserMenuItem}
	 */
	public static List<UserMenuItem> getStudentMenu(User user) {

		List<UserMenuItem> res = new ArrayList<UserMenuItem>();
		UserMenuItem tests = new UserMenuItem("student.menu.tests", TestService.getTestsCountByStudentUser(user),
				"studentTests");
		UserMenuItem statistic = new UserMenuItem("student.menu.statistic", NO_COUNT_IN_MENU, "studentStatistic");

		res.add(tests);
		res.add(statistic);

		return res;
	}

}
