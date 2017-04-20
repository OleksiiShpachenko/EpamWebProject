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
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.service.CommunityService;
import com.shpach.tutor.service.SessionServise;
import com.shpach.tutor.service.UserMenuService;
import com.shpach.tutor.service.UserService;
import com.shpach.tutor.view.service.MenuStrategy;
import com.shpach.tutor.view.service.UserMenuItem;

/**
 * Command which show Community page for Tutor
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandTutorCommunities implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandTutorCommunities.class);
	@Override
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
		UserMenuService.getInstance().setActiveMenuByCommand(tutorMenu, "tutorCommunities");
		request.setAttribute("menu", tutorMenu);

		List<Community> communities = CommunityService.getInstance().getCommunitiesByUserWithCategoriesAndUsers(user);
		request.setAttribute("communities", communities);
		page = Config.getInstance().getProperty(Config.TUTOR_COMMUNITIES);
		return page;
	}

}
