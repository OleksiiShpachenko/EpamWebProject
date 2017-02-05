package com.shpach.tutor.commands;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.manager.Message;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.entities.UsersRole;
import com.shpach.tutor.service.LoginService;
import com.shpach.tutor.service.SessionServise;
import com.shpach.tutor.service.UserRoleService;
import com.shpach.tutor.service.UserService;

/**
 * Command which makes log in user to the system.
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandLogin implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandLogin.class);
	private static final String LOGIN = "login";
	private static final String PASSWORD = "password";

	public String execute(HttpServletRequest request, HttpServletResponse responce)
			throws ServletException, IOException {
		String page = null;
		boolean checkSession = false;

		HttpSession session = request.getSession(false);

		if (session != null)
			checkSession = SessionServise.checkSession(session.getId(), (String) session.getAttribute("user"));
		if (!checkSession) {
			String login = request.getParameter(LOGIN);
			String password = request.getParameter(PASSWORD);
			boolean checkLogin = LoginService.checkLogin(login, password);
			if (!checkLogin) {
				session.invalidate();
				request.setAttribute("errorLoginPassMessage", Message.getInstance().getProperty(Message.LOGIN_ERROR));
				return page = Config.getInstance().getProperty(Config.LOGIN);
			}
			request.getSession().setAttribute("user", login);
			logger.info("the user " + login + " succefully logged in");

		}
		User user = UserService.getUserByLogin((String) session.getAttribute("user"));
		request.getSession().setAttribute("userEntity", user);
		request.getSession().setAttribute("userName", user.getUserName());
		UsersRole usersRole = UserRoleService.getUserRoleById(user.getRoleId());
		request.getSession().setAttribute("usersRole", usersRole);

		Map<String, String[]> lastRequest = new HashMap<String, String[]>();
		lastRequest.putAll(request.getParameterMap());

		request.getSession().setAttribute("lastRequest", lastRequest);
		page = "/pages";
		return page;
	}

}
