package com.shpach.tutor.commands;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.service.UserService;

/**
 * Command which register new user in the system
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandRegistration implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandRegistration.class);

	public String execute(HttpServletRequest request, HttpServletResponse responce)
			throws ServletException, IOException {
		String page = Config.getInstance().getProperty(Config.REGISTRATION);

		String userName = request.getParameter("userName");
		if (userName != null) {
			String userEmail = request.getParameter("userEmail");
			String userPassword = request.getParameter("password");
			String userPasswordConfirm = request.getParameter("confirm_password");
			String userRole = request.getParameter("userRole");
			request.setAttribute("userName", userName);
			request.setAttribute("userEmail", userEmail);
			int userRoleInt = 0;
			boolean validation = true;
			try {
				userRoleInt = Integer.parseInt(userRole);
			} catch (NumberFormatException ex) {
				validation = false;
			}
			if (userName.equals("")) {
				request.setAttribute("fillUserNameMessage", true);
				validation = false;
			}
			if (!userEmail.contains("@")) {
				request.setAttribute("fillCorrectEmailMessage", true);
				validation = false;
			}
			if (userPassword.equals("") || !userPassword.equals(userPasswordConfirm)) {
				request.setAttribute("fillCorrectPasswordMessage", true);
				validation = false;
			}
			if (validation) {
				User user = new User();
				user.setUserEmail(userEmail);
				user.setUserName(userName);
				user.setUserPassword(userPassword);
				user.setRoleId(userRoleInt);
				if (UserService.addNewUser(user)) {
					logger.info("new user succefull added. User email:" + userEmail);
					request.setAttribute("registrationSuccess", true);
					page = Config.getInstance().getProperty(Config.LOGIN);
				} else {
					request.setAttribute("emailAllReadyExistMessage", true);
				}
			}
		}
		return page;
	}

}
