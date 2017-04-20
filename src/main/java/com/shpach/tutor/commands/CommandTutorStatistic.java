package com.shpach.tutor.commands;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.service.CommunityService;
import com.shpach.tutor.service.SessionServise;
import com.shpach.tutor.service.TaskService;
import com.shpach.tutor.service.UserMenuService;
import com.shpach.tutor.service.UserService;
import com.shpach.tutor.view.service.MenuStrategy;
import com.shpach.tutor.view.service.UserMenuItem;

/**
 * Command which show Statistic page for Tutor
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandTutorStatistic implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandTutorStatistic.class);

	public String execute(HttpServletRequest request, HttpServletResponse responce)
			throws ServletException, IOException {
		String page = null;
		boolean checkSession = false;

		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.warn("try to access without session");
			return page = Config.getInstance().getProperty(Config.LOGIN);
		}
		checkSession = SessionServise.getInstance().checkSession(session.getId(), (String) session.getAttribute("user"));
		if (!checkSession) {
			session.invalidate();
			logger.warn("invalid session");
			return page = Config.getInstance().getProperty(Config.LOGIN);
		}
		User user = UserService.getInstance().getUserByLogin((String) session.getAttribute("user"));
		Map<String, String[]> lastRequest = new HashMap<String, String[]>();
		lastRequest.putAll(request.getParameterMap());
		request.getSession().setAttribute("lastRequest", lastRequest);

		List<UserMenuItem> tutorMenu = new MenuStrategy(user).getMenu();
		UserMenuService.getInstance().setActiveMenuByCommand(tutorMenu, "tutorStatistic");
		request.setAttribute("menu", tutorMenu);

		List<Community> communities = CommunityService.getInstance().getCommunitiesByUserWithCategoriesAndUsers(user);
		if (communities != null) {
			communities.forEach(c ->  c.getCategories().forEach(j-> TaskService.getInstance().insertTasksToTestsListByUser(j.getTests(), user)));
		}

		User bestWorstUser=UserService.getInstance().findUserWithGreatWorstStatistic();
		Test hardestTest=TaskService.getInstance().getHardestTestByTasksScore();
		
		request.setAttribute("bestWorstUser", bestWorstUser);
		request.setAttribute("hardestTest", hardestTest);
		
		request.setAttribute("communities", communities);

		page = Config.getInstance().getProperty(Config.TUTOR_STATISTIC);

		return page;

	}

}
