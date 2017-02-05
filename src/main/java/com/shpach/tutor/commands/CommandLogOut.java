package com.shpach.tutor.commands;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shpach.tutor.manager.Config;

/**
 * Command which log out user from the system
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandLogOut implements ICommand {

	public String execute(HttpServletRequest request, HttpServletResponse responce)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		session.invalidate();
		return Config.getInstance().getProperty(Config.LOGIN);

	}

}
