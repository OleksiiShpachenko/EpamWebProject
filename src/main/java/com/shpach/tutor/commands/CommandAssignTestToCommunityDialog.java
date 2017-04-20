package com.shpach.tutor.commands;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.service.SessionServise;
import com.shpach.tutor.service.TestService;
import com.shpach.tutor.service.UserService;

/**
 * Command which sets request attributes to enable to show "assign test to
 * community dialog"
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandAssignTestToCommunityDialog implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandAssignTestToCommunityDialog.class);

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

		List<Test> tests = TestService.getInstance().getTestsByUsers(user);
		//TestService.getInstance().insertCommunitiesToTests(tests);
		TestService.getInstance().insertCategoriesToTests(tests);
		request.setAttribute("tests", tests);
		request.setAttribute("assignTestToCommunityDialog", true);
		request.setAttribute("communityId", request.getParameter("communityId"));
		request.setAttribute("communityName", request.getParameter("communityName"));

		page = "/pages";
		return page;
	}

}
